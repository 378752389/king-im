<script setup>

import {computed, onMounted, reactive, ref} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import {useGroupsStore} from "@/stores/groups.js";

const props = defineProps({
  searchText: ''
})
const searchItemRef = ref([])
const atSearchRef = ref()
onMounted(() => {
  console.log("searchItemRef", searchItemRef.value)
})

const keyDownHandler = (e) => {
  if (e.key === 'ArrowUp') {

  } else if (e.key === 'ArrowDown') {

  }
}


const atSearchData = reactive({
  x: 0,
  y: 0,
  show: false,
  activeIdx: 0,
})

const handleExternalClick = (e) => {
  if (e.target !== atSearchRef.value) {
    close()
  }
}


const open = ({x, y}) => {
  console.log(x, y)
  atSearchData.x = x;
  atSearchData.y = y;
  atSearchData.show = true;
  console.log("open")
  document.addEventListener('click', handleExternalClick)

}

const close = () => {
  atSearchData.show = false;
  document.removeEventListener('click', handleExternalClick)
}

const chatStore = useChatsStore()
const groupStore = useGroupsStore()
const searchList = computed(() => {
  // if (chatStore.currentChatTypeGetter === 2) {
  //   let group = groupStore.getGroup(chatStore.currentChatIdGetter)
  //   if (group == null || group.roomMemberList == null) {
  //     return []
  //   }
  //   if (props.searchText === '' || props.searchText == null) {
  //     return group.roomMemberList
  //   } else {
  //     return group.roomMemberList.filter(member => member.name.startsWith(props.searchText))
  //   }
  // }
  // return []

  return [
    {
      id: 1,
      name: 'king',
    },
    {
      id: 2,
      name: 'ayfl',
    },
    {
      id: 1,
      name: 'pepe',
    },
  ]
})


const emits = defineEmits(["item-confirm"])

const onSearchItemClick = (item) => {
  emits('item-confirm', item)
  atSearchData.show = false
}

const isOpen = () => {
  return atSearchData.show
}

defineExpose({
  open,
  close,
  isOpen,
})
</script>

<template>
  <div ref="atSearchRef" class="at-search">
    <ul>
      <li ref="searchItemRef" :class="['pointer-select', 'item', atSearchData.activeIdx === idx ? 'active': '']"
          autofocus @click="onSearchItemClick(item)" :tabindex="item.id"
          :key="item.id" v-for="(item, idx) in searchList">
        {{ item.name }}
      </li>
    </ul>
  </div>
</template>

<style scoped lang="stylus">
.at-search
  position fixed
  left v-bind('atSearchData.x')
  top v-bind('atSearchData.y')
  display v-bind('atSearchData.show ? "block": "none"')
  transform translateY(-100%)
  background-color white
  width 120px
  max-height 140px
  text-overflow ellipsis
  white-space nowrap
  overflow auto
  outline 1px solid rgba(0, 0, 0, 0.1)

  li
    padding 5px 10px

    &.active, &:hover
      background-color rgba(0, 0, 0, 0.1)
</style>