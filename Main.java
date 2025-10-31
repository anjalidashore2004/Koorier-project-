package com.warehouse;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse();
        ConsoleAlertService alertService = new ConsoleAlertService();
        warehouse.addObserver(alertService);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== WAREHOUSE INVENTORY SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. Receive Shipment");
            System.out.println("3. Fulfill Order");
            System.out.println("4. Show Inventory");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Product Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    warehouse.addProduct(new Product(id, name, qty));
                    break;

                case 2:
                    System.out.print("Enter Product ID: ");
                    int rId = sc.nextInt();
                    System.out.print("Enter Quantity to Receive: ");
                    int rQty = sc.nextInt();
                    warehouse.receiveShipment(rId, rQty);
                    break;

                case 3:
                    System.out.print("Enter Product ID: ");
                    int fId = sc.nextInt();
                    System.out.print("Enter Quantity to Fulfill: ");
                    int fQty = sc.nextInt();
                    warehouse.fulfillOrder(fId, fQty);
                    break;

                case 4:
                    warehouse.showInventory();
                    break;

                case 5:
                    System.out.println("🚪 Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
