package com.bocop.fem.module.cash.kit;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bocop.fem.R;
import com.bocop.fem.base.BaseActivity;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.bocop.fem.module.cash.WithdrawOrderResultActivity;
import com.bocop.fem.value.Constant;
import com.chaos.util.java.intent.IntentJump;
import com.chaos.util.java.toast.ToastKit;
import com.chaos.widget.dialog.materialalertdialog.SingleChooseMaterialAlertDialogKit;
import com.chaos.widget.materialdatepicker.MaterialDatePickerKit;
import com.chaos.widget.textview.DrawableCenterTextView;

import java.lang.ref.WeakReference;

/**
 * Created on 2021/8/17
 *
 * @author zsp
 * @desc 取现预约配套元件
 */
public class WithdrawOrderActivityKit {
    /**
     * 展示
     *
     * @param appCompatActivity                        活动
     * @param withdrawOrderActivityTvAppointmentOutlet 预约网点
     */
    public void display(@NonNull AppCompatActivity appCompatActivity, TextView withdrawOrderActivityTvAppointmentOutlet) {
        SubBranchInformationDataBaseTable subBranchInformationDataBaseTable = (SubBranchInformationDataBaseTable) appCompatActivity.getIntent().getSerializableExtra(Constant.OUTLET);
        if (subBranchInformationDataBaseTable != null) {
            withdrawOrderActivityTvAppointmentOutlet.setText(subBranchInformationDataBaseTable.getName());
        }
    }

    /**
     * 选择
     *
     * @param appCompatActivity      活动
     * @param drawableCenterTextView DrawableCenterTextView
     * @param stringArrayResId       字符数组资源 ID
     */
    public void choose(AppCompatActivity appCompatActivity, @NonNull DrawableCenterTextView drawableCenterTextView, int stringArrayResId) {
        SingleChooseMaterialAlertDialogKit.getInstance().show(appCompatActivity, appCompatActivity.getResources().getStringArray(stringArrayResId), drawableCenterTextView::setText);
    }

    /**
     * 选择预约时间
     *
     * @param appCompatActivity      活动
     * @param drawableCenterTextView DrawableCenterTextView
     */
    public void chooseAppointmentTime(AppCompatActivity appCompatActivity, DrawableCenterTextView drawableCenterTextView) {
        MaterialDatePickerKit.getInstance().show(appCompatActivity, new MaterialDatePickerKit.MaterialDatePickerKitInterface() {
            @Override
            public void onPositiveButtonClick(String date) {
                drawableCenterTextView.setText(date);
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });
    }

    /**
     * 确定
     *
     * @param appCompatActivity                          活动
     * @param withdrawOrderActivityDctvDrawUse           取款用途
     * @param withdrawOrderActivityDctvDrawAccountNumber 取款账号
     * @param withdrawOrderActivityDctvAppointmentTime   预约时间
     * @param subBranchInformationDataBaseTable          支行信息数据库表
     */
    public void ensure(@NonNull AppCompatActivity appCompatActivity, @NonNull DrawableCenterTextView withdrawOrderActivityDctvDrawUse, DrawableCenterTextView withdrawOrderActivityDctvDrawAccountNumber, DrawableCenterTextView withdrawOrderActivityDctvAppointmentTime, SubBranchInformationDataBaseTable subBranchInformationDataBaseTable) {
        if (TextUtils.equals(appCompatActivity.getString(R.string.pleaseChoose), withdrawOrderActivityDctvDrawUse.getText().toString())) {
            ToastKit.showShort(appCompatActivity.getString(R.string.pleaseChooseDrawUse));
        } else if (TextUtils.equals(appCompatActivity.getString(R.string.pleaseChoose), withdrawOrderActivityDctvDrawAccountNumber.getText().toString())) {
            ToastKit.showShort(appCompatActivity.getString(R.string.pleaseChooseDrawAccountNumber));
        } else if (TextUtils.equals(appCompatActivity.getString(R.string.pleaseChoose), withdrawOrderActivityDctvAppointmentTime.getText().toString())) {
            ToastKit.showShort(appCompatActivity.getString(R.string.pleaseChooseAppointmentTime));
        } else {
            WeakReference<AppCompatActivity> weakReference = new WeakReference<>(appCompatActivity);
            BaseActivity baseActivity = (BaseActivity) weakReference.get();
            baseActivity.commonLoading(weakReference.get().getString(R.string.appointing), null);
            new Handler(appCompatActivity.getMainLooper()).postDelayed(() -> {
                baseActivity.dismissLoading();
                Intent intent = new Intent();
                intent.putExtra(Constant.OUTLET, subBranchInformationDataBaseTable);
                IntentJump.getInstance().jump(intent, appCompatActivity, true, WithdrawOrderResultActivity.class);
            }, 2000);
        }
    }
}
