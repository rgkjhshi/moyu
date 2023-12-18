import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKLbV0QMRyCureH0SQ4eViBaaurydylc\n' +
  'k2xwLYXQIT2oxN7drA/RWwb3DgXRP6gpFr8T9R9qKIkTIjM3ZzFzkLkCAwEAAQ=='

const privateKey = 'MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAottXRAxHIK6t4fRJ\n' +
  'Dh5WIFpq6vJ3KVyTbHAthdAhPajE3t2sD9FbBvcOBdE/qCkWvxP1H2ooiRMiMzdn\n' +
  'MXOQuQIDAQABAkB3zq8bQ4EMHRWUldcsyMqJkJr5bxiU2CMocjo7KAtcROC3sqKs\n' +
  'V9QYc7zsFHq6IOmwo3JwijtdrdCjECj4e3DRAiEA08rXV8CeUOm4dviCGtvgqszF\n' +
  'AiuTgJ+pZuWsvODkkBUCIQDE2Z7knPavsq2i+TA0aSl40zlphQ6jJ5mfxbq6qlWD\n' +
  'FQIgVtCe1Omv+5rScnILYtcekOS/HNSf8emAlx0uYX4OMsECIQCx41dLkOz8aaOw\n' +
  'GzAMrNvlbDv9t1g66JZTZAQSm8sa4QIhAKXEQXgAoAgJVw2hZTPW7Ui6L+6rDZqP\n' +
  'VhdvtYFSqttm'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

