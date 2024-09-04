<script setup>

import {ref} from "vue";

const props = defineProps({
  url: {
    type: String,
  }
})
const emits = defineEmits(['upload'])

const uploadRef = ref()
const onUploadClick = () => {
  uploadRef.value.click()
}

const onUploadChange = (e) => {
  emits('upload', e)
}
</script>

<template>
  <div class="king-upload pointer-select" @click="onUploadClick">
    <i v-if="!url" class="iconfont icon-add" />
    <img v-else width="100%" height="100%" :src="url" alt="">
    <input ref="uploadRef" type="file" name="file" hidden="hidden" @change="onUploadChange" />
  </div>
</template>

<style scoped lang="stylus">
.king-upload
  width 160px
  height 160px
  border 1px solid rgba(128, 128, 128, 0.8)
  border-radius 10px
  display flex
  justify-content center
  align-items center

  i
    font-size 50px
</style>