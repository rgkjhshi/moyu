<template>
  <div class="app-container">
    <el-card class="tip-error">
      <span class="tip-content">
        此流程为线下办理会员卡，请务必确认已收费，并确认好会员信息<br>
      </span>
    </el-card>
    <el-card header="办理会员">
      <el-form ref="form" label-width="100px">
        <el-form-item label="场馆名称">
          <el-col :span="6">
            <el-tag>{{ venueInfo.venueName }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="会员卡">
          <el-col :span="6">
            <el-card
              :class="[ 'box-card', cardInfo.cardType===1?'bg-gradual-orange':cardInfo.cardType===2?'bg-gradual-red':cardInfo.cardType===3?'bg-gradual-pink':'bg-gradual-blue']"
              shadow="hover"
            >
              <div class="card-title">
                <span>{{ cardInfo.cardName }}</span>
              </div>
              <div class="card-info">
                <span v-if="cardInfo.cardType === 1">储值卡 | 面值 {{ cardInfo.valueCount }}</span>
                <span v-if="cardInfo.cardType === 2">折扣卡 | 面值 {{ cardInfo.valueCount }}</span>
                <span v-if="cardInfo.cardType === 3">计次卡 | 共 {{ cardInfo.valueCount }} 次</span>
                <span v-if="cardInfo.cardType === 4">课时卡 | 共 {{ cardInfo.valueCount }} 课时</span>
              </div>
              <div class="card-info">
                <span>有效期 {{ cardInfo.validDaysLimit }} 天</span>
              </div>
              <div v-if="cardInfo.cardType === 2" class="card-info">
                <span>{{ cardInfo.discount/10 }}折</span>
              </div>
              <div class="card-info">
                <span v-if="cardInfo.goldenUseLimit === 0">仅限非黄时段</span>
                <span v-if="cardInfo.goldenUseLimit === 1">全时段通用</span>
              </div>
            </el-card>
          </el-col>
        </el-form-item>
        <el-form-item label="金额">
          <el-col :span="6">
            <el-tag>{{ cardInfo.amount }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-col :span="4">
            <el-input v-model="mobile" placeholder="请输入会员手机号" maxlength="11" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item label="手机号确认" prop="mobile2">
          <el-col :span="4">
            <el-input v-model="mobile2" placeholder="请在此确认会员手机号" maxlength="11" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="4">
            <el-button type="primary" style="width: 100%" @click="handleSubmit()">提交</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getVenueInfo, getVenueCardInfo, addVipCard } from '@/api/dodoyd'

export default {
  name: 'AddVipCard',
  components: { },
  data() {
    return {
      venueInfo: {},
      cardInfo: {},
      mobile: '',
      mobile2: ''
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
    this.templateId = this.$route.query.templateId
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
      getVenueCardInfo({ templateId: this.templateId }).then(response => {
        if (response.code === 0) {
          this.cardInfo = response.data
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 提交
    handleSubmit() {
      // 先检查手机号
      if (!/^1[3456789]\d{9}$/.test(this.mobile)) {
        this.$message({ showClose: true, message: '请输入正确的手机号', type: 'error' })
        return
      }
      if (this.mobile !== this.mobile2) {
        this.$message({ showClose: true, message: '两次输入的手机号不一致', type: 'error' })
        return
      }
      addVipCard({
        venueId: this.venueId,
        templateId: this.templateId,
        mobile: this.mobile
      }).then(response => {
        this.$message({
          showClose: true,
          message: response.message,
          type: response.code === 0 ? 'success' : 'error'
        })
        if (response.code === 0) {
          this.$router.push({ path: '/vip/vipList', query: { cardNo: response.data.cardNo }})
        }
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

.box-card {
  width: 300px;
  text-align: left;
  line-height: 23px;
}
.card-title {
  font-size: 16px;
  font-weight: bold;
  padding-bottom: 10px;
}
.card-info {
}
.bg-gradual-red {
  background-image: linear-gradient(45deg, #f43f3b, #ec008c);
  color: #ffffff;
}

.bg-gradual-orange {
  background-image: linear-gradient(45deg, #ff9700, #ed1c24);
  color: #ffffff;
}

.bg-gradual-green {
  background-image: linear-gradient(45deg, #39b54a, #8dc63f);
  color: #ffffff;
}

.bg-gradual-purple {
  background-image: linear-gradient(45deg, #9000ff, #5e00ff);
  color: #ffffff;
}

.bg-gradual-pink {
  background-image: linear-gradient(45deg, #ec008c, #6739b6);
  color: #ffffff;
}

.bg-gradual-blue {
  background-image: linear-gradient(45deg, #0081ff, #1cbbb4);
  color: #ffffff;
}

</style>
