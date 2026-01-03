<template>
  <div id="app1">
    <div class="content" style="border: 1px solid #eee; border-radius: 13px">
      <el-container>
        <el-table
          @sort-change="handleSortChange"
          :data="tableData"
          height="200"
          style="border-radius: 20px"
          size="small">
          <!-- <el-table-column width="50" align="center">
            <template slot="default" slot-scope="scope">
              <i class="el-icon-rank drag-handle" :key="scope.$index"></i>
            </template>
          </el-table-column> -->
          <el-table-column
            prop="competitionName"
            align="center"
            label="竞赛名称">
          </el-table-column>
          <el-table-column prop="trackName" align="center" label="赛道名称">
          </el-table-column>
          <el-table-column
            prop="awardLevel"
            sortable
            align="center"
            label="获奖级别">
          </el-table-column>
          <el-table-column
            prop="awardTime"
            sortable
            align="center"
            label="获奖时间">
          </el-table-column>
        </el-table>
      </el-container>
    </div>
  </div>
</template>

<script>
import api from "@/common/api";
// import Sortable from "sortablejs";
import "element-ui/lib/theme-chalk/index.css";

export default {
  props: {
    userInfo: {
      type: Object,
    },
  },
  data: function () {
    return {
      tableData: [],
      formData: {
        orderBy: null,
        sortOrder: null,
      },
    };
  },
  updated() {
    console.log(this.tableData);
  },
  created: function () {
    this.fetchData();
  },
  mounted() {
    // this.rowDrop();
  },
  computed: {},
  methods: {
    fetchData: function () {
      api.getAwardInfoByUserId(this.userInfo.number).then((response) => {
        this.tableData = response.data.data;
      });
    },
    awardLevelSort(a, b) {
      const levelList = [
        "国家级一等奖",
        "国家级二等奖",
        "国家级三等奖",
        "国家级优秀奖",
        "省级一等奖",
        "省级二等奖",
        "省级三等奖",
      ];
      return levelList.indexOf(b.awardLevel) - levelList.indexOf(a.awardLevel);
    },
    handleSortChange({ prop, order }) {
      this.formData.orderBy = prop;
      this.formData.sortOrder = order;
      this.fetchData();
    },
    // rowDrop() {
    //   const tbody = document.querySelector(
    //     ".draggable-table  .el-table__body-wrapper tbody"
    //   );
    //   Sortable.create(tbody, {
    //     animation: 150,
    //     // chosenClass: "chosen",
    //     ghostClass: "sortable-ghost", //拖拽样式
    //     handle: ".drag-handle",
    //     onEnd: ({ newIndex, oldIndex }) => {
    //       var temp = this.tableData[oldIndex];
    //       var i;
    //       for (i = oldIndex + 1; i < this.tableData.length; i++) {
    //         this.tableData[i - 1] = this.tableData[i];
    //       }
    //       for (i = this.tableData.length - 2; i >= newIndex; i--) {
    //         this.tableData[i + 1] = this.tableData[i];
    //       }
    //       this.tableData[newIndex] = temp;
    //       console.log("Updated tableData:", this.tableData, newIndex, oldIndex); // 添加日志
    //     },
    //   });
    // },
  },
};
</script>

<style scoped>
.drag-handle {
  cursor: move;
}



#app1 {
  /* margin: auto; */
  /* padding-top: 20px; */
  /* background-color: red; */
}
</style>
