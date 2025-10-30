package com.warehouse;

public class ConsoleAlertService implements AlertService {
    @Override
    public void onLowStock(Product product) {
        System.out.println("\u26A0\ufe0f  RESTOCK ALERT: Low stock for " 
            + product.getName() + " — only " + product.getQuantity() + " left!");
    }
}
