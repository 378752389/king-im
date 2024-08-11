import KingToast from "@/components/common/KingToast.vue";
import {createApp} from "vue";

export const ShowToast = ({message, timeout}) => {
    const container = document.createElement("div")
    document.body.appendChild(container)
    const toast = createApp(KingToast, {
        message,
    })
    // 挂载容器
    toast.mount(container)

    // 延迟后关闭容器
    setTimeout(() => {
        toast.unmount()
        container.remove()
    }, timeout || 1000)
}