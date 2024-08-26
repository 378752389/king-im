<script setup>
import {useUserStore} from "@/stores/user.js";
import {useGroupsStore} from "@/stores/groups.js";
import {computed} from "vue";
import {useChatsStore} from "@/stores/chats.js";

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

// const msg = reactive({
//   avatar: 'https://picsum.photos/512/512?id=1',
//   nickname: 'zabbix',
//   isSelf: false,
//   content: 'hello, world!',
//   contentType: 2,
//   pictureExtra: {
//     // url: 'https://picsum.photos/32/32?id=4',
//     url: 'https://picsum.photos/1920/1080?id=4',
//     // url: 'https://picsum.photos/512/512?id=4',
//   },
//   audioExtra: {
//     url: 'http://192.168.1.6/audios/one.wav',
//     // url: 'http://192.168.1.6/audios/one.mp3',
//   },
//   videoExtra: {
//     url: 'http://192.168.1.6/videos/three.mp4',
//     // url: 'http://localhost/videos/three.mp4',
//     // url: 'http://localhost/videos/two.mp4',
//     // url: 'http://localhost/videos/three.mp4',
//   },
//   fileExtra: {
//     url: 'http://192.168.1.6/videos/three.mp4',
//     // name: '我的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的备份.tar.gz',
//     // name: '我的的的的备份.tar.gz',
//     name: '我的的的的备份.tar.gz',
//   }
// })

const emits = defineEmits(['avatar', 'msg'])

const chatAvatar = computed(() => {
  if (props.msg.type === 1) {
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

</script>

<template>
  <div :class="['chat-bubble', isSelfMsg ? 'self': 'other']">
    <div @click="onAvatarClick" class="avatar">
      <img width="40" height="40" :src="chatAvatar" alt=""/>
    </div>
    <div>
      <div class="nickname" v-if="!isSelfMsg">{{ chatName }}</div>
      <div class="msg-wrapper" @contextmenu="onMsgClick">
        <div class="msg text-msg" v-if="msg.contentType === 1">
          <span v-if="msg.content">{{ msg.content }}</span>
        </div>
        <div class="msg picture-msg" v-else-if="msg.contentType === 2">
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
            <div>{{ msg.extra?.fileExtra?.name }}</div>
            <i class="iconfont icon-folder"></i>
          </div>
        </div>
        <div class="msg other-msg" v-else>
          <i class="iconfont icon-error"></i>无法识别消息类型
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="stylus">
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

.self
  flex-direction row-reverse

.other
  flex-direction row
</style>