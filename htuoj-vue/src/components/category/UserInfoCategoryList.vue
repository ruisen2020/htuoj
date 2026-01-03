<template>
  <div>
    <div id="app1" style="border-radius: 30px">
      <div
        class="tabs"
        style="
          padding-top: 10px;
          padding-left: 10px;
          padding-right: 10px;
          border-radius: 30px;
        ">
        <el-tabs v-model="activeName" type="card" @click="handleClick">
          <el-tab-pane label="最近提交" name="first">
            <span
              slot="label"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              ">
              <i
                style="width: 30px; height: 20px; font-size: 20px"
                class="el-icon-time"></i>
              <!-- <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="30"
                height="20"
                fill="currentColor">
                <path
                  fill-rule="evenodd"
                  d="M20.995 4.824A3 3 0 0018 2H6l-.176.005A3 3 0 003 5v14l.005.176A3 3 0 006 22h12l.176-.005A3 3 0 0021 19V5l-.005-.176zM6 4h12l.117.007A1 1 0 0119 5v14l-.007.117A1 1 0 0118 20H6l-.117-.007A1 1 0 015 19V5l.007-.117A1 1 0 016 4zm5.718 9.304a1 1 0 01.063 1.321l-.085.093-2.062 2a1 1 0 01-1.3.08l-.093-.08-.937-.91A1 1 0 018.6 14.292l.095.082.241.234 1.367-1.325a1 1 0 011.414.022zM17 15a1 1 0 00-1-1h-2l-.117.007A1 1 0 0014 16h2l.117-.007A1 1 0 0017 15zm-5.282-7.696a1 1 0 01.063 1.321l-.085.093-2.062 2a1 1 0 01-1.3.08l-.093-.08-.937-.91A1 1 0 018.6 8.292l.095.082.241.234 1.367-1.325a1 1 0 011.414.022zM17 9a1 1 0 00-1-1h-2l-.117.007A1 1 0 0014 10h2l.117-.007A1 1 0 0017 9z"
                  clip-rule="evenodd"></path>
              </svg> -->

              最近提交
            </span>
            <SubmissionsInfo
              v-if="activeName == 'first'"
              :userId="userId"></SubmissionsInfo>
          </el-tab-pane>
          <el-tab-pane label="题单" name="second" style="width: 100px">
            <span
              slot="label"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              ">
              <i
                style="width: 30px; height: 20px; font-size: 20px"
                class="el-icon-tickets"></i>
              <!-- <svg
                width="30px"
                height="20px"
                aria-hidden="true"
                focusable="false"
                data-prefix="far"
                data-icon="file-lines"
                class="svg-inline--fa fa-file-lines absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2"
                role="img"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 384 512">
                <path
                  fill="currentColor"
                  d="M64 464c-8.8 0-16-7.2-16-16V64c0-8.8 7.2-16 16-16H224v80c0 17.7 14.3 32 32 32h80V448c0 8.8-7.2 16-16 16H64zM64 0C28.7 0 0 28.7 0 64V448c0 35.3 28.7 64 64 64H320c35.3 0 64-28.7 64-64V154.5c0-17-6.7-33.3-18.7-45.3L274.7 18.7C262.7 6.7 246.5 0 229.5 0H64zm56 256c-13.3 0-24 10.7-24 24s10.7 24 24 24H264c13.3 0 24-10.7 24-24s-10.7-24-24-24H120zm0 96c-13.3 0-24 10.7-24 24s10.7 24 24 24H264c13.3 0 24-10.7 24-24s-10.7-24-24-24H120z"></path>
              </svg> -->

              题单
            </span>
            <!-- <div v-if="activeName == second"> -->
            <TrainingList
              v-if="activeName == 'second'"
              :userId="userId"></TrainingList>
            <!-- </div> -->
          </el-tab-pane>
          <el-tab-pane label="讨论" name="fourth">
            <span
              slot="label"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              ">
              <i
                style="width: 30px; height: 20px; font-size: 20px"
                class="el-icon-chat-round">
              </i>
              <!-- <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="30px"
                height="20px"
                fill="currentColor">
                <path
                  fill-rule="evenodd"
                  d="M2 11.001a9.001 9.001 0 014.974-8.047A8.876 8.876 0 0110.998 2h.535c.018 0 .037 0 .055.002 3.934.218 7.204 3.02 8.15 6.753a1 1 0 01-1.94.49c-.734-2.9-3.27-5.065-6.294-5.245h-.51a6.876 6.876 0 00-3.12.74l-.004.002A7.001 7.001 0 004 11.003v.002a6.873 6.873 0 00.738 3.117c.206.407.271.871.185 1.32l-.387 2.022 2.022-.387c.448-.086.912-.021 1.32.185.44.222.9.395 1.373.518a1 1 0 11-.502 1.936 8.865 8.865 0 01-1.773-.669.067.067 0 00-.042-.006l-3.47.665a1 1 0 01-1.17-1.17l.665-3.47a.067.067 0 00-.006-.043A8.873 8.873 0 012 11.001zM17.004 20h-.005a3 3 0 01-2.68-1.658l-.004-.007A2.936 2.936 0 0114 17.004v-.206a2.995 2.995 0 012.773-2.797l.233-.001c.46-.001.917.107 1.33.315l.007.004A3 3 0 0120 17v.005c.001.425-.09.845-.268 1.232l-.133.29a1 1 0 00-.074.606l.093.485-.484-.093a1 1 0 00-.606.073l-.29.134a2.937 2.937 0 01-1.234.268zm-.296-8A4.995 4.995 0 0012 16.738v.262c-.002.777.18 1.543.53 2.237a5 5 0 006.542 2.313l2.303.441c.365.07.686-.25.616-.615l-.441-2.303a5 5 0 00-2.312-6.541A4.937 4.937 0 0017 12h-.292z"
                  clip-rule="evenodd"></path>
              </svg> -->
              讨论
            </span>
            <DiscussList
              v-if="activeName == 'fourth'"
              :userId="userId"></DiscussList>
          </el-tab-pane>
          <el-tab-pane label="分享" name="sixth">
            <span
              slot="label"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              ">
              <i
                style="width: 30px; height: 20px; font-size: 20px"
                class="el-icon-share"></i>
              分享
            </span>
            <ShareList
              v-if="activeName == 'sixth'"
              :userId="userId"></ShareList>
          </el-tab-pane>
          <el-tab-pane label="题解" name="third">
            <span
              slot="label"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              ">
              <i
                style="width: 30px; height: 20px; font-size: 20px"
                class="el-icon-document-checked">
              </i>
              <!-- <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="30px"
                height="20px"
                fill="currentColor">
                <path
                  fill-rule="evenodd"
                  d="M20.995 4.824A3 3 0 0018 2H6l-.176.005A3 3 0 003 5v14l.005.176A3 3 0 006 22h12l.176-.005A3 3 0 0021 19V5l-.005-.176zM6 4h12l.117.007A1 1 0 0119 5v14l-.007.117A1 1 0 0118 20H6l-.117-.007A1 1 0 015 19V5l.007-.117A1 1 0 016 4z"
                  clip-rule="evenodd"></path>
                <path
                  fill-rule="evenodd"
                  d="M10.763 12.827l-1.06-1.06a1 1 0 00-1.415 1.414l1.415 1.414a1.5 1.5 0 002.12 0l3.889-3.888a1 1 0 00-1.415-1.414l-3.534 3.534z"
                  clip-rule="evenodd"></path>
              </svg> -->

              题解
            </span>
            <ProblemSolveList
              v-if="activeName == 'third'"
              :userId="userId"></ProblemSolveList>
          </el-tab-pane>

          <el-tab-pane label="收藏" name="fifth">
            <span
              slot="label"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              ">
              <i
                style="width: 30px; height: 20px; font-size: 20px"
                class="el-icon-collection">
              </i>
              <!-- <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="30px"
                height="20px"
                fill="currentColor"
                class="css-1rhb60f-Svg ea8ky5j0">
                <path
                  fill-rule="evenodd"
                  d="M15.392 8.23l5.695.832a.942.942 0 01.521 1.607l-4.12 4.013.972 5.67a.942.942 0 01-1.367.993L12 18.667l-5.093 2.678a.942.942 0 01-1.367-.993l.972-5.67-4.12-4.013a.942.942 0 01.52-1.607l5.696-.833 2.547-5.16a.942.942 0 011.69 0l2.547 5.16zm-1.329 1.826L12 5.876l-2.063 4.18-4.615.675 3.34 3.252-.789 4.594L12 16.407l4.127 2.17-.788-4.594 3.34-3.252-4.616-.675z"
                  clip-rule="evenodd"></path>
              </svg> -->
              收藏
            </span>
            <CollectList
              v-if="activeName == 'fifth'"
              :userId="userId"></CollectList>
          </el-tab-pane>
        </el-tabs>
        <!-- <router-view></router-view> -->
      </div>
    </div>
  </div>
</template>
<script>
import SubmissionsInfo from "@/components/category/SubmissionsInfo.vue";
import TrainingList from "@/components/category/TrainingList.vue";
import ProblemSolveList from "./ProblemSolveList.vue";
import DiscussList from "./DiscussList.vue";
import CollectList from "./CollectList.vue";
import ShareList from "./ShareList.vue";
export default {
  components: {
    SubmissionsInfo,
    TrainingList,
    ProblemSolveList,
    DiscussList,
    CollectList,
    ShareList,
  },

  props: {
    userId: {
      type: String,
    },
  },
  data() {
    return {
      activeName: "first",
    };
  },
  methods: {
    handleClick(tab) {
      this.activeName = tab.name;
    },
  },
};
</script>
<style scoped>
::v-deep .el-tabs__item {
  color: #262626bf;
  border-radius: 8px;
  /* //margin-left: 10px; border: 0 solid; */
}

::v-deep .el-tabs--card > .el-tabs__header .el-tabs__item {
  border-left: none;
}

::v-deep .el-tabs--card > .el-tabs__header {
  border: none;
}

/**选中 */
::v-deep .el-tabs__item.is-active {
  background-color: #000a200d;
  border: none;
}

::v-deep .el-table--border .el-table__cell {
  border-right: none !important;
}

::v-deep .el-tabs__nav,
.is-top {
  border: none !important;
}
</style>
