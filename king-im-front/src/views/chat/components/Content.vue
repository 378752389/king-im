<script setup>
import {ref, onMounted, watchEffect} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import {sendAudioMsgAPI, sendFileMsgAPI, sendPictureMsgAPI, sendVideoMsgAPI} from "@/http/message.js";
import {uploadAPI} from "@/http/user.js";
import SendArea from "@/views/chat/components/SendArea.vue";
import MessageList from "@/views/chat/components/MessageList.vue";

const chatStore = useChatsStore()

const contentRef = ref()
const msgListRef = ref()
const uploadFile = ref()

onMounted(() => {
  // 拖动API
  contentRef.value.addEventListener("dragover", (e) => {
    e.preventDefault()
    contentRef.value.style.backgroundColor = "gray"
  })

  contentRef.value.addEventListener("dragleave", (e) => {
    e.preventDefault()

    contentRef.value.style.backgroundColor = "transparent"
  })

  contentRef.value.addEventListener("drop", (e) => {
    e.preventDefault()
    contentRef.value.style.backgroundColor = "transparent"
    if (e.dataTransfer.files.length > 0) {
      uploadFile.value = e.dataTransfer.files[0]
      sendFileConfirmDialogRef.value.showModal()
    }
    console.log(e.dataTransfer.files[0])
  })



  // 监控chat 滚动到底部
  watchEffect(() => {
    chatStore.currentChatIdGetter || chatStore.currentChatTypeGetter
    scrollToBottom()
  })

})

const scrollToBottom = () => {
  // 下帧进行渲染
  if (msgListRef.value) {
    requestAnimationFrame(() => {
      msgListRef.value.scrollTop = msgListRef.value.scrollHeight
    })
  } else {
    console.log("目标元素不存在")
  }
}


const sendFileConfirmDialogRef = ref()

// 拖拽上传确认逻辑
const onSendFileConfirm = async () => {
  const uploadResult = await uploadAPI(uploadFile.value)
  if (!uploadResult) {
    console.error("上传失败")
  }

  let chatId = chatStore.currentChatIdGetter
  let chatType = chatStore.currentChatTypeGetter
  let referMsgId = null
  let atUids = null

  let fileType = uploadResult?.type
  let msg = null
  let baseData = {msgType: 5, text: '', referMsgId, atUids, chatId, chatType};
  if (fileType.startsWith('image')) {
    baseData.msgType = 2
    msg = await sendPictureMsgAPI(baseData,
        {
          size: uploadResult?.size,
          url: uploadResult?.url,
          name: uploadResult?.name,
          type: uploadResult?.type,
        })
  } else if (fileType.startsWith('audio')) {
    baseData.msgType = 3
    msg = await sendAudioMsgAPI(baseData, {
      size: uploadResult?.size,
      second: 0,
      type: uploadResult?.type,
      url: uploadResult?.url
    })
  } else if (fileType.startsWith('video')) {
    baseData.msgType = 4
    msg = await sendVideoMsgAPI(baseData, {
      size: uploadResult?.size,
      coverUrl: '',
      url: uploadResult?.url,
      type: uploadResult?.type
    })
  } else {
    baseData.msgType = 5
    msg = await sendFileMsgAPI(baseData, {
      size: uploadResult?.size,
      filename: uploadResult?.name,
      url: uploadResult?.url,
      type: uploadResult?.type
    })
  }

  chatStore.insertMessage(chatId, chatType, msg)
  scrollToBottom()
  sendFileConfirmDialogRef.value.close()
}

const onSendFileCancel = () => {
  sendFileConfirmDialogRef.value.close()
}

// 消息发送事件监听
const onSendMsg = () => {
  // 当有消息发送时滚动消息列表
  scrollToBottom()
}

defineExpose({
  scrollToBottom
})

const onSendAreaUploadFile = async (file) => {
  uploadFile.value = file
  sendFileConfirmDialogRef.value.showModal()
}

</script>

<template>
  <div ref="contentRef" class="content">
    <template v-if="chatStore.currentChatGetter">
      <div class="header">
        <div class="title">{{
            chatStore.currentChatGetter.chatName
          }}
        </div>
        <div class="other">...</div>
      </div>
      <message-list />
      <SendArea @send-msg="onSendMsg" @upload-file="onSendAreaUploadFile"/>
    </template>


    <dialog ref="sendFileConfirmDialogRef"
            style="position: fixed; margin: 200px auto auto; height: 100px; padding: 10px;">
      <form method="dialog"
            style="height: 100%; display: flex; flex-direction: column; justify-content: space-between;">
        <p>请确认是否发送文件：{{ uploadFile?.name || '' }}</p>
        <div style="align-self: flex-end; margin: 0 5px 5px 0;">
          <input class="btn" type="submit" @click="onSendFileConfirm" value="确认"/>
          <input class="btn" type="submit" @click="onSendFileCancel" value="取消"/>
        </div>
      </form>
    </dialog>
  </div>
</template>

<style lang="stylus" scoped>
.content
  flex 2
  display flex
  flex-direction column

  .header
    padding 30px 50px
    display flex
    justify-content space-between
    align-items center


    .title
      font-size 1.5em

    .other
      font-size 1.2em
  .send-area
    position relative

    .tools
      padding 20px 10px
      display flex

      .tool-item
        margin 0 10px

    .text-area
      margin 10px 0
      background-color rgba(255, 255, 255, 0)
      width 99%
      height 200px
      outline none
      border none
      line-height 1.5
      font-size 1.5em
      padding 0 20px
      resize none

      &:focus-within
        outline 2px solid black

    //.send
    //  display flex
    //  justify-content end
    //

    #send-btn
      position absolute
      bottom 20px
      right 10px
      width 100px
      padding 5px
      margin-right 10px
</style>