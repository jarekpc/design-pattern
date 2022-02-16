package com.example.warehouse.dal;

import com.example.warehouse.Product;
import com.example.warehouse.WarehouseException;
import com.example.warehouse.util.CsvReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jazy
 */
public final class MemoryInventoryDao implements InventoryDao {

//    private static class InventoryDaoHolder {
//        private static final InventoryDao INSTANCE = new MemoryInventoryDao();
//    }

    private static final String DEFAULT_INVENTORY_CSV_FILE = "inventory.csv";

//    public static InventoryDao getInstance() {
//        return InventoryDaoHolder.INSTANCE;
//    }

    private final ProductDao productDao;
    private final Map<Integer, Integer> inventory;

    public MemoryInventoryDao(ProductDao productDao) {
        this.productDao = productDao;
        this.inventory = new HashMap<>();
        try {
            readInventory();
        } catch (FileNotFoundException ex) {
            System.err.println("Please ensure the required CSV files are present: " + ex.getMessage());
            System.exit(1);
        } catch (WarehouseException ex) {
            System.err.println("Failed to initialize the warehouse: " + ex.getMessage());
            System.exit(2);
        }
    }

    private void readInventory() throws FileNotFoundException, WarehouseException {
        CsvReader reader = new CsvReader(new FileInputStream(DEFAULT_INVENTORY_CSV_FILE));
        while (reader.hasNextRow()) {
            final List<String> row = reader.nextRow();
            if (row.isEmpty()) {
                continue;
            }
            int id = 0;
            try {
                id = Integer.valueOf(row.get(0));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
//                throw new WarehouseException("Failed to read inventory: invalid product ID in CSV, must be an integer.", ex);
            }
//            if (MemoryProductDao.getInstance().getProduct(id) == null) {
//                throw new WarehouseException("Failed to read inventory: unknown product ID: " + id);
//            }
            int quantity = 0;
            try {
                quantity = Integer.valueOf(row.get(1));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
//                throw new WarehouseException("Failed to read inventory: invalid quantity in CSV, must be an integer.", ex);
            }
            inventory.put(id, quantity);
        }
    }

    @Override
    public void updateStock(Map<Product, Integer> quantities) throws WarehouseException {

    }
}
