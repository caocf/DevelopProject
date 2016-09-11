package com.ebox.mgt.ui.utils;

/**
 * Created by admin on 2015/9/20.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by prin on 2015/9/10.
 * 使用到的动画效果
 */
public class HardAnimUtils{


    public static final String TRANSLATIONY = "translationY";
    public static final String TRANSLATIONX = "translationX";
    public static final String ALPHA = "alpha";
    public static final String SCALEY = "scaleY";
    public static final String SCALEX = "scaleX";
    public static final String ROTATION = "rotation";


    /**
     * 视差显示首页按钮
     */
    public static void enterBTScale(View... views){
        for (int i=0;i<views.length;i++){
            ObjectAnimator viewXScale=ObjectAnimator.ofFloat(views[i],SCALEX,1f,0.8f);
            ObjectAnimator viewYScale=ObjectAnimator.ofFloat(views[i],SCALEX,1f,0.8f);

            AnimatorSet scaleAnim=new AnimatorSet();
            scaleAnim.setDuration(800);
            scaleAnim.setInterpolator(new OvershootInterpolator());

            scaleAnim.playTogether(viewXScale,viewYScale);
            scaleAnim.start();

        }
    }


    /**
     * 视差滚动测试标题提示动画
     */
    public static void headerAnim(TextView headerTitleTV, TextView headerPromptTV) {
        float curXTitle = 0f;
        float curXPrompt = 0f;
        ObjectAnimator tranX1 = ObjectAnimator.ofFloat(headerTitleTV, TRANSLATIONX, curXTitle + 600f, curXTitle);
        ObjectAnimator tranX2 = ObjectAnimator.ofFloat(headerPromptTV, TRANSLATIONX, curXPrompt + 600f, curXPrompt);
        tranX1.setDuration(1000);
        tranX2.setDuration(700);
        AnimatorSet anim = new AnimatorSet();
        anim.play(tranX1).with(tranX2);
        anim.start();
    }

    /**
     * 测试结果提示动画
     */
    public static void resultTVAnim(View... views){
        int duration=1100;
        AnimatorSet anim = new AnimatorSet();
        for (int i=0;i<views.length;i++){
            ObjectAnimator t1Anim = ObjectAnimator.ofFloat(views[i], TRANSLATIONX, 400f, 0f);
            t1Anim.setDuration(duration - 100 * i);
            anim.playTogether(t1Anim);
        }
        anim.start();
    }


    /**
     * 测试结果提示动画
     */
    public static void resultTVAnim(TextView test1TV, TextView test2TV, TextView test3TV, TextView test4TV) {

        ObjectAnimator t1Anim = ObjectAnimator.ofFloat(test1TV, TRANSLATIONX, 400f, 0f);
        ObjectAnimator t2Anim = ObjectAnimator.ofFloat(test2TV, TRANSLATIONX, 400f, 0f);
        ObjectAnimator t3Anim = ObjectAnimator.ofFloat(test3TV, TRANSLATIONX, 400f, 0f);
        ObjectAnimator t4Anim = ObjectAnimator.ofFloat(test4TV, TRANSLATIONX, 400f, 0f);


        t1Anim.setDuration(1100);
        t2Anim.setDuration(1000);
        t3Anim.setDuration(900);
        t4Anim.setDuration(800);

        AnimatorSet anim = new AnimatorSet();
        anim.play(t1Anim).with(t2Anim).with(t3Anim).with(t4Anim);
        anim.start();

    }


    public static void showState(TextView resultTV, String result, int color) {
        resultTV.setText(result);
        resultTV.setTextColor(color);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(resultTV, ALPHA, 0f, 1f);
        alpha.setDuration(1000);
        alpha.start();

    }

    public static void shootOverBT(Button overBT) {
        overBT.setVisibility(View.VISIBLE);
        ObjectAnimator sxAnim = ObjectAnimator.ofFloat(overBT, SCALEX, 0f, 1.4f, 1f);
        ObjectAnimator aAnim = ObjectAnimator.ofFloat(overBT, ALPHA, 0f, 1f);
        ObjectAnimator syAnim = ObjectAnimator.ofFloat(overBT, SCALEY, 0.6f, 1.2f, 1f);

        sxAnim.setDuration(1000);
        aAnim.setDuration(1000);
        aAnim.setDuration(1000);

        sxAnim.setInterpolator(new OvershootInterpolator());
        syAnim.setInterpolator(new BounceInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(sxAnim, syAnim, aAnim);
        animatorSet.start();
    }


    /**
     * ProHome的动画
     */
    /**
     * 菜单进入动画
     * @param view
     */
    public static void menuEnter(View view){
        float curSX=view.getScaleX();
        ObjectAnimator enterAnim=ObjectAnimator.ofFloat(view,SCALEX,curSX,0.9f);
        enterAnim.setDuration(800);
        enterAnim.setInterpolator(new OvershootInterpolator());
        enterAnim.start();
    }

    /**
     * 菜单划出动画
     */
    public static void menuExit(View view){
        float curSX=view.getScaleX();
        ObjectAnimator enterAnim=ObjectAnimator.ofFloat(view,SCALEX,curSX,1f);
        enterAnim.setDuration(800);
        enterAnim.setInterpolator(new OvershootInterpolator());
        enterAnim.start();
    }


    /**
     * 按钮
     * @param btExpLog
     */
    public static void enterBTTransLOG(Button btExpLog) {
        ObjectAnimator tranXExit=ObjectAnimator.ofFloat(btExpLog, TRANSLATIONX, 0f, -400f);
        ObjectAnimator alphaAnim=ObjectAnimator.ofFloat(btExpLog,ALPHA,0.2f,1f);
        AnimatorSet anim=new AnimatorSet();
        anim.setDuration(1000);
        anim.setInterpolator(new OvershootInterpolator(1.0f));
        anim.playTogether(tranXExit,alphaAnim);
        anim.start();
    }

    public static void enterBTTransSingle(Button btSingleTest) {
        ObjectAnimator tranXExit=ObjectAnimator.ofFloat(btSingleTest, TRANSLATIONX, 0f,400f);
        ObjectAnimator alphaAnim=ObjectAnimator.ofFloat(btSingleTest,ALPHA,0.2f,1f);
        AnimatorSet anim=new AnimatorSet();
        anim.setDuration(1000);
        anim.setInterpolator(new OvershootInterpolator(1.0f));
        anim.playTogether(tranXExit, alphaAnim);
        anim.start();
    }
}
