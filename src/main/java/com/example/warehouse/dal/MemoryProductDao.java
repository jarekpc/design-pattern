package com.example.warehouse.dal;

import com.example.warehouse.Product;
import com.example.warehouse.WarehouseException;
import com.example.warehouse.util.CsvReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jazy
 */
public class MemoryProductDao implements ProductDao {

    private static class ProductDaoHolder {
        private static final ProductDao INSTANCE = new MemoryProductDao();
    }

    private static final String DEFAULT_PRODUCTS_CSV_FILE = "products.csv";

    public static ProductDao getInstance() {
        return ProductDaoHolder.INSTANCE;
    }

    private final Map<Integer, Product> products;

    private MemoryProductDao() {
        this.products = new HashMap<>();
        try {
            readProducts();
        } catch (FileNotFoundException ex) {
            System.err.println("Please ensure the required CSV files are present: " + ex.getMessage());
            System.exit(1);
        } catch (WarehouseException ex) {
            System.err.println("Failed to initialize the warehouse: " + ex.getMessage());
            System.exit(2);
        }
    }

    private void readProducts() throws FileNotFoundException, WarehouseException {
        CsvReader reader = new CsvReader(new FileInputStream(DEFAULT_PRODUCTS_CSV_FILE));
        while (reader.hasNextRow()) {
            List<String> row = reader.nextRow();
            if (row.isEmpty()) {
                continue;
            }
            int id;
            try {
                id = Integer.valueOf(row.get(0));
            } catch (NumberFormatException ex) {
                throw new WarehouseException("Failed to read products: invalid product ID in CSV, must be an integer.", ex);
            }
            String name = row.get(1);
            int price;
            try {
                price = Integer.valueOf(row.get(2));
            } catch (NumberFormatException ex) {
                throw new WarehouseException("Failed to read products: invalid price in CSV, must be an integer.", ex);
            }
            if(products.containsKey(id)){
                throw new WarehouseException("Failed to read products: duplicate product ID in CSV.");
            }
            products.put(id, new Product(id, name, price));
        }
    }

    @Override
    public Collection<Product> getProducts() throws WarehouseException {
        return null;
    }

    @Override
    public Product getProduct(int id) throws WarehouseException {
        return null;
    }

    @Override
    public void addProduct(Product product) throws WarehouseException {

    }
}
