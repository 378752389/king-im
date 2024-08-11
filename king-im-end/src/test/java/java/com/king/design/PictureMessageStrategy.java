package java.com.king.design;

import com.king.im.msg.domain.entity.Msg;
import com.king.im.sender.domain.enums.MessageTypeEnum;

public class PictureMessageStrategy implements MessageStrategy{
    @Override
    public MessageTypeEnum getMessageType() {
        return MessageTypeEnum.PICTURE;
    }

    @Override
    public void process(Msg msg) {
        System.out.println("图片处理");
    }
}
