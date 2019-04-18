package com.wzb.xevent.util;

/**
 * Created by samwangzhibo on 2018/8/15.
 */

public class FormatUtil {

    public static boolean checkStr2Int(String intStr) {
        try {
            Integer.parseInt(intStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkStr2Long(String intStr) {
        try {
            Long.parseLong(intStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static long getLongFromString(String intStr) {
        try {
            return Long.parseLong(intStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean checkStr2Float(String intStr) {
        try {
            Float.parseFloat(intStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static float getFloatFromString(String intStr) {
        try {
            return Float.parseFloat(intStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean checkStr2Double(String intStr) {
        try {
            Double.parseDouble(intStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double getDoubleFromString(String intStr) {
        try {
            return Double.parseDouble(intStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    public static int getIntFromString(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean isInt(Object intStr) {
        return intStr instanceof Integer;
    }

    public static boolean isString(Object intStr) {
        return intStr instanceof String;
    }

    public static Integer getInt(Object intStr) {
        if (intStr == null){
            return -1;
        }
        try {
            return (Integer) intStr;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getString(Object intStr) {
        try {
            return (String) intStr;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getLogStr(Object intStr) {
        if (intStr == null){
            return "null";
        }
        try {
            return intStr.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
