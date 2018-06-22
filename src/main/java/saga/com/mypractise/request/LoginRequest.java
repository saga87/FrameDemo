package saga.com.mypractise.request;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lp on 2018/6/22.
 */

public interface LoginRequest {
    @POST("wkrjsystem/wkrjlogin/checkLogin_mobile")
    Call<ResponseBody> login(@Query("username") String username, @Query("password") String pwd);
}
