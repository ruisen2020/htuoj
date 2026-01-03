<template>
<div class="contest-detail">
    <div>
        <div class="animated-text">{{ contest.title }}</div>
        <div style="margin-top: 20px;">
            <el-card style="width: 1200px;">
                <el-tabs v-model="activeName" type="card" style="width: 1200px; font-size: 18px" @click="handleClick" default-active-name="first">
                    <el-tab-pane label="详情" name="first">
                        <div>
                            <div v-if="checkState(contest) == 0">
                                <div style="text-align: center;">
                                    距比赛开始还有
                                </div>
                                <div class="count-down-main">
                                    <div class="count-down-item">
                                        <div class="count-down-num">{{ contest.days }}</div>
                                        <div class="count-down-label">天</div>
                                    </div>
                                    <div class="count-down-item">
                                        <div class="count-down-num">{{ contest.hours }}</div>
                                        <div class="count-down-label">小时</div>
                                    </div>
                                    <div class="count-down-item">
                                        <div class="count-down-num">{{ contest.minutes }}</div>
                                        <div class="count-down-label">分钟</div>
                                    </div>
                                    <div class="count-down-item">
                                        <div class="count-down-num">{{ contest.seconds }}</div>
                                        <div class="count-down-label">秒</div>
                                    </div>
                                </div>

                            </div>
                            <div v-if="checkState(contest) !=2" style="width: 430px; margin: 0 auto;">
                                <el-button @click="handleRegister(contest)" v-if=" contest.isRegister == false" round style="width: 100%;margin-top: 20px;" type="primary">
                                    报名
                                </el-button>
                                <!-- <el-button v-else round size="medium" style="width: 100%;height: 100%;text-align: center;margin-top: 20px;">
                                    已报名
                                </el-button> -->
                            </div>
                            <div id="markdown" style="margin-top: 10px"></div>
                        </div>

                    </el-tab-pane>
                    <el-tab-pane label="题目" name="second">
                        <ContestProblemList v-if="activeName == 'second'" :contest=contest> </ContestProblemList>
                    </el-tab-pane>
                    <el-tab-pane label="提交" name="third">
                        <ContestSubmissionListVue v-if="activeName == 'third'" :contest=contest> </ContestSubmissionListVue>
                    </el-tab-pane>
                    <el-tab-pane label="排名" name="fourth">
                        <ContestRankList v-if="activeName == 'fourth'" :contest=contest> </ContestRankList>
                    </el-tab-pane>
                </el-tabs>
            </el-card>
        </div>
    </div>
</div>
</template>

<script>
import api from '@/common/api';
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import ContestProblemList from '@/components/contests/ContestProbelmList.vue';
import ContestSubmissionListVue from '@/components/contests/ContestSubmissionList.vue';
import ContestRankList from '@/components/contests/ContestRankList.vue';

export default {
    components: {
        ContestProblemList,
        ContestSubmissionListVue,
        ContestRankList
    },
    data() {
        return {
            markdownCherry: null,
            contestId: this.$route.params.contestId,
            activeName: 'first',
            contest: null,
        }
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
        handleRegister(contest) {
            if (contest.isRegister) {
                console.log('已报名');
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
        handleClick(tab) {
            this.activeName = tab.name;
            console.log(tab.name);

        },
        startCountdown(contest) {
            if (contest.timer) {
                clearInterval(contest.timer);
                return;
            }
            contest.countDown = new Date(contest.startTime).getTime() - new Date().getTime();

            this.formatCountdown(contest.countDown)
            contest.timer = setInterval(() => {
                contest.countDown -= 1000;
                this.formatCountdown(contest.countDown)
                // console.log(contest.countDown);
                this.$forceUpdate(); // 强制页面刷新
                // console.log(this.contest.days, this.contest.hours, this.contest.minutes, this.contest.seconds);
                if (contest.countDown <= 0) {
                    this.$set(this.contest, 'days', '00');
                    this.$set(this.contest, 'hours', '00');
                    this.$set(this.contest, 'minutes', '00');
                    this.$set(this.contest, 'seconds', '00');
                    clearInterval(contest.timer);
                    contest.timer = null;
                    // console.log('比赛已开始');
                }
            }, 1000);
        },

        formatCountdown(milliseconds) {
            const totalSeconds = Math.floor(milliseconds / 1000);
            this.$set(this.contest, 'days', String(Math.floor(totalSeconds / (3600 * 24))).padStart(2, '0'));
            this.$set(this.contest, 'hours', String(Math.floor((totalSeconds % (3600 * 24)) / 3600)).padStart(2, '0'));
            this.$set(this.contest, 'minutes', String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, '0'));
            this.$set(this.contest, 'seconds', String(totalSeconds % 60).padStart(2, '0'));

        },
        getContestById() {
            api.getContestById({
                contestId: this.contestId
            }).then(res => {
                if (res.data.code === '200') {
                    this.contest = res.data.data;
                    this.contest.days = "00";
                    this.contest.hours = "00";
                    this.contest.minutes = "00";
                    this.contest.seconds = "00";
                    this.startCountdown(this.contest);
                    this.$nextTick(() => {
                        if (this.markdownCherry) {
                            return;
                        }
                        this.markdownCherry = new Cherry({
                            id: "markdown",
                            value: this.contest.description,
                            themeSettings: {
                                codeBlockTheme: "one dark",
                            },
                            editor: {
                                defaultModel: "previewOnly",
                            },
                        });
                    });
                }

            })
        }
    },

    mounted() {
        this.getContestById();
    }
}
</script>

<style scoped>
::v-deep .cherry-previewer {
    border: none;
    padding: 10px;
}

::v-deep .cherry-drag {
    width: 5px;
    background-color: #ebedee;
}

::v-deep .cherry {
    box-shadow: none;
}

::v-deep .cherry-markdown a.anchor:before {
    content: "" !important;
}

.count-down-num {
    font-size: 60px;
    margin-bottom: 12px;
}

.count-down-main {
    margin-top: 20px;
    margin-left: 10px;
    text-align: center;
    letter-spacing: -4px;
}

.count-down-item {
    min-width: 90px;
    text-align: center;
    letter-spacing: 0;
    display: inline-block;
    margin-right: 10px;
    padding: 10px 5px;
    border-radius: 16px;
    background: #c6c6c6;
    color: #fff;
    font-size: 18px;
    line-height: 1.1;
}

.animated-text {
    margin-top: 20px;
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

.contest-detail {
    margin: 0 auto;
    width: 1200px;
}

::v-deep .el-card {
    border-radius: 13px;
    /* box-shadow: none; */
    border: none;
}

::v-deep .el-card__body {
    border-radius: 13px;
}

::v-deep .el-card .is-always-shadow {
    box-shadow: none;
}

::v-deep .el-tabs__item {
    color: #262626bf;
    border-radius: 8px;
    /* //margin-left: 10px; border: 0 solid; */
}

::v-deep .el-tabs--card>.el-tabs__header .el-tabs__item {
    font-size: 16px;
    border-left: none;
}

::v-deep .el-tabs--card>.el-tabs__header {
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
