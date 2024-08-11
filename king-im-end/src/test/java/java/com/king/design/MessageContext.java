package java.com.king.design;

import com.king.im.msg.domain.entity.Msg;
import com.king.im.sender.domain.enums.MessageTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageContext {

    private final Map<MessageTypeEnum, MessageStrategy> strategyMap = new ConcurrentHashMap<>();

    public MessageContext(List<MessageStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getMessageType(), strategy));
    }

    public void process(Msg msg) {
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.valueOf(msg.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("不支持该消息类型");
        }
        MessageStrategy messageStrategy = strategyMap.get(messageTypeEnum);
        messageStrategy.process(msg);
    }

}
