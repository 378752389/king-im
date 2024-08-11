<script setup>
import {ref} from "vue";
import AtSearch from "@/views/chat/components/AtSearch.vue";
import {deleteOne, getCursorNode} from "@/utils/textCursorUtils.js";
import {ShowToast} from "@/components/common/func/toast.js";
import KingToast from "@/components/common/KingToast.vue";

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
  <div ref="testRef" class="test">
    <button @click="init">初始化</button>
    <button @click="method2">初始化2</button>
    <button @click="select">初始化2</button>
    <div ref="editAreaRef" class="edit-area" @beforeinput="onBeforeinput" contenteditable="true" @input="onInput"/>
    <AtSearch @item-confirm="onItemConfirm" ref="atSearchRef" :search-text="''"/>
<!--    <king-toast :message="'ojbk'" />-->
    <!--    <textarea ref="editAreaRef" class="edit-area" contenteditable="true" @focus="onFocus" @input="onSendMessageClick" />-->
  </div>
</template>

<style scoped lang="stylus">

.test
  background-color #f6f6f6
  overflow hidden

.select-area
  width 80%
  height 300px
  margin auto
  background-color #f6f6f6

.box
  width 60px
  height 40px
  background linear-gradient(135deg, #f5f5f5 0%, blue 100%)

.edit-area
  padding 10px 5px
  margin 40px auto
  margin-top 100px
  width 80%
  height 200px
  outline 1px solid black
  background #f6f6f6
</style>

