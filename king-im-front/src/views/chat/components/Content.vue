<script setup>
import {ref, onMounted, watchEffect} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import {sendAudioMsgAPI, sendFileMsgAPI, sendPictureMsgAPI, sendVideoMsgAPI} from "@/http/message.js";
import {uploadAPI} from "@/http/user.js";
import SendArea from "@/views/chat/components/SendArea.vue";
import MessageList from "@/views/chat/components/MessageList.vue";
import {ossUploadAPI} from "@/http/oss.js";
import {ShowToast} from "@/components/common/func/toast.js";

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
})


const sendFileConfirmDialogRef = ref()

// 拖拽上传确认逻辑
const onSendFileConfirm = async () => {
  const downloadUrl = await ossUploadAPI({
    file: uploadFile.value,
    businessType: 3,
  })

  let chatId = chatStore.currentChatIdGetter
  let chatType = chatStore.currentChatTypeGetter
  let referMsgId = null
  let atUids = null

  let fileType = uploadFile.value.type
  let msg = null
  let baseData = {msgType: 5, text: '', referMsgId, atUids, chatId, chatType};
  if (fileType.startsWith('image')) {
    baseData.msgType = 2
    msg = await sendPictureMsgAPI(baseData,
        {
          size: uploadFile.value?.size,
          url: downloadUrl,
          name: uploadFile.value?.name,
          type: uploadFile.value?.type,
        })
  } else if (fileType.startsWith('audio')) {
    baseData.msgType = 3
    msg = await sendAudioMsgAPI(baseData, {
      size: uploadFile.value?.size,
      second: 0,
      type: uploadFile.value?.type,
      url: downloadUrl
    })
  } else if (fileType.startsWith('video')) {
    baseData.msgType = 4
    msg = await sendVideoMsgAPI(baseData, {
      size: uploadFile.value?.size,
      coverUrl: '',
      url: downloadUrl,
      type: uploadFile.value?.type
    })
  } else {
    baseData.msgType = 5
    msg = await sendFileMsgAPI(baseData, {
      size: uploadFile.value?.size,
      filename: uploadFile.value?.name,
      url: downloadUrl,
      type: uploadFile.value?.type
    })
  }

  chatStore.insertMessage(chatId, chatType, msg)
  msgListRef.value.scrollToBottom()
  sendFileConfirmDialogRef.value.close()
}

const onSendFileCancel = () => {
  sendFileConfirmDialogRef.value.close()
}

// 消息发送事件监听
const onSendMsg = () => {
  // 当有消息发送时滚动消息列表
  msgListRef.value.scrollToBottom()
}

const onSendAreaUploadFile = async (file) => {
  uploadFile.value = file
  sendFileConfirmDialogRef.value.showModal()
}

const onMoreClick = () => {
  ShowToast({
    message: "更多功能还未实现，敬请期待"
  })
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
        <div class="other pointer-select" @click="onMoreClick">...</div>
      </div>
      <message-list ref="msgListRef" :message-list="chatStore.currentChatGetter.messages" />
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
        margin 10px

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