package com.wjy.util;

import java.io.File;

/**
 * @author wjy
 */
public abstract class StringUtils {

    private static final String FOLDER_SEPARATOR = File.separator;

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';


    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

}

