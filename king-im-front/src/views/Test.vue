<script setup>
import EmojiList from "@/components/EmojiList.vue";
import KingPopover from "@/components/common/KingPopover.vue";
import KingContextMenu from "@/components/common/KingContextMenu.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";
import {ref} from "vue";

const kingContextMenuRef = ref()
const onClick = (e) => {
  kingContextMenuRef.value.open(e)
}
</script>

<template>
  <div class="test">
    <button @click="onClick">click</button>
    <king-context-menu ref="kingContextMenuRef">
      <template #default="{context}">
        <king-context-menu-item name="加好友" @item-click="addFriendClick"  />
        <king-context-menu-item name="详情" @item-click="detailClick" />
        <king-context-menu-item name="发消息" @item-click="sendMessageClick" />
      </template>
    </king-context-menu>

<!--    <king-popover trigger="click" position="top">-->
<!--      <template #reference>-->
<!--        <button >click me</button>-->
<!--      </template>-->
<!--      <emoji-list/>-->
<!--    </king-popover>-->
  </div>
</template>

<style scoped lang="stylus">

.test
  background-color #f6f6f6
  overflow hidden

.king-popover
  margin 300px auto
  width fit-content
  button
    margin 15px 0
</style>

