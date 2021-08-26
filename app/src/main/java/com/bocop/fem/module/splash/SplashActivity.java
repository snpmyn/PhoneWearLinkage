package com.bocop.fem.module.splash;

import android.os.Bundle;

import com.bocop.fem.R;
import com.bocop.fem.base.BaseActivity;
import com.bocop.fem.module.splash.kit.SplashActivityKit;

/**
 * @desc: 闪屏页
 * @author: zsp
 * @date: 2021/8/16 11:41 上午
 */
public class SplashActivity extends BaseActivity {
    private SplashActivityKit splashActivityKit;

    /**
     * 布局资源 ID
     *
     * @return 布局资源 ID
     */
    @Override
    protected int layoutResId() {
        return R.layout.activity_splash;
    }

    /**
     * 加载视图
     *
     * @param savedInstanceState 状态保存
     * @param layoutResId        布局资源 ID
     */
    @Override
    protected void initContentView(Bundle savedInstanceState, int layoutResId) {
        // 处理 PreviewWindow 背景避图直占内存（过渡绘制）
        getWindow().setBackgroundDrawable(null);
        super.initContentView(savedInstanceState, layoutResId);
    }

    /**
     * 初始控件
     */
    @Override
    protected void stepUi() {

    }

    /**
     * 初始配置
     */
    @Override
    protected void initConfiguration() {
        splashActivityKit = new SplashActivityKit();
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {

    }

    /**
     * 开始逻辑
     */
    @Override
    protected void startLogic() {
        splashActivityKit.requestPermission(this);
    }
}