package saga.com.mypractise.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import saga.com.mypractise.R;
import saga.com.mypractise.dao.FriendDaoImpl;
import saga.com.mypractise.model.Friend;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    @BindView(R.id.myfriend)
    TextView myfriend;
    Unbinder unbinder;
    @BindView(R.id.friends)
    TextView tv_friends;
    private View view;
    private List<Friend> friends;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        friends = FriendDaoImpl.queryAll();
        StringBuffer sb = new StringBuffer();
        for (Friend f : friends) {
            sb.append(f.getName()).append("     ");
        }
        tv_friends.setText(sb.toString());
        sb.setLength(0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            initView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
