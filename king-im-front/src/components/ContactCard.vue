<script setup>
import {nextTick, ref, watchEffect} from "vue";
import {useContactsStore} from "@/stores/contacts.js";
import {useChatsStore} from "@/stores/chats.js";
import {useRouter} from "vue-router";
import {updateContactAPI} from "@/http/social.js";

const contactStore = useContactsStore()

const props = defineProps({
  contactInfo: {}
})
const router = useRouter()
const chatStore = useChatsStore()
const isRemarkEdit = ref(false)
const markName = ref()
const markNameRef = ref()
const onRemarkClick = () => {
  isRemarkEdit.value = true
  markName.value = props.contactInfo.peerMarkName
  nextTick(() => {
    markNameRef.value.focus()
  })
}

const onRemarkEditConfirm = async (e) => {
  console.log(111)
  isRemarkEdit.value = false
  const resp = await updateContactAPI({
    peerId: props.contactInfo.peerId,
    peerMarkName: markName.value
  })
  await useContactsStore().loadContactList()
  alert("修改好友备注成功")
}

watchEffect(() => {
  props.contactInfo && true
  isRemarkEdit.value = false
  markName.value = ''
})

const onMarkNameBlur = () => {
  isRemarkEdit.value = false
}


const onSendMessageClick = () => {
  const chatId = props.contactInfo.peerId
  chatStore.insertMessage(chatId, 1, null)
  chatStore.setChat(chatId, 1)
  router.push({name: 'chat'})
}

const onSendVoiceCallClick = () => {

}

const onSendVideoCallClick = () => {

}


</script>

<template>
  <div class="contact-detail">
    <div class="base">
      <img class="avatar" :src="contactInfo?.peerAvatar" alt=""/>
      <div class="profile">
        <div class="name">
          {{ !!contactInfo.peerMarkName ? contactInfo.peerMarkName : contactInfo.peerNickname }}
          <i
              :class="['iconfont', 'icon-customer' ]"
              :style="{color: contactInfo.gender === 1 ? 'blue' :(contactInfo.gender === 2 ? 'red': 'grey'), verticalAlign: '0.1rem'}"></i>
        </div>
        <div v-if="!!contactInfo.peerMarkName" class="nickname">昵称：{{ contactInfo.peerNickname }}</div>
        <div class="address">地区：{{ contactInfo.province || '未知' + ' ' }} {{ contactInfo.city || '未知' }}</div>
      </div>
    </div>
    <div class="markName">
      备注： <span v-if="!isRemarkEdit"
                  @click.prevent="onRemarkClick">{{
        !!contactInfo.peerMarkName ? contactInfo.peerMarkName : '添加备注'
      }}</span>
      <span v-else><input ref="markNameRef"  v-model="markName"/>
        <button @click="onRemarkEditConfirm($event)">确定</button>
        <button @click="isRemarkEdit = false">取消</button>
      </span>
    </div>

    <div class="sign">
      签名：{{ contactInfo.sign || '这个人很懒，什么都没留下~~~' }}
    </div>

    <div class="action">
      <template v-if="contactStore.isFriend(contactInfo.peerId)">
        <div @click="onSendMessageClick" class="action-item pointer-select">发消息</div>
        <div @click="onSendVoiceCallClick" class="action-item pointer-select">语音聊天</div>
        <div @click="onSendVideoCallClick" class="action-item pointer-select">视频聊天</div>
      </template>
      <template v-else>
        <div class="action-item pointer-select">加好友</div>
      </template>
    </div>

  </div>
</template>

<style scoped lang="stylus">
.contact-detail
  margin auto
  margin-top 100px
  width 500px
  padding 20px
  border 1px solid rgba(0, 0, 0, 0.1)
  box-shadow 3px 3px 3px rgba(0, 0, 0, 0.1)

  .base
    display flex

    .avatar
      width 60px
      height 60px
      margin-right 20px

    .profile
      height 70px
      font-size 0.9rem

      & > div
        margin-bottom 5px

      .name
        font-size 1.1rem

  .markName
    height 60px
    margin 20px 0
    padding 20px 0
    border-top 1px solid rgba(0, 0, 0, 0.3)
    border-bottom 1px solid rgba(0, 0, 0, 0.3)

  .sign
    padding 0 0 20px
    border-bottom 1px solid rgba(0, 0, 0, 0.3)

  .action
    display flex
    padding 10px 0
    justify-content space-around

    .action-item
      padding 20px 30px
      display flex
      justify-content center
      align-items center

      &:hover
        cursor default
        background-color rgba(0, 0, 0, 0.1)

</style>