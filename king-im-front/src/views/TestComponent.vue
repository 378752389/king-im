<script setup>
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import {useChatsStore} from "@/stores/chats.js";

const onCloseReferMsg = () => {

}
</script>


<template>
<div class="test-comp">
  <i class="iconfont icon-close" @click="onCloseReferMsg"></i>
  <div class="refer-wrapper">
    <chat-bubble :msg="useChatsStore().chatsGetter[3].messages[6]" />
  </div>
</div>
</template>

<style scoped lang="stylus">
.test-comp
  display flex
  .refer-wrapper
    margin 5px
    border 1px rgba(128, 128, 128, 0.3) solid
    width 80%
    opacity 0.4
    .chat-bubble
      margin 5px
  .icon-close
    margin-top 5px
    font-size 30px
</style>