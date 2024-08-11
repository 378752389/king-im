import {CMD, getLogin, getPing} from "@/ws/cmd.js";

let ws = null;
let isConnected = false;

// 重连配置
let reconTimer = null;
let reconTimeout = 15000;

let businessCallback = null;
let loginSuccessCallback = null;

export const doConnect = (url) => {
    try {
        if (isConnected) {
            return
        }

        console.log("连接ws...")
        ws = new WebSocket(url)

        // 连接建立
        ws.onopen = (e) => {
            console.log("ws连接成功!")
            isConnected = true
            // 发送登录命令
            const login = getLogin()
            ws.send(login)
        }

        // 消息监听
        ws.onmessage = (e) => {
            let resp = JSON.parse(e.data)
            console.log(resp)
            switch (resp.cmd) {
                case CMD.LOGIN:
                    // 登录成功，开始心跳连接
                    console.log("登录成功，开启心跳")
                    loginSuccessCallback && loginSuccessCallback()
                    heartBeatBehavior.ping()
                    break
                case CMD.LOGOUT:
                    console.log("登出成功")
                    // todo 登出
                    break
                case CMD.PONG:
                    heartBeatBehavior.ping()
                    break
                default:
                    businessCallback && businessCallback(resp)
                    break
            }
        }

        // 连接关闭
        ws.onclose = (e) => {
            console.log("关闭ws...")
            isConnected = false
        }
    } catch (e) {
        console.log('连接创建失败，尝试重新连接')
        doReconnect(url);
    }
}

export const doDisconnect = () => {
    if (ws) {
        ws.close()
    }
}
export const setBusinessCallback = (cb) => {
    businessCallback = cb
}

export const setLoginSuccessCallback = (cb) => {
    loginSuccessCallback = cb
}

export const send = (data) => {
    ws.send(data)
}

window.mySend = send
const doReconnect = (url) => {
    if (isConnected) {
        return
    }

    reconTimer && clearTimeout(reconTimer)
    reconTimer = setTimeout(() => {
        doConnect(url)
    }, reconTimeout)
}

let heartBeatBehavior = {
    timeout: 10000,
    timer: null,
    ping() {
        this.timer && clearTimeout(this.timer)
        this.timer = setTimeout(() => {
            // 发送心跳命令
            const ping = getPing()
            ws.send(ping)
        }, this.timeout)
    }
}