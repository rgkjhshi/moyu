<template>
  <div class="app-container">
    <el-card header="添加规则配置">
      <el-form ref="configForm" :model="configForm" :rules="rules" label-width="120px">
        <el-form-item label="当前场馆">
          <el-col :span="6">
            <el-tag>{{ venueInfo.venueName }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="配置名称" prop="configName">
          <el-col :span="9">
            <el-input v-model="configForm.configName" placeholder="请输入配置名称" maxlength="30" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item label="配置类型:" prop="configType">
          <el-col :span="4">
            <el-select v-model="configForm.configType">
              <el-option v-for="item in configFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="配置规则" prop="configRule">
          <el-col :span="9">
            <el-input v-model="configForm.configRule" type="textarea" rows="10" maxlength="1024" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="9">
            <el-button type="primary" style="width: 100%" @click="handleAdd()">添加</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getVenueInfo, addRuleConfig } from '@/api/dodoyd'

export default {
  name: 'AddRuleConfig',
  components: { },
  data() {
    return {
      venueInfo: {},
      configForm: {
        configName: '',
        configType: 1,
        configRule: null
      },
      rules: {
        configName: [{ required: true, message: '请选填写配置名称', trigger: 'blur' }],
        configType: [{ required: true, message: '请选选择配置类型', trigger: 'blur' }],
        configRule: [{ required: true, message: '请选填写配置内容', trigger: 'blur' }]
      },
      // 0:全部,1:场地配置
      configFilterOptions: [
        { key: 1, name: '场地配置' }
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
      }).catch(err => {
        console.log(err)
      })
    },
    handleAdd() {
      this.configForm.venueId = this.venueId
      this.$refs.configForm.validate(valid => {
        if (valid) {
          // 检查通过之后则添加
          addRuleConfig(this.configForm).then(response => {
            this.$message({
              showClose: true,
              message: response.message,
              type: response.code === 0 ? 'success' : 'error'
            })
            if (response.code === 0) {
              this.$router.push({ path: '/sysAdmin/ruleConfigList' })
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
