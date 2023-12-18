<template>
  <div class="app-container">
    <el-card header="制卡">
      <el-form ref="cardForm" :model="cardForm" :rules="rules" label-width="120px">
        <el-form-item label="当前场馆">
          <el-col :span="6">
            <el-tag>{{ venueInfo.venueName }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="卡名称" prop="cardName">
          <el-col :span="9">
            <el-input v-model="cardForm.cardName" placeholder="如：钻石卡" maxlength="30" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item label="卡类型" prop="cardType">
          <el-col :span="8">
            <el-radio-group v-model="cardForm.cardType">
              <el-radio-button :label="1">储值卡</el-radio-button>
              <el-radio-button :label="2">折扣卡</el-radio-button>
              <el-radio-button :label="3">计次卡</el-radio-button>
              <el-radio-button :label="4">课时卡</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item label="适用项目种类:" prop="groundType">
          <el-col :span="4">
            <el-select v-model="cardForm.groundType">
              <el-option v-for="item in groundFilterOptions" :key="item.key" :label="item.name" :value="item.key" />
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="有效期" prop="validDaysLimit">
          <el-col :span="4">
            <el-input v-model="cardForm.validDaysLimit" placeholder="有效期天数">
              <template slot="append">天</template>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="可用时段" prop="goldenUseLimit">
          <el-col :span="6">
            <el-radio-group v-model="cardForm.goldenUseLimit">
              <el-radio-button :label="0">仅限非黄时段</el-radio-button>
              <el-radio-button :label="1">全时段通用</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item v-if="cardForm.cardType === 1 || cardForm.cardType === 2" label="面值" prop="valueCount">
          <el-col :span="3">
            <el-input v-model="cardForm.valueCount" placeholder="面值" />
          </el-col>
        </el-form-item>
        <el-form-item v-if="cardForm.cardType === 2" label="折扣" prop="discount">
          <el-col :span="3">
            <el-input v-model="cardForm.discount" placeholder="折扣率">
              <template slot="append">%</template>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item v-if="cardForm.cardType === 3" label="次数" prop="valueCount">
          <el-col :span="3">
            <el-input v-model="cardForm.valueCount" placeholder="次数">
              <template slot="append">次</template>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item v-if="cardForm.cardType === 4" label="课时" prop="valueCount">
          <el-col :span="3">
            <el-input v-model="cardForm.valueCount" placeholder="课时">
              <template slot="append">课时</template>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="价格" prop="amount">
          <el-col :span="4">
            <el-input v-model="cardForm.amount" placeholder="价格">
              <template slot="append">元</template>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="说明描述" prop="description">
          <el-col :span="9">
            <el-input v-model="cardForm.description" type="textarea" rows="10" maxlength="500" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="9">
            <el-button type="primary" style="width: 100%" @click="handleAddVenueCard()">添加</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getVenueInfo, addVenueCard } from '@/api/dodoyd'

export default {
  name: 'AddVenueCard',
  components: { },
  data() {
    const checkNumber = (rule, value, callback) => {
      // 使用正则表达式验证数字
      if (!/\d*$/.test(value)) {
        callback(new Error('请输入正确的数字'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    const checkNotEmpty = (rule, value, callback) => {
      if (!value) {
        callback(new Error('此项必填'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    return {
      venueInfo: {},
      cardForm: {
        cardName: '',
        cardType: 1,
        groundType: 1,
        validDaysLimit: 365,
        goldenUseLimit: 1,
        discount: null,
        valueCount: null,
        amount: null,
        description: null
      },
      rules: {
        cardName: [{ validator: checkNotEmpty, trigger: 'blur' }],
        cardType: [{ validator: checkNotEmpty, trigger: 'blur' }],
        groundType: [{ validator: checkNotEmpty, trigger: 'blur' }],
        validDaysLimit: [{ validator: checkNumber, trigger: 'blur' }],
        valueCount: [{ validator: checkNumber, trigger: 'blur' }],
        goldenUseLimit: [{ validator: checkNumber, trigger: 'blur' }],
        amount: [{ validator: checkNumber, trigger: 'blur' }],
        description: [{ validator: checkNotEmpty, trigger: 'blur' }]
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
    handleAddVenueCard() {
      this.cardForm.venueId = this.venueId
      this.$refs.cardForm.validate(valid => {
        if (valid) {
          // 字段检查
          if (this.cardForm.cardType === 2) {
            if (!this.cardForm.discount) {
              this.$message({ showClose: true, message: '折扣卡必须填写折扣', type: 'error' })
              return false
            }
          } else {
            if (!this.cardForm.valueCount) {
              this.$message({ showClose: true, message: '储值卡/计次卡/课时卡必须填写面值/次数/课时', type: 'error' })
              return false
            }
            this.cardForm.discount = null
          }
          // 检查通过之后则添加
          addVenueCard(this.cardForm).then(response => {
            this.$message({
              showClose: true,
              message: response.message,
              type: response.code === 0 ? 'success' : 'error'
            })
            if (response.code === 0) {
              this.$router.push({ path: '/vip/venueCardList' })
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
