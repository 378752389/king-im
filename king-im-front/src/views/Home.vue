<template>
  <div class="home">
    <SideBar/>
    <div class="main">
      <RouterView/>
    </div>

  </div>
</template>

<style lang="stylus" scoped>


.home
  width 100vw
  height 100vh
  min-width 1200px
  display flex
  overflow hidden

  .main
    flex 1
</style>
<script setup>
import {onMounted, onUnmounted} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import {doConnect, doDisconnect, setBusinessCallback, setLoginSuccessCallback} from "@/ws/ws.js";
import {CMD} from "@/ws/cmd.js";
import {useUserStore} from "@/stores/user.js";
import SideBar from "@/components/SideBar.vue";
import {useGroupsStore} from "@/stores/groups.js";
import {useContactsStore} from "@/stores/contacts.js";


onMounted(async () => {
  // 连接之前请求数据
  // 拉取离线消息
  console.log("home", "挂载")
  await useUserStore().loadInfo()
  doConnect("ws://localhost:8849")
  setLoginSuccessCallback(async () => {
    await Promise.all([
      useGroupsStore().loadGroupList(),
      useContactsStore().loadContactList(),
      useChatsStore().pullOfflineMsg(),
    ])
  })

  setBusinessCallback((resp) => {
    switch (resp.cmd) {
      case CMD.CHAT:
        let chatData = resp.data
        // console.log("收到消息", chatData)
        useChatsStore().insertMessage(chatData.type === 1 ? (chatData.toUid === useUserStore().info?.id ? chatData.fromUid : chatData.toUid) : chatData.roomId, chatData.type, chatData)
        break
      default:
        console.log("默认处理", resp)
        break
    }
  })
})

onUnmounted(() => {
  console.log("home", "卸载")
  doDisconnect()
})
</script>