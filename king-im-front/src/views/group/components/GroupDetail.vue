<script setup>
import GroupMemberList from "@/views/group/components/GroupMemberList.vue";
import GroupForm from "@/views/group/components/GroupForm.vue";
import {deleteGroupAPI, modifyGroupAPI, quitGroupAPI} from "@/http/social.js";
import {useGroupsStore} from "@/stores/groups.js";
import {useChatsStore} from "@/stores/chats.js";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import {ShowToast} from "@/components/common/func/toast.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";

const chatStore = useChatsStore()
const router = useRouter()
const groupsStore = useGroupsStore()
const {selectedGroupGetter} = storeToRefs(groupsStore)

const onSaveClick = async () => {
  console.log(selectedGroupGetter.value)
  await modifyGroupAPI({
    roomId: selectedGroupGetter.value.id,
    notice: selectedGroupGetter.value.notice,
    name: selectedGroupGetter.value.name,
    avatar: selectedGroupGetter.value.avatar?.startsWith('data') ? null : selectedGroupGetter.value.avatar,
    myName: selectedGroupGetter.value.myName,
    markName: selectedGroupGetter.value.markName,
  })
  ShowToast({
    message: "群信息修改成功",
    type: 'success',
  })
}

const onQuitClick = async () => {
  await quitGroupAPI({
    roomId: selectedGroupGetter.id
  })

  alert('退出成功')
}

const onDeleteClick = async () => {
  ShowMessageBox({
    message: `请确认是否删除 <span style="color: red;">${selectedGroupGetter.value.name}</span> 群聊 ？`,
    confirm: async () => {
      await deleteGroupAPI({
        roomId: selectedGroupGetter.value.id
      })
      // todo 弹窗提示
      ShowToast({
        message: "删除群聊成功",
        timeout: 3000,
        type: "success"
      })
      await groupsStore.loadGroupList()

    },
  })

}

const onSendClick = (groupData) => {
  let chatId = groupData.roomId
  let chatType = 2
  chatStore.insertMessage(chatId, chatType, null)
  chatStore.setChat(chatId, chatType)
  router.push({name: 'chat'})
}
</script>

<template>
  <div class="group-detail">
    <group-form @save="onSaveClick" @quit="onQuitClick" @send="onSendClick" @delete="onDeleteClick"
                :group-form="selectedGroupGetter"/>
    <group-member-list :room-id="selectedGroupGetter.id" :group-member-list="selectedGroupGetter.roomMemberList"/>
  </div>
</template>

<style scoped lang="stylus">
.group-detail
  margin 100px auto 0

  .profile
    display flex
    justify-content space-around

    img
      width 160px
      height 160px
      margin 10px 80px 0 0
</style>