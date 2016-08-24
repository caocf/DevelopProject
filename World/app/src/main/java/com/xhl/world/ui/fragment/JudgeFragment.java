package com.xhl.world.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.EvaluateModel;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.activity.ImageDetailsActivity;
import com.xhl.world.ui.activity.bar.OrderManagerItemChildBar;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.PermissionCheck;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.UploadHelper;
import com.xhl.xhl_library.AppFileConfig;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.BitmapUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/11.
 */
@ContentView(R.layout.fragment_judge)
public class JudgeFragment extends BaseAppFragment {
    private Order mOrder;


    @ViewInject(R.id.title_name)
    private TextView title_name;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        backDo();
    }

    private void backDo() {
        String title = getString(R.string.dialog_title);
        String msg = "您确定要放弃评价么";
        DialogMaker.showAlterDialog(getContext(), title, msg, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSumContext().popTopFragment(null);
            }
        });
    }

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;//字订单详细内容

    @ViewInject(R.id.ll_add_image_content)
    private LinearLayout ll_add_image_content;//图片缩略图

    @ViewInject(R.id.rating_bar_product)
    private RatingBar rating_bar_product;

    @ViewInject(R.id.rating_bar_service)
    private RatingBar rating_bar_service;

    @ViewInject(R.id.rating_bar_logistics)
    private RatingBar rating_bar_logistics;

    @ViewInject(R.id.ed_judge_content)
    private EditText ed_judge_content;

    @ViewInject(R.id.rl_add_image)
    private RelativeLayout rl_add_image;

    @Event(value = R.id.ripple_commit, type = RippleView.OnRippleCompleteListener.class)
    private void onLoginClick(View view) {
        commit();
    }

    @Event(R.id.rl_add_image)
    private void onAddImage(View view) {
        showCamera();
    }

    private ArrayList<String> mImagesFile = new ArrayList<>();

    private float mScore1 = 0;
    private float mScore2 = 0;
    private float mScore3 = 0;
    private File mCureFile;
    private EvaluateModel mEvaluate;

    @Override
    protected void initParams() {

        title_name.setText("发表评价");

        addChildView();
        rating_bar_product.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mScore1 = rating;
            }
        });
        rating_bar_service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mScore2 = rating;
            }
        });
        rating_bar_logistics.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mScore3 = rating;
            }
        });

    }

    //一次只能评价一个商品
    private void addChildView() {
        List<OrderDetail> childProducts = mOrder.getOrderDetailList();
        OrderManagerItemChildBar childBar = null;
        OrderDetail child = childProducts.get(0);//第一个为点击的商品数据
//        for (OrderDetail child : childProducts) {
        childBar = new OrderManagerItemChildBar(getContext());
        childBar.setChildOrderInfo(child);
        childBar.setArrowShow(false);

        int height = getResources().getDimensionPixelSize(R.dimen.item_order_height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, height);
        childBar.setLayoutParams(params);
        AutoUtils.autoSize(childBar);

        ll_content.addView(childBar);
//        }
        if (childBar != null) {
            childBar.hideLine();
        }
    }

    private void commit() {

        String content = ed_judge_content.getEditableText().toString().trim();

        if (mScore1 == 0 || mScore2 == 0 || mScore3 == 0) {
            SnackMaker.shortShow(title_name, "亲，请给我们的产品打个分吧");
            return;
        }

        StringBuilder rate = new StringBuilder();
        rate.append("1:").append((int) mScore1).append(";");
        rate.append("2:").append((int) mScore2).append(";");
        rate.append("3:").append((int) mScore3).append(";");

        if (TextUtils.isEmpty(content) || content.length() < 10) {
            SnackMaker.shortShow(title_name, "亲，请多写点您的吧");
            return;
        }

        mEvaluate = new EvaluateModel();
        //设置评分
        mEvaluate.setStoreOrderCode(mOrder.getStoreOrderCode());
        mEvaluate.setRate(rate.toString());
        mEvaluate.setRateContent(content);
        mEvaluate.setCompanyId(mOrder.getCompanyId());
        OrderDetail detail = mOrder.getOrderDetailList().get(0);
        mEvaluate.setGoodsId(detail.getGoodId());

        //图片添加上传
        showLoadingDialog();

        if (mImagesFile.size() > 0) {
            UploadHelper.uploadFile(mImagesFile, new UploadHelper.uploadFileListener() {
                @Override
                public void onSuccess(String ftpFile) {
                    commitData(ftpFile);
                }

                @Override
                public void onFailed(String msg) {
                    hideLoadingDialog();
                    SnackMaker.shortShow(title_name, msg);
                }
            });
        } else {
            commitData("");
        }

    }

    //添加图片地址
    private void commitData(String ftpFile) {
        mEvaluate.setRateImg(ftpFile);

        ApiControl.getApi().orderEvaluate(mEvaluate, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    SnackMaker.shortShow(title_name, result.getResultObj());
                    getSumContext().popTopFragment(null);
                } else {
                    SnackMaker.shortShow(title_name, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(title_name, R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });

    }


    private File getStoreFile() {
        File takePhoneFile = AppFileConfig.getTakePhoneFile();
        String curFilename = String.valueOf(System.currentTimeMillis()) + ".jpg";
        mCureFile = new File(takePhoneFile, curFilename);
        return mCureFile;
    }

    private void showCamera() {

        if (PermissionCheck.CameraHasPermission(getActivity())) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getStoreFile()));
            startActivityForResult(intent, 11);
        } else {
            PermissionCheck.CameraReq(getActivity(), 10);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != -1) {
            return;
        }

        if (requestCode != 11) {
            return;
        }
        Bitmap bitmap = BitmapUtil.readBitmapForFixMaxSize(mCureFile.getPath());

        if (bitmap == null) {
            return;
        }
        final ImageView imageView = new ImageView(getContext());

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setImageBitmap(bitmap);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
        params.leftMargin = 10;

        imageView.setLayoutParams(params);

        imageView.setTag(mCureFile.getPath());

        AutoUtils.autoSize(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                click(v);
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                String title = getString(R.string.dialog_title);
                String msg = "您确定要删除该图片吗？";
                DialogMaker.showAlterDialog(getContext(), title, msg, null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        longClick(v);
                    }
                });
                return true;
            }
        });

        ll_add_image_content.addView(imageView);

        mImagesFile.add(mCureFile.getPath());

        if (mImagesFile.size() == 4) {
            rl_add_image.setVisibility(View.GONE);
        }
    }

    private void longClick(View view) {

        ll_add_image_content.removeView(view);
        String file = (String) view.getTag();
        boolean remove = mImagesFile.remove(file);

        if (remove) {
            SnackMaker.shortShow(title_name, "删除成功");
        }
    }

    private void click(View view) {
        String file = (String) view.getTag();
        Intent intent = new Intent(getContext(), ImageDetailsActivity.class);

        intent.putExtra(ImageDetailsActivity.Image_source, file);

        intent.putExtra(ImageDetailsActivity.Image_type, ImageDetailsActivity.Image_file);
        getActivity().startActivity(intent);
    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof Order) {
            mOrder = (Order) data;
        }
    }

    @Override
    public boolean processBackPressed() {
        backDo();
        return true;
    }
}
