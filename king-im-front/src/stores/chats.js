import {ref, computed} from 'vue'
import {defineStore} from 'pinia'
import {pullMsgAPI, pullOfflineMsgAPI, revokeMsgAPI} from "@/http/message.js";
import {useContactsStore} from "@/stores/contacts.js";
import {useGroupsStore} from "@/stores/groups.js";
import {useUserStore} from "@/stores/user.js";
import {buildNoticeMessage} from "@/utils/msgUtils.js";

const messageStatus = {
    SENDING: 1,
    NORMAL: 2,
    REVOKE: 4,
}

export const useChatsStore = defineStore('chats', () => {
    const chats = ref([])
    const currentChatId = ref()
    const currentChatType = ref()
    const minMsgId = ref()
    const referMsgData = ref({
        msg: null,
        chatId: null,
        chatType: null
    })

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

    const referMsgDataGetter = computed(() =>  {
        return referMsgData.value
    })

    // chatId 可能为私聊，也可能为群聊，配合type可以唯一确定一个session
    const getChat = (chatId, type) => {
        for (let chat of chats.value) {
            if (chat.chatId === chatId && chat.type === type) {
                return chat
            }
        }
        console.log("会话不存在")
        // return chats.value.find(chat => chat.chatId === chatId && chat.type === type)
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
                atMe: false,
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
        let currentChat = getChat(chatId, type);
        if (currentChat) {
            currentChatId.value = chatId
            currentChatType.value = type
            currentChat.unreadCount = 0;
            currentChat.atMe = false
        }
    }
    const insertMessage = (chatId, type, message, idx) => {
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
            // console.log("查询到联系人信息：", session, chatId, type)
        } else if (type === 2) {
            const groupStore = useGroupsStore()
            session = groupStore.getGroup(chatId)
            // console.log("查询到群信息：", session, chatId, type)
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

        doInsertMessage(chat, message, idx)
    }

    const revokeMessage = async (msgId) => {
        await revokeMsgAPI(msgId)
        let msg = getChatMessage(currentChatId.value, currentChatType.value, msgId)
        if (msg == null) {
            console.log("待撤回消息不存在");
            return;
        }
        msg.status = messageStatus.REVOKE
        insertMessage(currentChatId.value, currentChatType.value, msg)
    }

    /**
     * 删除消息成功返回删除索引位置
     * @param chatId
     * @param type
     * @param msgId
     * @returns number
     */
    const deleteMessage = (chatId, type, msgId) => {
        let chat = getChat(chatId, type)
        if (chat == null) {
            return -1;
        }
        let delIdx = chat.messages?.findIndex(msg => msg.id === msgId)
        if (delIdx >= 0) {
            chat.messages.splice(delIdx, 1)
        }

        return delIdx;
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
            console.log("会话未找到")
            return
        }
        chats.value.splice(idx, 1)
    }

    // 执行插入消息操作
    const doInsertMessage = (chat, message, idx) => {
        let insertPos = chat.messages.length

        // 设置索引位置
        // 优先指定索引位置；没有指定则依照id大小顺序插入
        if (idx != null) {
            insertPos = idx
        } else {
            if (message.contentType === 999) {
                // todo
            } else {
                // 消息复原，找到消息对应索引位置
                for (let idx in chat.messages) {
                    // 重复消息
                    if (chat.messages[idx].id === message.id) {
                        // 新消息为撤回消息
                        if (message.status === 4) {
                            // 本地存在消息，需要移除该条消息  撤回消息处理
                            deleteMessage(chat.chatId, chat.type, message.id)
                            let noticeMsg = buildNoticeMessage({
                                type: chat.type,
                                toUid: message.toUid,
                                fromUid: message.fromUid,
                                content: message.fromUid === useUserStore().info?.id ? '你已撤回一条消息': '对方撤回一条消息',
                                roomId: message.roomId,
                                sendTime: message.sendTime,
                            });
                            doInsertMessage(chat, noticeMsg, idx)
                            chat.lastContent = noticeMsg.content
                        } else if (message.status === 2) {
                            // 修改消息状态为已发送，消息状态回调
                            chat.messages[idx].status = 2
                        }
                        return
                    }
                    if (chat.messages[idx].sendTime > message.sendTime) {
                        insertPos = idx
                        break
                    }
                }
            }
        }

        // 非当前会话消息处理
        // 对于computed 需要 通过value 才能拿到原始值

        // 第一次拉去在线消息，消息状态为未发送
        if ((!currentChatIdGetter.value && !currentChatTypeGetter.value) || currentChatIdGetter.value !== chat.chatId || currentChatTypeGetter.value !== chat.type) {
            let myId = useUserStore().info?.id
            if (message.status === 1 && message.fromUid !== myId) {
                chat.unreadCount += 1

                // at我，红字内容提示
                if (message.atUids && message.atUids.indexOf(myId) >= 0) {
                    chat.atMe = true
                }
            }

        }

        if (chat.lastSendTime < message.sendTime) {
            chat.lastSendTime = message.sendTime
            // 修改会话信息
            if (message.contentType === 1) {
                chat.lastContent = message.content
            } else if (message.contentType === 2) {
                chat.lastContent = '[图片]'
            } else if (message.contentType === 3) {
                chat.lastContent = '[音频]'
            } else if (message.contentType === 4) {
                chat.lastContent = '[视频]'
            } else if (message.contentType === 5) {
                chat.lastContent = '[文件]'
            } else {
                chat.lastContent = message.content
            }
        }

        if (minMsgId.value == null) {
            minMsgId.value = message.id
        } else {
            if (minMsgId.value < message.id) {
                minMsgId.value = message.id
            }
        }
        // minMsgId.value = minMsgId.value == null ? message.id : (minMsgId.value < message.id ? message.id : minMsgId.value)
        chat.messages.splice(insertPos, 0, message)
    }

    const pullOfflineMsg = async () => {
        await pullOfflineMsgAPI()
    }

    const setReferMessage = (chatId, chatType, msg) => {
        console.log(chatId, chatType, msg)
        referMsgData.value = {
            chatId,
            chatType,
            msg,
        }
    }

    const clearReferMessage = () => {
        referMsgData.value = {}
    }

    const pullMsg = async () => {
        await pullMsgAPI((minMsgId.value == null || minMsgId.value === 0) ? 0 : minMsgId.value)
    }

    const getChatMessage = (chatId, type, msgId) => {
        const chat = getChat(chatId, type)
        if (!chat) {
            return;
        }
        for (let message of chat.messages) {
            if (message.id === msgId) {
                return message;
            }
        }
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
        deleteMessage,

        referMsgDataGetter,
        setReferMessage,
        clearReferMessage,

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
