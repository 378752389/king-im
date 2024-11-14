<script setup>
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import {onMounted, ref, watch, watchEffect} from "vue";
import {useUserStore} from "@/stores/user.js";
import {useChatsStore} from "@/stores/chats.js";
import {getSendTimeNormalize} from "@/utils/dateUtils.js";
import {ShowToast} from "@/components/common/func/toast.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";
import {buildNoticeMessage} from "@/utils/msgUtils.js";
import KingContextMenu from "@/components/common/KingContextMenu.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";

const props = defineProps({
  messageList: {
    type: Array,
    default: []
  }
})

const userStore = useUserStore()
const chatStore = useChatsStore()
const msgBubbleContextmenuRef = ref()
const messageListRef = ref()

onMounted(() => {
  // 监控chat 滚动到底部
  watchEffect(() => {
    chatStore.currentChatIdGetter || chatStore.currentChatTypeGetter
    scrollToBottom()
  })

  // 当前会话修改，滚动到底部

  watch(() => {
    return chatStore.currentChatGetter.messages.length;
  }, () => {
    console.log("有新消息")
    scrollToBottom()
  })
})


onMounted(() => {
})
const onMessageClick = (e, msg) => {
  console.log("selected msg", msg)
  e.preventDefault()
  msgBubbleContextmenuRef.value.open(e, msg)
}

const onAvatarClick = (id) => {
  console.log(id)
}

// 消息 contextmenu 事件
const onCopyBtnClick = async (msg) => {
  await navigator.clipboard.writeText(msg.content)
  ShowToast({
    message: "内容复制成功",
    timeout: 1000,
    type: "success",
  })
  msgBubbleContextmenuRef.value.close()
}


const onRevokeBtnClick = (msg) => {
  let current = new Date().getTime()
  if (current - msg?.sendTime > 1000 * 60 * 3) {
    ShowToast({
      message: "消息已超过3分钟，无法撤回"
    })
  } else {
    chatStore.revokeMessage(msg?.id)
  }
  msgBubbleContextmenuRef.value.close()
}

const onReferBtnClick = (msg) => {
  chatStore.setReferMessage(chatStore.currentChatIdGetter, chatStore.currentChatTypeGetter, msg)
  console.log(chatStore.referMsgDataGetter)
  msgBubbleContextmenuRef.value.close()
}

const onDeleteBtnClick = (msg) => {
  let chatId = chatStore.currentChatIdGetter
  let chatType = chatStore.currentChatTypeGetter
  let msgId = msg?.id
  let fromUid = msg?.fromUid
  let toUid = msg?.toUid
  let sendTime = msg?.sendTime

  ShowMessageBox({
    message: "请确认是否要删除该条聊天消息？",
    confirm: () => {
      let idx = chatStore.deleteMessage(chatId, chatType, msgId)

      // 消息删除成功插入通知消息
      if (idx >= 0) {
        let notice = {
          content: "你删除了一条聊天消息",
          type: chatType,
          sendTime,
        }
        if (chatType === 1) {
          notice.toUid = toUid
          notice.fromUid = fromUid
        } else if (chatType === 2) {
          notice.roomId = chatId
          notice.fromUid = fromUid
        }
        let msg = buildNoticeMessage(notice)
        chatStore.insertMessage(chatId, chatType, msg, idx)
      }

      msgBubbleContextmenuRef.value.close()
    },
  })

}

const isInsertTimeMessage = (currentMsg, lastMsg) => {
  if (lastMsg == null) {
    return true
  }
  // 两条消息时间间隔超过5分钟，插入一条时间消息
  if (currentMsg.sendTime - lastMsg.sendTime >= 1000 * 60 * 5) {
    return true
  }
  return false
}

const scrollToBottom = () => {
  // 下帧进行渲染
  if (messageListRef.value) {
    requestAnimationFrame(() => {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    })
  } else {
    console.log("目标元素不存在")
  }
}

defineExpose({
  scrollToBottom
})
</script>

<template>
  <div ref="messageListRef" class="msg-list">
    <template v-for="(message, index) in messageList">
      <template v-if="isInsertTimeMessage(message, messageList[index - 1])">
        <div class="control-msg timeline">
          {{ getSendTimeNormalize(message.sendTime) }}
        </div>
      </template>
      <ChatBubble @avatar="onAvatarClick" @msg="onMessageClick" :msg="message"/>
    </template>

    <king-context-menu ref="msgBubbleContextmenuRef">
      <template #default="{context}">
        <king-context-menu-item name="复制" @click="onCopyBtnClick(context)" />
        <king-context-menu-item name="引用" @click="onReferBtnClick(context)" v-if="context?.fromUid !== userStore.info.id" />
        <king-context-menu-item name="撤回" @click="onRevokeBtnClick(context)" v-if="context?.fromUid === userStore.info.id && context?.sendTime" />
        <king-context-menu-item name="删除" @click="onDeleteBtnClick(context)" />
      </template>
    </king-context-menu>

  </div>
</template>

<style scoped lang="stylus">
.msg-list
  flex 1
  border 1px solid rgba(0, 0, 0, 0.1)
  overflow auto
  background-color #f0f0f0

  .timeline
    text-align center
    margin 50px 0
    color rgba(128, 128, 128, 0.8)
</style>