package com.warehouse;

import java.nio.file.Paths;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Warehouse wh = new Warehouse();
        wh.registerObserver(new ConsoleAlertService());

        // Try to load existing inventory (if any)
        try {
            wh.loadFromFile(Paths.get("data", "inventory.txt"));
            System.out.println("Loaded inventory from data/inventory.txt (if present).");
        } catch (Exception e) {
            System.err.println("Could not load inventory: " + e.getMessage());
        }

        // If inventory empty, create sample products
        if (wh.listAllProducts().isEmpty()) {
            wh.addProduct(new Product(1, "Laptop", 0, 5));
            wh.addProduct(new Product(2, "Smartphone", 20, 5));
            wh.addProduct(new Product(3, "Headphones", 15, 3));
        }

        System.out.println("Initial inventory:");
        for (Product p : wh.listAllProducts()) {
            System.out.println(p);
        }

        // Simulate shipments and orders concurrently (bonus: multithreading)
        ExecutorService ex = Executors.newFixedThreadPool(4);
        Runnable shipmentTask = () -> {
            try {
                wh.receiveShipment(1, 10); // Laptop +10
                System.out.println("[Shipment] Received 10 Laptops");
            } catch (Exception e) {
                System.err.println("Shipment error: " + e.getMessage());
            }
        };

        Runnable orderTask = () -> {
            try {
                wh.fulfillOrder(1, 6); // Laptop -6 (may trigger alert)
                System.out.println("[Order] Fulfilled 6 Laptops");
            } catch (Exception e) {
                System.err.println("Order error: " + e.getMessage());
            }
        };

        // Submit tasks
        ex.submit(shipmentTask);
        ex.submit(orderTask);

        // Additional tasks to demonstrate concurrent updates
        ex.submit(() -> {
            try { wh.fulfillOrder(2, 18); System.out.println("[Order] Fulfilled 18 Smartphones"); }
            catch (Exception e) { System.err.println("Order error: " + e.getMessage()); }
        });

        ex.submit(() -> {
            try { wh.receiveShipment(3, 5); System.out.println("[Shipment] Received 5 Headphones"); }
            catch (Exception e) { System.err.println("Shipment error: " + e.getMessage()); }
        });

        // Wait for tasks to finish
        ex.shutdown();
        try {
            if (!ex.awaitTermination(5, TimeUnit.SECONDS)) {
                ex.shutdownNow();
            }
        } catch (InterruptedException e) {
            ex.shutdownNow();
        }

        System.out.println("\nFinal inventory:");
        for (Product p : wh.listAllProducts()) {
            System.out.println(p);
        }

        // Save inventory
        try {
            wh.saveToFile(Paths.get("data", "inventory.txt"));
            System.out.println("\nSaved inventory to data/inventory.txt");
        } catch (Exception e) {
            System.err.println("Could not save inventory: " + e.getMessage());
        }

        System.out.println("\nDone.");
    }
}
