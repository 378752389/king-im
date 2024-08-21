<script setup>

import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import {reactive, ref, watchEffect} from "vue";
import {getHistoryCursorPage} from "@/http/message.js";
import KingDialog from "@/components/common/KingDialog.vue";
import {useChatsStore} from "@/stores/chats.js";

const props = defineProps({
  chatId: {
    type: Number
  },
  chatType: {
    type: Number
  },
})

const chatHistoryRef = ref()
const chatHistoryDialogRef = ref()
const chatHistoryData = reactive({
  showLoading: false,
  loadingText: 'loading...',
  beforeScrollHeight: 0,

  chatId: 5,
  chatType: 1,
  cursor: undefined,
  requestSize: 20,

  chatHistoryList: [],
  isLast: false,
})

const onScroll = async (e) => {
  if (e.target.scrollTop === 0 && !chatHistoryData.showLoading) {
    chatHistoryData.beforeScrollHeight = chatHistoryRef.value.scrollHeight
    await requestChatHistory()
    chatHistoryRef.value.scrollTop = chatHistoryRef.value.scrollHeight - chatHistoryData.beforeScrollHeight;
  }
}

const requestChatHistory = async () => {
  chatHistoryData.showLoading = true

  if (chatHistoryData.isLast) {
    chatHistoryData.loadingText = "没有更多聊天数据了～～～"
    setTimeout(() => {
      chatHistoryData.showLoading = false
    }, 500)
    return
  }

  try {
    //
    console.log("请求新历史聊天数据")
    const result = await getHistoryCursorPage({
      chatId: props.chatId,
      chatType: props.chatType,
      cursor: chatHistoryData.cursor,
      size: chatHistoryData.requestSize,
    })

    if (result && result.size > 0) {
      console.log(result)
      chatHistoryData.chatHistoryList.unshift(...result.data)
      chatHistoryData.cursor = result.cursor
      chatHistoryData.isLast = result.isLast
    }
  } catch (e) {
    console.log(e)
  } finally {
    chatHistoryData.showLoading = false
  }
}


const open = async ()=> {
  try {
    await requestChatHistory()
    requestAnimationFrame(() => {
      chatHistoryRef.value.scrollTop = chatHistoryRef.value.scrollHeight
    })
  } catch (e) {
    console.error(e)
  } finally {
    chatHistoryDialogRef.value.open()
  }
}

const close = () => {
  chatHistoryDialogRef.value.close()
}

const chatStore = useChatsStore()

watchEffect(() => {
  props.chatId && props.chatType

  // 监听到历史消息变化时，清空历史聊天
  if (props.chatId === chatStore.currentChatIdGetter.value && props.chatType === chatStore.currentChatTypeGetter.value) {
    return
  }
  chatHistoryData.chatHistoryList = []
  console.log("清空历史聊天", props.chatId, props.chatType)
})

defineExpose({
  open,
  close,
})

</script>

<template>
  <div>
    <KingDialog title="聊天历史" class="chat-history-dialog" ref="chatHistoryDialogRef">
      <div @scroll="onScroll" class="chat-history" ref="chatHistoryRef">
        <div v-if="chatHistoryData.showLoading" style="text-align: center; padding: 20px;">
          {{ chatHistoryData.loadingText }}
        </div>
        <ChatBubble :key="msg.id" :msg="msg" v-for="msg in chatHistoryData.chatHistoryList"/>
      </div>
    </KingDialog>
  </div>
</template>

<style scoped lang="stylus">
.chat-history-dialog
  width 900px
.chat-history
  padding 0 10px
  height 400px
  overflow scroll
</style>