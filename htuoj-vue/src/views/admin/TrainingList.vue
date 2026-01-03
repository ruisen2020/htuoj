<template>
  <div class="training-list-container">
    <div class="page-header">
      <h2>训练管理</h2>
      <el-button type="primary" size="small" @click="handleAddTraining">
        <i class="el-icon-plus"></i>
        创建训练
      </el-button>
    </div>
    
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="训练名称">
          <el-input v-model="searchForm.title" placeholder="请输入训练名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="创建者">
          <el-input v-model="searchForm.creator" placeholder="请输入创建者" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="公开" value="public"></el-option>
            <el-option label="私有" value="private"></el-option>
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
      <el-table :data="trainingList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="训练名称">
          <template slot-scope="scope">
            <router-link :to="'/training/' + scope.row.id" class="training-title-link">
              {{ scope.row.title }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="creator" label="创建者" width="120"></el-table-column>
        <el-table-column prop="problemCount" label="题目数" width="100" align="center"></el-table-column>
        <el-table-column prop="participantCount" label="参与人数" width="100" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="'public'"
              :inactive-value="'private'"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200" align="center">
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
  name: "AdminTrainingList",
  data() {
    return {
      // 搜索表单
      searchForm: {
        title: '',
        creator: '',
        status: ''
      },
      
      // 分页信息
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 训练列表数据
      trainingList: [],
      loading: false
    };
  },
  created() {
    this.fetchTrainingList();
  },
  methods: {
    // 获取训练列表
    fetchTrainingList() {
      this.loading = true;
      const params = {
        ...this.searchForm,
        current: this.pagination.current,
        size: this.pagination.size
      };
      
      api.getTrainingList(params).then(response => {
        if (response.data.code === "200") {
          this.trainingList = response.data.data.records;
          this.pagination.total = response.data.data.total;
        } else {
          this.$message.error('获取训练列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取训练列表失败');
      });
    },
    
    // 处理搜索
    handleSearch() {
      this.pagination.current = 1;
      this.fetchTrainingList();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        title: '',
        creator: '',
        status: ''
      };
      this.handleSearch();
    },
    
    // 处理分页大小变更
    handleSizeChange(val) {
      this.pagination.size = val;
      this.fetchTrainingList();
    },
    
    // 处理当前页变更
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.fetchTrainingList();
    },
    
    // 添加训练
    handleAddTraining() {
      this.$router.push('/admin/training-create');
    },
    
    // 编辑训练
    handleEdit(row) {
      this.$router.push(`/admin/training-create/${row.id}`);
    },
    
    // 题目管理
    handleProblems(row) {
      // 实现题目管理功能
      this.$router.push(`/admin/training-problems/${row.id}`);
    },
    
    // 处理删除训练
    handleDelete(row) {
      this.$confirm('确定要删除该训练吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteTraining({id: row.id}).then(response => {
          if (response.data.code === "200") {
            this.$message.success('删除成功');
            this.fetchTrainingList();
          } else {
            this.$message.error(response.data.message || '删除失败');
          }
        });
      }).catch(() => {});
    },
    
    // 处理状态变更
    handleStatusChange(row) {
      const statusText = row.status === 'public' ? '公开' : '私有';
      
      api.updateTraining({
        id: row.id,
        status: row.status
      }).then(response => {
        if (response.data.code === "200") {
          this.$message.success(`已将训练【${row.title}】设为${statusText}`);
        } else {
          this.$message.error(response.data.message || '设置失败');
          // 恢复原状态
          row.status = row.status === 'public' ? 'private' : 'public';
        }
      }).catch(() => {
        // 恢复原状态
        row.status = row.status === 'public' ? 'private' : 'public';
        this.$message.error('设置失败');
      });
    }
  }
};
</script>

<style scoped>
.training-list-container {
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

.training-title-link {
  color: #409EFF;
  text-decoration: none;
}

.training-title-link:hover {
  text-decoration: underline;
}
</style> 