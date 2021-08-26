package com.bocop.fem.module.outlets.kit;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bocop.fem.MainActivity;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.bocop.fem.module.outlets.adapter.OutletListAdapter;
import com.bocop.fem.value.Constant;
import com.chaos.util.java.intent.IntentJump;
import com.chaos.util.java.pinyin.HanZiConvertToPinYin;
import com.chaos.widget.recyclerview.configure.RecyclerViewConfigure;
import com.chaos.widget.recyclerview.controller.RecyclerViewDisplayController;
import com.chaos.widget.recyclerview.listener.OnRecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2021/8/16
 *
 * @author zsp
 * @desc 网点列表页配套元件
 */
public class OutletListActivityKit {
    /**
     * 展示网点
     *
     * @param appCompatActivity                     活动
     * @param recyclerView                          控件
     * @param subBranchInformationDataBaseTableList 数据集合
     */
    public void displayOutlet(AppCompatActivity appCompatActivity, RecyclerView recyclerView, List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTableList) {
        // 控件
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(appCompatActivity, recyclerView);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerViewConfigure.linearVerticalLayout(true, 50, true, true, false);
        }
        // 适配器
        OutletListAdapter outletListAdapter = new OutletListAdapter(appCompatActivity);
        outletListAdapter.setOutletsListData(subBranchInformationDataBaseTableList);
        outletListAdapter.setOnRecyclerViewOnItemClickListener(new OnRecyclerViewOnItemClickListener() {
            @Override
            public <T> void onItemClick(View view, int position, T t) {
                SubBranchInformationDataBaseTable subBranchInformationDataBaseTable = (SubBranchInformationDataBaseTable) t;
                Intent intent = new Intent();
                intent.putExtra(Constant.OUTLET, subBranchInformationDataBaseTable);
                IntentJump.getInstance().jump(intent, appCompatActivity, true, MainActivity.class);
            }
        });
        // 展示
        RecyclerViewDisplayController.display(recyclerView, outletListAdapter);
    }

    /**
     * 搜索网点并展示
     *
     * @param appCompatActivity                     活动
     * @param recyclerView                          控件
     * @param keyword                               关键词
     * @param subBranchInformationDataBaseTableList 数据集合
     */
    public void searchOutletAndDisplay(AppCompatActivity appCompatActivity, RecyclerView recyclerView, String keyword, @NonNull List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTableList) {
        List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTableResultList = new ArrayList<>();
        for (SubBranchInformationDataBaseTable subBranchInformationDataBaseTable : subBranchInformationDataBaseTableList) {
            HanZiConvertToPinYin hanZiConvertToPinYin = HanZiConvertToPinYin.getInstance();
            String name = hanZiConvertToPinYin.convertAll(subBranchInformationDataBaseTable.getName());
            String address = hanZiConvertToPinYin.convertAll(subBranchInformationDataBaseTable.getAddress());
            String currentDistance = hanZiConvertToPinYin.convertAll(subBranchInformationDataBaseTable.getCurrentDistance());
            String queQueue = hanZiConvertToPinYin.convertAll(String.valueOf(subBranchInformationDataBaseTable.getQueQueue()));
            String contactNumber = hanZiConvertToPinYin.convertAll(subBranchInformationDataBaseTable.getContactNumber());
            // 拼音搜索
            boolean baseOnName = name.contains(keyword) || subBranchInformationDataBaseTable.getName().contains(keyword);
            boolean baseAddress = address.contains(keyword) || subBranchInformationDataBaseTable.getAddress().contains(keyword);
            boolean baseCurrentDistance = currentDistance.contains(keyword) || subBranchInformationDataBaseTable.getCurrentDistance().contains(keyword);
            boolean baseQueQueue = queQueue.contains(keyword) || String.valueOf(subBranchInformationDataBaseTable.getQueQueue()).contains(keyword);
            boolean baseContactNumber = contactNumber.contains(keyword) || subBranchInformationDataBaseTable.getContactNumber().contains(keyword);
            if (baseOnName || baseAddress || baseCurrentDistance || baseQueQueue || baseContactNumber) {
                subBranchInformationDataBaseTableResultList.add(subBranchInformationDataBaseTable);
            }
        }
        displayOutlet(appCompatActivity, recyclerView, subBranchInformationDataBaseTableResultList);
    }
}
