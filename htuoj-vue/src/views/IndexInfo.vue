<template>
  <div class="index-info">
    <div>
      <div>
        <div class="animated-text">
          <!-- 因侯炳旭拖欠HTUOJ的运营维护费用，现HTUOJ的所有权归高新想所有，不受任何单位和个人限制，欠款仍在追讨中 -->
        </div>
        <div style="display: flex; justify-content: center">
          <div>
            <BloggerList />
          </div>
          <div style="margin-left: 20px">
            <DailyTaskDate />
            <ContestInfo />
            <DailyQuests />
          </div>
        </div>
      </div>
      <el-footer style="text-align: center"
        >© 2024-2025 HTUOJ 版权所有 |
        <a
          style="text-decoration: none"
          href="https://beian.miit.gov.cn/"
          target="_blank"
          >吉ICP备2024023192号</a
        >
        <div
          style="
            width: 260px;
            text-align: center;
            margin: 0 auto;
            display: flex;
            align-items: center;
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
        </div>
      </el-footer>
    </div>
  </div>
</template>

<script>
import BloggerList from "@/components/Forum/BloggerList.vue";
import DailyQuests from "@/components/DailyQuests.vue";
import DailyTaskDate from "@/components/DailyTask/DailyTaskDate.vue";
import ContestInfo from "@/components/contest/ContestInfo.vue";

export default {
  name: "IndexInfo",
  components: { BloggerList, DailyQuests, DailyTaskDate, ContestInfo },
};
</script>

<style scoped>
.index-info {
  margin-top: 20px;
  /* height: 1500px; */
  background-color: #fff;
  display: flex;
  justify-content: center;
}
.animated-text {
  margin-top: 20px;
  text-align: center;
  font-family: Source Han Sans CN;
  font-size: 22px;
  font-style: normal;
  color: #000100;
  font-weight: 700;
  line-height: normal;
  background: linear-gradient(
    to right,
    rgb(255, 173, 97),
    rgb(255, 92, 201),
    rgb(168, 255, 202),
    rgb(98, 121, 234),
    rgb(255, 245, 102),
    rgb(255, 133, 122),
    rgb(102, 255, 230)
  );
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: hue 10s linear infinite;
}
</style>
