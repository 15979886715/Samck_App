package ldu.guofeng.imdemo.bean;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jivesoftware.smack.packet.Element;
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
public class MessageIQTwo extends IQ {

    private static final String TAG = "MessageIQ";
    private  String name = "";
    public MessageIQTwo(String name) {
        super("roomChat","http://www.jivesoftware.org/protocol/roomChat#log");
        this.name = name;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        xml.element("value",name);
        xml.halfOpenElement("page").append(" ").append("pageNumber = '1'").append(" ").append("pageSize = '2'").rightAngleBracket().closeElement("page");
        return xml;
    }
}
