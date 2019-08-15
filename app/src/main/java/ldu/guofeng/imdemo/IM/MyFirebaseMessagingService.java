package ldu.guofeng.imdemo.IM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.MainActivity;
import ldu.guofeng.imdemo.base.IMApplication;

/**
 * 创建者
 * 创建时间   2018/11/21 16:50
 * 描述	      ${TODO}
 * <p>
 * 更新者
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMessagingServ";
    private NotificationManager myManager;
    private Notification myNotification;
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "消息服务已启动");
//        updateNofitication("消息服务已启动",1);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "===============通知来啦=================");
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "getData" + remoteMessage.getData());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            onReceive(remoteMessage.getNotification().getBody());
        }else {
        updateNofitication(remoteMessage.getNotification().getBody(),1);

        }


    }


    private void updateNofitication(String form, int type) {
        Intent intent_nftc = new Intent();
        intent_nftc.putExtra("form", form);
        intent_nftc.putExtra("type", type);
        intent_nftc.setAction("TYPE_NEW_MSG");
        IMApplication.getMyAppContext().sendBroadcast(intent_nftc);
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        Log.e(TAG, "更新token了: " + token);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onReceive(String body){

        //从系统服务中获得通知管理器
        myManager = ((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE));

        //通过Notification.Builder来创建通知
        Notification.Builder myBuilder = new Notification.Builder(this,"ABC");
        myBuilder.setContentTitle("NSTFCMDemo")
                .setContentText(body)
                .setTicker("新消息")
                //设置状态栏中的小图片
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH);//高优先级
        myNotification = myBuilder.build();
        //通过通知管理器来发起通知，ID区分通知
        myManager.notify(NOTIFICATION_ID, myNotification);

    }

}
