package com.example.warehouse.export;

import com.example.warehouse.Report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jazy
 */
public final class TxtExporter extends AbstractExporter {

    private static final String BORDER = "*";
    private static final String LEFT_BORDER = BORDER + " ";
    private static final String RIGHT_BORDER = " " + BORDER;
    private static final String SEPERATOR = " | ";

    private final List<Integer> widths;

    private final int totalWidth;

    TxtExporter(Report report, PrintStream out) {
        super(report, out);
        this.widths = calcWidths(report);

        totalWidth = widths.stream().mapToInt(Integer::intValue).sum()
                + LEFT_BORDER.length()
                + SEPERATOR.length() * (report.getLabels().size() - 1)
                + RIGHT_BORDER.length();
    }


    @Override
    protected void beforeRecords(PrintStream out) {
        printBorder(out);
    }

    private void printBorder(PrintStream out) {
        for (int i = 0; i < totalWidth; i++) {
            out.print(BORDER);
        }
        out.println();
    }


    @Override
    protected void handleRecord(PrintStream out, List<String> records, boolean first, boolean last) {
        printStrings(out, records);
    }

    @Override
    protected void handleLabels(PrintStream out, List<String> labels) {
        printStrings(out, labels);
    }

    private void printStrings(PrintStream out, List<String> strings) {
        out.print(LEFT_BORDER);
        out.print(IntStream.range(0, strings.size())
                .mapToObj(i -> {
                    final String fmt = String.format("%%%ss", widths.get(i));
                    return String.format(fmt, strings.get(i));
                })
                .collect(Collectors.joining(SEPERATOR)));

        out.print(RIGHT_BORDER);
        out.println();
    }

}
