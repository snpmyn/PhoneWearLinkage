package com.bocop.fem.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created on 2021/8/16
 *
 * @author zsp
 * @desc 支行信息数据库表
 */
public class SubBranchInformationDataBaseTable extends LitePalSupport implements Serializable {
    /**
     * 名称
     */
    @Column(nullable = false, unique = true)
    private final String name;
    /**
     * 位置
     */
    @Column(nullable = false, unique = true)
    private final String address;
    /**
     * 当前距离
     */
    @Column(nullable = false)
    private final String currentDistance;
    /**
     * 排队人数
     */
    @Column(nullable = false)
    private final int queQueue;
    /**
     * 联系电话
     */
    @Column(nullable = false, unique = true)
    private final String contactNumber;

    /**
     * constructor
     *
     * @param name            名称
     * @param address         位置
     * @param currentDistance 当前距离
     * @param queQueue        排队人数
     * @param contactNumber   联系电话
     */
    public SubBranchInformationDataBaseTable(String name, String address, String currentDistance, int queQueue, String contactNumber) {
        this.name = name;
        this.address = address;
        this.currentDistance = currentDistance;
        this.queQueue = queQueue;
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCurrentDistance() {
        return currentDistance;
    }

    public int getQueQueue() {
        return queQueue;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
