import axios from "axios";
import {refresh, isRefresh} from "@/http/login.js";
import {useUserStore} from "@/stores/user.js";
import router from "@/router/index.js";

const instance = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 5000,
})

instance.interceptors.request.use(config => {
    const userStore = useUserStore()
    const token = userStore.auth?.accessToken
    if (token) {
        config.headers.accessToken = token
    }
    return config
}, error => {
    return Promise.reject(error)
})

instance.interceptors.response.use(async response => {
    const {code, data, msg} = response.data
    if (code === 401) {
        if (!isRefresh(response.config)) {
            // 刷新token
            const refreshResp = await refresh()
            if (refreshResp == null) {
                return refreshResp
            }
            console.log("刷新token成功")
            const userStore = useUserStore()
            userStore.setAuth({
                accessToken: refreshResp.accessToken,
                refreshToken: refreshResp.refreshToken
            })

            response.config.headers.accessToken = userStore.auth?.accessToken
            // 重新执行请求
            return instance(response.config)
        } else {
            console.log("跳转到登录页")
            router.push('/login')
            // 刷新token无效，跳转到登录页
            // window.location.href = '/login'
        }

    }
    // 剥离后端CommonResult封装

    // 正常响应结果，返回响应数据
    if (code === 200){
        return data;
    }

    alert(msg)
    throw Error(msg)
}, error => {
    return Promise.reject(error)
})

export default instance
