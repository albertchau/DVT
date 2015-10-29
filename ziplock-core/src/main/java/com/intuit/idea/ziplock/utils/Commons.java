package com.intuit.idea.ziplock.utils;

import com.intuit.idea.ziplock.providers.DataProvider;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/28/15
 * ************************************
 */
public class Commons {

    public static String identifierName(DataProvider source, DataProvider target) {
        return "[" + source.getTableName() + "/" + target.getTableName() + "]";
    }
}
