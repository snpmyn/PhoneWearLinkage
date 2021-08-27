package com.bocop.fem.base;

import android.os.Bundle;
import android.view.KeyEvent;

import com.bocop.fem.R;
import com.bocop.fem.value.Magic;
import com.chaos.util.java.activity.ActivitySuperviseManager;

import butterknife.ButterKnife;

/**
 * Created on 2021/8/16
 *
 * @author zsp
 * @desc BaseActivity
 * 启应用后 {@link com.bocop.fem.MainActivity} 直存至应用杀死时销毁。
 */
public abstract class BaseActivity extends com.chaos.litepool.BaseActivity {
    /**
     * 加载视图
     *
     * @param savedInstanceState 状态保存
     * @param layoutResId        布局资源 ID
     */
    @Override
    protected void initContentView(Bundle savedInstanceState, int layoutResId) {
        setContentView(layoutResId);
        ButterKnife.bind(this);
    }

    /**
     * onKeyUp
     * <p>
     *
     * @param keyCode 键码值
     * @param event   键事件
     * @return boolean
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 闪屏页（点击事件消耗，不向下分发，点击无反应）
            // 该分支无 break(详看 break、continue、return 区别)
            if (Magic.STRING_SPLASH_ACTIVITY.equals(ActivitySuperviseManager.getInstance().getCurrentRunningActivityName(this))) {
                return true;
            }
            if (Magic.STRING_MAIN_ACTIVITY.equals(ActivitySuperviseManager.getInstance().getCurrentRunningActivityName(this))) {
                ActivitySuperviseManager.getInstance().twoClickToExit(getString(R.string.exitAppHint));
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
