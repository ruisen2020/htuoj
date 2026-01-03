<template>
  <div class="blogger-container">
    <el-card>

      <el-tabs v-model="activeName" type="card" style="width: 800px; font-size: 18px" @tab-click="handleClick">
        <el-tab-pane label="推荐" name="first" />
        <el-tab-pane label="公告" name="second" />
        <el-tab-pane label="讨论" name="third" />
        <el-tab-pane label="分享" name="fourth" />
        <el-tab-pane label="题解" name="fifth" />
        <el-tab-pane label="关注" name="sixth" />
      </el-tabs>
      <van-list offset="50" v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
        <div v-for="item in listData" :key="item.articleId" :title="item">
          <BloggerListItem :data="item" @like-event = "likeEvent" @collect-event="collectEvent"/>
        </div>
      </van-list>
    </el-card>
  </div>
</template>

<script>
import BloggerListItem from "@/components/Forum/BloggerListItem.vue";
import api from "@/common/api";
import { List as VanList } from 'vant';
// 2. 引入组件样式
import 'vant/lib/index.css';
export default {
  name: "BloggerList",
  components: { BloggerListItem, VanList },
  data() {
    return {
      loading: false,
      finished: false,
      activeName: "first",
      //后台传来的列表数据
      listData: [],
      total: 20,
      formData: {
        targetType: 0,
        current: 1,
        size: 10,
        categoryId: null,
        userId: null,
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      setTimeout(() => {
        this.formData.current++;
        this.fetchData();
        // 加载状态结束
        this.loading = false;
        console.log(this.total);
        // 数据全部加载完成
        if (this.listData.length >= this.total) {
          this.finished = true;
        }
      }, 1000);
    },
    likeEvent(data) {
      if (data.isLike == true) {
        data.likeCount = data.likeCount - 1;
      } else {
        data.likeCount = data.likeCount + 1;
      }
      data.isLike = !data.isLike;
      api.addLike({
        targetType: 0,
        targetId: data.articleId,
      });
    },
    collectEvent(data) {
      if (data.isCollect == true) {
        data.collectCount = data.collectCount - 1;
      } else {
        data.collectCount = data.collectCount + 1;
      }
      data.isCollect = !data.isCollect;
      api.addCollect({
        targetType: 0,
        targetId: data.articleId,
        isCollect: data.isCollect,
      });
    },
    fetchData() {
      api.getArticleList(this.formData).then((response) => {
        if (response.data.code === "200") {
          this.listData = this.listData.concat(response.data.data.records);
          this.total = response.data.data.total;
        }
      });
    },
    handleCurrentChange() {
      this.fetchData();
    },
    handleClick(tab) {
      this.listData = [];
      this.formData.current = 1;
      this.formData.size = 10;
      // 向子组件传递滤值（推荐，算法...），展示不同列表
      if (tab.name === "first") {
        this.formData.categoryId = null;
      } else if (tab.name === "second") {
        this.formData.categoryId = 1;
      } else if (tab.name === "third") {
        this.formData.categoryId = 2;
      } else if (tab.name === "fourth") {
        this.formData.categoryId = 3;
      } else if (tab.name === "fifth") {
        this.formData.categoryId = 4;
      } else if (tab.name === "sixth") {
        this.formData.categoryId = 5;
      }
      this.fetchData();
    },
  },
};
</script>

<style scoped>
/* .search :hover{
   border: 1px solid #ebeef5;
   border-radius: 16px;
} */
/* .el-card.is-always-shadow, .el-card.is-hover-shadow:focus, .el-card.is-hover-shadow:hover{
  box-shadow: none;
} */
/* .blogger-container{
  height: 1000px;
  background: red;
} */
::v-deep .el-input__inner {
  border: none;
}

::v-deep .el-card {
  border-radius: 13px;
  /* box-shadow: none; */
  border: none;
}

::v-deep .el-card__body {
  border-radius: 13px;
}

::v-deep .el-card .is-always-shadow {
  box-shadow: none;
}

::v-deep .el-tabs__item {
  color: #262626bf;
  border-radius: 8px;
  /* //margin-left: 10px; border: 0 solid; */
}

::v-deep .el-tabs--card>.el-tabs__header .el-tabs__item {
  font-size: 16px;
  border-left: none;
}

::v-deep .el-tabs--card>.el-tabs__header {
  border: none;
}

/**选中 */
::v-deep .el-tabs__item.is-active {
  background-color: #000a200d;
  border: none;
}

::v-deep .el-table--border .el-table__cell {
  border-right: none !important;
}

::v-deep .el-tabs__nav,
.is-top {
  border: none !important;
}
</style>