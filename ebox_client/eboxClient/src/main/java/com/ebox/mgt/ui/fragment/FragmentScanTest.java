package com.ebox.mgt.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ebox.R;

/**
 * 扫描枪测试
 */
public class FragmentScanTest extends Fragment {

    private View view;

    private EditText scanET;
    private Button clearBT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan_test, null);
        initViews();
        return view;
    }

    private void initViews() {
        scanET = (EditText) view.findViewById(R.id.mgt_et_fst_scan);

        scanET.requestFocus();


        clearBT = (Button) view.findViewById(R.id.mgt_bt_fst_clean);
        clearBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanET.setText("");
                scanET.requestFocus();
            }
        });
    }


}
