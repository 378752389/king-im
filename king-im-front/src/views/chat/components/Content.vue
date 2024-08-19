<script setup>
import {ref, onMounted, watchEffect, computed} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import {useUserStore} from "@/stores/user.js";
import {sendAudioMsgAPI, sendFileMsgAPI, sendPictureMsgAPI, sendVideoMsgAPI} from "@/http/message.js";
import {uploadAPI} from "@/http/user.js";
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import SendArea from "@/views/chat/components/SendArea.vue";

const chatStore = useChatsStore()
const userStore = useUserStore()
const contentRef = ref()
const pos = ref({
  x: 0,
  y: 0,
})
const showMsgBubbleContextMenuFlag = ref(false)
const msgBubbleContextmenuRef = ref()
const selectedMessage = ref()
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

  document.addEventListener('click', (e) => {
    if (e.target !== msgBubbleContextmenuRef.value) {
      showMsgBubbleContextMenuFlag.value = false
    }
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


// 消息 contextmenu 事件
const onCopyBtnClick = async () => {
  let msg = selectedMessage.value
  console.log(msg.content)
  await navigator.clipboard.writeText(msg.content)
}

const onRevokeBtnClick = () => {
  let msg = selectedMessage.value
  let type = msg.type
  let chatId = undefined
  if (type === 1) {
    // 单聊消息中， chatId 为对方的Uid，只需排除 fromUid 或 toUid 中非自身 id
    chatId = msg.fromUid === useUserStore().info?.id ? msg.toUid : msg.fromUid
  } else if (msg.type === 2) {
    chatId = msg.roomId
  }

  msg.status = 2
  msg.sendTime = new Date().getTime()
  console.log("msg", msg, selectedMessage.value)
  useChatsStore().revokeMessage(chatId, type, msg)

  selectedMessage.value = undefined
  showMsgBubbleContextMenuFlag.value = false
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

const onAvatarClick = (id) => {
  console.log(id)
}

const onMessageClick = (e, msg) => {
  e.preventDefault()
  pos.value = {
    x: e.x,
    y: e.y,
  }
  showMsgBubbleContextMenuFlag.value = true
  selectedMessage.value = msg
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
      <div ref="msgListRef" class="msg-list">
        <ChatBubble @avatar="onAvatarClick" @msg="onMessageClick" :msg="message" :key="message.id"
                    v-for="message in chatStore.currentChatGetter.messages"/>
      </div>
      <SendArea @send-msg="onSendMsg" @upload-file="onSendAreaUploadFile"/>
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

  .msg-list
    flex 1
    border 1px solid rgba(0, 0, 0, 0.1)
    overflow auto
    background-color #f0f0f0

    .msg-bubble
      display flex
      padding 20px 0 10px 30px
      align-items top

      .msg-avatar
        width 50px

      .msg-body
        .msg-nickname
          font-size 0.9em
          color rgba(0, 0, 0, 0.6)

        .msg-content
          white-space pre-wrap
          margin-top 10px
          width fit-content
          max-width 600px
          padding 10px
          font-size 1.2em
          background-color #3eaf7c

    .msg-control
      display flex
      justify-content center
      margin 10px 0

    .msg-bubble-self
      display flex
      padding 20px 30px 10px 0
      align-items top
      justify-content end

      .msg-body
        .msg-content
          white-space pre-wrap
          width fit-content
          max-width 600px
          padding 10px
          font-size 1.2em
          background-color #3eaf7c

      .msg-avatar
        text-align right
        width 50px

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