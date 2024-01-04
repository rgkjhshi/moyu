<template>
  <div class="app-container">
    <el-row type="flex" align="middle">
      <el-col :offset="2" :span="12">
        <el-image fit="contain" :src="loginPng" class="image" />
      </el-col>
      <el-col :offset="1" :span="10">
        <div class="login-container">
          <el-card>
            <div slot="header" class="login-title">
              <div>请登录</div>
            </div>
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="60px">
              <el-form-item label="用户名" prop="username">
                <el-col :span="22">
                  <el-input v-model="loginForm.username" placeholder="请输入手机号" maxlength="11" />
                </el-col>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-col :span="22">
                  <el-input v-model="loginForm.password" placeholder="请输入密码" show-password />
                </el-col>
              </el-form-item>
              <el-form-item prop="rememberMe">
                <el-col :span="22">
                  <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
                </el-col>
              </el-form-item>
              <el-form-item>
                <el-col :span="22">
                  <el-button type="primary" style="width: 100%" @click="loginClick('loginForm')">登录</el-button>
                </el-col>
              </el-form-item>
            </el-form>
          </el-card>
          <el-dialog :center="true" title="请完成验证" width="400px" :visible.sync="dialogVisible" :before-close="dialogBeforeClose" :show-close="false">
            <el-card>
              <slide-verify ref="slideblock" slider-text="请向右滑动完成验证" @success="onSuccess" />
            </el-card>
          </el-dialog>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import loginPng from '@/assets/login_images/login.png'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  components: { },

  data() {
    const checkRequire = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不可为空'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    return {
      loginPng: loginPng + '?' + +new Date(),
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        verCode: ''
      },
      codeCd: false,
      codeCdCount: 60,
      dialogVisible: false,
      verifySuccess: false,
      redirect: undefined,
      otherQuery: {},
      loginRules: {
        username: [{ validator: checkRequire, trigger: 'blur' }],
        password: [{ validator: checkRequire, trigger: 'blur' }]
      }
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
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
    loginClick(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 打开滑块验证
          this.dialogVisible = true
        } else {
          return false
        }
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.loading = true
          var username = this.loginForm.username
          var password = this.loginForm.password
          this.$store.dispatch('user/login', { username, password }).then(() => {
            this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    },
    dialogBeforeClose() {
      this.dialogVisible = false
    },
    onSuccess() {
      this.dialogVisible = false
      this.handleLogin()
      // 重置滑块验证
      this.$refs.slideblock.reset()
    }
  }
}
</script>

<style>
/* tab内容的样式 */
.tab {
  width: 60px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* tab的高度自适应 */
.el-tabs__item {
  height: 100%;
}

.image {

}
.login-container {
  width: 400px;
  height: 500px;
}
.login-title {
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}
</style>
