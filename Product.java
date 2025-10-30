package com.warehouse;

public class Product {
    private final int id;
    private final String name;
    private int quantity;
    private int reorderThreshold;

    public Product(int id, String name, int quantity, int reorderThreshold) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public synchronized int getQuantity() { return quantity; }

    public synchronized void increaseQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be >= 0");
        this.quantity += amount;
    }

    public synchronized void decreaseQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be >= 0");
        if (amount > this.quantity) throw new IllegalStateException("Insufficient stock");
        this.quantity -= amount;
    }

    public int getReorderThreshold() { return reorderThreshold; }
    public void setReorderThreshold(int reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', qty=%d, threshold=%d}",
                id, name, quantity, reorderThreshold);
    }
}
