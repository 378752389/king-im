import {createApp} from "vue";
import KingMessageBox from "@/components/common/KingMessageBox.vue";

export const ShowMessageBox = ({title, message, confirm, cancel}) => {
    let messageBoxContainer = document.createElement("div")
    document.body.appendChild(messageBoxContainer)
    let messageBox = createApp(KingMessageBox, {
        title,
        message,
        confirm: () => {
            try {
                confirm && confirm()
            } finally {
                messageBox.unmount()
                messageBoxContainer.remove()
            }
        },
        cancel: () => {
            try {
                cancel && cancel()
            } finally {
                messageBox.unmount()
                messageBoxContainer.remove()
            }
        }
    })
    messageBox.mount(messageBoxContainer)
}