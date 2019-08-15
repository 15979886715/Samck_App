package ldu.guofeng.imdemo.IM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.bean.Friend;
import ldu.guofeng.imdemo.bean.Meassemgaaa;
import ldu.guofeng.imdemo.bean.MessageIQ;
import ldu.guofeng.imdemo.bean.MessageIQTwo;
import ldu.guofeng.imdemo.bean.XMPPmessage;
import ldu.guofeng.imdemo.util.PreferencesUtils;

/**
 * 封装 Smack 常用方法
 */
public class SmackUtils {

    private static final String TAG = "SmackUtils";

    private static SmackUtils smackUtils;

    public static SmackUtils getInstance() {
        if (smackUtils == null) {
            smackUtils = new SmackUtils();
        }
        return smackUtils;
    }

    /**
     * 建立连接
     */
    public void getXMPPConnection() {
            ConnectionConfiguration.SecurityMode disabled;

        if (Constant.IM_SERVER.contains("192.168")){
            disabled = ConnectionConfiguration.SecurityMode.disabled;//设置TLS安全模式时使用的连接
        }else {
            disabled = ConnectionConfiguration.SecurityMode.required;//设置TLS安全模式时使用的连接
        }

        if (IMApplication.connection == null || !IMApplication.connection.isConnected()) {
            XMPPTCPConnectionConfiguration builder = null;
            try {
                builder = XMPPTCPConnectionConfiguration.builder()
                        .setHostAddress(InetAddress.getByName(Constant.IM_HOST))//IP
                        .setPort(Constant.IM_PORT)//端口
                        .setXmppDomain(JidCreate.domainBareFrom(Constant.IM_SERVER))//此处填写openfire服务器名称
                        .setCompressionEnabled(false)//是否允许使用压缩
                        .setSendPresence(true)//是否发送Presece信息
                        .setResource("Android")//设置登陆设备标识
                        .setConnectTimeout(15 * 1000)//连接超时时间
                        .setSecurityMode(disabled)//设置TLS安全模式时使用的连接
                        .build();
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            // 是否使用SASL
            SASLAuthentication.blacklistSASLMechanism(SASLMechanism.DIGESTMD5);
            IMApplication.connection = new XMPPTCPConnection(builder);
            try {
                IMApplication.connection.connect();
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查连接
     */
    private void checkConnect() {
        if (IMApplication.connection == null) {//null
            getXMPPConnection();
        }
        if (!IMApplication.connection.isConnected()) {//没有连接到服务器
            try {
                IMApplication.connection.connect();
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 断开连接
     */
    public void exitConnect() {
        if (IMApplication.connection != null && IMApplication.connection.isConnected()) {
            IMApplication.connection.disconnect();
            IMApplication.connection = null;
        }
    }


    /**
     * 检查登录
     */
    private void checkLogin() {
        if (!IMApplication.connection.isAuthenticated()) {//没有连接到服务器
            try {
                IMApplication.connection.login(
                        PreferencesUtils.getInstance().getString("username"),
                        PreferencesUtils.getInstance().getString("pwd")
                );
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     */
    public boolean register(String username, String password) {
        try {
            checkConnect();
            Map<String, String> map = new HashMap<String, String>();
            map.put("phone", "Android");
            AccountManager accountManager = AccountManager.getInstance(IMApplication.connection);
            //敏感操作跳过不安全的连接
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(Localpart.from(username), password, map);
        } catch (SmackException | XMPPException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */

    public boolean login(String username, String password) {
        try {
            checkConnect();
            if (IMApplication.connection.isAuthenticated()) {//已经登录
                return true;
            } else {
                IMApplication.connection.login(username, password);//登录
                return IMApplication.connection.isAuthenticated();
            }
        } catch (IOException | SmackException | XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取好友列表
     *
     * @return
     */
    public List<Friend> getFriendsList() {
        checkConnect();
        checkLogin();
        List<Friend> list = new ArrayList<Friend>();
        //Roster对象翻译成中文为"花名册",表示用户的所有好友清单以及申请加好友的用户清单
        Roster roster = Roster.getInstanceFor(IMApplication.connection);
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        for (RosterEntry rosterentry : rosterEntries) {
            Friend friend = new Friend();
            if (!rosterentry.getType().toString().equals("none")) {
                friend.setName(rosterentry.getUser().split("@")[0]);
                list.add(friend);
                Log.e("IMDemo", rosterentry.getUser());
            }

        }
        return list;
    }

    /**
     * 发送消息
     *
     * @param message
     * @param to
     */
    public void sendMessage(String message, String to) {
        try {
//            checkConnect();
//            checkLogin();
//            ChatManager mChatManager = ChatManager.getInstanceFor(IMApplication.connection);
//            Chat mChat = mChatManager.createChat(to + "@" + Constant.IM_HOST);
//            mChat.sendMessage(message);
//            mChat.close();


            Meassemgaaa i = new Meassemgaaa();
            Log.e(TAG, "sendMessage: " + i.toXML("") );
            IMApplication.connection.sendStanza(i);


            XMPPmessage xmpPmessage = new XMPPmessage();
            String charSequence = (String) xmpPmessage.toXML("");

            checkConnect();
            checkLogin();

            String name = PreferencesUtils.getInstance().getString("username");
            String userName = "";
            ChatManager mChatManager = ChatManager.getInstanceFor(IMApplication.connection);
            Chat mChat = mChatManager.createChat(JidCreate.entityBareFrom(to + "@" + Constant.IM_HOST));
//            Chat mChat = mChatManager.createChat(to + "@" + Constant.IM_HOST);
            Message me = new Message();
            me.setType(Message.Type.chat);
            if (name.equals("daiyunlong0428")){
                userName = "daiyunlong0429";
            }else {
                userName = "daiyunlong0428";
            }
            me.setTo(userName + "@192.168.133.151");

//            me.addSubject(" xmlns","urn:xmpp:receipts");
            me.setBody(message);

//            mChat.sendMessage(me);
//            mChat.sendMessage(me.toXML().toString());
            mChat.sendMessage(me);
            Log.e(TAG, "sendMessage: "  + me.toXML("").toString()  + "");
            Log.e(TAG, "sendMessage: "   + "+++++" + me.toXML(""));
            Log.e(TAG, "sendMessage: "   + "+++++" + me.toXML("").toString());
            mChat.close();


        } catch (SmackException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessageTwo(String message) {
        try {
            checkConnect();
            checkLogin();
            ChatManager mChatManager = ChatManager.getInstanceFor(IMApplication.connection);
            mChatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    Set<ChatMessageListener> listeners = chat.getListeners();
                    String s = listeners.toString();

                    Log.e(TAG, "返回来的群消息连接: " + createdLocally + s );
                }
            });

            String username = PreferencesUtils.getInstance().getString("username");
//            Chat mChat = mChatManager.createChat(username + "@" + Constant.IM_HOST);
            Chat mChat = mChatManager.createChat(JidCreate.entityBareFrom(username + "@" + Constant.IM_HOST));

            MessageIQ iq = new MessageIQ();
            iq.setType(IQ.Type.set);
            iq.setFrom(username + "@" + Constant.TO);
            Log.e(TAG, "sendMessageTwo: " + iq.toXML("").toString());
            IMApplication.connection.sendStanza(iq);



//            MessageIQTwo messageIQTwo = new MessageIQTwo("a" + "@" + Constant.TO);
//            messageIQTwo.setType(IQ.Type.get);

//            Log.e(TAG, "sendMessageTwo: " + messageIQTwo.toXML("").toString());
//            try {
//                IMApplication.connection.sendStanza(messageIQTwo);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        } catch (SmackException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加好友
     *
     * @param userName
     */
    public void addFriend(String userName) {
        try {
            checkConnect();
            checkLogin();
            Roster roster = Roster.getInstanceFor(IMApplication.connection);
            roster.createEntry(JidCreate.bareFrom(userName), userName, null);
//            roster.createEntry(userName, userName, null);
        } catch (SmackException | XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除好友
     *
     * @param userJID
     * @return
     */
    public void deleteFriend(String userJID) {
        try {
            checkConnect();
            checkLogin();
            Roster roster = Roster.getInstanceFor(IMApplication.connection);
            roster.removeEntry(roster.getEntry(JidCreate.bareFrom(userJID)));
//            roster.removeEntry(roster.getEntry(userJID));
        } catch (SmackException | XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }
}
