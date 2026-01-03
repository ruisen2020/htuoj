<template>
  <div>
    <div id="app">
      <div class="left">
        <div id="chartLeft" style="width: 270px; height: 180px"></div>
      </div>
      <el-divider direction="vertical">12121</el-divider>
      <div class="right">
        <div id="chartRight" style="width: 270px; height: 180px"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  props: {
    userPreferencesInfo: {
      type: Object,
    },
  },

  data() {
    return {
      acceptCount: 0,
      acceptOtherCount: 0,
    };
  },

  mounted() {
    this.acceptCount =
      this.userPreferencesInfo.acceptEasyCount +
      this.userPreferencesInfo.acceptMediumCount +
      this.userPreferencesInfo.acceptHardCount;

    this.acceptOtherCount =
      this.userPreferencesInfo.codeforcesAcceptCount +
      this.userPreferencesInfo.leetcodeAcceptCount +
      this.userPreferencesInfo.atcoderAcceptCount +
      this.userPreferencesInfo.luoguAcceptCount +
      this.userPreferencesInfo.nowcoderAcceptCount +
      this.userPreferencesInfo.acwingAcceptCount;
    this.initChart();
  },
  methods: {
    initChart() {
      const myChartLeft = echarts.init(document.getElementById("chartLeft"));
      const optionLeft = {
        title: {
          text: "本站OJ",
          subtext: "通过" + this.acceptCount + "道",
          left: "center",
        },
        tooltip: null,
        legend: {
          orient: "vertical",
          x: "left",
          y: "center",
          itemWidth: 20,
        },
        color: ["#5cb85c", "#f0ad4e", "#d9534f"],
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: ["40%", "70%"],
            center: ["50%", "60%"],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 20,
              borderColor: "#fff",
              borderWidth: 2,
            },
            label: {
              show: false,
              position: "center",
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              {
                value: this.userPreferencesInfo.acceptEasyCount,
                name: "简单\n" + this.userPreferencesInfo.acceptEasyCount,
              },
              {
                value: this.userPreferencesInfo.acceptMediumCount,
                name: "中等\n" + this.userPreferencesInfo.acceptMediumCount,
              },
              {
                value: this.userPreferencesInfo.acceptHardCount,
                name: "困难\n" + this.userPreferencesInfo.acceptHardCount,
              },
            ],
          },
        ],
      };
      myChartLeft.setOption(optionLeft);
      const myChartRight = echarts.init(document.getElementById("chartRight"));
      const optionRight = {
        title: {
          text: "其他OJ",
          left: "center",
          subtext: "通过" + this.acceptOtherCount + "道",
        },
        tooltip: null,
        legend: {
          // itemHeight: 6, // 修改icon图形大小
          itemWidth: 20,
          x: "left",
          y: "bottom",
          itemGap: 5, // 修改间距
          // show: false ,
          //
          // top: 'bottom',
          // left: 'center'
          orient: "vertical",
          // left: 'left',

          // left: 'center'
        },
        color: ["#5470c6", "#91cc75", "#fac858", "#73c0de", "#3ba272"],
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: ["40%", "70%"],
            center: ["50%", "60%"],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 20,
              borderColor: "#fff",
              borderWidth: 2,
            },
            label: {
              show: false,
              position: "center",
            },
            emphasis: {
              scale: false,
              label: {
                show: true,
                fontSize: 14,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              {
                value: this.userPreferencesInfo.codeforcesAcceptCount,
                name: "CF\n" + this.userPreferencesInfo.codeforcesAcceptCount,
              },
              {
                value: this.userPreferencesInfo.nowcoderAcceptCount,
                name: "牛客\n" + this.userPreferencesInfo.nowcoderAcceptCount,
              },
              {
                value: this.userPreferencesInfo.leetcodeAcceptCount,
                name: "力扣\n" + this.userPreferencesInfo.leetcodeAcceptCount,
              },
              {
                value: this.userPreferencesInfo.luoguAcceptCount,
                name: "洛谷\n" + this.userPreferencesInfo.luoguAcceptCount,
              },
              {
                value: this.userPreferencesInfo.atcoderAcceptCount,
                name: "AtC\n" + this.userPreferencesInfo.atcoderAcceptCount,
              },
            ],
          },
        ],
      };
      myChartRight.setOption(optionRight);
    },
  },
};
</script>
<style scoped>
#app {
  display: flex;
  border-radius: 8px;
  background-color: rgb(255 255 255);
}

#app > .el-divider--vertical {
  height: 180px;
}
</style>