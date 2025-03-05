<template>
	<!-- 组织选择器 -->
  <a-tree-select
      v-model:value="selectValue"
      v-model:treeExpandedKeys="defaultExpandedKeys"
      :dropdown-style="{ maxHeight: '500px', overflow: 'auto' }"
      placeholder="请选择"
      allow-clear
      :tree-data="props.treeData"
      :field-names="{ children: 'children', label: 'name', value: 'code' }"
      :tree-line="{ showLeafIcon:false }"
      :tree-checkable="props.multiSelect"
      :show-checked-strategy="TreeSelect.SHOW_ALL"
      @change="onChange"
  />
</template>

<script setup lang="ts">

import { TreeSelect } from "ant-design-vue"

const props = defineProps({
  // 是否多选
  multiSelect: {
    type: Boolean,
    default: false
  },
  defaultValue: {
    type: [String, Array]
  },
  // 树的节点数据
  treeData: {
    type: Array,
    default: []
  }
})

const emit = defineEmits(['onChange'])
// 树节点被选中时调用(https://antdv.com/components/tree-select-cn#api)
const onChange = (value, label, extra) => {
  // 传递被选中的节点key
  emit('onChange', value, label, extra)
}

// 默认展开的节点
const defaultExpandedKeys = ref([])
const selectValue = ref()

onMounted(() => {
  defaultExpandedKeys.value = [props.treeData[0]?.code]
  selectValue.value = props.defaultValue
})

// 暴露子组件的方法
defineExpose({
})
</script>

<style scoped>

</style>
