<template>
  <div id="appProblemInfo">
    <div class="container">
      <el-form :inline="true" class="demo-form-inline">
        <!-- 难度选择 -->
        <el-select
          v-model="formData.difficultyLevel"
          placeholder="难度"
          style="width: 100px">
          <el-option
            v-for="item in difficultyLevelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <!-- 状态选择 -->
        <el-select
          v-model="formData.status"
          placeholder="状态"
          style="width: 100px; margin-left: 10px">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <!-- 标签选择 -->
        <el-select
          v-model="formData.labels"
          placeholder="标签"
          style="width: 200px; margin-left: 10px"
          multiple
          filterable>
          <el-option
            v-for="item in labelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <!-- 来源选择 -->
        <el-select
          v-model="formData.sources"
          placeholder="来源"
          style="width: 200px; margin-left: 10px"
          multiple
          filterable>
          <el-option
            v-for="item in sourceOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>

        <el-input
          v-model="formData.search"
          placeholder="请输入编号或者标题"
          style="width: 200px; margin-left: 10px"></el-input>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" style="margin-left: 10px"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <div class="content">
      <el-container style="border: 1px solid #eee; border-radius: 20px">
        <el-main>
          <el-table :data="tableData" stripe>
            <el-table-column prop="status" align="center" label="状态">
              <template slot-scope="scope">
                <div
                  style="
                    text-align: center;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  ">
                  <span v-if="scope.row.status === null"></span>
                  <span
                    v-else-if="scope.row.status === 1"
                    style="
                      text-align: center;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    ">
                    <!-- <i class="el-icon-circle-check"></i> -->
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                      <path
                        fill-rule="evenodd"
                        clip-rule="evenodd"
                        d="M8 16a8 8 0 01-8-8 8 8 0 018-8 8 8 0 018 8 8 8 0 01-8 8zM5.552 7.948L7 9.397l3.948-3.949a.78.78 0 111.104 1.104l-4.5 4.5a.78.78 0 01-1.104 0l-2-2a.78.78 0 111.104-1.104z"
                        fill="#00AA54" />
                    </svg>
                  </span>
                  <span
                    v-else-if="scope.row.status === 2"
                    style="
                      text-align: center;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    ">
                    <!-- <i class="el-icon-circle-close"></i> -->
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                      <path
                        fill-rule="evenodd"
                        clip-rule="evenodd"
                        d="M0 8a8 8 0 008 8 8 8 0 008-8 8 8 0 00-8-8 8 8 0 00-8 8zm9.593-2.65a.75.75 0 111.06 1.061l-1.59 1.591 1.59 1.591a.75.75 0 11-1.06 1.06l-1.591-1.59-1.591 1.59a.75.75 0 11-1.06-1.06l1.59-1.591-1.59-1.591a.75.75 0 011.06-1.06l1.591 1.59 1.591-1.59z"
                        fill="#F04142" />
                    </svg>
                  </span>
                </div>
              </template>
              <!-- <i class="el-icon-circle-check"></i> -->

              <!-- <el-icon-circle-close></el-icon-circle-close>  -->
              <!-- </template> -->
            </el-table-column>
            <el-table-column prop="problemId" align="center" label="编号">
            </el-table-column>
            <el-table-column
              width="200"
              prop="title"
              align="center"
              label="标题">
              <template slot-scope="scope">
                <el-link
                  target="_blank"
                  :underline="false"
                  :href="generateLink(scope.row)">
                  {{ scope.row.title }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="souresList" align="center" label="来源">
              <template slot-scope="scope">
                <span
                  v-for="(item, index) in scope.row.souresList"
                  :key="index">
                  <el-tag
                    :key="index"
                    style="margin-right: 5px"
                    type="success"
                    size="mini">
                    {{ item }}
                  </el-tag></span
                >
              </template>
            </el-table-column>
            <el-table-column prop="labelList" align="center" label="算法标签">
              <!-- 遍历标签集合 -->
              <template slot-scope="scope">
                <span v-for="(item, index) in scope.row.labelList" :key="index">
                  <el-tag
                    :key="index"
                    style="margin-right: 5px"
                    type="primary"
                    size="mini">
                    {{ item }}
                  </el-tag></span
                >
              </template>
            </el-table-column>
            <el-table-column prop="passRate" align="center" label="通过率">
            </el-table-column>
            <el-table-column prop="difficultyLevel" align="center" label="难度">
              <template slot-scope="scope">
                <span
                  v-if="scope.row.difficultyLevel === 0"
                  style="color: #5cb85c">
                  简单
                </span>
                <span
                  v-else-if="scope.row.difficultyLevel === 1"
                  style="color: #f0ad4e">
                  中等
                </span>
                <span v-else style="color: #d9534f"> 困难 </span>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </div>
    <div class="last">
      <el-pagination
        background=""
        @current-change="handleCurrentChange"
        :current-page.sync="formData.current"
        :page-size="20"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
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
  <!-- <div class="last">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="formData.current"
        :page-size="20"
        layout="total, prev, pager, next"
        :total="total"
      >
      </el-pagination>
    </div> -->
</template>
<script>
import api from "@/common/api";
export default {
  data() {
    return {
      total: 0,
      tableData: [],
      formData: {
        current: 1,
        size: 20,
        difficultyLevel: null,
        status: null,
        labels: null,
        sources: null,
        search: null,
      },
      difficultyLevelOptions: [
        { value: "0", label: "简单" },
        { value: "1", label: "中等" },
        { value: "2", label: "困难" },
      ],

      statusOptions: [
        { value: "0", label: "未开始" },
        { value: "1", label: "已通过" },
        { value: "2", label: "尝试过" },
      ],
      labelOptions: [],
      sourceOptions: [],
    };
  },
  created() {
    this.statusList();
    this.sourceList();
    this.fetchData();
    // api
    //   .addSubmission({
    //     problemId: 1,
    //     code: "#include <iostream>\nusing namespace std;\nint main() {\nint a, b;\ncin >> a >> b;\ncout << a + b << endl;\n}",
    //     language: 0,
    //   })
    //   .then((response) => {
    //     console.log(response);
    //   })
    //   .catch((error) => {
    //     console.error("Error fetching data:", error);
    //   });
  },
  methods: {
    handleCurrentChange() {
      this.fetchData();
    },
    statusList() {
      api
        .getLabelAll()
        .then((response) => {
          this.labelOptions = response.data.data;
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },
    sourceList() {
      api
        .getSourceAll()
        .then((response) => {
          this.sourceOptions = response.data.data;
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },
    generateLink(row) {
      // 根据实际情况生成链接地址
      return "/problem/" + row.problemId;
    },
    onSubmit() {
      this.fetchData();
    },
    fetchData: function () {
      api
        .getProblemList(this.formData)
        .then((response) => {
          this.tableData = response.data.data.records;
          for (let i = 0; i < this.tableData.length; i++) {
            this.tableData[i].souresList = this.tableData[i].sources
              ? this.tableData[i].sources.split(",")
              : [];
            this.tableData[i].labelList = this.tableData[i].labels
              ? this.tableData[i].labels.split(",")
              : [];
          }
          this.total = response.data.data.total;
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },
  },
};
</script>

<style scoped>
#appProblemInfo {
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
