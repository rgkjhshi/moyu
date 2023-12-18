<template>
  <span>
    <el-select v-model="currentVenueId" :disabled="disabled" filterable placeholder="暂无场馆" style="width: 250px;" @change="changeVenue">
      <el-option v-for="item in venueList" :key="item.venueId" :label="item.venueName" :value="item.venueId" />
    </el-select>
  </span>
</template>

<script>

import store from '@/store'
import { queryVenueList } from '@/api/dodoyd'

export default {
  name: 'CurrentVenue',
  props: {
    // disabled: {
    //   type: Boolean,
    //   default: false
    // },
    callback: {
      type: Function,
      default: () => 0
    }
  },
  data() {
    return {
      currentVenueId: '',
      venueList: [],
      disabled: false
    }
  },
  created() {
    // 优先级：传参 > vuex > localStorage
    this.currentVenueId = this.$route.query && this.$route.query.venueId
    if (this.currentVenueId) {
      // 传参指定则修改 vuex 和 localStorage
      this.$store.commit('SET_VENUE_ID', this.currentVenueId)
      localStorage.setItem('venueId', this.currentVenueId)
    } else if (this.$store.getters.venueId) {
      // 传参未指定的取vuex中的
      this.currentVenueId = this.$store.getters.venueId
    } else if (localStorage.getItem('venueId')) {
      // 传参和vuex都无则取localStorage
      this.currentVenueId = localStorage.getItem('venueId')
    }
  },
  mounted() {
    this.getVenueList()
  },
  methods: {
    getVenueList() {
      queryVenueList().then(response => {
        if (response.code === 0) {
          this.venueList = response.data
          // 当前无VenueId时更新
          if (!this.currentVenueId && this.venueList[0]) {
            this.currentVenueId = this.venueList[0].venueId
            // 修改 vuex 和 localStorage
            store.dispatch('dodoyd/changeVenueId', this.currentVenueId)
            localStorage.setItem('venueId', this.currentVenueId)
          }
          // 获取到venueList之后，若只有一个，则取第一个并更新
          if (this.venueList.length === 1) {
            this.disabled = true
          } else if (this.venueList.length === 0) {
            this.disabled = true
            this.currentVenueId = null
          }
        }
      }).catch(err => {
        console.log(err)
      })
    },
    changeVenue() {
      if (this.currentVenueId) {
        store.dispatch('dodoyd/changeVenueId', this.currentVenueId)
        // store.dispatch 或 this.$store.commit 均可修改
        // this.$store.commit('dodoyd/SET_VENUE_ID', this.currentVenueId)
        // 采用 localStorage 存储, 取 localStorage.getItem('key')
        localStorage.setItem('venueId', this.currentVenueId)
      }
      this.callback(this.currentVenueId)
    }
  }
}
</script>

