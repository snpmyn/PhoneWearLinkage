package com.bocop.fem.module.outlets.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bocop.fem.R;
import com.bocop.fem.database.SubBranchInformationDataBaseTable;
import com.chaos.util.java.list.ListUtils;
import com.chaos.widget.recyclerview.listener.OnRecyclerViewOnItemClickListener;
import com.chaos.widget.textview.DrawableCenterTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2021/8/16
 *
 * @author zsp
 * @desc 网点列表适配器
 */
public class OutletListAdapter extends RecyclerView.Adapter<OutletListAdapter.ViewHolder> {
    private final Context context;
    private List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTables;
    private OnRecyclerViewOnItemClickListener onRecyclerViewOnItemClickListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public OutletListAdapter(Context context) {
        this.context = context;
        this.subBranchInformationDataBaseTables = new ArrayList<>();
    }

    public void setOutletsListData(List<SubBranchInformationDataBaseTable> subBranchInformationDataBaseTables) {
        this.subBranchInformationDataBaseTables = subBranchInformationDataBaseTables;
    }

    public void setOnRecyclerViewOnItemClickListener(OnRecyclerViewOnItemClickListener onRecyclerViewOnItemClickListener) {
        this.onRecyclerViewOnItemClickListener = onRecyclerViewOnItemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.outlet_list_item, viewGroup, false);
        // 点击监听
        view.setOnClickListener(v -> {
            int position = (Integer) view.getTag();
            onRecyclerViewOnItemClickListener.onItemClick(v, position, subBranchInformationDataBaseTables.get(position));
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        SubBranchInformationDataBaseTable subBranchInformationDataBaseTable = subBranchInformationDataBaseTables.get(position);
        // 名称
        holder.outletListItemTvName.setText(subBranchInformationDataBaseTable.getName());
        // 位置
        holder.outletListItemDctvAddress.setText(subBranchInformationDataBaseTable.getAddress());
        // 联系电话
        holder.outletListItemDctvContactNumber.setText(subBranchInformationDataBaseTable.getContactNumber());
    }

    @Override
    public int getItemCount() {
        if (ListUtils.listIsNotEmpty(subBranchInformationDataBaseTables)) {
            return subBranchInformationDataBaseTables.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.outletListItemTvName)
        TextView outletListItemTvName;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.outletListItemDctvAddress)
        DrawableCenterTextView outletListItemDctvAddress;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.outletListItemDctvContactNumber)
        DrawableCenterTextView outletListItemDctvContactNumber;

        private ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}