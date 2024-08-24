import {defineStore} from "pinia";
import {ref} from "vue";
import {infoAPI} from "@/http/user.js";
import {useChatsStore} from "@/stores/chats.js";
import {loginAPI} from "@/http/login.js";

export const useUserStore = defineStore('user', () => {
    const info = ref()

    const auth = ref()

    const login = async (username, password, terminal = 1) => {
        try {
            const resp = await loginAPI(username, password, terminal)
            auth.value = {
                accessToken: resp?.accessToken,
                refreshToken: resp?.refreshToken
            }
            return true;
        } catch (e) {
            console.error(e);
            return false;
        }
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

    return {info, auth, login, setAuth, loadInfo}
}, {
    persist: true
})