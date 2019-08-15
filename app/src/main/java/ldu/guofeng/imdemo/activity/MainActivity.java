package ldu.guofeng.imdemo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.util.LogTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import ldu.guofeng.imdemo.IM.SmackUtils;
import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.fragment.ContactsFragment;
import ldu.guofeng.imdemo.fragment.SessionFragment;
import ldu.guofeng.imdemo.fragment.SettingFragment;

/**
 * 主页
 */
public class MainActivity extends Activity implements View.OnClickListener {

    public static  String refreshedToken;

    private static final String TAG = "MainActivity";
    private  TelephonyManager TelephonyMgr;
    private SessionFragment sessionFragment;//会话页
    private ContactsFragment contactsFragment;//联系人页
    private SettingFragment settingFragment;//设置页

    private ImageButton mSession;//会话按钮
    private ImageButton mContacts;//联系人按钮
    private ImageButton mSetting;//设置按钮
    private View currentBtn;//标记 当前按钮view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String refreshedToken2 = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(refreshedToken2)){
        /**
         * 刷新token
         */
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, task  + "", Toast.LENGTH_SHORT).show();
                    }
                });

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        findView();
        init();

        //     如果要重新接收推送
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);




//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String newToken = instanceIdResult.getToken();
//                Log.e("newToken",newToken);
//
//            }
//        });





//            requestPower();
        //这里我直接在页面创建的时候请求权限，其实不太好，这里只是为了演示
        //一般是在触发某个事件的时候再请求动态权限，比如点击按钮跳转到一个拍照页面，如果权限通过就跳转，否者吐司说没有权限！
//        TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
//        } else {
//            requestPower();
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//        }

//        String deviceId = TelephonyMgr.getDeviceId();
//        String deviceId2 = TelephonyMgr.getSimSerialNumber();
//        String deviceId23 = TelephonyMgr.getSimSerialNumber();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "ABC"; String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }


        sendMessg();
    }


        @RequiresApi(api = Build.VERSION_CODES.O)
        private void createNotificationChannel(String channelId, String channelName, int importance) {

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

    }

    String token;

    private void findView() {

        sessionFragment = (SessionFragment) getFragmentManager().findFragmentById(R.id.fragment_session);
        contactsFragment = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_constact);
        settingFragment = (SettingFragment) getFragmentManager().findFragmentById(R.id.fragment_setting);
        mSession = (ImageButton) findViewById(R.id.buttom_session);//会话
        mContacts = (ImageButton) findViewById(R.id.buttom_contacts);//联系人
        mSetting = (ImageButton) findViewById(R.id.buttom_setting);//设置
    }

    private void init() {
        mSession.setOnClickListener(this);
        mContacts.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSession.performClick();//默认点击会话页
    }

    //-----------------------------------------------------
    @Override
    public void onClick(View view) {
        // 切换Fragment
        switch (view.getId()) {
            case R.id.buttom_session://消息
                getFragmentManager().beginTransaction()
                        .show(sessionFragment)
                        .hide(contactsFragment)
                        .hide(settingFragment)
                        .commit();
                setButton(view);
                break;
            case R.id.buttom_contacts://联系人
                getFragmentManager().beginTransaction()
                        .hide(sessionFragment)
                        .show(contactsFragment)
                        .hide(settingFragment)
                        .commit();
                setButton(view);
                break;
            case R.id.buttom_setting://设置
                getFragmentManager().beginTransaction()
                        .hide(sessionFragment)
                        .hide(contactsFragment)
                        .show(settingFragment)
                        .commit();
                setButton(view);
                break;
            default:
                break;
        }
    }

    private void setButton(View v) {
        if (currentBtn != null && currentBtn.getId() != v.getId()) {
            currentBtn.setEnabled(true);
        }
        v.setEnabled(false);
        currentBtn = v;
    }










    //申请两个权限，录音和拍照录像权限
    //1、首先声明一个数组permissions，将需要的权限都放在里面
    String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
    };
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//权限请求码


    private void requestPower() {


        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
//      Toast.makeText(this,"权限已经通过了",Toast.LENGTH_LONG).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

        }

             String deviceId = TelephonyMgr.getDeviceId();
             String deviceId2 = TelephonyMgr.getSimSerialNumber();



    }

    private void sendMessg() {

        SmackUtils.getInstance().sendMessageTwo("");

    }


        /**
         * 请求权限的回调
         *
         * 参数1：requestCode-->是requestPermissions()方法传递过来的请求码。
         * 参数2：permissions-->是requestPermissions()方法传递过来的需要申请权限
         * 参数3：grantResults-->是申请权限后，系统返回的结果，PackageManager.PERMISSION_GRANTED表示授权成功，PackageManager.PERMISSION_DENIED表示授权失败。
         * grantResults和permissions是一一对应的
         */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                //全部权限通过，可以进行下一步操作。。。
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
        }


    }

    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;
    String mPackName = "org.appspot.apprtc";//当前项目的包名
    private void showPermissionDialog() {


        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();

                        }
                    })
                    .create();
        }
        mPermissionDialog.show();

    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }



}
