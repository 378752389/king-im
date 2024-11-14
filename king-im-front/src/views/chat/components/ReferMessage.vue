<script setup>
import ChatBubble from "@/views/chat/components/ChatBubble.vue";

defineProps({
  referMsg: {
    type: Object,
  }
})
const emits = defineEmits(['close'])
const onCloseReferMsg = () => {
  emits('close')
}
</script>

<template>
  <div class="refer-message">
    <i class="iconfont icon-close" @click="onCloseReferMsg"></i>
    <div class="refer-wrapper">
      <chat-bubble :msg="referMsg"/>
    </div>
  </div>
</template>

<style scoped lang="stylus">
.refer-message
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