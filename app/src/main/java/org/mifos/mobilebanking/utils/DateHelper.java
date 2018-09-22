/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.mifos.mobilebanking.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ishankhanna on 30/05/14.
 * <p>This is a helper class that will be used to convert List<Interger> Type Dates
 * from MifosX into Simple Strings or Date Formats</p>
 */
public class DateHelper {

    public static final String LOG_TAG = DateHelper.class.getSimpleName();

    public static final String FORMAT_dd_MMMM_yyyy = "dd MMMM yyyy";

    public static List<Integer> getCurrentDate(String dateFormat, String separator) {
        List<Integer> date = new ArrayList<>();

        String s = new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date());
        for (String str : s.split(separator)) {
            date.add(Integer.parseInt(str));
        }

        return date;
    }

    /**
     * the result string uses the list given in a reverse order ([x, y, z] results in "z y x")
     *
     * @param integersOfDate [year-month-day] (ex [2016, 4, 14])
     * @return date in the format day month year (ex 14 Apr 2016)
     */
    public static String getDateAsString(List<Integer> integersOfDate) {

        StringBuilder stringBuilder = new StringBuilder();
        if (integersOfDate != null) {
            stringBuilder.append(integersOfDate.get(2))
                    .append(' ')
                    .append(getMonthName(integersOfDate.get(1)))
                    .append(' ')
                    .append(integersOfDate.get(0));
        }
        return stringBuilder.toString();
    }

    public static String getDateAsString(List<Integer> integersOfDate, String pattern) {
        return DateHelper.getFormatConverter("dd MMM yyyy",
                pattern, DateHelper.getDateAsString(integersOfDate));

    }

    /**
     * This Method converting the dd-MM-yyyy format type date string into dd MMMM yyyy
     *
     * @param format     Final Format of date string
     * @param dateString date string
     * @return dd MMMM yyyy format date string.
     */
    public static String getSpecificFormat(String format, String dateString) {
        SimpleDateFormat pickerFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat finalFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = pickerFormat.parse(dateString);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }
        return finalFormat.format(date);
    }

    public static String getFormatConverter(String currentFormat, String requiredFormat,
            String dateString) {
        SimpleDateFormat pickerFormat = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        SimpleDateFormat finalFormat = new SimpleDateFormat(requiredFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = pickerFormat.parse(dateString);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }
        return finalFormat.format(date);
    }

    /**
     * @param month an integer from 1 to 12
     * @return string representation of the month like Jan or Feb..etc
     */
    public static String getMonthName(int month) {
        String monthName = "";
        switch (month) {
            case 1:
                monthName = "Jan";
                break;
            case 2:
                monthName = "Feb";
                break;
            case 3:
                monthName = "Mar";
                break;
            case 4:
                monthName = "Apr";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "Jun";
                break;
            case 7:
                monthName = "Jul";
                break;
            case 8:
                monthName = "Aug";
                break;
            case 9:
                monthName = "Sep";
                break;
            case 10:
                monthName = "Oct";
                break;
            case 11:
                monthName = "Nov";
                break;
            case 12:
                monthName = "Dec";
                break;
        }
        return monthName;
    }

    public static long getDateAsLongFromString(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.d("TAG", e.getMessage());
        }
        return date.getTime();
    }


    public static long getDateAsLongFromList(List<Integer> integersOfDate) {
        String dateStr = getDateAsString(integersOfDate);
        return getDateAsLongFromString(dateStr, "dd MMM yyyy");
    }

    public static long subtractWeeks(int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -number);
        return calendar.getTimeInMillis();
    }


    public static long subtractMonths(int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -number);
        return calendar.getTimeInMillis();
    }

    public static String getDateAsStringFromLong(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(new Date(timeInMillis));
    }

    public static String getDateAndTimeAsStringFromLong(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a dd MMM yyyy");
        return sdf.format(new Date(timeInMillis));
    }

}
