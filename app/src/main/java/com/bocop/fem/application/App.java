package com.bocop.fem.application;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.bocop.fem.BuildConfig;
import com.bocop.fem.value.Folder;
import com.chaos.basic.BasicApp;
import com.chaos.litepal.configure.LitePalInitConfigure;
import com.chaos.util.java.activity.ActivitySuperviseManager;
import com.chaos.util.java.storage.mmkv.MmkvInitConfigure;
import com.chaos.widget.crash.CrashManagerInitConfigure;
import com.tencent.mmkv.MMKVContentChangeNotification;
import com.tencent.mmkv.MMKVHandler;
import com.tencent.mmkv.MMKVLogLevel;
import com.tencent.mmkv.MMKVRecoverStrategic;

import org.jetbrains.annotations.NotNull;

import configure.FragmentationInitConfig;
import timber.log.Timber;

/**
 * Created on 2021/8/13
 *
 * @author zsp
 * @desc 应用
 */
public class App extends BasicApp implements MMKVHandler, MMKVContentChangeNotification {
    /**
     * 应用程序创调
     * <p>
     * 创和实例化任何应用程序状态变量或共享资源变量，方法内获 Application 单例。
     */
    @Override
    public void onCreate() {
        Timber.d("onCreate");
        super.onCreate();
        // 初始化配置
        initConfiguration();
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        // 全局监听 Activity 生命周期
        registerActivityListener();
        // MMKV
        MmkvInitConfigure.initMmkv(this, BuildConfig.DEBUG, this, this);
        // 崩溃管理器
        CrashManagerInitConfigure.getInstance(this, Folder.CRASH);
        // LitePal
        LitePalInitConfigure.initLitePal(this);
        // Fragmentation
        FragmentationInitConfig.initFragmentation(BuildConfig.DEBUG);
    }

    /**
     * Activity 全局监听
     */
    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                // 添监听到创事件 Activity 至集合
                ActivitySuperviseManager.getInstance().pushActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                // 移监听到销事件 Activity 出集合
                ActivitySuperviseManager.getInstance().removeActivity(activity);
            }
        });
    }

    @Override
    public void onContentChangedByOuterProcess(String s) {
        Timber.i("[content changed] %s", s);
    }

    @Override
    public MMKVRecoverStrategic onMMKVCRCCheckFail(String s) {
        return MMKVRecoverStrategic.OnErrorRecover;
    }

    @Override
    public MMKVRecoverStrategic onMMKVFileLengthError(String s) {
        return MMKVRecoverStrategic.OnErrorRecover;
    }

    @Override
    public boolean wantLogRedirecting() {
        return true;
    }

    @Override
    public void mmkvLog(@NotNull MMKVLogLevel mmkvLogLevel, String s, int i, String s1, String s2) {
        String log = ("< " + s + " : " + i + " :: " + s1 + " > " + s2);
        switch (mmkvLogLevel) {
            case LevelDebug:
                Timber.d("[redirect logging MMKV] %s", log);
                break;
            case LevelInfo:
                Timber.i("[redirect logging MMKV] %s", log);
                break;
            case LevelWarning:
                Timber.w("[redirect logging MMKV] %s", log);
                break;
            case LevelError:
            case LevelNone:
                Timber.e("[redirect logging MMKV] %s", log);
                break;
            default:
                break;
        }
    }
}

