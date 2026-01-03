<template>
  <div>
    <div id="app">
      <div class="content">
        <!-- <el-empty
          v-if="tableData === null || tableData.length === 0"
          description="暂未提交过题目"
        ></el-empty> -->
        <el-container>
          <el-table
            :data="tableData"
            size="small"
            @filter-change="handleFilterChange">
            <el-table-column
              width="60"
              prop="submissionId"
              align="center"
              label="编号">
            </el-table-column>
            <el-table-column
              width="150"
              prop="problemTitle"
              align="center"
              label="题目">
              <template slot="header" slot-scope="scope">
                题目
                <el-input
                  style="width: 130px"
                  v-model="formData.search"
                  size="mini"
                  placeholder="输入题目或者编号"
                  @change="handleSearch" />
                {{ scope.row }}
              </template>
              <template slot-scope="scope">
                <!-- <router-link
                    style="text-decoration: none; color: #606266"
                    :to="'/home/stuInfo/' + scope.row.id">
                    {{ scope.row.userName }}
                  </router-link> -->
                <el-link
                  :href="'/problem/' + scope.row.problemId"
                  target="_blank"
                  :underline="false">
                  {{ scope.row.problemId }}.{{
                    scope.row.problemTitle
                  }}</el-link
                >
              </template>
            </el-table-column>
            <el-table-column
              prop="status"
              align="center"
              label="运行结果"
              :column-key="one"
              :filter-multiple="false"
              :filters="filtersResult"
              width="150">
              <template slot-scope="scope">
                <el-tag
                  :type="ResultTypeList[scope.row.status]"
                  disable-transitions
                  size="mini"
                  >{{ ResultList[scope.row.status] }}</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column prop="time" align="center" label="运行时间">
              <template slot-scope="scope">
                {{ formTime(scope.row.time) }}
              </template>
            </el-table-column>
            <el-table-column prop="memory" align="center" label="使用内存">
              <template slot-scope="scope">
                {{ formMemory(scope.row.memory) }}
              </template>
            </el-table-column>
            <el-table-column
              width="80"
              prop="language"
              align="center"
              label="语言"
              :column-key="two"
              :filters="filtersLanguage"
              :filter-multiple="false">
              <template slot-scope="scope">
                <el-tag disable-transitions size="mini">{{
                  LanguageList[scope.row.language]
                }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              width="150"
              prop="createTime"
              align="center"
              label="提交时间"
              :formatter="dateFormat">
            </el-table-column>
          </el-table>
        </el-container>
      </div>

      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="formData.current"
        background
        :page-size="15"
        layout="prev, pager, next"
        :total="total"
        style="margin-top: 20px; margin-bottom: 20px"
        v-show="total > 0">
      </el-pagination>
    </div>
  </div>
</template>
<script>
import api from "@/common/api";
import moment from "moment";
export default {
  props: {
    userId: {
      type: String,
    },
  },
  data: function () {
    return {
      total: 0,
      tableData: null,
      filtersResult: [
        { text: "Pending", value: 0 },
        { text: "Accepted", value: 1 },
        { text: "Wrong Answer", value: 2 },
        { text: "Time Limit Exceeded", value: 3 },
        { text: "Memory Limit Exceeded", value: 4 },
        { text: "Runtime Error", value: 5 },
        { text: "Compile Error", value: 6 },
        { text: "Presentation Error", value: 7 },
        { text: "System Error", value: 8 },
        { text: "Nonzero Exit Status", value: 9 },
        { text: "Internal Error", value: 10 },
        { text: "File Error", value: 11 },
        { text: "Output Limit Exceeded", value: 12 },
        { text: "Signalled", value: 13 },
        { text: "No Status", value: 15 },
      ],
      ResultList: {
        0: "Pending",
        1: "Accepted",
        2: "Wrong Answer",
        3: "Time Limit Exceeded",
        4: "Memory Limit Exceeded",
        5: "Runtime Error",
        6: "Compile Error",
        7: "Presentation Error",
        8: "System Error",
        9: "Nonzero Exit Status",
        10: "Internal Error",
        11: "File Error",
        12: "Output Limit Exceeded",
        13: "Signalled",
        15: "No Status",
      },

      ResultTypeList: {
        0: "info",
        1: "success",
        2: "danger",
        3: "warning",
        4: "warning",
        5: "danger",
        6: "danger",
        7: "danger",
        8: "danger",
        9: "danger",
        10: "danger",
        11: "danger",
        12: "warning",
        13: "warning",
        15: "danger",
      },

      LanguageList: {
        0: "C",
        1: "C++",
        2: "Java",
        3: "Python",
      },
      filtersLanguage: [
        { text: "C", value: 0 },
        { text: "C++", value: 1 },
        { text: "Java", value: 2 },
        { text: "Python", value: 3 },
      ],
      formData: {
        current: 1,
        size: 15,
        userId: this.userId,
        search: null,
        language: null,
        status: null,
      },
      one: "one", //名字随意
      two: "two", //名字随意
    };
  },
  created: function () {
    this.fetchData();
  },
  computed: {},

  methods: {
    formTime(time) {
      if (time == null) {
        return "N/A";
      }
      if (time >= 1000) return (time / 1000).toFixed(2) + " s";
      return time + " ms";
    },
    formMemory(memory) {
      if (memory == null) {
        return "N/A";
      }
      if (memory >= 1024) {
        return (memory / 1024).toFixed(2) + " MB";
      }

      return memory + " KB";
    },
    handleCurrentChange() {
      this.fetchData();
    },
    handleSearch() {
      this.fetchData();
    },
    dateFormat(row, column) {
      var date = row[column.property];
      if (date === undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD  HH:mm:ss");
    },
    handleFilterChange(filters) {
      if (filters.one) {
        this.formData.status = filters.one[0];
      }
      if (filters.two) {
        console.log(filters.two[0]);

        this.formData.language = filters.two[0];
      }
      this.fetchData();
    },
    fetchData: function () {
      api.getSumissionList(this.formData).then((response) => {
        if (response.data.code === "200") {
          this.total = response.data.data.total;
          this.tableData = response.data.data.records;
        }
      });
    },
  },
};
</script>
<style scoped>
::v-deep .el-table-filter {
  background-color: #f0f0f0; /* 背景色 */
  color: #333; /* 文字颜色 */
  border: 10px solid #ccc; /* 边框颜色 */
  border-radius: 10px;
}
/* 使用深度选择器穿透样式作用域 */
::v-deep .el-table-filter {
  background-color: #f0f0f0; /* 背景色 */
  color: #333; /* 文字颜色 */
  border: 1px solid #ccc; /* 边框颜色 */
}

::v-deep .el-table-filter .el-table-filter__list {
  padding: 10px;
}

::v-deep .el-table-filter .el-table-filter__list-item {
  padding: 5px 10px;
  border-radius: 10px;
  cursor: pointer;
}

::v-deep .el-table-filter .el-table-filter__list-item:hover {
  background-color: #e0e0e0; /* 鼠标悬停时的背景色 */
}

::v-deep .el-table-filter .el-table-filter__list-item.is-selected {
  background-color: #d0d0d0; /* 选中项的背景色 */
}
.content {
  height: 100%;
}
</style>
