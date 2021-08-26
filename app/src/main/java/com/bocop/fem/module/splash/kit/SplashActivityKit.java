package com.bocop.fem.module.splash.kit;

import android.Manifest;

import androidx.fragment.app.FragmentActivity;

import com.bocop.fem.MainActivity;
import com.bocop.fem.R;
import com.chaos.util.java.activity.ActivitySuperviseManager;
import com.chaos.util.java.intent.IntentJump;
import com.permissionx.guolindev.PermissionX;

/**
 * Created on 2021/8/16
 *
 * @author zsp
 * @desc 闪屏页配套元件
 */
public class SplashActivityKit {
    /**
     * 请求权限
     *
     * @param fragmentActivity FragmentActivity
     */
    public void requestPermission(FragmentActivity fragmentActivity) {
        PermissionX.init(fragmentActivity)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList, fragmentActivity.getString(R.string.suggestToAllowThesePermissions), fragmentActivity.getString(R.string.agree), fragmentActivity.getString(R.string.cancel)))
                .explainReasonBeforeRequest()
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        IntentJump.getInstance().jump(null, fragmentActivity, true, MainActivity.class);
                    } else {
                        ActivitySuperviseManager.getInstance().appExit();
                    }
                });
    }
}
