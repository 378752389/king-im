<script setup>
import {useUserStore} from "@/stores/user.js";
import {useGroupsStore} from "@/stores/groups.js";
import {computed} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import emojiUtils from "@/utils/emojiUtils.js";
import {ShowToast} from "@/components/common/func/toast.js";

const userStore = useUserStore()
const groupStore = useGroupsStore()
const chatStore = useChatsStore()

const props = defineProps({
  msg: {
    avatar: 'https://picsum.photos/512/512?id=1',
    nickname: 'zabbix',
    content: '',
    contentType: 1,
    extra: {
      pictureExtra: null,
      audioExtra: null,
      videoExtra: null,
      fileExtra: null
    }
  }
})

const emits = defineEmits(['avatar', 'msg'])

const chatAvatar = computed(() => {
  if (props.msg.type === 1) {
    if (props.msg.fromUid === userStore.info?.id) {
      return userStore.info?.avatar
    }
    return chatStore.currentChatGetter.chatAvatar;
  } else if (props.msg.type === 2) {
    if (props.msg.fromUid === userStore.info?.id) {
      return userStore.info?.avatar
    }
    const group = groupStore.getGroup(props.msg.roomId)
    if (group == null) {
      console.log("未找到群: " + props.msg.roomId)
      return null;
    }
    return group.roomMemberList.find(member => member.id === props.msg.fromUid)?.avatar;
  }
  return '';
})

const chatName = computed(() => {
  if (props.msg.type === 1) {
    return chatStore.currentChatGetter.chatName
  } else if (props.msg.type === 2) {
    const group = groupStore.getGroup(props.msg.roomId)
    if (group == null) {
      console.log("未找到群: " + props.msg.roomId)
      return null;
    }
    return group.roomMemberList.find(member => member.id === props.msg.fromUid)?.name;
  } else {
    return '未知';
  }
})

const isSelfMsg = computed(() => {
  return props.msg.fromUid === userStore.info?.id
})

const onAvatarClick = () => {
  emits('avatar', props.msg.fromUid)
}

const onMsgClick = (e) => {
  emits('msg', e, props.msg)
}

const onFileMsgClick = async () => {
  const a = document.createElement('a')
  a.href = props.msg.extra.fileExtra.url
  a.download = props.msg.extra.fileExtra.name
  a.click()
}

const onPictureClick = () => {
  ShowToast({
    message: "点击图片, 放大功能暂时未实现...",
  })
}

</script>

<template>
  <template v-if="msg.contentType === 999">
    <div class="notice-msg">
      {{ msg.content}}
    </div>
  </template>
  <div v-else :class="['chat-bubble', isSelfMsg ? 'self': 'other']">
    <div @click="onAvatarClick" class="avatar">
      <img width="40" height="40" :src="chatAvatar" alt=""/>
    </div>
    <div>
      <div class="nickname" v-if="!isSelfMsg">{{ chatName }}</div>
      <div class="msg-wrapper" @contextmenu="onMsgClick">
        <div class="msg text-msg" v-if="msg.contentType === 1">
          <span v-if="msg.content" v-html="emojiUtils.transform(msg.content)"></span>
        </div>
        <div @click="onPictureClick" class="msg picture-msg" v-else-if="msg.contentType === 2">
          <img v-if="msg.extra?.pictureExtra?.url" :src="msg.extra?.pictureExtra?.url"
               onerror="this.onerror=null; this.src='/default.png'; this.width=100;" alt="图片无法显示"/>
          <img v-else width='100' src="/default.png" alt=""/>
        </div>
        <div class="msg audio-msg" v-else-if="msg.contentType === 3">
          <audio v-if='msg.extra?.audioExtra?.url' :src="msg.extra?.audioExtra?.url" controls/>
          <i v-else class="iconfont icon-picture"/>
        </div>
        <div class="msg video-msg" v-else-if="msg.contentType === 4">
          <video v-if='msg.extra?.videoExtra?.url' :src="msg.extra?.videoExtra?.url" controls/>
          <i v-else class="iconfont icon-picture"/>
          <!--        <video :src="msg.extra.videoExtra.url" controls :poster="msg.extra.pictureExtra.url" />-->
        </div>
        <div class="msg file-msg" v-else-if="msg.contentType === 5">
          <div class="unknown-file pointer-select" @click="onFileMsgClick">
            <div>{{ msg.extra?.fileExtra?.filename }}</div>
            <i class="iconfont icon-folder"></i>
          </div>
        </div>
        <div class="msg other-msg" v-else>
          <i class="iconfont icon-error"></i>无法识别消息类型
        </div>
      </div>
    </div>
<!--    todo 等待发送方发送结果通知-->
<!--    <div class="msg-loading" v-if="msg.status === 1"><i class="iconfont icon-loading" /></div>-->
  </div>
</template>

<style scoped lang="stylus">

.notice-msg
  width 500px
  display flex
  //white-space pre-wrap
  justify-content center
  flex-wrap wrap
  margin 50px auto
  color rgba(128, 128, 128, 0.8)

.chat-bubble
  margin 25px 0
  display flex
  align-items flex-start

  .avatar
    margin 0 10px

  .nickname
    margin-bottom 5px

  .msg-wrapper
    i
      font-size 40px

    .text-msg
      max-width 500px
      line-height 1.3
      letter-spacing 1.3px
      padding 10px
      background-color v-bind("isSelfMsg ? '#55a532' : '#ffffffff'")
      white-space pre-wrap

    .picture-msg
      display flex
      border 1px solid rgba(0, 0, 0, 0.2)
      width fit-content
      padding 5px

      img
        min-width 80px
        max-height 300px

    .video-msg
      display flex

      video
        max-height 300px

    .file-msg
      .unknown-file
        width 200px
        display flex
        align-items center
        justify-content space-between
        font-size 12px
        border 1px solid rgba(0, 0, 0, 0.1)
        padding 20px
        background-color white

        &:hover
          background-color rgba(0, 0, 0, 0.1)

    .other-msg
      color red
      margin-top 10px

  .msg-loading
    margin 0 5px
    align-self center
.self
  flex-direction row-reverse

.other
  flex-direction row
</style>