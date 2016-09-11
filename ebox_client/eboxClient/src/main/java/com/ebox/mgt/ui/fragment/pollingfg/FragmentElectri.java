package com.ebox.mgt.ui.fragment.pollingfg;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ebox.R;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollType;
import com.ebox.mgt.ui.fragment.pollingfg.model.PollingStore;

/**
 * 供电系统
 */
public class FragmentElectri extends BasepollFragment {

    private View view;
    private EditText degreeET;
    private RadioGroup group;
    private RadioButton rb_ok, rb_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_electri, null);
        initViews();
        return view;
    }

    private void initViews() {
        degreeET = (EditText) view.findViewById(R.id.et_fe_degree);
        group = (RadioGroup) view.findViewById(R.id.rg_state);
        rb_ok = (RadioButton) view.findViewById(R.id.rb_ok);
        rb_no = (RadioButton) view.findViewById(R.id.rb_no);

        String pressureState = PollingStore.getStore("pressureState");
        String elecdegree = PollingStore.getStore("elecdegree");

        if (!pressureState.equals(PollingStore.POLL_NULL)) {
            if (pressureState.equals(PollType.OK)) {
                rb_ok.setChecked(true);
            } else {
                rb_ok.setChecked(false);
            }
        }else{
            PollingStore.storePoll("pressureState",PollType.OK);
        }


        if (!elecdegree.equals(PollingStore.POLL_NULL)) {
            degreeET.setText(elecdegree);
        }else{
            PollingStore.storePoll("elecdegree","0");
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ok:
                        PollingStore.storePoll("pressureState", PollType.OK);
                        break;

                    case R.id.rb_no:
                        PollingStore.storePoll("pressureState", PollType.ERROR);
                        break;
                }
            }
        });

        degreeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                PollingStore.storePoll("elecdegree", editable.toString());
            }
        });

    }


}
