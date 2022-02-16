package com.example.warehouse;

import com.example.warehouse.dal.CustomerDao;
import com.example.warehouse.dal.InventoryDao;
import com.example.warehouse.dal.OrderDao;
import com.example.warehouse.dal.ProductDao;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jazy
 */
public class Warehouse {

    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final InventoryDao inventoryDao;

    private final ReportGeneration reportGeneration;

    public Warehouse(ProductDao productDao, CustomerDao customerDao, InventoryDao inventoryDao, OrderDao orderDao, ReportGeneration reportGeneration) {
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.inventoryDao = inventoryDao;
        this.orderDao = orderDao;
        this.reportGeneration = reportGeneration;
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

    public void addProduct(String name, int price) throws WarehouseException {
        if (price < 0) {
            throw new IllegalArgumentException("The product's price cannot be negative.");
        }
        Product product = new Product(name, price);
        productDao.addProduct(product);
    }

    public void addOrder(int customerId, Map<Integer, Integer> quantities) throws WarehouseException {
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

//    public Report generateDailyRevenueReport(Report.Type type) {
//        if (type == Report.Type.DAILY_REVENUE) {
//            return generateDailyRevenueReport();
//        }
//        throw new UnsupportedOperationException(String.format("Report type: %s not yet implemented.", type));
//    }

//    private Report generateDailyRevenueReport() {
//        Report report = new Report();
//        report.addLabel("Date");
//        report.addLabel("Total revenue");
//        orderDao.getOrders().stream().filter(o -> !o.isPending()).sorted().collect(groupingBy(Order::getDate, LinkedHashMap::new, summingInt(Order::getTotalPrice))).forEach((date, totalRevenue) -> report.addRecord(Arrays.asList(date, totalRevenue)));
//        return report;
//    }

    public Report generateReport(Report.Type type) throws WarehouseException {
        return reportGeneration.generateReport(type);
    }

}
