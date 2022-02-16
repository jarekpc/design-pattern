package com.example.warehouse;

/**
 * @author jazy
 */
public class ReportDeliveryException  extends WarehouseException {

    public ReportDeliveryException(String message) {
        super(message);
    }

    public ReportDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
