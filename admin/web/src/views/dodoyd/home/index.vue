<template>
  <div class="app-container">
    <el-row>
      <el-col />
    </el-row>
    <el-row type="flex" align="" style="margin-top: 80px;">
      <el-col :offset="2" :span="8">
        <div class="main-box">
          <div class="title">多多运动Plus</div>
          <div class="content">多多运动小程序提供网球、羽毛球、足球、篮球等多种运动项目的在线预订功能。商家可以
            <span class="bold">免费入驻</span>，为商家提供场地预订、场地管理、订单管理等多种服务，致力于打造一款受欢迎的运动品牌，与商家一起合作共赢。</div>
          <el-empty description="微信扫码即可体验" :image-size="200" />
        </div>
      </el-col>
      <el-col :offset="1" :span="10">
        <el-image fit="contain" :src="loginPng" class="image" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import loginPng from '@/assets/login_images/login.png'

export default {
  name: 'Home',
  components: { },
  data() {
    return {
      loginPng: loginPng + '?' + +new Date()
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
  }
}
</script>

<style>

.main-box {
}
.title {
  font-size: 40px;
  font-weight: bold;
  padding-bottom: 10px;
}
.content {
  font-size: 20px;
  line-height: 36px;
}
.content .bold {
  font-weight: bold;
}
</style>
