package com.example.warehouse;

/**
 * @author jazy
 */
public class Customer {

    private int id;
    private String name;


    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Customer(Customer customer) {
        this(customer.id, customer.name);
    }

    public Customer(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
