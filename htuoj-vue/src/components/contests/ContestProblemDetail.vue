<template>
<div class="answering-info">
    <el-main>
        <div style="
            display: flex;
          
            justify-content: space-between;
          ">
            <div class="Left" style="width: 49.9vw; height: 95vh">
                <el-card style="height: 95vh; overflow: auto;    display: block;
    unicode-bidi: isolate; border-radius: 20px">
                    <ContestQuestion :count="count" :submissionInfoDisplayFlag="submissionInfoDisplayFlag" :submissionId="submissionId" @submissionId-event="submissionIdEvent" @submissionInfoDisplayFlag-event="submissionInfoDisplayFlagEvent
                                " />
                </el-card>
            </div>

            <div class="Right" style="width: 49.9vw;height: 95vh;">
                <div>
                    <el-card style="height: 67vh; border-radius: 20px;    display: block;
    unicode-bidi: isolate;">
                        <CodeMain :mode="mode" @code-change="codeChange" @mode-event="modeEvent" :code="code" />
                    </el-card>
                </div>
                <div>
                    <el-card style="
                  height: 28vh;
                  border-radius: 20px;
                  overflow: auto;
                  display: block;
                  unicode-bidi: isolate;
                ">
                        <TestOutPut :loading2="loading2" :Result="Result" @addTestSubmission="addTestSubmission" @add-submission="addSubmission" />
                    </el-card>
                </div>
            </div>
        </div>
    </el-main>
</div>
</template>

<script>
import ContestQuestion from "@/components/contests/ContestQuestion.vue";
import CodeMain from "@/components/Code/CodeMain.vue";
import TestOutPut from "@/components/problem/TestOutPut.vue";
// import Splitpane from "vue-splitpane";
import api from "@/common/api";
export default {
    name: "CodeAndQuestionInfo",
    components: {
        ContestQuestion,
        CodeMain,
        TestOutPut
    },
    data() {
        return {
            loading2: false,
            Result: null,
            submissionInfoDisplayFlag: false,
            submissionId: null,
            leftColumnSpan: 10,
            rightColumnSpan: 10,
            boxWidth: 0,
            leftWidth: 50,
            rightWidth: 50,
            code: "",
            pollingInterval: null,
            count: 0,
            mode: "c++",
            LanguageList: {
                0: "C",
                1: "C++",
                2: "Java",
                3: "Python",
            },
        };
    },
    mounted() {
        this.getLastSubmissionByProblemId();
    },
    methods: {
        addTestSubmission(sampleList) {
            this.loading2 = true;
            var language = 0;
            if (this.mode == "C") {
                language = 0;
            } else if (this.mode == "C++") {
                language = 1;
            } else if (this.mode == "Java") {
                language = 2;
            } else if (this.mode == "Python") {
                language = 3;
            }

            api
                .addTestSubmission({
                    problemId: this.$route.params.problemId,
                    sampleList: sampleList,
                    language: language,
                    code: this.code,
                })
                .then((response) => {
                    if (response.data.code === "200") {
                        this.loading2 = false;
                        this.Result = response.data.data.resultList;
                    }
                });
        },
        modeEvent(mode) {
            this.mode = mode;
        },
        getLastSubmissionByProblemId() {

            api.checkRegister(this.$route.params.contestId).then((response) => {
                if (response.data.code != 200) {
                    // this.$message.error("请先报名");
                    this.$router.push({
                        path: "/home/contestDetail/" + this.$route.params.contestId
                    });
                } else {
                    api
                        .getLastSubmissionByProblemId({
                            problemId: this.$route.params.problemId,
                        })
                        .then((response) => {
                            if (response.data.code === "200") {
                                this.code = response.data.data.code;
                                this.mode = this.LanguageList[response.data.data.language];
                            }
                        });
                }
            });

        },
        submissionIdEvent(submissionId) {
            this.submissionId = submissionId;
        },
        submissionInfoDisplayFlagEvent(flag) {
            this.submissionInfoDisplayFlag = flag;
        },
        addSubmission() {
            this.loading2 = true;
            var language = 0;
            if (this.mode == "C") {
                language = 0;
            } else if (this.mode == "C++") {
                language = 1;
            } else if (this.mode == "Java") {
                language = 2;
            } else if (this.mode == "Python") {
                language = 3;
            }

            this.submissionInfoDisplayFlag = false;
            api
                .addSubmission({
                    problemId: this.$route.params.problemId,
                    code: this.code,
                    language: language,
                    contestId: this.$route.params.contestId,
                })
                .then((response) => {
                    if (response.data.code === "200") {
                        this.submissionId = response.data.data.submissionId;
                    }
                })
                .catch((error) => {
                    console.error("Error fetching data:", error);
                });

            this.pollingInterval = setInterval(() => {
                this.submissionInfoDisplayFlag = false;
                this.count++;
                if (this.count > 10) {
                    this.count = 0;
                    this.loading2 = false;
                    this.$message.error("服务器繁忙,正在排队评测中～");
                    this.stopPolling();
                    return;
                }
                api
                    .getSubmissionById({
                        submissionId: this.submissionId
                    })
                    .then((response) => {
                        if (response.data.code === "200") {
                            var status = response.data.data.status;
                            if (status != null && status != 0) {
                                this.loading2 = false;
                                this.submissionInfoDisplayFlag = true;
                                this.stopPolling(); // 停止轮询
                            }
                        }
                    })
                    .catch((error) => {
                        console.error("Error fetching data:", error);
                    });
            }, 1000); // 每1秒执行一次
        },
        stopPolling() {
            if (this.pollingInterval) {
                clearInterval(this.pollingInterval);
                this.pollingInterval = null;
            }
        },
        codeChange(newCode) {
            this.code = newCode;
        },
    },
};
</script>

<style scoped>
.answering-info {
    width: 100vw;
    /* overflow: hidden; */
    /* padding: 0px; */
    height: 95vh;
    /* background-color: #f7f8fa; */
}

/* .Left,
        .Right {
          overflow: auto;
          height: 100%;
          transition: width 0.3s;
        } */

/* .Left {
          background-color: lightblue;
        }
        
        .Right {
          background-color: lightgreen;
        } */
</style>
