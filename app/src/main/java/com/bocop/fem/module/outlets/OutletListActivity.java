package com.bocop.fem.module.outlets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.bocop.fem.R;
import com.bocop.fem.base.BaseActivity;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.bocop.fem.module.outlets.kit.OutletListActivityKit;
import com.chaos.litepal.kit.LitePalKit;
import com.chaos.util.java.statusbar.StatusBarUtils;
import com.chaos.widget.searchview.SearchViewKit;
import com.google.android.material.appbar.MaterialToolbar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc: 网点列表页
 * @author: zsp
 * @date: 2021/8/16 10:50 上午
 */
public class OutletListActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.outletListActivityMt)
    MaterialToolbar outletListActivityMt;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.outletListActivitySv)
    SearchView outletListActivitySv;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.outletListActivityRv)
    RecyclerView outletListActivityRv;
    /**
     * 网点列表页配套元件
     */
    private OutletListActivityKit outletListActivityKit;

    /**
     * 布局资源 ID
     *
     * @return 布局资源 ID
     */
    @Override
    protected int layoutResId() {
        return R.layout.activity_outlet_list;
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
        SearchViewKit.setSearchView(this, outletListActivitySv, R.dimen.sp_12, R.color.fontInput, R.color.fontHint);
    }

    /**
     * 初始配置
     */
    @Override
    protected void initConfiguration() {
        outletListActivityKit = new OutletListActivityKit();
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        // MaterialToolbar
        outletListActivityMt.setNavigationOnClickListener(view -> finish());
        // SearchView
        outletListActivitySv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                outletListActivityKit.searchOutletAndDisplay(OutletListActivity.this, outletListActivityRv, query, LitePalKit.getInstance().findAll(SubBranchInformationDataBaseTable.class));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                outletListActivityKit.displayOutlet(OutletListActivity.this, outletListActivityRv, LitePalKit.getInstance().findAll(SubBranchInformationDataBaseTable.class));
                return false;
            }
        });
    }

    /**
     * 开始逻辑
     */
    @Override
    protected void startLogic() {
        outletListActivityKit.displayOutlet(this, outletListActivityRv, LitePalKit.getInstance().findAll(SubBranchInformationDataBaseTable.class));
    }

    @OnClick({R.id.outletsListActivityTv})
    public void onViewClicked(@NonNull View view) {
        if (view.getId() == R.id.outletsListActivityTv) {
            outletListActivityKit.searchOutletAndDisplay(OutletListActivity.this, outletListActivityRv, outletListActivitySv.getQuery().toString(), LitePalKit.getInstance().findAll(SubBranchInformationDataBaseTable.class));
        }
    }
}