export const buildNoticeMessage = ({type, toUid, roomId, fromUid, content}) => {
    return {
        toUid,
        roomId,
        fromUid,
        content,
        type,
        sendTime: new Date().getTime(),
        contentType: 999,
    }
}