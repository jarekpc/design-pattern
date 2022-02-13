package com.example.warehouse;

/**
 * @author jazy
 */
public final class WarehouseException extends Exception{

    WarehouseException(String message) {
        super(message);
    }

    WarehouseException(String message, Throwable cause) {
        super(message, cause);
    }
}
