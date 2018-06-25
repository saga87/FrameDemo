package saga.com.mypractise;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


import saga.com.mypractise.model.DaoMaster;
import saga.com.mypractise.model.DaoSession;

/**
 * Created by lp on 2018/6/22.
 */

public class MyApplication extends Application{
    private static MyApplication application;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        setupDatabase();
    }

    private void setupDatabase() {
        //创建数据库friend.db"   SQLite数据库的SQLiteOpenHelper的具体实现
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "friend.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象 GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者 管理所有的Dao对象，对象中存在着增删改查等API
        daoSession = daoMaster.newSession();
    }

    public static MyApplication getApplication(){
        return application;
    }

    public static DaoSession getDaoInstance() {
        return daoSession;
    }
}
