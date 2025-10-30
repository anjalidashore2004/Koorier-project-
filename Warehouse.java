package com.warehouse;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Warehouse {
    // Use ConcurrentHashMap for thread-safe reads/writes
    private final Map<Integer, Product> inventory = new ConcurrentHashMap<>();
    private final List<AlertService> observers = Collections.synchronizedList(new ArrayList<>());

    // Observer registration
    public void registerObserver(AlertService obs) {
        if (obs != null) observers.add(obs);
    }

    public void removeObserver(AlertService obs) {
        observers.remove(obs);
    }

    private void notifyLowStock(Product p) {
        synchronized (observers) {
            for (AlertService obs : observers) {
                try {
                    obs.onLowStock(p);
                } catch (Exception e) {
                    System.err.println("Observer threw: " + e.getMessage());
                }
            }
        }
    }

    // Add new product (dynamic)
    public void addProduct(Product p) {
        if (p == null) throw new IllegalArgumentException("Product is null");
        if (inventory.containsKey(p.getId())) throw new IllegalArgumentException("ID exists: " + p.getId());
        inventory.put(p.getId(), p);
    }

    // Get product safely
    private Product getProductOrThrow(int productId) {
        Product p = inventory.get(productId);
        if (p == null) throw new NoSuchElementException("Product ID not found: " + productId);
        return p;
    }

    // Receive shipment -> increase
    public void receiveShipment(int productId, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        Product p = getProductOrThrow(productId);
        p.increaseQuantity(amount);
    }

    // Fulfill order -> decrease and maybe trigger alert
    public void fulfillOrder(int productId, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        Product p = getProductOrThrow(productId);
        synchronized (p) {
            if (p.getQuantity() < amount) {
                throw new IllegalStateException("Insufficient stock for product " + productId);
            }
            p.decreaseQuantity(amount);
            if (p.getQuantity() < p.getReorderThreshold()) {
                notifyLowStock(p);
            }
        }
    }

    // Utility methods
    public List<Product> listAllProducts() {
        return new ArrayList<>(inventory.values());
    }

    public Optional<Product> getProduct(int id) {
        return Optional.ofNullable(inventory.get(id));
    }

    // Persistence: save inventory to CSV file (id,name,quantity,threshold)
    public void saveToFile(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Product p : listAllProducts()) {
                writer.write(String.format("%d,%s,%d,%d%n",
                        p.getId(),
                        p.getName().replaceAll(",", ""), // strip commas
                        p.getQuantity(),
                        p.getReorderThreshold()));
            }
        }
    }

    // Load inventory from CSV file; existing inventory will be cleared
    public void loadFromFile(Path path) throws IOException {
        inventory.clear();
        if (!Files.exists(path)) return;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                int qty = Integer.parseInt(parts[2].trim());
                int thr = Integer.parseInt(parts[3].trim());
                Product p = new Product(id, name, qty, thr);
                inventory.put(id, p);
            }
        }
    }
}
