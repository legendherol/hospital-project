
// 客户端（前端）需要获取服务端（后端）的公钥，此处使用接口获取，可以不通过接口获取
import request from "@/utils/request";
import store from "@/store";
import CryptoJS from "crypto-js";
import forge from "node-forge";
import {b64tohex, KJUR} from "jsrsasign";

var AesKey="O8MEO1Wsq9yLKu0N";
export function getPrivateKey() {
  return request({
    url: '/rsa/key/getPrivateKey',
    method: 'get'
  })
}

// 生成AES密钥
export function getmm(num = 16) {
  var amm = ['!', '@', '#', '$', '%', '&', '*', '(', ')', '_', 1, 2, 3, 4, 5, 6, 7, 8, 9]
  var tmp = Math.floor(Math.random() * num)
  var s = tmp
  s = s + amm[tmp]
  for (let i = 0; i < Math.floor(num / 2) - 1; i++) {
    tmp = Math.floor(Math.random() * 26)
    s = s + String.fromCharCode(65 + tmp)
  }
  for (let i = 0; i < (num - Math.floor(num / 2) - 1); i++) {
    tmp = Math.floor(Math.random() * 26)
    s = s + String.fromCharCode(97 + tmp)
  }
  return s
}

// 生成客户端RSA公钥和私钥 需要双向加密时需要，将在文末讨论
// export function jsrsasignFn() {
//   var rsaKeypair = jsrsasign.KEYUTIL.generateKeypair('RSA', 2048)
//   var private1 = jsrsasign.KEYUTIL.getPEM(rsaKeypair.prvKeyObj, 'PKCS1PRV')
//   var public1 = jsrsasign.KEYUTIL.getPEM(rsaKeypair.pubKeyObj)
//   let a = {
//     'privateKey': private1.substring(31, private1.length - 31).replace(/\r\n/g, ''),
//     'publicKey': public1.substring(28, public1.length - 28).replace(/\r\n/g, '')
//   }
//   console.log(a)
//   return a
// }

// AES 加密  data：要加密解密的数据，AES_KEY：密钥，
export function encrypt(data,AES_KEY) {
  const key = CryptoJS.enc.Utf8.parse(AES_KEY)
  const encrypted = CryptoJS.AES.encrypt(data, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  // console.log("加密数据：" + data)
  // console.log("数据加密后：" + encrypted.toString())
  return encrypted.toString()
}

export function md5Encry(text){
  const md5Data = CryptoJS.MD5(JSON.stringify(text)); // 生成哈希值
  const hashString = md5Data.toString(); // 将哈希值转换为字符串
  return hashString;
  // console.log(hashString)
}
// 使用服务端的公钥加密AES密钥
export function pubencrypt(aeskey, pubencryptKey) {
  // console.log("aeskey：" + aeskey)
  // console.log("服务器公钥：" + pubencryptKey)
  // 此处的前后缀不能改变
  const publicKeyAll = '-----BEGIN PUBLIC KEY-----\n' + pubencryptKey + '\n-----END PUBLIC KEY-----'
  var publicKey = forge.pki.publicKeyFromPem(publicKeyAll)
  var buffer = forge.util.createBuffer(aeskey, 'utf8')
  var bytes = buffer.getBytes()
  // console.log("公钥加密后的AES密钥：" + a)
  return forge.util.encode64(publicKey.encrypt(bytes, 'RSAES-PKCS1-V1_5'))
}

// 使用前端私钥加密
export function privencrypt(data, privencryptKey) {
  // 此处的前后缀不能改变
  const privateKeyAll = '-----BEGIN PRIVATE KEY-----\n' + privencryptKey + '\n-----END PRIVATE KEY-----'
  var privateKey = forge.pki.privateKeyFromPem(privateKeyAll)
  var buffer = forge.util.createBuffer(data, 'utf8')
  var bytes = buffer.getBytes()
  // 使用 SHA-1 作为签名算法
  var md = forge.md.sha1.create()
  md.update(bytes)
  var signature = privateKey.sign(md)
  return forge.util.encode64(signature)
}

// post请求参数加密
export function resEncryption(data) {
  // 生成随机密钥（key）
  var key = AesKey;
  // 密钥 加密 数据（将json转成字符串再进行加密）
  var newData = encrypt(JSON.stringify(data), key)
  // console.log("--------------------"+newData)
  // 公钥 加密 密钥（key组成）
  var aesKey = pubencrypt(key, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIchYauP3Gx9am0WKzUpIZMx0og+uRBT33FZN02d4rGGhhcv2OPnoJC6F+JMFtVVvc+/zMgilVHSSNIUrkpJyogpnF97Rpzi2Rmas0AXyeJ9d+Pu+jmMUZYqBIfgSPGY8dvDDMAg8EffnNQzBdKbBdpuWy9eKS7eoogyWHkFxmWQIDAQAB")
  // console.log(aesKey)
  // 返回数据
  return {
    data: newData,
    aesKey: aesKey,
  };
}

// get请求参数加密
export function resEncryptionForGet(data) {
  // 生成随机密钥（key）
  var key = getmm()
  // 密钥 加密 数据（将json转成字符串再进行加密）
  var newData = encrypt(JSON.stringify(data), key)
  console.log("--------------------"+newData)
  // 公钥 加密 密钥（key组成）
  var aesKey = pubencrypt(key, store.state.user.serverPublicKey)
  // console.log(aesKey)
  // 返回数据
  return {
    'data': newData,
    'aesKey': aesKey
  }
}

// 使用服务端的公钥验签AES密钥
export function pubverify(aeskey, pubencryptKey, sign) {
  // console.log("aeskey：" + aeskey)
  // console.log("服务器公钥：" + pubencryptKey)
  const publicKeyAll = '-----BEGIN PUBLIC KEY-----\n' + pubencryptKey + '\n-----END PUBLIC KEY-----'
  try {
    let sig =  new KJUR.crypto.Signature({alg: "MD5withRSA"});
    sig.init(publicKeyAll)
    sig.updateString(aeskey)
    return sig.verify(b64tohex(sign))
  } catch (e) {
    console.log(e)
  }
}

// AES 加密  data：要加密解密的数据，AES_KEY：密钥，
export function aesDecrypt(data, AES_KEY) {
  // console.log("---------------------开始解密AES")
  const decrypted = CryptoJS.AES.decrypt(data.toString(), CryptoJS.enc.Utf8.parse(AES_KEY), {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  // console.log("数据：" + data.toString())
  // console.log("解密后：" + decrypted.toString(CryptoJS.enc.Utf8))
  return decrypted.toString(CryptoJS.enc.Utf8)
}

// 响应解密
export function resDecryption(data) {
  // 使用公钥先验签aesKey
  let verifyResult = pubverify(data.aesKey, store.state.user.serverPublicKey, data.sign);
  if (!verifyResult) {
    this.$message({
      type: 'warning',
      message: '响应数据不合法！'
    });
    return ;
  }
  console.log(verifyResult)
  // 使用aesKey解密数据
  return aesDecrypt(data.data, data.aesKey);
}
