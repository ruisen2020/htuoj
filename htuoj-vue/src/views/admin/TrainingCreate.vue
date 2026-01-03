<template>
  <div class="training-create-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑训练' : '创建训练' }}</h2>
    </div>
    
    <el-card shadow="never">
      <el-form ref="trainingForm" :model="trainingForm" :rules="rules" label-width="100px">
        <el-form-item label="训练名称" prop="title">
          <el-input v-model="trainingForm.title" placeholder="请输入训练名称"></el-input>
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="trainingForm.description" rows="4" placeholder="请输入训练描述"></el-input>
        </el-form-item>
        
        <el-form-item label="公开状态" prop="status">
          <el-radio-group v-model="trainingForm.status">
            <el-radio label="public">公开</el-radio>
            <el-radio label="private">私有</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="trainingForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="trainingForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="难度">
          <el-rate
            v-model="trainingForm.difficulty"
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            :max="5">
          </el-rate>
        </el-form-item>
        
        <el-form-item label="访问密码" v-if="trainingForm.status === 'private'">
          <el-input v-model="trainingForm.password" placeholder="请输入训练访问密码（可选）"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm('trainingForm')">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <div v-if="isEdit" style="margin-top: 20px;">
      <el-card shadow="never" class="problem-card">
        <div slot="header" class="card-header">
          <span>题目管理</span>
          <el-button type="primary" size="small" @click="handleAddProblem">添加题目</el-button>
        </div>
        
        <el-table
          :data="problemList"
          border
          style="width: 100%"
          row-key="id"
          v-loading="problemListLoading">
          <el-table-column type="index" width="50" align="center"></el-table-column>
          <el-table-column prop="problemId" label="ID" width="80" align="center"></el-table-column>
          <el-table-column prop="title" label="题目名称">
            <template slot-scope="scope">
              <router-link :to="'/problem/' + scope.row.problemId" class="problem-title-link" target="_blank">
                {{ scope.row.title }}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="difficulty" label="难度" width="100" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.difficulty === 'easy'" type="success">简单</el-tag>
              <el-tag v-else-if="scope.row.difficulty === 'medium'" type="warning">中等</el-tag>
              <el-tag v-else type="danger">困难</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="moveUp(scope.$index)" :disabled="scope.$index === 0">
                <i class="el-icon-arrow-up"></i>
              </el-button>
              <el-button type="text" size="mini" @click="moveDown(scope.$index)" :disabled="scope.$index === problemList.length - 1">
                <i class="el-icon-arrow-down"></i>
              </el-button>
              <el-button type="text" size="mini" style="color: #F56C6C;" @click="removeProblem(scope.$index)">
                <i class="el-icon-delete"></i>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 添加题目对话框 -->
    <el-dialog title="添加题目" :visible.sync="problemDialogVisible" width="70%">
      <el-form :inline="true" :model="problemSearchForm" class="search-form" style="margin-bottom: 15px;">
        <el-form-item label="题目ID">
          <el-input v-model="problemSearchForm.problemId" placeholder="请输入题目ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="题目标题">
          <el-input v-model="problemSearchForm.title" placeholder="请输入题目标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="problemSearchForm.difficulty" placeholder="请选择难度" clearable>
            <el-option label="简单" value="easy"></el-option>
            <el-option label="中等" value="medium"></el-option>
            <el-option label="困难" value="hard"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchProblems">查询</el-button>
          <el-button @click="resetProblemSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table
        :data="allProblemList"
        border
        style="width: 100%"
        v-loading="searchLoading"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="problemId" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="题目名称"></el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.difficulty === 'easy'" type="success">简单</el-tag>
            <el-tag v-else-if="scope.row.difficulty === 'medium'" type="warning">中等</el-tag>
            <el-tag v-else type="danger">困难</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitCount" label="提交数" width="100" align="center"></el-table-column>
        <el-table-column prop="acceptCount" label="通过数" width="100" align="center"></el-table-column>
      </el-table>
      
      <div class="pagination-container" style="margin-top: 15px;">
        <el-pagination
          @size-change="handleProblemSizeChange"
          @current-change="handleProblemCurrentChange"
          :current-page="problemPagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="problemPagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="problemPagination.total">
        </el-pagination>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="problemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addSelectedProblems">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminTrainingCreate",
  props: {
    id: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      isEdit: false,
      trainingForm: {
        id: null,
        title: '',
        description: '',
        status: 'public',
        startTime: '',
        endTime: '',
        difficulty: 3,
        password: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入训练名称', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入训练描述', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ]
      },
      problemList: [],
      problemListLoading: false,
      
      // 添加题目对话框
      problemDialogVisible: false,
      allProblemList: [],
      searchLoading: false,
      selectedProblems: [],
      problemSearchForm: {
        problemId: '',
        title: '',
        difficulty: ''
      },
      problemPagination: {
        current: 1,
        size: 10,
        total: 0
      }
    };
  },
  created() {
    if (this.id) {
      this.isEdit = true;
      this.fetchTrainingInfo();
      this.fetchProblemList();
    }
  },
  methods: {
    // 获取训练信息
    fetchTrainingInfo() {
      api.getTrainingInfo({ id: this.id }).then(response => {
        if (response.data.code === "200") {
          this.trainingForm = response.data.data;
        } else {
          this.$message.error('获取训练信息失败');
        }
      });
    },
    
    // 获取题目列表
    fetchProblemList() {
      this.problemListLoading = true;
      api.getProblemListByTrainingId({ trainingId: this.id }).then(response => {
        if (response.data.code === "200") {
          this.problemList = response.data.data;
        } else {
          this.$message.error('获取题目列表失败');
        }
        this.problemListLoading = false;
      }).catch(() => {
        this.problemListLoading = false;
        this.$message.error('获取题目列表失败');
      });
    },
    
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 更新训练
            api.updateTraining(this.trainingForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('更新成功');
                this.goBack();
              } else {
                this.$message.error(response.data.message || '更新失败');
              }
            });
          } else {
            // 创建训练
            api.addTraining(this.trainingForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('创建成功');
                this.goBack();
              } else {
                this.$message.error(response.data.message || '创建失败');
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    
    // 返回列表
    goBack() {
      this.$router.push('/admin/training-list');
    },
    
    // 添加题目
    handleAddProblem() {
      this.problemDialogVisible = true;
      this.searchProblems();
    },
    
    // 搜索题目
    searchProblems() {
      this.searchLoading = true;
      const params = {
        ...this.problemSearchForm,
        current: this.problemPagination.current,
        size: this.problemPagination.size
      };
      
      api.getProblemList(params).then(response => {
        if (response.data.code === "200") {
          this.allProblemList = response.data.data.records;
          this.problemPagination.total = response.data.data.total;
        } else {
          this.$message.error('获取题目列表失败');
        }
        this.searchLoading = false;
      }).catch(() => {
        this.searchLoading = false;
        this.$message.error('获取题目列表失败');
      });
    },
    
    // 重置题目搜索
    resetProblemSearch() {
      this.problemSearchForm = {
        problemId: '',
        title: '',
        difficulty: ''
      };
      this.problemPagination.current = 1;
      this.searchProblems();
    },
    
    // 处理题目分页大小变更
    handleProblemSizeChange(val) {
      this.problemPagination.size = val;
      this.searchProblems();
    },
    
    // 处理题目当前页变更
    handleProblemCurrentChange(val) {
      this.problemPagination.current = val;
      this.searchProblems();
    },
    
    // 处理题目选择变更
    handleSelectionChange(selection) {
      this.selectedProblems = selection;
    },
    
    // 添加选中题目
    addSelectedProblems() {
      if (this.selectedProblems.length === 0) {
        this.$message.warning('请选择要添加的题目');
        return;
      }
      
      // 查找已添加的题目ID
      const existingIds = this.problemList.map(item => item.problemId);
      // 过滤掉已添加的题目
      const newProblems = this.selectedProblems.filter(item => !existingIds.includes(item.problemId));
      
      if (newProblems.length === 0) {
        this.$message.warning('所选题目已全部添加');
        this.problemDialogVisible = false;
        return;
      }
      
      // 添加到题目列表
      this.problemList = [...this.problemList, ...newProblems];
      
      // 更新训练题目
      this.updateTrainingProblems();
      
      this.problemDialogVisible = false;
      this.$message.success(`已添加 ${newProblems.length} 道题目`);
    },
    
    // 更新训练题目
    updateTrainingProblems() {
      if (!this.isEdit) return;
      
      const problemIds = this.problemList.map(item => item.problemId);
      api.updateTrainingProblemList({
        trainingId: this.id,
        problemIds: problemIds
      }).then(response => {
        if (response.data.code !== "200") {
          this.$message.error(response.data.message || '更新题目失败');
        }
      });
    },
    
    // 移除题目
    removeProblem(index) {
      this.$confirm('确定要移除该题目吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.problemList.splice(index, 1);
        this.updateTrainingProblems();
      }).catch(() => {});
    },
    
    // 上移题目
    moveUp(index) {
      if (index > 0) {
        const temp = this.problemList[index];
        this.$set(this.problemList, index, this.problemList[index - 1]);
        this.$set(this.problemList, index - 1, temp);
        this.updateTrainingProblems();
      }
    },
    
    // 下移题目
    moveDown(index) {
      if (index < this.problemList.length - 1) {
        const temp = this.problemList[index];
        this.$set(this.problemList, index, this.problemList[index + 1]);
        this.$set(this.problemList, index + 1, temp);
        this.updateTrainingProblems();
      }
    }
  }
};
</script>

<style scoped>
.training-create-container {
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

.problem-card {
  margin-top: 20px;
}

.problem-title-link {
  color: #409EFF;
  text-decoration: none;
}

.problem-title-link:hover {
  text-decoration: underline;
}
</style> 