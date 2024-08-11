package java.com.king.design;

import com.king.im.msg.domain.entity.Msg;
import com.king.im.sender.domain.enums.MessageTypeEnum;

public class AudioMessageStrategy implements MessageStrategy{
    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.Audio;
    }

    @Override
    public void process(Msg msg) {
        System.out.println("处理音频");
    }
}
