package com.example.warehouse;

/**
 * @author jazy
 */
public interface ReportGeneration {
    Report generateReport(Report.Type type) throws WarehouseException;
}
