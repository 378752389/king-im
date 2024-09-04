<script setup>

import {useUserStore} from "@/stores/user.js";
import {useVModel} from "@vueuse/core";
import KingUpload from "@/components/common/form/KingUpload.vue";
import {ossUploadAPI} from "@/http/oss.js";

const userStore = useUserStore()

const props = defineProps({
  groupForm: {}
})

const emits = defineEmits(['save', 'quit', 'delete', 'send'])

const formData = useVModel(props, 'groupForm')

const onSaveClick = () => {
  emits('save')
}
const onQuitClick = () => {
  emits('quit')
}
const onDeleteClick = () => {
  emits('delete')
}

const onSendClick = () => {
  emits('send', formData.value)
}

const onUploadGroupAvatar = async (e) => {
  let file = e.target.files[0]
  const downloadUrl = await ossUploadAPI({
    file,
    businessType: 2,
  })
  if (downloadUrl != null) {
    formData.value.avatar = downloadUrl;
  }
}
</script>

<template>
  <div class="group-form">
    <div class="profile">
<!--      <img alt="" :src="formData.avatar"/>-->
      <king-upload @upload="onUploadGroupAvatar" :url="formData.avatar" />
      <form class="group-detail-form">
        <div class="form-item">
          <label for="name">群名称</label>
          <input :disabled="formData.leaderId !== userStore.info?.id" type="text" id="name" name="name"
                 v-model="formData.name" class="king-input-text"/>
        </div>

        <div class="form-item">
          <label for="createBy">群主 </label>
          <input disabled type="text" id="createBy" name="createBy" v-model="formData.createBy" class="king-input-text"/>
        </div>

        <div class="form-item">
          <label for="remark">备注 </label>
          <input type="text" id="remark" name="remark" v-model="formData.markName" class="king-input-text"/>
        </div>

        <div class="form-item">
          <label for="myName">昵称 </label>
          <input type="text" id="myName" name="myName" v-model="formData.myName" class="king-input-text"/>
        </div>

        <div class="form-item">
          <label for="notice">公告 </label>
          <input :disabled="formData.leaderId !== userStore.info?.id" type="text" id="notice" class="king-input-text"
                 name="notice" v-model="formData.notice"/>
        </div>

        <div class="form-item" style="margin-top: 30px">
          <input type="button" @click="onSendClick" value="发消息"/>
          <input type="button" @click="onSaveClick" value="保存"/>
          <input v-if="formData.leaderId !== userStore.info?.id" type="button" @click="onQuitClick" value="退出群聊"/>
          <input v-else type="button" @click="onDeleteClick" value="删除群聊"/>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped lang="stylus">
.profile
  display flex
  justify-content space-around

  .king-upload
    width 160px
    height 160px
    margin 10px 80px 0 0
</style>