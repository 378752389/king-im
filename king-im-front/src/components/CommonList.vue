<script setup>
import {computed, ref, watchEffect} from "vue";
import KingContextMenu from "@/components/common/KingContextMenu.vue";

const props = defineProps({
  selectedId: {
    type: Number,
    default: 0,
  },
  renderProps: {
    id: 'id',
    name: 'name',
    avatar: 'avatar',
  },
  list: {
    type: Array,
    required: true
  }
})

const searchText = ref()

const filterableList = computed(() => {
  if (!searchText.value) {
    return props.list
  }
  return props.list.filter((item) => {
    return !!(item[props.renderProps.name] && item[props.renderProps.name].toLowerCase().includes(searchText.value.toLowerCase()));
  })
})

const activeId = ref()

watchEffect(() => {
  activeId.value = props.selectedId
})

const emits = defineEmits(["itemChange", "searchInput"])

const onItemClick = (item) => {
  activeId.value = item[props.renderProps.id]
  emits('itemChange', item)
}


const onContactSearchInput = () => {
  emits('searchInput', searchText.value)
}


const itemAvatar = (item) => {
  if (item[props.renderProps.avatar] == null || item[props.renderProps.avatar] === '') {
    // todo
    return `https://picsum.photos/512/512?id=${item[props.renderProps.id]}`
  }
  return item[props.renderProps.avatar]
}

const contextMenuRef = ref()
const contextMenuData = ref()
const contextMenuClose = ref()
const onContextmenuClick = (e, data) => {
  e.preventDefault();
  contextMenuRef.value.open(e, data)
  contextMenuData.value = data
  contextMenuClose.value = contextMenuRef.value.close
}


</script>

<template>
  <div class="list">
    <div class="search-wrap">
      <input type="search" @input="onContactSearchInput" v-model="searchText" class="search"/>
    </div>

    <div :class="['item', {'active': item[renderProps.id] === activeId}]"
         :key="item[renderProps.id]"
         v-for="item in filterableList"
         @click="onItemClick(item)"
         @contextmenu="onContextmenuClick($event, item)"
    >
      <img class="avatar" :src="itemAvatar(item)" alt=""/>
      <span class="name">{{ item[renderProps.name] }}</span>
    </div>

    <KingContextMenu ref="contextMenuRef">
      <slot name="contextmenu" :contextmenu="contextMenuData" :close="contextMenuClose"></slot>
    </KingContextMenu>
  </div>
</template>

<style scoped lang="stylus">
.list
  height 100%
  width 300px
  padding 0 2px
  border-right 1px solid rgba(0, 0, 0, 0.1)
  background-color #f6f6f6
  overflow auto

  .search-wrap
    padding 10px 5px
    display flex
    justify-content space-between
    align-items center

    .search
      padding 0 5px
      border 1px solid rgba(0, 0, 0, 0.2)
      outline none
      height 40px
      width 100%
      font-size 1.5em

  .item
    display flex
    align-items center
    height 70px
    //border-top 1px solid black
    padding 0 10px
    background-color white

    &.active
      background-color rgba(0, 0, 0, 0.2)

    &:not(.active):hover
      background-color rgba(0, 0, 0, 0.1)

    .avatar
      width: 50px
      height: 50px
      margin-right 20px

    .name
      white-space nowrap
      overflow hidden
      text-overflow ellipsis
</style>