<script setup>
import {ref} from "vue";
import AtSearch from "@/views/chat/components/AtSearch.vue";
import {deleteOne, getCursorNode} from "@/utils/textCursorUtils.js";
import {ShowToast} from "@/components/common/func/toast.js";
import KingToast from "@/components/common/KingToast.vue";
import TestComponent from "@/views/TestComponent.vue";

const atSearchRef = ref()
const onInput = (e) => {
  // 删除 deleteContentBackward  编辑  insertText
  // if (e.data === '@') {
  //   let sel = document.getSelection()
  //   let rect = sel.getRangeAt(0).getBoundingClientRect()
  //   atSearchRef.value.open({
  //     x: rect.x + 'px',
  //     y: rect.y + 'px',
  //   })
  // }
}

const deleteOneWord = () => {
  let sel = document.getSelection()
  let range = sel.getRangeAt(0)
  let focusNode = sel.focusNode

  // 如果是父容器，则获取其最后一个节点
  if (focusNode === editAreaRef.value) {
    focusNode = focusNode.lastChild
  }

  if (focusNode.nodeType === Node.ELEMENT_NODE) {
    if (focusNode.nodeName === 'SPAN') {
      const preNode = focusNode.previousSibling
      editAreaRef.value.removeChild(focusNode)
      sel.removeAllRanges()
      range.setEndAfter(preNode)
      sel.addRange(range)
    }
  }

}

const onItemConfirm = (item) => {

}

const onBeforeinput = (e) => {
  const {data, inputType} = e
  if (inputType === 'deleteContentBackward') {
    e.preventDefault()
    deleteOne(editAreaRef.value)
  }
}

const editAreaRef = ref()
const init = () => {
  editAreaRef.value.innerHTML = '我不如<br>地狱，<span id="sp">谁入地</span>狱'
}


const method2 = () => {
  console.log(document.getSelection().focusNode.previousSibling.previousSibling.nodeName);
}


const select = () => {
  ShowToast({message: "ojbk", timeout: 1000})
  // let selection = document.getSelection();
  // let rangeAt = selection.getRangeAt(0);
  // let node = selection.focusNode
  // let sp = document.querySelector("#sp")
  // rangeAt.setStartAfter(sp)
  // rangeAt.collapse(true)
  // // rangeAt.setEnd(node, 0)
  // console.log(selection, rangeAt)
  // // rangeAt.collapse()
  // // console.log(rangeAt)
}

</script>

<template>
   <div>
     <TestComponent />
   </div>
</template>

<style scoped lang="stylus">

.test
  background-color #f6f6f6
  overflow hidden

</style>

