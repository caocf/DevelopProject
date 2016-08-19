package com.xhl.bqlh.view.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.AppConfig.NetWorkConfig;
import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.xhl_library.Base.Anim.AnimType;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商品介绍图片
 * Created by Sum on 16/7/8.
 */
@ContentView(R.layout.fragment_product_extend_info_image)
public class ProductExInfoImageFragment extends BaseAppFragment {

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private ArrayList<String> mUrls;

    @Override
    protected void initParams() {
        String dataUrl = getArguments().getString("images");
//        Logger.v("url:" + dataUrl);
        ArrayList<String> urls = new ArrayList<>();
        mUrls = urls;
        //匹配图片
        Matcher matcher = Pattern.compile("src=\".+?\"").matcher(dataUrl);
        while (matcher.find()) {
            String url = dataUrl.substring(matcher.start(), matcher.end());
            //截取
            int start = url.indexOf("\"");
            String newUrl = url.substring(start + 1, url.length() - 1);
            urls.add(NetWorkConfig.imageHost + newUrl);
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();
        int i = 0;
        for (String url : urls) {
            holders.add(new ImageDataHolder(url, i++));
        }
        if (holders.size() == 0) {
            tv_text_null.setVisibility(View.VISIBLE);
        }
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(), holders));
    }

    class ImageDataHolder extends RecyclerDataHolder<String> {
        private int type;

        public ImageDataHolder(String data, int type) {
            super(data);
            this.type = type;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
            return new ImageViewHolder(View.inflate(context, R.layout.item_ex_product_image, null));
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, final String data) {
            ImageViewHolder holder = (ImageViewHolder) vHolder;
            holder.onBind(data);
        }

        class ImageViewHolder extends RecyclerViewHolder {


            private ImageView imageView;
            private String mUrl;

            public ImageViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.image);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSumContext().pushFragmentToBackStack(ImageDetails2Fragment.class, mUrl, AnimType.ANIM_CENTER);
                    }
                });
            }

            public void onBind(String url) {
                if (TextUtils.isEmpty(mUrl)) {
                    mUrl = url;
                    x.image().loadDrawable(url, LifeCycleImageView.imageOptions, new Callback.CommonCallback<Drawable>() {
                        @Override
                        public void onSuccess(Drawable result) {
                            if (result != null) {
                                //解决图片大小缩放不一致问题

                                int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
                                int intrinsicWidth = result.getIntrinsicWidth();
                                int autoHeight = (int) (result.getIntrinsicHeight() * 1.0f / intrinsicWidth * screenWidth);
                                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                                layoutParams.width = screenWidth;
                                layoutParams.height = autoHeight;
                                imageView.setLayoutParams(layoutParams);

                                imageView.setImageDrawable(result);
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            }
        }
    }
}
