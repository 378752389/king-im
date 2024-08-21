<script setup>

import {sendAPI} from "@/http/message.js";
import {computed, reactive, ref} from "vue";
import {useChatsStore} from "@/stores/chats.js";
import KingDialog from "@/components/common/KingDialog.vue";
import AtSearch from "@/views/chat/components/AtSearch.vue";
import ChatHistory from "@/views/chat/components/ChatHistory.vue";
import {useGroupsStore} from "@/stores/groups.js";
import {ShowToast} from "@/components/common/func/toast.js";

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

const onShowEmojiClick = () => {
  ShowToast({
    message: "该功能还在开发中...",
    timeout: 3000,
  })
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
const onShowChatHistoryClick = async () => {
  chatHistoryRef.value.open()
}

const chatHistoryRef = ref()
const atSearchRef = ref()

const onEditAreaInput = (e) => {
  if (!editAreaData.inCompositionEdit) {
    // 群聊打开 at功能
    if (e.data === '@' && chatStore.currentChatTypeGetter === 2) {
      let sel = document.getSelection()
      let range = sel.getRangeAt(0)
      editAreaData.focusNode = sel.focusNode
      editAreaData.focusOffset = sel.focusOffset
      editAreaData.atSearchText = ''
      let rect = range.getBoundingClientRect();
      atSearchRef.value.open({x: rect.x + 'px', y: rect.y + 'px'})
    } else {
      let sel = document.getSelection()
      editAreaData.focusNode = sel.focusNode
      editAreaData.focusOffset = sel.focusOffset
      let atIdx = editAreaData.focusNode.textContent.indexOf('@')
      if (atIdx !== -1) {
        editAreaData.atSearchText = editAreaData.focusNode.textContent.substring(atIdx + 1)
        console.log("搜索文本：", editAreaData.atSearchText)
      }
    }
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
  onEditAreaInput(e)
}

const searchItemConfirm = (item) => {
  let sel = document.getSelection()
  let range = sel.getRangeAt(0)
  range.setStart(editAreaData.focusNode, editAreaData.focusOffset - 1 - editAreaData.atSearchText.length)
  range.setEnd(editAreaData.focusNode, editAreaData.focusOffset)
  range.deleteContents()
  range.collapse()
  // 插入 span标签，表示引用用户
  let span = document.createElement("span")
  span.dataset.atUid = item.id
  span.classList.add('at')
  span.contentEditable = 'false'
  span.innerText = `@${item.name}`
  range.insertNode(span)
  range.collapse()
  // 不需要选择at对象，打开下面代码
  range.insertNode(document.createTextNode("\u00A0"))
  range.collapse()
  editAreaRef.value.focus()
  atSearchRef.value.close()

  editAreaData.atSearchText = ''
}

const onEditAreaBlur = () => {
  let selection = window.getSelection()
  editAreaData.focusNode = selection.focusNode
  editAreaData.focusOffset = selection.focusOffset
}

const groupStore = useGroupsStore()

const searchList = computed(() => {
  if (chatStore.currentChatTypeGetter === 2) {
    let group = groupStore.getGroup(chatStore.currentChatIdGetter)
    if (group == null || group.roomMemberList == null) {
      return []
    }
    if (editAreaData.atSearchText === '' || editAreaData.atSearchText == null) {
      return group.roomMemberList
    } else {
      return group.roomMemberList.filter(member => member.name.startsWith(editAreaData.atSearchText))
    }
  }
})

const onUpKeydown = () => {
  if (atSearchRef.value.isOpen()) {
    atSearchRef.value.moveUp()
  }
}
const onDownKeydown = () => {
  if (atSearchRef.value.isOpen()) {
    atSearchRef.value.moveDown();
  }
}

const onEnterKeydown = (e) => {
  e.preventDefault()
  if (atSearchRef.value.isOpen()) {
    let item = atSearchRef.value.atItemGetter()
    searchItemConfirm(item)
  } else {
    onSendBtnClick()
  }
}

const onEscKeydown = () => {
  if (atSearchRef.value.isOpen()) {
    atSearchRef.value.close()
  }
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
    <div ref="editAreaRef" class="text-area" contenteditable="true"
         @compositionstart="onCompositionstart"
         @compositionend="onCompositionend"
         @keydown.up.prevent="onUpKeydown"
         @keydown.down.prevent="onDownKeydown"
         @keydown.enter.prevent="onEnterKeydown"
         @keydown.esc.prevent="onEscKeydown"
         @input="onEditAreaInput"
         @blur="onEditAreaBlur"
    ></div>
    <div class="send">
      <button @click="onSendBtnClick" id="send-btn">发送</button>
    </div>
    <AtSearch @confirm="searchItemConfirm" :text="editAreaData.atSearchText" :list="searchList" ref="atSearchRef"/>
    <ChatHistory ref="chatHistoryRef" :chat-id="chatStore.currentChatIdGetter" :chat-type="chatStore.currentChatTypeGetter"/>
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
    overflow scroll

    &:focus-within
      outline 2px solid rgba(0, 0, 0, 0.2)

  #send-btn
    position absolute
    bottom 20px
    right 10px
    width 100px
    padding 5px
    margin-right 10px
</style>