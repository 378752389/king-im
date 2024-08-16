<script setup>
import {reactive, ref, watchEffect} from "vue";

const props = defineProps({
  text: {
    type: String,
    default: '',
  },
  list: {
    type: Array,
    default: []
  }
})
const searchItemRef = ref([])
const atSearchRef = ref()
const atSearchData = reactive({
  x: 0,
  y: 0,
  show: false,
  activeIdx: 0,
})

const emits = defineEmits(["confirm"])

const moveUp = () => {
  if (atSearchData.activeIdx > 0) {
    atSearchData.activeIdx--;
  }
}
const moveDown = () => {
  if (atSearchData.activeIdx < props.list.length - 1) {
    atSearchData.activeIdx++;
  }
}
const open = ({x, y}) => {
  console.log(x, y)
  atSearchData.x = x;
  atSearchData.y = y;
  atSearchData.show = true;
}
const close = () => {
  atSearchData.show = false;
  atSearchData.activeIdx = 0;
  atSearchData.x = '-9999px';
  atSearchData.y = '-9999px';
}


const onClick = (item) => {
  emits('confirm', item)
  close()
}

const isOpen = () => {
  return atSearchData.show
}

const atItemGetter = () => {
  return props.list[atSearchData.activeIdx]
}

// 重置激活索引
watchEffect(() => {
  if (props.text) {
    console.log(props.text)
    atSearchData.activeIdx = 0
  }
})

defineExpose({
  open,
  close,
  isOpen,
  atItemGetter,

  moveUp,
  moveDown,
  confirm,
})
</script>

<template>
  <div ref="atSearchRef" class="at-search">
    <ul>
      <li ref="searchItemRef" :class="['pointer-select', 'item', atSearchData.activeIdx === idx ? 'active': '']"
          autofocus @click="onClick(item)" :tabindex="item.id"
          :key="item.id" v-for="(item, idx) in list">
        <img :src="item.avatar" class="avatar" alt="" /> {{ item.name }}
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
    display flex

    .avatar
      margin-right 10px
      height 20px
      width 20px

    &.active, &:hover
      background-color rgba(0, 0, 0, 0.1)
</style>