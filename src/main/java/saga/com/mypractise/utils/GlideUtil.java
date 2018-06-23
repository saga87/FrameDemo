package saga.com.mypractise.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import saga.com.mypractise.MyApplication;
import saga.com.mypractise.R;


public class GlideUtil {
    /**
     * gilde 获取图片
     */
    public static void getBitmap(String url, ImageView imageView) {
        Glide.with(MyApplication.getApplication()).load(url)
                .placeholder(R.drawable.dengdai)
                .error(R.drawable.error)
                .skipMemoryCache(false)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    /**
     *
     * @param url
     * @param imageView
     */
    public static void getIconBitmap(String url, ImageView imageView) {
        Glide.with(MyApplication.getApplication()).load(url)
                .error(R.drawable.error)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void getDefaultIconBitmap(int url, ImageView imageView) {
        Glide.with(MyApplication.getApplication()).load(url)
                .error(R.drawable.error)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }




}
