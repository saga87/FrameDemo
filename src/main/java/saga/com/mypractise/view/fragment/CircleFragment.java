package saga.com.mypractise.view.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import saga.com.mypractise.R;
import saga.com.mypractise.model.PostList;
import saga.com.mypractise.presenter.CirclePresenterImpl;
import saga.com.mypractise.presenter.ICirclePresenter;
import saga.com.mypractise.utils.SharedPreferencesHelper;
import saga.com.mypractise.view.adapter.PostAdapter;
import saga.com.mypractise.view.iview.ICircleView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends Fragment implements ICircleView {
    @BindView(R.id.recyclerview_post)
    RecyclerView recyclerviewPost;
    @BindView(R.id.refresh_post)
    TwinklingRefreshLayout refreshPost;
    Unbinder unbinder;
    private View view;
    private String deptId;
    private PostList list;
    private ICirclePresenter iCirclePresenter;
    private PostAdapter adapter;


    private int page = 1;
    private boolean flag = true;

    public CircleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_circle, container, false);
        unbinder = ButterKnife.bind(this, view);
        SharedPreferencesHelper helper = new SharedPreferencesHelper(getContext(), "user");
        deptId = (String) helper.getSharedPreference("deptId", "");
        iCirclePresenter = new CirclePresenterImpl(this);
        iCirclePresenter.getCircleList(page, deptId);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCircleList(Object object) {
        list = (PostList) object;
        if (flag) {
            setRecyclerAdapter(list.getList());
        } else {
            if (page == 1) {
                adapter.replaceData(list.getList());
            } else {
                adapter.addData(list.getList());
            }
        }
    }

    private void setRecyclerAdapter(List<PostList.ListBean> list) {
        recyclerviewPost.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(list, getContext());
        recyclerviewPost.setAdapter(adapter);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //动画默认只执行一次,如果想重复执行可设置
        adapter.isFirstOnly(false);
        flag = false;
        refreshPost.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        iCirclePresenter.getCircleList(page, deptId);
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
                        iCirclePresenter.getCircleList(page, deptId);
                        refreshLayout.finishLoadmore();
                    }
                }, 500);
            }
        });
    }
}
