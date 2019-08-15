package ldu.guofeng.imdemo.bean;

import android.text.TextUtils;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import org.jivesoftware.smack.packet.IQ;


/**
 * 创建者
 * 创建时间   2018/11/26 10:19
 * 描述	      ${TODO}
 * <p>
 * 更新者
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MessageIQ extends IQ {

    private static final String TAG = "MessageIQ";


    public MessageIQ() {
        super("query","urn:xmpp:push");
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {

        String  refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String token = "fXcC0k-Ps-8:APA91bEtPY0EYACIlK1omAkHvGbGk3ah6-ilJ6dejvHudIQPMcw3HVTKxWcu-6CpVd2-sUQu9AmXw2NkdMzqGma92sC-1Gxunjnb-Aafjl83kOJWP3qbOTTbkte07mMdEOs3AzhB32hw";
        if (!TextUtils.isEmpty(refreshedToken)){
            token = refreshedToken;
            Log.e(TAG, "新TOKEN新TOKEN新TOKEN新TOKEN: " + token);
        }

        xml.rightAngleBracket();
        xml.element("token",token);
        xml.element("tokenType","Android");
        return xml;
    }
}
