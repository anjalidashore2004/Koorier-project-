package com.warehouse;

public class ConsoleAlertService implements AlertService {

    @Override
    public void onLowStock(Product product) {
        System.out.println("⚠ Alert: " + product.getName() + " stock is now " + product.getQuantity());
    }
}
