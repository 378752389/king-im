<script setup>
import {computed, ref} from 'vue';
import {useChatsStore} from "@/stores/chats.js";
import {getDateDiff} from "@/utils/dateUtils.js";
import KingContextMenu from "@/components/common/KingContextMenu.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";
import {ShowToast} from "@/components/common/func/toast.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";
import emojiUtils from "@/utils/emojiUtils.js";
import KingDialog from "@/components/common/KingDialog.vue";
import InviteMember from "@/views/group/components/InviteMember.vue";
import {useGroupsStore} from "@/stores/groups.js";

const chatsStore = useChatsStore()
const searchText = ref('')

const filterableChatStore = computed(() => {
  return chatsStore.chatsGetter.filter(chat => chat?.chatName.includes(searchText.value))
})

const onContactClick = (chat) => {
  // todo 通知渲染content
  // 清空未读标志
  chatsStore.setChat(chat.chatId, chat.type)
}

const contextmenuRef = ref()
const onContextmenuClick = (e, chat) => {
  console.log(contextmenuRef.value)
  contextmenuRef.value.open(e, chat)
}


const onMoveTopChatClick = (context, close) => {
  chatsStore.moveChatTop(context.chatId, context.type)
  close()
}

const onSharkClick = (context, close) => {
  ShowToast({
    message: "抖动通知功能暂未实现，敬请期待"
  })
  close()
}

const onRemoveChatClick = (context, close) => {
  ShowMessageBox({
    message: "请确认是否要删除聊天? 删除聊天会 <span style='color: red;'>清理本地聊天记录</span>!",
    confirm: () => {
      chatsStore.removeChat(context.chatId, context.type)
      close()

      ShowToast({
        message: "删除聊天成功！",
        type: 'success'
      })
    }
  })

}


const createGroupList = ref([])
const createGroupDialogRef = ref()
const inviteMemberRef = ref()
const onCreateGroupClick = () => {
  createGroupDialogRef.value.open()
  // ShowToast({
  //   message: "创建群聊功能还在开发中。。。"
  // })
}

const onCreateGroupConfirm = () => {
  createGroupList.value = []
  // todo
  console.log(inviteMemberRef.value.selectedList)
  let members =  inviteMemberRef.value.selectedList
  useGroupsStore().createGroup(members)
  inviteMemberRef.value.clearSelectedList()
  createGroupDialogRef.value.close()
}
const onCreateGroupCancel = () => {
  createGroupList.value = []
  inviteMemberRef.value.clearSelectedList()
  createGroupDialogRef.value.close()
}
</script>

<template>
  <div class="contact">
    <div class="session-header">
      <div class="search-area">
        <input type="search" v-model="searchText" id="search-input"/>
      </div>
      <div class="add-group">
        <i class="iconfont" @click="onCreateGroupClick" style="font-size: 30px">&#xea0b;</i>
<!--        <div id="add-group-btn">拉群聊</div>-->
      </div>
    </div>
    <div class="list">
      <div @click="onContactClick(chat)"
           @contextmenu="onContextmenuClick($event, chat)"
           :class="['contact-item', {'active': chatsStore.currentChatIdGetter === chat.chatId && chatsStore.currentChatTypeGetter === chat.type}]"
           :key="chat.chatId + ':' + chat.type"
           v-for="chat in filterableChatStore">
        <div class="avatar-wrapper">
          <div class="unread-count" v-if="chat.unreadCount && chat.unreadCount > 0">{{ chat.unreadCount }}
          </div>
          <img class="avatar error" :src="chat.chatAvatar" alt="头像"/>
        </div>
        <div class="body-wrapper">
          <div class="upper">
            <div class="title">{{ chat.chatName }}</div>
            <div class="last-time">{{ getDateDiff(chat.lastSendTime || chat.createTime) }}</div>
          </div>
          <div class="lower" v-html="emojiUtils.transform(chat.lastContent)">
          </div>
        </div>
      </div>
    </div>

    <KingContextMenu ref="contextmenuRef">
      <template #default="{context, close}">
        <KingContextMenuItem name="置顶" @itemClick="onMoveTopChatClick(context, close)"/>
        <KingContextMenuItem name="抖动通知" @itemClick="onSharkClick(context, close)"/>
        <KingContextMenuItem name="删除聊天" @itemClick="onRemoveChatClick(context, close)"/>
      </template>
    </KingContextMenu>

    <king-dialog @confirm="onCreateGroupConfirm" @cancel="onCreateGroupCancel" ref="createGroupDialogRef">
      <InviteMember ref="inviteMemberRef" :current-member-list="createGroupList" />
    </king-dialog>
  </div>
</template>

<style lang="stylus" scoped>
.contact
  flex 1
  max-width 330px
  font-size 14px

  .session-header
    height 60px
    display flex

    .search-area
      flex 1

      display flex
      justify-content center
      align-items center

      #search-input
        outline none
        height 40px
        font-size 1.5em
        width 280px

    .add-group
      display flex
      justify-content center
      align-items center

      #add-group-btn
        display flex
        align-items center
        justify-content center
        font-size 0.7rem
        width 40px
        height 40px
        background #cccccc

  .list
    padding 0 2px
    height calc(100% - 60px)
    overflow auto

    .active
      background #cccccc

    .contact-item
      height 100px
      border 1px solid rgba(0, 0, 0, 0.3)
      display flex
      align-items center

      &:not(.active):hover
        background-color rgba(0, 0, 0, 0.1)

      .avatar-wrapper
        position relative

        .avatar
          width 80px
          height 80px
          margin 0 10px

        .unread-count
          right 8px
          border-radius 30%
          position absolute
          height 20px
          width 20px
          background-color red
          color white
          text-align center

      .body-wrapper
        flex 1
        padding 20px 10px
        height 100%

        .upper
          display flex
          justify-content space-between
          align-items end

          .title
            max-width 300px
            font-size 1.4em
            overflow hidden
            white-space nowrap
            text-overflow ellipsis

          .last-time
            font-size 0.9em

        .lower
          width 160px
          margin-top 10px
          padding-right 5px
          font-size 0.9em
          overflow hidden
          white-space nowrap
          text-overflow ellipsis


</style>