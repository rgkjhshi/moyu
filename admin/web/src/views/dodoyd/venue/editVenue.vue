<template>
  <div class="app-container">
    <el-card class="tip">
      <span class="tip-content">
        店长或店员可以修改场馆信息<br>
      </span>
    </el-card>
    <el-card header="编辑场馆">
      <el-form ref="venueForm" :model="venueForm" :rules="rules" label-width="100px">
        <el-form-item label="场馆ID">
          <el-col :span="6">
            <el-tag>{{ venueForm.venueId }}</el-tag>
          </el-col>
        </el-form-item>
        <el-form-item label="场馆名称" prop="venueName">
          <el-col :span="8">
            <el-input v-model="venueForm.venueName" placeholder="请输入场馆名称" maxlength="30" disabled />
          </el-col>
        </el-form-item>
        <el-form-item label="营业时间" prop="openStart">
          <el-col :span="20" style="padding-bottom: 10px;">
            <el-slider v-model="openTimeRange" range :step="1" :max="24" :marks="marks" :format-tooltip="formatTooltip" @change="sliderChange" />
          </el-col>
        </el-form-item>
        <el-form-item label="黄金场次">
          <el-col :span="20">
            <el-checkbox-group v-model="venueForm.goldenClockList" size="small" @change="checkboxChange">
              <el-checkbox-button v-for="clock in clockList" :key="clock" :label="clock">{{ clock+':00' }}</el-checkbox-button>
            </el-checkbox-group>
          </el-col>
        </el-form-item>
        <el-row>
          <el-col :span="6">
            <el-form-item label="周末全黄">
              <el-switch v-model="venueForm.goldenWeekend" active-text="是" inactive-text="否" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="节假日全黄">
              <el-switch v-model="venueForm.goldenHoliday" active-text="是" inactive-text="否" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="预定开关">
              <el-switch v-model="venueForm.openBook" active-text="开放" inactive-text="关闭" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="约课开关">
              <el-switch v-model="venueForm.openLesson" active-text="开放" inactive-text="关闭" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-col :span="14">
            <el-input v-model="venueForm.address" placeholder="请输入场馆地址" maxlength="50" />
          </el-col>
        </el-form-item>
        <el-row>
          <el-col :span="6">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="venueForm.latitude" placeholder="请输入纬度" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="venueForm.longitude" placeholder="请输入纬度" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="更多信息描述" prop="description">
          <el-col :span="9">
            <el-input v-model="venueForm.description" type="textarea" rows="5" maxlength="500" show-word-limit />
          </el-col>
        </el-form-item>
        <el-form-item label="场馆状态" prop="venueStatus">
          <el-col :span="6">
            <el-radio-group v-model="venueForm.venueStatus">
              <el-radio-button :label="0">未上线</el-radio-button>
              <el-radio-button :label="1">正常营业</el-radio-button>
              <el-radio-button :label="2">暂停营业</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="4">
            <el-button type="primary" style="width: 100%" @click="handleUpdateVenue()">保存</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { updateVenue, getVenueInfo } from '@/api/dodoyd'

export default {
  name: 'EditVenue',
  components: { },
  data() {
    const checkOpenTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请填写营业时间'))
      }
      // 时间判定
      if (!/^(([0-9])$|1[0-9])$|(2[0-4])$/.test(value)) {
        callback(new Error('营业时间应在0~24之间'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    const checkVenueName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不可为空白'))
      }
      // 自定义校验规则 需要调用callback()函数！
      callback()
    }
    return {
      openTimeRange: [8, 23],
      clockList: [],
      venueForm: {},
      rules: {
        openStart: [{ validator: checkOpenTime, trigger: 'blur' }],
        venueName: [{ validator: checkVenueName, trigger: 'blur' }],
        address: [{ validator: checkVenueName, trigger: 'blur' }]
      },
      marks: {
        0: {
          label: this.$createElement('strong', '0:00')
        },
        6: '6:00',
        8: '8:00',
        10: '10:00',
        12: {
          label: this.$createElement('strong', '12:00')
        },
        14: '14:00',
        16: '16:00',
        18: '18:00',
        20: '20:00',
        22: '22:00',
        24: {
          label: this.$createElement('strong', '24:00')
        }
      }
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
          this.venueForm = response.data
          this.openTimeRange = [this.venueForm.openStart, this.venueForm.openEnd]
          this.sliderChange(this.openTimeRange)
        }
      }).catch(err => {
        console.log(err)
      })
    },
    formatTooltip(val) {
      return val + ':00'
    },
    sliderChange(newVal) {
      this.venueForm.openStart = this.openTimeRange.at(0)
      this.venueForm.openEnd = this.openTimeRange.at(1)
      const clockList = []
      for (let i = this.venueForm.openStart; i < this.venueForm.openEnd; ++i) {
        clockList.push(i)
      }
      this.clockList = clockList
      // 保证黄金时间点在营业时间范围内
      this.venueForm.goldenClockList = this.venueForm.goldenClockList.filter(e => (e >= this.venueForm.openStart && e < this.venueForm.openEnd))
    },
    checkboxChange(newVal) {
    },
    handleUpdateVenue() {
      this.$refs.venueForm.validate(valid => {
        if (valid) {
          if (this.venueForm.openStart >= this.venueForm.openEnd) {
            this.$message({
              showClose: true,
              message: '营业开始时间必须早于截止时间',
              type: 'error'
            })
            return false
          }
          updateVenue(this.venueForm).then(response => {
            this.$message({ showClose: true, message: response.message, type: response.code === 0 ? 'success' : 'error' })
            if (response.code === 0) {
              this.$router.push({ path: '/venue/venueInfo' })
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
