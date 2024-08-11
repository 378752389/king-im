import request from "@/http/request.js";

/************************************* 联系人 ****************************************************************/
export const getContactListAPI = () => {
    return request({
        url: "/friend/list",
        method: "get"
    })
}

export const addContactAPI = ({friendId}) => {
    return request({
        url: "/friend",
        method: "post",
        params: {friendId}
    })
}

export const removeContactAPI = ({friendId}) => {
    return request({
        url: "/friend",
        method: "delete",
        params: {friendId}
    })
}

export const updateContactAPI = ({peerId, peerMarkName}) => {
    return request({
        url: '/friend',
        method: 'put',
        data: {
            peerId,
            peerMarkName
        }
    })
}


/************************************* 群 ****************************************************************/
export const getGroupListAPI = () => {
    return request({
        url: "/room/list",
        method: "get"
    })
}

export const inviteFriendJoinGroupAPI = ({roomId, friendIds}) => {
    return request({
        url: "/room/invite",
        method: "post",
        params: {
            roomId, friendIds
        }
    })
}

export const applyGroupAPI = ({roomId}) => {
    return request({
        url: "/room/apply",
        method: "get",
        params: {
            roomId
        }
    })
}

export const addGroupAPI = ({name, notice, avatar}) => {
    return request({
        url: "/room",
        method: "post",
        data: {
            name, notice, avatar
        }
    })
}


export const modifyGroupAPI = ({roomId, notice, name, avatar, myName, markName}) => {
    return request({
        url: "/room",
        method: "put",
        data: {
            roomId, notice, name, avatar, myName, markName
        }
    })
}

export const quitGroupAPI = ({roomId}) => {
    return request({
        url: "/room/quit",
        method: "delete",
        params: {roomId}
    })
}

export const deleteGroupAPI = ({roomId}) => {
    return request({
        url: "/room",
        method: "delete",
        params: {
            roomId
        }
    })
}

/************************************* 公共 ****************************************************************/
export const socialSearchAPI = ({type, method, text}) => {
    return request({
        url: "/social/search",
        method: "get",
        params: {
            type,
            method,
            text
        }
    })
}