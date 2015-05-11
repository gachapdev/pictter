package com.elzup.pictter.pictter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hiro on 5/12/15.
 */
public class StringUtils {

    public static String join(String delimiter, List<String> strs) {
        StringBuilder buf = new StringBuilder();
        for(String str : strs){
            buf.append(str);
        }
        return buf.toString();
    }

    public static String join(String delimiter, String[] strs) {
        return join(delimiter, Arrays.asList(strs));
    }
}
