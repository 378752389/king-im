<script setup>
import {useUserStore} from "@/stores/user.js";
import {useRouter} from "vue-router";
import {ShowToast} from "@/components/common/func/toast.js";
import {reactive} from "vue";

const router = useRouter()
const userStore = useUserStore()
const loginData = reactive({
  username: '',
  password: '',
  terminal: 1,
})

const onLogin = async (e) => {
  let username = loginData.username;
  let password = loginData.password;
  let terminal = loginData.terminal;

  if (await userStore.login(username, password, terminal)) {
    router.push('/chat')
  } else {
    // 登录失败
    ShowToast({
      message: "登录失败",
      timeout: 3000,
      type: 'danger'
    })
  }
}
</script>

<template>
  <div class="login">
    <form class="login-form">
      <div class="form-item">
        <label for="username">用户名</label>
        <input id="username" v-model="loginData.username" type="text" name="username"/>
      </div>
      <div class="form-item">
        <label for="password">密码</label>
        <input id="password" v-model="loginData.password" name="password" class="king-input-text" type="password"/>
      </div>
      <button type="button" class="login-btn" @click.prevent="onLogin">登录</button>
    </form>

    <div class="banner">
      <h2>系统公告</h2>
      <ul>
        <li>
          如需查看项目源码，请点击<a style="margin-left: 10px; color: #0086b3;" href="" target="_blank">🔗链接</a>, 如果感兴趣欢迎 star；
        </li>
        <li>
          <div style="margin-bottom: 10px">提供以下测试账号供测试使用，也可自行注册；</div>
          <ol>
            <li>ew / 123</li>
            <li>men / 123</li>
            <li>jc / 123</li>
          </ol>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped lang="stylus">
.login
  width 100vw
  height 100vh
  background url("@/assets/bg.jpg")
  display flex
  flex-direction row-reverse
  justify-content flex-start
  align-items center


  .login-form
    width 500px
    padding 20px
    display flex
    flex-direction column
    border 1px solid rgba(128, 128, 128, 0.8)
    margin 50px

    .login-btn
      padding 10px
      margin 20px 0 0
      width 83%
      align-self flex-end

  .banner
    width 500px
    height 300px
    background transparent
    margin auto
    justify-self flex-end

.banner
  border 1px solid rgba(128, 128, 128, 0.8)
  padding 20px
  h2
    margin-bottom 30px

  li
    margin-left 20px
    padding 3px
</style>