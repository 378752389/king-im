<script setup>
import {useUserStore} from "@/stores/user.js";
import {useRouter} from "vue-router";
import request from '@/http/request.js'
import {refresh} from "@/http/login.js";

const router = useRouter()
const userStore = useUserStore()

const onSubmit = async (e) => {
  e.preventDefault();
  let username = e.target.username.value;
  let password = e.target.password.value;

  if (await userStore.login(username, password)) {
    router.push('/chat')
  } else {
    // 登录失败
    console.log('登录失败');
  }
}


const getToken = async () => {
  await userStore.login('admin', '123')
  console.log("登录成功")
}

const refreshToken = async () => {
  const res = await refresh()
  console.log(res)
}

const insertMessage = () => {
}

</script>

<template>
  <form @submit="onSubmit" method="post">
    <label for="username">用户名</label>
    <input id="username" name="username"/>
    <label for="password">密码</label>
    <input id="password" name="password" type="password"/>
    <input type="submit" value="登录"/>
  </form>


  <button class="btn" @click="getToken">获取token</button>
  <button class="btn" @click="refreshToken">刷新token</button>
  <button class="btn" @click="insertMessage">插入消息</button>
</template>

<style scoped lang="stylus">
.btn
  margin 0 20px
</style>