<template>
  <div class="content">
    <el-container style="border: 1px solid #eee; border-radius: 20px">
      <el-main>
        <el-table :data="currentTableData">
          <el-table-column prop="avatar" align="center" label="头像">
            <template #default="scope">
              <div
                style="
                  text-align: center;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                ">
                <el-avatar :src="scope.row.avatar" shape="square" />
              </div>
              <!--用户头像-->
            </template>
            <!--            <template slot-scope="scope">-->
            <!--              <img :src="scope.row.avatar" class="avatar" height="50px"/>-->
            <!--            </template>-->
            <!-- <img :src="avatar" class="avatar" width="300px" /> -->
          </el-table-column>
          <el-table-column prop="userName" align="center" label="姓名">
            <template default="scope">
              <router-link
                style="text-decoration: none; color: #606266"
                to="/home/stuInfo/">
                {{ scope.row.userName }}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="number" align="center" label="学号">
          </el-table-column>
          <el-table-column prop="profile" align="center" label="个人简介">
          </el-table-column>
          <el-table-column prop="profile" align="center">
            <template slot="header" slot-scope="scope">
              {{ scope.number }}
              <div
                style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                  color: #909399;
                ">
                <div
                  @click="toggleModal(true)"
                  :style="{
                    width: '60px',
                    cursor: 'pointer',
                  }"
                  :class="{ selectedClass: selected }">
                  <div style="text-align: center">总过题数</div>
                </div>
                <el-divider
                  direction="vertical"
                  content-position="center"></el-divider>
                <div
                  class="follower"
                  @click="toggleModal(false)"
                  :style="{
                    width: '60px',
                    cursor: 'pointer',
                  }"
                  :class="{ selectedClass: !selected }">
                  <div style="text-align: center">在线状态</div>
                </div>
              </div>
            </template>
            <template slot-scope="scope">
              <div v-if="selected == true">{{ scope.row.acceptCount }}</div>
              <div v-else>
                <span
                  v-if="scope.row.onlineStatus == '在线'"
                  style="color: #00f268">
                  在线1212
                </span>
                <span v-else>
                  离线: {{ formattedTimeDifference(scope.row.lastLoginTime) }}
                </span>
              </div>
              <!-- <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)"
                >Edit</el-button
              >
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
                >Delete</el-button
              > -->
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-link target="_blank" :underline="false">
            
          </el-link> -->
      </el-main>
    </el-container>
  </div>
</template>

<script>
import _ from "lodash";

export default {
  data() {
    return {
      currentTableData: null,
      selected: true,
      sortData: null,
    };
  },
  props: {
    tableData: {
      type: Array,
    },
  },

  watch: {
    tableData: {
      handler() {
        console.log("tableData changed");
        this.sortData = _.cloneDeep(this.tableData);
        this.currentTableData = this.sortData;
        this.sortTableData();
      },
      immediate: true, // 立即执行一次
    },
  },

  methods: {
    sortTableData() {
      if (this.selected == true) {
        this.currentTableData.sort((a, b) => b.acceptCount - a.acceptCount);
      } else {
        this.currentTableData.sort((a, b) => {
          const aTime = new Date(a.lastLoginTime);
          const bTime = new Date(b.lastLoginTime);
          console.log(aTime, bTime);
          return bTime - aTime;
        });
      }
    },
    toggleModal(flag) {
      this.selected = flag;
      this.currentTableData = this.sortData;
      this.sortTableData();
    },
    formattedTimeDifference(inputDate) {
      if (!inputDate) return "";
      const inputDateTime = new Date(inputDate);
      const now = new Date();
      const millisecondsDifference = now - inputDateTime;

      const secondsDifference = Math.floor(millisecondsDifference / 1000);
      const minutesDifference = Math.floor(secondsDifference / 60);
      const hoursDifference = Math.floor(minutesDifference / 60);
      const daysDifference = Math.floor(hoursDifference / 24);
      const monthsDifference = Math.floor(daysDifference / 30);
      const yearsDifference = Math.floor(monthsDifference / 12);

      const timeDifference = {
        years: yearsDifference,
        months: monthsDifference % 12,
        days: daysDifference % 30,
        hours: hoursDifference % 24,
        minutes: minutesDifference % 60,
        seconds: secondsDifference % 60,
      };
      const { years, months, days, hours, minutes } = timeDifference;
      if (years > 0) {
        return `${years} 年前`;
      }
      if (months > 0) {
        return `${months} 月前`;
      }
      if (days > 0) {
        return `${days} 天前`;
      }
      if (hours > 0) {
        return `${hours} 小时前`;
      }
      if (minutes > 0) {
        return `${minutes} 分钟前`;
      }
    },
  },
};
</script>

<style scoped>
.selectedClass {
  color: #000;
}
</style>
