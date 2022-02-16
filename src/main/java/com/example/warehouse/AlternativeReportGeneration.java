package com.example.warehouse;

import com.example.warehouse.dal.OrderDao;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jazy
 */
public class AlternativeReportGeneration implements ReportGeneration {

    private final OrderDao orderDao;

    public AlternativeReportGeneration(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Report generateReport(Report.Type type) throws WarehouseException {
        checkReportType(type);
        Report report = new Report();
        report.addLabel("Date");
        report.addLabel("Total products");
        report.addLabel("Total revenue");
        orderDao.getOrders()
                .stream()
                .sorted()
                .collect(Collectors.groupingBy(Order::getDate, LinkedHashMap::new, Collectors.toList()))
                .forEach((date, orders) -> report.addRecord(Arrays.asList(
                        date,
                        orders
                                .stream()
                                .sorted()
                                .map(Order::getQuantities)
                                .map(Map::values)
                                .flatMap(Collection::stream)
                                .mapToInt(Integer::intValue)
                                .sum(),
                        orders
                                .stream()
                                .sorted()
                                .mapToInt(Order::getTotalPrice)
                                .sum())));
        return report;
    }

    private void checkReportType(Report.Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Report type cannot be null.");
        }
        if (type != Report.Type.DAILY_REVENUE) {
            throw new UnsupportedOperationException(String.format("Report type: %s not yet implemented.", type));
        }
    }
}
