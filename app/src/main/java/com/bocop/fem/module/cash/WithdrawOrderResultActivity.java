package com.bocop.fem.module.cash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bocop.fem.R;
import com.bocop.fem.base.BaseActivity;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.bocop.fem.module.cash.kit.WithdrawOrderResultActivityKit;
import com.bocop.fem.utils.HiWearHandleKit;
import com.bocop.fem.value.Constant;
import com.chaos.util.java.intent.IntentVerify;
import com.chaos.util.java.statusbar.StatusBarUtils;
import com.google.android.material.appbar.MaterialToolbar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc: 取现预约结果页
 * @author: zsp
 * @date: 2021/8/17 3:10 下午
 */
public class WithdrawOrderResultActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderResultActivityMt)
    MaterialToolbar withdrawOrderResultActivityMt;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.withdrawOrderResultActivityTvReturnHomepage)
    TextView withdrawOrderResultActivityTvReturnHomepage;
    /**
     * 取现预约结果页配套元件
     */
    private WithdrawOrderResultActivityKit withdrawOrderResultActivityKit;
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
        return R.layout.activity_withdraw_order_result;
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
        withdrawOrderResultActivityKit = new WithdrawOrderResultActivityKit();
        subBranchInformationDataBaseTable = (SubBranchInformationDataBaseTable) IntentVerify.getSerializableExtra(getIntent(), Constant.OUTLET);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        withdrawOrderResultActivityMt.setNavigationOnClickListener(view -> finish());
    }

    /**
     * 开始逻辑
     */
    @Override
    protected void startLogic() {
        HiWearHandleKit.getInstance().checkPermissions(this, "com.bocop.watch", "com.bocop.watch_BGNTi0xttHSpG89/0lm+LBZV5haw5qBGoNCcjEDJybay3YWcgjRZHyTrkr1Ft2gxhbBFpOG6+RhUhVAtufkeCTE=", String.valueOf(subBranchInformationDataBaseTable.getQueQueue()));
    }

    @OnClick({R.id.withdrawOrderResultActivityTvReturnHomepage})
    public void onViewClicked(@NonNull View view) {
        if (view.getId() == R.id.withdrawOrderResultActivityTvReturnHomepage) {
            withdrawOrderResultActivityKit.returnHomepage(this);
        }
    }
}