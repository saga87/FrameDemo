package saga.com.mypractise.view.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import saga.com.mypractise.R;
import saga.com.mypractise.model.NewsList;
import saga.com.mypractise.presenter.HeadPresenterImpl;
import saga.com.mypractise.presenter.IheadPresenter;
import saga.com.mypractise.utils.SharedPreferencesHelper;
import saga.com.mypractise.view.adapter.NewsListAdapter;
import saga.com.mypractise.view.iview.IheadView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadFragment extends Fragment implements IheadView {
    @BindView(R.id.recyclerview_head)
    android.support.v7.widget.RecyclerView recyclerviewHead;
    @BindView(R.id.refresh_head)
    com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout refreshHead;
    Unbinder unbinder;
    private View view;
    private String deptId;
    private IheadPresenter iheadPresenter;
    private NewsListAdapter adapter;
    private int page = 1;
    private NewsList list;
    private boolean flag = true;

    public HeadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_head, container, false);
        unbinder = ButterKnife.bind(this, view);
        SharedPreferencesHelper helper = new SharedPreferencesHelper(getContext(), "user");
        deptId = (String) helper.getSharedPreference("deptId", "");
        iheadPresenter = new HeadPresenterImpl(this);
        iheadPresenter.getNewsList(page, deptId);//加载第一页新闻数据
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        flag = true;
    }

    @Override
    public void onNewsList(Object object) {
        list = (NewsList) object;
        if (flag) {
            setRecyclerAdapter(list.getList());
        }else{
            if(page == 1) {
                adapter.replaceData(list.getList());
            }else{
                adapter.addData(list.getList());
            }
        }
    }

    private void setRecyclerAdapter(List<NewsList.ListBean> list) {
        recyclerviewHead.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsListAdapter(list);
        recyclerviewHead.setAdapter(adapter);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //动画默认只执行一次,如果想重复执行可设置
        adapter.isFirstOnly(false);
        flag = false;
        refreshHead.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        iheadPresenter.getNewsList(page, deptId);
                        refreshLayout.finishRefreshing();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = page + 1;
                        iheadPresenter.getNewsList(page, deptId);
                        refreshLayout.finishLoadmore();
                    }
                }, 500);
            }
        });
    }


}
