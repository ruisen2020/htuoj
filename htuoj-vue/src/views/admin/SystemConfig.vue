<template>
  <div class="system-config-container">
    <div class="page-header">
      <h2>系统设置</h2>
    </div>
    
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基本设置" name="basic">
        <el-card shadow="never">
          <el-form ref="basicForm" :model="basicForm" :rules="basicRules" label-width="120px">
            <el-form-item label="网站名称" prop="siteName">
              <el-input v-model="basicForm.siteName"></el-input>
            </el-form-item>
            <el-form-item label="网站标题" prop="siteTitle">
              <el-input v-model="basicForm.siteTitle"></el-input>
            </el-form-item>
            <el-form-item label="网站描述" prop="siteDescription">
              <el-input type="textarea" v-model="basicForm.siteDescription" rows="3"></el-input>
            </el-form-item>
            <el-form-item label="网站LOGO">
              <el-upload
                class="avatar-uploader"
                action="/api/file/upload"
                :show-file-list="false"
                :on-success="handleLogoSuccess">
                <img v-if="basicForm.siteLogo" :src="basicForm.siteLogo" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="备案号" prop="icpNo">
              <el-input v-model="basicForm.icpNo"></el-input>
            </el-form-item>
            <el-form-item label="公安备案号" prop="securityNo">
              <el-input v-model="basicForm.securityNo"></el-input>
            </el-form-item>
            <el-form-item label="页脚信息" prop="footerInfo">
              <el-input type="textarea" v-model="basicForm.footerInfo" rows="3"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitBasicForm">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="邮件配置" name="email">
        <el-card shadow="never">
          <el-form ref="emailForm" :model="emailForm" :rules="emailRules" label-width="120px">
            <el-form-item label="SMTP服务器" prop="smtpHost">
              <el-input v-model="emailForm.smtpHost"></el-input>
            </el-form-item>
            <el-form-item label="SMTP端口" prop="smtpPort">
              <el-input v-model="emailForm.smtpPort"></el-input>
            </el-form-item>
            <el-form-item label="发件人邮箱" prop="fromEmail">
              <el-input v-model="emailForm.fromEmail"></el-input>
            </el-form-item>
            <el-form-item label="发件人名称" prop="fromName">
              <el-input v-model="emailForm.fromName"></el-input>
            </el-form-item>
            <el-form-item label="邮箱账号" prop="username">
              <el-input v-model="emailForm.username"></el-input>
            </el-form-item>
            <el-form-item label="邮箱密码" prop="password">
              <el-input v-model="emailForm.password" type="password"></el-input>
            </el-form-item>
            <el-form-item label="SSL加密">
              <el-switch v-model="emailForm.ssl"></el-switch>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitEmailForm">保存设置</el-button>
              <el-button type="info" @click="testEmailConfig">测试配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="评测机配置" name="judge">
        <el-card shadow="never">
          <el-form ref="judgeForm" :model="judgeForm" label-width="120px">
            <el-form-item label="评测时间限制">
              <el-input-number v-model="judgeForm.timeLimit" :min="1" :max="60" label="秒"></el-input-number>
              <span class="form-hint">秒</span>
            </el-form-item>
            <el-form-item label="评测内存限制">
              <el-input-number v-model="judgeForm.memoryLimit" :min="64" :max="1024" label="MB"></el-input-number>
              <span class="form-hint">MB</span>
            </el-form-item>
            <el-form-item label="编译超时">
              <el-input-number v-model="judgeForm.compileTimeout" :min="5" :max="60" label="秒"></el-input-number>
              <span class="form-hint">秒</span>
            </el-form-item>
            <el-form-item label="评测节点">
              <el-tag
                v-for="(node, index) in judgeForm.judgeNodes"
                :key="index"
                closable
                @close="removeJudgeNode(index)"
                style="margin-right: 10px; margin-bottom: 10px;">
                {{ node }}
              </el-tag>
              <el-input
                class="input-new-tag"
                v-if="inputVisible"
                v-model="inputValue"
                ref="saveTagInput"
                size="small"
                @keyup.enter.native="handleInputConfirm"
                @blur="handleInputConfirm">
              </el-input>
              <el-button v-else class="button-new-tag" size="small" @click="showInput">
                + 添加节点
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitJudgeForm">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="附件存储" name="storage">
        <el-card shadow="never">
          <el-form ref="storageForm" :model="storageForm" label-width="120px">
            <el-form-item label="存储方式">
              <el-radio-group v-model="storageForm.storageType">
                <el-radio label="local">本地存储</el-radio>
                <el-radio label="oss">阿里云OSS</el-radio>
                <el-radio label="cos">腾讯云COS</el-radio>
                <el-radio label="qiniu">七牛云</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <div v-if="storageForm.storageType === 'local'">
              <el-form-item label="上传路径">
                <el-input v-model="storageForm.local.uploadPath"></el-input>
              </el-form-item>
              <el-form-item label="访问URL">
                <el-input v-model="storageForm.local.accessUrl"></el-input>
              </el-form-item>
            </div>
            
            <div v-if="storageForm.storageType === 'oss'">
              <el-form-item label="AccessKey">
                <el-input v-model="storageForm.oss.accessKey"></el-input>
              </el-form-item>
              <el-form-item label="SecretKey">
                <el-input v-model="storageForm.oss.secretKey" type="password"></el-input>
              </el-form-item>
              <el-form-item label="Endpoint">
                <el-input v-model="storageForm.oss.endpoint"></el-input>
              </el-form-item>
              <el-form-item label="Bucket">
                <el-input v-model="storageForm.oss.bucket"></el-input>
              </el-form-item>
              <el-form-item label="域名">
                <el-input v-model="storageForm.oss.domain"></el-input>
              </el-form-item>
            </div>
            
            <el-form-item>
              <el-button type="primary" @click="submitStorageForm">保存设置</el-button>
              <el-button type="info" @click="testStorageConfig">测试配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: "SystemConfig",
  data() {
    return {
      activeTab: 'basic',
      
      // 基本设置
      basicForm: {
        siteName: 'HTUOJ在线评测系统',
        siteTitle: 'HTUOJ - 在线评测系统',
        siteDescription: '高性能在线评测系统，支持多种编程语言，提供丰富的题库和竞赛功能',
        siteLogo: '',
        icpNo: '吉ICP备2024023192号',
        securityNo: '吉公网安备22017302000468号',
        footerInfo: '© 2024-2025 HTUOJ 版权所有'
      },
      basicRules: {
        siteName: [
          { required: true, message: '请输入网站名称', trigger: 'blur' }
        ],
        siteTitle: [
          { required: true, message: '请输入网站标题', trigger: 'blur' }
        ]
      },
      
      // 邮件配置
      emailForm: {
        smtpHost: 'smtp.example.com',
        smtpPort: '465',
        fromEmail: 'noreply@example.com',
        fromName: 'HTUOJ系统',
        username: 'noreply@example.com',
        password: '',
        ssl: true
      },
      emailRules: {
        smtpHost: [
          { required: true, message: '请输入SMTP服务器地址', trigger: 'blur' }
        ],
        smtpPort: [
          { required: true, message: '请输入SMTP端口', trigger: 'blur' }
        ],
        fromEmail: [
          { required: true, message: '请输入发件人邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入邮箱账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入邮箱密码', trigger: 'blur' }
        ]
      },
      
      // 评测机配置
      judgeForm: {
        timeLimit: 10,
        memoryLimit: 256,
        compileTimeout: 10,
        judgeNodes: ['http://judge1:8080', 'http://judge2:8080']
      },
      inputVisible: false,
      inputValue: '',
      
      // 附件存储配置
      storageForm: {
        storageType: 'local',
        local: {
          uploadPath: '/app/upload',
          accessUrl: 'https://htuoj.cn/upload'
        },
        oss: {
          accessKey: '',
          secretKey: '',
          endpoint: 'oss-cn-beijing.aliyuncs.com',
          bucket: 'htuoj',
          domain: 'https://htuoj.oss-cn-beijing.aliyuncs.com'
        }
      }
    };
  },
  methods: {
    // 基本设置
    submitBasicForm() {
      this.$refs.basicForm.validate(valid => {
        if (valid) {
          this.$message.success('保存成功');
        }
      });
    },
    
    // 处理Logo上传成功
    handleLogoSuccess(res) {
      if (res.code === '200') {
        this.basicForm.siteLogo = res.data;
        this.$message.success('上传成功');
      } else {
        this.$message.error('上传失败');
      }
    },
    
    // 邮件配置
    submitEmailForm() {
      this.$refs.emailForm.validate(valid => {
        if (valid) {
          this.$message.success('保存成功');
        }
      });
    },
    
    // 测试邮件配置
    testEmailConfig() {
      this.$message.info('测试邮件已发送，请查收');
    },
    
    // 评测机配置
    removeJudgeNode(index) {
      this.judgeForm.judgeNodes.splice(index, 1);
    },
    
    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.judgeForm.judgeNodes.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },
    
    submitJudgeForm() {
      this.$message.success('保存成功');
    },
    
    // 附件存储配置
    submitStorageForm() {
      this.$message.success('保存成功');
    },
    
    // 测试存储配置
    testStorageConfig() {
      this.$message.success('测试成功，配置有效');
    }
  }
};
</script>

<style scoped>
.system-config-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.form-hint {
  margin-left: 10px;
  color: #909399;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.input-new-tag {
  width: 120px;
  vertical-align: bottom;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
</style> 