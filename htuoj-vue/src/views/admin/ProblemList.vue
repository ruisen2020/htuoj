<template>
  <div class="problem-list-container">
    <div class="page-header">
      <h2>题目管理</h2>
      <el-button type="primary" size="small" @click="handleAddProblem">
        <i class="el-icon-plus"></i>
        添加题目
      </el-button>
    </div>
    
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="题目ID">
          <el-input v-model="searchForm.problemId" placeholder="请输入题目ID" clearable></el-input>
        </el-form-item>
        <el-form-item label="题目标题">
          <el-input v-model="searchForm.title" placeholder="请输入题目标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="searchForm.difficulty" placeholder="请选择难度" clearable>
            <el-option label="简单" value="easy"></el-option>
            <el-option label="中等" value="medium"></el-option>
            <el-option label="困难" value="hard"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="searchForm.labelId" placeholder="请选择标签" clearable>
            <el-option
              v-for="item in labelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
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
      <el-table :data="problemList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="problemId" label="题目ID" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="题目标题" width="250">
          <template slot-scope="scope">
            <router-link :to="'/problem/' + scope.row.problemId" class="problem-title-link">
              {{ scope.row.title }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="source" label="来源" width="120"></el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.difficulty === 'easy'" type="success">简单</el-tag>
            <el-tag v-else-if="scope.row.difficulty === 'medium'" type="warning">中等</el-tag>
            <el-tag v-else type="danger">困难</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="labels" label="标签" width="200">
          <template slot-scope="scope">
            <el-tag
              v-for="tag in scope.row.labels"
              :key="tag.id"
              size="mini"
              style="margin-right: 5px; margin-bottom: 5px;">
              {{ tag.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitCount" label="提交数" width="100" align="center"></el-table-column>
        <el-table-column prop="acceptCount" label="通过数" width="100" align="center"></el-table-column>
        <el-table-column prop="passRate" label="通过率" width="100" align="center">
          <template slot-scope="scope">
            {{ calculatePassRate(scope.row.acceptCount, scope.row.submitCount) }}
          </template>
        </el-table-column>
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
        <el-table-column label="操作" fixed="right" width="250" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleViewCases(scope.row)">测试用例</el-button>
            <el-button type="text" size="small" @click="handleSubmissionStats(scope.row)">提交统计</el-button>
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
    
    <!-- 查看测试用例对话框 -->
    <el-dialog title="测试用例" :visible.sync="casesVisible" width="700px">
      <el-table :data="testCases" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="input" label="输入">
          <template slot-scope="scope">
            <pre>{{ scope.row.input }}</pre>
          </template>
        </el-table-column>
        <el-table-column prop="output" label="输出">
          <template slot-scope="scope">
            <pre>{{ scope.row.output }}</pre>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEditCase(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDeleteCase(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 15px; text-align: center;">
        <el-button type="primary" @click="handleAddCase">添加测试用例</el-button>
      </div>
    </el-dialog>
    
    <!-- 提交统计对话框 -->
    <el-dialog title="提交统计" :visible.sync="statsVisible" width="600px">
      <div style="height: 300px;">
        <v-chart :options="submissionChartOptions" autoresize></v-chart>
      </div>
      <div class="stats-summary">
        <div class="stats-item">
          <div class="stats-label">总提交数</div>
          <div class="stats-value">{{ currentProblemStats.submitCount }}</div>
        </div>
        <div class="stats-item">
          <div class="stats-label">通过数</div>
          <div class="stats-value">{{ currentProblemStats.acceptCount }}</div>
        </div>
        <div class="stats-item">
          <div class="stats-label">通过率</div>
          <div class="stats-value">{{ calculatePassRate(currentProblemStats.acceptCount, currentProblemStats.submitCount) }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "ProblemList",
  data() {
    return {
      // 搜索表单
      searchForm: {
        problemId: '',
        title: '',
        difficulty: '',
        labelId: '',
        status: ''
      },
      
      // 分页信息
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 题目列表数据
      problemList: [],
      loading: false,
      
      // 标签选项
      labelOptions: [],
      
      // 测试用例对话框
      casesVisible: false,
      testCases: [],
      currentProblemId: null,
      
      // 提交统计对话框
      statsVisible: false,
      currentProblemStats: {
        submitCount: 0,
        acceptCount: 0
      },
      submissionChartOptions: {
        title: {
          text: '提交结果分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['通过', '编译错误', '答案错误', '超时', '内存溢出', '运行错误']
        },
        series: [
          {
            name: '提交结果',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: []
          }
        ]
      }
    };
  },
  created() {
    this.fetchProblemList();
    this.fetchLabelOptions();
  },
  methods: {
    // 获取题目列表
    fetchProblemList() {
      this.loading = true;
      const params = {
        ...this.searchForm,
        current: this.pagination.current,
        size: this.pagination.size
      };
      
      api.getProblemList(params).then(response => {
        if (response.data.code === "200") {
          this.problemList = response.data.data.records;
          this.pagination.total = response.data.data.total;
        } else {
          this.$message.error('获取题目列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取题目列表失败');
      });
    },
    
    // 获取标签选项
    fetchLabelOptions() {
      api.getLabelAll().then(response => {
        if (response.data.code === "200") {
          this.labelOptions = response.data.data.map(item => {
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
      this.fetchProblemList();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        problemId: '',
        title: '',
        difficulty: '',
        labelId: '',
        status: ''
      };
      this.handleSearch();
    },
    
    // 处理分页大小变更
    handleSizeChange(val) {
      this.pagination.size = val;
      this.fetchProblemList();
    },
    
    // 处理当前页变更
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.fetchProblemList();
    },
    
    // 计算通过率
    calculatePassRate(acceptCount, submitCount) {
      if (!submitCount || submitCount === 0) {
        return '0%';
      }
      const rate = (acceptCount / submitCount * 100).toFixed(2);
      return `${rate}%`;
    },
    
    // 添加题目
    handleAddProblem() {
      this.$router.push('/admin/problem-edit');
    },
    
    // 编辑题目
    handleEdit(row) {
      this.$router.push(`/admin/problem-edit/${row.problemId}`);
    },
    
    // 处理删除题目
    handleDelete(row) {
      this.$confirm('确定要删除该题目吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除API
        this.deleteProblem(row.problemId);
      }).catch(() => {});
    },
    
    // 处理状态变更
    handleStatusChange(row) {
      const statusText = row.status === 'public' ? '公开' : '私有';
      this.$message({
        message: `已将题目【${row.title}】设置为${statusText}`,
        type: 'success'
      });
      
      // 这里应该调用更新题目状态的API
      // api.updateProblemStatus({problemId: row.problemId, status: row.status})
    },
    
    // 查看测试用例
    handleViewCases(row) {
      this.currentProblemId = row.problemId;
      this.casesVisible = true;
      this.fetchTestCases(row.problemId);
    },
    
    // 获取测试用例
    fetchTestCases(problemId) {
      api.getSampleByProbelmId(problemId).then(response => {
        if (response.data.code === "200") {
          this.testCases = response.data.data;
        } else {
          this.$message.error('获取测试用例失败');
        }
      });
    },
    
    // 添加测试用例
    handleAddCase() {
      // 实现添加测试用例的逻辑
      this.$message.info('添加测试用例功能待实现');
    },
    
    // 编辑测试用例
    handleEditCase(row) {
      // 实现编辑测试用例的逻辑
      this.$message.info(`编辑测试用例ID: ${row.id}`);
    },
    
    // 删除测试用例
    handleDeleteCase(row) {
      this.$confirm('确定要删除该测试用例吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除测试用例的API
        this.$message({
          type: 'success',
          message: `删除测试用例ID: ${row.id}成功!`
        });
        this.fetchTestCases(this.currentProblemId);
      }).catch(() => {});
    },
    
    // 查看提交统计
    handleSubmissionStats(row) {
      this.currentProblemStats = {
        submitCount: row.submitCount,
        acceptCount: row.acceptCount
      };
      this.statsVisible = true;
      this.fetchSubmissionStats(row.problemId);
    },
    
    // 获取提交统计数据
    fetchSubmissionStats(problemId) {
      // 模拟数据，实际应该从API获取
      this.$message.info(`获取题目ID: ${problemId}的提交统计`);
      this.submissionChartOptions.series[0].data = [
        { value: 458, name: '通过' },
        { value: 182, name: '编译错误' },
        { value: 234, name: '答案错误' },
        { value: 135, name: '超时' },
        { value: 87, name: '内存溢出' },
        { value: 124, name: '运行错误' }
      ];
    }
  }
};
</script>

<style scoped>
.problem-list-container {
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

.problem-title-link {
  color: #409EFF;
  text-decoration: none;
}

.problem-title-link:hover {
  text-decoration: underline;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  font-family: Consolas, Monaco, monospace;
  font-size: 12px;
}

.stats-summary {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
  text-align: center;
}

.stats-item {
  padding: 10px;
}

.stats-label {
  font-size: 14px;
  color: #909399;
}

.stats-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-top: 5px;
}
</style> 