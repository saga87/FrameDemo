package saga.com.mypractise.presenter;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saga.com.mypractise.model.NewsList;
import saga.com.mypractise.request.HeadRequest;
import saga.com.mypractise.retrofit.RetrofitFactory;
import saga.com.mypractise.utils.JsonGson;
import saga.com.mypractise.view.iview.IheadView;

/**
 * Created by lp on 2018/6/23.
 */

public class HeadPresenterImpl implements IheadPresenter {

    private IheadView view;
    private int pagesize = 10;

    public HeadPresenterImpl(IheadView iheadView) {
        view = iheadView;
    }

    @Override
    public void getNewsList(int page, String deptId) {
        HeadRequest request = RetrofitFactory.getInstence().getRetrofit().create(HeadRequest.class);
        Call<ResponseBody> call = request.getNewsList(page, pagesize, deptId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String htmlStr = null;
                try {
                    htmlStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                NewsList list = JsonGson.getObject(htmlStr, NewsList.class);
                view.onNewsList(list);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
