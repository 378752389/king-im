import request from "@/http/request.js";

/**
 *
 * @param msgType 1-5 *
 * @param text 发送文本内容， 媒体消息暂时默认为空 *
 * @param referMsgId 回执消息id
 * @param atUids at用户列表
 *
 * @param toUid  测试必传*
 * @param chatType 测试必传*
 * @param roomId  测试必传*
 *
 * @param file 媒体消息上传时的文件对象
 * @param uploadResult  媒体上传结果
 * @returns {*}
 */
export const sendAPI = ({
                            msgType,
                            text,
                            referMsgId,
                            atUids,
                            chatId,
                            chatType,
                            extra,
                        }) => {

    let msgRequest = {
        msgType,
        text,
        referMsgId,
        atUids,
        extra,
    }

    if (chatType === 1) {
        msgRequest.toUid = chatId
    } else if (chatType === 2) {
        msgRequest.roomId = chatId
    } else {
        throw Error("不支持传入的会话类型")
    }

    return request({
        url: "/msg/send",
        method: "post",
        data: msgRequest
    })
}

export const pullOfflineMsgAPI = () => {
    return request({
        url: `/msg/pullOfflineMessage`,
        method: "get",
    })
}

export const pullMsgAPI = (minMsgId) => {
    return request({
        url: `/msg/pullMessage?minMsgId=${minMsgId}`,
        method: 'get'
    })
}

let msgTypeEnum = {
    TEXT: 1,
    PICTURE: 2,
    AUDIO: 3,
    VIDEO: 4,
    FILE: 5,
}

export const sendPictureMsgAPI = ({
                                      msgType,
                                      text,
                                      referMsgId,
                                      atUids,
                                      chatId,
                                      chatType
                                  }, {size, name, type, url}) => {
    let extra = {}
    extra.pictureExtra = {
        size,
        name,
        type,
        url
    }
    return sendAPI({msgType, text, referMsgId, atUids, chatId, chatType, extra})
}

export const sendAudioMsgAPI = ({
                                    msgType,
                                    text,
                                    referMsgId,
                                    atUids,
                                    chatId,
                                    chatType
                                }, {size, second, type, url}) => {
    let extra = {}
    extra.audioExtra = {
        size,
        second,
        type,
        url
    }
    return sendAPI({msgType, text, referMsgId, atUids, chatId, chatType, extra})
}

export const sendVideoMsgAPI = ({
                                    msgType,
                                    text,
                                    referMsgId,
                                    atUids,
                                    chatId,
                                    chatType
                                }, {size, coverUrl, type, url}) => {
    let extra = {}
    extra.videoExtra = {
        size,
        coverUrl,
        type,
        url
    }
    return sendAPI({msgType, text, referMsgId, atUids, chatId, chatType, extra})
}


export const sendFileMsgAPI = ({
                                   msgType,
                                   text,
                                   referMsgId,
                                   atUids,
                                   chatId,
                                   chatType
                               }, {size, filename, type, url}) => {
    let extra = {}
    extra.fileExtra = {
        size,
        filename,
        url
    }
    return sendAPI({msgType, text, referMsgId, atUids, chatId, chatType, extra})
}


export const getHistoryCursorPage = ({chatId, chatType, cursor, size}) => {
    return request({
        url: '/msg/history',
        params: {
            chatId,
            chatType,
            cursor,
            size
        }
    })
}

export const revokeMsgAPI = (msgId) => {
    return request({
        url: '/msg/revoke',
        method: 'post',
        params: {
            msgId
        }
    })
}