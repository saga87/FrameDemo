package saga.com.mypractise.view.adapter;


import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import saga.com.mypractise.R;
import saga.com.mypractise.model.NewsList;
import saga.com.mypractise.retrofit.HttpConfig;
import saga.com.mypractise.utils.DateUtil;
import saga.com.mypractise.utils.GlideUtil;

/**
 * Created by lp on 2018/6/23.
 */

public class NewsListAdapter extends BaseQuickAdapter<NewsList.ListBean,BaseViewHolder> {

    public NewsListAdapter( List<NewsList.ListBean> data) {
        super(R.layout.item_frontpage, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsList.ListBean item) {
        List<String> imgs = new ArrayList<>();
        String str = item.getPic_url();
        if(str!=null&&!str.equals("")) {
            String[] strs = str.split(",");
            for (int i = 0, len = strs.length; i < len; i++) {
                imgs.add(strs[i]);
            }
            if (imgs.size() > 0) {
                helper.getView(R.id.iv_pic).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_info).setVisibility(View.VISIBLE);
                helper.getView(R.id.fp_news_contact).setVisibility(View.VISIBLE);
                helper.getView(R.id.fp_news_time).setVisibility(View.VISIBLE);
                String url = HttpConfig.BASE_URL + imgs.get(0);
                GlideUtil.getBitmap(url, (ImageView) helper.getView(R.id.iv_pic));
                helper.setText(R.id.tv_info, item.getNews_title());
                helper.setText(R.id.fp_news_contact, item.getNews_content().trim());
                helper.setText(R.id.fp_news_time, DateUtil.getDateToString(item.getInput_time(),"yyyy-MM-dd"));
            } else {
                imgs.clear();
                helper.getView(R.id.iv_pic).setVisibility(View.GONE);
                helper.getView(R.id.tv_info).setVisibility(View.GONE);
                helper.getView(R.id.fp_news_contact).setVisibility(View.GONE);
                helper.getView(R.id.fp_news_time).setVisibility(View.GONE);
            }
        }else {
            imgs.clear();
            helper.getView(R.id.iv_pic).setVisibility(View.GONE);
            helper.getView(R.id.tv_info).setVisibility(View.GONE);
            helper.getView(R.id.fp_news_contact).setVisibility(View.GONE);
            helper.getView(R.id.fp_news_time).setVisibility(View.GONE);
        }
    }
}
