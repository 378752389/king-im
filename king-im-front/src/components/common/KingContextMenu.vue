<script setup>
import {onMounted, onUnmounted, reactive, ref} from 'vue'

const contextMenuRef = ref()
const contextMenuConfig = reactive({
  showContextMenu: false,
  left: 0,
  top: 0,
  width: 0,
  height: 0,
  data: null
})

// 监听外部点击事件
const outsideClick = (e) => {
  if (contextMenuConfig.showContextMenu) {
    if (e.clientX < contextMenuConfig.left || e.clientX > contextMenuConfig.width + contextMenuConfig.left
        || e.clientY < contextMenuConfig.top || e.clientY > contextMenuConfig.height + contextMenuConfig.top) {
      contextMenuConfig.showContextMenu = false
    }
  }
}

// 计算 contextmenu 宽高
const calcContextmenu = () => {
  contextMenuConfig.showContextMenu = true
  requestAnimationFrame(() => {
    let rect = contextMenuRef.value.getBoundingClientRect()
    contextMenuConfig.width = rect.right - rect.left
    contextMenuConfig.height = rect.bottom - rect.top
    contextMenuConfig.showContextMenu = false
  })
}

onMounted(() => {
  calcContextmenu()
  document.addEventListener("click", outsideClick)
})

onUnmounted(() => {
  document.removeEventListener('click', outsideClick)
})

const open = (e, data) => {
  e.preventDefault()
  close()
  contextMenuConfig.left = e.clientX
  contextMenuConfig.top = e.clientY
  contextMenuConfig.showContextMenu = true
  contextMenuConfig.data = data
}

const close = () => {
  contextMenuConfig.left = 0
  contextMenuConfig.top = 0
  contextMenuConfig.data = null
  contextMenuConfig.showContextMenu = false
}

defineExpose({
  open,
  close,
  contextMenuData: contextMenuConfig.data
})
</script>

<template>
  <div ref="contextMenuRef" class="king-context-menu" v-show="contextMenuConfig.showContextMenu">
    <slot :context="contextMenuConfig.data" :close="close"/>
  </div>
</template>

<style scoped lang="stylus">
.king-context-menu
  position fixed
  min-width 100px
  left v-bind('contextMenuConfig.left+"px"')
  top v-bind('contextMenuConfig.top+"px"')
  background-color #dad6d6

</style>