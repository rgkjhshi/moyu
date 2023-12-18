<template>
  <div
    class="brick"
    :class="[ type ? 'brick-type-' + type : '', size ? 'brick-size' + size : '', disabled ? 'disabled' : '']"
    @click="handleClick"
  >
    <div v-if="activity" class="brick-activity">
      {{ activity }}
    </div>
    <span v-if="$slots.default"><slot /></span>
  </div>
</template>

<script>

export default {
  name: 'Brick',
  props: {
    type: {
      type: String,
      default: 'default'
    },
    size: {
      type: String,
      default: ''
    },
    activity: {
      type: String,
      default: ''
    },
    disabled: Boolean
  },

  data() {
    return {
    }
  },

  methods: {
    handleClick(evt) {
      this.$emit('click', evt)
    }
  }
}
</script>

<style>
.brick {
  display: flex;
  width: 80px;
  height: 40px;
  border-left: 1px solid #FFFFFF;
  border-bottom: 1px solid #FFFFFF;
  border-radius: 3px;
  font-size: 14px;
  justify-content: center;
  align-items: center;
  line-height: 40px;
}

.brick,
.brick-type-default {
  background-color: #dfe4ed;
}

.brick:focus,
.brick:hover,
.brick-type-default:focus,
.brick-type-default:hover {
  background-color: #f4f4f5;
  cursor: pointer;
}

.brick-type-used {
  background-color: #E6A23C;
}

.brick-type-used:focus,
.brick-type-used:hover {
  background-color: #EBB563;
  cursor: pointer;
}

.brick-type-available {
  background-color: #67C23A;
}

.brick-type-available:focus,
.brick-type-available:hover {
  background-color: #85ce61;
  cursor: pointer;
}

.brick-type-active {
  background-color: #409EFF;
}

.brick-type-active:focus,
.brick-type-active:hover {
  background-color: #66b1ff;
  cursor: pointer;
}

.brick-size2 {
  height: 80px;
}
.brick-size3 {
  height: 120px;
}
.brick-size4 {
  height: 160px;
}

.brick-activity {
  position: absolute;
  top: 0;
  right: 0;
  padding: 0 4px;
  background-color: #F56C6C;
  border: 1px solid #FFF;
  border-radius: 4px;
  color: #FFF;
  font-size: 12px;
  line-height: 14px;
}
</style>
