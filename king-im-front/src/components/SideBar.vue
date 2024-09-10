<script setup>
import {useUserStore} from "@/stores/user.js";
import {useChatsStore} from "@/stores/chats.js";
import KingDialog from "@/components/common/KingDialog.vue";
import {onMounted, ref} from "vue";
import RelationList from "@/components/RelationList.vue";
import {buildNoticeMessage} from '@/utils/msgUtils.js';
const userStore = useUserStore()
const onFileClick = () => {
  useChatsStore().removeChat(22, 2)
  // useChatsStore().openChat(22, 2, useGroupsStore().groupList[1])
}

const onLiftClick = () => {
  let msg = buildNoticeMessage({type: 2, fromUid: 1, roomId: 1, content: "king创建了群聊"})
  useChatsStore().insertMessage(1, 2, msg)
  // useChatsStore().insertMessage(5, 2, {
  //   id: 5521,
  //   roomId: 20,
  //   fromUid: 5,
  //   toUid: undefined,
  //   type: 2,
  //   content: '你好',
  //   name: 'Allen',
  //   atUids: undefined,
  //   sendTime: 1713779058910,
  //   status: 2,
  //   avatar: 'https://picsum.photos/512/512?id=5',
  // },)
}

const dialogRef = ref()
onMounted(async () => {
  // dialogRef.value.open()
})
const addContactOrJoinGroup = () => {
  dialogRef.value.open()
}

const avatarRef = ref()
const detailRef = ref()
onMounted(() => {
  avatarRef.value.addEventListener("mouseenter", () => {
    detailRef.value.style.display = 'block'
  })

  avatarRef.value.addEventListener("mouseleave", () => {
    detailRef.value.style.display = 'none'
  })
})

</script>

<template>
  <div class="sidebar">
    <div class="avatar" ref="avatarRef">
      <img height="80" width="80" :src="userStore.info?.avatar" alt="avatar" onerror="this.classList.add('error')"/>

      <div class="detail" ref="detailRef">
        <ul>
          <li>id: {{userStore.info?.id}}</li>
          <li>nickname: {{userStore.info?.nickName}}</li>
          <li>city: {{userStore.info?.city}}</li>
          <li>ip: {{userStore.info?.ip}}</li>
          <!--        <li>loginTime: {{userStore.info?.loginTime}}</li>-->
          <li>status: {{userStore.info?.isOnline ? '在线': '离线'}}</li>
        </ul>
      </div>
    </div>

    <div class="list">
      <div class="item">
        <router-link to="/chat"><i class="iconfont icon-message-comments"></i></router-link>
      </div>
      <div class="item">
        <router-link to="/contact"><i class="iconfont icon-customer"></i></router-link>
      </div>
      <div class="item">
        <router-link to="/group"><i class="iconfont icon-customer-group"></i></router-link>
      </div>
      <div class="item">
        <i class="iconfont icon-add" @click="addContactOrJoinGroup"></i>
<!--        <i class="iconfont icon-add" @click="onLiftClick"></i>-->
      </div>
      <div class="item">
        <router-link to="/profile"><i class="iconfont icon-edit"></i></router-link>
      </div>
<!--      <div class="item">-->
<!--        <router-link to="/file"><i class="iconfont icon-folder"></i></router-link>-->
<!--      </div>-->
      <!--      <div class="item">-->
      <!--        <router-link to="/qwe" @click="onLiftClick"><i class="iconfont icon-link"></i></router-link>-->
      <!--      </div>-->
      <div class="item last">
        <router-link to="/login"><i class="iconfont icon-shut-down"></i></router-link>
      </div>
    </div>

    <king-dialog ref="dialogRef">
      <relation-list/>
    </king-dialog>
  </div>
</template>

<style scoped lang="stylus">
.sidebar
  display flex
  flex-direction column
  align-items center
  width 5%
  min-width 90px
  border-right 1px solid black
  background-color #3a3838

  .avatar
    margin 0 20px
    border 1px solid rgba(128, 128, 128, 0.3)
    position relative
    .detail
      padding 10px
      display none
      width 200px
      position absolute
      z-index 999
      top 0
      left 100%
      background-color white
      li
        margin 3px
        list-style none

  .list
    flex 1
    display flex
    flex-direction column
    justify-content flex-start
    align-items center

    .item
      width 50px
      height 50px
      margin 10px 0

      .router-link-exact-active,
      .router-link-active i
        color: #55a532;

      i
        font-size 3rem
        color rgba(255, 255, 255, .6)

    .last
      margin-top auto
</style>