<script setup>
import EmojiList from "@/components/EmojiList.vue";
import KingPopover from "@/components/common/KingPopover.vue";


</script>

<template>
  <div>
    <king-popover trigger="click" position="top">
      <template #reference>
        <button >click me</button>
      </template>
      <emoji-list/>
    </king-popover>


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

