package saga.com.mypractise.view.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saga.com.mypractise.R;
import saga.com.mypractise.view.fragment.CircleFragment;
import saga.com.mypractise.view.fragment.HeadFragment;
import saga.com.mypractise.view.fragment.MyFragment;
import saga.com.mypractise.view.fragment.UpFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.rb_fp)
    RadioButton rbFp;
    @BindView(R.id.rb_circle)
    RadioButton rbCircle;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_up)
    RadioButton rbUp;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAdapter();
        setListener();
        applyPermission();//动态权限申请
    }

    private void applyPermission() {
        if (AndPermission.hasPermission(this,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //有权限不再申请
        }else{
            AndPermission.with(this)
                    .requestCode(100)
                    .permission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    //权限申请回调接口
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if(requestCode == 100) {
                // TODO 相应代码。
                //do nothing
            }
        }
        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                // 用自定义的提示语
                AndPermission.defaultSettingDialog(MainActivity.this, 103)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };

    private void setAdapter() {
        fragments = new ArrayList<>();
        fragments.add(new HeadFragment());
        fragments.add(new UpFragment());
        fragments.add(new CircleFragment());
        fragments.add(new MyFragment());
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);
    }

    private class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void setListener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.rb_fp:
                       vp.setCurrentItem(0);
                        break;
                    case R.id.rb_up:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.rb_circle:
                        vp.setCurrentItem(2);
                        break;
                    case R.id.rb_me:
                        vp.setCurrentItem(3);
                        break;
                }
            }
        });

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rbFp.setChecked(true);
                        rbFp.setTextColor(Color.parseColor("#ac1234"));
                        rbUp.setTextColor(Color.BLACK);
                        rbCircle.setTextColor(Color.BLACK);
                        rbMe.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        rbUp.setChecked(true);
                        rbUp.setTextColor(Color.parseColor("#ac1234"));
                        rbFp.setTextColor(Color.BLACK);
                        rbCircle.setTextColor(Color.BLACK);
                        rbMe.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        rbCircle.setChecked(true);
                        rbCircle.setTextColor(Color.parseColor("#ac1234"));
                        rbUp.setTextColor(Color.BLACK);
                        rbFp.setTextColor(Color.BLACK);
                        rbMe.setTextColor(Color.BLACK);
                        break;
                    case 3:
                        rbMe.setChecked(true);
                        rbMe.setTextColor(Color.parseColor("#ac1234"));
                        rbUp.setTextColor(Color.BLACK);
                        rbCircle.setTextColor(Color.BLACK);
                        rbFp.setTextColor(Color.BLACK);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
