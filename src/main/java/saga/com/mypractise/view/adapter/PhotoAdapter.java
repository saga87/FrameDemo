package saga.com.mypractise.view.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import saga.com.mypractise.R;


public class PhotoAdapter extends BaseQuickAdapter<Bitmap, BaseViewHolder> {

    public PhotoAdapter(List data) {
        super(R.layout.item_cphoto, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bitmap item) {
        ImageView iv = helper.getView(R.id.iv_item_photo);
        iv.setImageBitmap(item);
    }
}
