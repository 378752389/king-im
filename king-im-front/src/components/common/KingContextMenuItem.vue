<script setup>

const props = defineProps({
  name: '',
  icon: '',
})

const emits = defineEmits(['item-click'])
const onMenuItemClick = () => {
  emits('item-click')
}

</script>

<template>
  <div @click="onMenuItemClick" class="king-context-menu-item pointer-select">
    <i v-if="icon != null" :class="['iconfont', icon]"/>
    <div>{{ name }}</div>
  </div>
</template>

<style scoped lang="stylus">
.king-context-menu-item
  padding 10px 20px
  display flex
  justify-content flex-start

  &:hover
    color #9a6e3a
</style>