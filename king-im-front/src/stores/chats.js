import {ref, computed} from 'vue'
import {defineStore} from 'pinia'
import {pullMsgAPI, pullOfflineMsgAPI} from "@/http/message.js";
import {useContactsStore} from "@/stores/contacts.js";
import {useGroupsStore} from "@/stores/groups.js";
import {useUserStore} from "@/stores/user.js";

const messageStatus = {
    NORMAL: 1,
    REVOKE: 2,
    DELETE: 3,
    ERROR: 9
}

export const useChatsStore = defineStore('chats', () => {
    const chats = ref([])
    const currentChatId = ref()
    const currentChatType = ref()
    const minMsgId = ref()

    const currentChatGetter = computed(() => {
        return getChat(currentChatId.value, currentChatType.value)
    })

    const chatsGetter = computed(() => {
        return chats.value
    })

    const currentChatIdGetter = computed(() => {
        return currentChatId.value
    })

    const currentChatTypeGetter = computed(() => {
        return currentChatType.value
    })

    const minMsgIdGetter = computed(() => {
        return minMsgId.value
    })

    // chatId 可能为私聊，也可能为群聊，配合type可以唯一确定一个session
    const getChat = (chatId, type) => {
        return chats.value.find(chat => chat.chatId === chatId && chat.type === type)
    }

    const moveChatTop = (chatId, type) => {
        const chat = getChat(chatId, type)
        const idx = chats.value.indexOf(chat)
        if (idx === -1) {
            return
        }
        chats.value.splice(idx, 1)
        chats.value = [chat, ...chats.value]
    }

    // 创建聊天会话
    const openChat = (chatId, type, session) => {
        if (session == null) {
            return;
        }
        const {avatar, peerAvatar, markName, peerMarkName, peerNickname, name} = session
        let chat = getChat(chatId, type)
        if (!chat) {
            chat = {
                chatId: chatId,
                type: type,
                unreadCount: 0,
                messages: [],
                lastNickname: undefined,
                lastContent: undefined,
                lastSendTime: null,
                chatAvatar: avatar || peerAvatar,
                chatName: peerMarkName || peerNickname || markName || name,
                createTime: new Date().getTime()
            }

            chats.value.push(chat)
        }
        return chat
    }

    const setChat = (chatId, type) => {
        currentChatId.value = chatId
        currentChatType.value = type
        let currentChat = getChat(chatId, type);
        currentChat.unreadCount = 0;
    }
    const insertMessage = (chatId, type, message) => {
        let session = null;
        if (chatId == null || type == null) {
            throw new Error("回话信息不能为空")
        }
        if (type === 1) {
            const contactStore = useContactsStore();
            session = contactStore.getContact(chatId)
            // 如果消息是发送给自己，则创建一个session
            if (!session) {
                let userStore = useUserStore()
                if (userStore.info.id === chatId) {
                    session = {
                        peerAvatar: userStore.info?.avatar,
                        peerNickname: userStore.info?.nickName,
                    }
                }
            }
            console.log("查询到联系人信息：", session, chatId, type)
        } else if (type === 2) {
            const groupStore = useGroupsStore()
            session = groupStore.getGroup(chatId)
            console.log("查询到群信息：", session, chatId, type)
        } else {
            throw Error("type must be 1 or 2")
        }

        let chat = openChat(chatId, type, session)
        if (chat == null) {
            console.error("chatId: ", chatId, " type: ", type, "回话不存在")
            return;
        }

        if (message == null) {
            return;
        }

        // 撤回消息处理
        // if (message.status === messageStatus.REVOKE) {
        //     revokeMessage(chatId, type, message)
        //     return
        // }

        doInsertMessage(chat, message)
    }

    const revokeMessage = (chatId, type, msg) => {
        // 非撤回消息不进行处理
        if (msg.status !== messageStatus.REVOKE) {
            return
        }
        let chat = getChat(chatId, type)
        if (!chat) {
            throw new Error('chat not found')
        }
        let msgIdx = chat.messages.findIndex(x => x.id === msg.id)
        if (msgIdx === -1) {
            throw new Error('message not found')
        }
        if (chat.messages[msgIdx].status === messageStatus.REVOKE) {
            console.log("message already revoked")
            return;
        }

        // 删除旧聊天消息
        chat.messages.splice(msgIdx, 1)

        // 创建撤回消息
        const senderName = msg.type === 1 ? '对方' : msg.name
        const revokeMessage = {
            id: msg.id,
            roomId: msg.roomId,
            fromUid: msg.fromUid,
            toUid: msg.toUid,
            type: msg.type,
            name: msg.name,
            content: senderName + '  撤回一条消息',
            atUids: msg.atUids,
            sendTime: msg.sendTime,
            status: messageStatus.REVOKE,
        }
        console.log("revokeMessage", revokeMessage)

        doInsertMessage(chat, revokeMessage)
    }

    const clearChat = () => {
        chats.value = []
        currentChatId.value = null
        currentChatType.value = null
        minMsgId.value = null
    }

    const removeChat = (chatId, type) => {
        let idx = chats.value.findIndex(chat => chat.chatId === chatId && chat.type === type)
        if (idx === -1) {
            throw new Error('chat not found')
        }
        chats.value.splice(idx, 1)
    }

    // 执行插入消息操作
    const doInsertMessage = (chat, message) => {
        let insertPos = chat.messages.length
        for (let idx in chat.messages) {
            // 重复消息，直接跳过，不做处理
            if (chat.messages[idx].id === message.id) {
                return
            }
            if (chat.messages[idx].sendTime > message.sendTime) {
                insertPos = idx
                break
            }
        }
        // 非当前会话消息处理
        // 对于computed 需要 通过value 才能拿到原始值
        if ((!currentChatIdGetter.value && !currentChatTypeGetter.value) || currentChatIdGetter.value !== chat.chatId || currentChatTypeGetter.value !== chat.type) {
            chat.unreadCount += 1
        }
        // 修改会话信息
        chat.lastContent = message.content
        chat.lastSendTime = chat.lastSendTime > message.sendTime ? chat.lastSendTime : message.sendTime
        minMsgId.value = minMsgId.value == null ? message.id : (minMsgId.value < message.id ? message.id : minMsgId.value)

        chat.messages.splice(insertPos, 0, message)
    }

    const pullOfflineMsg = async () => {
        await pullOfflineMsgAPI()
    }

    const pullMsg = async () => {
        await pullMsgAPI((minMsgId.value == null || minMsgId.value === 0) ? 0 : minMsgId.value)
    }

    const getChatMessage = (chatId, type) => {
        const chat = getChat(chatId, type)
        if (!chat) {
            return []
        }
        return chat.message.filter(x => x.status === messageStatus.NORMAL)
    }

    return {
        chats,
        minMsgId,
        currentChatId,
        currentChatType,

        chatsGetter,
        currentChatGetter,
        currentChatIdGetter,
        minMsgIdGetter,
        currentChatTypeGetter,

        insertMessage,
        pullOfflineMsg,
        pullMsg,
        getChatMessage,
        revokeMessage,

        moveChatTop,
        openChat,
        removeChat,
        setChat,
        clearChat,
        getChat,
    }
}, {
    persist: true
})
