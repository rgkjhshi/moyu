## 魔芋
提高研发效率的工具系统

可以根据DDL生成代码（Model、Dao、Service、Controller）

## Change Log
### 2024-06-13
为了简化使用，采用node@16.16.0

### 2024-06-03
node版本若高于16,为解决openssl的问题`ERR_OSSL_EVP_UNSUPPORTED`
在package.json做如下修改:

```shell
"dev": "vue-cli-service serve",
改为
"dev": "export NODE_OPTIONS='--openssl-legacy-provider' && vue-cli-service serve",
```


