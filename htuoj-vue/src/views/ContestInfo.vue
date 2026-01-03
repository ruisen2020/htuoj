<template>
  <div class="contest-info">
    <div class="animated-text">HTUOJ在线竞赛</div>
    <div style="width: 1200px;margin-top: 20px; display: flex;">
      <div>
        <div style="width: 800px;" v-for="(item, index) in contestList" :key="item.contestId">

          <el-card style="width: 800px;height: 160px;" shadow="hover"
            :style="index === 0 ? 'margin-top: 0px;' : 'margin-top: 20px;'">
            <div style="display: flex; align-items: center;">
              <div style="width: 80%;">
                <div style="font-size: 24px; color: #000100;line-height: normal;font-weight: 600;
              cursor: pointer;" @click="handleContestDetail(item)">
                  {{ item.title }}
                </div>
                <div style="font-size: 14px; color: #000100;line-height: normal;font-weight: 400;
            margin-top: 10px;">
                  <i class="el-icon-time"></i> 比赛时间：{{ formatDate(item.startTime) }} - {{ formatDate(item.endTime) }}
                  （{{ formatTime(item.duration) }}）
                </div>
                <div style="font-size: 14px; color: #000100;line-height: normal;font-weight: 400;
            margin-top: 10px;">
                  <i class="el-icon-user"></i> 参与人数：{{ item.registerSum }}
                </div>
                <div style="font-size: 14px; color: #000100;line-height: normal;font-weight: 400;
            margin-top: 10px;display: flex; align-items: center;">
                  <i class="el-icon-sunny" style="margin-right: 3px;"></i> 出题人：<span
                    v-for="(maker, index) in item.makerList" :key="index">
                    <el-link :href="'/home/stuInfo/' + maker" target="_blank" :underline="false"
                      style="margin-left: 5px;">{{ item.userNameList[index] }}</el-link>
                  </span>
                </div>
              </div>
              <div style="width: 20%;">
                <div v-if="checkState(item) !=2">
                  <el-button  round v-if="item.isRegister == false" type="primary" size="medium"
                  style="width: 100%;height: 100%;text-align: center;" @click="handleRegister(item)">
                  报名
                </el-button>
                <el-button round v-if="item.isRegister == true" size="medium"
                  style="width: 100%;height: 100%;text-align: center;" @click="handleRegister(item)">
                  已报名
                </el-button>
                </div>
               
                <div v-if="checkState(item) == 0"
                  style="font-size: 14px; color: #000100;line-height: normal;font-weight: 400; margin-top: 10px;">
                  <i class="el-icon-timer"></i> 距比赛 {{ item.countdownText }}
                </div>

                <div v-else-if="checkState(item) == 1"
                  style="font-size: 14px; color: #000100;line-height: normal;font-weight: 400; margin-top: 10px; text-align: center;">
                  比赛已开始
                </div>
                <div v-else
                  style="font-size: 14px; color: #000100;line-height: normal;font-weight: 400; margin-top: 10px; text-align: center;">
                  比赛已结束
                </div>
              </div>
            </div>

          </el-card>
        </div>

      </div>

      <el-card style="width: 300px;height: 600px;  margin-left: 20px;" shadow="hover">
        这里还没想好放什么
      </el-card>
    </div>
  </div>
</template>

<script>
import api from "@/common/api";
export default {
  data() {
    return {
      timer: null,
      contestList: null,
      formData: {
        current: 1,
        pageSize: 10,
        title: "",
      },
    }
  },
  mounted() {
    this.getContestList();
  },
  methods: {
    checkState(contest) {
      var nowTime = new Date().getTime();
      var startTime = new Date(contest.startTime).getTime();
      var endTime = new Date(contest.endTime).getTime();
      if (nowTime >= startTime && nowTime <= endTime) {
        return 1;
      } else if (nowTime < startTime) {
        return 0;
      } else {
        return 2;
      }

    },
    handleContestDetail(contest) {
      window.open('/home/contestDetail/' + contest.contestId, '_blank');
    },
    handleRegister(contest) {
      if (contest.isRegister) {
        this.handleContestDetail(contest);
      } else {
        api.handleRegister({
          contestId: contest.contestId
        }).then((response) => {
          if (response.data.code === "200") {
            contest.isRegister = true;
            contest.registerSum += 1;
          }
        });
      }

    },
    startCountdown(contest) {
      if (contest.timer) {
        clearInterval(contest.timer);
        return;
      }
      contest.countDown = new Date(contest.startTime).getTime() - new Date().getTime();
      // console.log(contest.countDown);

      contest.timer = setInterval(() => {
        contest.countDown -= 1000;
        contest.countdownText = this.formatCountdown(contest.countDown);
        // console.log(contest.countdownText);
        // console.log(contest.title + " " + contest.countdownText);

        if (contest.countDown <= 0) {
          clearInterval(contest.timer);
          contest.timer = null;
          contest.countdownText = "比赛已开始";
        }
      }, 1000);
    },

    formatCountdown(milliseconds) {
      const totalSeconds = Math.floor(milliseconds / 1000);
      const days = Math.floor(totalSeconds / (3600 * 24));
      const hours = Math.floor((totalSeconds % (3600 * 24)) / 3600);
      const minutes = Math.floor((totalSeconds % 3600) / 60);
      const seconds = totalSeconds % 60;

      return `${days}天${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    },
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
    formatTime(time) {
      const hours = time / 3600;
      return `${hours.toFixed(1)} h`;
    },
    getContestList() {
      api.getContestList(this.formData).then((response) => {
        if (response.data.code === "200") {
          this.contestList = response.data.data.records;
          for (let i = 0; i < this.contestList.length; i++) {
            this.$set(this.contestList[i], 'makerList', this.contestList[i].makers.split(","));
            this.$set(this.contestList[i], 'userNameList', this.contestList[i].usernames.split(","));
            this.$set(this.contestList[i], 'countdownText', '');
            this.startCountdown(this.contestList[i]);
          }
        }
      });
    }
  }
}
</script>

<style scoped>
.contest-info {
  /* display: flex; */
  /* justify-content: center; */
  margin: 0 auto;
  width: 1100px;
  /* align-items: center; */
}

.animated-text {
  text-align: center;
  font-family: Source Han Sans CN;
  font-size: 52px;
  font-style: normal;
  color: #000100;
  font-weight: 700;
  line-height: normal;
  background: linear-gradient(to right, rgb(255, 245, 102), rgb(168, 255, 202), rgb(255, 173, 97), rgb(255, 133, 122), rgb(255, 92, 201), rgb(98, 121, 234), rgb(102, 255, 230));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: hue 3s linear infinite;
}

@keyframes hue {
  from {
    filter: hue-rotate(0deg);
  }

  to {
    filter: hue-rotate(360deg);
  }
}

::v-deep .el-card {

  border-radius: 18px;

  /* padding: 32px; */
}
</style>
