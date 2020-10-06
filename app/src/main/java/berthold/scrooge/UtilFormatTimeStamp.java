package berthold.scrooge;
/*
 * UtilFormatTimeStamp.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 9/30/18 8:36 PM
 */


/**
 * This class provides methods to format H2- DB's timestamp- String for output
 */

public class UtilFormatTimeStamp {

    public static final Boolean WITH_TIME = true;
    public static final Boolean WITHOUT_TIME = false;

    /**
     * Common fromH2DataBaseTogermanDateFormat formatting
     *
     * @param timestamp String containing H2- timestamp: YYYY-MM-DD HH:mm.ss
     * @param TIME      If true, time is appended
     * @return Formated Date and time.
     */

    public static String fromH2DataBaseTogermanDateFormat(String timestamp, Boolean TIME) {
        String formated;

        // After this:
        // parts [0] contains the date "20xx-xx-xx"
        // Parts [1] contains the time "xx:xx:xx.xxx"

        String parts[] = timestamp.split(" ");

        // Extract year, month and day

        try {
            String dateparts[] = parts[0].split("-");
            String year = dateparts[0];
            String month = dateparts[1];
            String day = dateparts[2];

            // Extract hour+ minutes

            dateparts = parts[1].split(":");
            String hour = dateparts[0];
            String minutes = dateparts[1];

            if (!TIME) formated = day + "." + month + "." + year;
            else formated = day + "." + month + "." + year + "  " + hour + ":" + minutes;

        } catch (ArrayIndexOutOfBoundsException e) {
            formated = "Could not convert";
        }

        return formated;
    }

    /**
     * Converts simple date format to german date
     */
    public static String fromSimpleDateFormatToGerman(String timestamp) {

        String formated;

        try {
            String dateparts[] = timestamp.split("-");
            String year = dateparts[0];
            String month = dateparts[1];
            String day = dateparts[2];

            formated = day + "." + month + "." + year;

        } catch (ArrayIndexOutOfBoundsException e) {
            formated = "Could not convert";
        }
        return formated;
    }
}
