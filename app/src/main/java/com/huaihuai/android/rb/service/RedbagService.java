package com.huaihuai.android.rb.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.huaihuai.android.rb.util.Notifier;

import java.util.List;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/7/25
 */
public class RedbagService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";

    private static final long DEF_DURATION = 100L;

    // 通知栏关键字捕获 - begin
    private static final String NOTIFY_KEY_TEXT = "[微信红包]恭喜发财，大吉大利！";
    private static final String NOTIFY_KEY_HOME = "回到桌面";
    // 通知栏关键字捕获 - end

    // 对话(聊天)页面
    private static final String CHATUI = "com.tencent.mm.ui.LauncherUI";
    // 对话(聊天)页面关键字
    private static final String CHATUI_KEY_TEXT = "领取红包";
    // 等待拆红包的dialog
    private static final String REDBAG_INFO_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    private static final String REDBAG_INFO_KEY_ID = "com.tencent.mm:id/bag";
    // 拆完红包后的页面
    private static final String REDBAG_DETAIL_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

    // 后退键
    // performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    // Home键
    // performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    // 模拟左滑
    // performGlobalAction(AccessibilityService.GESTURE_SWIPE_LEFT);

    @Override
    protected void onServiceConnected() {
        Notifier.getInstance().notify("坏坏抢红包助手", "微信红包服务已启动", "微信红包服务已启动",
                Notifier.TYPE_WECHAT_SERVICE_RUNNING, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Notifier.getInstance().cancelByType(Notifier.TYPE_WECHAT_SERVICE_RUNNING);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 接收系统发送来的AccessibilityEvent，接收来的AccessibilityEvent是经过过滤的，过滤是在配置工作时设置的
        if (event == null) {
            return;
        }
        switch (event.getEventType()) {
            // 通知栏状态发生变化,如收到新通知
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                dealWithNotification(event);
                break;
            // 当前ui布局发生变化
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                dealWithRedBag(event);
                break;
            default:
                break;
        }
    }

    /**
     * 过滤通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dealWithNotification(AccessibilityEvent event) {
        List<CharSequence> mText = event.getText();
        if (mText.isEmpty()) {
            return;
        }
        for (CharSequence t : mText) {
            String text = String.valueOf(t);
            if (text.endsWith(NOTIFY_KEY_TEXT)) { // 过滤 红包
                sleep();
                openNotification(event);
                break;
            } else if (text.endsWith(NOTIFY_KEY_HOME)) { // 回到 桌面
                int count = 0;
                while (count != 5) {
                    if (performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)) {
                        count = 5;
                    } else {
                        count++;
                        sleep(500L);
                    }
                }
                break;
            }
        }
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotification(AccessibilityEvent event) {
        if (event.getParcelableData() == null ||
                !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dealWithRedBag(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        if (CHATUI.equals(className)) {
            // 找到红包,点中红包,下一步就是去拆红包
            findAndClickRedBag();
        } else if (REDBAG_INFO_UI.equals(event.getClassName())) {
            // 在聊天界面,去寻找红包(含有红包关键字),点击红包
            openRedBag();
        } else if (REDBAG_DETAIL_UI.equals(event.getClassName())) {
            // 拆完红包后看详细的纪录界面
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openRedBag() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.wtf(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(REDBAG_INFO_KEY_ID);
        for (AccessibilityNodeInfo n : list) {
            sleep();
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void findAndClickRedBag() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.wtf(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(CHATUI_KEY_TEXT);
        if (!list.isEmpty()) {
            // 找到最新的红包 点击领取
            for (int i = list.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                if (parent != null) {
                    sleep(1000L);
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }

    private final void sleep() {
        sleep(DEF_DURATION);
    }

    private final void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
        }
    }

    @Override
    public void onInterrupt() {
        // 系统想要中断AccessibilityService返给的响应时会调用,在整个生命周期里会被调用多次
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // 系统将要关闭这个AccessibilityService会被调用
        return super.onUnbind(intent);
    }

}
