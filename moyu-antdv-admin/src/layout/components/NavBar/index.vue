<template>
	<div class="admin-ui-navbar">
		<!-- 左侧面板 -->
		<div class="left-panel">
			<!-- Hamburger -->
			<Hamburger/>
			<!-- Breadcrumb -->
			<Breadcrumb />
		</div>
		<!-- 右侧面板 -->
		<div class="right-panel">
			<user-bar />
		</div>
	</div>
</template>
<script setup>
	import Breadcrumb from '@/layout/components/Breadcrumb/index.vue'
	import Hamburger from "@/layout/components/NavBar/Hamburger/index.vue";
	import UserBar from "@/layout/components/NavBar/userBar.vue";

</script>

<style scoped>
.admin-ui-navbar {
	display: flex;
	justify-content: space-between;
	background: #FFFFFF;
	border-bottom: 1px solid var(--header-bottom);
	box-shadow: 0 0.4px 0.5px rgb(0 21 41 / 12%);
	.ant-menu-item {
		height: 48px;
		line-height: 48px;
	}
}
.admin-ui-navbar .left-panel {
	display: flex;
	align-items: center;
}
.admin-ui-navbar .right-panel {
	display: flex;
	align-items: center;
}

</style>
