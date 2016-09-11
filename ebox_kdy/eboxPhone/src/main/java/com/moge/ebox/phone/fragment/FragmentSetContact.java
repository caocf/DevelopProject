package com.moge.ebox.phone.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.ebox.phone.R;

public class FragmentSetContact extends Fragment implements View.OnClickListener{
    private RelativeLayout rl_phone;
    private TextView phone_contact;
    private  String  phone_num = "17701583467";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_set_contact, null);
        rl_phone = (RelativeLayout)view.findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        phone_contact =(TextView)view.findViewById(R.id.phone_contact);
        phone_contact.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        phone_contact.getPaint().setAntiAlias(true);
        phone_contact.setText(phone_num);
		return view;
	}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_phone:

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_num));
                startActivity(intent);
                break;
        }
    }
}
