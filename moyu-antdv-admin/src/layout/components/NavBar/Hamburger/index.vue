<template>
	<!-- Hamburger -->
	<div class="panel-item" @click="onClick">
		<MenuUnfoldOutlined v-if="menuCollapsed" />
		<MenuFoldOutlined v-else />
	</div>
</template>
<script setup>
	import { useSettingsStore } from '@/store'
	import { MenuFoldOutlined, MenuUnfoldOutlined } from "@ant-design/icons-vue";

	const settingsStore = useSettingsStore()

	const menuCollapsed = computed(() => {
		return settingsStore.menuCollapsed
	})

	const onClick = () => {
		settingsStore.toggleConfig('menuCollapsed')
	}
</script>

<style scoped>
	.panel-item {
		padding: 0 10px;
		cursor: pointer;
		height: 100%;
		display: flex;
		align-items: center;
		/*color: var(--font-color);*/
	}

	.panel-item:hover {
		background: var(--header-color-split);
	}
</style>
