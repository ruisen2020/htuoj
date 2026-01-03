<template>
  <div id="appAwardInfo">
    <div class="container">
      <el-form :inline="true" class="demo-form-inline">
        <el-col :span="100"></el-col>
        <div class="d1" style="display: block">
          <el-form-item label="姓名: " style="padding-left: 28px">
            <el-input
              v-model="formData.studentName"
              placeholder="请输入姓名"></el-input>
          </el-form-item>

          <!-- <el-form-item label="学院: " style="padding-left: 28px">
            <el-input
              v-model="formData.studentCollege"
              placeholder="请输入学院"></el-input>
          </el-form-item> -->
          <el-form-item label="竞赛: ">
            <el-select
              v-model="formData.competitionName"
              placeholder="请选择竞赛">
              <el-option
                v-for="item in competitionList"
                :key="item"
                :label="item"
                :value="item"></el-option>
            </el-select>
            <!-- <el-input
              v-model="formData.competitionName"
              placeholder="请选择竞赛"></el-input> -->
          </el-form-item>
          <el-form-item label="科目: ">
            <el-select v-model="formData.trackName" placeholder="请选择科目">
              <el-option
                v-for="item in trackList"
                :key="item"
                :label="item"
                :value="item"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div class="d2">
          <el-form-item label="等级: ">
            <el-select
              multiple
              collapse-tags
              v-model="formData.awardLevel"
              placeholder="请输入获奖等级">
              <el-option
                v-for="item in awardLevel"
                :key="item.label"
                :label="item.label"
                :value="item.value"></el-option>
            </el-select>
            <!-- <el-col>
              <el-autocomplete
                class="inline-input"
                v-model="formData.awardLevel"
                :fetch-suggestions="querySearch"
                placeholder="请输入获奖等级"
                @select="handleSelect"></el-autocomplete>
            </el-col> -->
            <!--            <el-input v-model="formInline.user" placeholder="审批人"></el-input>-->
          </el-form-item>
          <el-form-item label="时间: ">
            <el-input
              v-model="formData.awardTime"
              placeholder="请输入获奖时间"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="restForm">重置</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <div class="content">
      <el-container style="border: 1px solid #eee; border-radius: 20px">
        <el-main>
          <el-table stripe @sort-change="handleSortChange" :data="tableData">
            <el-table-column prop="studentName" align="center" label="姓名">
              <template slot-scope="scope">
                <span v-if="!checkHasData(scope.row)">
                  {{ scope.row.studentName }}
                </span>
                <el-link
                  v-else
                  target="_blank"
                  :underline="false"
                  :href="generateLink(scope.row)">
                  {{ scope.row.studentName }}
                </el-link>
              </template>
            </el-table-column>
            <!-- <el-table-column prop="studentNumber" align="center" label="学号">
            </el-table-column> -->
            <!-- <el-table-column prop="studentCollege" align="center" label="学院">
            </el-table-column> -->
            <el-table-column
              prop="competitionName"
              align="center"
              label="竞赛名称">
            </el-table-column>
            <el-table-column
              width="200"
              prop="trackName"
              align="center"
              label="科目名称">
            </el-table-column>
            <el-table-column
              prop="awardLevel"
              sortable
              align="center"
              label="获奖级别">
              <template slot-scope="scope">
                <span>{{ awards[scope.row.awardLevel - 1] }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="awardTime"
              sortable
              align="center"
              label="获奖时间">
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </div>

    <div class="last">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page.sync="formData.current"
        :page-size="20"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
      <!--        <el-pagination-->

      <!--                background-->
      <!--                layout="prev, pager, next"-->
      <!--                :total="1000">-->
      <!--        </el-pagination>-->
    </div>
    <el-footer style="text-align: center"
      >© 2024 HTUOJ 版权所有 |
      <a
        style="text-decoration: none"
        href="https://beian.miit.gov.cn/"
        target="_blank"
        >吉ICP备2024023192号</a
      >
      <span
        style="
          display: inline-block;
          vertical-align: middle;
          align-items: center;
          justify-content: center;
        ">
        <img
          src="@/assets/备案图标.png"
          style="width: 16px; height: 16px; margin-left: 10px" />
        <a
          style="text-decoration: none; margin-left: 5px"
          href="https://beian.mps.gov.cn/#/query/webSearch?code=22017302000468"
          rel="noreferrer"
          target="_blank"
          >吉公网安备22017302000468号</a
        >
      </span>
    </el-footer>
  </div>
</template>

<script>
// import axios from "axios";
import "element-ui/lib/theme-chalk/index.css";
import api from "@/common/api";
export default {
  data: function () {
    return {
      total: 0,
      tableData: [],
      trackList: [],
      competitionList: [],
      awardLevel: [
        { label: "国家级一等奖", value: "1" },
        { label: "国家级二等奖", value: "2" },
        { label: "国家级三等奖", value: "3" },
        { label: "国家级优秀奖", value: "4" },
        { label: "省级一等奖", value: "5" },
        { label: "省级二等奖", value: "6" },
        { label: "省级三等奖", value: "7" },
        { label: "省级优秀奖", value: "8" },
      ],
      awards: [
        "国家级一等奖",
        "国家级二等奖",
        "国家级三等奖",
        "国家级优秀奖",
        "省级一等奖",
        "省级二等奖",
        "省级三等奖",
        "省级优秀奖",
      ],
      formData: {
        current: 1,
        size: 20,
        orderBy: "awardLevel",
        sortOrder: "ascending",
        studentName: null,
        studentNumber: null,
        studentCollege: null,
        competitionName: null,
        trackName: null,
        awardLevel: null,
        awardTime: null,
      },
    };
  },
  created: function () {
    this.fetchData();
  },
  computed: {},

  methods: {
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
        { value: "国家级一等奖" },
        { value: "国家级二等奖" },
        { value: "国家级三等奖" },
        { value: "国家级优秀奖" },
        { value: "省级一等奖" },
        { value: "省级二等奖" },
        { value: "省级三等奖" },
      ];
    },
    handleSelect(item) {
      console.log(item);
    },
    fetchData: function () {
      api.getTrackList().then((response) => {
        if (response.data.code == 200) {
          this.trackList = response.data.data;
        }
      });
      api.getCompetitionList().then((response) => {
        if (response.data.code == 200) {
          this.competitionList = response.data.data;
        }
      });
      api.getAwardInfoList(this.formData).then((response) => {
        if (response.data.code == 200) {
          this.tableData = response.data.data.records;
          this.total = response.data.data.total;
        }
      });
    },
    // awardLevelSort(a, b) {
    //   const levelList = [
    //     "国家级一等奖",
    //     "国家级二等奖",
    //     "国家级三等奖",
    //     "国家级优秀奖",
    //     "省级一等奖",
    //     "省级二等奖",
    //     "省级三等奖",
    //   ];
    //   // 翻转链表
    //   // levelList.reverse();
    //   return levelList.indexOf(b.awardLevel) - levelList.indexOf(a.awardLevel);
    // },
    generateLink(row) {
      // 根据实际情况生成链接地址
      return "/home/stuInfo/" + row.userId;
    },
    checkHasData(row) {
      // 这里假设根据某个条件判断是否有数据来决定是否生成超链接
      // 例如判断 row 的某个属性是否有特定值
      if (row.userId == null) return false;
      return true;
    },
    handleSortChange({ prop, order }) {
      this.formData.orderBy = prop;
      this.formData.sortOrder = order;
      this.fetchData();
    },
    onSubmit() {
      this.fetchData();
    },
    restForm() {
      return (this.formData = {
        current: 1,
        size: 20,
        orderBy: "awardLevel",
        sortOrder: "ascending",
        studentName: null,
        studentNumber: null,
        studentCollege: null,
        competitionName: null,
        trackName: null,
        awardLevel: null,
        awardTime: null,
      });
    },
    // 分页
    handleCurrentChange() {
      this.fetchData();
    },
  },
  mounted() {
    this.restaurants = this.loadAll();
  },
};
</script>

<style scoped>
.demo-form-inline {
  width: 100%;
}
/* .el-form-item{
  width: 100px;
} */
.container {
  width: 1200px;
  display: flex;
  /* width: 50%; */
  /* margin-left: 30px; */
  text-align: center;
  /* display: flex;
  flex-wrap: wrap; */
  /* white-space: nowrap; */
}

.el-form-item {
  display: inline-block;
}
.content {
  width: 100%;
  height: 100%;
}

.last {
  padding-top: 30px;
  padding-bottom: 20px;
  width: 100%;
  display: grid;
  justify-items: end;
}
#appAwardInfo {
  width: 1200px;
  margin: auto;
  padding-top: 20px;
  /* background-color: #f7f8fa; */
}
</style>
