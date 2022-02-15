package com.example;

import com.example.cli.Cli;
import com.example.web.Web;

import java.util.Arrays;

/**
 * @author jazy
 */
public class Main {

    public static void main(String[] args) {
        new Web(Arrays.asList(args)).run();
        new Cli(Arrays.asList(args)).run();

        System.exit(0);
    }
}
