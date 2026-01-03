<template>
  <div class="discuss-item-content">
    <div class="discuss">
      <div class="discuss-item-header">
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px">
          <div style="display: flex; float: left">
            <el-avatar size="large" shape="circle"/>
            <span style="font-size: larger; font-weight: bold; display:flex; align-items: center; margin-left: 10px">{{title}}</span>
          </div>
          <el-button type="success" size="medium" icon="el-icon-plus">关注</el-button>
        </div>
        <span style="font-size: 14px; color: #909399;">{{description}}</span>
      </div>
      <div class="discuss-item-main" v-html="content"/>
      <div class="discuss-item-footer">
        <div style="display: flex">
          <div style="display: flex; justify-content: center;">
            <svg t="1729492886103" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4245" width="16" height="16" @click="isLike = !isLike">
              <path d="M64 483.04V872c0 37.216 30.144 67.36 67.36 67.36H192V416.32l-60.64-0.64A67.36 67.36 0 0 0 64 483.04zM857.28 344.992l-267.808 1.696c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-68.832-155.488-137.568-145.504-60.608 8.8-67.264 61.184-67.264 126.816v59.264c0 76.064-63.84 140.864-137.856 148L256 416.96v522.4h527.552a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824z" :fill="isLike?'#F56C6C':'#909399'" p-id="4246"></path>
            </svg>
            <span style="margin-left: 10px; font-weight: bold; color: #909399" >{{likeNum}}</span>
          </div>
          <el-divider direction="vertical"/>
          <svg v-if="!isCollection" t="1729493620357" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4409" width="16" height="16" @click="isCollection = true">
            <path d="M512 720.416l232.096 171.904a19.2 19.2 0 0 0 16.864 2.88c9.536-2.88 14.592-12.224 11.84-20.864l-88.96-281.6 237.344-176.96a16.48 16.48 0 0 0 6.816-13.152c0-9.152-7.904-16.928-18.112-16.96l-292-0.448-88.48-277.12a17.568 17.568 0 0 0-11.776-11.264c-9.856-2.976-20.16 2.304-23.04 11.264l-88.48 277.12-292 0.48a18.624 18.624 0 0 0-14.624 6.944 16.096 16.096 0 0 0 3.328 23.136l237.376 176.96-88.992 281.6a15.904 15.904 0 0 0 2.72 14.688c6.048 7.744 17.856 9.312 25.984 3.296L512 720.416z m-194.016 223.36a83.008 83.008 0 0 1-114.56-15.424 79.904 79.904 0 0 1-13.28-73.28l75.296-238.24-200.864-149.76a80.096 80.096 0 0 1-15.424-113.92 82.624 82.624 0 0 1 64.864-31.456l245.312-0.384 74.304-232.672c13.6-42.56 59.52-66.112 102.56-53.024A81.536 81.536 0 0 1 590.4 88.64l74.304 232.64 245.312 0.416c45.184 0.064 82.08 36.16 82.016 81.024a80.48 80.48 0 0 1-32.576 64.352l-200.864 149.76 75.296 238.24c13.568 42.976-11.072 88.352-54.496 101.408a83.2 83.2 0 0 1-73.344-12.736L512 800.064l-194.016 143.68z" fill="#909399" p-id="4410"></path>
          </svg>
          <svg v-if="isCollection" t="1729493738069" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4571" width="16" height="16" @click="isCollection = false">
            <path d="M512 760.224L298.944 918.08c-22.08 16.32-53.536 12.16-70.272-9.376a47.904 47.904 0 0 1-8-43.968l82.144-259.936-219.136-163.328a48.096 48.096 0 0 1-9.376-68.544c9.472-12.096 24.192-19.2 39.776-19.2l268.64-0.448 81.408-254.88c8.224-25.792 36.32-40.16 62.784-32.16 15.744 4.8 28.064 16.8 32.96 32.16l81.408 254.88 268.64 0.448c27.712 0.032 50.112 21.952 50.08 48.96 0 15.2-7.296 29.536-19.68 38.784l-219.136 163.328 82.144 259.936c8.16 25.792-6.688 53.184-33.152 61.12a51.2 51.2 0 0 1-45.12-7.776L512 760.224z" fill="#E6A23C" p-id="4572"></path>
          </svg>
          <svg t="1729493984159" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4733" width="16" height="16" style="margin-left: 10px">
            <path d="M397.184 642.112a192 192 0 1 1-10.144-270.464L554.592 262.4a144 144 0 1 1 36.8 52.416l-165.44 107.84c14.08 26.72 22.016 57.12 22.016 89.376a191.36 191.36 0 0 1-15.808 76.384l241.056 135.584a144 144 0 1 1-28.992 57.12l-247.072-138.976z" fill="#909399" p-id="4734"></path>
          </svg>
        </div>
        <el-button size="mini" type="primary" icon="el-icon-edit" @click="showComment = !showComment">发表评论</el-button>
      </div>
    </div>
    <PostComment :show-comment="showComment" size="medium" :level="1" @level-submit="handleSubmit"/>
  </div>
</template>

<script>
import PostComment from "@/components/Discuss/PostComment.vue";
import eventBus from "@/assets/js/eventBus";
export default {
  name: "DiscussItem",
  components:{PostComment},
  data(){
    return {
      title:'我是标题',
      description:'gao，发布于2024-10-21',
      content:'<img src="https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg" style="max-width: 100%"/><br>想🦌',
      isLike:false,//点赞情况
      isCollection:false,
      likeNum: 0,//点赞数
      showComment:false,
    }
  },
  methods:{
    handleSubmit(form){
      eventBus.$emit("top-submit",form)
    }
  }
}
</script>

<style scoped>
.discuss-item-content{
  border: #DCDFE6 2px solid;
  width: 650px;
  border-radius: 5px;
  padding: 10px;
  background-color: white;
}
.discuss-item-header{
  border-bottom: 1px solid #dddddd;
  padding-bottom: 10px;
}
.discuss-item-main{
  padding: 20px;
  border-bottom: 1px solid #dddddd;
}
.discuss-item-footer{
  padding: 15px 15px 0px 15px;
  display: flex;
  justify-content:space-between;
  align-items: center;
}
.icon:hover{
  cursor:pointer;
}

</style>