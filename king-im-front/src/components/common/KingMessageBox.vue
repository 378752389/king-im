<script setup>

import {onMounted, ref} from "vue";

const messageBoxRef = ref()
const props = defineProps({
  title: {
    type: String,
    default: "警告"
  },
  icon: {
    type: String,
    default: 'icon-warning',   // icon-info
  },
  message: {
    type: String,
    default: "请确认是否执行？"
  },
  confirm: {
    type: Function,
  },
  cancel: {
    type: Function,
  }
})

onMounted(() => {
  messageBoxRef.value.showModal()
})

const onConfirmClick = () => {
  props.confirm && props.confirm()
}
const onCancelClick = () => {
  props.cancel && props.cancel()
}

</script>

<template>
  <dialog ref="messageBoxRef" class="king-message-box">
    <h2><i :class="['iconfont', icon]"></i> {{ title }}</h2>
    <p v-html="message"></p>
    <div class="btn-group">
      <button class="btn confirm" @click="onConfirmClick">确定</button>
      <button class="btn cancel" @click="onCancelClick">取消</button>
    </div>
  </dialog>
</template>

<style scoped lang="stylus">
.king-message-box
  display flex
  flex-direction column
  width 500px
  padding 10px
  margin auto
  border none

  h2
    margin 10px 0 20px
    display flex
    align-items center
    i
      font-size 28px
      margin-right 5px
  .btn-group
    align-self flex-end

    .btn
      padding 3px 10px
      margin 10px
      border-color white
    .confirm
      background #0086b3
      color #cccccc
</style>