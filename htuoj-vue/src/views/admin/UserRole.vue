<template>
  <div class="role-container">
    <div class="page-header">
      <h2>角色管理</h2>
      <el-button type="primary" size="small" @click="handleAddRole">
        <i class="el-icon-plus"></i>
        添加角色
      </el-button>
    </div>
    
    <el-card shadow="never">
      <el-table :data="roleList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="roleKey" label="角色标识" width="150"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column prop="userCount" label="用户数" width="100" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="'normal'"
              :inactive-value="'disabled'"
              @change="handleStatusChange(scope.row)"
              :disabled="scope.row.isSystem">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="260" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)" :disabled="scope.row.isSystem">编辑</el-button>
            <el-button type="text" size="small" @click="handlePermission(scope.row)">权限设置</el-button>
            <el-button type="text" size="small" @click="handleUserList(scope.row)">用户列表</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)" :disabled="scope.row.isSystem || scope.row.userCount > 0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 角色表单对话框 -->
    <el-dialog :title="isEdit ? '编辑角色' : '添加角色'" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form ref="roleForm" :model="roleForm" :rules="roleRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="roleForm.roleKey" placeholder="请输入角色标识，如admin"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="roleForm.description" rows="3" placeholder="请输入角色描述"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="roleForm.status">
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
    
    <!-- 权限设置对话框 -->
    <el-dialog title="权限设置" :visible.sync="permissionVisible" width="600px">
      <div v-if="currentRole.roleName" class="permission-title">
        正在为角色 <span style="color: #409EFF; font-weight: bold;">{{ currentRole.roleName }}</span> 设置权限
      </div>
      
      <el-tree
        ref="permissionTree"
        :data="permissionData"
        :props="permissionProps"
        show-checkbox
        node-key="id"
        :default-checked-keys="selectedPermissions"
        @check="handlePermissionCheck">
      </el-tree>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="permissionVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePermissions">保 存</el-button>
      </div>
    </el-dialog>
    
    <!-- 角色用户列表对话框 -->
    <el-dialog title="角色用户列表" :visible.sync="userListVisible" width="800px">
      <div v-if="currentRole.roleName" class="permission-title">
        角色 <span style="color: #409EFF; font-weight: bold;">{{ currentRole.roleName }}</span> 的用户列表
      </div>
      
      <el-table :data="roleUserList" border style="width: 100%" v-loading="userListLoading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="avatar" label="头像" width="80" align="center">
          <template slot-scope="scope">
            <el-avatar :size="40" :src="scope.row.avatar"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="120"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'normal'" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleRemoveUser(scope.row)">移除角色</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="userListVisible = false">关 闭</el-button>
        <el-button type="primary" @click="handleAddUser">添加用户</el-button>
      </div>
    </el-dialog>
    
    <!-- 添加角色用户对话框 -->
    <el-dialog title="添加用户到角色" :visible.sync="addUserVisible" width="800px">
      <el-form :inline="true" :model="userSearchForm" class="search-form" style="margin-bottom: 15px;">
        <el-form-item label="用户名">
          <el-input v-model="userSearchForm.userName" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userSearchForm.email" placeholder="请输入邮箱" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchUsers">查询</el-button>
          <el-button @click="resetUserSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table
        :data="userList"
        border
        style="width: 100%"
        v-loading="userSearchLoading"
        @selection-change="handleUserSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="avatar" label="头像" width="80" align="center">
          <template slot-scope="scope">
            <el-avatar :size="40" :src="scope.row.avatar"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="120"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'normal'" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container" style="margin-top: 15px;">
        <el-pagination
          @size-change="handleUserSizeChange"
          @current-change="handleUserCurrentChange"
          :current-page="userPagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="userPagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="userPagination.total">
        </el-pagination>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="addUserVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUsersToRole">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "AdminUserRole",
  data() {
    return {
      // 角色列表
      roleList: [],
      loading: false,
      
      // 角色表单
      dialogVisible: false,
      isEdit: false,
      roleForm: {
        id: null,
        roleName: '',
        roleKey: '',
        description: '',
        status: 'normal'
      },
      roleRules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        roleKey: [
          { required: true, message: '请输入角色标识', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '角色标识只能包含字母、数字和下划线', trigger: 'blur' }
        ]
      },
      
      // 权限设置
      permissionVisible: false,
      currentRole: {},
      permissionData: [],
      permissionProps: {
        label: 'name',
        children: 'children'
      },
      selectedPermissions: [],
      checkedKeys: [],
      checkedNodes: [],
      
      // 角色用户
      userListVisible: false,
      roleUserList: [],
      userListLoading: false,
      
      // 添加用户
      addUserVisible: false,
      userList: [],
      selectedUsers: [],
      userSearchLoading: false,
      userSearchForm: {
        userName: '',
        email: ''
      },
      userPagination: {
        current: 1,
        size: 10,
        total: 0
      }
    };
  },
  created() {
    this.fetchRoleList();
    this.fetchPermissionTree();
  },
  methods: {
    // 获取角色列表
    fetchRoleList() {
      this.loading = true;
      
      api.getRoleList({current: 1, size: 100}).then(response => {
        if (response.data.code === "200") {
          this.roleList = response.data.data.records.map(item => {
            return {
              ...item,
              isSystem: ['admin', 'teacher', 'student'].includes(item.roleKey)
            };
          });
        } else {
          this.$message.error('获取角色列表失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$message.error('获取角色列表失败');
      });
    },
    
    // 获取权限树
    fetchPermissionTree() {
      api.getPermissionTree().then(response => {
        if (response.data.code === "200") {
          this.permissionData = response.data.data;
        } else {
          this.$message.error('获取权限数据失败');
        }
      });
    },
    
    // 添加角色
    handleAddRole() {
      this.isEdit = false;
      this.roleForm = {
        id: null,
        roleName: '',
        roleKey: '',
        description: '',
        status: 'normal'
      };
      this.dialogVisible = true;
    },
    
    // 编辑角色
    handleEdit(row) {
      if (row.isSystem) {
        this.$message.warning('系统角色不能编辑');
        return;
      }
      
      this.isEdit = true;
      this.roleForm = {
        id: row.id,
        roleName: row.roleName,
        roleKey: row.roleKey,
        description: row.description,
        status: row.status
      };
      this.dialogVisible = true;
    },
    
    // 重置表单
    resetForm() {
      this.$refs.roleForm && this.$refs.roleForm.resetFields();
    },
    
    // 提交表单
    submitForm() {
      this.$refs.roleForm.validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 更新角色
            api.updateRole(this.roleForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('更新成功');
                this.dialogVisible = false;
                this.fetchRoleList();
              } else {
                this.$message.error(response.data.message || '更新失败');
              }
            });
          } else {
            // 添加角色
            api.addRole(this.roleForm).then(response => {
              if (response.data.code === "200") {
                this.$message.success('添加成功');
                this.dialogVisible = false;
                this.fetchRoleList();
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
    
    // 处理角色状态变更
    handleStatusChange(row) {
      if (row.isSystem) {
        this.$message.warning('系统角色不能修改状态');
        row.status = row.status === 'normal' ? 'disabled' : 'normal'; // 恢复原状态
        return;
      }
      
      const statusText = row.status === 'normal' ? '启用' : '禁用';
      api.updateRoleStatus({
        roleId: row.id,
        status: row.status
      }).then(response => {
        if (response.data.code === "200") {
          this.$message.success(`已${statusText}角色: ${row.roleName}`);
        } else {
          this.$message.error(response.data.message || `${statusText}失败`);
          // 恢复原状态
          row.status = row.status === 'normal' ? 'disabled' : 'normal';
        }
      }).catch(() => {
        // 恢复原状态
        row.status = row.status === 'normal' ? 'disabled' : 'normal';
        this.$message.error(`${statusText}失败`);
      });
    },
    
    // 处理删除角色
    handleDelete(row) {
      if (row.isSystem) {
        this.$message.warning('系统角色不能删除');
        return;
      }
      
      if (row.userCount > 0) {
        this.$message.warning('该角色下有用户，不能删除');
        return;
      }
      
      this.$confirm('确定要删除该角色吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteRole({roleId: row.id}).then(response => {
          if (response.data.code === "200") {
            this.$message.success('删除成功');
            this.fetchRoleList();
          } else {
            this.$message.error(response.data.message || '删除失败');
          }
        });
      }).catch(() => {});
    },
    
    // 显示权限设置对话框
    handlePermission(row) {
      this.currentRole = row;
      // 获取角色的权限
      api.getRolePermissions({roleId: row.id}).then(response => {
        if (response.data.code === "200") {
          this.selectedPermissions = response.data.data;
          this.permissionVisible = true;
        } else {
          this.$message.error('获取角色权限失败');
        }
      });
    },
    
    // 处理权限选择
    handlePermissionCheck() {
      // 更新选中的权限
      this.checkedKeys = this.$refs.permissionTree.getCheckedKeys();
      this.checkedNodes = this.$refs.permissionTree.getCheckedNodes();
    },
    
    // 保存权限
    savePermissions() {
      const checkedNodes = this.$refs.permissionTree.getCheckedNodes();
      const checkedKeys = checkedNodes.map(node => node.id);
      
      api.updateRolePermissions({
        roleId: this.currentRole.id,
        permissionIds: checkedKeys
      }).then(response => {
        if (response.data.code === "200") {
          this.$message.success('权限设置成功');
          this.permissionVisible = false;
        } else {
          this.$message.error(response.data.message || '权限设置失败');
        }
      });
    },
    
    // 显示角色用户列表
    handleUserList(row) {
      this.currentRole = row;
      this.userListLoading = true;
      
      api.getRoleUsers({roleId: row.id}).then(response => {
        if (response.data.code === "200") {
          this.roleUserList = response.data.data;
          this.userListVisible = true;
        } else {
          this.$message.error('获取角色用户列表失败');
        }
        this.userListLoading = false;
      }).catch(() => {
        this.userListLoading = false;
        this.$message.error('获取角色用户列表失败');
      });
    },
    
    // 移除角色用户
    handleRemoveUser(user) {
      this.$confirm(`确定要将用户 ${user.userName} 从角色 ${this.currentRole.roleName} 中移除吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.removeUserFromRole({
          userId: user.id,
          roleId: this.currentRole.id
        }).then(response => {
          if (response.data.code === "200") {
            this.$message.success('移除成功');
            // 重新获取角色用户列表
            this.handleUserList(this.currentRole);
            // 更新角色列表
            this.fetchRoleList();
          } else {
            this.$message.error(response.data.message || '移除失败');
          }
        });
      }).catch(() => {});
    },
    
    // 显示添加用户对话框
    handleAddUser() {
      this.addUserVisible = true;
      this.userSearchForm = {
        userName: '',
        email: ''
      };
      this.userPagination = {
        current: 1,
        size: 10,
        total: 0
      };
      this.searchUsers();
    },
    
    // 搜索用户
    searchUsers() {
      this.userSearchLoading = true;
      
      const params = {
        ...this.userSearchForm,
        current: this.userPagination.current,
        size: this.userPagination.size,
        roleId: this.currentRole.id,
        notInRole: true // 查询不在当前角色的用户
      };
      
      api.getUserList(params).then(response => {
        if (response.data.code === "200") {
          this.userList = response.data.data.records;
          this.userPagination.total = response.data.data.total;
        } else {
          this.$message.error('获取用户列表失败');
        }
        this.userSearchLoading = false;
      }).catch(() => {
        this.userSearchLoading = false;
        this.$message.error('获取用户列表失败');
      });
    },
    
    // 重置用户搜索
    resetUserSearch() {
      this.userSearchForm = {
        userName: '',
        email: ''
      };
      this.userPagination.current = 1;
      this.searchUsers();
    },
    
    // 处理用户分页大小变更
    handleUserSizeChange(val) {
      this.userPagination.size = val;
      this.searchUsers();
    },
    
    // 处理用户当前页变更
    handleUserCurrentChange(val) {
      this.userPagination.current = val;
      this.searchUsers();
    },
    
    // 处理用户选择变更
    handleUserSelectionChange(selection) {
      this.selectedUsers = selection;
    },
    
    // 添加用户到角色
    addUsersToRole() {
      if (this.selectedUsers.length === 0) {
        this.$message.warning('请选择要添加的用户');
        return;
      }
      
      const userIds = this.selectedUsers.map(user => user.id);
      
      api.addUsersToRole({
        roleId: this.currentRole.id,
        userIds: userIds
      }).then(response => {
        if (response.data.code === "200") {
          this.$message.success('添加成功');
          this.addUserVisible = false;
          // 重新获取角色用户列表
          this.handleUserList(this.currentRole);
          // 更新角色列表
          this.fetchRoleList();
        } else {
          this.$message.error(response.data.message || '添加失败');
        }
      });
    }
  }
};
</script>

<style scoped>
.role-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.permission-title {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #EBEEF5;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 