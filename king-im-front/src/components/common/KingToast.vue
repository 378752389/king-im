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
  padding 10px 50px

.info
  background-color grey
  color white

.danger
  background-color crimson
  color white

.success
  background-color #55a532
  color white

.fade-enter-active,
.fade-leave-active {
  transition opacity 2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity 0;
}
</style>