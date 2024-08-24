import KingToast from "@/components/common/KingToast.vue";
import {createApp} from "vue";

export const ShowToast = ({message, timeout, type}) => {

    let toastContainerElement = document.querySelector("#king-toast-container")
    if (toastContainerElement == null) {
        toastContainerElement = document.createElement("div")
        toastContainerElement.id = 'king-toast-container'
        document.body.appendChild(toastContainerElement)
    }

    const container = document.createElement("div")
    container.classList.add('toast-item')
    toastContainerElement.appendChild(container)
    const toast = createApp(KingToast, {
        message,
        type,
    })
    // 挂载容器
    toast.mount(container)

    // 延迟后关闭容器
    setTimeout(() => {
        toast.unmount()
        container.remove()
        if (toastContainerElement.childElementCount === 0) {
            toastContainerElement.remove();
        }
    }, timeout || 1000)
}
