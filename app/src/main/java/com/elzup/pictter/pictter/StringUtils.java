package com.elzup.pictter.pictter;

import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String join(String delimiter, List<String> strs) {
        return Joiner.on(delimiter).join(strs);
    }

    public static String join(String delimiter, String[] strs) {
        return join(delimiter, Arrays.asList(strs));
    }
}
