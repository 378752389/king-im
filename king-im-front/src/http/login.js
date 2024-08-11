import request from '@/http/request.js'
import {useUserStore} from "@/stores/user.js";

export const login = (username, password) => {
    return request({
        url: '/login',
        method: 'post',
        data: {
            username,
            password
        }
    })
}

export const logout = () => {

}



let refreshPromise = null
export const refresh = (refreshToken) => {
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