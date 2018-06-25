package saga.com.mypractise.view.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saga.com.mypractise.R;
import saga.com.mypractise.model.Success;
import saga.com.mypractise.request.SendRequest;
import saga.com.mypractise.retrofit.RetrofitFactory;
import saga.com.mypractise.utils.CameraBottomMenu;
import saga.com.mypractise.utils.JsonGson;
import saga.com.mypractise.utils.PictureUtil;
import saga.com.mypractise.utils.SharedPreferencesHelper;
import saga.com.mypractise.utils.Util;
import saga.com.mypractise.view.adapter.PhotoAdapter;

/**
 * 上传图片，发布动态
 */
public class UpFragment extends TakePhotoFragment {
    @BindView(R.id.up_content)
    EditText upContent;
    @BindView(R.id.up_pics)
    RecyclerView upPics;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.cancel)
    Button cancel;
    Unbinder unbinder;
    private View view;
    private PhotoAdapter adapter;
    private AlertDialog.Builder normalDialog;
    private String userId;
    private String deptId;
    private SharedPreferencesHelper helper;
    private ProgressDialog waitingDialog;

    private TakePhoto takePhoto;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;  //图片保存路径
    private List<Bitmap> pics = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> orderurl = new ArrayList<>();

    public UpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    private void initView() {
        helper = new SharedPreferencesHelper(getActivity(), "user");
        userId = (String) helper.getSharedPreference("userId", "");
        deptId = (String) helper.getSharedPreference("deptId", "");
        pics.add(BitmapFactory.decodeResource(getResources(), R.drawable.photo));
        upPics.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PhotoAdapter(pics);
        upPics.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (urls.size() == position) {
                    CameraBottomMenu menuWindow = new CameraBottomMenu(getActivity(), clickListener);
                    menuWindow.show();
                } else {
                    showNormalDialog(position);
                }
            }
        });
    }

    /**
     * 删除图片对话框
     */
    private void showNormalDialog(final int position) {
        normalDialog = new AlertDialog.Builder(getActivity());
        normalDialog.setTitle("删除");
        normalDialog.setMessage("您是否要删除这张图片");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove1(position);
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                normalDialog.create();
            }
        });
        normalDialog.show();
    }

    /**
     * 删除图片
     */
    private void remove1(int position) {
        if (urls.size() > 8) {
            pics.add(BitmapFactory.decodeResource(getResources(), R.drawable.photo));
            urls.remove(urls.size() - 1 - position);
            pics.remove(position);
        } else {
            urls.remove(urls.size() - 1 - position);
            pics.remove(position);
        }
        adapter.replaceData(pics);
    }

    @OnClick({R.id.send, R.id.cancel})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.send:
                send();
                break;
            case R.id.cancel:
                Util.showToast("取消");
                break;
        }
    }

    private void send() {
        SendRequest request = RetrofitFactory.getInstence().getRetrofit().create(SendRequest.class);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId).addFormDataPart("dept_id", deptId)
                .addFormDataPart("content", upContent.getText().toString().trim()).addFormDataPart("type", "5");
        for (int i = 0; i < urls.size(); i++) {
            File f = new File(urls.get(i));
            if (f != null) {
                builder.addFormDataPart("photo" + i, f.getName(), RequestBody.create(MediaType.parse("image/png"), f));
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Call<ResponseBody> call = request.send(parts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String htmlStr = null;
                try {
                    htmlStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Success s = JsonGson.getObject(htmlStr, Success.class);
                EventBus.getDefault().post(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Success s) {
        if (s.isSuccess()) {
            upContent.setText("");
            urls.clear();
            pics.clear();
            pics.add(BitmapFactory.decodeResource(getResources(), R.drawable.photo));
            adapter.replaceData(pics);
            Util.showToast("发布成功");
        } else {
            Util.showToast(s.getMsg());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTakePhoto();
    }

    private void initTakePhoto() {
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(40 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true); //设置为需要压缩
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String iconPath = result.getImage().getOriginalPath();
        String tpath = Environment.getExternalStorageDirectory() + "/temp_practise_ys/" + System.currentTimeMillis() + ".jpg";
        String compressPath = PictureUtil.compressImage(iconPath, tpath, 40);
        if (urls.size() < 10) {
            urls.add(0, compressPath);
            pics.clear();
            for (String s : converse(urls)) {
                pics.add(BitmapFactory.decodeFile(s));
            }
            pics.add(BitmapFactory.decodeResource(getResources(), R.drawable.photo));
            adapter.replaceData(pics);

        } else {
            Util.showToast("最多添加九张");
        }
        if (pics.size() == 10) {
            pics.remove(9);
            adapter.replaceData(pics);
        }
    }

    private List<String> converse(List<String> urls) {
        orderurl.clear();
        for (int i = 0; i < urls.size(); i++) {
            orderurl.add(0, urls.get(i));
        }
        return orderurl;
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    imageUri = getImageCropUri();
                    //不裁剪
                    takePhoto.onPickFromCapture(imageUri);
                    break;
                case R.id.btn2:
                    imageUri = getImageCropUri();
                    //不裁剪
                    takePhoto.onPickFromGallery();
                    break;
            }
        }
    };

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
}
