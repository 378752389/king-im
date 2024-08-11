<script setup>
defineProps({
  message: '',
  type: '',
})


</script>

<template>
  <transition name="fade">
    <div :class="['king-toast', `${type || 'info'}`]">
      {{ message }}
    </div>
  </transition>
</template>

<style scoped lang="stylus">
.king-toast
  border-radius 20px
  position fixed
  padding 10px 50px
  left 50%
  transform translateX(-50%)
  top 80%

.info
  background-color grey

.danger
  background-color crimson

.success
  background-color #55a532

.fade-enter-active,
.fade-leave-active {
  transition opacity 2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity 0;
}
</style>