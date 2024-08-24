<script setup>

import {reactive} from "vue";
import {ShowToast} from "@/components/common/func/toast.js";
import {registerAPI} from "@/http/login.js";
import {useRouter} from "vue-router";

const registerData = reactive({
  email: '',
  username: '',
  password: '',
  confirmPassword: '',
})

const router = useRouter()

const onRegister = async () => {
  if (registerData.username === '' || registerData.password === '') {
    ShowToast({
      message: "用户名或密码不能为空",
      timeout: 3000,
      type: 'danger'
    })
  }

  if (registerData.password !== registerData.confirmPassword) {
    ShowToast({
      message: "确认密码不一致",
      timeout: 3000,
      type: 'danger'
    })
  }

  let resp;
  try {
    resp = await registerAPI({
      email: registerData.email,
      username: registerData.username,
      password: registerData.password,
    })
    
    ShowToast({
      message: `注册成功，跳转到登录页`,
      timeout: 3000,
      type: 'success'
    })
    // 登录成功，跳转到登录页
    router.push({
      name: 'login'
    })
  } catch (e) {
    console.error(e)
    ShowToast({
      message: `注册失败: ${resp}`,
      timeout: 3000,
      type: 'danger'
    })
  }

}
</script>

<template>
  <div class="register">
    <form class="register-form">
      <div class="form-item">
        <label for="email">邮箱</label>
        <input id="email" v-model="registerData.email" type="email" class="king-input-text" name="email"/>
      </div>
      <div class="form-item">
        <label for="username">用户名</label>
        <input id="username" v-model="registerData.username" type="text" name="username"/>
      </div>
      <div class="form-item">
        <label for="password">密码</label>
        <input id="password" v-model="registerData.password" name="password" class="king-input-text" type="password"/>
      </div>
      <div class="form-item">
        <label for="confirm-password">密码</label>
        <input id="password" v-model="registerData.confirmPassword" name="confirm-password" class="king-input-text"
               type="password"/>
      </div>
      <div class="form-item">
        <label for="register"></label>
        <button name="register" type="button" class="register-btn" @click="onRegister">注册</button>
      </div>
    </form>
  </div>
</template>

<style scoped lang="stylus">
.register
  width 100vw;
  height 100vh;
  background-image url("@/assets/bg.jpg")
  overflow hidden;

  .register-form
    margin 100px auto
    display flex
    flex-direction column

    .register-btn
      padding 10px
      margin 20px 0
      width 500px
      align-self flex-end
</style>