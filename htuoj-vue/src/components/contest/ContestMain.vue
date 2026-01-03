<template>
  <div class="task-container">
    <!-- <el-empty
      :image-size="100"
      v-if="total == 0"
      description="暂无数据"></el-empty> -->
    <ul
      v-infinite-scroll="load"
      infinite-scroll-distance="10"
      :infinite-scroll-disabled="disabled"
      class="task-list">
      <transition-group appear name="slide">
        <li v-for="(item, index) in contestList" :key="index">
          <el-card class="task" shadow="hover" style="width: 230px">
            <div class="task-inner">
              <div class="check-box">
                <span v-if="item.oj == 'codeforces'"
                  ><img
                    style="width: 20px; height: 20px"
                    src="@/assets/cf.png" />
                </span>
                <span v-else-if="item.oj == 'leetcode'"
                  ><img
                    style="width: 20px; height: 20px"
                    src="@/assets/leetcode.png" />
                </span>
                <span v-else-if="item.oj == 'luogu'"
                  ><img
                    style="width: 20px; height: 20px"
                    src="@/assets/luogu.png" />
                </span>
                <span v-else-if="item.oj == 'nowcoder'"
                  ><img
                    style="width: 20px; height: 20px"
                    src="@/assets/nowcoder.png" />
                </span>
                <span v-else-if="item.oj == 'atcoder'"
                  ><img
                    style="width: 20px; height: 20px"
                    src="@/assets/atcoder.png" />
                </span>
                <span v-else-if="item.oj == 'acwing'"
                  ><img
                    style="width: 20px; height: 20px"
                    src="@/assets/acwing.png" />
                </span>
              </div>
              <div style="">
                <div class="description">
                  <!-- <div style="font-size: 16px; color: #303133; font-weight: bold">
                  {{ item.title }}
                </div> -->

                  <el-tooltip
                    v-show="true"
                    class="item"
                    effect="dark"
                    :content="item.title"
                    placement="bottom">
                    <div
                      class="description-text"
                      style="
                        width: 180px;
                        max-width: 180px;
                        position: relative;
                        height: 20px;
                        font-size: 14px;
                        white-space: nowrap !important;
                        overflow: hidden !important;
                        text-overflow: ellipsis !important;
                        color: #0000008c;
                      ">
                      <el-link
                        style="font-size: 13px; color: #3172cc"
                        :href="item.url"
                        target="_blank"
                        :underline="false">
                        {{ item.title }}</el-link
                      >
                    </div>
                  </el-tooltip>
                </div>
                <div
                  style="
                    display: flex;
                    justify-content: space-between;
                    margin-top: 5px;
                  ">
                  <!-- <span style="font-size: 14px; color: #909399"
                      >截止时间：</span
                    > -->
                  <div style="font-size: 12px; color: rgb(31, 54, 41)">
                    {{ formatDate(new Date(item.startTime)) }}
                  </div>
                  <div style="font-size: 12px; color: rgb(31, 54, 41)">
                    {{ formatTime(new Date(item.duration)) }}
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </li>
      </transition-group>
    </ul>
    <!-- <p
        v-if="noMore"
        style="font-size: small; display: flex; justify-content: center">
        没有更多了
      </p> -->
    <p
      v-loading="loading"
      element-loading-spinner="el-icon-loading"
      style="padding-top: 40px" />
  </div>
</template>

<script>
import api from "@/common/api";
export default {
  props: {
    curDate: null,
  },
  data() {
    return {
      contestList: null,
      task: null,
      loading: false,
      index: 1,
      total: null,
      formData: {
        current: 1,
        size: 5,
      },
    };
  },
  mounted() {
    this.getAllContest();
  },
  computed: {
    noMore() {
      return this.index >= Math.ceil(this.total / 5);
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  methods: {
    formatTime(time) {
      const hours = time / 3600;
      return `${hours.toFixed(1)} h`;
    },
    cardColor(date) {
      const theDate = new Date(date);
      const now = new Date();
      if (theDate < now) {
        return { backgroundColor: "#ffdc00" };
      } else if (this.isSameDay(theDate, now))
        return { backgroundColor: "#1cc076" };
    },
    formatDate(date) {
      const year = date.getFullYear().toString().slice(-2); // 获取年份的后两位
      const month = (date.getMonth() + 1).toString().padStart(2, "0"); // 月份从0开始，需要加1
      const day = date.getDate().toString().padStart(2, "0");
      const hours = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      //   const seconds = date.getSeconds().toString().padStart(2, "0");

      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },

    isSameDay(date1, date2) {
      return (
        date1.getFullYear() === date2.getFullYear() &&
        date1.getMonth() === date2.getMonth() &&
        date1.getDate() === date2.getDate()
      );
    },
    getAllContest() {
      this.index = 1;
      api.getContestListByOtherOJ().then((response) => {
        if (response.data.code == "200") {
          this.contestList = response.data.data;
        }
      });
      api.getAllTask(this.formData).then((response) => {
        if (response.data.code == "200") {
          this.task = response.data.data.records;
          this.total = response.data.data.total;
        }
      });
    },
    getNextTaskList() {
      api.getAllTask(this.formData).then((response) => {
        if (response.data.code == "200") {
          this.task = this.task.concat(response.data.data.records);
        }
      });
    },
    // 无限滚动
    load() {
      this.loading = true;
      setTimeout(() => {
        // 此处调用后台方法
        this.index++;
        this.formData.current = this.index;
        this.getNextTaskList();
        this.loading = false;
      }, 500);
    },
    finished(taskId, index) {
      if (index === -1) return;
      api.completedTask({ taskId: taskId }).then((response) => {
        console.log(response);
      });
      setTimeout(() => {
        this.task.splice(index, 1);
      }, 500);
    },
  },
};
</script>

<style scoped>
::v-deep .el-card__body {
  padding: 10px;
}
::v-deep .el-card {
  border-radius: 13px;
}
.task-container {
  height: 308px;
  overflow: auto;
}
.task-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
  width: 100%;
}
.task {
  background-color: #ffffff;
  margin-top: 10px;
  /* background-color: rgb(3, 243, 3); */
  /* background-color: #2Db55D; */
}
.task-inner {
  display: flex;
  align-items: center;
}
.check-box {
  width: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 10px;
}
.description {
  /* width: 50px;
    height: 50px; */
  /* flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between; */
  /* align-items: center; */
}
.slide-enter,
.slide-leave-to {
  opacity: 0;
  transform: translateX(50px);
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.5s ease;
}

.slide-move {
  transition: transform 0.5s;
}
/* 美化滚动条 */
.task-container::-webkit-scrollbar {
  width: 12px;
}

.task-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.task-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

.task-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
