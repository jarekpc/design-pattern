package com.example.warehouse.export;

import com.example.warehouse.Report;

import java.io.PrintStream;
import java.util.List;

/**
 * @author jazy
 */
public class JsonExporter implements Exporter {

    private final Report report;
    private final PrintStream out;

    public JsonExporter(Report report, PrintStream out) {
        this.report = report;
        this.out = out;
    }

    @Override
    public void export() {
        out.println("[");
        final List<List<String>> records = report.getRecords();
        for (int i = 0; i < records.size(); i++) {
            out.println("\t{");
            final List<String> record = records.get(i);
            for (int j = 0; j < report.getLabels().size(); j++) {
                final String label = report.getLabels().get(j);
                final String field = record.get(i);
                out.printf("\t\t\"%s\":", label);
                if (j == 0) {
                    out.printf("\"%s\",%n", field);
                }else if(i == 1){
                    out.printf("%s%n", field);
                }
            }
            out.print("\t}");
            if(i != records.size() - 1){
                out.print(",");
            }
            out.println();
        }
        out.println("]");
    }
}
