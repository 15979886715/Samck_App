package ldu.guofeng.imdemo.bean;

import java.io.Serializable;

import ldu.guofeng.imdemo.base.Constant;

/**
 * 会话体
 */

public class SessionModel implements Serializable {
    private String form;//发送者
    private int type;//信息类型
    private String content;//信息内容
    private String to =Constant.TO;//设置包的发送去处//设置包的发送去处


//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public String getTo(){
//        return to;
//    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
