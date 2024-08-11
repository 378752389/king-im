<script setup>
import {useAttrs, onMounted} from "vue";

// const emits = defineEmits(['on-confirm'])
const attrs = useAttrs()
const emits = defineEmits()
onMounted(() => {
  console.log(attrs)
})

const doClick = () => {
  emits('on-confirm')
}
</script>

<template>
  <div class="test">hello, world</div>
  <button @click="doClick">点击</button>
</template>

<style scoped lang="stylus">
.test
  height 200px
  background-color #55a532
</style>