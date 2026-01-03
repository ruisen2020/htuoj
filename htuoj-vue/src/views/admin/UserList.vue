<template>
  <div class="user-list-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" size="small" @click="handleAddUser">
        <i class="el-icon-plus"></i>
        添加用户
      </el-button>
    </div>
    
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="searchForm.number" placeholder="请输入学号" clearable></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择角色" clearable>
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="教师" value="teacher"></el-option>
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="normal"></el-option>
            <el-option label="禁用" value="disabled"></el-option>
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
      <el-table :data="userList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="avatar" label="头像" width="100" align="center">
          <template slot-scope="scope">
            <el-avatar :src="scope.row.avatar" :size="40"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="120"></el-table-column>
        <el-table-column prop="number" label="学号" width="120"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="schoolName" label="学校"></el-table-column>
        <el-table-column prop="roleName" label="角色" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.roleName === 'admin'" type="danger">管理员</el-tag>
            <el-tag v-else-if="scope.row.roleName === 'teacher'" type="warning">教师</el-tag>
            <el-tag v-else type="success">学生</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="'normal'"
              :inactive-value="'disabled'"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180"></el-table-column>
        <el-table-column label="操作" fixed="right" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleResetPassword(scope.row)">重置密码</el-button>
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
    
    <!-- 用户表单对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form :model="userForm" :rules="userRules" ref="userForm" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userForm.userName" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="学号" prop="number">
          <el-input v-model="userForm.number" placeholder="请输入学号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="学校" prop="schoolId">
          <el-select v-model="userForm.schoolId" filterable placeholder="请选择学校" style="width: 100%;">
            <el-option
              v-for="item in schoolOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="userForm.roleId" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" value="1"></el-option>
            <el-option label="教师" value="2"></el-option>
            <el-option label="学生" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="isAdd" label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="normal">正常</el-radio>
            <el-radio label="disabled">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
    
    <!-- 重置密码对话框 -->
    <el-dialog title="重置密码" :visible.sync="resetPwdVisible" width="400px">
      <el-form :model="resetPwdForm" :rules="resetPwdRules" ref="resetPwdForm" label-width="80px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="resetPwdForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPwdForm.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPwdVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitResetPwd">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "UserList",
  data() {
    // 自定义校验规则：确认密码与新密码一致
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.resetPwdForm.newPassword) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    
    return {
      // 搜索表单
      searchForm: {
        userName: '',
        number: '',
        role: '',
        status: ''
      },
      
      // 分页信息
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      
      // 用户列表数据
      userList: [],
      loading: false,
      
      // 对话框控制
      dialogVisible: false,
      dialogTitle: '添加用户',
      isAdd: true,
      
      // 用户表单
      userForm: {
        id: null,
        userName: '',
        number: '',
        email: '',
        password: '',
        schoolId: '',
        roleId: '3',
        status: 'normal'
      },
      
      // 表单校验规则
      userRules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        number: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
        ],
        schoolId: [
          { required: true, message: '请选择学校', trigger: 'change' }
        ],
        roleId: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      },
      
      // 学校选项
      schoolOptions: [],
      
      // 重置密码相关
      resetPwdVisible: false,
      currentUserId: null,
      resetPwdForm: {
        newPassword: '',
        confirmPassword: ''
      },
      resetPwdRules: {
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    // 添加示例数据
    this.userList = [
      {
        id: 1,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'admin',
        number: 'admin001',
        email: 'admin@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'admin',
        status: 'normal',
        createTime: '2023-01-01 08:00:00'
      },
      {
        id: 2,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'teacher1',
        number: 'T2023001',
        email: 'teacher1@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'teacher',
        status: 'normal',
        createTime: '2023-02-15 10:30:00'
      },
      {
        id: 3,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'student1',
        number: '2023001',
        email: 'student1@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'student',
        status: 'normal',
        createTime: '2023-03-10 14:20:00'
      },
      {
        id: 4,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'student2',
        number: '2023002',
        email: 'student2@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'student',
        status: 'disabled',
        createTime: '2023-03-12 09:45:00'
      },
      {
        id: 5,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'teacher2',
        number: 'T2023002',
        email: 'teacher2@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'teacher',
        status: 'normal',
        createTime: '2023-04-05 11:15:00'
      },
      {
        id: 6,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'student3',
        number: '2023003',
        email: 'student3@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'student',
        status: 'normal',
        createTime: '2023-04-20 16:30:00'
      },
      {
        id: 7,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'student4',
        number: '2023004',
        email: 'student4@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'student',
        status: 'normal',
        createTime: '2023-05-10 13:40:00'
      },
      {
        id: 8,
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        userName: 'student5',
        number: '2023005',
        email: 'student5@htuoj.com',
        schoolName: '河南师范大学',
        roleName: 'student',
        status: 'disabled',
        createTime: '2023-05-15 10:25:00'
      }
    ];
    this.pagination.total = this.userList.length;
    
    // 添加示例学校选项
    this.schoolOptions = [
      { value: 1, label: '河南师范大学' },
      { value: 2, label: '郑州大学' },
      { value: 3, label: '河南大学' },
      { value: 4, label: '河南工业大学' },
      { value: 5, label: '河南科技大学' }
    ];
    
    // 注释掉原有的API调用
    // this.fetchUserList();
    // this.fetchSchoolOptions();
  },
  methods: {
    // 获取用户列表
    fetchUserList() {
      this.loading = true;
      const params = {
        ...this.searchForm,
        current: this.pagination.current,
        size: this.pagination.size
      };
      
      api.getUserList(params).then(response => {
        if (response.data.code === "200") {
          this.userList = response.data.data.records;
          this.pagination.total = response.data.data.total;
        } else {
          this.$message.error('获取用户列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取用户列表失败');
      });
    },
    
    // 获取学校选项
    fetchSchoolOptions() {
      api.getSchoolList({ schoolName: "" }).then(response => {
        if (response.data.code === "200") {
          this.schoolOptions = response.data.data.map(item => {
            return {
              value: item.schoolId,
              label: item.schoolName
            };
          });
        }
      });
    },
    
    // 处理搜索
    handleSearch() {
      this.pagination.current = 1;
      this.fetchUserList();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        userName: '',
        number: '',
        role: '',
        status: ''
      };
      this.handleSearch();
    },
    
    // 处理分页大小变更
    handleSizeChange(val) {
      this.pagination.size = val;
      this.fetchUserList();
    },
    
    // 处理当前页变更
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.fetchUserList();
    },
    
    // 添加用户
    handleAddUser() {
      this.isAdd = true;
      this.dialogTitle = '添加用户';
      this.userForm = {
        id: null,
        userName: '',
        number: '',
        email: '',
        password: '',
        schoolId: '',
        roleId: '3',
        status: 'normal'
      };
      this.dialogVisible = true;
    },
    
    // 编辑用户
    handleEdit(row) {
      this.isAdd = false;
      this.dialogTitle = '编辑用户';
      this.userForm = {
        id: row.id,
        userName: row.userName,
        number: row.number,
        email: row.email,
        schoolId: row.schoolId,
        roleId: row.roleId,
        status: row.status
      };
      this.dialogVisible = true;
    },
    
    // 重置表单
    resetForm() {
      if (this.$refs.userForm) {
        this.$refs.userForm.resetFields();
      }
    },
    
    // 提交表单
    submitForm() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          if (this.isAdd) {
            // 添加用户
            api.register(this.userForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('添加用户成功');
                this.dialogVisible = false;
                this.fetchUserList();
              } else {
                this.$message.error(response.data.message || '添加用户失败');
              }
            });
          } else {
            // 编辑用户
            api.updateUserAllInfo(this.userForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('编辑用户成功');
                this.dialogVisible = false;
                this.fetchUserList();
              } else {
                this.$message.error(response.data.message || '编辑用户失败');
              }
            });
          }
        }
      });
    },
    
    // 处理删除用户
    handleDelete(row) {
      this.$confirm('确定要删除该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 这里调用删除用户的API
        this.$message({
          type: 'success',
          message: `删除用户: ${row.userName}成功!`
        });
        this.fetchUserList();
      }).catch(() => {});
    },
    
    // 处理状态变更
    handleStatusChange(row) {
      const statusText = row.status === 'normal' ? '启用' : '禁用';
      this.$message({
        message: `已${statusText}用户: ${row.userName}`,
        type: 'success'
      });
      
      // 这里应该调用更新用户状态的API
      // api.updateUserStatus({userId: row.id, status: row.status})
    },
    
    // 处理重置密码
    handleResetPassword(row) {
      this.resetPwdForm = {
        newPassword: '',
        confirmPassword: ''
      };
      this.currentUserId = row.id;
      this.resetPwdVisible = true;
    },
    
    // 提交重置密码
    submitResetPwd() {
      this.$refs.resetPwdForm.validate(valid => {
        if (valid) {
          const params = {
            userId: this.currentUserId,
            newPassword: this.resetPwdForm.newPassword
          };
          
          api.resetPassword(params).then(response => {
            if (response.data.code === "200") {
              this.$message.success('密码重置成功');
              this.resetPwdVisible = false;
            } else {
              this.$message.error(response.data.message || '密码重置失败');
            }
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.user-list-container {
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
</style> 