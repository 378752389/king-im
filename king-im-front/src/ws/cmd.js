import {useUserStore} from "@/stores/user.js";
export const CMD = {
    PING: 1,
    PONG: 2,
    LOGIN: 3,
    LOGOUT: 4,
    CHAT: 5,
}

export const getPing = () => {
    return JSON.stringify({
        cmd: CMD.PING,
        data: {}
    })
}

export const getLogin = () => {
    console.log(useUserStore().auth?.accessToken)
    return JSON.stringify({
        cmd: CMD.LOGIN,
        data: {accessToken: useUserStore().auth?.accessToken}
    })
}

export const getLogout = () => {
    return JSON.stringify({
        cmd: CMD.LOGOUT,
        data: {}
    })
}
