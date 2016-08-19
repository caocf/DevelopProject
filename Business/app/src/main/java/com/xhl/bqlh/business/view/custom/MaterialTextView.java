package com.xhl.bqlh.business.view.custom;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xhl.xhl_library.R;

/**
 * Created by Sum on 16/3/11.
 */
public class MaterialTextView extends TextView {

    private static final String Roboto_Medium = "fonts/Roboto-Medium.ttf";
    private static final String Roboto_Regular = "fonts/Roboto-Regular.ttf";

    public MaterialTextView(Context context) {
        super(context);
        iniAttrs(context, null, 0);
    }

    public MaterialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniAttrs(context, attrs, 0);
    }

    public MaterialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        iniAttrs(context, attrs, defStyleAttr);
    }

    private void iniAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        AssetManager assets = context.getAssets();
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialTextView);
            int type = typedArray.getInt(R.styleable.MaterialTextView_Material_Typeface, 0);
            Typeface font;
            if (type == 1) {
                font = Typeface.createFromAsset(assets, Roboto_Regular);
            } else {
                font = Typeface.createFromAsset(assets, Roboto_Medium);
            }
            setTypeface(font);
            typedArray.recycle();
        } else {
            setTypeface(Typeface.createFromAsset(assets, Roboto_Regular));
        }
    }


}
