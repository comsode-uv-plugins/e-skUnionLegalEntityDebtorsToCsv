package eu.comsode.unifiedviews.plugins.extractor.service;

public class Transform {
    public static String numToZip(String num) {
        if (Compare.isZIP(num)) {
            return num;
        } else {
            String zip = "";
            if (Compare.isInteger(num)) {
                if (num.length() == 4) {
                    zip = "0" + num.substring(0, 2) + " " + num.substring(2);
                }
                if (num.length() == 5) {
                    zip = num.substring(0, 3) + " " + num.substring(3);
                }
            }
            return zip;
        }
    }
}