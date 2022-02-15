package com.example.warehouse;

import com.example.warehouse.dal.*;
import com.example.warehouse.util.CsvReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * @author jazy
 */
public class Warehouse {

    public static class WarehouseHolder {
        private static final Warehouse INSTANCE = new Warehouse();
    }

    public static Warehouse getInstance() {
        return WarehouseHolder.INSTANCE;
    }

    private static final String DEFAULT_PRODUCTS_CSV_FILE = "products.csv";
    private static final String DEFAULT_INVENTORY_CSV_FILE = "inventory.csv";
    private static final String DEFAULT_CUSTOMERS_CSV_FILE = "customers.csv";
    private static final String DEFAULT_ORDERS_CSV_FILE = "orders.csv";

    private final ProductDao productDao = MemoryProductDao.getInstance();
    private final CustomerDao customerDao = DbCustomerDao.getInstance();
    private final OrderDao orderDao = MemoryOrderDao.getInstance();
    private final InventoryDao inventoryDao = MemoryInventoryDao.getInstance();


    private Warehouse() {
    }

    public Collection<Product> getProducts() throws WarehouseException {
        return productDao.getProducts().stream().sorted(Comparator.comparing(Product::getId)).collect(Collectors.toList());
    }

    public Collection<Customer> getCustomers() throws WarehouseException {
        return customerDao.getCustomers().stream().sorted(Comparator.comparing(Customer::getId)).collect(Collectors.toList());
    }

    public Collection<Order> getOrders() {
        return orderDao.getOrders().stream().sorted().sorted(Comparator.comparing(Order::getId)).collect(Collectors.toList());
    }

    public synchronized void addProduct(String name, int price) throws WarehouseException {
        if (price < 0) {
            throw new IllegalArgumentException("The product's price cannot be negative.");
        }
        Product product = new Product(name, price);
        productDao.addProduct(product);
    }

    public synchronized void addOrder(int customerId, Map<Integer, Integer> quantities) throws WarehouseException {
        if (quantities.isEmpty()) {
            throw new IllegalArgumentException("There has to items in the order, it cannot be empty.");
        }
        Customer customer = customerDao.getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Unknown customer ID: " + customerId);
        }
        HashMap<Product, Integer> mappedQuantities = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            final Product product = productDao.getProduct(entry.getKey());
            if (product == null) {
                throw new IllegalArgumentException("Unknown product ID: " + entry.getKey());
            }
            int quantity = entry.getValue();
            if (quantity < 1) {
                throw new IllegalArgumentException("Ordered quantity must be greater than 0.");
            }
            mappedQuantities.put(product, quantity);
        }

        inventoryDao.updateStock(mappedQuantities);
        Order order = new Order(customer, mappedQuantities);
        orderDao.addOrder(order);
    }

    public Report generateDailyRevenueReport(Report.Type type) {
        if (type == Report.Type.DAILY_REVENUE) {
            return generateDailyRevenueReport();
        }
        throw new UnsupportedOperationException(String.format("Report type: %s not yet implemented.", type));
    }

    private Report generateDailyRevenueReport() {
        Report report = new Report();
        report.addLabel("Date");
        report.addLabel("Total revenue");
        orderDao.getOrders().stream()
                .filter(o -> !o.isPending())
                .sorted()
                .collect(groupingBy(Order::getDate, LinkedHashMap::new, summingInt(Order::getTotalPrice)))
                .forEach((date, totalRevenue) -> report.addRecord(Arrays.asList(date, totalRevenue)));
        return report;
    }


}
