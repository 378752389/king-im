<script setup>
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import {onMounted, ref, watchEffect} from "vue";
import {useUserStore} from "@/stores/user.js";
import {useChatsStore} from "@/stores/chats.js";
import {getSendTimeNormalize} from "@/utils/dateUtils.js";

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
}


const onRevokeBtnClick = () => {

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
    <template v-for="(message, index) in chatStore.currentChatGetter.messages">
      <template v-if="isInsertTimeMessage(message, chatStore.currentChatGetter.messages[index - 1])">
        <div class="control-msg timeline">
          {{ getSendTimeNormalize(message.sendTime) }}
        </div>
      </template>
      <ChatBubble @avatar="onAvatarClick" @msg="onMessageClick" :msg="message"/>
    </template>


    <div ref="msgBubbleContextmenuRef" v-if="showMsgBubbleContextMenuFlag"
         :style="{width: '100px', backgroundColor: '#f6f6f6', position: 'fixed', left: pos.x + 'px', top: pos.y + 'px'}">
      <div class="menu-item"
           :style="{padding: '10px 0', textAlign: 'center', borderBottom: '1px solid rgba(0, 0, 0, 0.1)'}"
           @click="onCopyBtnClick"
      >复制
      </div>
      <div class="menu-item"
           :style="{padding: '10px 0', textAlign: 'center', borderBottom: '1px solid rgba(0, 0, 0, 0.1)'}"
           @click="onRevokeBtnClick" v-if="selectedMessage.fromUid === userStore.info.id">撤回
      </div>
      <div class="menu-item"
           :style="{padding: '10px 0', textAlign: 'center', borderBottom: '1px solid rgba(0, 0, 0, 0.1)'}">删除
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
</style>