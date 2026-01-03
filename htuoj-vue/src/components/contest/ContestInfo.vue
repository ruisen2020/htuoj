<template>
  <div style="border-radius: 13px; margin-top: 20px">
    <el-card style="width: 300px" shadow="hover">
      <div style="display: flex; justify-content: space-between">
        <div style="font-size: 18px; font-weight: bold">近期比赛</div>
      </div>
      <div>
        <ContestMain ref="ContestMain" />
      </div>
    </el-card>
  </div>
</template>

<script>
import ContestMain from "./ContestMain";
import api from "@/common/api";
export default {
  name: "DailyTaskDate",
  components: { ContestMain },
  data() {
    return {
      restaurants: [],
      visible: false,

      selectedTab: null,
      form: {
        title: "",
        description: "",
        deadline: "",
      },
      rules: {
        title: [{ required: true, message: "请输入任务标题", trigger: "blur" }],
        deadline: [
          {
            required: true,
            type: "date",
            message: "请选择截止时间",
            trigger: "blur",
          },
        ],
      },
    };
  },

  // 一定要记得在组件销毁阶段清除定时器
  beforeDestroy() {
    // 清除任务定时器
    clearInterval(this.config.intervalTimer);
    // 清除定时器timeout
    clearTimeout(this.config.timeOutTimer);
  },
  methods: {
    addTask(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          api.addTask(this.form).then((response) => {
            if (response.data.code === "200") {
              this.$refs.DailyTaskMain.getAllTask();
              this.visible = false;
              this.form = {
                title: "",
                description: "",
                deadline: "",
              };
            }
          });
        } else {
          return false;
        }
      });
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (
          restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        );
      };
    },
    loadAll() {
      return [
        { value: "写题" },
        { value: "写题解" },
        { value: "写分享" },
        { value: "写讨论" },
      ];
    },
    handleSelect(item) {
      console.log(item);
    },
  },
  mounted() {
    this.restaurants = this.loadAll();
  },
};
</script>
<style>
/* 修改 el-popover 样式 https://zhuanlan.zhihu.com/p/774828511 */
.log-popover {
  border-radius: 13px;
}
</style>
<style scoped>
::v-deep .el-card {
  border-radius: 13px;
}
.top-tab {
  display: flex;
  justify-content: center;
  height: 40px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
}
.tab {
  width: 35px;
  height: 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 12px;
  align-items: center;
}
.tab:hover {
  background-color: #f0f0f0;
  border-radius: 5px;
  cursor: pointer;
}
.tab.active {
  background-color: #f5f5f5;
  border-radius: 5px;
}
</style>
