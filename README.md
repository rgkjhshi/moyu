## 魔芋
MY管理后台极简版

## 前端运行

### 安装依赖
```shell
npm install --registry=https://registry.npmmirror.com
```
### 启动服务
```shell
# 本地开发 启动项目
npm run dev
```

## Change Log
### 2024-06-13

### 2024-06-03
node版本若高于16,为解决openssl的问题`ERR_OSSL_EVP_UNSUPPORTED`
在package.json做如下修改:

```shell
"dev": "vue-cli-service serve",
改为
"dev": "export NODE_OPTIONS='--openssl-legacy-provider' && vue-cli-service serve",
```


