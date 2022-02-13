package com.example.warehouse.export;

import com.example.warehouse.Report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jazy
 */
public abstract class AbstractExporter implements Exporter {
    private final Report report;
    private final PrintStream out;

    AbstractExporter(Report report, PrintStream out) {
        this.report = report;
        this.out = out;
    }

    public final void export() {
        beforeRecords(out);
        handleLabels(out, report.getLabels());
        afterLabels(out);

        beforeRecords(out);
        handleRecords();
        afterRecords(out);
    }

    protected void afterRecords(PrintStream out) {
    }

    protected void afterLabels(PrintStream out) {
    }

    protected List<Integer> calcWidths(Report report) {
        List<Integer> widths = new ArrayList<>();
        report.getLabels().forEach(l -> widths.add(l.length()));
        for (List<String> record : report.getRecords()) {
            for (int i = 0; i < widths.size(); i++) {
                int maxWidth = widths.get(i);
                int width = record.get(i).length();
                if(width > maxWidth){
                    widths.set(i, width);
                }
            }
        }
        return widths;
    }

    protected void handleRecords() {
        final List<List<String>> records = report.getRecords();
        if (records.size() == 1) {
            handleRecord(out, records.get(0), true, true);
        } else if (records.size() >= 2) {
            handleRecord(out, records.get(0), true, true);
            for (List<String> record : records.subList(1, records.size() - 1)) {
                handleRecord(out, record, false, false);
            }
            handleRecord(out, records.get(records.size() - 1), false, true);
        }
    }

    protected abstract void handleRecord(PrintStream out, List<String> strings, boolean first, boolean last);

    protected abstract void handleLabels(PrintStream out, List<String> labels);

    protected void beforeRecords(PrintStream out) {

    }

}
