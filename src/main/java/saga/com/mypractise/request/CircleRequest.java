package saga.com.mypractise.request;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lp on 2018/6/23.
 */

public interface CircleRequest {
    @POST("wkrj/mobile/interactivepost/m_postlist")
    Call<ResponseBody> getCircleList(@Query("page") int page, @Query("pagesize") int pagesize, @Query("dept_id") String dept_id);
}
