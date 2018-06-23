package saga.com.mypractise.presenter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saga.com.mypractise.model.PostList;
import saga.com.mypractise.request.CircleRequest;
import saga.com.mypractise.retrofit.RetrofitFactory;
import saga.com.mypractise.utils.JsonGson;
import saga.com.mypractise.view.iview.ICircleView;

/**
 * Created by lp on 2018/6/23.
 */

public class CirclePresenterImpl implements ICirclePresenter {
    private ICircleView iCircleView;
    private int pagesize = 10;

    public CirclePresenterImpl (ICircleView view){
        iCircleView = view;
    }

    @Override
    public void getCircleList(int page, String deptId) {
        CircleRequest request = RetrofitFactory.getInstence().getRetrofit().create(CircleRequest.class);
        Call<ResponseBody> call = request.getCircleList(page, pagesize, deptId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String htmlStr = null;
                try {
                    htmlStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PostList list = JsonGson.getObject(htmlStr, PostList.class);
                iCircleView.onCircleList(list);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
