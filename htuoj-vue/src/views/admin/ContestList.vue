<template>
  <div class="contest-list-container">
    <div class="page-header">
      <h2>竞赛管理</h2>
      <el-button type="primary" size="small" @click="handleAddContest">
        <i class="el-icon-plus"></i>
        添加竞赛
      </el-button>
    </div>
    
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="竞赛名称">
          <el-input v-model="searchForm.title" placeholder="请输入竞赛名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="竞赛类型">
          <el-select v-model="searchForm.type" placeholder="请选择竞赛类型" clearable>
            <el-option label="公开竞赛" value="public"></el-option>
            <el-option label="私有竞赛" value="private"></el-option>
            <el-option label="团队竞赛" value="team"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="未开始" value="not_started"></el-option>
            <el-option label="进行中" value="in_progress"></el-option>
            <el-option label="已结束" value="ended"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 竞赛列表 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="contestList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="contestId" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="竞赛名称">
          <template slot-scope="scope">
            <router-link :to="'/home/contestDetail/' + scope.row.contestId" class="contest-title-link">
              {{ scope.row.title }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getContestTypeTag(scope.row.type)">
              {{ getContestTypeLabel(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180"></el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="180"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleProblems(scope.row)">题目管理</el-button>
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
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminContestList",
  data() {
    return {
      // 搜索表单
      searchForm: {
        title: '',
        type: '',
        status: ''
      },
      
      // 分页信息
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 竞赛列表数据
      contestList: [],
      loading: false
    };
  },
  created() {
    this.fetchContestList();
  },
  methods: {
    // 获取竞赛列表
    fetchContestList() {
      this.loading = true;
      const params = {
        ...this.searchForm,
        current: this.pagination.current,
        size: this.pagination.size
      };
      
      api.getContestList(params).then(response => {
        if (response.data.code === "200") {
          this.contestList = response.data.data.records;
          this.pagination.total = response.data.data.total;
        } else {
          this.$message.error('获取竞赛列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取竞赛列表失败');
      });
    },
    
    // 处理搜索
    handleSearch() {
      this.pagination.current = 1;
      this.fetchContestList();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        title: '',
        type: '',
        status: ''
      };
      this.handleSearch();
    },
    
    // 处理分页大小变更
    handleSizeChange(val) {
      this.pagination.size = val;
      this.fetchContestList();
    },
    
    // 处理当前页变更
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.fetchContestList();
    },
    
    // 添加竞赛
    handleAddContest() {
      this.$router.push('/admin/contest/edit');
    },
    
    // 编辑竞赛
    handleEdit(row) {
      this.$router.push(`/admin/contest/edit/${row.contestId}`);
    },
    
    // 管理竞赛题目
    handleProblems(row) {
      this.$router.push(`/admin/contest/problems/${row.contestId}`);
    },
    
    // 处理删除竞赛
    handleDelete(row) {
      this.$confirm('确定要删除该竞赛吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteContest({contestId: row.contestId}).then(response => {
          if (response.data.code === "200") {
            this.$message.success('删除成功');
            this.fetchContestList();
          } else {
            this.$message.error(response.data.message || '删除失败');
          }
        });
      }).catch(() => {});
    },
    
    // 获取竞赛类型标签样式
    getContestTypeTag(type) {
      const types = {
        'public': 'success',
        'private': 'info',
        'team': 'warning'
      };
      return types[type] || 'info';
    },
    
    // 获取竞赛类型标签文本
    getContestTypeLabel(type) {
      const labels = {
        'public': '公开竞赛',
        'private': '私有竞赛',
        'team': '团队竞赛'
      };
      return labels[type] || '未知类型';
    },
    
    // 获取状态标签样式
    getStatusType(status) {
      const types = {
        'not_started': 'info',
        'in_progress': 'success',
        'ended': 'warning'
      };
      return types[status] || 'info';
    },
    
    // 获取状态标签文本
    getStatusLabel(status) {
      const labels = {
        'not_started': '未开始',
        'in_progress': '进行中',
        'ended': '已结束'
      };
      return labels[status] || '未知状态';
    }
  }
};
</script>

<style scoped>
.contest-list-container {
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

.contest-title-link {
  color: #409EFF;
  text-decoration: none;
}

.contest-title-link:hover {
  text-decoration: underline;
}
</style> 