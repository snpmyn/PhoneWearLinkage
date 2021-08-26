package com.bocop.fem.utils;

import com.huawei.wearengine.auth.Permission;
import com.huawei.wearengine.device.Device;
import com.huawei.wearengine.p2p.Message;

import java.util.List;

/**
 * Created on 2021/8/22
 *
 * @author zsp
 * @desc HiWear 接口
 */
public class HiWearInterface {
    public interface CheckAvailableDevice {
        /**
         * 查询可用穿戴设备成功
         *
         * @param result Boolean
         */
        void onSuccess(Boolean result);

        /**
         * 查询可用穿戴设备失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface CheckPermission {
        /**
         * 查询单个权限成功
         *
         * @param aBoolean Boolean
         */
        void onSuccess(Boolean aBoolean);

        /**
         * 查询单个权限失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface CheckPermissions {
        /**
         * 查询多个权限成功
         *
         * @param booleans Boolean[]
         */
        void onSuccess(Boolean[] booleans);

        /**
         * 查询多个权限失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface RequestPermission {
        /**
         * 请求权限
         * <p>
         * 返回用户授予的权限列表
         *
         * @param permissions Permission[]
         */
        void onOk(Permission[] permissions);

        /**
         * 请求权限
         * <p>
         * 用户取消授权
         */
        void onCancel();

        /**
         * 请求权限成功
         *
         * @param successVoid Void
         */
        void onSuccess(Void successVoid);

        /**
         * 请求权限失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface CheckWearDeviceInfo {
        /**
         * 查询穿戴设备信息成功
         *
         * @param devices         List<Device>
         * @param connectedDevice Device
         */
        void onSuccess(List<Device> devices, Device connectedDevice);

        /**
         * 查询穿戴设备信息失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface CheckWearThirdApplicationInstall {
        /**
         * 检测穿戴设备侧第三方应用安装成功
         *
         * @param isAppInstalled Boolean
         */
        void onSuccess(Boolean isAppInstalled);

        /**
         * 检测穿戴设备侧第三方应用安装失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface CheckWearThirdApplicationOnline {
        /**
         * 检测穿戴设备侧第三方应用在线
         * <p>
         * 与设备端 ping 通信的结果
         *
         * @param errCode int
         */
        void onPingResult(int errCode);

        /**
         * 检测穿戴设备侧第三方应用在线成功
         *
         * @param successVoid Void
         */
        void onSuccess(Void successVoid);

        /**
         * 检测穿戴设备侧第三方应用在线失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface SendMessage {
        /**
         * 发送消息结果
         *
         * @param resultCode int
         */
        void onSendResult(int resultCode);

        /**
         * 发送消息进度
         *
         * @param progress long
         */
        void onSendProgress(long progress);

        /**
         * 发送消息成功
         *
         * @param successVoid Void
         */
        void onSuccess(Void successVoid);

        /**
         * 发送消息失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface SendFile {
        /**
         * 发送文件结果
         *
         * @param resultCode int
         */
        void onSendResult(int resultCode);

        /**
         * 发送文件进度
         *
         * @param progress long
         */
        void onSendProgress(long progress);

        /**
         * 发送文件成功
         *
         * @param successVoid Void
         */
        void onSuccess(Void successVoid);

        /**
         * 发送文件失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface CancelSendFile {
        /**
         * 取消发送文件结果
         *
         * @param errCode int
         */
        void onCancelFileTransferResult(int errCode);

        /**
         * 取消发送文件成功
         *
         * @param successVoid Void
         */
        void onSuccess(Void successVoid);

        /**
         * 取消发送文件失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface ReceiveOrCancelMessage {
        /**
         * 接收或取消接收消息
         * <p>
         * 接收消息
         *
         * @param message     Message
         * @param messageType int
         */
        void onReceiveMessage(Message message, int messageType);

        /**
         * 接收或取消接收消息
         * <p>
         * 注册成功
         *
         * @param successVoid Void
         */
        void onRegisterSuccess(Void successVoid);

        /**
         * 接收或取消接收消息
         * <p>
         * 注册失败
         *
         * @param e Exception
         */
        void onRegisterFailure(Exception e);

        /**
         * 接收或取消接收消息
         * <p>
         * 取消注册成功
         *
         * @param successVoid Void
         */
        void onUnregisterSuccess(Void successVoid);

        /**
         * 接收或取消接收消息
         * <p>
         * 取消注册失败
         *
         * @param e Exception
         */
        void onUnregisterFailure(Exception e);
    }

    public interface CheckOrCancelCheckConnectionState {
        /**
         * 检测或取消检测连接状态
         * <p>
         * 与 Wear Engine 服务连接成功
         */
        void onServiceConnect();

        /**
         * 检测或取消检测连接状态
         * <p>
         * 与 Wear Engine 服务断开连接
         */
        void onServiceDisconnect();

        /**
         * 检测或取消检测连接状态
         * <p>
         * 注册成功
         *
         * @param successVoid Void
         */
        void onRegisterSuccess(Void successVoid);

        /**
         * 检测或取消检测连接状态
         * <p>
         * 注册失败
         *
         * @param e Exception
         */
        void onRegisterFailure(Exception e);

        /**
         * 检测或取消检测连接状态
         * <p>
         * 取消注册成功
         *
         * @param successVoid Void
         */
        void onUnregisterSuccess(Void successVoid);

        /**
         * 检测或取消检测连接状态
         * <p>
         * 取消注册失败
         *
         * @param e Exception
         */
        void onUnregisterFailure(Exception e);
    }

    public interface ReleaseConnection {
        /**
         * 释放连接
         * <p>
         * 与 Wear Engine 服务连接成功
         */
        void onServiceConnect();

        /**
         * 释放连接
         * <p>
         * 与 Wear Engine 服务断开连接
         */
        void onServiceDisconnect();

        /**
         * 释放连接成功
         *
         * @param successVoid Void
         */
        void onSuccess(Void successVoid);

        /**
         * 释放连接失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface GetWearEngineSdkClientApiLevel {
        /**
         * 获取 Wear Engine SDK 客户端 API 级别
         * <p>
         * 与 Wear Engine 服务连接成功
         */
        void onServiceConnect();

        /**
         * 获取 Wear Engine SDK 客户端 API 级别
         * <p>
         * 与 Wear Engine 服务断开连接
         */
        void onServiceDisconnect();

        /**
         * 获取 Wear Engine SDK 客户端 API 级别成功
         *
         * @param integer Integer
         */
        void onSuccess(Integer integer);

        /**
         * 获取 Wear Engine SDK 客户端 API 级别失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }

    public interface GetWearEngineSdkServiceApiLevel {
        /**
         * 获取 Wear Engine SDK 服务端 API 级别
         * <p>
         * 与 Wear Engine 服务连接成功
         */
        void onServiceConnect();

        /**
         * 获取 Wear Engine SDK 服务端 API 级别
         * <p>
         * 与 Wear Engine 服务断开连接
         */
        void onServiceDisconnect();

        /**
         * 获取 Wear Engine SDK 服务端 API 级别成功
         *
         * @param integer Integer
         */
        void onSuccess(Integer integer);

        /**
         * 获取 Wear Engine SDK 服务端 API 级别失败
         *
         * @param e Exception
         */
        void onFailure(Exception e);
    }
}
