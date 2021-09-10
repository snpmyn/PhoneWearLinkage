/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bocop.fem.official;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bocop.fem.R;
import com.chaos.util.java.list.ListUtils;
import com.huawei.wearengine.HiWear;
import com.huawei.wearengine.auth.AuthCallback;
import com.huawei.wearengine.auth.Permission;
import com.huawei.wearengine.device.Device;
import com.huawei.wearengine.device.DeviceClient;
import com.huawei.wearengine.monitor.MonitorClient;
import com.huawei.wearengine.monitor.MonitorItem;
import com.huawei.wearengine.monitor.MonitorListener;
import com.huawei.wearengine.p2p.Message;
import com.huawei.wearengine.p2p.P2pClient;
import com.huawei.wearengine.p2p.Receiver;
import com.huawei.wearengine.p2p.SendCallback;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * WearEngine Sample Code
 *
 * @author xxx
 * @since 2020-08-05
 */
public class WearEngineMainActivity extends AppCompatActivity {
    private static final String DEVICE_NAME_OF = "'s ";
    private static final String SEND_MESSAGE_TO = "Send message to ";
    private static final String FAILURE = " task failure";
    private static final String SUCCESS = " task success";
    private static final String STRING_RESULT = " result:";
    private static final String STRING_PING = " Ping ";
    private static final String MONITOR_INT_DATA = "], int data[";
    private static final String MONITOR_BOOLEAN_DATA = "], boolean data[";
    private static final String HASH_CODE = " , hashcode is: ";
    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int SELECT_FILE_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int SCROLL_HIGH = 50;
    private static final String FINGER_PRINT = "com.bocop.watch_BGNTi0xttHSpG89/0lm+LBZV5haw5qBGoNCcjEDJybay3YWcgjRZHyTrkr1Ft2gxhbBFpOG6+RhUhVAtufkeCTE=";
    private final List<Device> deviceList = new ArrayList<>();
    private final Map<String, Device> deviceMap = new HashMap<>();
    private final MonitorItem monitorItemType = MonitorItem.MONITOR_ITEM_CONNECTION;
    private RadioGroup devicesRadioGroup;
    private EditText messageEditText;
    private TextView logOutputTextView;
    private final Receiver receiver = message -> {
        if (null != message) {
            String data = new String(message.getData());
            printOperationResult("ReceiveMessage is: " + data);
        } else {
            printOperationResult("Receiver Message is null");
        }
    };
    private EditText peerPkgNameEditText;
    private P2pClient p2pClient;
    private MonitorClient monitorClient;
    private DeviceClient deviceClient;
    private Device selectedDevice;
    private Message sendMessage;
    private int index = 0;
    private String peerPkgName;
    private MonitorListener monitorListener = (resultCode, monitorItem, monitorData) -> {
        if ((null != monitorData) && (null != monitorItem)) {
            printOperationResult(
                    "MonitorListener result is: resultCode: " + resultCode + "string data[" + monitorData.asString()
                            + MONITOR_INT_DATA + monitorData.asInt() + MONITOR_BOOLEAN_DATA + monitorData.asBool() + " ]");
        } else {
            printOperationResult("monitorItem is null or monitorData is null!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_engine_main);
        initView();
        initData();
        addViewListener();
        verifyStoragePermissions();
    }

    /**
     * Apply for the read permission on the external storage device.
     */
    private void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * Initialization: Obtain the authorization.
     * <p>
     * 查询用户是否授予第三方应用权限
     */
    private void initData() {
        AuthCallback authCallback = new AuthCallback() {
            @Override
            public void onOk(Permission[] permissions) {
                Timber.d("getAuthClient onOk");
            }

            @Override
            public void onCancel() {
                Timber.e("getAuthClient onCancel");
            }
        };
        HiWear.getAuthClient(this)
                .requestPermission(authCallback, Permission.DEVICE_MANAGER, Permission.NOTIFY, Permission.SENSOR)
                .addOnSuccessListener(successVoid -> Timber.d("getAuthClient onSuccess"))
                .addOnFailureListener(e -> Timber.d("getAuthClient onFailure"));
        p2pClient = HiWear.getP2pClient(this);
        deviceClient = HiWear.getDeviceClient(this);
        monitorClient = HiWear.getMonitorClient(this);
    }

    /**
     * Initialize the view.
     */
    private void initView() {
        devicesRadioGroup = findViewById(R.id.device_radio_group);
        logOutputTextView = findViewById(R.id.log_output_text_view);
        messageEditText = findViewById(R.id.message_edit_text);
        peerPkgNameEditText = findViewById(R.id.peer_pkg_name_edit_text);
    }

    /**
     * Add a view listener.
     */
    private void addViewListener() {
        logOutputTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        devicesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Timber.d("onCheckedChanged: %s", checkedId);
            selectedDevice = deviceList.get(checkedId);
        });
        peerPkgNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null) {
                    Timber.e("peerPkgNameEditText After message text changed editable is null");
                    return;
                }
                Timber.d("After package text changed %s", editable);
                setPeerPkgName(editable);
            }
        });
    }

    /**
     * Set the name and fingerprint of the device app package specified by the third-party app for communication.
     */
    private void setPeerPkgName(@NonNull Editable editable) {
        peerPkgName = editable.toString().trim();
        p2pClient.setPeerPkgName(peerPkgName);
        // You need to set the fingerprint information of the target application.
        p2pClient.setPeerFingerPrint(FINGER_PRINT);
    }

    /**
     * Check whether the permission is granted.
     *
     * @param view UI object
     */
    public void checkPermission(View view) {
        HiWear.getAuthClient(this)
                .checkPermission(Permission.DEVICE_MANAGER).addOnSuccessListener(isResult -> printOperationResult("checkPermission task onSuccess! device_manager permission is = " + isResult)).addOnFailureListener(e -> printOperationResult("checkPermission task submission error"));
    }

    /**
     * Querying Whether a Permission Group Is Granted
     *
     * @param view UI object
     */
    public void checkPermissions(View view) {
        Permission[] permissions = {Permission.DEVICE_MANAGER};
        HiWear.getAuthClient(this)
                .checkPermissions(permissions).addOnSuccessListener(booleans -> {
            printOperationResult("checkPermission task onSuccess!");
            if (booleans.length > 0) {
                printOperationResult("device_manager permission is = " + booleans[0]);
            }
        }).addOnFailureListener(e -> printOperationResult("checkPermissions task submission error"));
    }

    /**
     * Obtain the paired device list.
     * <p>
     * 获取已配对的设备列表
     *
     * @param view UI object
     */
    public void getBoundDevices(View view) {
        deviceClient.getBondedDevices().addOnSuccessListener(devices -> {
            if (ListUtils.listIsEmpty(devices)) {
                WearEngineMainActivity.this.printOperationResult("getBondedDevices list is null or list size is 0");
                return;
            }
            WearEngineMainActivity.this.printOperationResult("getBondedDevices onSuccess! devices list size = " + devices.size());
            WearEngineMainActivity.this.updateDeviceList(devices);
        }).addOnFailureListener(e -> printOperationResult("getBondedDevices task submission error"));
    }

    /**
     * Ping the paired device.
     *
     * @param view UI object
     */
    public void pingBoundDevices(View view) {
        if (checkDevice() || checkPackageName()) {
            return;
        }
        p2pClient.ping(selectedDevice, result -> printOperationResult(Calendar.getInstance().getTime() + STRING_PING + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + STRING_RESULT + result)).addOnSuccessListener(result -> WearEngineMainActivity.this.printOperationResult(STRING_PING + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + SUCCESS)).addOnFailureListener(e -> printOperationResult(STRING_PING + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + FAILURE));
    }

    /**
     * Send a message to the app with the specified package name on the specified device.
     *
     * @param view UI object
     */
    public void sendMessage(View view) {
        if (checkDevice() || checkPackageName()) {
            return;
        }
        // Build the request param message.
        String sendMessageStr = messageEditText.getText().toString();
        if (sendMessageStr.length() > 0) {
            Message.Builder builder = new Message.Builder();
            builder.setPayload(sendMessageStr.getBytes(StandardCharsets.UTF_8));
            sendMessage = builder.build();
        }
        if ((null == sendMessage) || (sendMessage.getData().length == 0)) {
            printOperationResult("please input message for send!");
            return;
        }
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSendResult(int resultCode) {
                printOperationResult(Calendar.getInstance().getTime() + SEND_MESSAGE_TO + selectedDevice.getName()
                        + DEVICE_NAME_OF + peerPkgName + STRING_RESULT + resultCode);
            }

            @Override
            public void onSendProgress(long progress) {
                printOperationResult(Calendar.getInstance().getTime() + SEND_MESSAGE_TO + selectedDevice.getName()
                        + DEVICE_NAME_OF + peerPkgName + " progress: " + progress);
            }
        };
        p2pClient.send(selectedDevice, sendMessage, sendCallback).addOnSuccessListener(result -> printOperationResult(SEND_MESSAGE_TO + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + SUCCESS)).addOnFailureListener(e -> printOperationResult(SEND_MESSAGE_TO + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + FAILURE));
    }

    /**
     * Select a file to send.
     *
     * @param view UI object
     */
    public void selectFileAndSend(View view) {
        if (checkDevice() || checkPackageName()) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, SELECT_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            logOutputTextView.append("ActivityNotFoundException" + System.lineSeparator());
        }
    }

    /**
     * Send the file.
     *
     * @param sendFilePath File path
     */
    public void sendFile(String sendFilePath) {
        File sendFile = new File(sendFilePath);
        Message.Builder builder = new Message.Builder();
        builder.setPayload(sendFile);
        Message fileMessage = builder.build();
        p2pClient.send(selectedDevice, fileMessage, new SendCallback() {
            @Override
            public void onSendResult(int resultCode) {
                printOperationResult(Calendar.getInstance().getTime() + SEND_MESSAGE_TO + selectedDevice.getName()
                        + DEVICE_NAME_OF + peerPkgName + STRING_RESULT + resultCode);
            }

            @Override
            public void onSendProgress(long progress) {
                printOperationResult(Calendar.getInstance().getTime() + SEND_MESSAGE_TO + selectedDevice.getName()
                        + DEVICE_NAME_OF + peerPkgName + " progress: " + progress);
            }
        }).addOnSuccessListener(success -> printOperationResult(
                SEND_MESSAGE_TO + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + SUCCESS)).addOnFailureListener(error -> printOperationResult(
                SEND_MESSAGE_TO + selectedDevice.getName() + DEVICE_NAME_OF + peerPkgName + FAILURE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == SELECT_FILE_CODE) && (resultCode == RESULT_OK)) {
            if (null == data) {
                Timber.e("Invalid file data.");
                return;
            }
            Uri selectFileUri = data.getData();
            String selectFilePath = SelectFileManager.getFilePath(this, selectFileUri);
            sendFile(selectFilePath);
        }
    }

    /**
     * Register a specified app on a specified device.
     *
     * @param view UI object
     */
    public void receiveMessage(View view) {
        if (checkDevice() || checkPackageName()) {
            return;
        }
        int receiverPid = android.os.Process.myPid();
        int receiverHashCode = System.identityHashCode(receiver);
        Timber.d("receiveMessageButtonOnClick receiver pid is: " + receiverPid + HASH_CODE + receiverHashCode);
        p2pClient.registerReceiver(selectedDevice, receiver).addOnSuccessListener(avoid -> printOperationResult("register receiver listener" + SUCCESS)).addOnFailureListener(e -> printOperationResult("register receiver listener" + FAILURE));
    }

    /**
     * Deregister a registered app.
     *
     * @param view UI object
     */
    public void cancelReceiveMessage(View view) {
        int receiverPid = android.os.Process.myPid();
        int receiverHashCode = System.identityHashCode(receiver);
        Timber.d("cancelReceiveMessage receiver pid is: " + receiverPid + HASH_CODE + receiverHashCode);
        p2pClient.unregisterReceiver(receiver).addOnSuccessListener(successVoid -> printOperationResult("cancel receive message " + SUCCESS)).addOnFailureListener(e -> printOperationResult("cancel receive message " + FAILURE));
    }

    /**
     * Register a listening object on the specified device to listen for the Bluetooth connection status.
     *
     * @param view UI object
     */
    public void registerEventStatus(View view) {
        if (checkDevice()) {
            return;
        }
        monitorListener = (errorCode, monitorItem, monitorData) -> {
            String result = "ReceiveMonitorMessage is: string data[" + monitorData.asString() + MONITOR_INT_DATA
                    + monitorData.asInt() + MONITOR_BOOLEAN_DATA + monitorData.asBool() + "]";
            printOperationResult(result);
        };
        int receiverPid = android.os.Process.myPid();
        int receiverHashCode = System.identityHashCode(monitorListener);
        Timber.d("registerEventStatus receiver pid is: " + receiverPid + HASH_CODE + receiverHashCode);
        monitorClient.register(selectedDevice, monitorItemType, monitorListener)
                .addOnSuccessListener(avoid -> printOperationResult("register status event listener " + SUCCESS))
                .addOnFailureListener(e -> printOperationResult("register status event listener " + FAILURE));
    }

    /**
     * Deregister a registered app.
     *
     * @param view UI object
     */
    public void unRegisterEventStatus(View view) {
        int receiverPid = android.os.Process.myPid();
        int receiverHashCode = System.identityHashCode(monitorListener);
        Timber.d("cancelReceiveMessage receiver pid is: " + receiverPid + HASH_CODE + receiverHashCode);
        monitorClient.unregister(monitorListener).addOnSuccessListener(avoid -> printOperationResult("cancel register status event listener " + SUCCESS)).addOnFailureListener(e -> printOperationResult("cancel register status event listener " + FAILURE));
    }

    /**
     * Clear the result printing box.
     *
     * @param view UI object
     */
    public void clearOutputTextView(View view) {
        logOutputTextView.setText("");
        logOutputTextView.scrollTo(0, 0);
    }

    /**
     * Print the result.
     *
     * @param string Print result character string
     */
    private void printOperationResult(final String string) {
        Timber.i(string);
        runOnUiThread(() -> {
            logOutputTextView.append(string + System.lineSeparator());
            int offset = logOutputTextView.getLineCount() * logOutputTextView.getLineHeight();
            if (offset > logOutputTextView.getHeight()) {
                logOutputTextView.scrollTo(0, offset - logOutputTextView.getHeight() + SCROLL_HIGH);
            }
        });
    }

    /**
     * Check whether the target device is selected.
     *
     * @return true/false Return whether the target device is selected.
     */
    private boolean checkDevice() {
        if (selectedDevice == null) {
            printOperationResult("Please select the target device!");
            return true;
        }
        return false;
    }

    /**
     * Check whether the target app package name is set.
     *
     * @return true/false Return whether the target app package name is set
     */
    private boolean checkPackageName() {
        if (TextUtils.isEmpty(peerPkgName)) {
            printOperationResult("Please input target app packageName!");
            return true;
        }
        return false;
    }

    /**
     * Update the device display box.
     *
     * @param devices Device list
     */
    private void updateDeviceList(@NonNull List<Device> devices) {
        for (Device device : devices) {
            printOperationResult("device Name: " + device.getName());
            printOperationResult("device connect status: " + device.isConnected());
            if (deviceMap.containsKey(device.getUuid())) {
                continue;
            }
            deviceList.add(device);
            deviceMap.put(device.getUuid(), device);
            RadioButton deviceRadioButton = new RadioButton(this);
            setRaidButton(deviceRadioButton, device.getName(), index);
            devicesRadioGroup.addView(deviceRadioButton);
            index++;
        }
    }

    /**
     * Set the device list.
     */
    private void setRaidButton(@NonNull final RadioButton radioButton, String text, int id) {
        radioButton.setChecked(false);
        radioButton.setId(id);
        radioButton.setText(text);
    }

    /**
     * Jump to the sensor page.
     *
     * @param view UI object
     */
    public void startSensor(View view) {
        if (isDeviceNotConnected()) {
            printOperationResult("No available device or the device has not been connected.");
            return;
        }
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra("currentDevice", selectedDevice);
        startActivity(intent);
    }

    /**
     * Jump to the notification page.
     *
     * @param view UI object
     */
    public void startNotify(View view) {
        if (isDeviceNotConnected()) {
            printOperationResult("No available device or the device has not been connected.");
            return;
        }
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("currentDevice", selectedDevice);
        intent.putExtra("packageName", peerPkgName);
        startActivity(intent);
    }

    /**
     * Check whether the device is connected.
     *
     * @return true:not connected,false:connected
     */
    private boolean isDeviceNotConnected() {
        boolean isConnected = ((null != selectedDevice) && selectedDevice.isConnected());
        return !isConnected;
    }
}