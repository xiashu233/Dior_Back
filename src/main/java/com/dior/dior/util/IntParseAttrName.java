package com.dior.dior.util;

public class IntParseAttrName {

    public static String parse(String attrId){
        String attrName = "";
        switch (attrId){
            case "1":
                attrName = "尺码";
                break;
            case "2":
                attrName = "色号";
                break;
        }
        return attrName;
    }
}
