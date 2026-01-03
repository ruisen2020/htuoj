<template>
  <div class="tag-container">
    <div class="page-header">
      <h2>标签管理</h2>
      <el-button type="primary" size="small" @click="handleAddTag">
        <i class="el-icon-plus"></i>
        添加标签
      </el-button>
    </div>
    
    <el-card shadow="never">
      <el-table :data="tagList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="标签名称" width="150"></el-table-column>
        <el-table-column prop="color" label="颜色" width="100">
          <template slot-scope="scope">
            <el-tag :color="scope.row.color">{{ scope.row.name }}</el-tag>
          </template>
        </el-table-column>
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
    
    <!-- 标签表单对话框 -->
    <el-dialog :title="isEdit ? '编辑标签' : '添加标签'" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="tagForm" :model="tagForm" :rules="tagRules" label-width="100px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="tagForm.name" placeholder="请输入标签名称"></el-input>
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="tagForm.color"></el-color-picker>
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
  name: "AdminProblemTag",
  data() {
    return {
      tagList: [],
      loading: false,
      
      // 标签表单
      dialogVisible: false,
      isEdit: false,
      tagForm: {
        id: null,
        name: '',
        color: '#409EFF'
      },
      tagRules: {
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        color: [
          { required: true, message: '请选择标签颜色', trigger: 'change' }
        ]
      }
    };
  },
  created() {
    this.fetchTagList();
  },
  methods: {
    // 获取标签列表
    fetchTagList() {
      this.loading = true;
      
      api.getProblemTags().then(response => {
        if (response.data.code === "200") {
          this.tagList = response.data.data;
        } else {
          this.$message.error('获取标签列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取标签列表失败');
      });
    },
    
    // 添加标签
    handleAddTag() {
      this.isEdit = false;
      this.tagForm = {
        id: null,
        name: '',
        color: '#409EFF'
      };
      this.dialogVisible = true;
    },
    
    // 编辑标签
    handleEdit(row) {
      this.isEdit = true;
      this.tagForm = {
        id: row.id,
        name: row.name,
        color: row.color
      };
      this.dialogVisible = true;
    },
    
    // 重置表单
    resetForm() {
      this.$refs.tagForm && this.$refs.tagForm.resetFields();
    },
    
    // 提交表单
    submitForm() {
      this.$refs.tagForm.validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 更新标签
            api.updateProblemTag(this.tagForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('更新成功');
                this.dialogVisible = false;
                this.fetchTagList();
              } else {
                this.$message.error(response.data.message || '更新失败');
              }
            });
          } else {
            // 添加标签
            api.addProblemTag(this.tagForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('添加成功');
                this.dialogVisible = false;
                this.fetchTagList();
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
    
    // 处理删除标签
    handleDelete(row) {
      if (row.problemCount > 0) {
        this.$message.warning('该标签下有题目，不能删除');
        return;
      }
      
      this.$confirm('确定要删除该标签吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteProblemTag({ tagId: row.id }).then(response => {
          if (response.data.code === "200") {
            this.$message.success('删除成功');
            this.fetchTagList();
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
.tag-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style> 