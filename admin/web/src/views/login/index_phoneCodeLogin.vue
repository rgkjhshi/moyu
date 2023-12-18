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
            <el-form ref="mobileForm" :model="mobileForm" :rules="mobileRules" label-width="60px">
              <el-form-item label="手机号" prop="phoneNumber">
                <el-col :span="22">
                  <el-input v-model="mobileForm.phoneNumber" placeholder="请输入手机号" maxlength="11">
                    <template slot="prepend">+86</template>
                  </el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="验证码" prop="verCode">
                <el-col :span="22">
                  <el-input v-model="mobileForm.verCode" placeholder="请输入验证码">
                    <el-button slot="append" :disabled="codeCd" :loading="codeCd" @click="verCodeClick('mobileForm')">
                      <span v-if="codeCd">重新发送({{ codeCdCount }})</span>
                      <span v-else>获取验证码</span>
                    </el-button>
                  </el-input>
                </el-col>
              </el-form-item>
              <el-form-item>
                <el-col :span="22">
                  <el-button type="primary" style="width: 100%" @click="handleLogin('mobileForm')">登录</el-button>
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

export default {
  name: 'Login2',
  components: { },

  data() {
    const checkPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请填写手机号'))
      }
      // 使用正则表达式进行验证手机号码
      if (!/^1[3456789]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    const checkVerCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请填写短信验证码'))
      }
      // 使用正则表达式进行验证手机号码
      if (!/\d{6}/.test(value)) {
        callback(new Error('请输入正确短信验证码'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    return {
      loginPng: loginPng + '?' + +new Date(),
      mobileForm: {
        phoneNumber: '',
        verCode: ''
      },
      codeCd: false,
      codeCdCount: 60,
      dialogVisible: false,
      verifySuccess: false,
      redirect: undefined,
      otherQuery: {},
      mobileRules: {
        phoneNumber: [{ validator: checkPhone, trigger: 'blur' }],
        verCode: [{ validator: checkVerCode, trigger: 'blur' }]
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
  },
  methods: {
    verCodeClick(formName) {
      // 进行手机号校验需要使用到validateField对部分表单字段进行校验，valid是校验完的提示信息，当valid为空时代表校验成功
      this.$refs.mobileForm.validateField('phoneNumber', async(valid) => {
        if (!valid) {
          // 打开滑块验证
          if (!this.verifySuccess) {
            this.dialogVisible = true
            return
          }
          // 获取验证码 https://www.cnblogs.com/ushen/p/16703614.html
          console.log('to sendVerCode')
          // 开启计时
          this.codeCd = true
          const timer = setInterval(() => {
            this.codeCdCount--
            if (this.codeCdCount <= 0) {
              this.codeCdCount = 60
              this.codeCd = false
              clearInterval(timer)
            }
          }, 1000)
        } else {
          return false
        }
        // 重置滑块验证
        if (this.verifySuccess) {
          this.verifySuccess = false
          // 刷新
          this.$refs.slideblock.reset()
        }
      })
    },
    handleLogin(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.loading = true
          var username = this.mobileForm.phoneNumber
          var password = this.mobileForm.password
          this.$store.dispatch('user/login', { username, password })
            .then(() => {
              this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
              this.loading = false
            })
            .catch(() => {
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
      this.verifySuccess = true
      this.verCodeClick()
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
