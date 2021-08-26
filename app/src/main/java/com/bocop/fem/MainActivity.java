package com.bocop.fem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bocop.fem.base.BaseActivity;
import com.bocop.fem.kit.MainActivityKit;
import com.bocop.fem.module.outlets.OutletListActivity;
import com.bocop.fem.utils.HiWearHandleKit;
import com.chaos.util.java.intent.IntentJump;
import com.chaos.util.java.statusbar.StatusBarUtils;
import com.chaos.widget.choose.provincialandurbanlinkage.ProvincialAndUrbanLinkage;
import com.chaos.widget.textview.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc: 主页
 * @author: zsp
 * @date: 2021/8/10 3:29 下午
 */
public class MainActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mainActivityTvName)
    TextView mainActivityTvName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mainActivityTvAddress)
    TextView mainActivityTvAddress;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mainActivityDctvCurrentDistance)
    DrawableCenterTextView mainActivityDctvCurrentDistance;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mainActivityTvQueQueue)
    TextView mainActivityTvQueQueue;
    /**
     * 主页配套元件
     */
    private MainActivityKit mainActivityKit;
    /**
     * 省市区联动
     */
    private ProvincialAndUrbanLinkage provincialAndUrbanLinkage;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivityKit.display(this, 1, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityKit.destroy();
        HiWearHandleKit.getInstance().destroy();
    }

    /**
     * 布局资源 ID
     *
     * @return 布局资源 ID
     */
    @Override
    protected int layoutResId() {
        return R.layout.activity_main;
    }

    /**
     * 加载视图
     *
     * @param savedInstanceState 状态保存
     * @param layoutResId        布局资源 ID
     */
    @Override
    protected void initContentView(Bundle savedInstanceState, int layoutResId) {
        StatusBarUtils.statusBarLight(this, 0);
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
        mainActivityKit = new MainActivityKit();
        provincialAndUrbanLinkage = new ProvincialAndUrbanLinkage(this);
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
        mainActivityKit.stepOutlets(this, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.mainActivityTvChooseOtherOutlets, R.id.mainActivityMb})
    public void onViewClicked(@NonNull View view) {
        switch (view.getId()) {
            // 选择其它网点
            case R.id.mainActivityTvChooseOtherOutlets:
                mainActivityKit.chooseOtherOutlets(provincialAndUrbanLinkage, location -> IntentJump.getInstance().jump(null, MainActivity.this, false, OutletListActivity.class));
                break;
            // 立即取号
            case R.id.mainActivityMb:
                mainActivityKit.takeNumberImmediately(this);
                break;
            default:
                break;
        }
    }
}