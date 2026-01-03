<template>
  <div class="category-container">
    <div class="page-header">
      <h2>题目分类管理</h2>
      <el-button type="primary" size="small" @click="handleAddCategory">
        <i class="el-icon-plus"></i>
        添加分类
      </el-button>
    </div>
    
    <el-card shadow="never">
      <el-table :data="categoryList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="分类名称" width="150"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column prop="problemCount" label="题目数量" width="100" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" fixed="right" width="150" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)" :disabled="scope.row.problemCount > 0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 分类表单对话框 -->
    <el-dialog :title="isEdit ? '编辑分类' : '添加分类'" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="categoryForm" :model="categoryForm" :rules="categoryRules" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="categoryForm.description" rows="3" placeholder="请输入分类描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminProblemCategory",
  data() {
    return {
      categoryList: [],
      loading: false,
      
      // 分类表单
      dialogVisible: false,
      isEdit: false,
      categoryForm: {
        id: null,
        name: '',
        description: ''
      },
      categoryRules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.fetchCategoryList();
  },
  methods: {
    // 获取分类列表
    fetchCategoryList() {
      this.loading = true;
      
      api.getProblemCategories().then(response => {
        if (response.data.code === "200") {
          this.categoryList = response.data.data;
        } else {
          this.$message.error('获取分类列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取分类列表失败');
      });
    },
    
    // 添加分类
    handleAddCategory() {
      this.isEdit = false;
      this.categoryForm = {
        id: null,
        name: '',
        description: ''
      };
      this.dialogVisible = true;
    },
    
    // 编辑分类
    handleEdit(row) {
      this.isEdit = true;
      this.categoryForm = {
        id: row.id,
        name: row.name,
        description: row.description
      };
      this.dialogVisible = true;
    },
    
    // 重置表单
    resetForm() {
      this.$refs.categoryForm && this.$refs.categoryForm.resetFields();
    },
    
    // 提交表单
    submitForm() {
      this.$refs.categoryForm.validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 更新分类
            api.updateProblemCategory(this.categoryForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('更新成功');
                this.dialogVisible = false;
                this.fetchCategoryList();
              } else {
                this.$message.error(response.data.message || '更新失败');
              }
            });
          } else {
            // 添加分类
            api.addProblemCategory(this.categoryForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('添加成功');
                this.dialogVisible = false;
                this.fetchCategoryList();
              } else {
                this.$message.error(response.data.message || '添加失败');
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    
    // 处理删除分类
    handleDelete(row) {
      if (row.problemCount > 0) {
        this.$message.warning('该分类下有题目，不能删除');
        return;
      }
      
      this.$confirm('确定要删除该分类吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteProblemCategory({ categoryId: row.id }).then(response => {
          if (response.data.code === "200") {
            this.$message.success('删除成功');
            this.fetchCategoryList();
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
.category-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style> 