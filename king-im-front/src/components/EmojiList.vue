<script setup>
import {getCurrentInstance} from "vue";

const {proxy} = getCurrentInstance();
const emits = defineEmits(['select'])

const onEmojiClick = (emojiText, index) => {
  emits('select', emojiText, index)
}
</script>

<template>
  <div class="emoji-list">
    <div @click="onEmojiClick(emojiText, index)" class="emoji pointer-select"
         v-html="proxy.$emoji.textToImg(emojiText)" v-for="(emojiText, index) in proxy.$emoji.emojiTextList"></div>
  </div>
</template>

<style scoped lang="stylus">
.emoji-list
  display flex
  margin auto
  width 560px
  flex-wrap wrap
  background-color rgba(128, 128, 128, 0.8)
  position relative
  &:after
    content ''
    position absolute
    bottom 0
    left 50%
    border-width 10px 10px 0
    border-style solid
    transform translate(-50%, 100%)
    border-color rgba(128, 128, 128, 0.3) transparent transparent
</style>