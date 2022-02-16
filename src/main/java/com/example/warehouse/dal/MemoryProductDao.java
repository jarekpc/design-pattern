package com.example.warehouse.dal;

import com.example.warehouse.Product;
import com.example.warehouse.WarehouseException;
import com.example.warehouse.util.CsvReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jazy
 */
public class MemoryProductDao implements ProductDao {

//    private static class ProductDaoHolder {
//        private static final ProductDao INSTANCE = new MemoryProductDao();
//    }

    private static final String DEFAULT_PRODUCTS_CSV_FILE = "products.csv";

//    public static ProductDao getInstance() {
//        return ProductDaoHolder.INSTANCE;
//    }

    private final Map<Integer, Product> products;

    public MemoryProductDao() {
        this.products = new HashMap<>();
        try {
            readProducts();
        } catch (FileNotFoundException ex) {
            System.err.println("Please ensure the required CSV files are present: " + ex.getMessage());
            System.exit(1);
        } catch (WarehouseException ex) {
            System.err.println("Failed to initialize the warehouse!!!: " + ex.getMessage());
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
            int id = 0;
            try {
                id = Integer.valueOf(row.get(0));
            } catch (NumberFormatException ex) {
//                throw new WarehouseException("Failed to read products: invalid product ID in CSV, must be an integer.", ex);
                ex.printStackTrace();
            }
            String name = row.get(1);
            int price = 0;
            try {
                price = Integer.valueOf(row.get(2));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
//                throw new WarehouseException("Failed to read products: invalid price in CSV, must be an integer.", ex);
            }
            if (products.containsKey(id)) {
//                throw new WarehouseException("Failed to read products: duplicate product ID in CSV.");
            }
            products.put(id, new Product(id, name, price));
        }
    }

    @Override
    public Collection<Product> getProducts() throws WarehouseException {
        return products.values().stream().map(Product::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Product getProduct(int id) throws WarehouseException {
        Product product = products.get(id);
        return product == null ? null : new Product(product);

    }

    @Override
    public void addProduct(Product product) throws WarehouseException {
        int max = Collections.max(products.keySet());
        int id = max + 1;
        product.setId(id);
        products.put(id, product);
    }
}
