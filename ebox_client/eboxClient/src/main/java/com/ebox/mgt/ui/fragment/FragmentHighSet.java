package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.mgt.ui.LedTestActivity;
import com.ebox.mgt.ui.SqliteQueryActivity;
import com.ebox.mgt.ui.bar.AppIcon;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.service.SpaceCtrl;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.utils.FunctionUtil;

import java.util.List;

/**
 * 高级配置
 */
public class FragmentHighSet extends Fragment implements View.OnClickListener {

    private GridView mGrid;
    private View view;

    private Button btRestart, btRestartOS, btSysSet, btLed, btBoxTest, btSdcard, btRead,btquerySqlite;

    private List<ResolveInfo> mApps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadApps();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_high_set, null);

        initViews();
        return view;
    }

    private void initViews() {

        mGrid = (GridView) view.findViewById(R.id.apps_list);
        mGrid.setAdapter(new AppsAdapter());

        mGrid.setOnItemClickListener(listener);

        btRestart = (Button) view.findViewById(R.id.mgt_bt_fhs_restart);
        btRestartOS = (Button) view.findViewById(R.id.mgt_bt_fhs_restartos);
        btSysSet = (Button) view.findViewById(R.id.mgt_bt_fhs_sysset);
        btLed = (Button) view.findViewById(R.id.mgt_bt_fhs_led);
        btBoxTest = (Button) view.findViewById(R.id.mgt_bt_fhs_boxtest);
        btSdcard = (Button) view.findViewById(R.id.mgt_bt_fhs_sdcard);
        btRead = (Button) view.findViewById(R.id.mgt_bt_fhs_read);
        btquerySqlite= (Button) view.findViewById(R.id.bt_querySqlite);

        btRestart.setOnClickListener(this);
        btRestartOS.setOnClickListener(this);
        btSysSet.setOnClickListener(this);
        btLed.setOnClickListener(this);
        btBoxTest.setOnClickListener(this);
        btSdcard.setOnClickListener(this);
        btRead.setOnClickListener(this);
        btquerySqlite.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mgt_bt_fhs_restart:
                AppApplication.getInstance().exitApp();
                break;
            case R.id.mgt_bt_fhs_restartos:
               AppApplication.getInstance().restartOs();
                break;
            case R.id.mgt_bt_fhs_sysset:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.mgt_bt_fhs_led:
                Intent intent1 = new Intent(getActivity(), LedTestActivity.class);
                startActivity(intent1);
                break;
            case R.id.mgt_bt_fhs_boxtest:
                if (GlobalField.boxLocalInit) {
                    for (int i = 0; i < GlobalField.boxInfoLocal.size(); i++) {
                        RackInfo rack = GlobalField.boxInfoLocal.get(i);

                        for (int j = 0; j < rack.getBoxes().length; j++) {
                            BoxInfo box = rack.getBoxes()[j];

                            // 开门
                            BoxOp op = new BoxOp();
                            op.setOpId(BoxOpId.OpenDoor);
                            op.setOpBox(box);
                            BoxCtrlTask.addBoxCtrlQueue(op);
                        /*try {
                            Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Toast.makeText(this, BoxUtils.getBoxDesc(box)+"已打开！", Toast.LENGTH_SHORT).show();*/
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "箱门数据初始化尚未完成！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mgt_bt_fhs_sdcard:
                Toast.makeText(getActivity(), "space all:" + SpaceCtrl.getAllSize() +
                        "M available:" + SpaceCtrl.getAvailaleSize() +
                        "M free:" + SpaceCtrl.getFreeSize() + "M", Toast.LENGTH_LONG).show();
                break;
            case R.id.mgt_bt_fhs_read:
                GlobalField.boxLocalInit = false;
                GlobalField.boxInfoLocal = null;
                break;

            case R.id.bt_querySqlite:
                Intent queryIntent = new Intent(getActivity(), SqliteQueryActivity.class);
                startActivity(queryIntent);
                break;

        }
    }


    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ResolveInfo info = mApps.get(position);

            //该应用的包名
            String pkg = info.activityInfo.packageName;
            //应用的主activity类
            String cls = info.activityInfo.name;

            FunctionUtil.startApp(getActivity(), pkg, cls);
        }

    };

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
    }


    public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            AppIcon icon;

            if (convertView == null) {
                icon = new AppIcon(getActivity());
            } else {
                icon = (AppIcon) convertView;
            }

            ResolveInfo info = mApps.get(position);
            icon.setData(info.activityInfo.loadIcon(getActivity().getPackageManager()),
                    info.activityInfo.loadLabel(getActivity().getPackageManager()).toString());

            return icon;
        }

        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

}
