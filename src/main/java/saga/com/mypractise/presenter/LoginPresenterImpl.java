package saga.com.mypractise.presenter;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saga.com.mypractise.MyApplication;
import saga.com.mypractise.retrofit.RetrofitFactory;
import saga.com.mypractise.model.UserMessage;
import saga.com.mypractise.request.LoginRequest;
import saga.com.mypractise.utils.SharedPreferencesHelper;
import saga.com.mypractise.view.iview.ILoginView;

/**
 * Created by lp on 2018/6/22.
 */

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView iLoginView;
    Handler handler;

    public LoginPresenterImpl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void login(String username, String pwd) {
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(pwd)){
            iLoginView.onLoginResult(false,"用户名或密码不能为空");
            return;
        }
        LoginRequest loginRequest = RetrofitFactory.getInstence().getRetrofit().create(LoginRequest.class);
        Call<ResponseBody> call = loginRequest.login(username,pwd);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("login onResponse","========="+response);
                String htmlStr = null;
                try {
                    htmlStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UserMessage userMessage = gson.fromJson(htmlStr, UserMessage.class);
                if (userMessage!=null &&userMessage.isSuccess()){
                        SharedPreferencesHelper helper = new SharedPreferencesHelper(MyApplication.getApplication(), "user");
                        helper.put("userId", userMessage.getUser_id());
                        helper.put("deptId", userMessage.getDept_id());
                        helper.put("roleId", userMessage.getRole_id());
                        helper.put("orideptId",userMessage.getDept_id());
                        helper.put("isGly",userMessage.getIsGly());
                        iLoginView.onLoginResult(true,userMessage);
                }else{
                    iLoginView.onLoginResult(false,"用户名或密码不对");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("login onFailure","========="+t);
            }
        });

    }
}
