<template>
  <div class="admin-layout">
    <el-container style="height: 100vh;">
      <el-aside width="220px" style="background-color: #fff;">
        <div class="logo-container">
          <img src="@/assets/logo.png" alt="HTUOJ Logo" class="logo-img">
          <h2 class="logo-title">HTUOJ 管理系统</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          background-color="#fff"
          text-color="#303133"
          active-text-color="#409EFF"
          router
          unique-opened
        >
          <el-menu-item index="/admin/dashboard">
            <i class="el-icon-s-home"></i>
            <span slot="title">控制面板</span>
          </el-menu-item>
          
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/user/list">用户列表</el-menu-item>
            <el-menu-item index="/admin/user/role">角色管理</el-menu-item>
          </el-submenu>
          
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-order"></i>
              <span>题目管理</span>
            </template>
            <el-menu-item index="/admin/problem/list">题目列表</el-menu-item>
            <el-menu-item index="/admin/problem/category">题目分类</el-menu-item>
            <el-menu-item index="/admin/problem/tag">标签管理</el-menu-item>
          </el-submenu>
          
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-s-cooperation"></i>
              <span>竞赛管理</span>
            </template>
            <el-menu-item index="/admin/contest/list">竞赛列表</el-menu-item>
            <el-menu-item index="/admin/contest/edit">创建竞赛</el-menu-item>
          </el-submenu>
          
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-reading"></i>
              <span>训练管理</span>
            </template>
            <el-menu-item index="/admin/training/list">训练列表</el-menu-item>
            <el-menu-item index="/admin/training/edit">创建训练</el-menu-item>
          </el-submenu>
          
          <el-submenu index="5">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>文章管理</span>
            </template>
            <el-menu-item index="/admin/article/list">文章列表</el-menu-item>
            <el-menu-item index="/admin/article/category">分类管理</el-menu-item>
          </el-submenu>
          
          <el-menu-item index="/admin/system/config">
            <i class="el-icon-setting"></i>
            <span slot="title">系统设置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6;">
          <div style="display: flex; justify-content: space-between; align-items: center; height: 100%;">
            <div>
              <i class="el-icon-s-fold" style="font-size: 20px; cursor: pointer;"></i>
            </div>
            <div>
              <el-dropdown trigger="click">
                <span class="el-dropdown-link" style="cursor: pointer;">
                  <el-avatar size="small" :src="userAvatar"></el-avatar>
                  <span style="margin-left: 10px;">{{ userName }}</span>
                  <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>个人资料</el-dropdown-item>
                  <el-dropdown-item>修改密码</el-dropdown-item>
                  <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { useUserStore } from "../../../public/stores/modules/user";

export default {
  name: "AdminLayout",
  data() {
    return {
      activeMenu: this.$route.path,
      userAvatar: "",
      userName: "",
    };
  },
  created() {
    const userStore = useUserStore();
    if (userStore.userInfo) {
      this.userAvatar = userStore.getUserAvatar;
      this.userName = userStore.getUserName;
    }
  },
  watch: {
    $route: {
      handler(route) {
        this.activeMenu = route.path;
      },
      immediate: true
    }
  },
  methods: {
    logout() {
      const userStore = useUserStore();
      userStore.reset();
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.el-header {
  line-height: 60px;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.el-aside {
  color: #333;
  transition: width 0.3s;
  overflow: hidden;
}

.el-menu {
  border-right: none;
}

.logo-container {
  height: 55px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 0;
  border-bottom: 1px solid #e6e6e6;
  background-color: #fff;
}

.logo-img {
  width: 30px;
  height: 30px;
  margin-right: 8px;
}

.logo-title {
  color: #303133;
  font-size: 15px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.el-menu-item, .el-submenu__title {
  height: 40px;
  line-height: 40px;
  font-size: 14px;
}

.el-menu-item i, .el-submenu__title i {
  margin-right: 8px;
  font-size: 16px;
}

/* 一级菜单项样式 */
.el-menu > .el-menu-item, 
.el-menu > .el-submenu > .el-submenu__title {
  height: 40px;
  line-height: 40px;
  font-size: 14px;
}

.el-menu > .el-menu-item i, 
.el-menu > .el-submenu > .el-submenu__title i {
  font-size: 16px;
}

/* 二级菜单项样式 */
.el-submenu .el-menu-item {
  min-width: 0;
  padding-left: 48px !important;
  height: 40px;
  line-height: 40px;
  font-size: 14px;
}

.el-submenu .el-menu-item i {
  font-size: 16px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
}

.el-dropdown-link .el-avatar {
  background-color: #409EFF;
}

.el-menu-item:hover, .el-submenu__title:hover {
  background-color: #f5f7fa !important;
}

.el-menu-item.is-active {
  background-color: #ecf5ff !important;
}
</style> 