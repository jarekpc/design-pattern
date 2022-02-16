package com.example.warehouse;

import com.example.warehouse.export.ExportType;

/**
 * @author jazy
 */
public class NoReportDelivery implements ReportDelivery {

    @Override
    public void deliver(Report.Type reportType, ExportType exportType, byte[] bytes) throws ReportDeliveryException {
        // INFO: intentionally left empty.
    }

    @Override
    public String getName() {
        return "No report delivery";
    }
}
