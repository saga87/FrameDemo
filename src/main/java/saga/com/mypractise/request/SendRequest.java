package saga.com.mypractise.request;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lp on 2018/6/23.
 */

public interface SendRequest {
    @Multipart
    @POST("wkrj/mobile/release/releaseinfo")
    Call<ResponseBody> send(@Part List<MultipartBody.Part> partList);
}
