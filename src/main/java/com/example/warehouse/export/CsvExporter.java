package com.example.warehouse.export;

import com.example.warehouse.Report;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jazy
 */
public final class CsvExporter extends AbstractExporter {

    private static final String SEPARATOR = ",";

    private final boolean includeHeader;

    public CsvExporter(Report report, PrintStream out, boolean includeHeader) {
        super(report, out);
        this.includeHeader = includeHeader;
    }

    @Override
    protected void handleRecord(PrintStream out, List<String> records, boolean first, boolean last) {
        printStrings(out, records);
    }

    @Override
    protected void handleLabels(PrintStream out, List<String> labels) {
        if (includeHeader) {
            printStrings(out, labels);
        }
    }

    private void printStrings(PrintStream out, List<String> records) {
        out.println(records.stream().collect(Collectors.joining(SEPARATOR)));
    }
}
