package com.example.administrator.mynews.utils;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

import java.util.Objects;

/**
 * Created by Administrator on 2016/12/13.
 */

public class NoHttpInstences {
    private static Object o=new Object();
    private static RequestQueue requestQueue;

    private NoHttpInstences() {


    }
    public static RequestQueue getInstance(){
        if(requestQueue==null){
           synchronized (o){
               if (requestQueue==null){
                   requestQueue = NoHttp.newRequestQueue();
               }
           }
        }

        return requestQueue;
    }
}
