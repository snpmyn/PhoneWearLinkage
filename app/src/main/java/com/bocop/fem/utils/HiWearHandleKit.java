package com.bocop.fem.utils;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bocop.fem.MainActivity;
import com.bocop.fem.R;
import com.bocop.fem.base.BaseActivity;
import com.chaos.util.java.intent.IntentJump;
import com.chaos.widget.dialog.materialalertdialog.MyMaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huawei.wearengine.auth.Permission;
import com.huawei.wearengine.device.Device;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

/**
 * Created on 2021/8/23
 *
 * @author zsp
 * @desc HiWear 操作配套元件
 */
public class HiWearHandleKit {
    private static HiWearHandleKit instance;
    private Disposable disposable;
    private int messageContentInner;

    public static HiWearHandleKit getInstance() {
        if (null == instance) {
            synchronized (HiWearHandleKit.class) {
                if (null == instance) {
                    instance = new HiWearHandleKit();
                }
            }
        }
        return instance;
    }

    /**
     * 查询多个权限
     *
     * @param appCompatActivity 活动
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     */
    public void checkPermissions(AppCompatActivity appCompatActivity, String devicePkgName, String peerFingerPrint, String messageContent) {
        WeakReference<AppCompatActivity> weakReference = new WeakReference<>(appCompatActivity);
        BaseActivity baseActivity = (BaseActivity) weakReference.get();
        baseActivity.commonLoading(weakReference.get().getString(R.string.sending), null);
        Permission[] permissions = {Permission.DEVICE_MANAGER, Permission.NOTIFY};
        HiWearApiKit.getInstance().checkPermissions(appCompatActivity, permissions, new HiWearInterface.CheckPermissions() {
            @Override
            public void onSuccess(Boolean[] booleans) {
                checkWearDeviceInfo(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent);
            }

            @Override
            public void onFailure(Exception e) {
                requestPermission(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent);
            }
        });
    }

    /**
     * 查询穿戴设备信息
     *
     * @param appCompatActivity 活动
     * @param baseActivity      活动
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     */
    private void checkWearDeviceInfo(AppCompatActivity appCompatActivity, BaseActivity baseActivity, String devicePkgName, String peerFingerPrint, String messageContent) {
        HiWearApiKit.getInstance().checkWearDeviceInfo(appCompatActivity, new HiWearInterface.CheckWearDeviceInfo() {
            @Override
            public void onSuccess(List<Device> devices, Device connectedDevice) {
                checkWearThirdApplicationOnline(appCompatActivity, baseActivity, connectedDevice, devicePkgName, peerFingerPrint, messageContent);
            }

            @Override
            public void onFailure(Exception e) {
                showDialog(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent, appCompatActivity.getString(R.string.wearNotConnect));
            }
        });
    }

    /**
     * 检测穿戴设备侧第三方应用在线
     *
     * @param appCompatActivity 活动
     * @param baseActivity      活动
     * @param connectedDevice   已连接设备
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     */
    private void checkWearThirdApplicationOnline(AppCompatActivity appCompatActivity, BaseActivity baseActivity, Device connectedDevice, String devicePkgName, String peerFingerPrint, String messageContent) {
        HiWearApiKit.getInstance().checkWearThirdApplicationOnline(appCompatActivity, connectedDevice, devicePkgName, new HiWearInterface.CheckWearThirdApplicationOnline() {
            @Override
            public void onPingResult(int errCode) {

            }

            @Override
            public void onSuccess(Void successVoid) {
                // 穿戴侧设备熄屏时需延时发送，否仅唤醒屏幕。
                new Handler(appCompatActivity.getMainLooper()).postDelayed(() -> sendMessage(appCompatActivity, baseActivity, connectedDevice, devicePkgName, peerFingerPrint, messageContent), 600);
            }

            @Override
            public void onFailure(Exception e) {
                showDialog(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent, appCompatActivity.getString(R.string.wearThirdApplicationNotInstall));
            }
        });
    }

    /**
     * 发送消息
     *
     * @param appCompatActivity 活动
     * @param baseActivity      活动
     * @param connectedDevice   已连接设备
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     */
    private void sendMessage(AppCompatActivity appCompatActivity, BaseActivity baseActivity, Device connectedDevice, String devicePkgName, String peerFingerPrint, String messageContent) {
        HiWearApiKit.getInstance().sendMessage(appCompatActivity, connectedDevice, devicePkgName, peerFingerPrint, messageContent, new HiWearInterface.SendMessage() {
            @Override
            public void onSendResult(int resultCode) {

            }

            @Override
            public void onSendProgress(long progress) {

            }

            @Override
            public void onSuccess(Void successVoid) {
                // 穿戴侧设备熄屏时需延时发送，否仅唤醒屏幕。
                new Handler(appCompatActivity.getMainLooper()).postDelayed(() -> sendMessageTwo(appCompatActivity, baseActivity, connectedDevice, devicePkgName, peerFingerPrint, messageContent), 600);
            }

            @Override
            public void onFailure(Exception e) {
                showDialog(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent, appCompatActivity.getString(R.string.sendMessageFail));
            }
        });
    }

    /**
     * 发送消息二
     *
     * @param appCompatActivity 活动
     * @param baseActivity      活动
     * @param connectedDevice   已连接设备
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     */
    private void sendMessageTwo(AppCompatActivity appCompatActivity, BaseActivity baseActivity, Device connectedDevice, String devicePkgName, String peerFingerPrint, String messageContent) {
        messageContentInner = Integer.parseInt(messageContent);
        disposable = Observable.interval(0, 6, TimeUnit.SECONDS)
                .map(aLong -> aLong)
                .subscribe(count -> {
                    Timber.i("accept %s", count);
                    HiWearApiKit.getInstance().sendMessage(appCompatActivity, connectedDevice, devicePkgName, peerFingerPrint, String.valueOf(messageContentInner), new HiWearInterface.SendMessage() {
                        @Override
                        public void onSendResult(int resultCode) {

                        }

                        @Override
                        public void onSendProgress(long progress) {

                        }

                        @Override
                        public void onSuccess(Void successVoid1) {
                            baseActivity.dismissLoading();
                            messageContentInner--;
                        }

                        @Override
                        public void onFailure(Exception e) {
                            showDialog(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent, appCompatActivity.getString(R.string.sendMessageFail));
                        }
                    });
                });
    }

    /**
     * 请求权限
     *
     * @param appCompatActivity 活动
     * @param baseActivity      活动
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     */
    private void requestPermission(AppCompatActivity appCompatActivity, BaseActivity baseActivity, String devicePkgName, String peerFingerPrint, String messageContent) {
        Permission[] permissions = {Permission.DEVICE_MANAGER, Permission.NOTIFY};
        HiWearApiKit.getInstance().requestPermission(appCompatActivity, permissions, new HiWearInterface.RequestPermission() {
            @Override
            public void onOk(Permission[] permissions) {

            }

            @Override
            public void onCancel() {
                showDialog(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent, appCompatActivity.getString(R.string.userCancelAuthorization));
            }

            @Override
            public void onSuccess(Void successVoid) {
                checkPermissions(appCompatActivity, devicePkgName, peerFingerPrint, messageContent);
            }

            @Override
            public void onFailure(Exception e) {
                showDialog(appCompatActivity, baseActivity, devicePkgName, peerFingerPrint, messageContent, appCompatActivity.getString(R.string.requestPermissionFail));
            }
        });
    }

    /**
     * 显示对话框
     *
     * @param appCompatActivity 活动
     * @param baseActivity      活动
     * @param devicePkgName     设备包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     * @param hint              提示
     */
    private void showDialog(AppCompatActivity appCompatActivity, @NonNull BaseActivity baseActivity, String devicePkgName, String peerFingerPrint, String messageContent, String hint) {
        baseActivity.dismissLoading();
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MyMaterialAlertDialogBuilder(appCompatActivity);
        materialAlertDialogBuilder.setMessage(hint)
                .setNegativeButton(appCompatActivity.getString(R.string.returnHomepage), (dialog, which) -> {
                    dialog.dismiss();
                    IntentJump.getInstance().jump(null, appCompatActivity, true, MainActivity.class);
                })
                .setPositiveButton(appCompatActivity.getString(R.string.tryAgain), (dialog, which) -> {
                    dialog.dismiss();
                    checkPermissions(appCompatActivity, devicePkgName, peerFingerPrint, messageContent);
                }).setCancelable(false).show();
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
