package com.warehouse;

import java.util.*;

public class Warehouse {

    private Map<Integer, Product> products = new HashMap<>();
    private List<AlertService> observers = new ArrayList<>();

    public void addObserver(AlertService observer) {
        observers.add(observer);
    }

    private void notifyObservers(Product product) {
        for (AlertService obs : observers) {
            obs.onLowStock(product);
        }
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        System.out.println("✅ Added: " + product);
    }

    public void receiveShipment(int productId, int quantity) {
        Product product = products.get(productId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            System.out.println("📦 Received " + quantity + " units of " + product.getName());
        } else {
            System.out.println("❌ Product not found!");
        }
    }

    public void fulfillOrder(int productId, int quantity) {
        Product product = products.get(productId);
        if (product == null) {
            System.out.println("❌ Invalid product ID!");
            return;
        }

        if (product.getQuantity() < quantity) {
            System.out.println("⚠ Not enough stock for " + product.getName());
            return;
        }

        product.setQuantity(product.getQuantity() - quantity);
        System.out.println("📤 Order fulfilled for " + quantity + " units of " + product.getName());
        notifyObservers(product);
    }

    public void showInventory() {
        System.out.println("\n📋 Current Inventory:");
        for (Product p : products.values()) {
            System.out.println(p);
        }
    }
}
