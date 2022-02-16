package com.example.warehouse;

import com.example.warehouse.export.ExportType;

/**
 * @author jazy
 */
public interface ReportDelivery {

    void deliver(Report.Type reportType, ExportType exportType, byte[] bytes) throws ReportDeliveryException;

    String getName();
}
