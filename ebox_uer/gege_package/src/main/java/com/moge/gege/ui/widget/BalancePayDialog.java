package com.moge.gege.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.R;

public class BalancePayDialog extends Dialog implements View.OnClickListener
{
    private Context context;
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;

    private OnClickListener positiveButtonClickListener;
    private OnClickListener negativeButtonClickListener;
    private OnClickListener getCodeButtonClickListener;

    private TextView messageText;
    private EditText codeEdit;
    private Button getCodeBtn;

    public BalancePayDialog(Context context, String title, String message, String positiveButtonText, String negativeButtonText)
    {
        super(context, R.style.customdialog);
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
    }

    public BalancePayDialog(Context context, int title, int message, int positiveButtonText, int negativeButtonText)
    {
        super(context, R.style.customdialog);
        this.context = context;
        this.title = (String) context
                .getText(title);
        this.message = (String) context
                .getText(message);;
        this.positiveButtonText = (String) context
                .getText(positiveButtonText);
        this.negativeButtonText = (String) context
                .getText(negativeButtonText);
    }

    public BalancePayDialog setMessage(String message)
    {
        this.message = message;
        messageText.setText(message);
        return this;
    }

    public BalancePayDialog setMessage(int message)
    {
        this.message = (String) context.getText(message);
        messageText.setText(message);
        return this;
    }

    public BalancePayDialog setPositiveButtonListener(OnClickListener listener)
    {
        this.positiveButtonClickListener = listener;
        return this;
    }

    public BalancePayDialog setNegativeButtonListener(OnClickListener listener)
    {
        this.negativeButtonClickListener = listener;
        return this;
    }

    public BalancePayDialog setGetCodeButtonListener(OnClickListener listener)
    {
        this.getCodeButtonClickListener = listener;
        return this;
    }

    public String getCode()
    {
        return codeEdit.getText().toString().trim();
    }

    public Button getGetCodeBtn()
    {
        return this.getCodeBtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_balancepay_dialog, null);
        setContentView(layout);

        TextView titleText = (TextView)layout.findViewById(R.id.titleText);
        messageText = (TextView)layout.findViewById(R.id.messageText);
        codeEdit = (EditText)layout.findViewById(R.id.codeEdit);
        getCodeBtn = (Button)layout.findViewById(R.id.getCodeBtn);
        Button negativeButton = (Button)layout.findViewById(R.id.negativeButton);
        Button positiveButton = (Button)layout.findViewById(R.id.positiveButton);

        titleText.setText(title);
        messageText.setText(message);
        negativeButton.setText(negativeButtonText);
        positiveButton.setText(positiveButtonText);

        getCodeBtn.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        positiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.getCodeBtn:
                if(getCodeButtonClickListener != null)
                {
                    getCodeButtonClickListener.onClick(this, 0);
                }
                break;
            case R.id.positiveButton:
                if(positiveButtonClickListener != null)
                {
                    positiveButtonClickListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
                }
                break;
            case R.id.negativeButton:
                if (negativeButtonClickListener != null)
                {
                    negativeButtonClickListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
                }
                break;
            default:
                break;
        }

    }

}
