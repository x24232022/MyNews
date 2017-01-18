package com.example.administrator.mynews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/12/12.
 */

public class SpUtils {
    private static SharedPreferences sp;
    public static String GEADED="readed";
    public static void putBooleanData(Context context,String key,boolean b){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,b);
        edit.commit();
    }
    public static void putStringData(Context context,String key,String str){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);

        }

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,str);
        edit.commit();
    }
    public static boolean getBooleanData(Context context,String key,boolean defValue){
        if (sp==null){
         sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);

    }
    public static String getStringData(Context context,String key){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        return sp.getString(key,"");
    }
}
