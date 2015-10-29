package com.intuit.idea.ziplock.core.utils;

import com.intuit.idea.ziplock.core.providers.DataProvider;

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
