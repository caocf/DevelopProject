package com.moge.gege.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.moge.gege.R;
import com.moge.gege.ui.fragment.AboutUsFragment;
import com.moge.gege.ui.fragment.ContactUsFragment;
import com.moge.gege.ui.fragment.FeedbackFragment;
import com.moge.gege.ui.fragment.InviteFriendFragment;

public class OtherDetailActivity extends BaseActivity
{
    private Context mContext;

    private int mType; // 1 - about us; 2 - contact us; 3 - feedback

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherdetail);

        mType = getIntent().getIntExtra("type", 0);

        mContext = OtherDetailActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();

        switch (mType)
        {
            case 1:
                AboutUsFragment aboutusFragment = new AboutUsFragment();
                fragmentTransaction
                        .add(R.id.fragmentContainer, aboutusFragment);
                break;
            case 2:
                ContactUsFragment contactusFragment = new ContactUsFragment();
                fragmentTransaction.add(R.id.fragmentContainer,
                        contactusFragment);
                break;
            case 3:
                InviteFriendFragment inviteFriendFragment = new InviteFriendFragment();
                fragmentTransaction.add(R.id.fragmentContainer,
                        inviteFriendFragment);
                break;
            case 4:
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                fragmentTransaction.add(R.id.fragmentContainer,
                        feedbackFragment);
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }
}
