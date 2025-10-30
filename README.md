# Warehouse Inventory Tracker (Event-based)

Simple Java console application demonstrating:
- Product model
- Warehouse manager (add, receiveShipment, fulfillOrder)
- Observer pattern for low-stock alerts
- In-memory storage (ConcurrentHashMap)
- Optional persistence to `data/inventory.txt`
- Simple multithreading demo in `Main.java`

## How to run

Requirements:
- Java JDK 11+ installed

Compile:
```
javac -d out src/com/warehouse/*.java
```

Run:
```
java -cp out com.warehouse.Main
```

The program will:
- Load `data/inventory.txt` if present
- Populate sample products if none exist
- Run a small threaded simulation of shipments and orders
- Save the final inventory to `data/inventory.txt`

## Files
- `src/com/warehouse/*.java` : source code
- `data/` : where inventory.txt is saved/loaded
- `README.md` : this file

## GitHub
1. Create a new repository on GitHub
2. `git init` in project root
3. Add, commit, and push as usual

## Notes
- All operations are via methods (no direct field access)
- Proper exceptions thrown for invalid IDs / insufficient stock
