<script setup>
import {onMounted, onUnmounted, reactive, ref} from "vue";

const props = defineProps({
  trigger: {
    type: String
  },
  position: {
    type: String,
    default: 'top'
  }
})

const popoverData = reactive({
  showFlag: false,
  position: {
    left: 0,
    top: 0,
  }
})
const kingPopoverRef = ref()
const referenceRef = ref()
const contentRef = ref()

onMounted(() => {
  setReferencePosition()

  if (props.trigger === 'click') {
    kingPopoverRef.value.addEventListener("click", (e) => {
      if (referenceRef.value.contains(e.target)) {
        popoverData.showFlag = !popoverData.showFlag
        e.target.focus()
        requestAnimationFrame(() => {
          contentRef.value.style.left = popoverData.position.left
          contentRef.value.style.top = popoverData.position.top
        })
      }
    }, true)

  } else if (props.trigger === 'hover') {
    kingPopoverRef.value.addEventListener("mouseover", (e) => {
      popoverData.showFlag = true;
    })
    kingPopoverRef.value.addEventListener("mouseleave", (e) => {
      popoverData.showFlag = false;
    })
  }
})


// onUpdated(() => {
//   setReferencePosition()
// })

const setReferencePosition = () => {
  // 计算content盒子大小
  popoverData.showFlag = true
  requestAnimationFrame(() => {
    let referenceBox = referenceRef.value.getBoundingClientRect()
    let contentBox = contentRef.value.getBoundingClientRect()
    console.log(contentBox)
    if (props.position === 'top') {
      popoverData.position.left = (referenceBox.width / 2 - contentBox.width / 2) + 'px'
      popoverData.position.top = (-contentBox.height) + 'px'
    } else if (props.position === 'left') {
      popoverData.position.left = (-contentBox.width) + 'px'
      popoverData.position.top = (-contentBox.height / 2 + referenceBox.height / 2) + 'px'
      console.log(popoverData.position.left, popoverData.position.top)
    } else if (props.position === 'bottom') {
      popoverData.position.left = (referenceBox.width / 2 - contentBox.width / 2) + 'px'
      popoverData.position.top = referenceBox.height + 'px'
      console.log(popoverData.position.left, popoverData.position.top)
    } else if (props.position === 'right') {
      popoverData.position.left = (referenceBox.width) + 'px'
      popoverData.position.top = (-contentBox.height / 2 + referenceBox.height / 2) + 'px'
      console.log(popoverData.position.left, popoverData.position.top)
    } else if (props.position === 'top left') {
      popoverData.position.left = 0;
      popoverData.position.top = (-contentBox.height) + 'px'
    } else if (props.position === 'top right') {
      popoverData.position.left = (referenceBox.width) + 'px'
      popoverData.position.top = (-contentBox.height) + 'px'
    } else if (props.position === 'bottom left') {
      popoverData.position.left = 0;
      popoverData.position.top = referenceBox.height + 'px'
    } else if (props.position === 'bottom right') {
      popoverData.position.left = (referenceBox.width) + 'px'
      popoverData.position.top = referenceBox.height + 'px'
    }
    popoverData.showFlag = false
  })


}

onUnmounted(() => {
  if (kingPopoverRef.value) {
    kingPopoverRef.value.removeAllListeners()
  }
})

const open = () => {
  popoverData.showFlag = true
}

const close = () => {
  popoverData.showFlag = false
}

defineExpose({
  close,
  open
})
</script>

<template>
  <div ref="kingPopoverRef" class="king-popover">
    <div ref="referenceRef" class="reference">
      <slot name="reference"/>
    </div>
    <div ref="contentRef" v-show="popoverData.showFlag" :class="['content']">
      <slot/>
    </div>
  </div>
</template>

<style scoped lang="stylus">
.king-popover
  position relative

  .content
    position absolute
    z-index 999

</style>