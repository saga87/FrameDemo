package saga.com.mypractise;

import android.app.Application;

/**
 * Created by lp on 2018/6/22.
 */

public class MyApplication extends Application{
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MyApplication getApplication(){
        return application;
    }
}
