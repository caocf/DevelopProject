package com.ebox.ex.ui.base;

import android.os.Bundle;
import android.util.Log;

import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;

/**
 * Created by Android on 2015/10/29.
 */
public abstract class BaseOpenDoorFragment extends BaseFragment implements BoxOp.resultListener, Runnable {

    private String box_code;
    private int checkTime = 0;

    private boolean hasOpened;

    private Thread mThread;
    private boolean mThreadIsRun;

    protected abstract String getBoxCode();

    /**
     * 开门成功回调
     */
    public abstract void OpenDoorSuccess();

    /**
     * 关门成功回调
     */

    public abstract void CloseDoorSuccess();

    /**
     * 开门失败回调
     */
    public abstract void OpenDoorFailed();

    //默认自动操作开门
    public boolean isNeedOpen() {
        return true;
    }
    //默认打开箱门后自动检测是否关闭
    public boolean isNeedCheckCloose(){
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasOpened = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        //获取箱门号
        box_code = getBoxCode();

        if (box_code == null || box_code.equals("")) {
            Log.e(GlobalField.tag, "箱门号为空");
        } else
        {
            if (isNeedOpen())
            {
                openDoor();
            }
        }
    }

    /**
     * 开门方法
     */
    public void openDoor() {

        if (hasOpened) {
            Log.w("", "door has opened:" + box_code);
            return;
        }

        BoxInfo box = BoxUtils.getBoxByCode(box_code);

        if (BoxUtils.getBoxLocalState(box).equals(DoorStatusType.unknow)) {
            Log.e(TAG, "Box state error:" + box_code);
        } else {
            Log.i(GlobalField.tag, "open door:" + box_code);
            //记录关门检测次数为0次
            checkTime = 0;
            // 开门
            BoxOp op = new BoxOp();
            op.setOpId(BoxOpId.OpenDoor);
            op.setOpBox(box);
            op.setListener(this);
            BoxCtrlTask.addBoxCtrlQueue(op);
        }
    }


    @Override
    public void onDestroy()
    {
        //退出线程还存在，中断检测线程
        if (mThread != null && mThread.isAlive())
        {
            mThreadIsRun=false;
        }

        super.onDestroy();
    }

    @Override
    public void onResult(int result) {
        if (result == RstCode.Success)
        {
            hasOpened = true;
            Log.i(GlobalField.tag, "open door success " + box_code);
            //开门成功
            OpenDoorSuccess();
            if (isNeedCheckCloose())
            {
                mThreadIsRun=true;
                //启动线程检测箱门关闭状态
                mThread = new Thread(this);
                mThread.start();
            }

        } else {
            Log.i(GlobalField.tag, "open door failed  " + box_code);
            //开门失败
            OpenDoorFailed();
        }
    }

    @Override
    public void run() {
        checkTime = 0;
        while (mThreadIsRun)
        {
            BoxInfo box = BoxUtils.getBoxByCode(box_code);
            // 获取箱门状态
            BoxOp op = new BoxOp();
            op.setOpId(BoxOpId.GetDoorsStatus);
            op.setOpBox(box);
            BoxCtrlTask.addBoxCtrlQueue(op);
            // 箱门关闭
            if (BoxUtils.getBoxLocalState(box) == DoorStatusType.close)
            {
                checkTime++;
                if (checkTime==2)
                {
                    Log.i(GlobalField.tag, "close door success " + box_code);
                    //关门成功
                    CloseDoorSuccess();
                    //已经关闭
                    hasOpened=false;
                    break;
                }
            }
            try
            {
                Thread.sleep(800);
            } catch (InterruptedException e)
            {
                hasOpened=false;
                CloseDoorSuccess();
            }
        }
    }
}
