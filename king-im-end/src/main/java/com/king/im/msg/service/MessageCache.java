package com.king.im.msg.service;

public interface MessageCache {

    public Long getRoomMessagePosition(Long roomId, Long userId);

    public void setRoomMessagePosition(Long roomId, Long userId, Long position);
}
