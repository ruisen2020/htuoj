<template>
  <div>
    <div id="app" ref="chart">
      <v-chart class="rating" :option="RantingOption"></v-chart>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
// import api from "@/common/api";
export default {
  props: {
    userPreferencesInfo: {
      type: Object,
    },
  },

  comments: {},

  mounted() {
  
   
    this.RantingOption.series[0].data[0] =
      this.userPreferencesInfo.codeforcesRating;
    this.RantingOption.series[0].data[1] =
      this.userPreferencesInfo.nowcoderRating;
    this.RantingOption.series[0].data[2] =
      this.userPreferencesInfo.leetcodeRating;
    this.RantingOption.series[0].data[3] =
      this.userPreferencesInfo.acwingRating;
    this.RantingOption.series[0].data[4] = this.userPreferencesInfo.luoguRating;
    this.RantingOption.series[0].data[5] =
      this.userPreferencesInfo.atcoderRating;
    this.updateChart();
  },
  methods: {
    updateChart() {
      // 初始化绘制图表的echarts实例
      const trendcharts = echarts.init(this.$refs.chart);
      trendcharts.clear();
      // 对实例对象进行配置
      trendcharts.setOption(this.RantingOption, true);
    },
  },
  data() {
    return {
      RantingOption: {
        title: {
          text: "Rating",
        },
        xAxis: {
          type: "category",
          data: ["CF", "牛客", "力扣", "AcW", "洛谷", "AtC"],
          size: 10,
        },
        yAxis: {
          interval: 400,
          type: "value",
          size: 20,
        },
        grid: {
          left: "15%",
          // right: "10%",
          top: "20%",
          bottom: "10%",
        },
        series: [
          {
            itemStyle: {
              // '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#9a60b4', '#ea7ccc'
              // 柱子的背景颜色
              color: function (params) {
                // 根据索引动态设置颜色
                switch (params.dataIndex) {
                  case 0:
                    return "#5470c6";
                  case 1:
                    return "#91cc75";
                  case 2:
                    return "#fac858";
                  case 3:
                    return "#ee6666";
                  case 4:
                    return "#73c0de";
                  default:
                    return "#3ba272";
                }
              },
              // 柱子的 border-radius
              barBorderRadius: [5, 5, 0, 0],
            },
            // center: ['50%', '60%'],
            data: [1600, 1200, 1500, 1800, 1700, 1900],
            type: "bar",

            label: {
              // 显示标签
              show: true,
              // 标签位置，可以设置为 'inside'、'top'、'bottom' 等
              position: "top",
              // 调整 offset 数值来控制标签的位置
              // offset: [0, 5],
              textStyle: {
                color: "#A9A9A9", // 设置文字颜色为白色
              },
            },
          },
        ],
      },
    };
  },
};
</script>
<style scoped>
#app {
  width: 280px;
  height: 180px;
  border-radius: 8px;
  background-color: rgb(255 255 255);
}
</style>