<template>
  <div class="contest-problem-list-container">
    <div class="page-header">
      <h2>竞赛题目管理</h2>
      <div class="header-actions">
        <el-button type="primary" size="small" @click="handleAddProblem">
          <i class="el-icon-plus"></i>
          添加题目
        </el-button>
        <el-button type="success" size="small" @click="handleImportProblems">
          <i class="el-icon-upload2"></i>
          批量导入
        </el-button>
      </div>
    </div>
    
    <el-card shadow="never">
      <el-table :data="problemList" style="width: 100%" v-loading="loading">
        <el-table-column prop="problemId" label="题目ID" width="100"></el-table-column>
        <el-table-column prop="title" label="题目标题"></el-table-column>
        <el-table-column prop="score" label="分值" width="100"></el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100">
          <template slot-scope="scope">
            <el-tag :type="getDifficultyType(scope.row.difficulty)">
              {{ getDifficultyLabel(scope.row.difficulty) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
    
    <!-- 添加/编辑题目对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="题目" prop="problemId">
          <el-select v-model="form.problemId" placeholder="请选择题目" filterable>
            <el-option
              v-for="item in problemOptions"
              :key="item.problemId"
              :label="item.title"
              :value="item.problemId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input-number v-model="form.score" :min="1" :max="100"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button>
      </div>
    </el-dialog>
    
    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入题目" :visible.sync="importDialogVisible" width="40%">
      <el-upload
        class="upload-demo"
        drag
        action="/api/admin/contest/problems/import"
        :headers="uploadHeaders"
        :data="{ contestId: contestId }"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        accept=".xlsx,.xls">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传 xlsx/xls 文件</div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminContestProblemList",
  data() {
    return {
      contestId: null,
      loading: false,
      submitLoading: false,
      problemList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      importDialogVisible: false,
      dialogTitle: "添加题目",
      form: {
        problemId: "",
        score: 100
      },
      rules: {
        problemId: [
          { required: true, message: "请选择题目", trigger: "change" }
        ],
        score: [
          { required: true, message: "请输入分值", trigger: "blur" }
        ]
      },
      uploadHeaders: {
        // 如果需要认证，在这里添加认证头
      },
      problemOptions: []
    };
  },
  created() {
    this.contestId = this.$route.params.contestId;
    this.fetchProblemList();
    this.fetchProblemOptions();
  },
  methods: {
    // 获取竞赛题目列表
    fetchProblemList() {
      this.loading = true;
      api.getContestProblems({
        contestId: this.contestId,
        page: this.currentPage,
        size: this.pageSize
      }).then(response => {
        if (response.data.code === "200") {
          this.problemList = response.data.data.records;
          this.total = response.data.data.total;
        } else {
          this.$message.error("获取竞赛题目列表失败");
        }
        this.loading = false;
      });
    },
    
    // 获取可选题目列表
    fetchProblemOptions() {
      api.getProblemList({ size: 1000 }).then(response => {
        if (response.data.code === "200") {
          this.problemOptions = response.data.data.records;
        }
      });
    },
    
    // 处理分页大小变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchProblemList();
    },
    
    // 处理页码变化
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchProblemList();
    },
    
    // 获取难度标签类型
    getDifficultyType(difficulty) {
      const types = {
        EASY: "success",
        MEDIUM: "warning",
        HARD: "danger"
      };
      return types[difficulty] || "info";
    },
    
    // 获取难度标签文本
    getDifficultyLabel(difficulty) {
      const labels = {
        EASY: "简单",
        MEDIUM: "中等",
        HARD: "困难"
      };
      return labels[difficulty] || "未知";
    },
    
    // 添加题目
    handleAddProblem() {
      this.dialogTitle = "添加题目";
      this.form = {
        problemId: "",
        score: 100
      };
      this.dialogVisible = true;
    },
    
    // 编辑题目
    handleEdit(row) {
      this.dialogTitle = "编辑题目";
      this.form = { ...row };
      this.dialogVisible = true;
    },
    
    // 删除题目
    handleDelete(row) {
      this.$confirm("确认删除该题目吗？", "提示", {
        type: "warning"
      }).then(() => {
        api.deleteContestProblem({
          contestId: this.contestId,
          problemId: row.problemId
        }).then(response => {
          if (response.data.code === "200") {
            this.$message.success("删除成功");
            this.fetchProblemList();
          } else {
            this.$message.error(response.data.message || "删除失败");
          }
        });
      });
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          const params = {
            ...this.form,
            contestId: this.contestId
          };
          
          api.addContestProblem(params).then(response => {
            if (response.data.code === "200") {
              this.$message.success("保存成功");
              this.dialogVisible = false;
              this.fetchProblemList();
            } else {
              this.$message.error(response.data.message || "保存失败");
            }
            this.submitLoading = false;
          });
        }
      });
    },
    
    // 批量导入题目
    handleImportProblems() {
      this.importDialogVisible = true;
    },
    
    // 导入成功处理
    handleImportSuccess(response) {
      if (response.code === "200") {
        this.$message.success("导入成功");
        this.importDialogVisible = false;
        this.fetchProblemList();
      } else {
        this.$message.error(response.message || "导入失败");
      }
    },
    
    // 导入失败处理
    handleImportError() {
      this.$message.error("导入失败，请检查文件格式是否正确");
    }
  }
};
</script>

<style scoped>
.contest-problem-list-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.delete-btn {
  color: #F56C6C;
}

.delete-btn:hover {
  color: #f78989;
}
</style> 