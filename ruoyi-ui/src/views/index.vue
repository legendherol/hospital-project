<template>
  <div>
    <img :src="require(`./1.jpg`)" alt="image">
  </div>
</template>

<script>
export default {
  name: 'MyComponent',
  data() {
    return {
      imageName: '医院管理系统.jpg' // 图片的文件名
    }
  }
}
</script>




img :src="require(`./1.jpg`)" alt="image">
return {
imageName: '医院管理系统.jpg' // 图片的文件名
}
