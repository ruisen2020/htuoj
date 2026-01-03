<template>
  <div class="discuss-main-content">
    <DiscussItem/>
    <div class="tag">
      <span @click="isHot = !isHot" style="font-size: 14px; font-weight: bold;color: #909399">{{isHot?'最热':'最新'}}<i class="el-icon-sort"/></span>
    </div>
    <div class="comment">
      <CommentList/>
    </div>
  </div>
</template>
<script>
import DiscussItem from "@/components/Discuss/DiscussItem.vue";
import CommentList from "@/components/Discuss/CommentList.vue";

export default {
  name: "DiscussMain",
  components: {CommentList, DiscussItem},
  data(){
    return {
      isHot:false,
    }
  },
  methods:{
  }
}
</script>

<style scoped>
.discuss-main-content{
  padding: 20px;
  width: 674px;
}
.tag{
  padding: 5px 10px 5px 5px;
  border-radius: 5px;
  border: #DCDFE6 1.5px solid;
  margin-top: 10px;
  margin-bottom: 10px;
  background-color: white;
  display: flex;
  justify-content: flex-end;
}
.tag:hover{
  cursor: pointer;
}
</style>