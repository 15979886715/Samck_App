package ldu.guofeng.imdemo.base;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Application
 *
 * @author
 */
public class IMApplication extends Application {

    private static Context myAppContext = null;
    public static XMPPTCPConnection connection = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppContext = getApplicationContext();
        //在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(myAppContext);
        FirebaseApp.initializeApp(myAppContext);

    }

    public static Context getMyAppContext() {
        return myAppContext;
    }

}
