<template>
  <div class="brick_group">
    <div v-if="title" class="brick_group_title">
      {{ title }}
    </div>
    <slot v-else name="title" />

    <slot />
  </div>
</template>
<script>
export default {
  name: 'BrickGroup',
  props: {
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style>
.brick_group {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.brick_group_title {
  height: 40px;
  line-height: 40px;
  font-size: 12px;
  text-align: center;
}
</style>
