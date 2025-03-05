import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import Antd from 'ant-design-vue'
import i18n from "@/locale"
import App from './App.vue'

// style
import 'ant-design-vue/dist/reset.css'
import '@/style/index.less'
import * as antdvIcons from '@ant-design/icons-vue'

const app = createApp(App);
app.use(createPinia())
app.use(router)
app.use(Antd)
app.use(i18n)

// 统一注册antdv图标
for (const icon in antdvIcons) {
  app.component(icon, antdvIcons[icon])
}

// 挂载app
app.mount('#app')
