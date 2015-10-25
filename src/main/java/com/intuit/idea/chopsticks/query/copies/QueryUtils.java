package com.intuit.idea.chopsticks.query.copies;

import com.intuit.idea.app.exceptions.FrameworkException;
import com.intuit.idea.app.globals.Literals;
import com.intuit.idea.app.metadata.Metadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metadata class gets the metadata for the table Created by achau1 on 8/24/14.
 */
public class QueryUtils {

    public static String validateDate(String date) throws FrameworkException {
        Pattern p1 = Pattern.compile("(0?[1-9]|1[012])[/,-](0?[1-9]|[12][0-9]|3[01])[/,-]((19|20)\\d\\d)\\s([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
        Matcher m1 = p1.matcher(date);
        if (m1.matches()) {
            date.replace("/", "-");
            return date;
        } else {
            Pattern p2 = Pattern.compile("(0?[1-9]|1[012])[/,-](0?[1-9]|[12][0-9]|3[01])[/,-]((19|20)\\d\\d)");
            Matcher m2 = p2.matcher(date);
            if (m2.matches()) {
                date.replace("/", "-");
                date = date + " 00:00:00";
                return date;
            } else
                throw new FrameworkException("incorrect start date format");
        }
    }

    public static boolean isDate(String type) {
        return Literals.getDateTypes().contains(type);
    }

    public static boolean isDate(Metadata md) {
        return isDate(md.getSrcType()) || isDate(md.getTarType());
    }

}
