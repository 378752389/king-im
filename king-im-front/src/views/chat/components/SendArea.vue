<script setup>

import {sendAPI} from "@/http/message.js";
import {reactive, ref} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import KingDialog from "@/components/common/KingDialog.vue";
import ChatBubble from "@/views/chat/components/ChatBubble.vue";
import AtSearch from "@/views/chat/components/AtSearch.vue";
import {getCursorNode} from "@/utils/textCursorUtils.js";

const chatStore = useChatsStore()
const editAreaData = reactive({
  canEdit: true,
  inCompositionEdit: false,
  sendContent: '',
  focusNode: null,
  focusOffset: 0,
  atSearchText: '',
})
const editAreaRef = ref()
const emits = defineEmits(['send-msg', 'upload-file'])

const createSendContent = () => {
  const atUids = []
  editAreaRef.value.childNodes.forEach(node => {
    if (node.nodeType === Node.ELEMENT_NODE) {
      if (node.nodeName === 'SPAN') {
        let atUid = node.dataset.atUid
        atUid && atUids.push(atUid)
        editAreaData.sendContent += html2Escape(node.textContent)
      }
    } else if (node.nodeType === Node.TEXT_NODE) {
      editAreaData.sendContent += node.textContent
    } else {
      console.log("解析出其他的节点类型", node, editAreaRef.value.textContent)
    }
  })
}

const html2Escape = (strHtml) => {
  return strHtml.replace(/[<>&"]/g, function (c) {
    return {
      '<': '&lt;',
      '>': '&gt;',
      '&': '&amp;',
      '"': '&quot;'
    }[c];
  });
}

const onSendBtnClick = async () => {
  // 内容为空不能直接发送
  createSendContent();
  if (editAreaData.sendContent == null || editAreaData.sendContent.trim() === '') {
    alert("发送内容不能为空")
    return
  }
  const msg = await sendAPI({
    msgType: 1,
    text: editAreaData.sendContent,
    chatId: chatStore.currentChatIdGetter,
    chatType: chatStore.currentChatTypeGetter
  })
  chatStore.insertMessage(chatStore.currentChatIdGetter, chatStore.currentChatTypeGetter, msg)
  editAreaData.sendContent = ''
  editAreaRef.value.innerHTML = ''
  emits('send-msg')
}

const onTextAreaKeyDown = (e) => {
  console.log("keydown", e)
  // 阻止回车默认行为
  if (e.shiftKey && e.keyCode === 13) {
    // todo 换行
    e.preventDefault()
    createNewLine()
  } else if (e.keyCode === 13) {
    // todo
    // onSendBtnClick()
  }
}

const createNewLine = () => {
  console.log("create new line")
  let newLineNode = document.createTextNode('\n')
  let sel = document.getSelection()
  let range = sel.getRangeAt(0)
  range.insertNode(newLineNode)
  range.setStartAfter(newLineNode)
  range.collapse()
  sel.removeAllRanges()
  sel.addRange(range)

  console.log(editAreaRef.value.childNodes)
}

const onTextAreaKeyUp = (e) => {

}

const onShowEmojiClick = () => {
  alert("该功能还在开发中...")
}

const uploadRef = ref()
const onUploadFileClick = () => {
  uploadRef.value.click()
}

const uploadConfirm = (e) => {
  emits('upload-file', e.target.files[0])
}


const onScreenShotClick = () => {
  alert("该功能还在开发中...")
}


// 聊天历史
const onShowChatHistoryClick = () => {
  chatHistoryDialogRef.value.open()
}
const chatHistoryDialogRef = ref()
const chatHistory = reactive({
  data: [
    {
      "id": 4,
      "roomId": 4,
      "fromUid": 1,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "很好，很棒",
      "contentType": 1,
      "sendTime": 1720664821000,
      "type": 2,
      "status": 1
    },
    {
      "id": 5,
      "roomId": 4,
      "fromUid": 1,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "nice",
      "contentType": 1,
      "sendTime": 1720664841000,
      "type": 2,
      "status": 1
    },
    {
      "id": 6,
      "roomId": 4,
      "fromUid": 1,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "不行，我得下了",
      "contentType": 1,
      "sendTime": 1720664860000,
      "type": 2,
      "status": 1
    },
    {
      "id": 50,
      "roomId": 4,
      "fromUid": 1,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": {
        "videoExtra": null,
        "audioExtra": null,
        "pictureExtra": null,
        "fileExtra": null
      },
      "content": "滚蛋",
      "contentType": 1,
      "sendTime": 1722245804000,
      "type": 2,
      "status": 1
    },
    {
      "id": 75,
      "roomId": 4,
      "fromUid": 3,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "ojbk",
      "contentType": 1,
      "sendTime": 1722400229000,
      "type": 2,
      "status": 1
    },
    {
      "id": 76,
      "roomId": 4,
      "fromUid": 3,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "a",
      "contentType": 1,
      "sendTime": 1722400237000,
      "type": 2,
      "status": 1
    },
    {
      "id": 77,
      "roomId": 4,
      "fromUid": 3,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "asdas",
      "contentType": 1,
      "sendTime": 1722400242000,
      "type": 2,
      "status": 1
    },
    {
      "id": 78,
      "roomId": 4,
      "fromUid": 1,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "行吧，先下了",
      "contentType": 1,
      "sendTime": 1722407305000,
      "type": 2,
      "status": 1
    },
    {
      "id": 82,
      "roomId": 4,
      "fromUid": 4,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": {
        "videoExtra": {
          "size": 14710639,
          "url": "http://localhost/im-upload/20240731/wangwu/6874ccbfa922440ec193e7aa9dc10fb4.mp4",
          "coverUrl": null
        },
        "audioExtra": null,
        "pictureExtra": null,
        "fileExtra": null
      },
      "content": "",
      "contentType": 4,
      "sendTime": 1722407654000,
      "type": 2,
      "status": 1
    },
    {
      "id": 83,
      "roomId": 4,
      "fromUid": 4,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "ok",
      "contentType": 1,
      "sendTime": 1722408219000,
      "type": 2,
      "status": 1
    },
    {
      "id": 85,
      "roomId": 4,
      "fromUid": 4,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "ok",
      "contentType": 1,
      "sendTime": 1722413217000,
      "type": 2,
      "status": 1
    },
    {
      "id": 86,
      "roomId": 4,
      "fromUid": 4,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "no",
      "contentType": 1,
      "sendTime": 1722413222000,
      "type": 2,
      "status": 1
    },
    {
      "id": 87,
      "roomId": 4,
      "fromUid": 4,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "请问",
      "contentType": 1,
      "sendTime": 1722413349000,
      "type": 2,
      "status": 1
    },
    {
      "id": 88,
      "roomId": 4,
      "fromUid": 4,
      "toUid": null,
      "referMsgId": null,
      "atUids": [],
      "extra": null,
      "content": "滚",
      "contentType": 1,
      "sendTime": 1722413421000,
      "type": 2,
      "status": 1
    }
  ],
})


const atSearchRef = ref()

const onTextAreaInput = (e) => {
  if (!editAreaData.inCompositionEdit) {
    // todo
    if (e.data === '@') {
      let sel = document.getSelection()
      let range = sel.getRangeAt(0)
      editAreaData.focusNode = sel.focusNode
      editAreaData.focusOffset = sel.focusOffset
      let rect = range.getBoundingClientRect();
      atSearchRef.value.open({x: rect.x + 'px', y: rect.y + 'px'})
    } else {
      let sel = document.getSelection()
      editAreaData.focusNode = sel.focusNode
      editAreaData.focusOffset = sel.focusOffset
      let atIdx = editAreaData.focusNode.textContent.indexOf('@')
      editAreaData.atSearchText = editAreaData.focusNode.textContent.substring(atIdx + 1)
      console.log(editAreaData.atSearchText)
    }
  }
}

const deleteOneWord = () => {
  let sel = document.getSelection()
  let range = sel.getRangeAt(0)
  let focusNode = sel.focusNode

  console.log("del", focusNode)

  if (focusNode.nodeType === Node.TEXT_NODE) {
    let content = focusNode.textContent
    let len = content.length
    if (len > 0) {
      range.setStart(focusNode, len - 1)
      range.setEnd(focusNode, len)
      range.extractContents()
    } else {
      let preNode = focusNode.previousSibling
      if (preNode) {
        sel.removeAllRanges()
        let newRange = document.createRange()
        if (preNode.nodeType === Node.TEXT_NODE) {
          newRange.setEnd(preNode, preNode.textContent.length)
        } else {
          newRange.setEnd(preNode, 1)
        }
        newRange.collapse()
        sel.addRange(newRange)
        // text节点为空，获取并设置为前一个节点，然后再次进行删除操作
        deleteOneWord()
      }
    }
  } else if (focusNode.nodeType === Node.ELEMENT_NODE) {
    let preNode = focusNode.previousSibling
    if (focusNode.nodeName === 'SPAN') {
      deleteNodeAndResetRange(preNode, range)
    } else if (focusNode === editAreaRef.value) {
      let node = editAreaRef.value.lastChild
      if (node) {
        range.setEnd(node, node.textContent.length)
        range.collapse()
        deleteOneWord()
      }
    } else if (focusNode.nodeName === 'DIV') {  // 处理换行
      // 设置光标坐标
      deleteNodeAndResetRange(preNode, range)
    }
  }
}

const deleteNodeAndResetRange = (preNode, range) => {
  // 设置光标坐标
  console.log("preNode", preNode, preNode.childNodes.length)
  if (preNode && preNode.nodeType === Node.TEXT_NODE) {
    // 删除节点内容
    range.setStart(preNode, preNode.textContent.length)
    range.extractContents()
    range.setEnd(preNode, preNode.textContent.length)
    range.collapse()
  } else {
    let len = preNode.childNodes.length
    if (len > 0) {
      range.setStart(preNode, len)
      range.extractContents()
      range.setEnd(preNode, len)
      range.collapse()
    } else {
      // todo 删除换行失效
      range.setStart(preNode, 0)
      range.extractContents()
      range.collapse(true)
    }
  }
}


const onBeforeinput = (e) => {
  const {inputType, data} = e
  console.log(e)
  if (inputType === 'deleteContentBackward') {
    let node = getCursorNode(editAreaRef.value)
    if (node === Node.ELEMENT_NODE) {
      e.preventDefault()
      deleteOneWord()
    }
  }
  if (inputType === 'insertParagraph') {
    // 发送消息
    e.preventDefault()
    // onSendBtnClick()
  } else if (inputType === 'insertLineBreak') {
    // e.preventDefault()
    // 换行
  }
}


const onCompositionstart = () => {
  editAreaData.inCompositionEdit = true
}
/**
 * 组合结束，调用监听
 */
const onCompositionend = (e) => {
  editAreaData.inCompositionEdit = false
  onTextAreaInput(e)
}

const searchItemConfirm = (item) => {
  let sel = document.getSelection()
  let range = sel.getRangeAt(0)
  // 删除 @ 内容
  let atIdx = editAreaData.focusNode.textContent.lastIndexOf('@')
  range.setStart(editAreaData.focusNode, atIdx)
  range.setEnd(editAreaData.focusNode, editAreaData.focusOffset)
  range.deleteContents()
  // 插入 span标签，表示引用用户
  let span = document.createElement("span")
  span.dataset.atUid = item.id
  span.innerText = `@${item.name}`
  range.insertNode(span)
  range.collapse()
  // 不需要选择at对象，打开下面代码
  range.insertNode(document.createTextNode("\u00A0"))
  range.collapse()
  editAreaRef.value.focus()
}

</script>

<template>
  <div class="send-area">
    <div class="tools">
      <div class="tool-item pointer-select" @click="onShowEmojiClick">emoji</div>
      <div class="tool-item pointer-select" @click="onUploadFileClick">文件<input type="file" @input="uploadConfirm"
                                                                                  ref="uploadRef" v-show="false"/></div>
      <div class="tool-item pointer-select" @click="onScreenShotClick">截屏</div>
      <div class="tool-item pointer-select" @click="onShowChatHistoryClick">聊天历史</div>
    </div>
    <div ref="editAreaRef" class="text-area" contenteditable="true" @compositionstart="onCompositionstart"
         @compositionend="onCompositionend" @beforeinput="onBeforeinput" @input="onTextAreaInput"
         @keydown="onTextAreaKeyDown"
         @keyup="onTextAreaKeyUp"></div>
    <div class="send">
      <button @click="onSendBtnClick" id="send-btn">发送</button>
    </div>
    <AtSearch @item-confirm="searchItemConfirm" :search-text="editAreaData.atSearchText" ref="atSearchRef"/>
    <KingDialog title="聊天历史" class="chat-history" ref="chatHistoryDialogRef">
      <div class="chat-list">
        <ChatBubble :key="msg.id" :msg="msg" v-for="msg in chatHistory.data"/>
      </div>
    </KingDialog>
  </div>
</template>

<style scoped lang="stylus">
.send-area
  position relative

  .tools
    padding 10px 10px
    display flex

    .tool-item
      padding 5px 10px

      &:hover
        background-color rgba(0, 0, 0, 0.2)

  .text-area
    margin-bottom 5px
    background-color rgba(255, 255, 255, 0)
    width 99%
    height 160px
    outline none
    border none
    line-height 1.5
    font-size 1em
    padding 5px 10px
    resize none
    white-space pre

    &:focus-within
      outline 2px solid rgba(0, 0, 0, 0.2)

  #send-btn
    position absolute
    bottom 20px
    right 10px
    width 100px
    padding 5px
    margin-right 10px

.chat-history
  width 900px
</style>