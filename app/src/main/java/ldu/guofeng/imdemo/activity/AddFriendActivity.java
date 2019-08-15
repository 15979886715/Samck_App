package ldu.guofeng.imdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;

import java.util.ArrayList;
import java.util.List;

import ldu.guofeng.imdemo.IM.SmackUtils;
import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 添加好友
 */
public class AddFriendActivity extends CustomReturnToolbar {

    private static final String TAG = "AddFriendActivity";

    private EditText userName;
    private Button btn_add;

    /**
     * 添加好友
     */
    private void doAddFriend() {
        final String username = userName.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {



/**
                UserSearchManager usm = new UserSearchManager(IMApplication.connection);
                Form searchForm = null;
                try {
                    searchForm = usm.getSearchForm(IMApplication.connection.getServiceName());
                    Form answerForm = searchForm.createAnswerForm();
                    answerForm.setAnswer("Username", 1);
                    answerForm.setAnswer("search", username);
                    ReportedData data = usm.getSearchResults(answerForm, IMApplication.connection.getServiceName());
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
*/





//                //把对方拉入黑名单
//                PrivacyListManager plm = PrivacyListManager.getInstanceFor(IMApplication.connection);
//                try {
//                     List<PrivacyList> privacyLists = plm.getPrivacyLists();
//                     PrivacyList privacyList = privacyLists.get(0);
//                     String name = privacyList.getName();
////                    List<PrivacyItem> items = privacyList.getItems();
//                    Log.e(TAG, "name: " + name);
////                    Log.e(TAG, "run: " + items.size());
////                    Log.e(TAG, "Value: " + items.get(0).getValue());
//                // 再创建一个隐私列表
//                List<PrivacyItem> items = new ArrayList<PrivacyItem>();
//                // 黑名单实现
//                PrivacyItem item1 = new PrivacyItem(PrivacyItem.Type.jid,"ron002" + "@" + Constant.IM_HOST,false, 1);
//                item1.setFilterIQ(true);
//                item1.setFilterPresenceIn(true);
//                item1.setFilterPresenceOut(true);
//                item1.setFilterMessage(true);
//                    items.add(item1);
//                    String activeListName = plm.getActiveListName();
//                    // 最后要将这个隐私列表创建到服务器上，并设置为激活状态。
//                    plm.createPrivacyList("MylistName", items);
//                    plm.setActiveListName("MylistName");
//
//
//                } catch (SmackException.NoResponseException e) {
//                    e.printStackTrace();
//                } catch (XMPPException.XMPPErrorException e) {
//                    e.printStackTrace();
//                } catch (SmackException.NotConnectedException e) {
//                    e.printStackTrace();
//                }




//                try {
//
//
//                    MultiUserChatManager instanceFor = MultiUserChatManager.getInstanceFor(IMApplication.connection);
//                    MultiUserChat chatRoom = new MultiUserChat(IMApplication.connection, "房间名1" + "@" + Constant.IM_HOST,instanceFor);
//
//
//
//                    Form configurationForm = chatRoom.getConfigurationForm();
//                    Form submitForm  = configurationForm.createAnswerForm();
//
//                    //房间的名称
//                    submitForm.setAnswer("muc#roomconfig_roomname", "TestUserNumber");
//                    //保证只有注册的昵称才能进入房间
//                    submitForm.setAnswer("x-muc#roomconfig_reservednick",true);
//                    //设置为永久房间
//                    submitForm.setAnswer("muc#roomconfig_persistentroom",true);
//
//                    List<String> list = new ArrayList<>();
//                    list.add("1");
//                    list.add("2");
//                    list.add("3");
//                    list.add("4");
//                    list.add("5");
//
//                    //设置房间人数上限，注意，参数不是int！！！！！
//                    submitForm.setAnswer("muc#roomconfig_maxusers",list);
//                    chatRoom.sendConfigurationForm(submitForm);
//
//
////                    chatRoom.join("房间1" + "@" + Constant.IM_HOST);
//
//                } catch (SmackException.NoResponseException e) {
//                    e.printStackTrace();
//                } catch (XMPPException.XMPPErrorException e) {
//                    e.printStackTrace();
//                } catch (SmackException.NotConnectedException e) {
//                    e.printStackTrace();
//                }


                SmackUtils.getInstance().addFriend(username + "@" + Constant.IM_HOST);

            }
        }).start();
    }

    //------------------------------------------
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("添加好友");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    private void initView() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAddFriend();
            }
        });
    }


    private void findView() {
        userName = (EditText) findViewById(R.id.et_username);
        btn_add = (Button) findViewById(R.id.btn_add);
    }
}
