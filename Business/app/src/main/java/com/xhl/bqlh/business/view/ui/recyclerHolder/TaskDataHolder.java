package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.TaskShop;
import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/4/18.
 */
public class TaskDataHolder extends RecyclerDataHolder<TaskShop> {

    private RecycleViewCallBack callBack;

    public TaskDataHolder(TaskShop data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_task, null);
        int hight = context.getResources().getDimensionPixelOffset(R.dimen.task_item_height);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, hight));
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, TaskShop data) {
        TaskViewHolder holder = (TaskViewHolder) vHolder;
        holder.onBindData(data, position);
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }

    class TaskViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.iv_shop)
        private ImageView iv_shop;

        @ViewInject(R.id.iv_state)
        private ImageView iv_state;

        @ViewInject(R.id.iv_state_finish)
        private ImageView iv_state_finish;

        @ViewInject(R.id.tv_task_shop_name)
        private TextView tv_task_shop_name;

        @ViewInject(R.id.tv_task_shop_location)
        private TextView tv_task_shop_location;

        @ViewInject(R.id.tv_task_state)
        private TextView tv_task_state;

        @ViewInject(R.id.tv_task_type)
        private TextView tv_task_type;

        @ViewInject(R.id.tv_task_shop_index)
        private TextView tv_task_shop_index;

        @ViewInject(R.id.line)
        private View line;

        private TaskShop mShop;

        public TaskViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
            view.setOnClickListener(this);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClick(v);
                    return true;
                }
            });
        }

        private void longClick(View view) {
            if (mShop == null) {
                return;
            }
            //临时计划并且未完成
            if (mShop.getTaskType() == TaskType.TYPE_Temporary && mShop.getTaskState() != TaskType.STATE_FINISH) {
                AlertDialog.Builder dialog = DialogMaker.getDialog(view.getContext());
                dialog.setTitle("删除计划");
                dialog.setMessage("确定删除该条临时计划?");
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbTaskHelper.getInstance().deleteTaskRecord(mShop.getTaskId());
                        if (callBack != null) {
                            callBack.onItemClick(getAdapterPosition());
                        }
                    }
                });
                dialog.setNegativeButton(R.string.dialog_cancel, null);
                dialog.show();
            }
        }

        public void onBindData(TaskShop shop, int pos) {
            if (shop == null) {
                return;
            }
            tv_task_shop_index.setText(NumberUtil.fixNumber(pos + 1));
            mShop = shop;
            tv_task_shop_name.setText(shop.getShopName());

            tv_task_shop_location.setText(TextUtils.concat("地址：", shop.getShopLocation()));
            //拜访状态
            tv_task_state.setText(taskState(shop.getTaskState()));
            //拜访显示
            setStyle(shop.getTaskState(), tv_task_state);
//            setHintText(shop.getTaskState(), tv_task_shop_index);
//            setHintText(shop.getTaskState(), tv_task_shop_name);

            //拜访状态
            setHintImage(shop.getTaskState(), iv_state);
            //拜访状态图片
            setFinishStateImage(shop.getTaskState(), iv_state_finish);

            if (shop.getTaskType() == TaskType.TYPE_Temporary) {
                tv_task_type.setVisibility(View.VISIBLE);
            } else {
                tv_task_type.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if (mShop != null) {
                EventHelper.postTaskShop(mShop);
            }
        }


        public void setHintImage(int state, ImageView imageView) {
            Drawable drawable = imageView.getDrawable();
            if (state == TaskType.STATE_FINISH) {
                DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.app_light_green));
            } else {
                DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.app_light_grey1));
            }
        }

        public void setFinishStateImage(int state, ImageView imageView) {
            if (state == TaskType.STATE_FINISH) {
                imageView.setImageResource(R.drawable.ic_task_finish);
                ViewHelper.setViewGone(imageView, false);
                ViewHelper.setViewGone(line, false);
            } else if (state == TaskType.STATE_UN_FINISH) {
                ViewHelper.setViewGone(imageView, false);
                ViewHelper.setViewGone(line, false);
                imageView.setImageResource(R.drawable.ic_task_finish_not);
            } else {
                ViewHelper.setViewGone(imageView, true);
                ViewHelper.setViewGone(line, true);
            }
        }

        public void setHintText(int state, TextView textView) {
            if (state == TaskType.STATE_FINISH) {
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.colorPrimary));
            } else {
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.base_dark_text_color));
            }
        }


        public void setStyle(int state, TextView textView) {
            if (state == TaskType.STATE_FINISH) {
                textView.setBackgroundColor(ContextCompat.getColor(textView.getContext(), R.color.transparent));
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.app_orange));
                ViewHelper.setViewGone(textView, false);
            } else if (state == TaskType.STATE_UN_FINISH) {
                textView.setBackgroundColor(ContextCompat.getColor(textView.getContext(), R.color.transparent));
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.base_light_text_color));
                ViewHelper.setViewGone(textView, false);
            } else if (state == TaskType.STATE_UN_START) {
                ViewHelper.setViewGone(textView, true);
                ViewHelper.setViewGone(line, true);
            } else if (state == TaskType.STATE_START) {
                textView.setBackgroundColor(ContextCompat.getColor(textView.getContext(), R.color.transparent));
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.colorAccent));
                ViewHelper.setViewGone(textView, false);
            }
        }

        public String taskState(int state) {
            String res = "";
            if (state == TaskType.STATE_START) {
                res = "";
            } else if (state == TaskType.STATE_FINISH) {
                res = "已拜访";
            } else if (state == TaskType.STATE_UN_START) {
                res = "未开始";
            } else if (state == TaskType.STATE_UN_FINISH) {
                res = "未拜访";
            }
            return res;
        }


    }

}
