import request from '@/http/request.js'
import {useUserStore} from "@/stores/user.js";

export const loginAPI = (username, password, terminal) => {
    return request({
        url: '/login',
        method: 'post',
        params: {
            username,
            password,
            terminal,
        }
    })
}

let refreshPromise = null
export const refreshAPI = (refreshToken) => {
    if (refreshPromise) {
        return refreshPromise
    }

    refreshPromise = new Promise((resolve) => {
        const resp = request({
            url: '/refreshToken',
            method: 'post',
            params: {
                refreshToken: refreshToken ? refreshToken : useUserStore().auth?.refreshToken
            },
            ___isRefresh: true // 标记是刷新令牌的请求
        })
        resolve(resp)
    })

    refreshPromise.finally(() => {
        refreshPromise = null
    })

    return refreshPromise
}

export const isRefresh = (config) => {
    return !!config.___isRefresh
}