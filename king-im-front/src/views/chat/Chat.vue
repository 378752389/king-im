<script setup>
import Content from "@/views/chat/components/Content.vue";
import ChatList from "@/views/chat/components/ChatList.vue";
</script>


<template>
  <div class="chat">
    <ChatList/>
    <Content/>
  </div>
</template>

<style lang="stylus" scoped>
.chat
  background-color #f0f0f0
  display flex
  height 100%
</style>
