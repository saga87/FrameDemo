package saga.com.mypractise.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import saga.com.mypractise.utils.ActivityCollector;

/**
 * Created by lp on 2018/6/22.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
