<template>
  <div class="app-container">
    <el-card class="tip">
      <span class="tip-content">
        代扣记录用户是可以看到的
      </span>
    </el-card>
    <el-card header="会员卡代扣">
      <el-form ref="form" label-width="100px">
        <el-form-item label="会员">
          <el-col :span="6">
            <div v-if="cardInfo.userInfo.userId" class="user-box">
              <el-avatar :src="cardInfo.userInfo.avatar">{{ cardInfo.userInfo.nickname }}</el-avatar>
              <div class="user-title">{{ cardInfo.userInfo.nickname }}</div>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="会员卡">
          <el-col :span="10">
            <!-- 不同类型的会员卡 -->
            <el-card
              :class="[ 'box-card', cardInfo.cardType===1?'bg-gradual-orange':cardInfo.cardType===2?'bg-gradual-red':cardInfo.cardType===3?'bg-gradual-pink':'bg-gradual-blue']"
              shadow="hover"
            >
              <div class="card-title">
                <span>{{ cardInfo.cardName }}</span>
              </div>
              <div class="card-info">
                <span v-if="cardInfo.cardType === 1">储值卡 | 剩余 {{ cardInfo.cardValue }}</span>
                <span v-if="cardInfo.cardType === 2">折扣卡 | 剩余 {{ cardInfo.cardValue }}</span>
                <span v-if="cardInfo.cardType === 3">计次卡 | 剩余 {{ cardInfo.cardValue }} 次</span>
                <span v-if="cardInfo.cardType === 4">课时卡 | 剩余 {{ cardInfo.cardValue }} 课时</span>
              </div>
              <div class="card-info">
                <span>有效期至: {{ cardInfo.expireTime | parseTime('{y}-{m}-{d}') }}</span>
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
        <el-form-item label="代扣数额" prop="mobile">
          <el-col :span="4">
            <el-input v-model="consumeValue" placeholder="请输入代扣数" />
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
import { getVipCardInfo, vipCardWithhold } from '@/api/dodoyd'

export default {
  name: 'VipCardWithhold',
  components: { },
  data() {
    return {
      cardInfo: {
        userInfo: {}
      },
      consumeValue: null
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
    this.cardNo = this.$route.query.cardNo
    this.getData()
  },
  methods: {
    // 获取信息
    getData() {
      getVipCardInfo({ cardNo: this.cardNo }).then(response => {
        if (response.code === 0) {
          this.cardInfo = response.data
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 提交
    handleSubmit() {
      // 校验代扣数额
      if (!/^[1-9]\d*$/.test(this.consumeValue)) {
        this.$message({ showClose: true, message: '请输入正确的数字', type: 'error' })
        return
      }
      vipCardWithhold({
        cardNo: this.cardNo,
        consumeValue: this.consumeValue
      }).then(response => {
        this.$message({
          showClose: true,
          message: response.message,
          type: response.code === 0 ? 'success' : 'error'
        })
        if (response.code === 0) {
          this.$router.push({ path: '/vip/vipList', query: { cardNo: this.cardInfo.cardNo }})
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
.user-box {
  display: flex;
}
.user-title {
  padding-left: 10px;
  height: 40px;
  line-height: 40px;
  font-size: 16px;
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
