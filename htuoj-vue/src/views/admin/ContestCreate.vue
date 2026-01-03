<template>
  <div class="contest-create-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑竞赛' : '创建竞赛' }}</h2>
    </div>
    
    <el-card shadow="never">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="竞赛名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入竞赛名称"></el-input>
        </el-form-item>
        
        <el-form-item label="竞赛时间" prop="time">
          <el-date-picker
            v-model="form.time"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="竞赛说明" prop="description">
          <markdown-editor v-model="form.description" height="300px"></markdown-editor>
        </el-form-item>
        
        <el-form-item label="竞赛规则" prop="rules">
          <markdown-editor v-model="form.rules" height="300px"></markdown-editor>
        </el-form-item>
        
        <el-form-item label="参与方式">
          <el-radio-group v-model="form.participationType">
            <el-radio label="public">公开参与</el-radio>
            <el-radio label="private">私有竞赛</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="参与密码" v-if="form.participationType === 'private'" prop="password">
          <el-input v-model="form.password" placeholder="请输入参与密码"></el-input>
        </el-form-item>
        
        <el-form-item label="题目显示">
          <el-radio-group v-model="form.problemDisplayType">
            <el-radio label="all">全部显示</el-radio>
            <el-radio label="progress">进度显示</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import api from "@/common/api";
import MarkdownEditor from "@/components/MarkdownEditor.vue";

export default {
  name: "AdminContestCreate",
  components: {
    MarkdownEditor
  },
  data() {
    return {
      isEdit: false,
      contestId: null,
      form: {
        title: '',
        time: [],
        description: '',
        rules: '',
        participationType: 'public',
        password: '',
        problemDisplayType: 'all'
      },
      rules: {
        title: [
          { required: true, message: '请输入竞赛名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        time: [
          { required: true, message: '请选择竞赛时间', trigger: 'change' }
        ],
        description: [
          { required: true, message: '请输入竞赛说明', trigger: 'blur' }
        ],
        rules: [
          { required: true, message: '请输入竞赛规则', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入参与密码', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    const contestId = this.$route.params.id;
    if (contestId) {
      this.isEdit = true;
      this.contestId = contestId;
      this.fetchContestDetail();
    }
  },
  methods: {
    // 获取竞赛详情
    fetchContestDetail() {
      api.getContestDetail({ contestId: this.contestId }).then(response => {
        if (response.data.code === "200") {
          const contest = response.data.data;
          this.form = {
            title: contest.title,
            time: [contest.startTime, contest.endTime],
            description: contest.description,
            rules: contest.rules,
            participationType: contest.participationType,
            password: contest.password,
            problemDisplayType: contest.problemDisplayType
          };
        } else {
          this.$message.error('获取竞赛详情失败');
          this.$router.push('/admin/contest-list');
        }
      });
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const [startTime, endTime] = this.form.time;
          const data = {
            title: this.form.title,
            startTime,
            endTime,
            description: this.form.description,
            rules: this.form.rules,
            participationType: this.form.participationType,
            password: this.form.password,
            problemDisplayType: this.form.problemDisplayType
          };
          
          if (this.isEdit) {
            data.contestId = this.contestId;
            api.updateContest(data).then(response => {
              if (response.data.code === "200") {
                this.$message.success('更新成功');
                this.$router.push('/admin/contest-list');
              } else {
                this.$message.error(response.data.message || '更新失败');
              }
            });
          } else {
            api.createContest(data).then(response => {
              if (response.data.code === "200") {
                this.$message.success('创建成功');
                this.$router.push('/admin/contest-list');
              } else {
                this.$message.error(response.data.message || '创建失败');
              }
            });
          }
        }
      });
    },
    
    // 取消
    handleCancel() {
      this.$router.push('/admin/contest-list');
    }
  }
};
</script>

<style scoped>
.contest-create-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}
</style> 