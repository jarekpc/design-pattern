package com.example.warehouse;

/**
 * @author jazy
 */
public class WarehouseException extends Exception{

    public WarehouseException(String message) {
        super(message);
    }

    public WarehouseException(String message, Throwable cause) {
        super(message, cause);
    }
}
