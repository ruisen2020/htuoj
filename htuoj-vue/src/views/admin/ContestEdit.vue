<template>
  <div class="contest-edit-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑竞赛' : '创建竞赛' }}</h2>
    </div>
    
    <el-card shadow="never">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="竞赛标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入竞赛标题"></el-input>
        </el-form-item>
        
        <el-form-item label="竞赛描述" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="4" placeholder="请输入竞赛描述"></el-input>
        </el-form-item>
        
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="报名开始时间" prop="registerStartTime">
          <el-date-picker
            v-model="form.registerStartTime"
            type="datetime"
            placeholder="选择报名开始时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="报名结束时间" prop="registerEndTime">
          <el-date-picker
            v-model="form.registerEndTime"
            type="datetime"
            placeholder="选择报名结束时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="竞赛类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择竞赛类型">
            <el-option label="个人赛" value="INDIVIDUAL"></el-option>
            <el-option label="团队赛" value="TEAM"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="参赛人数限制" prop="maxParticipants">
          <el-input-number v-model="form.maxParticipants" :min="1" :max="1000"></el-input-number>
        </el-form-item>
        
        <el-form-item label="是否公开" prop="isPublic">
          <el-switch v-model="form.isPublic"></el-switch>
        </el-form-item>
        
        <el-form-item label="是否允许查看排名" prop="allowRanking">
          <el-switch v-model="form.allowRanking"></el-switch>
        </el-form-item>
        
        <el-form-item label="是否允许查看提交" prop="allowSubmission">
          <el-switch v-model="form.allowSubmission"></el-switch>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">保存</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminContestEdit",
  data() {
    return {
      isEdit: false,
      loading: false,
      form: {
        title: "",
        description: "",
        startTime: "",
        endTime: "",
        registerStartTime: "",
        registerEndTime: "",
        type: "INDIVIDUAL",
        maxParticipants: 100,
        isPublic: true,
        allowRanking: true,
        allowSubmission: true
      },
      rules: {
        title: [
          { required: true, message: "请输入竞赛标题", trigger: "blur" },
          { min: 2, max: 100, message: "长度在 2 到 100 个字符", trigger: "blur" }
        ],
        description: [
          { required: true, message: "请输入竞赛描述", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "请选择开始时间", trigger: "change" }
        ],
        endTime: [
          { required: true, message: "请选择结束时间", trigger: "change" }
        ],
        registerStartTime: [
          { required: true, message: "请选择报名开始时间", trigger: "change" }
        ],
        registerEndTime: [
          { required: true, message: "请选择报名结束时间", trigger: "change" }
        ],
        type: [
          { required: true, message: "请选择竞赛类型", trigger: "change" }
        ],
        maxParticipants: [
          { required: true, message: "请输入参赛人数限制", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    const contestId = this.$route.params.id;
    if (contestId) {
      this.isEdit = true;
      this.fetchContestDetail(contestId);
    }
  },
  methods: {
    // 获取竞赛详情
    fetchContestDetail(contestId) {
      this.loading = true;
      api.getContestDetail(contestId).then(response => {
        if (response.data.code === "200") {
          this.form = response.data.data;
        } else {
          this.$message.error("获取竞赛详情失败");
        }
        this.loading = false;
      });
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          const apiMethod = this.isEdit ? api.updateContest : api.createContest;
          const params = this.isEdit ? { ...this.form, contestId: this.$route.params.id } : this.form;
          
          apiMethod(params).then(response => {
            if (response.data.code === "200") {
              this.$message.success(this.isEdit ? "更新成功" : "创建成功");
              this.$router.push("/admin/contest/list");
            } else {
              this.$message.error(response.data.message || (this.isEdit ? "更新失败" : "创建失败"));
            }
            this.loading = false;
          });
        }
      });
    },
    
    // 取消
    handleCancel() {
      this.$router.push("/admin/contest/list");
    }
  }
};
</script>

<style scoped>
.contest-edit-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.el-date-picker {
  width: 100%;
}
</style> 