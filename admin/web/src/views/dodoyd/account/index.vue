<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card header="About me" shadow="always">
          <el-empty description="用户头像">
            <el-avatar slot="image" :size="150" :src="userInfo.avatar" />
          </el-empty>
          <el-descriptions :column="1" border label-class-name="LS">
            <el-descriptions-item label="注册手机号">{{ userInfo.mobile }}</el-descriptions-item>
            <el-descriptions-item label="昵称">{{ userInfo.nickname }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ userInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="性别">
              <el-tag v-if="userInfo.gender === 1" size="small"><i class="el-icon-male" /></el-tag>
              <el-tag v-else-if="userInfo.gender === 2" size="small" type="danger"><i class="el-icon-female" /></el-tag>
              <el-tag v-else size="small" type="info">保密</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card header="账号信息">
          <el-form ref="userForm" :model="userForm" :rules="userFormRules" label-width="100px">
            <el-form-item label="昵称" prop="nickname">
              <el-col :span="6">
                <el-input v-model="userForm.nickname" placeholder="请输入昵称" maxlength="10" show-word-limit />
              </el-col>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-col :span="6">
                <el-input v-model="userForm.name" placeholder="请输入姓名" maxlength="10" show-word-limit />
              </el-col>
            </el-form-item>
            <el-form-item label="性别">
              <el-col :span="8">
                <el-radio-group v-model="userForm.gender" prop="gender">
                  <el-radio-button :label="0">保密</el-radio-button>
                  <el-radio-button :label="1"><span><i class="el-icon-male" />男性</span></el-radio-button>
                  <el-radio-button :label="2"><span><i class="el-icon-female" />女性</span></el-radio-button>
                </el-radio-group>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="4">
                <el-button @click="resetForm('userForm')">重置</el-button>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="handleUpdateUserInfo('userForm')">保存</el-button>
              </el-col>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card header="修改密码">
          <el-form ref="pwdForm" :model="pwdForm" :rules="pwdFormRules" label-width="100px">
            <el-form-item label="原密码" prop="oldPwd">
              <el-col :span="6">
                <el-input v-model="pwdForm.oldPwd" placeholder="请输入原密码" show-password />
              </el-col>
            </el-form-item>
            <el-form-item label="新密码" prop="newPwd">
              <el-col :span="6">
                <el-input v-model="pwdForm.newPwd" placeholder="请输入新密码，最少6个字符" show-password />
              </el-col>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPwd">
              <el-col :span="6">
                <el-input v-model="pwdForm.checkPwd" placeholder="请在此输入新密码" show-password />
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="4">
                <el-button @click="resetForm('pwdForm')">重置</el-button>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="handleChangePwd('pwdForm')">提交</el-button>
              </el-col>
            </el-form-item>
          </el-form>
        </el-card>
        <!-- 全注释掉
        <el-card header="第三方账号">
          <el-row>
            <el-col :span="4" :offset="1">
              <div class="wx-svg-container">
                <span style="padding-right: 5px"><svg-icon icon-class="wechat" class="svg-icon" /></span>
                <span>微信</span>
              </div>
            </el-col>
            <el-col :span="4">
              <el-tag>已关联</el-tag>
            </el-col>
            <el-col :span="4">
              <el-tag type="info" style="cursor: pointer;" @click="wxConnect()"><i class="el-icon-paperclip" /> 取消关联</el-tag>
            </el-col>
          </el-row>
          <el-divider />
          <el-row>
            <el-col :span="4" :offset="1">
              <div class="wx-svg-container">
                <span style="padding-right: 5px"><svg-icon icon-class="wechat" class="svg-icon" /></span>
                <span>微信</span>
              </div>
            </el-col>
            <el-col :span="4">
              <el-tag type="info">未关联</el-tag>
            </el-col>
            <el-col :span="4">
              <el-tag style="cursor: pointer;" @click="wxConnect()"><i class="el-icon-paperclip" /> 关联</el-tag>
            </el-col>
          </el-row>
          <el-divider />
        </el-card>
        -->
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserInfo, updateUserInfo, changePassword } from '@/api/user'

export default {
  name: 'AccountInfo',
  components: { },
  data() {
    const checkNickname = (rule, value, callback) => {
      if (!value) {
        callback(new Error('昵称不能为空'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    const checkPwd = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'))
      }
      // 使用正则表达式进行验证手机号码
      if (value.length < 6) {
        callback(new Error('密码最少6个字符'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    return {
      userInfo: {},
      userForm: {
        nickname: '',
        name: '',
        gender: 0
      },
      pwdForm: {
        oldPwd: '',
        newPwd: '',
        checkPwd: ''
      },
      userFormRules: {
        nickname: [{ validator: checkNickname, trigger: 'blur' }]
      },
      pwdFormRules: {
        oldPwd: [{ validator: checkPwd, trigger: 'blur' }],
        newPwd: [{ validator: checkPwd, trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    // 获账号信息
    getUserInfo() {
      getUserInfo().then(response => {
        this.userInfo = response.data
        this.userForm.nickname = this.userInfo.nickname
        this.userForm.name = this.userInfo.name
        this.userForm.gender = this.userInfo.gender
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      if (formName === 'userForm') {
        this.userForm.nickname = this.userInfo.nickname
        this.userForm.gender = this.userInfo.gender
      }
    },
    handleUpdateUserInfo(formName) {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          // 没变化则直接提示成功
          if (this.userForm.nickname === this.userInfo.nickname && this.userForm.name === this.userInfo.name && this.userForm.gender === this.userInfo.gender) {
            this.$message({ showClose: true, message: '成功', type: 'success' })
            return false
          }

          updateUserInfo(this.userForm).then(response => {
            if (response.code === 0) {
              this.getUserInfo()
            }
            this.$message({ showClose: true, message: response.message, type: response.code === 0 ? 'success' : 'error' })
          }).catch(err => {
            console.log(err)
          })
        } else {
          return false
        }
      })
    },
    handleChangePwd(formName) {
      this.$refs.pwdForm.validate(valid => {
        if (valid) {
          // 没变化则直接提示成功
          if (this.pwdForm.newPwd !== this.pwdForm.checkPwd) {
            this.$message({ showClose: true, message: '两次输入的新密码不一致', type: 'error' })
            return false
          }

          changePassword(this.pwdForm).then(response => {
            if (response.code === 0) {
              this.$message({ showClose: true, message: response.message, type: response.code === 0 ? 'success' : 'error' })
            }
          }).catch(err => {
            console.log(err)
          })
        } else {
          return false
        }
      })
    },
    wxConnect() {
      console.log('wxConnect')
    }
  }
}
</script>

<style lang="scss" scoped>

.wx-svg-container {
  display: flex;
  align-items: center;
}

.svg-icon {
  color: #24da70;
  font-size: 24px;
  line-height: 40px;
}

.LS {
  width: 100px;
}

</style>
