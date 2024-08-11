import {defineStore} from "pinia";
import {ref} from "vue";
import {infoAPI} from "@/http/user.js";
import {useChatsStore} from "@/stores/chats.js";

export const useUserStore = defineStore('user', () => {
    const info = ref()

    const auth = ref()

    const login = async (username, password, terminal = 1) => {
        const resp = await loginAPI(username, password, terminal)
        if (resp.code === 200) {
            auth.value = {
                accessToken: resp.data.accessToken,
                refreshToken: resp.data.refreshToken
            }
            return true;
        }

        return false;
    }

    const logout = () => {

    }

    const loginAPI = async (username, password, terminal) => {
        const resp = await fetch(`http://localhost:8080/login?username=${username}&password=${password}&terminal=${terminal}`, {
            method: 'post',
        })
        return resp.json()
    }

    const setAuth = (data) => {
        auth.value = data
    }

    const loadInfo = async () => {
        const userInfo = await infoAPI()
        if (!info.value?.id || userInfo?.id !== info.value.id) {
            const chatStore = useChatsStore()
            chatStore.clearChat()
        }
        info.value = userInfo
    }

    return {info, auth, logout, login, setAuth, loadInfo}
}, {
    persist: true
})