package ldu.guofeng.imdemo.bean;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Stanza;

/**
 * 创建者
 * 创建时间   2018/11/26 15:37
 * 描述	      ${TODO}
 * <p>
 * 更新者
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class XMPPmessage extends Stanza {

    @Override
    public CharSequence toXML(String enclosingNamespace) {

         return enclosingNamespace;
    }

    @Override
    public String toString() {
        return null;
    }

//    @Override
//    public CharSequence toXML(String enclosingNamespace) {
//        return null;
//    }


//    public XMPPmessage(){
//    }
//
//    @Override
//    public String getNamespace() {
//        return null;
//    }
//
//    @Override
//    public String getElementName() {
//        return null;
//    }
//
////    @Override
////    public CharSequence toXML() {
////        return null;
////    }
//
//
//
//    /**
//     27      * 返回扩展的xml字符串
//     28      * 此字符串作为message元素的子元素
//     29      */
//     @Override
//     public CharSequence toXML() {
//
//         /**
//          * <message type="chat" to="daiyunlong0429@192.168.133.151"><request xmlns="urn:xmpp:receipts"></request><body>Assad fasts</body></message>
//          */
//
//         StringBuilder builder = new StringBuilder();
//         builder.append("<message").append("type=").append("chat").append("to=").append("daiyunlong0429@192.168.133.151").append(">").append("<request").append("xmlns=").append("urn:xmpp:receipts")
//                 .append(">").append("</").append("request").append("><").append("body>").append("黎德荣").append("</body></message>");
//
//         return builder.toString();
//
//     }

}
