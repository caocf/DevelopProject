package com.ebox.ex.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.enums.BoxUserState;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;

public class CloseDoorActivity extends CommonActivity implements
        OnClickListener {
    public static final String TAG = "ClooseDoorActivity";
    public static String Flag = "flag";
    public static int Login = 0;
    public static int pickUp = 1;

    private static Handler handler;
    private static Runnable run;
    private int checkTime = 0;
    private ArrayList<BoxInfo> mList;
    private static int curBox;
    private String hint;
    private static Button bt_ok, bt_report;
    private TextView tv_door;

    private DialogUtil dialogUtil;
    private AlertDialog mDialog;

    private int tag = 0;// 0 派件，1 取件
    private boolean isReported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_cloose_door);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        tag = getIntent().getIntExtra(Flag, -1);
        isReported = false;
        init();
        cantOp();
        getAllOpenDoor();

    }

    private void init() {
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
        bt_report = (Button) findViewById(R.id.bt_report);
        bt_report.setOnClickListener(this);
        tv_door = (TextView) findViewById(R.id.tv_door);
        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(this);

        findViewById(R.id.tv_timer).setOnClickListener(this);
        initTitle();
    }

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 0;
        data.tvContent = getResources().getString(R.string.ex_hint_cloose_door);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    // 读取数据库缓存的门的信息
    private void getAllOpenDoor() {

        mList = new ArrayList<BoxInfo>();

        ArrayList<BoxInfo> list = BoxDynSyncOp.getAllSyncBoxList();
        for (BoxInfo boxInfo : list) {
            Integer doorState = boxInfo.getDoorState();
            if (boxInfo.getBoxUserState() == BoxUserState.normal)
            {
                if (doorState == DoorStatusType.open)
                {
                    mList.add(boxInfo);
                }
            }
        }


        curBox = 0;
        if (mList.size() > 0) {
            BoxInfo boxInfo = mList.get(0);
            hint = boxInfo.getBoardNum() + "组" + boxInfo.getBoxNum() + "号门";
            tv_door.setText(hint);
            check();
        } else {
            start();
        }
    }

    private void canOp() {
        bt_ok.setEnabled(true);
        bt_ok.setVisibility(View.VISIBLE);
        //bt_ok.setText("关    闭");
        bt_ok.setTextColor(getResources().getColor(R.color.pub_white));
    }

    private void cantOp() {
        bt_ok.setEnabled(false);
        bt_ok.setVisibility(View.INVISIBLE);
        //bt_ok.setText("请您关闭所有箱门");
        //bt_ok.setTextColor(getResources().getColor(R.color.black));
    }

    private void check() {
        // 定时检测箱门状态
        handler = new MyHandler();
        run = new Runnable() {
            @Override
            public void run() {
                if (mList == null) {
                    handler.removeCallbacks(run);
                    return;
                }
                if (curBox >= mList.size()) {
                    if (!isReported) {
                        start();
                    } else {
                        canOp();
                    }
                    return;
                }
                BoxInfo boxInfo = mList.get(curBox);

                if (boxInfo.getBoxUserState() == BoxUserState.normal) {
                    if (boxInfo != null) {
                        // 获取箱门状态
                        BoxOp op = new BoxOp();
                        op.setOpId(BoxOpId.GetDoorsStatus);
                        op.setOpBox(boxInfo);
                        BoxCtrlTask.addBoxCtrlQueue(op);

                        hint = "" + boxInfo.getBoardNum() + "组" + boxInfo.getBoxNum() + "号门";
                        //	Log.i("main", "当前：" +  hint+",开门个数["+mList.size()+"]");
                        tv_door.setText(hint);
                        // 箱门关闭
                        if (BoxUtils.getBoxLocalState(boxInfo) == DoorStatusType.close) {
                            checkTime++;
                            Log.i(GlobalField.tag, "Box close");
//                            if (checkTime < 0) {
//                                handler.sendEmptyMessage(0);
//                            } else {
//                            }
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler != null) {
            handler.removeCallbacks(run);
            canOp();
        }
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                this.postDelayed(run, 1000);
            } else if (msg.what == 1) {
                curBox++;
                this.postDelayed(run, 1000);
            } else if (msg.what == 6) {
            }
        }
    }

    private TextView description;
    private TextView tv_hint;

    private void report() {

        mDialog = new AlertDialog.Builder(this).create();
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialog.setCancelable(false);
        Window window = mDialog.getWindow();
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.pub_confirm_dialog, null);
        window.setContentView(v);
        MGViewUtil.scaleContentView((ViewGroup) v);
        description = (TextView) window.findViewById(R.id.description);
        tv_hint = (TextView) window.findViewById(R.id.tv_hint);
        tv_hint.setVisibility(View.VISIBLE);
        description.setText("上报：" + hint + "故障");
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isclick = false;
                mDialog.dismiss();
                AppService.getIntance().hasOnKeyDown();
            }
        });
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDialog.cancel();
                isclick = false;
                AppService.getIntance().hasOnKeyDown();
            }
        });
    }


    private boolean isclick = false;
    public int clickTimes = 0;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_ok) {
            start();
        } else if (v.getId() == R.id.bt_report) {
            if (!isclick) {
                isclick = true;
                report();
            }
        }else if(v.getId() == R.id.tv_timer){
            clickTimes++;
            if (clickTimes>=6) {
                start();
            }
        }
    }

    private void start() {
        if (tag == Login) {
            Intent intent = null;
            if (GlobalField.config.getDot() == DotType.HENGXI) {
                intent = new Intent(CloseDoorActivity.this, OperatorLoginByCardActivity.class);

            } else {
                intent = new Intent(CloseDoorActivity.this, OperatorLoginActivity.class);
            }
            startActivity(intent);
        } else if (tag == pickUp) {
            Intent intent = new Intent(CloseDoorActivity.this, PickupActivity.class);
            startActivity(intent);
        }
        this.finish();
    }

}
