<template>
  <div class="app-container">
    <el-divider content-position="left">
      <span style="font-size: 20px; font-weight: bold">场馆信息</span>
    </el-divider>
    <!--    <el-card class="tip">-->
    <!--      <span class="tip-content">修改信息请联系客服</span>-->
    <!--    </el-card>-->
    <!--    <el-carousel type="card">-->
    <!--      <el-carousel-item v-for="item in urls" :key="item">-->
    <!--        <el-image :src="item" :preview-src-list="urls" class="image" />-->
    <!--      </el-carousel-item>-->
    <!--    </el-carousel>-->
    <el-card header="">
      <el-descriptions title="基本信息" :column="2" border label-class-name="LS">
        <template v-if="isAdmin" slot="extra">
          <router-link to="/venue/editVenue">
            <el-button type="primary" plain size="small">修改</el-button>
          </router-link>
        </template>
        <el-descriptions-item label="场馆名称" :span="2">
          <span>{{ venueInfo.venueName }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="营业状态">
          <el-tag v-if="venueInfo.venueStatus===0" type="info" effect="plain">未上线</el-tag>
          <el-tag v-else-if="venueInfo.venueStatus===1" type="success">正常营业</el-tag>
          <el-tag v-else-if="venueInfo.venueStatus===2" type="info">暂停营业</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="营业时间">
          <span style="margin-right: 20px">{{ venueInfo.openTime }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="黄金场次" :span="2">
          <el-tag v-if="venueInfo.goldenWeekend" type="info">周末全黄</el-tag>
          <el-tag v-if="venueInfo.goldenHoliday" type="info">节假日全黄</el-tag>
          <el-tag v-for="clock in venueInfo.goldenClockList" :key="clock" type="info" effect="plain">{{ clock+':00' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="订场开关">
          <el-switch v-model="openBook" active-text="打开" inactive-text="关闭" @change="openBookChange()" />
        </el-descriptions-item>
        <el-descriptions-item label="约课开关">
          <el-switch v-model="openLesson" active-text="打开" inactive-text="关闭" @change="openLessonChange()" />
        </el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">
          <span style="margin-right: 20px">{{ venueInfo.address }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="店员" :span="2">
          <div v-for="user in clerkList" :key="user.userId" class="avatar-container">
            <div class="avatar-wrap">
              <el-avatar :size="50" :src="user.avatar">{{ user.nickname }}</el-avatar>
            </div>
            <div class="avatar-title">{{ user.nickname }}</div>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="教练" :span="2">
          <div v-for="user in coachList" :key="user.userId" class="avatar-container">
            <div class="avatar-wrap">
              <el-avatar :size="50" :src="user.avatar">{{ user.nickname }}</el-avatar>
            </div>
            <div class="avatar-title">{{ user.nickname }}</div>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="更多描述信息" :span="2">
          <div v-for="item in venueInfo.descList" :key="item">{{ item }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>

import { getVenueInfo, getVenueUserList, updateVenueConfig } from '@/api/dodoyd'

export default {
  name: 'VenueInfo',
  components: {
  },
  data() {
    return {
      urls: [
        'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
        'https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg',
        'https://fuss10.elemecdn.com/9/bb/e27858e973f5d7d3904835f46abbdjpeg.jpeg'
      ],
      venueInfo: {},
      openBook: null,
      openLesson: null,
      clerkList: [],
      coachList: []
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
    roles() {
      // roles是一个数组 用法：roles && roles.length > 0 && roles.indexOf('admin') > -1
      return this.$store.getters.roles
    },
    isAdmin() {
      const roles = this.$store.getters.roles
      return roles && roles.length > 0 && roles.indexOf('admin') > -1
    }
  },
  created() {
    this.getData()
  },
  methods: {
    // 获取表格内的数据列表
    getData() {
      getVenueInfo(this.venueId).then(response => {
        if (response.code === 0) {
          this.venueInfo = response.data
          this.venueInfo.openTime = this.venueInfo.openStart + ':00 - ' + this.venueInfo.openEnd + ':00'
          this.openBook = response.data.openBook
          this.openLesson = response.data.openLesson
        }
      }).then(async() => {
        return getVenueUserList({ venueId: this.venueId })
      }).then(response => {
        if (response.code === 0) {
          const userList = response.data
          const clerkList = []
          const coachList = []
          userList.forEach(e => {
            if (e.userRole === 2) {
              clerkList.push(e)
            } else if (e.userRole === 3) {
              coachList.push(e)
            }
          })
          this.clerkList = clerkList
          this.coachList = coachList
        }
        this.listLoading = false
      }).catch(err => {
        console.log(err)
      })
    },

    // 更新场馆地址
    openBookChange() {
      let title = '确定要关闭订场开关吗？'
      let message = '关闭后用户将无法预订场地'
      if (this.openBook) {
        title = '确定要打开订场开关吗'
        message = '打开后将允许用户预订场地'
      }
      this.updateVenueConfig(title, message)
    },

    // 更新场馆地址
    openLessonChange() {
      let title = '确定要关闭约课开关吗？'
      let message = '关闭后用户将无法约课'
      if (this.openLesson) {
        title = '确定要打开约课开关吗'
        message = '打开后将允许用户约课'
      }
      this.updateVenueConfig(title, message)
    },

    // 更新场馆配置信息
    updateVenueConfig(title, message) {
      this.$confirm(message, title, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        return updateVenueConfig({ venueId: this.venueId, openBook: this.openBook, openLesson: this.openLesson })
      }).then(response => {
        this.$message({ type: 'success', message: response.message })
        this.getData()
      }).catch(err => {
        console.log(err)
        this.getData()
      })
    }
  }
}
</script>

<style>
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

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.image {
  width: 100%;
  height: 100%;
}
.text {
  font-size: 14px;
  line-height: 1.5em;
}
.text-title {
  color: #5e6d82;
}
.LS {
  width: 120px;
}

.el-tag + .el-tag {
  margin-left: 10px;
}

.avatar-container {
  width: 80px;
  display: inline-block;
}
.avatar-wrap {
  display: flex;
  justify-content: center;
}
.avatar-title {
  display: flex;
  justify-content: center;
  padding: 5px;
}

.square-container {
  width: 100px;
}
.square-content {
  width: 80px;
  height: 100px;
  background-color: #4A9FF9;
}
.square-wrap {
  display: flex;
  justify-content: center;
}
.square-title {
  display: flex;
  justify-content: center;
  padding: 5px;
}
</style>
