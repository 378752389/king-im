package java.com.king.design;

import com.king.im.msg.domain.entity.Msg;
import com.king.im.sender.domain.enums.MessageTypeEnum;

public interface MessageStrategy {

    public MessageTypeEnum getMessageType();

    public void process(Msg msg);
}
