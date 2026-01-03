<template>
  <div>
    <div id="appMemberInfo">
      <div class="container">
        <el-form :inline="true" class="demo-form-inline">
          <el-input v-model="formData.userName" placeholder="请输入用户名" style="width: 200px; margin-left: 10px"></el-input>
          <el-select style="width: 200px; margin-left: 10px" v-model="formData.schoolId" filterable remote
            reserve-keyword placeholder="请输入学校名称" :remote-method="remoteMethod" :loading="loading">
            <el-option v-for="item in schoolOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <el-input v-model="formData.number" placeholder="请输入学号" style="width: 200px; margin-left: 10px"></el-input>

          <el-form-item>
            <el-button type="primary" @click="onSubmit" style="margin-left: 10px">查询
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="content">
        <el-container style="border: 1px solid #eee; border-radius: 20px">
          <el-main>
            <el-table :data="tableData" stripe>
              <el-table-column prop="avatar" align="center" label="头像">
                <template #default="scope">
                  <div style="
                      text-align: center;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    ">
                    <el-avatar fit="contain" :src="scope.row.avatar" shape="square" />
                  </div>
                  <!--用户头像-->
                </template>
                <!--            <template slot-scope="scope">-->
                <!--              <img :src="scope.row.avatar" class="avatar" height="50px"/>-->
                <!--            </template>-->
                <!-- <img :src="avatar" class="avatar" width="300px" /> -->
              </el-table-column>
              <el-table-column prop="userName" align="center" label="用户名">
                <template #default="scope">
                  <!-- <router-link
                    style="text-decoration: none; color: #606266"
                    :to="'/home/stuInfo/' + scope.row.id">
                    {{ scope.row.userName }}
                  </router-link> -->
                  <el-link :href="'/home/stuInfo/' + scope.row.id" target="_blank" :underline="false">
                    {{ scope.row.userName }}</el-link>
                </template>
              </el-table-column>
              <el-table-column prop="schoolName" align="center" label="学校">
              </el-table-column>
              <el-table-column prop="number" align="center" label="学号">
              </el-table-column>
              <el-table-column prop="profile" align="center" label="个人简介">
              </el-table-column>
              <el-table-column prop="profile" align="center">
                <template #header="scope">
                  {{ scope.number }}
                  <div style="
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      color: #909399;
                    ">
                    <div @click="toggleModal(true)" :style="{
                      width: '60px',
                      cursor: 'pointer',
                    }" :class="{ selectedClass: selected }">
                      <div style="text-align: center">总过题数</div>
                    </div>
                    <el-divider direction="vertical" content-position="center"></el-divider>
                    <div class="follower" @click="toggleModal(false)" :style="{
                      width: '60px',
                      cursor: 'pointer',
                    }" :class="{ selectedClass: !selected }">
                      <div style="text-align: center">在线状态</div>
                    </div>
                  </div>
                </template>
                <template #default="scope">
                  <div v-if="selected == true">{{ scope.row.acceptCount }}</div>
                  <div v-else>
                    <span v-if="scope.row.onlineStatus == '在线'" style="color: #00f268">
                      在线
                    </span>
                    <span v-else>
                      离线:
                      {{ formattedTimeDifference(scope.row.lastLoginTime) }}
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
      <div class="last">
        <el-pagination background @current-change="handleCurrentChange" :current-page="formData.current"
          :page-size="20" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>
    <el-footer style="text-align: center">© 2024-2025 HTUOJ 版权所有 |
      <a style="text-decoration: none" href="https://beian.miit.gov.cn/" target="_blank">吉ICP备2024023192号</a>
      <div style="
        width: 260px;
        text-align: center;
        margin: 0 auto;
          display: flex; align-items: center;
        ">
        <img src="@/assets/备案图标.png" style="width: 16px; height: 16px; margin-left: 10px" />
        <a style="text-decoration: none; margin-left: 5px"
          href="https://beian.mps.gov.cn/#/query/webSearch?code=22017302000468" rel="noreferrer"
          target="_blank">吉公网安备22017302000468号</a>
      </div>
    </el-footer>
  </div>
</template>
<script>
import api from "@/common/api";
export default {
  data() {
    return {
      schoolOptions: [],
      loading: false,
      total: 0,
      selected: true,
      tableData: [],
      formData: {
        current: 1,
        size: 20,
        userName: null,
        number: null,
        orderBy: "acceptCount",
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.schoolOptions = [];
          api.getSchoolList({ schoolName: query }).then((response) => {
            if (response.data.code === "200") {
              for (let i = 0; i < response.data.data.length; i++) {
                this.schoolOptions.push({
                  value: response.data.data[i].schoolId,
                  label: response.data.data[i].schoolName,
                });
              }
            }
          });
          // this.options = this.list.filter(item => {
          //   return item.label.toLowerCase()
          //     .indexOf(query.toLowerCase()) > -1;
          // });
        }, 200);
      } else {
        this.schoolOptions = [];
      }
    },
    handleCurrentChange() {
      this.fetchData();
    },
    toggleModal(flag) {
      this.selected = flag;
      if (flag == true) {
        this.formData.orderBy = "acceptCount";
      } else {
        this.formData.orderBy = "lastLoginTime";
      }
      this.fetchData();
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
    onSubmit() {
      this.fetchData();
    },
    fetchData: function () {
      api.getUserList(this.formData).then((response) => {
        if (response.data.code == "200") {
          this.tableData = response.data.data.records;
          this.total = response.data.data.total;
        }
      });
    },
  },
};
</script>

<style scoped>
.selectedClass {
  color: #000;
}

#appMemberInfo {
  margin: 0 auto;
  width: 1200px;
}

.container {
  text-align: center;
  width: 1200px;
  padding-top: 30px;
  /* padding-left: 30px; */
}

.last {
  padding-top: 30px;
  padding-bottom: 20px;
  width: 100%;
  display: grid;
  justify-items: end;
}
</style>
