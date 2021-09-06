package com.bocop.fem.application;

import androidx.annotation.NonNull;

import com.bocop.fem.BuildConfig;
import com.bocop.fem.value.Folder;
import com.chaos.litepal.configure.LitePalInitConfigure;
import com.chaos.litepool.application.LitePoolApp;
import com.chaos.util.java.storage.mmkv.MmkvInitConfigure;
import com.chaos.widget.crash.CrashManagerInitConfigure;
import com.tencent.mmkv.MMKVContentChangeNotification;
import com.tencent.mmkv.MMKVHandler;
import com.tencent.mmkv.MMKVLogLevel;
import com.tencent.mmkv.MMKVRecoverStrategic;

import configure.FragmentationInitConfig;
import timber.log.Timber;

/**
 * Created on 2021/8/13
 *
 * @author zspx
 * @desc 应用
 */
public class App extends LitePoolApp implements MMKVHandler, MMKVContentChangeNotification {
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
        // MMKV
        MmkvInitConfigure.initMmkv(this, BuildConfig.DEBUG, this, this);
        // 崩溃管理器
        CrashManagerInitConfigure.getInstance(this, Folder.CRASH);
        // LitePal
        LitePalInitConfigure.initLitePal(this);
        // Fragmentation
        FragmentationInitConfig.initFragmentation(BuildConfig.DEBUG);
    }

    @Override
    public void onContentChangedByOuterProcess(String mmapID) {
        Timber.i("[content changed] %s", mmapID);
    }

    @Override
    public MMKVRecoverStrategic onMMKVCRCCheckFail(String mmapID) {
        return MMKVRecoverStrategic.OnErrorRecover;
    }

    @Override
    public MMKVRecoverStrategic onMMKVFileLengthError(String mmapID) {
        return MMKVRecoverStrategic.OnErrorRecover;
    }

    @Override
    public boolean wantLogRedirecting() {
        return true;
    }

    @Override
    public void mmkvLog(@NonNull MMKVLogLevel level, String file, int line, String function, String message) {
        String log = ("< " + file + " : " + line + " :: " + function + " > " + message);
        switch (level) {
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

