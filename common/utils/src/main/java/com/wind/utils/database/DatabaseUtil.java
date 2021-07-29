package com.wind.utils.database;



import java.util.ArrayList;

public class DatabaseUtil {
    public static boolean stringChecker(String str) {
        return str != null;
    }

    public static boolean isStringExist(String str) {
        return str != null && !str.equals("");
    }

    public static boolean isCharExist(Character c) {
        return c != null && c == '\0';
    }

    public static char stringToChar(String str) {
        return str.charAt(0);
    }

    public static boolean listChecker(ArrayList<String> list) {
        return list != null && list.size() != 0;
    }

    public static boolean listNotEmpty(ArrayList<String> list) {
        if (list.size() == 0) {
            return false;
        } else return !list.get(0).equals("");
    }

    public static boolean booleanChecker(Boolean b) {
        return b != null;
    }

    public static boolean integerChecker(Integer i) {
        return i != null;
    }

    public static boolean lessThanMax(Integer i, int max) {
        return i <= max;
    }

    public static boolean multipleOfTwo(Integer i) {
        while (true) {
            int j = i % 2;
            i = i / 2;
            if (j == 1) {
                return false;
            }
            if (i == 2) {
                return true;
            }
        }
    }

    public static boolean modeCheckerSQL(String writerMode) {
        return writerMode.equals("insert") || writerMode.equals("update") || writerMode.equals("replace");
    }

    public static boolean modeCheckerTxt(String writerMode) {
        return writerMode.equals("truncate") || writerMode.equals("append") || writerMode.equals("nonConflict");
    }

    public static boolean fileFormatChecker(String fileFormat) {
        return fileFormat.equals("csv") || fileFormat.equals("text");
    }
}
