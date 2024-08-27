import axios from "axios";
import {refreshAPI, isRefresh} from "@/http/login.js";
import {useUserStore} from "@/stores/user.js";
import router from "@/router/index.js";
import {ShowToast} from "@/components/common/func/toast.js";

const instance = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
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
            const refreshResp = await refreshAPI()
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

    // 如果为400，则返回报错信息
    if (code === 400) {
        ShowToast({
            message: msg,
            timeout: 3000,
            type: 'danger'
        })
        // throw new Error(msg);
        return msg;
    }

    ShowToast({
        message: msg,
        timeout: 3000,
    })
    throw Error(msg)
}, error => {
    // 非 2xx 3xx 响应状态码处理结果
    console.log(`异常请求，路径：${error.config.url} 参数：${error.config.params}, 请求数据：${error.config.data}, 返回结果：${JSON.stringify(error.response.data)}`)
    return Promise.reject(error)
})

export default instance
