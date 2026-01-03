<template>
  <div>
    <el-image class="comment-image"
              :src = src
              :zoom-rate="1.2"
              :preview-src-list="imgList?imgList:[src]"
              :inital-index="0"
              fit="scale-down"
              loading="lazy"
              hide-on-click-modal
    />
  </div>
</template>

<script>
export default {
  name: "CommentImage",
  props:{
    src:{
      type:String
    },
    imgList:{
      type:Array
    }
  }
}
</script>

<style scoped>
.comment-image{
  width: 80px;
  height: 80px;
  background-color: #eeeeee;
}
</style>