package com.ebox.mgt.ui.fragment.pollingfg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ebox.R;
import com.ebox.mgt.ui.BoxManageActivity;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;

/**
 * 柜体和锁
 */
public class FragmentBoxLock extends BasepollFragment {

    private View view;
    private Button boxmanBT;

    private EditText e1, e2, e3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_box_lock, null);
        initViews();
        return view;
    }

    private void initViews() {


        boxmanBT = (Button) view.findViewById(R.id.mgt_bt_fbl_boxman);
        boxmanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), BoxManageActivity.class);
                startActivity(intent1);
            }
        });
        e1 = (EditText) view.findViewById(R.id.et_g1);
        e2 = (EditText) view.findViewById(R.id.et_g2);
        e3 = (EditText) view.findViewById(R.id.et_g3);


        String startLockNum = PollingStore.getStore("startLockNum");
        String lockLockNum = PollingStore.getStore("lockLockNum");
        String changeLock = PollingStore.getStore("changeLock");

        if (!startLockNum.equals(PollingStore.POLL_NULL)) {
            e1.setText(startLockNum);
        }else{
            PollingStore.storePoll("startLockNum","0");
        }
        if (!lockLockNum.equals(PollingStore.POLL_NULL)) {
            e2.setText(lockLockNum);
        }else{
            PollingStore.storePoll("lockLockNum", "0");
        }
        if (!changeLock.equals(PollingStore.POLL_NULL)) {
            e3.setText(changeLock);
        }else{
            PollingStore.storePoll("changeLock", "0");
        }

        e1.addTextChangedListener(new MyTextWather() {
            @Override
            public void afterTextChanged(Editable editable) {
                PollingStore.storePoll("startLockNum",editable.toString());
            }
        });

        e2.addTextChangedListener(new MyTextWather() {
            @Override
            public void afterTextChanged(Editable editable) {
                PollingStore.storePoll("lockLockNum", editable.toString());
            }
        });

        e3.addTextChangedListener(new MyTextWather() {
            @Override
            public void afterTextChanged(Editable editable) {
                PollingStore.storePoll("changeLock", editable.toString());
            }
        });

    }


    private class MyTextWather implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }


}
