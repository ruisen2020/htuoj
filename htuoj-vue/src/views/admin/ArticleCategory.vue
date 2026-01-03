<template>
  <div class="category-container">
    <div class="page-header">
      <h2>文章分类管理</h2>
    </div>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>添加分类</span>
          </div>
          <el-form ref="categoryForm" :model="categoryForm" :rules="categoryRules" label-width="80px">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="categoryForm.name" placeholder="请输入分类名称"></el-input>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="categoryForm.sort" :min="1" :max="99"></el-input-number>
            </el-form-item>
            <el-form-item label="图标" prop="icon">
              <el-input v-model="categoryForm.icon" placeholder="请输入图标class">
                <template slot="prepend">
                  <i :class="categoryForm.icon"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="状态">
              <el-switch
                v-model="categoryForm.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                :active-value="'enable'"
                :inactive-value="'disable'">
              </el-switch>
            </el-form-item>
            <el-form-item label="描述">
              <el-input type="textarea" v-model="categoryForm.description" rows="3" placeholder="请输入分类描述"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('categoryForm')">{{ isEdit ? '更新' : '添加' }}</el-button>
              <el-button v-if="isEdit" @click="resetForm">取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>分类列表</span>
            <div class="category-search">
              <el-input
                placeholder="请输入分类名称"
                v-model="searchKeyword"
                clearable
                @keyup.enter.native="handleSearch"
                style="width: 200px;">
                <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
              </el-input>
            </div>
          </div>
          
          <el-table :data="filteredCategoryList" border style="width: 100%" v-loading="loading">
            <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
            <el-table-column prop="name" label="分类名称" width="150">
              <template slot-scope="scope">
                <span>
                  <i :class="scope.row.icon" style="margin-right: 5px;"></i>
                  {{ scope.row.name }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="articleCount" label="文章数" width="80" align="center"></el-table-column>
            <el-table-column prop="sort" label="排序" width="80" align="center"></el-table-column>
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 'enable'" type="success">启用</el-tag>
                <el-tag v-else-if="scope.row.status === 'disable'" type="info">禁用</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button 
                  type="text" 
                  size="small" 
                  style="color: #F56C6C;" 
                  @click="handleDelete(scope.row)"
                  :disabled="scope.row.articleCount > 0">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminArticleCategory",
  data() {
    return {
      // 分类表单
      categoryForm: {
        id: null,
        name: '',
        sort: 1,
        icon: 'el-icon-folder',
        status: 'enable',
        description: ''
      },
      categoryRules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入排序', trigger: 'blur' }
        ]
      },
      isEdit: false,
      
      // 分类列表
      categoryList: [],
      loading: false,
      searchKeyword: ''
    };
  },
  computed: {
    filteredCategoryList() {
      if (!this.searchKeyword) {
        return this.categoryList;
      }
      return this.categoryList.filter(item => 
        item.name.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
        item.description.toLowerCase().includes(this.searchKeyword.toLowerCase())
      );
    }
  },
  created() {
    this.fetchCategoryList();
  },
  methods: {
    // 获取分类列表
    fetchCategoryList() {
      this.loading = true;
      
      // 假设有获取分类列表的API
      api.getArticleListByCategory({current: 1, size: 100}).then(response => {
        if (response.data.code === "200") {
          this.categoryList = response.data.data.records.map(item => {
            return {
              ...item,
              articleCount: Math.floor(Math.random() * 100) // 模拟数据，实际应该从后端获取
            };
          });
        } else {
          this.$message.error('获取分类列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取分类列表失败');
      });
    },
    
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 更新分类
            // 这里应该调用更新分类的API
            this.$message.success('更新成功');
            this.resetForm();
            this.fetchCategoryList();
          } else {
            // 添加分类
            // 这里应该调用添加分类的API
            this.$message.success('添加成功');
            this.resetForm('categoryForm');
            this.fetchCategoryList();
          }
        } else {
          return false;
        }
      });
    },
    
    // 重置表单
    resetForm(formName) {
      if (formName) {
        this.$refs[formName].resetFields();
      }
      this.categoryForm = {
        id: null,
        name: '',
        sort: 1,
        icon: 'el-icon-folder',
        status: 'enable',
        description: ''
      };
      this.isEdit = false;
    },
    
    // 处理搜索
    handleSearch() {
      // 客户端搜索，已通过computed属性实现
    },
    
    // 编辑分类
    handleEdit(row) {
      this.isEdit = true;
      this.categoryForm = {
        id: row.id,
        name: row.name,
        sort: row.sort,
        icon: row.icon,
        status: row.status,
        description: row.description
      };
    },
    
    // 处理删除分类
    handleDelete(row) {
      if (row.articleCount > 0) {
        this.$message.warning('该分类下有文章，不能删除');
        return;
      }
      
      this.$confirm('确定要删除该分类吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 这里应该调用删除分类的API
        this.$message.success('删除成功');
        this.fetchCategoryList();
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.category-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 