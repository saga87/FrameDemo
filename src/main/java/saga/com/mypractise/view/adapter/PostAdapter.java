package saga.com.mypractise.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import saga.com.mypractise.MyApplication;
import saga.com.mypractise.R;
import saga.com.mypractise.dao.FriendDaoImpl;
import saga.com.mypractise.model.Friend;
import saga.com.mypractise.model.PostList;
import saga.com.mypractise.retrofit.HttpConfig;
import saga.com.mypractise.utils.DateUtil;
import saga.com.mypractise.utils.GlideUtil;
import saga.com.mypractise.utils.SharedPreferencesHelper;


public class PostAdapter extends BaseQuickAdapter<PostList.ListBean, BaseViewHolder> {
    private Context context;
    private String userId;
    private AlertDialog.Builder addDialog;

    public PostAdapter(List data, Context ctx) {
        super(R.layout.item_npost, data);
        context = ctx;
        SharedPreferencesHelper helper = new SharedPreferencesHelper(MyApplication.getApplication(), "user");
        userId = (String) helper.getSharedPreference("userId", "");
    }

    @Override
    protected void convert(BaseViewHolder helper, final PostList.ListBean item) {
        String pic = item.getHeadpic();
        if (null == pic || "".equals(pic.trim())) {
            GlideUtil.getDefaultIconBitmap(R.drawable.mydefault, (ImageView) helper.getView(R.id.iv_touxiang_npost));
        } else {
            GlideUtil.getIconBitmap(HttpConfig.BASE_URL + pic, (ImageView) helper.getView(R.id.iv_touxiang_npost));
        }

        helper.getView(R.id.iv_touxiang_npost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userId.equals(item.getInput_user())) {
                    addDialog = new AlertDialog.Builder(mContext);
                    addDialog.setMessage("您是否要添加" + item.getUser_name() + "为好友?");
                    addDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Friend f = new Friend();
                            f.setName(item.getUser_name());
                            f.setSchool(item.getDept_name());
                            FriendDaoImpl.insertFriend(f);
                        }
                    });
                    addDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addDialog.create();
                        }
                    });
                    addDialog.show();
                }
            }
        });

        helper.setText(R.id.item_tv_post_title, item.getPost_title());
        helper.setText(R.id.item_tv_post_contact, item.getUser_name());

        if (item.getDept_name() == null || "".equals(item.getDept_name())) {
            helper.setText(R.id.item_tv_post_time, DateUtil.getDateToString(item.getInput_time(), "yyyy-MM-dd"));
        } else {
            helper.setText(R.id.item_tv_post_time, DateUtil.getDateToString(item.getInput_time(), "yyyy-MM-dd") + " | "
                    + item.getDept_name());
        }


        GridView gv = helper.getView(R.id.item_gv_post);
        String str = item.getPic_url();
        List<String> imgs = new ArrayList<>();
        if (str != null && !str.equals("")) {
            String[] strs = str.split(",");
            for (int i = 0, len = strs.length; i < len; i++) {
                imgs.add(strs[i]);
            }
            if (imgs.size() > 0) {
                SmallImageAdapter adapter = new SmallImageAdapter(imgs, context);
                gv.setAdapter(adapter);
            } else {
                imgs.clear();
            }
        } else {
            imgs.clear();
            SmallImageAdapter adapter = new SmallImageAdapter(imgs, context);
            gv.setAdapter(adapter);
        }
    }
}
