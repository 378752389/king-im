<script setup>
import {onMounted, reactive} from "vue";
import {useRouter} from "vue-router";
import KingUpload from "@/components/common/form/KingUpload.vue";
import {useUserStore} from "@/stores/user.js";
import {modifyUserInfoAPI} from "@/http/user.js";
import {ShowToast} from "@/components/common/func/toast.js";
import {ossUploadAPI} from "@/http/oss.js";

const profileData = reactive({
  username: '',
  nickName: '',
  avatar: '',
  sign: '',
  gender: 1,
})

const userStore = useUserStore()
onMounted(async () => {
  await useUserStore().loadInfo()
  profileData.username = userStore.info.username
  profileData.nickName = userStore.info.nickName
  profileData.avatar = userStore.info.avatar
  profileData.sign = userStore.info.sign
  profileData.gender = userStore.info.gender
})

const router = useRouter()

const confirm = async () => {
  await modifyUserInfoAPI({
    username: profileData.username,
    nickName: profileData.nickName,
    avatar: profileData.avatar,
    gender: profileData.gender,
    sign: profileData.sign,
  })
  ShowToast({
    message: "修改个人信息成功",
    timeout: 3000,
    type: 'success'
  })
  await userStore.loadInfo()
  router.back()
}

const cancel = () => {
  router.back()
}

const onUpload = async (e) => {
  let file = e.target.files[0]
  if (file.type.startsWith("image")) {
    let downloadUrl = await ossUploadAPI({
      file,
      businessType: 1,
    })
    profileData.avatar = downloadUrl
  }
}
</script>

<template>
  <div class="profile">
    <h2>编辑个人信息</h2>
    <form class="user-form">
      <div class="form-item">
        <label for="username">用户名</label>
        <input id="username" v-model="profileData.username" disabled type="text" class="king-input-text" name="username"/>
      </div>
      <div class="form-item">
        <label for="nickname">昵称</label>
        <input id="nickname" class="king-input-text" v-model="profileData.nickName" type="text" name="nickname"/>
      </div>
      <div class="form-item">
        <label>性别</label>
        <label for="male" style="width: fit-content;">男</label>
        <input id="male" class="king-input-radio" v-model="profileData.gender" value="1" type="radio" name="gender"/>
        <label for="female" style="width: fit-content;">女</label>
        <input id="female" class="king-input-radio" v-model="profileData.gender" value="2" type="radio" name="gender"/>
      </div>
      <div class="form-item" style="align-items: flex-start;">
        <label>头像</label>
        <king-upload :url="profileData.avatar" @upload="onUpload"/>
      </div>
      <div class="form-item">
        <label for="confirm-password">签名</label>
        <input id="sign" v-model="profileData.sign" name="sign" class="king-input-text"
               type="text"/>
      </div>
      <div class="form-item">
        <!--        <label for="register"></label>-->
        <button name="confirm" type="button" class="confirm-btn" @click="confirm">确认</button>
        <button name="confirm" type="button" class="confirm-btn" @click="cancel">取消</button>
      </div>
    </form>
  </div>
</template>

<style scoped lang="stylus">
.profile
  width 800px
  margin auto
  padding 20px 0

  h2
    margin-bottom 50px

  .user-form
    .form-item
      justify-content flex-start
      align-items center

      label
        width 70px

      .king-input-text
        flex 1

    button
      padding 10px
      flex 1
      margin 30px 100px
</style>