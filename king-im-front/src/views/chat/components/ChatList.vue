<script setup>
import {ref} from 'vue';
import {useChatsStore} from "@/stores/chats.js";
import {getDateDiff} from "@/utils/dateUtils.js";
import KingContextMenu from "@/components/common/KingContextMenu.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";

const chatsStore = useChatsStore()
const searchText = ref('')


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
  alert("提醒用户," + context.chatName)
  close()
}

const onRemoveChatClick = (context, close) => {
  chatsStore.removeChat(context.chatId, context.type)
  close()
}
</script>

<template>
  <div class="contact">
    <div class="session-header">
      <div class="search">
        <input type="search" v-model="searchText" id="search-input"/>
      </div>
      <div class="add-group">
        <div id="add-group-btn">拉群聊</div>
      </div>
    </div>
    <div class="list">
      <div @click="onContactClick(chat)"
           @contextmenu="onContextmenuClick($event, chat)"
           :class="['contact-item', {'active': chatsStore.currentChatIdGetter === chat.chatId && chatsStore.currentChatTypeGetter === chat.type}]"
           :key="chat.chatId + ':' + chat.type"
           v-for="chat in chatsStore.chatsGetter">
        <div class="avatar-wrapper">
          <div class="unread-count" v-if="chat.unreadCount && chat.unreadCount > 0">{{ chat.unreadCount }}
          </div>
          <img class="avatar error" :src="chat.chatAvatar" alt="头像"/>
        </div>
        <div class="body">
          <div class="upper">
            <div class="title">{{ chat.chatName }}</div>
            <div class="last-time">{{ getDateDiff(chat.lastSendTime) }}</div>
          </div>
          <div class="lower">
            {{ chat.lastContent }}
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
  </div>
</template>

<style lang="stylus" scoped>
.contact
  flex 1
  max-width 300px
  font-size 14px

  .session-header
    height 60px
    display flex

    .search
      flex 1

      display flex
      justify-content center
      align-items center

      #search-input
        outline none
        height 40px
        width 90%
        font-size 1.5em

    .add-group
      width 15%
      display flex
      justify-content right
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

      .body
        flex 1
        padding 20px 10px
        height 100%

        .upper
          display flex
          justify-content space-between
          align-items end

          .title
            font-size 1.4em

        .lower
          width 160px
          margin-top 10px
          padding-right 5px
          font-size 0.9em
          overflow hidden
          white-space nowrap
          text-overflow ellipsis


</style>