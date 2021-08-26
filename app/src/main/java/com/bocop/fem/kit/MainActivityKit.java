package com.bocop.fem.kit;

import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bocop.fem.R;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.bocop.fem.module.cash.WithdrawOrderActivity;
import com.bocop.fem.value.Constant;
import com.chaos.litepal.kit.LitePalKit;
import com.chaos.util.java.intent.IntentJump;
import com.chaos.util.java.toast.ToastKit;
import com.chaos.widget.choose.provincialandurbanlinkage.Location;
import com.chaos.widget.choose.provincialandurbanlinkage.ProvincialAndUrbanLinkage;
import com.chaos.widget.textview.DrawableCenterTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

/**
 * Created on 2021/8/18
 *
 * @author zsp
 * @desc 主页配套元件
 */
public class MainActivityKit {
    private Disposable disposable;
    private SubBranchInformationDataBaseTable subBranchInformationDataBaseTable;

    /**
     * 初始化网点
     *
     * @param appCompatActivity               活动
     * @param mainActivityTvName              名称
     * @param mainActivityTvAddress           位置
     * @param mainActivityDctvCurrentDistance 当前距离
     * @param mainActivityTvQueQueue          排队人数
     */
    public void stepOutlets(AppCompatActivity appCompatActivity, TextView mainActivityTvName, TextView mainActivityTvAddress, DrawableCenterTextView mainActivityDctvCurrentDistance, TextView mainActivityTvQueQueue) {
        if (null != LitePalKit.getInstance().findFirst(SubBranchInformationDataBaseTable.class)) {
            display(appCompatActivity, 0, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue);
            upgrade(appCompatActivity, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue);
            return;
        }
        List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTableList = new ArrayList<>(6);
        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable("西安中心浐灞半岛支行", "西安市东湖路（中新浐灞半岛A6区25号楼1单元）", "2.12", 22, "029-88323565"));
        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable("西安南郊唐都医院支行", "西安市东湖路（中新浐灞半岛A6区腾讯大厦旁）", "5.2", 16, "029-88321234"));
        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable("西安浐灞商务中心支行", "西安市浐灞生态区东湖1号", "4.5", 19, "029-85267895"));
        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable("西安热电厂支行", "浐灞区电厂西路", "8.1", 25, "029-85347859"));
        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable("西安酒十路支行", "西安市十里铺", "3.12", 23, "029-88326588"));
        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable("西安矿山路支行", "西安市矿山路", "6.25", 14, "009-88541256"));
        if (LitePalKit.getInstance().multiSave(subBranchInformationDataBaseTableList)) {
            display(appCompatActivity, 0, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue);
            upgrade(appCompatActivity, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue);
        } else {
            ToastKit.showShort(appCompatActivity.getString(R.string.stepOutletsFail));
        }
    }

    /**
     * 展示
     *
     * @param appCompatActivity               活动
     * @param flag                            标志
     *                                        0 打开应用进主页、1 网点列表页跳主页
     * @param mainActivityTvName              名称
     * @param mainActivityTvAddress           位置
     * @param mainActivityDctvCurrentDistance 当前距离
     * @param mainActivityTvQueQueue          排队人数
     */
    public void display(AppCompatActivity appCompatActivity, int flag, TextView mainActivityTvName, TextView mainActivityTvAddress, DrawableCenterTextView mainActivityDctvCurrentDistance, TextView mainActivityTvQueQueue) {
        if (flag == 0) {
            subBranchInformationDataBaseTable = LitePalKit.getInstance().findFirst(SubBranchInformationDataBaseTable.class);
        } else if ((flag == 1) && (null != appCompatActivity.getIntent().getSerializableExtra(Constant.OUTLET))) {
            subBranchInformationDataBaseTable = (SubBranchInformationDataBaseTable) appCompatActivity.getIntent().getSerializableExtra(Constant.OUTLET);
        }
        if (null != subBranchInformationDataBaseTable) {
            mainActivityTvName.setText(subBranchInformationDataBaseTable.getName());
            mainActivityTvAddress.setText(subBranchInformationDataBaseTable.getAddress());
            mainActivityDctvCurrentDistance.setText(subBranchInformationDataBaseTable.getCurrentDistance());
            mainActivityTvQueQueue.setText(String.valueOf(subBranchInformationDataBaseTable.getQueQueue()));
        }
    }

    /**
     * 更新
     *
     * @param appCompatActivity               活动
     * @param mainActivityTvName              名称
     * @param mainActivityTvAddress           位置
     * @param mainActivityDctvCurrentDistance 当前距离
     * @param mainActivityTvQueQueue          排队人数
     */
    public void upgrade(AppCompatActivity appCompatActivity, TextView mainActivityTvName, TextView mainActivityTvAddress, DrawableCenterTextView mainActivityDctvCurrentDistance, TextView mainActivityTvQueQueue) {
        disposable = Observable.interval(6, 6, TimeUnit.SECONDS)
                .map(aLong -> aLong)
                .subscribe(count -> {
                    Timber.i("accept %s", count);
                    List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTableList = new ArrayList<>(6);
                    for (SubBranchInformationDataBaseTable subBranchInformationDataBaseTable : LitePalKit.getInstance().findAll(SubBranchInformationDataBaseTable.class)) {
                        subBranchInformationDataBaseTableList.add(new SubBranchInformationDataBaseTable(subBranchInformationDataBaseTable.getName(), subBranchInformationDataBaseTable.getAddress(), subBranchInformationDataBaseTable.getCurrentDistance(), subBranchInformationDataBaseTable.getQueQueue() + 1, subBranchInformationDataBaseTable.getContactNumber()));
                    }
                    LitePalKit.getInstance().allDelete(SubBranchInformationDataBaseTable.class);
                    if (LitePalKit.getInstance().multiSave(subBranchInformationDataBaseTableList)) {
                        appCompatActivity.runOnUiThread(() -> display(appCompatActivity, 0, mainActivityTvName, mainActivityTvAddress, mainActivityDctvCurrentDistance, mainActivityTvQueQueue));
                    } else {
                        Timber.d(appCompatActivity.getString(R.string.upgradeFail));
                    }
                });
    }

    /**
     * 选择其它网点
     *
     * @param provincialAndUrbanLinkage 省市区联动
     * @param location                  定位
     */
    public void chooseOtherOutlets(@NonNull ProvincialAndUrbanLinkage provincialAndUrbanLinkage, Location location) {
        provincialAndUrbanLinkage.stepOptionsPickerView();
        if (!provincialAndUrbanLinkage.areShowing()) {
            provincialAndUrbanLinkage.setLocation(location);
            provincialAndUrbanLinkage.loadData();
        }
    }

    /**
     * 立即取号
     *
     * @param appCompatActivity 活动
     */
    public void takeNumberImmediately(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent();
        intent.putExtra(Constant.OUTLET, subBranchInformationDataBaseTable);
        IntentJump.getInstance().jump(intent, appCompatActivity, false, WithdrawOrderActivity.class);
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (null != disposable) {
            disposable.dispose();
        }
    }
}
