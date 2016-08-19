package com.xhl.bqlh.business.view.ui.activity;

import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.TaskRecord;
import com.xhl.bqlh.business.Db.TaskShop;
import com.xhl.bqlh.business.Model.App.TemporaryTask;
import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.event.SelectTaskEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.TemporaryTaskDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/4/19.
 */
@ContentView(R.layout.activity_add_temporary_task)
public class AddTemporaryTaskActivity extends BaseAppActivity implements Toolbar.OnMenuItemClickListener {


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.ed_input)
    private EditText editText;

    @ViewInject(R.id.spinner)
    private AppCompatSpinner spinner;

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    private RecyclerAdapter mAdapter;

    private List<TemporaryTask> mAllShops;

    private List<TemporaryTask> mCurShops;

    private List<TemporaryTask> mShops;

    private int mToday;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_clear);
        setTitle(R.string.temporary_task);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        loadShop();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByWeek(position);
                ViewHelper.KeyBoardHide(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    show(mCurShops);
                } else {
                    List<TemporaryTask> temp = new ArrayList<>();
                    List<TemporaryTask> allShops = mCurShops;
                    if (allShops == null) {
                        return;
                    }
                    for (TemporaryTask task : allShops) {
                        TaskShop taskShop = task.getTaskShop();
                        if (taskShop.getShopName().contains(s)) {
                            temp.add(task);
                        }
                    }
                    //显示临时数据
                    show(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadShop() {
        //今天所对应的星期
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        //数据库存储week和Calendar一致
        mToday = day;
        List<TaskShop> notInDay = DbTaskHelper.getInstance().getTaskShopNotInDay(day);
        if (notInDay == null) {
            mAllShops = new ArrayList<>();
            return;
        }

        //过滤已经选过的店铺
        List<TaskRecord> records = DbTaskHelper.getInstance().getTemporaryByEndTime();
        if (records != null) {
            for (TaskRecord record : records) {
                String shopId = record.getShopId();
                for (TaskShop shop : notInDay) {
                    String sId = shop.getShopId();
                    if (shopId.equals(sId)) {
                        notInDay.remove(shop);
                        break;
                    }
                }
            }
        }

        //创建勾选店铺数据
        List<TemporaryTask> tasks = new ArrayList<>();
        for (TaskShop shop : notInDay) {
            TemporaryTask task = new TemporaryTask(shop);
            tasks.add(task);
        }
        //记录首次加载的数据
        mAllShops = tasks;
    }


    private void filterByWeek(int week) {
        List<TemporaryTask> temp = new ArrayList<>();
        List<TemporaryTask> allShops = this.mAllShops;
        if (allShops == null) {
            return;
        }
        //全部数据
        if (week == 0) {
            temp.addAll(allShops);
        } else {
            for (TemporaryTask task : allShops) {
                TaskShop taskShop = task.getTaskShop();
                if (taskShop.getWeek() == week) {
                    temp.add(task);
                }
            }
        }
        mCurShops = temp;
        //显示临时数据
        show(temp);
    }


    private void show(List<TemporaryTask> tasks) {
        mShops = tasks;
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (TemporaryTask task : tasks) {
            TemporaryTaskDataHolder holder = new TemporaryTaskDataHolder(task);
            holders.add(holder);
        }
        if (holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        mAdapter.setDataHolders(holders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mToolbar.inflateMenu(R.menu.menu_commit);
        mToolbar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_commit:
                if (mShops == null || mShops.size() == 0) {
                    SnackUtil.shortShow(mToolbar, "没有可添加的店铺");
                } else {
                    List<TaskShop> taskShops = new ArrayList<>();
                    List<TemporaryTask> shops = this.mShops;
                    boolean exitToay = false;
                    //获取选择的数据
                    for (TemporaryTask shop : shops) {
                        if (shop.isChecked()) {
                            TaskShop taskShop = shop.getTaskShop();
                            //当天任务不添加到临时中
                            if (taskShop.getWeek() == mToday) {
                                exitToay = true;
                            } else {
                                taskShops.add(taskShop);
                            }
                        }
                    }
                    if (exitToay) {
                        ToastUtil.showToastShort("当天拜访任务不能添加为临时任务");
                    }
                    if (taskShops.size() == 0) {
                        if (!exitToay) {
                            SnackUtil.shortShow(mToolbar, "请勾选店铺");
                        } else {
                            SnackUtil.shortShow(mToolbar, "请重新勾选店铺");
                        }
                        return true;
                    }
                    //设置临时任务信息
                    for (TaskShop task : taskShops) {
                        task.setTaskState(TaskType.STATE_START);
                        task.setTaskType(TaskType.TYPE_Temporary);
                        Date date = DbTaskHelper.getInstance().addTemporaryTask(task.getShopId());
                        int taskId = DbTaskHelper.getInstance().getTemporaryTaskId(date);
                        //根据这个id删除表数据
                        task.setTaskId(taskId);
                    }
                    SelectTaskEvent event = new SelectTaskEvent();
                    event.shops = taskShops;
                    event.type = SelectTaskEvent.type_add;
                    EventBus.getDefault().post(event);

                    AddTemporaryTaskActivity.this.finish();
                }
                return true;
        }
        return false;
    }
}
