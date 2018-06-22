package saga.com.mypractise.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import saga.com.mypractise.R;
import saga.com.mypractise.presenter.ILoginPresenter;
import saga.com.mypractise.presenter.LoginPresenterImpl;
import saga.com.mypractise.utils.StatusBarUtil;
import saga.com.mypractise.utils.Util;
import saga.com.mypractise.view.iview.ILoginView;

public class LoginActivity extends BaseActivity implements ILoginView{

    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.et_login_paw)
    EditText etLoginPaw;
    @BindView(R.id.tv_login_find_paw)
    TextView tvLoginFindPaw;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;

    ILoginPresenter iLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarUtil.setWindowStatusBarColor(this, Color.parseColor("#B3D5FC"));
        iLoginPresenter = new LoginPresenterImpl(this);//view与presenter绑定
    }

    @OnClick({R.id.tv_login_find_paw,R.id.btn_login,R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                iLoginPresenter.login(etLoginUsername.getText().toString().trim(),
                        etLoginPaw.getText().toString().trim());
                break;
            default:
                Util.showToast("除登陆外暂未开发");
                break;
        }
    }

    @Override
    public void onLoginResult(boolean ret, Object obj) {
        if (ret){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Util.showToast(obj.toString());
        }
    }
}
