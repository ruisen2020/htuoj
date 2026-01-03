<template>
  <div class="article-list-container">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" size="small" @click="handleAddArticle">
        <i class="el-icon-plus"></i>
        创建文章
      </el-button>
    </div>
    
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入文章标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="searchForm.author" placeholder="请输入作者" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="已发布" value="published"></el-option>
            <el-option label="草稿" value="draft"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="articleList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="articleId" label="ID" width="60" align="center"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="200">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.title" placement="top" :disabled="scope.row.title.length < 30">
              <router-link :to="'/home/articleInfo/' + scope.row.articleId" class="article-title-link" target="_blank">
                {{ scope.row.title.length > 30 ? scope.row.title.substring(0, 30) + '...' : scope.row.title }}
              </router-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="120">
          <template slot-scope="scope">
            <router-link :to="'/home/stuInfo/' + scope.row.userId" class="user-link" target="_blank">
              {{ scope.row.author }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="80" align="center"></el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80" align="center"></el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80" align="center"></el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'published'" type="success">已发布</el-tag>
            <el-tag v-else-if="scope.row.status === 'draft'" type="info">草稿</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handlePreview(scope.row)">预览</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </el-card>
    
    <!-- 文章预览对话框 -->
    <el-dialog
      title="文章预览"
      :visible.sync="previewVisible"
      fullscreen
      :destroy-on-close="true">
      <div class="article-preview">
        <h1 class="article-preview-title">{{ previewArticle.title }}</h1>
        <div class="article-preview-info">
          <span>作者: {{ previewArticle.author }}</span>
          <span>分类: {{ previewArticle.categoryName }}</span>
          <span>发布时间: {{ previewArticle.createTime }}</span>
        </div>
        <div class="article-preview-content" v-html="previewArticle.content"></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEdit(previewArticle)">编辑</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminArticleList",
  data() {
    return {
      // 搜索表单
      searchForm: {
        title: '',
        author: '',
        categoryId: '',
        status: ''
      },
      
      // 分页信息
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 文章列表数据
      articleList: [],
      loading: false,
      
      // 分类选项
      categoryOptions: [],
      
      // 预览对话框
      previewVisible: false,
      previewArticle: {
        title: '',
        author: '',
        categoryName: '',
        createTime: '',
        content: ''
      }
    };
  },
  created() {
    this.fetchArticleList();
    this.fetchCategoryOptions();
  },
  methods: {
    // 获取文章列表
    fetchArticleList() {
      this.loading = true;
      const params = {
        ...this.searchForm,
        current: this.pagination.current,
        size: this.pagination.size
      };
      
      api.getArticleList(params).then(response => {
        if (response.data.code === "200") {
          this.articleList = response.data.data.records;
          this.pagination.total = response.data.data.total;
        } else {
          this.$message.error('获取文章列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取文章列表失败');
      });
    },
    
    // 获取分类选项
    fetchCategoryOptions() {
      // 假设有获取分类列表的API
      api.getArticleListByCategory({current: 1, size: 100}).then(response => {
        if (response.data.code === "200") {
          this.categoryOptions = response.data.data.records.map(item => {
            return {
              value: item.id,
              label: item.name
            };
          });
        }
      });
    },
    
    // 处理搜索
    handleSearch() {
      this.pagination.current = 1;
      this.fetchArticleList();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        title: '',
        author: '',
        categoryId: '',
        status: ''
      };
      this.handleSearch();
    },
    
    // 处理分页大小变更
    handleSizeChange(val) {
      this.pagination.size = val;
      this.fetchArticleList();
    },
    
    // 处理当前页变更
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.fetchArticleList();
    },
    
    // 添加文章
    handleAddArticle() {
      this.$router.push('/markdownEditor');
    },
    
    // 编辑文章
    handleEdit(row) {
      this.$router.push(`/markdownEditor?articleId=${row.articleId}`);
    },
    
    // 预览文章
    handlePreview(row) {
      api.getArticleById(row.articleId).then(response => {
        if (response.data.code === "200") {
          this.previewArticle = response.data.data;
          this.previewVisible = true;
        } else {
          this.$message.error(response.data.message || '获取文章详情失败');
        }
      });
    },
    
    // 处理删除文章
    handleDelete(row) {
      this.$confirm('确定要删除该文章吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteArticle({articleId: row.articleId}).then(response => {
          if (response.data.code === "200") {
            this.$message.success('删除成功');
            this.fetchArticleList();
          } else {
            this.$message.error(response.data.message || '删除失败');
          }
        });
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.article-list-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.article-title-link, .user-link {
  color: #409EFF;
  text-decoration: none;
}

.article-title-link:hover, .user-link:hover {
  text-decoration: underline;
}

.article-preview {
  padding: 20px;
}

.article-preview-title {
  text-align: center;
  margin-bottom: 20px;
}

.article-preview-info {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  color: #909399;
  font-size: 14px;
}

.article-preview-info span {
  margin: 0 10px;
}

.article-preview-content {
  line-height: 1.6;
}
</style> 