<script setup>
import ChatHistory from "@/views/chat/components/ChatHistory.vue";
import {onMounted, ref} from "vue";
import {getHistoryCursorPage} from "@/http/message.js";

// const chatList = ref([])
// onMounted(async () => {
//   const result = await getHistoryCursorPage({chatId: 5, chatType: 1, cursor: 500, size: 10})
//   console.log("分页请求结果", result)
//   chatList.value = result.data
//   console.log(chatList.value)
// })
</script>

<template>
<div class="test">
  <ChatHistory/>
</div>
</template>

<style scoped lang="stylus">
.test
  height 200px
  background-color #55a532
</style>