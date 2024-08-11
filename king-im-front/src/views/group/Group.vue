<script setup>
import CommonList from "@/components/CommonList.vue";
import {useGroupsStore} from "@/stores/groups.js";
import {storeToRefs} from "pinia";
import GroupDetail from "@/views/group/components/GroupDetail.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";

const groupStore = useGroupsStore()
const {groupListGetter, selectedGroupGetter} = storeToRefs(groupStore)

const onItemChange = (item) => {
  console.log(item)
  groupStore.setSelectedGroup(item)
}
</script>

<template>
  <div class="group">
    <CommonList :selected-id="groupStore.selectedGroupGetter?.roomId" @item-change="onItemChange"
                :list="groupListGetter" :render-props="{id: 'roomId', name: 'name', avatar: 'avatar'}">
      <template #contextmenu>
        <KingContextMenuItem name="标记"/>
        <KingContextMenuItem name="删除群聊天记录"/>
        <KingContextMenuItem name="退出群聊"/>
      </template>
    </CommonList>
    <GroupDetail v-if="selectedGroupGetter != null"/>
  </div>
</template>

<style scoped lang="stylus">
.group
  display flex
  height 100%
</style>