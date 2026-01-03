<template>
  <div class="comment-content">
    <div class="comment-header">
      <el-avatar size="medium"/>
      <span style="margin-left:10px; display: flex;align-items: center">{{commentData.username}}</span>
    </div>
    <div class="comment-main-content" v-html="commentData.content"></div>
    <div class="comment-rooter">
      <div style="display: flex; justify-content: center;">
        <svg t="1729492886103" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4245" width="16" height="16" @click="isLike = !isLike">
          <path d="M64 483.04V872c0 37.216 30.144 67.36 67.36 67.36H192V416.32l-60.64-0.64A67.36 67.36 0 0 0 64 483.04zM857.28 344.992l-267.808 1.696c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-68.832-155.488-137.568-145.504-60.608 8.8-67.264 61.184-67.264 126.816v59.264c0 76.064-63.84 140.864-137.856 148L256 416.96v522.4h527.552a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824z" :fill="isLike?'#F56C6C':'#909399'" p-id="4246"></path>
        </svg>
        <span style="margin-left: 5px; font-weight: bold; color: #909399; font-size: 14px">{{likeNum > 0 ? likeNum : '点赞'}}</span>
      </div>
      <div style="margin-left: 10px; display: flex; justify-content: center;">
        <svg t="1729586795636" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="920" width="16" height="16" @click="showComment = !showComment">
          <path d="M853.9 101.5H170.1c-58.4 0-105.9 49.2-105.9 109.7V700c0 60.5 47.5 109.7 105.9 109.7h211.3l110.2 112.9 110.2-112.9H854c58.4 0 105.9-49.2 105.9-109.7V211.2c-0.1-60.5-47.6-109.7-106-109.7zM307 499c-27.6 0-50-22.4-50-50s22.4-50 50-50 50 22.4 50 50-22.4 50-50 50z m205 0c-27.6 0-50-22.4-50-50s22.4-50 50-50 50 22.4 50 50-22.4 50-50 50z m205 0c-27.6 0-50-22.4-50-50s22.4-50 50-50 50 22.4 50 50-22.4 50-50 50z" fill="#909399" p-id="921"></path>
        </svg>
        <span style="margin-left: 5px; font-weight: bold; color: #909399; font-size: 14px;">
          评论
        </span>
      </div>
    </div>
    <div class="sub-comment-list-content">
      <ul class="sub-comment-list">
        <li v-for="item in commentData.children" :key="item.id">
          <div class="comment-header">
            <el-avatar size="small"/>
            <span style="margin-left:10px; display: flex;align-items: center">{{item.username}}</span>
          </div>
          <div class="comment-main-content" v-html="item.content"></div>
          <div class="comment-rooter">
            <div style="display: flex; justify-content: center;">
              <svg t="1729492886103" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4245" width="16" height="16" @click="isSubLike = !isSubLike">
                <path d="M64 483.04V872c0 37.216 30.144 67.36 67.36 67.36H192V416.32l-60.64-0.64A67.36 67.36 0 0 0 64 483.04zM857.28 344.992l-267.808 1.696c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-68.832-155.488-137.568-145.504-60.608 8.8-67.264 61.184-67.264 126.816v59.264c0 76.064-63.84 140.864-137.856 148L256 416.96v522.4h527.552a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824z" :fill="isSubLike?'#F56C6C':'#909399'" p-id="4246"></path>
              </svg>
              <span style="margin-left: 5px; font-weight: bold; color: #909399; font-size: 14px">{{likeSubNum > 0 ? likeSubNum : '点赞'}}</span>
            </div>
            <div style="margin-left: 10px; display: flex; justify-content: center;">
              <svg t="1729586795636" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="920" width="16" height="16" @click="showComment = true">
                <path d="M853.9 101.5H170.1c-58.4 0-105.9 49.2-105.9 109.7V700c0 60.5 47.5 109.7 105.9 109.7h211.3l110.2 112.9 110.2-112.9H854c58.4 0 105.9-49.2 105.9-109.7V211.2c-0.1-60.5-47.6-109.7-106-109.7zM307 499c-27.6 0-50-22.4-50-50s22.4-50 50-50 50 22.4 50 50-22.4 50-50 50z m205 0c-27.6 0-50-22.4-50-50s22.4-50 50-50 50 22.4 50 50-22.4 50-50 50z m205 0c-27.6 0-50-22.4-50-50s22.4-50 50-50 50 22.4 50 50-22.4 50-50 50z" fill="#909399" p-id="921"></path>
              </svg>
              <span style="margin-left: 5px; font-weight: bold; color: #909399; font-size: 14px;">评论</span>
            </div>
          </div>
        </li>
      </ul>
      <PostComment size="small" :level="2" @level-submit="handleSubmit" :show-comment="showComment"/>
    </div>
    <!-- 预览层 -->
    <transition name="fade">
      <div v-if="showPreview" ref="previewOverlay" class="preview-overlay">
        <img :src="currentImageSrc" alt="Large Preview">
        <button @click="closePreview">关闭</button>
      </div>
    </transition>
  </div>
</template>

<script>
import PostComment from "@/components/Discuss/PostComment.vue";

export default {
  name: "CommentItem",
  components: {PostComment},
  props:{
    commentData:{
      type: Object,
      required:true
    }
  },
  data() {
    return {
      showPreview: false,
      isLike:false,
      likeNum:0,
      isSubLike:false,
      likeSubNum:0,
      currentImageSrc: '',
      showComment: false
    };
  },
  methods: {
    openPreview(event) {
      // 获取点击的图片的src
      this.currentImageSrc = event.target.src;
      this.showPreview = true;
      // 设置预览层的样式
      document.body.style.overflow = 'hidden';
    },
    closePreview() {
      this.showPreview = false;
      document.body.style.overflow = 'auto';
    },
    handleSubmit(form) {
      this.subComments=form
    }
  },
  mounted() {
    // 监听content中的图片点击事件
    const imgElements = this.$el.querySelectorAll('.comment-main-content img');
    imgElements.forEach(img => {
      img.addEventListener('click', this.openPreview);
    });
  },
  beforeDestroy() {
    // 移除监听器
    const imgElements = this.$el.querySelectorAll('.comment-main-content img');
    imgElements.forEach(img => {
      img.removeEventListener('click', this.openPreview);
    });
  }
}
</script>

<style scoped>
.comment-content{
  width: 650px;
  padding: 10px;
  border-bottom: 1px #dddddd solid;
}
.comment-header {
  display: flex;
}

.preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
}

.preview-overlay img {
  max-width: 90%;
  max-height: 90%;
}

.preview-overlay button {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease-in-out;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.comment-main-content{
  padding: 10px;
}
.icon:hover{
  cursor:pointer;
}
.comment-rooter{
  display: flex;
}
.sub-comment-list-content{
  padding: 15px 25px 25px 25px;
}
.sub-comment-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
</style>