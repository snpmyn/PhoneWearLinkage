package com.bocop.fem.module.cash.kit;

import androidx.appcompat.app.AppCompatActivity;

import com.bocop.fem.MainActivity;
import com.chaos.util.java.intent.IntentJump;

/**
 * Created on 2021/8/19
 *
 * @author zsp
 * @desc 取现预约结果页配套元件
 */
public class WithdrawOrderResultActivityKit {
    /**
     * 返回主页
     *
     * @param appCompatActivity 活动
     */
    public void returnHomepage(AppCompatActivity appCompatActivity) {
        IntentJump.getInstance().jump(null, appCompatActivity, true, MainActivity.class);
    }
}
