<template>
  <div class="app-container">
    <el-row type="flex" align="middle">
      <el-col :offset="2" :span="12">
        <el-image fit="contain" :src="loginPng" class="image"/>
      </el-col>
      <el-col :offset="1" :span="10">
        <div class="login-container">
          <el-card>
            <div slot="header" class="login-title">
              <div>MY管理系统</div>
            </div>
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" placeholder="账号" prefix-icon="el-icon-user" type="text"
                          auto-complete="off"/>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" placeholder="密码" prefix-icon="el-icon-lock" show-password
                          type="password"/>
              </el-form-item>
              <el-form-item prop="rememberMe">
                <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
              </el-form-item>
              <el-form-item style="width:100%;">
                <el-button :loading="loading" type="primary" style="width:100%" @click="handleLogin">登录</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import loginPng from '@/assets/login_images/login.png'
import Cookies from 'js-cookie'
import {encrypt, decrypt} from '@/utils/jsencrypt'

export default {
  name: 'Login',
  components: {},

  data() {
    return {
      loginPng: loginPng + '?' + +new Date(),
      loginForm: {
        username: 'admin',
        password: '111111',
        rememberMe: false,
      },
      loginRules: {
        username: [{required: true, trigger: "blur", message: "请输入您的账号"}],
        password: [{required: true, trigger: "blur", message: "请输入您的密码"}],
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    }
  },

  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
    this.getCode()
    this.getCookie()
  },
  methods: {
    // 获取验证码
    getCode() {
      // getCodeImg().then(res => {
      //   this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
      //   if (this.captchaEnabled) {
      //     this.codeUrl = "data:image/gif;base64," + res.img;
      //     this.loginForm.uuid = res.uuid;
      //   }
      // })
    },
    // 获取cookie数据
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)

      }
    },
    // 登陆
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, {expires: 30});
            Cookies.set("password", encrypt(this.loginForm.password), {expires: 30});
            Cookies.set('rememberMe', this.loginForm.rememberMe, {expires: 30});
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          // 会到src/store/modules/user.js中请求login方法。
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode();
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      });
    }
  }
}
</script>

<style>

.login-container {
  width: 400px;
}

.login-title {
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}
</style>
