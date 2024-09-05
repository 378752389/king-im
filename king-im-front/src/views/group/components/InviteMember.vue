<script setup>
import CommonList from "@/components/CommonList.vue";
import {useContactsStore} from "@/stores/contacts.js";
import {computed, ref} from "vue";

const props = defineProps({
  currentMemberList: {
    type: Array,
    default: []
  }
})

const contactStore = useContactsStore()


const selectedList = ref([])
const onItemChange = (contact) => {
  if (selectedList.value.findIndex(item => item.peerId === contact.peerId) === -1) {
    selectedList.value.push(contact)
  }
  // todo for test selectedList.value.push(contact)
}

const onRemoveClick = (selectedItem) => {
  selectedList.value = selectedList.value.filter(item => item.peerId !== selectedItem.peerId)
}

const selectableContactList = computed(() => {
  let list = props.currentMemberList.map(member => member.id)
  return contactStore.contactListGetter.filter(contact => list.findIndex(userId => contact.peerId === userId) === -1)
})

const clearSelectedList = () => {
  selectedList.value = []
}

defineExpose({
  selectedList,
  clearSelectedList
})
</script>

<template>
  <div class="invite-member">
    <CommonList @item-change="onItemChange" :list="selectableContactList"
                :render-props="{id: 'id', name: 'peerNickname', avatar: 'peerAvatar'}"/>

    <div class="selected-area">
      <div class="selected-item" v-for="selectedItem in selectedList">
        <img width="60" height="60" :src="selectedItem.peerAvatar" alt=""/>
        <i @click="onRemoveClick(selectedItem)" class="iconfont icon-close"/>
        <div class="name">
          {{ selectedItem.peerNickname }}
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped lang="stylus">
.invite-member
  width 560px
  height 400px
  display flex

  .list
    width 240px

  .selected-area
    overflow auto
    display flex
    flex-wrap wrap
    width 280px
    border 1px solid rgba(0, 0, 0, 0.2)
    margin 0 20px

    .selected-item
      position relative
      margin 10px

      i
        position absolute
        right -10px
        top -8px

      .name
        text-align center
        overflow hidden
        white-space nowrap
        text-overflow ellipsis


</style>