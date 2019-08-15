package ldu.guofeng.imdemo.bean;

import org.jivesoftware.smack.packet.Stanza;

/**
 * 创建者
 * 创建时间   2019/4/23 19:59
 * 描述	      ${TODO}
 * <p>
 * 更新者
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Meassemgaaa extends Stanza {
    @Override
    public String toString() {
        return null;
    }

    @Override
    public CharSequence toXML(String enclosingNamespace) {

        String s = "<iq type='get' id='b36d63aa-c8f3-4262-9368-2e7ab0858bbe'>" +
                "<certificate xmlns='iq:certificate:query'></certificate>" +
                "</iq>";

        return s;
    }
}
