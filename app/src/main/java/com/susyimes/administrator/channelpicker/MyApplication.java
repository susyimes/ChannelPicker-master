package com.susyimes.administrator.channelpicker;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;



/**
 * MyApplication
 * Created by Susyimes on 2016/3/2.
 */
public class MyApplication extends Application {

    public static Context AppContenxt;
    private static MyApplication application;
    public static String version;
    //调试模式(打印日志)
    private static boolean DEBUG = true;
    public static LiteOrm sDb;

    private static final String DB_NAME = "channelpicker.db";

    @Override
    public void onCreate() {
        super.onCreate();

        AppContenxt = getApplicationContext();
        application = this;
        version="v1.0.0";
        //Fresco.initialize(this);
        //KLog.init(DEBUG);


        sDb = LiteOrm.newSingleInstance(this, DB_NAME);
        if (BuildConfig.DEBUG) {
            sDb.setDebugged(true);
        }

    }

    public static Context getAppContenxt() {
        return  AppContenxt;
    }





   /* //解决dex file 64k问题
    @Override
        protected void attachBaseContext(Context base) {
                 super.attachBaseContext(base);
                 MultiDex.install(this) ;
    }*/

    public static MyApplication getApplication(){
        return application;
    }

}