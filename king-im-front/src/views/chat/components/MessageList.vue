<script setup>
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import {onMounted, ref} from "vue";
import {useUserStore} from "@/stores/user.js";
import {useChatsStore} from "@/stores/chats.js";

const userStore = useUserStore()
const chatStore = useChatsStore()
const showMsgBubbleContextMenuFlag = ref(false)
const msgBubbleContextmenuRef = ref()
const selectedMessage = ref()
const pos = ref({
  x: 0,
  y: 0,
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
  // let msg = selectedMessage.value
  // let type = msg.type
  // let chatId = undefined
  // if (type === 1) {
  //   // 单聊消息中， chatId 为对方的Uid，只需排除 fromUid 或 toUid 中非自身 id
  //   chatId = msg.fromUid === userStore.info?.id ? msg.toUid : msg.fromUid
  // } else if (msg.type === 2) {
  //   chatId = msg.roomId
  // }
  //
  // msg.status = 2
  // msg.sendTime = new Date().getTime()
  // console.log("msg", msg, selectedMessage.value)
  // useChatsStore().revokeMessage(chatId, type, msg)
  //
  // selectedMessage.value = undefined
  // showMsgBubbleContextMenuFlag.value = false
}
</script>

<template>
  <div ref="msgListRef" class="msg-list">
    <ChatBubble @avatar="onAvatarClick" @msg="onMessageClick" :msg="message"
                v-for="message in chatStore.currentChatGetter.messages"/>

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

</style>