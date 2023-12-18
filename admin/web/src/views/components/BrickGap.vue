<template>
  <!-- 时间轴 -->
  <div class="brick_gap_wrap">
    <!--  最上面一个  -->
    <div class="brick_gap">
      <div class="brick_gap_title">{{ startTime }}:00</div>
      <div class="brick_gap_line" />
    </div>
    <!--  下面的元素，v-for是从1开始的  -->
    <div v-for="item in (endTime - startTime)" :key="item" class="brick_gap">
      <div class="brick_gap_title">{{ startTime + item }}:00</div>
      <div class="brick_gap_line" />
    </div>
  </div>
</template>

<script>

export default {
  name: 'BrickGap',
  components: { },
  props: {
    startTime: {
      type: Number,
      require: true,
      default: 8
    },
    endTime: {
      type: Number,
      default: 22
    }
  },

  data() {
    return {
    }
  },

  methods: {
  }
}
</script>

<style>
.brick_gap_wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.brick_gap {
  display: flex;
  width: 50px;
  height: 40px;
}

.brick_gap .brick_gap_line {
  width: 10px;
  height: 40px;
  border-bottom: 1px solid #000000;
}

.brick_gap .brick_gap_title {
  width: 40px;
  height: 20px;
  line-height: 20px;
  font-size: 12px;
  text-align: center;
  -webkit-transform: translateX(0%) translateY(150%);
  transform: translateX(0%) translateY(150%)
}

</style>
