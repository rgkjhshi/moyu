<template>
  <div class="app-container">
    <el-card class="tip">
      <span class="tip-content">
        场地状态为暂停使用时，不可预订
      </span>
    </el-card>
    <el-card header="修改场地">
      <el-form ref="groundForm" :model="groundForm" :rules="rules" label-width="100px">
        <el-form-item label="场地ID">
          <el-col :span="6">
            <el-tag>{{ groundForm.groundId }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="场地名称" prop="groundName">
          <el-col :span="4">
            <el-input v-model="groundForm.groundName" placeholder="如：1号场" maxlength="10" />
          </el-col>
        </el-form-item>
        <el-form-item label="场地类型:" prop="groundType">
          <el-col :span="4">
            <el-select v-model="groundForm.groundType">
              <el-option v-for="item in groundFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="配置编号" prop="configNo">
          <el-col :span="6">
            <el-input v-model="groundForm.configNo" placeholder="价格配置编号" />
          </el-col>
        </el-form-item>
        <el-form-item label="价格" prop="groundPrice">
          <el-col :span="4">
            <el-input v-model="groundForm.groundPrice" placeholder="普通时段价格">
              <template slot="append">元</template>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="场地状态" prop="venueStatus">
          <el-col :span="6">
            <el-radio-group v-model="groundForm.groundStatus">
              <el-radio-button v-if="isAdmin" :label="0">未上线</el-radio-button>
              <el-radio-button :label="1">正常使用</el-radio-button>
              <el-radio-button :label="2">暂停使用</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="4">
            <el-button type="primary" style="width: 100%" @click="handleUpdateGround()">保存</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getGroundInfo, updateGround } from '@/api/dodoyd'

export default {
  name: 'EditGround',
  components: { },
  data() {
    const checkPrice = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请填写价格'))
      }
      // 使用正则表达式进行验证手机号码
      if (!/^[1-9]\d*$/.test(value)) {
        callback(new Error('请输入正确的数字'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    return {
      groundId: null,
      groundForm: {},
      rules: {
        groundName: [{ required: true, message: '请选填写配置内容', trigger: 'blur' }],
        groundPrice: [{ required: true, validator: checkPrice, trigger: 'blur' }],
        configNo: [{ required: true, message: '请选填写配置编号', trigger: 'blur' }]
      },
      // 0:全部,1:网球,2:羽毛球,3:篮球,4:足球
      groundFilterOptions: [
        { key: 1, name: '网球' },
        { key: 2, name: '羽毛球' },
        { key: 3, name: '篮球' },
        { key: 4, name: '足球' }
      ]
    }
  },
  computed: {
    venueId() {
      let venueId = this.$store.getters.venueId
      if (!venueId) {
        venueId = localStorage.getItem('venueId')
      }
      return venueId
    },
    isAdmin() {
      const roles = this.$store.getters.roles
      return roles && roles.length > 0 && roles.indexOf('admin') > -1
    }
  },
  created() {
    this.groundId = this.$route.query.groundId
    this.getData()
  },
  methods: {
    // 获取信息
    getData() {
      getGroundInfo({ venueId: this.venueId, groundId: this.groundId }).then(response => {
        if (response.code === 0) {
          this.groundForm = response.data
        }
      }).catch(err => {
        console.log(err)
      })
    },
    handleUpdateGround() {
      this.$refs.groundForm.validate(valid => {
        if (valid) {
          updateGround(this.groundForm).then(response => {
            this.$message({
              showClose: true,
              message: response.message,
              type: response.code === 0 ? 'success' : 'error'
            })
            if (response.code === 0) {
              this.$router.push({ path: '/sysAdmin/groundList' })
            }
          }).catch(err => {
            console.log(err)
          })
        } else {
          return false
        }
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

</style>
