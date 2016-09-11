package com.ebox.mgt.ui.fragment.pollingfg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;

/**
 * 触屏测试
 */
public class FragmentTouchTest extends BasepollFragment implements View.OnClickListener {

    private View view;

    private ImageView touch1IV;
    private ImageView touch2IV;
    private ImageView touch3IV;
    private ImageView touch4IV;
    private ImageView touch5IV;

    private boolean flag = false;


    private Button okBT, errorBT;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_touch_test, null);
        initViews();
        return view;
    }

    private void initViews() {
        touch1IV = (ImageView) view.findViewById(R.id.iv_touch_1);
        touch2IV = (ImageView) view.findViewById(R.id.iv_touch_2);
        touch3IV = (ImageView) view.findViewById(R.id.iv_touch_3);
        touch4IV = (ImageView) view.findViewById(R.id.iv_touch_4);
        touch5IV = (ImageView) view.findViewById(R.id.iv_touch_5);

        touch1IV.setOnClickListener(this);
        touch2IV.setOnClickListener(this);
        touch3IV.setOnClickListener(this);
        touch4IV.setOnClickListener(this);
        touch5IV.setOnClickListener(this);

        touch1IV.setVisibility(View.VISIBLE);
        touch2IV.setVisibility(View.GONE);
        touch3IV.setVisibility(View.GONE);
        touch4IV.setVisibility(View.GONE);
        touch5IV.setVisibility(View.GONE);

        touch1IV.setBackgroundResource(R.drawable.ex_code_blue);
        touch2IV.setBackgroundResource(R.drawable.ex_code_blue);
        touch3IV.setBackgroundResource(R.drawable.ex_code_blue);
        touch4IV.setBackgroundResource(R.drawable.ex_code_blue);
        touch5IV.setBackgroundResource(R.drawable.ex_code_blue);

        okBT = (Button) view.findViewById(R.id.bt_ok);
        errorBT = (Button) view.findViewById(R.id.bt_error);

        okBT.setVisibility(View.GONE);
        errorBT.setVisibility(View.GONE);

        PollingStore.storePoll("isTestTouch",PollType.TEST_OK);
    }


    @Override
    public void onClick(View view) {

        flag = !flag;
        switch (view.getId()) {
            case R.id.iv_touch_1:
                if (flag) {
                    touch1IV.setBackgroundResource(R.drawable.ex_code_square_yellow);
                } else {
                    touch1IV.setVisibility(View.GONE);
                    touch2IV.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_touch_2:

                if (flag) {
                    touch2IV.setBackgroundResource(R.drawable.ex_code_square_yellow);
                } else {
                    touch2IV.setVisibility(View.GONE);
                    touch3IV.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_touch_3:
                if (flag) {
                    touch3IV.setBackgroundResource(R.drawable.ex_code_square_yellow);
                } else {
                    touch3IV.setVisibility(View.GONE);
                    touch4IV.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_touch_4:
                if (flag) {
                    touch4IV.setBackgroundResource(R.drawable.ex_code_square_yellow);
                } else {
                    touch4IV.setVisibility(View.GONE);
                    touch5IV.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_touch_5:
                if (flag) {
                    touch5IV.setBackgroundResource(R.drawable.ex_code_square_yellow);
                } else {
                    okBT.setVisibility(View.VISIBLE);
                    errorBT.setVisibility(View.VISIBLE);


                    okBT.setOnClickListener(this);
                    errorBT.setOnClickListener(this);

                }
                break;

            case R.id.bt_ok:
                PollingStore.storePoll("isTestTouch", PollType.TEST_OK);
                Toast.makeText(getActivity(), "触屏测试成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_error:
                PollingStore.storePoll("isTestTouch", PollType.TEST_ERROR);
                Toast.makeText(getActivity(), "触屏测试失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
