<script setup>
import {ref, onMounted, useAttrs} from "vue";

const dialogRef = ref()
const attrs = useAttrs()
const emits = defineEmits()
const defaultConfirmAction = ref(true)
const defaultCancelAction = ref(true)

defineProps({
  title: {
    type: String
  }
})

onMounted(() => {
  defaultConfirmAction.value = attrs['onConfirm'] == null
  defaultCancelAction.value = attrs['onCancel'] == null
})

const handleExternalClick = (e) => {
  if (e.target === dialogRef.value && isOpen()) {
    close()
  }
}

const onConfirmClick = () => {
  !defaultConfirmAction.value && emits('confirm')
  defaultConfirmAction.value && close()
}

const onCancelClick = () => {
  !defaultCancelAction.value && emits('cancel')
  defaultCancelAction.value && close()
}

const open = () => {
  dialogRef.value.showModal()
  setTimeout(() => {
    document.addEventListener('click', handleExternalClick)
  }, 0)

}

const isOpen = () => {
  return dialogRef.value.open
}

const close = () => {
  dialogRef.value.close()
  setTimeout(() => {
    document.removeEventListener('click', handleExternalClick)
  }, 0)
}

defineExpose({
  open,
  close,
  isOpen,
})
</script>

<template>
  <dialog ref="dialogRef">
    <div class="dialog-wrapper">
      <h2 style="padding: 0 5px 10px; border-bottom: 1px solid rgba(0, 0, 0, 0.1)" v-if="title != null">{{ title }}</h2>
      <div class="content">
        <slot/>
      </div>
      <form method="dialog">
        <button type="button" @click="onConfirmClick">确定</button>
        <button type="button" @click="onCancelClick">取消</button>
      </form>
    </div>
  </dialog>
</template>

<style scoped lang="stylus">
dialog
  padding 20px
  position fixed
  left 50%
  top 100px
  transform translateX(-50%)

  .dialog-wrapper
    display flex
    flex-direction column

    .content
      min-height 100px
      max-height 460px
      overflow auto

    form
      display flex
      justify-content space-around
      border-top 1px solid rgba(0, 0, 0, 0.1)

    button[type=button]
      width 100px
      height 40px
      margin 20px 20px 0
</style>