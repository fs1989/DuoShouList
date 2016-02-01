package com.duoshoulist.duoshoulist.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Dan on 2016/2/1.
 */
public class utils {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
