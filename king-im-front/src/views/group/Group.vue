<script setup>
import CommonList from "@/components/CommonList.vue";
import {useGroupsStore} from "@/stores/groups.js";
import {storeToRefs} from "pinia";
import GroupDetail from "@/views/group/components/GroupDetail.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";
import {ShowToast} from "@/components/common/func/toast.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";
import {useUserStore} from "@/stores/user.js";
import {useChatsStore} from "@/stores/chats.js";

const groupStore = useGroupsStore()
const {groupListGetter, selectedGroupGetter} = storeToRefs(groupStore)

const onItemChange = (item) => {
  console.log(item)
  groupStore.setSelectedGroup(item)
}

const onMarkGroupClick = (data, close) => {
  ShowToast({
    message: "标记功能暂时还未实现，敬请期待",
  })
  close()
}
const onDeleteGroupChatClick = async (data, close) => {
  // ShowToast({
  //   message: "删除群聊天记录功能暂时还未实现，敬请期待"
  // })
  ShowMessageBox({
    message: "请确认是否删除群聊聊天记录",
    confirm: async () => {
      useChatsStore().removeChat(data.roomId, 2)
      ShowToast({
        message: `${data.name} 聊天记录清除成功`,
        type: "success",
      })
      close()
    },
    cancel: () => {
      close()
    }
  })
}

const onRemoveGroupClick = (data, close) => {
  ShowMessageBox({
    message: "请确认是否移除群聊，移除群聊将会同时将聊天记录删除",
    confirm: async () => {
      await useGroupsStore().deleteGroup(data.roomId)
      ShowToast({
        message: `${data.name} 群聊移除成功`,
        type: "success",
      })
      close()
    },
    cancel: () => {
      close()
    }
  })
}
const onQuitGroupClick = (data, close) => {
  ShowMessageBox({
    message: "请确认是否退出群聊，退出群聊将会同时将聊天记录删除",
    confirm: async () => {
      await useGroupsStore().quitGroup(data.roomId)
      ShowToast({
        message: `${data.name} 群聊移除成功`,
        type: "success",
      })
      close()
    },
    cancel: () => {
      close()
    }
  })
}

</script>

<template>
  <div class="group">
    <CommonList :selected-id="groupStore.selectedGroupGetter?.roomId" @item-change="onItemChange"
                :list="groupListGetter" :render-props="{id: 'roomId', name: 'name', avatar: 'avatar'}">
      <template #contextmenu="{contextmenu, close}">
        <KingContextMenuItem @item-click="onMarkGroupClick(contextmenu, close)" name="标记"/>
        <KingContextMenuItem @item-click="onDeleteGroupChatClick(contextmenu, close)" name="删除群聊天记录"/>
        <KingContextMenuItem v-if="contextmenu?.leaderId === useUserStore().info?.id" @item-click="onRemoveGroupClick(contextmenu, close)" name="移除群聊"/>
        <KingContextMenuItem v-else @item-click="onQuitGroupClick(contextmenu, close)" name="退出群聊"/>
      </template>
    </CommonList>
    <GroupDetail v-if="selectedGroupGetter"/>
  </div>
</template>

<style scoped lang="stylus">
.group
  display flex
  height 100%
</style>