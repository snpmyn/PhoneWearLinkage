package com.bocop.fem.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.wearengine.HiWear;
import com.huawei.wearengine.auth.AuthCallback;
import com.huawei.wearengine.auth.AuthClient;
import com.huawei.wearengine.auth.Permission;
import com.huawei.wearengine.client.ServiceConnectionListener;
import com.huawei.wearengine.client.WearEngineClient;
import com.huawei.wearengine.device.Device;
import com.huawei.wearengine.device.DeviceClient;
import com.huawei.wearengine.p2p.CancelFileTransferCallBack;
import com.huawei.wearengine.p2p.FileIdentification;
import com.huawei.wearengine.p2p.Message;
import com.huawei.wearengine.p2p.P2pClient;
import com.huawei.wearengine.p2p.Receiver;
import com.huawei.wearengine.p2p.SendCallback;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import timber.log.Timber;

/**
 * Created on 2021/8/19
 *
 * @author zsp
 * @desc HiWear API 配套元件
 */
public class HiWearApiKit {
    private final String TAG = this.getClass().getSimpleName();
    private static HiWearApiKit instance;

    public static HiWearApiKit getInstance() {
        if (null == instance) {
            synchronized (HiWearApiKit.class) {
                if (null == instance) {
                    instance = new HiWearApiKit();
                }
            }
        }
        return instance;
    }

    /**
     * 查询可用穿戴设备
     * <p>
     * Wear Engine 提供查询用户是否有可用穿戴设备（即支持 Wear Engine 能力且与手机侧运动健康 App 处于连接状态的穿戴设备）的接口。
     * 该接口的调用无需用户授权，建议开发者在使用 Wear Engine 其他 API 接口前先实现该接口功能。
     *
     * @param appCompatActivity    活动
     * @param checkAvailableDevice HiWearInterface.CheckAvailableDevice
     */
    public void checkAvailableDevice(AppCompatActivity appCompatActivity, HiWearInterface.CheckAvailableDevice checkAvailableDevice) {
        // 步骤 1：获取 DeviceClient 对象
        DeviceClient deviceClient = HiWear.getDeviceClient(appCompatActivity);
        // 步骤 2：调用 hasAvailableDevices 方法，查询用户是否有可用穿戴设备
        deviceClient.hasAvailableDevices().addOnSuccessListener(result -> {
            // 查询是否有可用设备任务执行成功，result 值表示是否有可用设备
            Timber.i("onSuccess %s", result);
            checkAvailableDevice.onSuccess(result);
        }).addOnFailureListener(e -> {
            // 查询是否有可用设备任务执行失败
            Timber.e("onFailure %s", e.getMessage());
            checkAvailableDevice.onFailure(e);
        });
    }

    /**
     * 查询单个权限
     * <p>
     * 为保护用户隐私，Wear Engine 的 API 需要用户授权才可以正常访问。建议开发者在终端用户首次调用 Wear Engine 开放能力的时候执行本章节操作。
     * 用于查询用户是否授予第三方应用权限。如果检测到用户未授权，请参见下一节申请用户穿戴设备权限向用户请求权限。
     * 若用户的运动健康 App 版本为 11.0.3.512 及以上，且对第三方应用未进行过授权操作，则消息通知 NOTIFY、设备基础信息 DEVICE_MANAGER 将默认授予第三方应用。建议在请求用户授权前，先使用该接口查询应用是否已有相关权限。
     * <p>
     * 请确保权限已在申请 Wear Engine 服务中审批通过，否则会遇到错误码为 8 的提示。
     *
     * @param appCompatActivity 活动
     * @param checkPermission   HiWearInterface.CheckPermission
     */
    public void checkPermission(AppCompatActivity appCompatActivity, HiWearInterface.CheckPermission checkPermission) {
        // 步骤 1：获取 AuthClient 对象
        AuthClient authClient = HiWear.getAuthClient(appCompatActivity);
        // 步骤 2：调用 checkPermission 方法，查询权限是否授予
        authClient.checkPermission(Permission.DEVICE_MANAGER).addOnSuccessListener(aBoolean -> {
            // 返回权限是否授予，true 为授予，false 为未授予
            Timber.i("onSuccess %s", aBoolean);
            checkPermission.onSuccess(aBoolean);
        }).addOnFailureListener(e -> {
            // 接口调用失败
            Timber.e("onFailure %s", e.getMessage());
            checkPermission.onFailure(e);
        });
    }

    /**
     * 查询多个权限
     * <p>
     * 为保护用户隐私，Wear Engine 的 API 需要用户授权才可以正常访问。建议开发者在终端用户首次调用 Wear Engine 开放能力的时候执行本章节操作。
     * 用于查询用户是否授予第三方应用权限。如果检测到用户未授权，请参见下一节申请用户穿戴设备权限向用户请求权限。
     * 若用户的运动健康 App 版本为 11.0.3.512 及以上，且对第三方应用未进行过授权操作，则消息通知 NOTIFY、设备基础信息 DEVICE_MANAGER 将默认授予第三方应用。建议在请求用户授权前，先使用该接口查询应用是否已有相关权限。
     * <p>
     * 请确保权限已在申请 Wear Engine 服务中审批通过，否则会遇到错误码为 8 的提示。
     *
     * @param appCompatActivity 活动
     * @param permissions       权限数组
     * @param checkPermissions  HiWearKitInterface.CheckPermissions
     */
    public void checkPermissions(AppCompatActivity appCompatActivity, Permission[] permissions, HiWearInterface.CheckPermissions checkPermissions) {
        // 步骤 1：获取 AuthClient 对象
        AuthClient authClient = HiWear.getAuthClient(appCompatActivity);
        // 或调用 checkPermissions 方法，查询一组权限是否授予
        authClient.checkPermissions(permissions).addOnSuccessListener(booleans -> {
            // 返回权限是否授予，true 为授予，false 为未授予，按照权限的查询顺序返回对应的值
            Timber.i("onSuccess %s", Arrays.toString(booleans));
            checkPermissions.onSuccess(booleans);
        }).addOnFailureListener(e -> {
            // 接口调用失败
            Timber.e("onFailure %s", e.getMessage());
            checkPermissions.onFailure(e);
        });
    }

    /**
     * 请求权限
     * <p>
     * 说明：可一次申请多个权限，多个权限之间用逗号分隔：authClient.requestPermission(authCallback, Permission.DEVICE_MANAGER,Permission.NOTIFY, 其它权限)。
     * 说明：调用该 API 后，会弹出授权界面，让用户选择授予权限。
     * <p>
     * 请确保向用户请求的权限已在申请 Wear Engine 服务中审批通过。否则会遇到错误码为 8 的提示。
     * 该功能可以多次调用，如果申请的权限之前已经授予了，不会再弹出授权页面，接口会返回已经授权的权限。
     * 通过入参的 Permission 对象，获取第三方应用需要的权限。参见获取的权限了解应用所需请求的权限类型。
     * 通过 AuthCallback 对象，返回用户的授权结果：onOk 的回调返回用户授权的权限列表；onCancel 表示取消授权。
     *
     * @param appCompatActivity 活动
     * @param permissions       权限数组
     * @param requestPermission HiWearKitInterface.RequestPermission
     */
    public void requestPermission(AppCompatActivity appCompatActivity, Permission[] permissions, HiWearInterface.RequestPermission requestPermission) {
        // 步骤 1：获取 AuthClient 对象
        AuthClient authClient = HiWear.getAuthClient(appCompatActivity);
        // 步骤 2：定义用户授权的回调对象
        AuthCallback authCallback = new AuthCallback() {
            @Override
            public void onOk(Permission[] permissions) {
                // 返回用户授予的权限列表
                Timber.i("onOk %s", Arrays.toString(permissions));
                requestPermission.onOk(permissions);
            }

            @Override
            public void onCancel() {
                // 用户取消授权
                Timber.i("onCancel 用户取消授权");
                requestPermission.onCancel();
            }
        };
        // 步骤 3：请求用户授权指定的权限（如：DEVICE_MANAGER、NOTIFY、设备管理权限）
        authClient.requestPermission(authCallback, permissions)
                .addOnSuccessListener(successVoid -> {
                    // 请求授权任务执行成功
                    Timber.i("onSuccess %s", successVoid);
                    requestPermission.onSuccess(successVoid);
                })
                .addOnFailureListener(e -> {
                    // 请求授权任务执行失败
                    Timber.e("onFailure %s", e.getMessage());
                    requestPermission.onFailure(e);
                });
    }

    /**
     * 查询穿戴设备信息
     * <p>
     * 手机侧应用获取已配对的华为穿戴设备列表，并从设备列表中选定需要操作的设备。
     * 应用在获取设备列表之前，需要向手机侧用户申请获取 DEVICE_MANAGER 权限的授权（参见请求用户授权），否则设备列表将获取失败。
     * <p>
     * 具有 Wear Engine 能力的穿戴设备才能通过 getBondedDevices 方法获取到。
     * 若用户的运动健康 App 版本为 11.0.3.512 及以上，用户对同一个设备进行了解绑并重新绑定操作后，设备详细信息中的 UUID 会发生变化，如果使用之前缓存的设备调用业务接口，有可能会出现 UUID 非法异常（ERROR_CODE_DEVICE_UUID_IS_INVALID），建议此时通过 getBondedDevices 方法重新获取设备。
     * 设备的详细信息参见 Device 对象。
     * <p>
     * 从设备列表中选定需要操作的设备（可选，若需要实现其他功能，如：应用间消息通信，则需要实现该步骤），必须是已连接的设备，不然后续操作会失败。
     * 华为运动健康 App 可以与多款穿戴设备配对，但同一时间只能连接一款穿戴设备。
     *
     * @param appCompatActivity   活动
     * @param checkWearDeviceInfo HiWearInterface.CheckWearDeviceInfo
     */
    public void checkWearDeviceInfo(AppCompatActivity appCompatActivity, HiWearInterface.CheckWearDeviceInfo checkWearDeviceInfo) {
        // 步骤 1：获取 DeviceClient 对象
        DeviceClient deviceClient = HiWear.getDeviceClient(appCompatActivity);
        // 步骤 2：获取已配对的设备列表
        deviceClient.getBondedDevices()
                .addOnSuccessListener(devices -> {
                    Timber.i("onSuccess %s", devices.toString());
                    // 步骤 3：从设备列表选定需要操作的设备，必须是已连接的设备，不然后续操作会失败
                    Device connectedDevice = null;
                    if (!devices.isEmpty()) {
                        // 说明：该部分为获取已连接的设备的代码逻辑，开发者需要根据实际情况修改，获取需要通信的设备
                        for (Device device : devices) {
                            if (device.isConnected()) {
                                connectedDevice = device;
                            }
                        }
                    }
                    checkWearDeviceInfo.onSuccess(devices, connectedDevice);
                })
                .addOnFailureListener(e -> {
                    // 获取设备列表异常时，处理相关的逻辑
                    Timber.e("onFailure %s", e.getMessage());
                    checkWearDeviceInfo.onFailure(e);
                });
    }

    /**
     * 检测穿戴设备侧第三方应用安装
     *
     * @param appCompatActivity                活动
     * @param connectedDevice                  已连接设备
     * @param devicePkgName                    设备包名
     * @param checkWearThirdApplicationInstall HiWearInterface.CheckWearThirdApplicationInstall
     */
    public void checkWearThirdApplicationInstall(AppCompatActivity appCompatActivity, Device connectedDevice, String devicePkgName, HiWearInterface.CheckWearThirdApplicationInstall checkWearThirdApplicationInstall) {
        // 步骤 2：获取点对点通信对象
        P2pClient p2pClient = HiWear.getP2pClient(appCompatActivity);
        // 步骤 3：检测穿戴设备侧对应的第三方应用是否安装
        if ((null != connectedDevice) && connectedDevice.isConnected()) {
            p2pClient.isAppInstalled(connectedDevice, devicePkgName)
                    .addOnSuccessListener(isAppInstalled -> {
                        // 检测指定的设备侧应用是否安装任务执行成功相关逻辑
                        // true：已安装 false：未安装
                        Timber.i("onSuccess %s", isAppInstalled);
                        checkWearThirdApplicationInstall.onSuccess(isAppInstalled);
                    })
                    .addOnFailureListener(e -> {
                        // 检测指定的设备侧应用是否安装任务执行失败时三方应用处理相关的逻辑
                        Timber.e("onFailure %s", e.getMessage());
                        checkWearThirdApplicationInstall.onFailure(e);
                    });
        }
    }

    /**
     * 检测穿戴设备侧第三方应用在线
     * <p>
     * 在发送点对点消息前，可以用 Ping 命令检测手机上的第三方应用是否能跟穿戴设备侧对应的第三方应用正常通信。
     * <p>
     * 参见穿戴设备信息查询章节，获取已配对的设备列表，并从设备列表中选定需要通信的设备。
     * 调用 HiWear 中的 getP2pClient 方法，获取 P2pClient 对象。
     * 调用 setPeerPkgName 方法，指定需要 ping 通信的穿戴设备侧应用包名（可选，当没有指定包名时，默认需要 ping 通信的穿戴设备侧包名与手机侧第三方应用的包名一致）。
     * 调用 ping 方法，检测穿戴设备侧对应的第三方应用是否在线。
     * <p>
     * 请确保穿戴设备和华为运动健康 App 处于连接状态。用户可进入 App “设备”界面查看设备是否在线。开发者可调用 isConnected 方法了解设备是否在线，如果返回 false（true：已连接；false：未连接）提醒用户重新连接设备。
     * <p>
     * ping 方法的调用有两个过程，一个是手机给设备发指令的过程，一个是设备给手机回响应的过程。
     * ping 方法的调用使用的是 Task 模式，通过 OnSuccessListener 和 OnFailureListener 回调来获取调用结果，属于手机给设备发指令的过程。如果回调到 OnSuccessListener 表示 ping 方法调用成功，如果回调到 OnFailureListener 表示 ping 方法调用失败。
     * ping 方法的 PingCallback 是收到设备返回信息接口，属于设备给手机回响应的过程。
     *
     * @param appCompatActivity               活动
     * @param connectedDevice                 已连接设备
     * @param peerPkgName                     需通信穿戴设备侧应用包名
     * @param checkWearThirdApplicationOnline HiWearInterface.CheckWearThirdApplicationOnline
     */
    public void checkWearThirdApplicationOnline(AppCompatActivity appCompatActivity, Device connectedDevice, String peerPkgName, HiWearInterface.CheckWearThirdApplicationOnline checkWearThirdApplicationOnline) {
        // 步骤 2：获取点对点通信对象
        P2pClient p2pClient = HiWear.getP2pClient(appCompatActivity);
        // 步骤 3：先设置需要通信的穿戴设备侧应用包名
        p2pClient.setPeerPkgName(peerPkgName);
        // 步骤 4：检测穿戴设备侧对应的第三方应用是否在线
        if ((null != connectedDevice) && connectedDevice.isConnected()) {
            p2pClient.ping(connectedDevice, errCode -> {
                // 与设备端 ping 通信的结果
                Timber.i("onPingResult %s", errCode);
                checkWearThirdApplicationOnline.onPingResult(errCode);
            }).addOnSuccessListener(successVoid -> {
                // ping 任务执行成功时三方应用处理相关的逻辑
                Timber.i("onSuccess %s", successVoid);
                checkWearThirdApplicationOnline.onSuccess(successVoid);
            }).addOnFailureListener(e -> {
                // ping 任务执行失败时三方应用处理相关的逻辑
                Timber.e("onFailure %s", e.getMessage());
                checkWearThirdApplicationOnline.onFailure(e);
            });
        }
    }

    /**
     * 发送消息
     * <p>
     * 收发点对点消息前，需要向手机侧用户申请获取 DEVICE_MANAGER 权限的授权（参见请求用户授权），否则接口将调用失败。
     * <p>
     * 使用该功能前，请确保穿戴设备侧已有对应的第三方应用（参见轻量级智能穿戴设备开发）。
     * 手机 App 和穿戴设备 App 必须同时处于启动状态。
     * 当手机 App 启动，穿戴设备 App 没有启动时，手机 App 可以通过 ping 方法拉起穿戴设备 App。
     * <p>
     * 消息长度大小的限制为 1KB。针对消息长度超过限制的情况可以采用发送文件（文件大小不超过 100MB）的方式或进行消息分包控制。
     *
     * @param appCompatActivity 活动
     * @param connectedDevice   已连接设备
     * @param peerPkgName       需通信穿戴设备侧应用包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param messageContent    消息内容
     * @param sendMessage       HiWearInterface.SendMessage
     */
    public void sendMessage(AppCompatActivity appCompatActivity, Device connectedDevice, String peerPkgName, String peerFingerPrint, @NonNull String messageContent, HiWearInterface.SendMessage sendMessage) {
        // 步骤 2：需要发送的消息 Message
        Message.Builder builder = new Message.Builder();
        builder.setPayload(messageContent.getBytes(StandardCharsets.UTF_8));
        Message message = builder.build();
        // 步骤 3：获取点对点通信对象
        P2pClient p2pClient = HiWear.getP2pClient(appCompatActivity);
        // 步骤 4：指定需要通信的穿戴设备侧应用包名
        p2pClient.setPeerPkgName(peerPkgName);
        // 步骤 5：指定需要通信的穿戴设备侧应用签名证书指纹，轻量级智能穿戴设备侧应用指纹最大长度为 127 位
        p2pClient.setPeerFingerPrint(peerFingerPrint);
        // 步骤 6：从手机上的第三方应用发送简短消息到穿戴设备侧对应的第三方应用
        // 创建回调方法
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSendResult(int resultCode) {
                Timber.i("onSendResult %s", resultCode);
                sendMessage.onSendResult(resultCode);
            }

            @Override
            public void onSendProgress(long progress) {
                Timber.i("onSendProgress %s", progress);
                sendMessage.onSendProgress(progress);
            }
        };
        if ((null != connectedDevice) && connectedDevice.isConnected() && (null != message)) {
            p2pClient.send(connectedDevice, message, sendCallback)
                    .addOnSuccessListener(successVoid -> {
                        // send 任务执行成功时三方应用处理相关的逻辑
                        Timber.i("onSuccess %s", successVoid);
                        sendMessage.onSuccess(successVoid);
                    })
                    .addOnFailureListener(e -> {
                        // send 任务执行失败时三方应用处理相关的逻辑
                        Timber.e("onFailure %s", e.getMessage());
                        sendMessage.onFailure(e);
                    });
        }
    }

    /**
     * 发送文件
     *
     * @param appCompatActivity 活动
     * @param connectedDevice   已连接设备
     * @param peerPkgName       需通信穿戴设备侧应用包名
     * @param peerFingerPrint   需通信穿戴设备侧应用签名证书指纹
     * @param filePath          文件路径
     * @param sendFile          HiWearInterface.SendFile
     */
    public void sendFile(AppCompatActivity appCompatActivity, Device connectedDevice, String peerPkgName, String peerFingerPrint, String filePath, HiWearInterface.SendFile sendFile) {
        // 步骤 2：构造需要发送的文件 Message
        File file = new File(filePath);
        Message.Builder builder = new Message.Builder();
        builder.setPayload(file);
        Message fileMessage = builder.build();
        // 步骤 3：获取点对点通信对象
        P2pClient p2pClient = HiWear.getP2pClient(appCompatActivity);
        // 步骤 4：指定需要通信的穿戴设备侧应用包名
        p2pClient.setPeerPkgName(peerPkgName);
        // 步骤 5：指定需要通信的穿戴设备侧应用签名证书指纹
        p2pClient.setPeerFingerPrint(peerFingerPrint);
        // 步骤 6：从手机上的第三方应用发送文件到穿戴设备侧对应的第三方应用
        // 创建发送文件的回调对象
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSendResult(int resultCode) {
                Timber.i("onSendResult %s", resultCode);
                sendFile.onSendResult(resultCode);
            }

            @Override
            public void onSendProgress(long progress) {
                Timber.i("onSendProgress %s", progress);
                sendFile.onSendProgress(progress);
            }
        };
        // 发送文件到设备侧
        if ((null != connectedDevice) && connectedDevice.isConnected() && (null != fileMessage)) {
            p2pClient.send(connectedDevice, fileMessage, sendCallback)
                    .addOnSuccessListener(successVoid -> {
                        // send 任务执行成功时三方应用处理相关的逻辑
                        Timber.i("onSuccess %s", successVoid);
                        sendFile.onSuccess(successVoid);
                    })
                    .addOnFailureListener(e -> {
                        // send 任务执行失败时三方应用处理相关的逻辑
                        Timber.e("onFailure %s", e.getMessage());
                        sendFile.onFailure(e);
                    });
        }
    }

    /**
     * 取消发送文件
     * <p>
     * cancelFileTransfer 仅支持取消正在发送的文件。
     * 如果取消发送的文件不存在、文件不在发送过程中或者对端包名填写错误都会报非法参数错误（Invalid argument），错误码 5。
     *
     * @param appCompatActivity 活动
     * @param connectedDevice   已连接设备
     * @param peerPkgName       需通信穿戴设备侧应用包名
     * @param filePath          文件路径
     * @param cancelSendFile    HiWearInterface.CancelSendFile
     */
    public void cancelSendFile(AppCompatActivity appCompatActivity, Device connectedDevice, String peerPkgName, String filePath, HiWearInterface.CancelSendFile cancelSendFile) {
        // 步骤 2：构造需要取消发送的文件标识信息 FileIdentification
        File cancelFile = new File(filePath);
        FileIdentification.Builder builder = new FileIdentification.Builder();
        builder.setFile(cancelFile);
        FileIdentification fileIdentification = builder.build();
        // 步骤 3：获取点对点通信对象
        P2pClient p2pClient = HiWear.getP2pClient(appCompatActivity);
        // 步骤 4：指定需要通信的穿戴设备侧应用包名
        p2pClient.setPeerPkgName(peerPkgName);
        // 步骤 5：从手机上的第三方应用发送文件到穿戴设备侧对应的第三方应用
        // 创建取消发送文件的回调对象
        CancelFileTransferCallBack cancelFileTransferCallBack = errCode -> {
            /*
             * 常见几个错误码解释如下：
             * errCode: 207 取消文件发送成功
             * errCode: 1990020004 取消发送文件失败
             * errCode: 5
             * (1) 调用取消发送文件接口，传的参数为空。
             * (2) 取消发送的文件不存在、文件不在发送过程中或者对端包名填写错误
             */
            Timber.i(TAG, "cancel send file callBack, errCode: %s", errCode);
            cancelSendFile.onCancelFileTransferResult(errCode);
        };
        // 取消发送文件到设备侧
        if ((null != connectedDevice) && connectedDevice.isConnected() && (null != fileIdentification)) {
            p2pClient.cancelFileTransfer(connectedDevice, fileIdentification, cancelFileTransferCallBack)
                    .addOnSuccessListener(successVoid -> {
                        // 取消文件发送任务执行成功时三方应用处理相关的逻辑
                        Timber.i(TAG, "cancel send file submission success %s", successVoid);
                        cancelSendFile.onSuccess(successVoid);
                    })
                    .addOnFailureListener(e -> {
                        // 取消文件发送任务执行失败时三方应用处理相关的逻辑
                        Timber.e(TAG, "cancel send file onFailure e: %s", e.getMessage());
                        cancelSendFile.onFailure(e);
                    });
        }
    }

    /**
     * 接收或取消接收消息
     * <p>
     * 若开发者集成的 SDK 版本为 5.0.0.301 及以下，调用 registerReceiver 和 onReceiveMessage 两个方法，只能接收点对点消息。若集成的 SDK 版本是 5.0.1.300 及以上，则还可以接收文件。
     *
     * @param appCompatActivity      活动
     * @param connectedDevice        已连接设备
     * @param peerPkgName            需通信穿戴设备侧应用包名
     * @param peerFingerPrint        需通信穿戴设备侧应用签名证书指纹
     * @param register               注册
     * @param receiveOrCancelMessage HiWearInterface.ReceiveOrCancelMessage
     */
    public void receiveOrCancelMessage(AppCompatActivity appCompatActivity, Device connectedDevice, String peerPkgName, String peerFingerPrint, boolean register, HiWearInterface.ReceiveOrCancelMessage receiveOrCancelMessage) {
        // 步骤 2：获取点对点通信对象
        P2pClient p2pClient = HiWear.getP2pClient(appCompatActivity);
        // 步骤 3：指定需要通信的穿戴设备侧应用包名
        p2pClient.setPeerPkgName(peerPkgName);
        // 步骤 4：指定需要通信的穿戴设备侧应用签名证书指纹
        p2pClient.setPeerFingerPrint(peerFingerPrint);
        // 步骤 5：手机第三方应用接收穿戴设备侧第三方应用发过来的消息或文件
        Receiver receiver = message -> {
            Timber.i("onReceiveMessage %s", message);
            if (message.getType() == Message.MESSAGE_TYPE_DATA) {
                // 处理穿戴设备侧第三方 App 发过来的消息
                receiveOrCancelMessage.onReceiveMessage(message, Message.MESSAGE_TYPE_DATA);
            } else if (message.getType() == Message.MESSAGE_TYPE_FILE) {
                // 处理穿戴设备侧第三方 App 发过来的文件
                receiveOrCancelMessage.onReceiveMessage(message, Message.MESSAGE_TYPE_FILE);
            }
        };
        if ((null != connectedDevice) && connectedDevice.isConnected()) {
            if (register) {
                HiWear.getP2pClient(appCompatActivity).registerReceiver(connectedDevice, receiver)
                        .addOnFailureListener(e -> {
                            // 手机侧第三方应用接收消息或文件失败
                            Timber.e("onFailure %s", e.getMessage());
                            receiveOrCancelMessage.onRegisterFailure(e);
                        })
                        .addOnSuccessListener(successVoid -> {
                            // 手机侧第三方应用接收消息或文件成功
                            Timber.i("onSuccess 手机侧第三方应用接收消息或文件成功");
                            receiveOrCancelMessage.onRegisterSuccess(successVoid);
                        });
            } else {
                // 步骤 6：手机第三方应用取消接收穿戴设备侧第三方应用发过来的消息或文件
                HiWear.getP2pClient(appCompatActivity).unregisterReceiver(receiver)
                        .addOnSuccessListener(successVoid -> {
                            // 手机侧第三方应用取消接收消息或文件成功
                            Timber.i("onSuccess 手机侧第三方应用取消接收消息或文件成功");
                            receiveOrCancelMessage.onUnregisterSuccess(successVoid);
                        })
                        .addOnFailureListener(e -> {
                            // 手机侧第三方应用取消接收消息或文件失败
                            Timber.e("onFailure %s", e.getMessage());
                            receiveOrCancelMessage.onUnregisterFailure(e);
                        });
            }
        }
    }

    /**
     * 检测或取消检测连接状态
     * <p>
     * 华为运动健康 App 在后台停止服务（如功耗过高），从而导致应用与 Wear Engine 服务的连接状态发生变化。
     * 对于类似这种不确定的断开情况，开发者可以通过本功能特性了解当前应用和 Wear Engine 的连接状态。
     * 前提是在设备断开前，开发者已经使用该功能进行过监测。
     *
     * @param appCompatActivity                 活动
     * @param register                          注册
     * @param checkOrCancelCheckConnectionState HiWearInterface.CheckOrCancelCheckConnectionState
     */
    public void checkOrCancelCheckConnectionState(AppCompatActivity appCompatActivity, boolean register, HiWearInterface.CheckOrCancelCheckConnectionState checkOrCancelCheckConnectionState) {
        // 步骤 1：定义连接状态监测接口对象
        ServiceConnectionListener serviceConnectionListener = new ServiceConnectionListener() {
            @Override
            public void onServiceConnect() {
                // 处理与 Wear Engine 服务连接成功事件
                Timber.i("onServiceConnect 处理与 Wear Engine 服务连接成功事件");
                checkOrCancelCheckConnectionState.onServiceConnect();
            }

            @Override
            public void onServiceDisconnect() {
                // 处理与 Wear Engine 服务断开连接事件
                Timber.i("onServiceDisconnect 处理与 Wear Engine 服务断开连接事件");
                checkOrCancelCheckConnectionState.onServiceDisconnect();
            }
        };
        // 步骤 2：获取 WearEngineClient 对象
        WearEngineClient wearEngineClient = HiWear.getWearEngineClient(appCompatActivity, serviceConnectionListener);
        if (register) {
            // 步骤 3：调用 registerServiceConnectionListener 方法，监测与 Wear Engine 服务的连接状态
            wearEngineClient.registerServiceConnectionListener()
                    .addOnSuccessListener(successVoid -> {
                        // 接口调用成功
                        Timber.i("onSuccess %s", successVoid);
                        checkOrCancelCheckConnectionState.onRegisterSuccess(successVoid);
                    })
                    .addOnFailureListener(e -> {
                        // 接口调用失败
                        Timber.e("onFailure %s", e.getMessage());
                        checkOrCancelCheckConnectionState.onRegisterFailure(e);
                    });
        } else {
            // 步骤 4：调用 unregisterServiceConnectionListener 方法，取消监测与 Wear Engine 服务的连接状态
            wearEngineClient.unregisterServiceConnectionListener()
                    .addOnSuccessListener(successVoid -> {
                        // 接口调用成功
                        Timber.i("onSuccess %s", successVoid);
                        checkOrCancelCheckConnectionState.onUnregisterSuccess(successVoid);
                    })
                    .addOnFailureListener(e -> {
                        // 接口调用失败
                        Timber.e("onFailure %s", e.getMessage());
                        checkOrCancelCheckConnectionState.onUnregisterFailure(e);
                    });
        }
    }

    /**
     * 释放连接
     * <p>
     * 断开后，将释放 Wear Engine 资源，监测设备状态、收消息、收文件等功能不可用。如需重新连接，主动调用任意接口即可。
     *
     * @param appCompatActivity 活动
     * @param releaseConnection HiWearInterface.ReleaseConnection
     */
    public void releaseConnection(AppCompatActivity appCompatActivity, HiWearInterface.ReleaseConnection releaseConnection) {
        // 步骤 1：定义连接状态监测接口对象
        ServiceConnectionListener serviceConnectionListener = new ServiceConnectionListener() {
            @Override
            public void onServiceConnect() {
                // 处理与 Wear Engine 服务连接成功事件
                Timber.i("onServiceConnect 处理与 Wear Engine 服务连接成功事件");
                releaseConnection.onServiceConnect();
            }

            @Override
            public void onServiceDisconnect() {
                // 处理与 Wear Engine 服务断开连接事件
                Timber.i("onServiceDisconnect 处理与 Wear Engine 服务断开连接事件");
                releaseConnection.onServiceDisconnect();
            }
        };
        // 步骤 2：获取 WearEngineClient 对象
        WearEngineClient wearEngineClient = HiWear.getWearEngineClient(appCompatActivity, serviceConnectionListener);
        // 步骤 3：调用 releaseConnection 方法，断开应用与 Wear Engine 服务的连接
        wearEngineClient.releaseConnection()
                .addOnSuccessListener(successVoid -> {
                    // 接口调用成功
                    Timber.i("onSuccess %s", successVoid);
                    releaseConnection.onSuccess(successVoid);
                })
                .addOnFailureListener(e -> {
                    // 接口调用失败
                    Timber.e("onFailure %s", e.getMessage());
                    releaseConnection.onFailure(e);
                });
    }

    /**
     * 获取 Wear Engine SDK 客户端 API 级别
     * <p>
     * Wear Engine 的每个接口都有对应的 API level，比如：0、2。其中 SDK 客户端各个接口的 API level 可以查看 API 参考文档。
     * Wear Engine 服务端和客户端作为两个独立的整体也有对应的 API level，是其支持的所有接口的最高 API level，比如：客户端有 40 个接口，这些接口的 API level 有 0、2 两个值，则客户端作为一个整体其 API level 为 2。
     * 若服务端的 API level 比客户端低，则表示客户端的部分接口，服务端不支持，此时开发者可以建议用户升级运动健康 App 版本。若客户端的 API level 比服务端低或两者相同，则表示客户端的功能，服务端均支持。
     *
     * @param appCompatActivity              活动
     * @param getWearEngineSdkClientApiLevel HiWearInterface.GetWearEngineSdkClientApiLevel
     */
    public void getWearEngineSdkClientApiLevel(AppCompatActivity appCompatActivity, HiWearInterface.GetWearEngineSdkClientApiLevel getWearEngineSdkClientApiLevel) {
        // 步骤 1：定义连接状态监测接口对象
        ServiceConnectionListener serviceConnectionListener = new ServiceConnectionListener() {
            @Override
            public void onServiceConnect() {
                // 处理与 Wear Engine 服务连接成功事件
                Timber.i("onServiceConnect 处理与 Wear Engine 服务连接成功事件");
                getWearEngineSdkClientApiLevel.onServiceConnect();
            }

            @Override
            public void onServiceDisconnect() {
                // 处理与 Wear Engine 服务断开连接事件
                Timber.i("onServiceDisconnect 处理与 Wear Engine 服务断开连接事件");
                getWearEngineSdkClientApiLevel.onServiceDisconnect();
            }
        };
        // 步骤 2：获取 WearEngineClient 对象
        WearEngineClient wearEngineClient = HiWear.getWearEngineClient(appCompatActivity, serviceConnectionListener);
        // 步骤 3：调用 getClientApiLevel 方法，获取客户端 API level
        wearEngineClient.getClientApiLevel()
                .addOnSuccessListener(integer -> {
                    // 接口调用成功，integer 返回客户端 API level
                    Timber.i(TAG, "sdk apiLevel: %s", integer);
                    getWearEngineSdkClientApiLevel.onSuccess(integer);
                })
                .addOnFailureListener(e -> {
                    // 接口调用失败
                    Timber.e(TAG, "sdk apiLevel e: %s", e.getMessage());
                    getWearEngineSdkClientApiLevel.onFailure(e);
                });
    }

    /**
     * 获取 Wear Engine SDK 服务端 API 级别
     * <p>
     * Wear Engine 的每个接口都有对应的 API level，比如：0、2。其中 SDK 客户端各个接口的 API level 可以查看 API 参考文档。
     * Wear Engine 服务端和客户端作为两个独立的整体也有对应的 API level，是其支持的所有接口的最高 API level，比如：客户端有 40 个接口，这些接口的 API level 有 0、2 两个值，则客户端作为一个整体其 API level 为 2。
     * 若服务端的 API level 比客户端低，则表示客户端的部分接口，服务端不支持，此时开发者可以建议用户升级运动健康 App 版本。若客户端的 API level 比服务端低或两者相同，则表示客户端的功能，服务端均支持。
     *
     * @param appCompatActivity               活动
     * @param getWearEngineSdkServiceApiLevel HiWearInterface.GetWearEngineSdkServiceApiLevel
     */
    public void getWearEngineSdkServiceApiLevel(AppCompatActivity appCompatActivity, HiWearInterface.GetWearEngineSdkServiceApiLevel getWearEngineSdkServiceApiLevel) {
        // 步骤 1：定义连接状态监测接口对象
        ServiceConnectionListener serviceConnectionListener = new ServiceConnectionListener() {
            @Override
            public void onServiceConnect() {
                // 处理与 Wear Engine 服务连接成功事件
                Timber.i("onServiceConnect 处理与 Wear Engine 服务连接成功事件");
                getWearEngineSdkServiceApiLevel.onServiceConnect();
            }

            @Override
            public void onServiceDisconnect() {
                // 处理与 Wear Engine 服务断开连接事件
                Timber.i("onServiceDisconnect 处理与 Wear Engine 服务断开连接事件");
                getWearEngineSdkServiceApiLevel.onServiceDisconnect();
            }
        };
        // 步骤 2：获取 WearEngineClient 对象
        WearEngineClient wearEngineClient = HiWear.getWearEngineClient(appCompatActivity, serviceConnectionListener);
        // 步骤 2：调用 getServiceApiLevel 方法，获取服务端 API level
        wearEngineClient.getServiceApiLevel()
                .addOnSuccessListener(integer -> {
                    // 接口调用成功，integer 返回服务端 API level
                    Timber.i(TAG, "service apiLevel: %s", integer);
                    getWearEngineSdkServiceApiLevel.onSuccess(integer);
                })
                .addOnFailureListener(e -> {
                    // 调用接口失败
                    Timber.e(TAG, "service apiLevel e: %s", e.getMessage());
                    getWearEngineSdkServiceApiLevel.onFailure(e);
                });
    }
}
