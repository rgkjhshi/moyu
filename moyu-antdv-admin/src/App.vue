<template>
  <a-config-provider :locale="locale" :theme="{token: { borderRadius: 2 }}">
    <a-app id="app" class="app">
      <a-watermark :content="watermarkEnabled && userInfo ? [userInfo.name, userInfo.account] : undefined"
                   class="admin-ui-main">
        <router-view/>
      </a-watermark>
    </a-app>
  </a-config-provider>
</template>

<script setup lang="ts">
  import { useUserStore, useSettingsStore } from '@/store'
  import i18n from "@/locale"

  const userStore = useUserStore()
  const settingsStore = useSettingsStore()

  // 本地语言
  const locale = i18n.global.messages.value[i18n.global.locale.value].lang

  // 获取用户信息
  const userInfo = computed(() => {
    return userStore.userInfo
  })
  // 水印开关
  const watermarkEnabled = computed(() => {
    return settingsStore.watermarkEnabled
  })
</script>

<style scoped>

</style>
