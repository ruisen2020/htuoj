<template>
  <div class="problem-edit-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑题目' : '添加题目' }}</h2>
    </div>
    
    <el-card shadow="never">
      <el-form ref="problemForm" :model="problemForm" :rules="problemRules" label-width="120px">
        <el-form-item label="题目标题" prop="title">
          <el-input v-model="problemForm.title" placeholder="请输入题目标题"></el-input>
        </el-form-item>
        
        <el-form-item label="题目难度" prop="difficulty">
          <el-select v-model="problemForm.difficulty" placeholder="请选择难度">
            <el-option label="简单" value="easy"></el-option>
            <el-option label="中等" value="medium"></el-option>
            <el-option label="困难" value="hard"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="时间限制(ms)" prop="timeLimit">
          <el-input-number v-model="problemForm.timeLimit" :min="100" :max="15000" :step="100"></el-input-number>
        </el-form-item>
        
        <el-form-item label="内存限制(MB)" prop="memoryLimit">
          <el-input-number v-model="problemForm.memoryLimit" :min="4" :max="512" :step="4"></el-input-number>
        </el-form-item>
        
        <el-form-item label="题目分类" prop="categoryId">
          <el-select v-model="problemForm.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="标签">
          <el-select
            v-model="problemForm.tags"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请选择标签">
            <el-option
              v-for="item in tagOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="题目描述" prop="description">
          <div class="editor-container">
            <markdown-editor v-model="problemForm.description" height="300px"></markdown-editor>
          </div>
        </el-form-item>
        
        <el-form-item label="输入描述" prop="inputDescription">
          <el-input type="textarea" v-model="problemForm.inputDescription" rows="3" placeholder="请输入输入描述"></el-input>
        </el-form-item>
        
        <el-form-item label="输出描述" prop="outputDescription">
          <el-input type="textarea" v-model="problemForm.outputDescription" rows="3" placeholder="请输入输出描述"></el-input>
        </el-form-item>
        
        <el-form-item label="样例">
          <div v-for="(sample, index) in problemForm.samples" :key="index" class="sample-item">
            <div class="sample-header">
              <span>样例 #{{ index + 1 }}</span>
              <el-button type="text" @click="removeSample(index)" v-if="problemForm.samples.length > 1">删除</el-button>
            </div>
            <el-form-item :label="'输入'" :prop="'samples.' + index + '.input'">
              <el-input type="textarea" v-model="sample.input" rows="3" placeholder="请输入样例输入"></el-input>
            </el-form-item>
            <el-form-item :label="'输出'" :prop="'samples.' + index + '.output'">
              <el-input type="textarea" v-model="sample.output" rows="3" placeholder="请输入样例输出"></el-input>
            </el-form-item>
          </div>
          <el-button type="primary" plain @click="addSample">添加样例</el-button>
        </el-form-item>
        
        <el-form-item label="提示">
          <div class="editor-container">
            <markdown-editor v-model="problemForm.hint" height="200px"></markdown-editor>
          </div>
        </el-form-item>
        
        <el-form-item label="状态">
          <el-radio-group v-model="problemForm.status">
            <el-radio label="public">公开</el-radio>
            <el-radio label="private">私有</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import api from "@/common/api";
import MarkdownEditor from "@/components/MarkdownEditor.vue";

export default {
  name: "AdminProblemEdit",
  components: {
    MarkdownEditor
  },
  props: {
    id: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      isEdit: false,
      problemForm: {
        id: null,
        title: "",
        difficulty: "medium",
        timeLimit: 1000,
        memoryLimit: 128,
        categoryId: null,
        tags: [],
        description: "",
        inputDescription: "",
        outputDescription: "",
        samples: [
          {
            input: "",
            output: ""
          }
        ],
        hint: "",
        status: "public"
      },
      problemRules: {
        title: [
          { required: true, message: "请输入题目标题", trigger: "blur" },
          { min: 3, max: 100, message: "长度在 3 到 100 个字符", trigger: "blur" }
        ],
        difficulty: [
          { required: true, message: "请选择难度", trigger: "change" }
        ],
        timeLimit: [
          { required: true, message: "请输入时间限制", trigger: "blur" }
        ],
        memoryLimit: [
          { required: true, message: "请输入内存限制", trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: "请选择分类", trigger: "change" }
        ],
        description: [
          { required: true, message: "请输入题目描述", trigger: "blur" }
        ]
      },
      categoryOptions: [],
      tagOptions: []
    };
  },
  created() {
    this.fetchCategories();
    this.fetchTags();
    
    if (this.id) {
      this.isEdit = true;
      this.fetchProblemDetail();
    }
  },
  methods: {
    fetchCategories() {
      api.getProblemCategories().then(response => {
        if (response.data.code === "200") {
          this.categoryOptions = response.data.data;
        }
      });
    },
    
    fetchTags() {
      api.getProblemTags().then(response => {
        if (response.data.code === "200") {
          this.tagOptions = response.data.data;
        }
      });
    },
    
    fetchProblemDetail() {
      api.getProblemDetail({ problemId: this.id }).then(response => {
        if (response.data.code === "200") {
          const data = response.data.data;
          this.problemForm = {
            id: data.id,
            title: data.title,
            difficulty: data.difficulty,
            timeLimit: data.timeLimit,
            memoryLimit: data.memoryLimit,
            categoryId: data.categoryId,
            tags: data.tags.map(tag => tag.id),
            description: data.description,
            inputDescription: data.inputDescription,
            outputDescription: data.outputDescription,
            samples: data.samples,
            hint: data.hint,
            status: data.status
          };
        }
      });
    },
    
    addSample() {
      this.problemForm.samples.push({
        input: "",
        output: ""
      });
    },
    
    removeSample(index) {
      this.problemForm.samples.splice(index, 1);
    },
    
    submitForm() {
      this.$refs.problemForm.validate((valid) => {
        if (valid) {
          const apiMethod = this.isEdit ? api.updateProblem : api.addProblem;
          
          apiMethod(this.problemForm).then(response => {
            if (response.data.code === "200") {
              this.$message.success(this.isEdit ? "更新成功" : "添加成功");
              this.$router.push("/admin/problem-list");
            } else {
              this.$message.error(response.data.message || (this.isEdit ? "更新失败" : "添加失败"));
            }
          });
        }
      });
    },
    
    cancel() {
      this.$router.push("/admin/problem-list");
    }
  }
};
</script>

<style scoped>
.problem-edit-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.sample-item {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
}

.sample-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}
</style> 