<!--评论列表，从此获取后端传来的评论-->
<template>
  <div class="comment-list-content">
    <ul class="comment-list">
      <li v-for="item in comments" :key="item.data.id" v-show="item.showComment">
        <CommentItem :comment-data="item.data"/>
      </li>
    </ul>
    <p v-if="noMore" style="flex: 1; text-align: center">没有更多评论了</p>
    <p v-loading="loading" element-loading-spinner="el-icon-loading" style="padding-top: 10px"/>
  </div>
</template>

<script>
import CommentItem from "@/components/Discuss/CommentItem.vue";
import eventBus from "@/assets/js/eventBus";

let count = 0;
let subCount = 0;
export default {
  name: "CommentList",
  components: { CommentItem },
  data() {
    return {
      comments: this.test(),
      loading: false,
      index: 1,
      total: 20,
    };
  },
  computed: {
    noMore() {
      return this.index >= Math.ceil(this.total / 10);
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  methods: {
    handleScroll() {
      if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 10) {
        if (!this.disabled) {
          this.load();
        }
      }
    },
    load() {
      this.loading = true;
      setTimeout(() => {
        // 此处调用后台方法
        this.index++;
        this.comments = this.comments.concat(this.test());
        this.loading = false;
      }, 2000);
    },
    test() {
      return new Array(5).fill(null).map(() => ({
        data: {
          id: count++,
          username: 'gao',
          content: '<p>🦌</p><img style="max-width: 100%; height: 75px; width: 100px" src="https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg" alt="示例图片"/>',
          children:{
            data: {
              id: subCount++,
              username:'child',
              content:'<p>🦌</p><img style="max-width: 100%; height: 75px; width: 100px" src="https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg" alt="示例图片"/>',
            }
          }
        },
        showComment: true,
      }));
    },

  },
  mounted() {
    eventBus.$on("top-submit",(form)=>{
      this.comments.unshift({
        data:{
          id:count++,
          username:'gao',
          content:'<p>'+form.comment+'</p>'
        },
        showComment: true,
      });
    })
    window.addEventListener('scroll', this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll);
  },
};
</script>

<style scoped>
.comment-list-content {
  background-color: white;
  border: #DCDFE6 2px solid;
  border-radius: 5px;
  height: auto; /* 无需固定高度 */
  overflow: visible; /* 不需要内部滚动条 */
}

.comment-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
</style>