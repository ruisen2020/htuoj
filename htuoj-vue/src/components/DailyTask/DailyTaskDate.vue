<template>
  <div style="border-radius: 13px">
    <el-card style="width: 300px" shadow="hover">
      <!-- <div class="top-tab">
       <div
           class="tab"
           :class="{ active: selectedTab === 0 }"
           @click="selectTab(0)"
           style="margin-left: 0">
         <span style="font-size: 14px; color: #D0D0D0;">{{day[0]}}</span>
         <span style="font-size: 12px; padding-top: 3px; font-family: 'Andale Mono'; font-weight: bolder">{{date[0]}}</span>
       </div>
       <div v-for="index in 6" :key="index">
         <div
             class="tab"
             :class="{ active: selectedTab === index }"
             @click="selectTab(index)"
             style="margin-left: 7px">
           <span style="font-size: 14px; color: #D0D0D0;">{{day[index]}}</span>
           <span style="font-size: 12px; padding-top: 4px; font-family: 'Andale Mono'; font-weight: bolder">{{date[index]}}</span>
         </div>
       </div>
     </div> -->
      <div style="display: flex; justify-content: space-between">
        <div style="font-size: 18px; font-weight: bold">任务清单</div>
        <div class="new-task">
          <el-popover
            title="新建任务"
            v-model="visible"
            placement="bottom-end"
            width="280"
            popper-class="log-popover">
            <div>
              <el-form
                :model="form"
                ref="form"
                label-width="80px"
                :rules="rules">
                <el-form-item label="任务标题" prop="title">
                  <!-- <el-input
                    style="width: 150px; height: 20px"
                    v-model="form.name"
                  /> -->
                  <el-autocomplete
                    size="medium"
                    style="width: 200px; height: 40px"
                    class="inline-input"
                    v-model="form.title"
                    :fetch-suggestions="querySearch"
                    placeholder="请输入任务标题"
                    @select="handleSelect"></el-autocomplete>
                </el-form-item>
                <el-form-item label="任务描述">
                  <el-input
                    size="medium"
                    style="width: 200px; height: 40px"
                    v-model="form.description"
                    placeholder="请输入任务描述" />
                </el-form-item>
                <el-form-item label="截止时间" prop="deadline">
                  <el-date-picker
                    style="width: 200px; height: 40px"
                    v-model="form.deadline"
                    type="datetime"
                    placeholder="选择日期时间"
                    default-time="12:00:00"
                    size="medium">
                  </el-date-picker>
                </el-form-item>
              </el-form>
            </div>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="visible = false"
                >取消</el-button
              >
              <el-button type="primary" size="mini" @click="addTask('form')"
                >确定</el-button
              >
            </div>
            <el-button size="small" round slot="reference">新建任务</el-button>
          </el-popover>
          <!-- <el-button size="small" round>新建任务</el-button> -->
        </div>
      </div>
      <div>
        <DailyTaskMain ref="DailyTaskMain" />
      </div>
    </el-card>
  </div>
</template>

<script>
import DailyTaskMain from "@/components/DailyTask/DailyTaskMain.vue";
import api from "@/common/api";
export default {
  name: "DailyTaskDate",
  components: { DailyTaskMain },
  data() {
    return {
      restaurants: [],
      visible: false,
      origin: ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"],
      day: ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"],
      date: [],
      // 定时任务配置
      config: {
        time: "00:05:00", // 每天几点执行
        interval: 1, // 隔几天执行一次
        runNow: false, // 是否立即执行
        intervalTimer: "",
        timeOutTimer: "",
      },
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
