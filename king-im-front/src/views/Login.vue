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
        <input id="username" v-model="loginData.username" class="king-input-text" type="text" name="username"/>
      </div>
      <div class="form-item">
        <label for="password">密码</label>
        <input id="password" v-model="loginData.password" name="password" class="king-input-text" type="password"/>
      </div>
      <div class="form-item">
        <label></label>
        <button type="button" class="login-btn"  @click.prevent="onLogin">登录</button>
      </div>

      <router-link class="register-link" to="/register">用户注册</router-link>
    </form>

    <div class="banner">
      <h2>系统公告</h2>
      <ul>
        <li>
          本项目为仿微信IM桌面端应用；项目底层采用netty框架搭建，使用websocket进行消息订阅，使用redis做全局会话管理，支持多实例部署，
          如需查看项目源码，请点击<a style="margin-left: 10px; color: #0086b3;" href="" target="_blank">🔗链接</a>,
          如果感兴趣欢迎 star；
        </li>
        <li>
          <div style="margin-bottom: 10px">提供以下账号供测试使用，也可自行注册；</div>
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

    label
      width 70px

    .login-btn
      padding 10px
      flex 1

    .register-link
      align-self flex-end

  .banner
    width 500px
    background transparent
    margin auto
    justify-self flex-end

.banner
  border 1px solid rgba(128, 128, 128, 0.8)
  padding 20px
  font-size 16px
  letter-spacing 2px
  line-height 1.8

  h2
    margin-bottom 20px

  li
    margin-left 20px
    padding 3px
</style>