import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ppp from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'
import './permission.js'
import './styles/global.styl'
import './assets/iconfont/iconfont.css'

const app = createApp(App)

app.use(createPinia().use(ppp))
app.use(router)

app.mount('#app')
