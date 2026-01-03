// 创建路由对象
import VueRouter from "vue-router";
// import { useUserStore } from "../../public/stores/modules/user.js";
const router = new VueRouter({
  mode: "history",
  routes: [
    {
      path: "/",
      redirect: "/home",
      // component: () => import("./components/LoginPage.vue"),
    },
    {
      path: "/login",
      component: () => import("@/components/LoginPage.vue"),
    },
    {
      path: "/markdownEditor",
      component: () => import("@/views/MarkdownEditor.vue"),
    },
    {
      path: "/problem/:problemId",
      component: () => import("@/components/problem/AnsweringInfo.vue"),
    },
    {
      path: "/problem/:contestId/:problemId",
      component: () => import("@/components/contests/ContestProblemDetail.vue"),
    },
    {
      path: "/home",
      redirect: "/home/indexInfo",

      component: () => import("@/components/Home.vue"),
      children: [
        // {
        //   path: "chat",
        //   component: () => import("@/views/ChatInfo.vue"),
        // },
        // {
        //   path: "llm",
        //   component: () => import("@/views/LLMInfo.vue"),
        // },
        {
          path: "search/:searchContent",
          component: () => import("@/views/SearchInfo.vue"),
        },
        {
          path: "contestDetail/:contestId",
          component: () => import("@/views/ContestDetail.vue"),
        },
        {
          path: "contest",
          component: () => import("@/views/ContestInfo.vue"),
        },
        {
          path: "awardInfo",
          component: () => import("@/views/AwardInfo.vue"),
        },
        {
          path: "stuInfo/:userId",
          component: () => import("@/views/UserInfo.vue"),
        },
        {
          path: "memberInfo",
          component: () => import("@/views/memberInfo.vue"),
        },
        {
          path: "problemInfo",
          component: () => import("@/views/ProblemInfo.vue"),
        },

        {
          path: "indexInfo",
          component: () => import("@/views/IndexInfo.vue"),
        },
        {
          path: "articleInfo/:articleId",
          name: "articleInfo",
          component: () => import("@/views/ArticleInfo.vue"),
        },

        {
          path: "discussInfo",
          component: () => import("@/views/DiscussInfo.vue"),
        },
        {
          path: "trainingInfo",
          component: () => import("@/views/TrainingInfo.vue"),
        },
        {
          path: "training/:trainingId",
          component: () => import("@/views/TrainingPersonalInfo.vue"),
        },
      ],
    },
    // 后台管理系统路由
    {
      path: "/admin",
      component: () => import("@/components/admin/AdminLayout.vue"),
      redirect: "/admin/dashboard",
      children: [
        {
          path: "dashboard",
          name: "AdminDashboard",
          component: () => import("@/views/admin/Dashboard.vue"),
          meta: { title: "控制面板", icon: "el-icon-s-home" },
        },
        {
          path: "user/list",
          name: "AdminUserList",
          component: () => import("@/views/admin/UserList.vue"),
          meta: { title: "用户列表", icon: "el-icon-user" },
        },
        {
          path: "user/role",
          name: "AdminUserRole",
          component: () => import("@/views/admin/UserRole.vue"),
          meta: { title: "角色管理", icon: "el-icon-s-check" },
        },
        {
          path: "problem/list",
          name: "AdminProblemList",
          component: () => import("@/views/admin/ProblemList.vue"),
          meta: { title: "题目列表", icon: "el-icon-s-order" },
        },
        {
          path: "problem/edit/:id?",
          name: "AdminProblemEdit",
          component: () => import("@/views/admin/ProblemEdit.vue"),
          meta: { title: "编辑题目", icon: "el-icon-edit" },
          props: true,
        },
        {
          path: "problem/category",
          name: "AdminProblemCategory",
          component: () => import("@/views/admin/ProblemCategory.vue"),
          meta: { title: "题目分类", icon: "el-icon-menu" },
        },
        {
          path: "problem/tag",
          name: "AdminProblemTag",
          component: () => import("@/views/admin/ProblemTag.vue"),
          meta: { title: "标签管理", icon: "el-icon-collection-tag" },
        },
        {
          path: "contest/list",
          name: "AdminContestList",
          component: () => import("@/views/admin/ContestList.vue"),
          meta: { title: "竞赛列表", icon: "el-icon-s-cooperation" },
        },
        {
          path: "contest/edit/:id?",
          name: "AdminContestEdit",
          component: () => import("@/views/admin/ContestEdit.vue"),
          meta: { title: "编辑竞赛", icon: "el-icon-edit" },
          props: true,
        },
        {
          path: "contest/problems/:contestId",
          name: "AdminContestProblems",
          component: () => import("@/views/admin/ContestProblemList.vue"),
          meta: { title: "竞赛题目", icon: "el-icon-s-order" },
          props: true,
        },
        {
          path: "training/list",
          name: "AdminTrainingList",
          component: () => import("@/views/admin/TrainingList.vue"),
          meta: { title: "训练列表", icon: "el-icon-reading" },
        },
        {
          path: "training/edit/:id?",
          name: "AdminTrainingEdit",
          component: () => import("@/views/admin/TrainingCreate.vue"),
          meta: { title: "编辑训练", icon: "el-icon-edit" },
          props: true,
        },
        {
          path: "article/list",
          name: "AdminArticleList",
          component: () => import("@/views/admin/ArticleList.vue"),
          meta: { title: "文章列表", icon: "el-icon-document" },
        },
        {
          path: "article/category",
          name: "AdminArticleCategory",
          component: () => import("@/views/admin/ArticleCategory.vue"),
          meta: { title: "分类管理", icon: "el-icon-folder" },
        },
        {
          path: "system/config",
          name: "AdminSystemConfig",
          component: () => import("@/views/admin/SystemConfig.vue"),
          meta: { title: "系统设置", icon: "el-icon-setting" },
        },
      ],
    },
  ],
});

// 移除路由守卫
// router.beforeEach((to, from, next) => {
//   // 检查路由是否需要鉴权
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     const userStore = useUserStore();
//     const token = userStore.getToken;
//     const userInfo = userStore.userInfo;
//
//     // 判断用户是否登录
//     if (!token || token === 'null' || token === '') {
//       next({
//         path: '/login',
//         query: { redirect: to.fullPath }
//       });
//     } else if (to.matched.some(record => record.meta.adminAuth)) {
//       // 检查管理员权限
//       if (userInfo && userInfo.roleId === 1) { // 假设角色ID为1表示管理员
//         next();
//       } else {
//         next({ path: '/home' });
//       }
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// });

export default router;
