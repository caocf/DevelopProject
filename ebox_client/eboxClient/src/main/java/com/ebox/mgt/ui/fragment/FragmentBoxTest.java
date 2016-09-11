package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.mgt.ui.utils.BoxOpenController;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.view.MaterialButton;
import com.ebox.pub.utils.DialogUtil;

import java.util.ArrayList;

/**
 * 箱体测试
 */
public class FragmentBoxTest extends Fragment implements View.OnClickListener{

    private View view;
    private MaterialButton openBT;

    private Context mContext;

    private Intent intent = null;

    private EditText boardET;
    private EditText boxET;
    private Button beginOpenTV;

    private RelativeLayout inputRL;
    private RelativeLayout showRL;

    private TextView lastNextTV;

    private BoxOpenController controller;

    private int boardNum;

    private int boxNum;

    private int boxCode;

    private DialogUtil mDialogUtil;

    // 箱门状态汇报
    public static final int BOX_STATUS = 9;//获取副柜状态
    public static final int BOX_OPEN = 10;
    public static final int BOX_CLOSE = 11;
    public static final int BOX_ERROR = 12;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_box_test, null);

        mContext = getActivity();

        initViews();
        return view;
    }

    private void initViews() {
        // TODO Auto-generated method stub


        boardET = (EditText) view.findViewById(R.id.et_boardnum);
        boxET = (EditText) view.findViewById(R.id.et_boxnum);
        boardET.setText("1");
        boxET.setText("22");

        beginOpenTV = (MaterialButton) view.findViewById(R.id.bt_box_test);

        inputRL = (RelativeLayout) view.findViewById(R.id.rl_box_content_input);
        showRL = (RelativeLayout) view.findViewById(R.id.rl_box_content_show);

        beginOpenTV.setOnClickListener(this);

        mDialogUtil = new DialogUtil();
        mDialogUtil.createProgressDialog(getActivity());
    }

    private TextView stateTV;
    private Handler boxHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 箱门打开
                case BOX_OPEN:
                    mDialogUtil.closeProgressDialog();
                    inputRL.setVisibility(View.GONE);
                    showRL.setVisibility(View.VISIBLE);
                    if (showRL != null) {
                        stateTV = (TextView) showRL
                                .findViewById(R.id.tv_box_state);
                        String boardStr = zeroStr(boardNum);
                        String boxStr = zeroStr(boxCode);
                        stateTV.setText(boardStr + "组" + boxStr + "号门已打开，请关闭箱门");
                    }
                    controller.checkOpenBox(); // 重新检测该箱门状态
                    break;

                // 箱门打开失败
                case BOX_ERROR:
                    mDialogUtil.closeProgressDialog();
                    inputRL.setVisibility(View.GONE);
                    showRL.setVisibility(View.VISIBLE);
                    if (showRL != null) {
                        TextView stateTV = (TextView) showRL
                                .findViewById(R.id.tv_box_state);
                        String boardStr = zeroStr(boardNum);
                        String boxStr = zeroStr(boxCode);
                        stateTV.setText(boardStr + "组" + boxStr + "号门打开失败");
                    }

                    // controller.openBox(boardNum, boxCode);

                    break;

                // 箱门关闭 打开下一个箱门
                case BOX_CLOSE:
                    mDialogUtil.closeProgressDialog();

                    if (boxCode == boxNum) {
                        // 检测完毕
                        lastNextTV.setVisibility(View.VISIBLE);
                        stateTV.setText("测试完成，请点击下一步进入测试报告");
                    } else {
                        boxCode++;
                        Log.i("tag", "boxcode---" + boxCode);
                        controller.openBox(boardNum, boxCode);
                        mDialogUtil.showProgressDialog();
                    }

                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            // 开始检测
            case R.id.bt_box_test:
                if (!boardET.getText().equals("") && !boxET.getText().equals("")) {

                    Toast.makeText(getActivity(),"开始箱门检测", Toast.LENGTH_SHORT).show();
                    controller = BoxOpenController.getInstance();

                    controller.init(boxHandler);

                    boardNum = Integer.valueOf(boardET.getText().toString().trim());
                    boxNum = Integer.valueOf(boxET.getText().toString().trim());

                    GlobalField.boxInfoLocal = new ArrayList<RackInfo>();
                    RackInfo rack = new RackInfo();
                    rack.setBoardNum(boardNum);
                    rack.setCount(boxNum);
                    rack.setBoxes(new BoxInfo[boxNum]);
                    for (int i = 0; i < boxNum; i++) {
                        rack.getBoxes()[i] = new BoxInfo();
                        rack.getBoxes()[i].setBoardNum(boardNum);
                        rack.getBoxes()[i].setBoxNum(i + 1);
                        rack.getBoxes()[i].setDoorState(DoorStatusType.close);
                    }
                    GlobalField.boxInfoLocal.add(rack);
                    boxCode = 1;
                    mDialogUtil.showProgressDialog();
                    controller.openBox(boardNum, boxCode);

                }
                break;


            default:
                break;
        }
    }

    /**
     * 封装int成string
     */
    public static String zeroStr(int num) {
        String pattern = "00";
        java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
        return df.format(num);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogUtil != null) {
            mDialogUtil.closeProgressDialog();
        }
    }
}
