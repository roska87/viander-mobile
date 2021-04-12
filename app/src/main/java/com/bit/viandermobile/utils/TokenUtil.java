package com.bit.viandermobile.utils;

import android.text.TextUtils;

public class TokenUtil {

    public static String formatTokenKey(String loginKey){
        if(TextUtils.isEmpty(loginKey)){
            return null;
        }
        return "Token " + loginKey;
    }

}
