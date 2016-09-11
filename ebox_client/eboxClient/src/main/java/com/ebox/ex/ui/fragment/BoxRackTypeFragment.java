package com.ebox.ex.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.network.model.enums.RackType;
import com.ebox.pub.utils.MGViewUtil;

public class BoxRackTypeFragment extends Fragment implements View.OnClickListener {

    private OnFragmentRackListener mListener;

    //0:快递柜；1：生鲜柜；2：保温柜
    private TextView box_kd_count, box_sx_count, box_bw_count;

    public static BoxRackTypeFragment newInstance() {
        BoxRackTypeFragment fragment = new BoxRackTypeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BoxRackTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = initView(inflater, container);

        MGViewUtil.scaleContentView((ViewGroup) view);

        return view;
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.ex_fragment_box_rack_type, container, false);

        view.findViewById(R.id.box_kd).setOnClickListener(this);
        view.findViewById(R.id.box_bw).setOnClickListener(this);
        view.findViewById(R.id.box_sx).setOnClickListener(this);
        view.findViewById(R.id.bt_ok).setOnClickListener(this);

        box_kd_count = (TextView  ) view.findViewById(R.id.box_kd_count);

        box_sx_count = (TextView) view.findViewById(R.id.box_sx_count);

        box_bw_count = (TextView) view.findViewById(R.id.box_bw_count);


        int count = BoxInfoHelper.getCountByRackType(RackType.box_kd);
        box_kd_count.setText(count + "个");
        setState(view.findViewById(R.id.box_kd), count);

        count = BoxInfoHelper.getCountByRackType(RackType.box_sx);
        box_sx_count.setText(count + "个");
        setState(view.findViewById(R.id.box_sx), count);

        count = BoxInfoHelper.getCountByRackType(RackType.box_bw);
        box_bw_count.setText(count + "个");
        setState(view.findViewById(R.id.box_bw), count);

        return view;
    }

    private void setState(View view, int count) {

        if (count == 0)
        {
            view.setEnabled(false);
        } else {
            view.setEnabled(true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentRackListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.box_bw:
                if (mListener != null) {
                    mListener.onRackChoosed(RackType.box_bw);
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.box_sx:
                if (mListener != null) {
                    mListener.onRackChoosed(RackType.box_sx);
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.box_kd:
                if (mListener != null) {
                    mListener.onRackChoosed(RackType.box_kd);
                    getFragmentManager().popBackStack();
                }
                break;

            case R.id.bt_ok:
                if (mListener != null) {
                    mListener.onManualChooseClick();
                }
                break;
        }

    }

    public interface OnFragmentRackListener {
        public void onRackChoosed(int rackType);//选择箱门类型

        public void onManualChooseClick();//手动选择

    }

}
