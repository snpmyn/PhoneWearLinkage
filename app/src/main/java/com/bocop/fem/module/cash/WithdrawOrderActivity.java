package com.bocop.fem.module.cash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bocop.fem.R;
import com.bocop.fem.base.BaseActivity;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.bocop.fem.module.cash.kit.WithdrawOrderActivityKit;
import com.bocop.fem.value.Constant;
import com.chaos.util.java.statusbar.StatusBarUtils;
import com.chaos.util.java.toast.ToastKit;
import com.chaos.widget.textview.DrawableCenterTextView;
import com.google.android.material.appbar.MaterialToolbar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc: 取现预约页
 * @author: zsp
 * @date: 2021/8/17 9:28 上午
 */
public class WithdrawOrderActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderActivityMt)
    MaterialToolbar withdrawOrderActivityMt;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderActivityTvAppointmentOutlet)
    TextView withdrawOrderActivityTvAppointmentOutlet;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderActivityDctvDrawUse)
    DrawableCenterTextView withdrawOrderActivityDctvDrawUse;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderActivityDctvDrawAccountNumber)
    DrawableCenterTextView withdrawOrderActivityDctvDrawAccountNumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderActivityDctvAppointmentTime)
    DrawableCenterTextView withdrawOrderActivityDctvAppointmentTime;
    /**
     * 取现预约配套元件
     */
    private WithdrawOrderActivityKit withdrawOrderActivityKit;
    /**
     * 支行信息数据库表
     */
    private SubBranchInformationDataBaseTable subBranchInformationDataBaseTable;

    /**
     * 布局资源 ID
     *
     * @return 布局资源 ID
     */
    @Override
    protected int layoutResId() {
        return R.layout.activity_withdraw_order;
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
        withdrawOrderActivityKit = new WithdrawOrderActivityKit();
        subBranchInformationDataBaseTable = (SubBranchInformationDataBaseTable) getIntent().getSerializableExtra(Constant.OUTLET);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        withdrawOrderActivityMt.setNavigationOnClickListener(view -> finish());
    }

    /**
     * 开始逻辑
     */
    @Override
    protected void startLogic() {
        withdrawOrderActivityKit.display(this, withdrawOrderActivityTvAppointmentOutlet);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.withdrawOrderActivityRlDrawUse, R.id.withdrawOrderActivityRlDrawAccountNumber, R.id.withdrawOrderActivityRlAppointmentTime,
            R.id.mainActivityMbEnsure, R.id.mainActivityTvAppointmentRecord})
    public void onViewClicked(@NonNull View view) {
        switch (view.getId()) {
            case R.id.withdrawOrderActivityRlDrawUse:
                withdrawOrderActivityKit.choose(this, withdrawOrderActivityDctvDrawUse, R.array.drawUseArray);
                break;
            case R.id.withdrawOrderActivityRlDrawAccountNumber:
                withdrawOrderActivityKit.choose(this, withdrawOrderActivityDctvDrawAccountNumber, R.array.drawAccountNumberArray);
                break;
            case R.id.withdrawOrderActivityRlAppointmentTime:
                withdrawOrderActivityKit.chooseAppointmentTime(this, withdrawOrderActivityDctvAppointmentTime);
                break;
            case R.id.mainActivityMbEnsure:
                withdrawOrderActivityKit.ensure(this, withdrawOrderActivityDctvDrawUse, withdrawOrderActivityDctvDrawAccountNumber, withdrawOrderActivityDctvAppointmentTime, subBranchInformationDataBaseTable);
                break;
            case R.id.mainActivityTvAppointmentRecord:
                ToastKit.showShort(getString(R.string.pleaseWaitAndSee));
                break;
            default:
                break;
        }
    }
}