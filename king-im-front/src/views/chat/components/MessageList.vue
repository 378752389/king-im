<script setup>
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import {onMounted, ref, watchEffect} from "vue";
import {useUserStore} from "@/stores/user.js";
import {useChatsStore} from "@/stores/chats.js";
import {getSendTimeNormalize} from "@/utils/dateUtils.js";
import {ShowToast} from "@/components/common/func/toast.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";
import {buildNoticeMessage} from "@/utils/msgUtils.js";

const props = defineProps({
  messageList: {
    type: Array,
    default: []
  }
})

const userStore = useUserStore()
const chatStore = useChatsStore()
const showMsgBubbleContextMenuFlag = ref(false)
const msgBubbleContextmenuRef = ref()
const selectedMessage = ref()
const pos = ref({
  x: 0,
  y: 0,
})
const messageListRef = ref()

onMounted(() => {
  // 监控chat 滚动到底部
  watchEffect(() => {
    chatStore.currentChatIdGetter || chatStore.currentChatTypeGetter
    scrollToBottom()
  })
})


onMounted(() => {
  document.addEventListener('click', (e) => {
    if (e.target !== msgBubbleContextmenuRef.value) {
      showMsgBubbleContextMenuFlag.value = false
    }
  })
})
const onMessageClick = (e, msg) => {
  console.log("selected msg", msg)
  e.preventDefault()
  pos.value = {
    x: e.x,
    y: e.y,
  }
  showMsgBubbleContextMenuFlag.value = true
  selectedMessage.value = msg
}

const onAvatarClick = (id) => {
  console.log(id)
}

// 消息 contextmenu 事件
const onCopyBtnClick = async () => {
  let msg = selectedMessage.value
  await navigator.clipboard.writeText(msg.content)
  ShowToast({
    message: "内容复制成功",
    timeout: 1000,
    type: "success",
  })
}


const onRevokeBtnClick = () => {
  let current = new Date().getTime()
  if (current - selectedMessage.value?.sendTime > 1000 * 60 * 3) {
    ShowToast({
      message: "消息已超过3分钟，无法撤回"
    })
  } else {
    chatStore.revokeMessage(selectedMessage.value?.id)
    // ShowToast({
    //   message: "撤销消息功能暂时还未实现"
    // })
  }
}

const onReferBtnClick = () => {
  ShowToast({
    message: "引用消息功能暂时还未实现"
  })
}

const onDeleteBtnClick = () => {
  let chatId = chatStore.currentChatIdGetter
  let chatType = chatStore.currentChatTypeGetter
  let msgId = selectedMessage.value?.id
  let fromUid = selectedMessage.value?.fromUid
  let toUid = selectedMessage.value?.toUid
  let sendTime = selectedMessage.value?.sendTime

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


    <div ref="msgBubbleContextmenuRef" class="menu" v-if="showMsgBubbleContextMenuFlag"
         :style="{left: pos.x + 'px', top: pos.y + 'px'}">
      <div class="menu-item pointer-select"
           @click="onCopyBtnClick"
      >复制
      </div>
      <div class="menu-item pointer-select"
           @click="onReferBtnClick" v-if="selectedMessage.fromUid !== userStore.info.id">引用
      </div>
      <div class="menu-item pointer-select"
           @click="onRevokeBtnClick" v-if="selectedMessage.fromUid === userStore.info.id && selectedMessage.sendTime">撤回
      </div>
      <div class="menu-item pointer-select" @click="onDeleteBtnClick">删除
      </div>
    </div>
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

.menu
  width 100px
  background-color #f6f6f6
  position fixed
  z-index 999
  .menu-item
    padding 10px 3px
    text-align center
    border-bottom 1px solid rgba(0, 0, 0, 0.1)
</style>