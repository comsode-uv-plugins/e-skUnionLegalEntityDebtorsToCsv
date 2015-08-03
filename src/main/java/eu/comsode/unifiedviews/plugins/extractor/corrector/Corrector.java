package eu.comsode.unifiedviews.plugins.extractor.corrector;

import java.util.ArrayList;
import java.util.List;

import eu.comsode.unifiedviews.plugins.extractor.service.Compare;
import eu.comsode.unifiedviews.plugins.extractor.service.Transform;

public class Corrector {

    public String[] getArgs() {
        String[] args = {};
        return args;
    }

    public List<String> correct(List<String> row) {
        if (isHeader(row)) {
            return null;
        }
        List<String> newRow = new ArrayList<>();
        for (String s : row) {
            newRow.add(s.replace('\"', '\'').trim());
        }
        if (!newRow.get(2).isEmpty() && !newRow.get(3).isEmpty()) {
            newRow.set(2, newRow.get(2) + " " + newRow.get(3));
            newRow.set(3, "");
        }
        if (newRow.get(2).isEmpty() && !newRow.get(3).isEmpty()) {
            newRow.remove(2);
        }
        if (!newRow.get(2).isEmpty() && newRow.get(3).isEmpty()) {
            newRow.remove(3);
        }
        if (newRow.get(3).isEmpty() && (Compare.isInteger(newRow.get(4)) || Compare.isZIP(newRow.get(4)))) {
            newRow.remove(3);
        }
        if (newRow.size() > 6) {
            for (int i = newRow.size() - 1; i > 2; i--) {
                if (newRow.get(i).isEmpty()) {
                    newRow.remove(i);
                }
            }
            if (newRow.get(2).isEmpty() && (Compare.isInteger(newRow.get(4)) || Compare.isZIP(newRow.get(4)))) {
                newRow.remove(2);
            }
        }
        if (newRow.get(3).length() > 5) {
            newRow.add(3, "");
        }
        newRow.set(3, Transform.numToZip(newRow.get(3)));

        newRow.add(isConsistent(newRow) ? "correct" : "mismatch");
        return newRow;
    }

    private boolean isConsistent(List<String> row) {
        if (row.size() != 6) {
            return false;
        }
        if (!Compare.isZIP(row.get(3))) {
            return false;
        }
        if (!Compare.isDouble(row.get(5))) {
            return false;
        }
        return true;
    }

    public boolean isHeader(List<String> row) {
        if (row.get(0).trim().startsWith("Názov")) {
            return true;
        }
        return false;
    }
}