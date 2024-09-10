export const buildNoticeMessage = ({type, toUid, roomId, fromUid, content, sendTime}) => {
    return {
        toUid,
        roomId,
        fromUid,
        content,
        type,
        sendTime: sendTime || new Date().getTime(),
        contentType: 999,
    }
}