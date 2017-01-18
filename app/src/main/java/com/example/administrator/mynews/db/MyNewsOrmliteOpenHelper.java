package com.example.administrator.mynews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.mynews.entity.CollectNewBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/12/22.
 */

public class MyNewsOrmliteOpenHelper extends OrmLiteSqliteOpenHelper{
    private static final String NEWS_TABLE = "news_table";
    private static final int DB_VERSON=  1;
    private static MyNewsOrmliteOpenHelper instance;


    private MyNewsOrmliteOpenHelper(Context context) {
        super(context, NEWS_TABLE, null, DB_VERSON);
    }

    public  static  MyNewsOrmliteOpenHelper getInstances(Context context){
        if (instance == null) {
            instance= new MyNewsOrmliteOpenHelper(context);
        }
        return  instance;
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTableIfNotExists(connectionSource,CollectNewBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        if(newVersion>oldVersion){
            try {
                TableUtils.dropTable(connectionSource,CollectNewBean.class,true);
                onCreate(database,connectionSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
