package com.duoshoulist.duoshoulist.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Register;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dan on 2016/2/1.
 */
public class utils {

    // 隐藏键盘
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // 检查手机号码
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
