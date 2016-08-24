package com.xhl.xhl_library.Base.Sum;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.xhl.xhl_library.Base.BaseActivity;
import com.xhl.xhl_library.R;

import org.xutils.common.util.LogUtil;

/**
 * Created by Administrator on 2015/11/15.
 * <p/>
 * 管理一个Activity里面的fragment
 */
public abstract class SumFragmentActivity extends BaseActivity {

    public static boolean DEBUG = false;

    protected SumFragment mCurrentFragment;

    protected abstract int getFragmentContainerId();

    //表示栈最后一个做为根布局
    protected boolean needRoot() {
        return true;
    }

    private boolean isFirstAdd = true;

    /**
     * 添加Fragment到管理栈里，同一个实力对象只会创建一次
     * 功能实现原理FragmentTabhost相同,注意hide和detach区别
     *
     * @param cls  显示的Fragment
     * @param data 给Fragment传递数据
     */

    public void pushFragmentToBackStack(Class<?> cls, Object data) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        param.data = data;
        goToThisFragment(param);
    }

    protected String getFragmentTag(FragmentParam param) {
        StringBuilder sb = new StringBuilder(param.cls.toString());
        return sb.toString();
    }

    private void goToThisFragment(FragmentParam param) {
        int containerId = getFragmentContainerId();
        Class<?> cls = param.cls;
        if (cls == null) {
            return;
        }
        try {
            String fragmentTag = getFragmentTag(param);
            FragmentManager fm = getSupportFragmentManager();
            if (DEBUG) {
                LogUtil.d("before operate, stack entry count: " + fm.getBackStackEntryCount());
            }
            SumFragment fragment = (SumFragment) fm.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                fragment = (SumFragment) cls.newInstance();
                LogUtil.e("newInstance " + fragmentTag);
            }
            if (mCurrentFragment != null && mCurrentFragment != fragment) {
                mCurrentFragment.onLeave();
            }

            fragment.onEnter(param.data);

            FragmentTransaction ft = fm.beginTransaction();
            if (!needRoot() || !isFirstAdd) {
                //新加入的Fragment动画，栈中fragment退出动画，战中fragment进去动画，栈定fragment推出动画
                ft.setCustomAnimations(fragmentOpenEnterAnim(), fragmentOpenExitAnim(), fragmentCloseEnterAnim(), fragmentCloseExitAnim());
            }

            ft.replace(containerId, fragment, fragmentTag);

            mCurrentFragment = fragment;

            ft.addToBackStack(fragmentTag);

            ft.commit();

            isFirstAdd = false;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected int fragmentOpenEnterAnim() {
        return R.anim.activity_open_enter;
    }

    protected int fragmentOpenExitAnim() {
        return R.anim.activity_open_exit;
    }

    protected int fragmentCloseEnterAnim() {
        return R.anim.activity_close_enter;
    }

    protected int fragmentCloseExitAnim() {
        return R.anim.activity_close_exit;
    }

    public void goToFragment(Class<?> cls, Object data) {
        if (cls == null) {
            return;
        }
        SumFragment fragment = (SumFragment) getSupportFragmentManager().findFragmentByTag(cls.toString());
        if (fragment != null) {
            mCurrentFragment = fragment;
            fragment.onBackWithData(data);
        }
        getSupportFragmentManager().popBackStackImmediate(cls.toString(), 0);
    }

    public void popTopFragment(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 1 && needRoot()) {
            this.finish();
        } else {
            fm.popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && mCurrentFragment != null) {
                mCurrentFragment.onBackWithData(data);
            }
        }
    }

    public void popToRoot(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 1 || !needRoot()) {
            fm.popBackStackImmediate();
        }
        popTopFragment(data);
    }

    protected void doReturnBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1 && needRoot()) {
            finish();
        } else if (count == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && mCurrentFragment != null) {
                mCurrentFragment.onBack();
            }
        }
    }

    private boolean tryToUpdateCurrentAfterPop() {
        FragmentManager fm = getSupportFragmentManager();
        int cnt = fm.getBackStackEntryCount();
        if (cnt > 0) {
            String name = fm.getBackStackEntryAt(cnt - 1).getName();
            Fragment fragment = fm.findFragmentByTag(name);
            if (fragment != null && fragment instanceof SumFragment) {
                mCurrentFragment = (SumFragment) fragment;
            }
            return true;
        }
        return false;
    }

    /**
     * process the return back logic
     * return true if back pressed event has been processed and should stay in current view
     *
     * @return
     */
    protected boolean processBackPressed() {
        return false;
    }


    @Override
    public void onBackPressed() {
        // process back for fragment
        if (processBackPressed()) {
            return;
        }

        // process back for fragment
        boolean enableBackPressed = true;
        if (mCurrentFragment != null && mCurrentFragment.isResumed()) {
            enableBackPressed = !mCurrentFragment.processBackPressed();
        }
        if (enableBackPressed) {
            doReturnBack();
        }
    }

    public void hideKeyboardForCurrentFocus() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showKeyboardAtView(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void forceShowKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    protected void exitFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
}
