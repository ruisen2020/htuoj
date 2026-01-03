<template>
  <div>
    <el-card
      style="width: 300px; border-radius: 13px"
      class="card"
      shadow="hover">
      <div>
        <div class="card-text">
          <span style="font-size: 18px; font-weight: bold">排行榜</span>
          <router-link
            to="/home/memberInfo"
            style="
              font-size: 12px;
              text-align: right;
              color: #409eff;
              text-decoration: none;
            ">
            查看全部
          </router-link>
        </div>
        <div class="card-table" style="width: 260px; height: 375px">
          <el-table size="mini" :data="tableData" :fit="false">
            <el-table-column
              width="50"
              height="30"
              prop="rank"
              label="排名"
              align="center">
              <template #default="scope">
                <div class="range-img" v-if="scope.$index + 1 === 1">
                  <el-image
                    :src="require('../assets/第一.png')"
                    :style="{ width: 20 + 'px', height: 20 + 'px' }" />
                </div>
                <div class="range-img" v-else-if="scope.$index + 1 === 2">
                  <el-image
                    :src="require('../assets/第二.png')"
                    :style="{ width: 20 + 'px', height: 20 + 'px' }" />
                </div>
                <div class="range-img" v-else-if="scope.$index + 1 === 3">
                  <el-image
                    :src="require('../assets/第三.png')"
                    :style="{ width: 20 + 'px', height: 20 + 'px' }" />
                </div>
                <div v-else>
                  {{ scope.$index + 1 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              width="50"
              prop="avatar"
              label="头像"
              align="center">
              <template #default="scope">
                <!--用户头像-->
                <div
                  style="
                    text-align: center;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  "
                  @click="UserInfo(scope.row.userId)">
                  <el-avatar
                    size="small"
                    :src="scope.row.avatar"
                    style="
                      cursor: pointer;
                      width: 30px;
                      height: 30px;
                      margin-top: 3px;
                      margin-bottom: 3px;
                    " />
                </div>
              </template>
            </el-table-column>
            <el-table-column
              width="100"
              prop="userName"
              label="姓名"
              align="center">
              <template #default="scope">
                <el-tooltip
                  v-show="true"
                  class="item"
                  effect="dark"
                  :content="scope.row.userName"
                  placement="bottom">
                  <div
                    class="description-text"
                    style="
                      width: 100%;

                      position: relative;

                      font-size: 14px;
                      white-space: nowrap !important;
                      overflow: hidden !important;
                      text-overflow: ellipsis !important;
                      color: #0000008c;
                    ">
                    <el-link
                      :href="'/home/stuInfo/' + scope.row.userId"
                      target="_blank"
                      :underline="false">
                      {{ scope.row.userName }}</el-link
                    >
                  </div>
                </el-tooltip>
                <!-- <router-link
                    style="text-decoration: none; color: #606266"
                    :to="'/home/stuInfo/' + scope.row.id">
                    {{ scope.row.userName }}
                  </router-link> -->
              </template></el-table-column
            >
            <el-table-column
              width="60"
              prop="acceptCount"
              label="总题数"
              align="center"></el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
// import router from "../../public/router";
import api from "@/common/api";

export default {
  name: "DailyQuests",
  data() {
    return {
      tableData: null,
      formData: {
        current: 1,
        size: 10,
      },
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData: function () {
      api.getUserTopList(this.formData).then((response) => {
        if (response.data.code == "200") {
          this.tableData = response.data.data.records;
        }
      });
    },
    UserInfo(userId) {
      window.open("/home/stuInfo/" + userId, "_blank");
    },
    //后台交互方法
    load() {},
  },
};
</script>

<style scoped>
.card {
  margin-top: 23px;
}

.card-text {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
}

::v-deep .el-table--mini .el-table__cell {
  padding: 0 0;
}

.range-img {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-table-column {
  width: 600px;
}

::v-deep .el-table__header {
  width: 260px;
}
</style>
