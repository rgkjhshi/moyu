<template>
  <div class="app-container">
    <el-card class="tip">
      <span class="tip-content">
        通过手机号查询之后，才可添加。用户不存在时，添加后将自动注册<br>
      </span>
    </el-card>
    <el-card header="添加成员">
      <el-form ref="mobileForm" label-width="100px">
        <el-form-item label="场馆名称">
          <el-col :span="6">
            <el-tag>{{ venueInfo.venueName }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="成员角色">
          <el-col :span="6">
            <el-radio-group v-model="userRole" prop="gender">
              <el-radio-button :label="2">店员</el-radio-button>
              <el-radio-button :label="3">教练</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item label="成员">
          <el-col :span="4">
            <div v-if="userInfo" class="user-box">
              <el-avatar :src="userInfo.avatar">{{ userInfo.nickname }}</el-avatar>
              <div class="user-title">{{ userInfo.nickname }}</div>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-col :span="4">
            <el-input v-model="mobile" placeholder="请输入被添加人的手机号" maxlength="11" show-word-limit @change="onChange()" />
          </el-col>
          <el-col :offset="1" :span="2">
            <el-button type="primary" icon="el-icon-search" @click="clickQueryUser">
              查询
            </el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="6">
            <el-input v-model="remark" placeholder="备注" maxlength="20" />
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="4">
            <el-button type="primary" :disabled="!hasQuery" @click="handleAddVenueUser()">添加</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getVenueInfo, addVenueUser } from '@/api/dodoyd'
import { getInfoByMobile } from '@/api/user'

export default {
  name: 'AddStaff',
  components: { },
  data() {
    return {
      venueInfo: {},
      userInfo: null,
      userRole: null,
      mobile: '',
      remark: '',
      hasQuery: false
    }
  },
  computed: {
    venueId() {
      let venueId = this.$store.getters.venueId
      if (!venueId) {
        venueId = localStorage.getItem('venueId')
      }
      return venueId
    }
  },
  created() {
    this.getData()
  },
  methods: {
    // 获取信息
    getData() {
      getVenueInfo(this.venueId).then(response => {
        if (response.code === 0) {
          this.venueInfo = response.data
        }
      })
    },
    // 手机号变更
    onChange() {
      this.hasQuery = false
    },
    // 查询用户
    clickQueryUser() {
      // 先检查手机号
      if (!/^1[3456789]\d{9}$/.test(this.mobile)) {
        this.$message({ showClose: true, message: '请输入正确的手机号', type: 'error' })
        this.userInfo = null
        return
      }
      getInfoByMobile(this.mobile).then(response => {
        if (response.code === 0) {
          this.userInfo = response.data
        } else {
          this.userInfo = null
        }
      }).catch(() => {
        this.userInfo = null
        console.log('此手机号未注册，添加之后将会自动注册')
      })
      this.hasQuery = true
    },
    handleAddVenueUser() {
      // 先检查手机号
      if (!this.hasQuery) {
        this.$message({ showClose: true, message: '先通过手机号查询后再添加', type: 'error' })
        this.userInfo = null
        return
      }
      if (!this.userRole) {
        this.$message({ showClose: true, message: '请填选择角色', type: 'error' })
        return
      }
      addVenueUser({
        venueId: this.venueId,
        addMobile: this.mobile,
        userRole: this.userRole,
        remark: this.remark
      }).then(response => {
        this.$message({
          showClose: true,
          message: response.message,
          type: response.code === 0 ? 'success' : 'error'
        })
        this.$router.push({ path: '/staff/staffList' })
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.tip {
  background-color: #ecf8ff;
  border-radius: 4px;
  border-left: 5px solid #50bfff;
}
.tip-error {
  background-color: #fff6f7;
  border-radius: 4px;
  border-left: 5px solid #fe6c6f;
}
.tip-content {
  font-size: 14px;
  color: #5e6d82;
  line-height: 1.5em;
}

.user-box {
  display: flex;
}
.user-title {
  padding-left: 10px;
  height: 40px;
  line-height: 40px;
  font-size: 16px;
}
</style>
