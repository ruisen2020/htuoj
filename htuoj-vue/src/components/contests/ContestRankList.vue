<template>
<div>
    <div v-if="checkState(contest) == 0">
        <el-empty :image-size="200" description="比赛尚未开始，敬请等待"></el-empty>
    </div>
    <div v-else width="95%">
        <div style="font-size: 14px;margin-bottom: 10px;">PS: 如果同时选择了学校和省份，默认只按省份筛选，</div>
        <div style="margin-bottom: 20px;">
            <el-input v-model="formData.userName" placeholder="输入用户名回车搜索" @change="handleSearch" style="width: 240px;"></el-input>
            <el-select style="margin-left: 20px;width: 240px;" collapse-tags v-model="formData.schoolId" filterable multiple remote reserve-keyword placeholder="请选择学校" :remote-method="remoteMethod" :loading="loading" @change="handleSearch()">
                <el-option v-for="item in schoolOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
            <el-select style="margin-left: 20px;width: 240px;" @change="handleSearch()" v-model="formData.provinceId" collapse-tags placeholder="请选择省份" multiple>
                <el-option v-for="item in provinceList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
        </div>
        <el-table :data="rankList" stripe style="cursor: pointer;width: 96%; height: 100%;">
            <el-table-column prop="rank" align="center" label="排名" width="">
            </el-table-column>
            <el-table-column prop="avatar" align="center" label="头像" width="">
                <template #default="scope">
                    <div style="
                      text-align: center;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    ">
                        <el-avatar :fit="contain" :src="scope.row.avatar" shape="square" />
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="userName" align="center" label="用户名" width="">
                <template #default="scope">
                    <!-- <router-link
                    style="text-decoration: none; color: #606266"
                    :to="'/home/stuInfo/' + scope.row.id">
                    {{ scope.row.userName }}
                  </router-link> -->
                    <el-link :href="'/home/stuInfo/' + scope.row.userId" target="_blank" :underline="false">
                        {{ scope.row.userName }}</el-link>
                </template>
            </el-table-column>
            <el-table-column prop="schoolName" align="center" label="学校" width="">
            </el-table-column>
            <el-table-column prop="acceptSum" align="center" label="通过" width="">
            </el-table-column>
            <el-table-column prop="penaltyTimeSum" align="center" label="罚时" width="">
            </el-table-column>

            <el-table-column v-for="(column, index) in columns" :key="index" :prop="column.prop" :label="column.label" align="center" width="50">
                <template #default="scope">
                    <div @click="openCode(scope.row.passList[index])" style="width: 50px;height: 50px; text-align: center;display: flex;   
                        justify-content: center;
                        align-items: center;
                        cursor: pointer;
                        flex-direction: column;" :style="{ backgroundColor: getColor(scope.row.passList[index]) }">
                        <div style="  display: flex;
                        justify-content: center;
                        align-items: center; " v-if="scope.row.passList[index].isAccept != null && scope.row.passList[index].isAccept">

                            {{ scope.row.passList[index].penaltyTime }}
                        </div>
                        <div style="  display: flex;
                        justify-content: center;
                        align-items: center;
                       " v-if="scope.row.passList[index].waSum > 0">
                            (- {{ scope.row.passList[index].waSum }})
                        </div>
                    </div>

                </template>
            </el-table-column>

        </el-table>
    </div>
    <el-dialog title="" :visible.sync="isModalVisible" width="840px"  @open="initializeEditor" @close="handleDialogClose">
        <div class="modal">
            <div>
                <el-card>
                    <el-descriptions title="提交结果">
                        <el-descriptions-item label="运行结果">
                            <el-tag :type="ResultTypeList[submissionInfo.status]" size="mini">{{
              ResultList[submissionInfo.status]
            }}</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item label="运行时间">{{ formTime(submissionInfo.time) }}
                        </el-descriptions-item>
                        <el-descriptions-item label="运行空间">{{ formMemory(submissionInfo.memory) }}
                        </el-descriptions-item>
                        <el-descriptions-item label="提交时间">
                            {{ submissionInfo.createTime }}
                        </el-descriptions-item>
                    </el-descriptions>
                </el-card>
                <el-card style="margin-top: 20px">
                    <div style="font-size: 16px; color: #303133; font-weight: 700">
                        提交代码
                    </div>
                    <div id="codemarkdown"></div>
                </el-card>
            </div>
        </div>
    </el-dialog>

</div>
</template>

<script>
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import api from "@/common/api";
import moment from "moment";
// import SubmissionDetail from "../problem/SubmissionDetail.vue";
export default {
    components: {
        // SubmissionDetail
    },
    props: {
        contest: Object
    },
    data() {
        return {
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
            codemarkdown: null,
            LanguageList: {
                0: "C",
                1: "C++",
                2: "Java",
                3: "Python",
            },
            submissionId: null,
            submissionInfo: {},
            isModalVisible: false,
            schoolOptions: [],
            loading: false,
            formData: {
                contestId: this.contest.contestId,
                userName: "",
                schoolId: [],
                provinceId: [],
            },
            provinceList: [{
                    value: 1,
                    label: "北京市"
                },
                {
                    value: 2,
                    label: "天津市"
                },
                {
                    value: 3,
                    label: "河北省"
                },
                {
                    value: 4,
                    label: "山西省"
                },
                {
                    value: 5,
                    label: "内蒙古自治区"
                },
                {
                    value: 6,
                    label: "辽宁省"
                },
                {
                    value: 7,
                    label: "吉林省"
                },
                {
                    value: 8,
                    label: "黑龙江省"
                },
                {
                    value: 9,
                    label: "上海市"
                },
                {
                    value: 10,
                    label: "江苏省"
                },
                {
                    value: 11,
                    label: "浙江省"
                },
                {
                    value: 12,
                    label: "安徽省"
                },
                {
                    value: 13,
                    label: "福建省"
                },
                {
                    value: 14,
                    label: "江西省"
                },
                {
                    value: 15,
                    label: "山东省"
                },
                {
                    value: 16,
                    label: "河南省"
                },
                {
                    value: 17,
                    label: "湖北省"
                },
                {
                    value: 18,
                    label: "湖南省"
                },
                {
                    value: 19,
                    label: "广东省"
                },
                {
                    value: 20,
                    label: "广西壮族自治区"
                },
                {
                    value: 21,
                    label: "海南省"
                },
                {
                    value: 22,
                    label: "重庆市"
                },
                {
                    value: 23,
                    label: "四川省"
                },
                {
                    value: 24,
                    label: "贵州省"
                },
                {
                    value: 25,
                    label: "云南省"
                },
                {
                    value: 26,
                    label: "西藏自治区"
                },
                {
                    value: 27,
                    label: "陕西省"
                },
                {
                    value: 28,
                    label: "甘肃省"
                },
                {
                    value: 29,
                    label: "青海省"
                },
                {
                    value: 30,
                    label: "宁夏回族自治区"
                },
                {
                    value: 31,
                    label: "新疆维吾尔自治区"
                }
            ],
            columns: [
                // { prop: "rank", label: "排名" },
                // { prop: "userName", label: "用户名" },
                // { prop: "number", label: "学号" },
                // { prop: "acceptSum", label: "通过" },
                // { prop: "penaltyTime", label: "罚时" },
            ],
            rankList: null,
            flag: true,
        }
    },
    mounted: function () {
        this.getRankList();
        this.getContestProblemMap();
    },
    methods: {
        formTime(time) {
            if (time == null) {
                return "N/A";
            }
            if (time >= 1000) return (time / 1000).toFixed(2) + " s";
            return time + " ms";
        },
        handleDialogClose() {
      this.isModalVisible = false;
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
        dateFormat(date) {
            return moment(date).format("YYYY-MM-DD  HH:mm:ss");
        },
        initializeEditor() {
            this.$nextTick(() => {
                if (this.codemarkdown != null) return;
                this.codemarkdown = new Cherry({
                    id: "codemarkdown",
                    value: "```" +
                        this.LanguageList[this.submissionInfo.language] +
                        "\n" +
                        this.submissionInfo.code +
                        "\n```",
                    themeSettings: {
                        codeBlockTheme: "one dark",
                    },
                    editor: {
                        defaultModel: "previewOnly",
                    },
                });
            });

        },
        fetchData() {
            api
                .getSubmissionById({
                    submissionId: this.submissionId
                })
                .then((response) => {
                    if (response.data.code === "200") {
                        this.submissionInfo = response.data.data;
                        console.log(this.submissionInfo);

                        this.submissionInfo.createTime = this.dateFormat(
                            this.submissionInfo.createTime
                        );

                        this.codemarkdown.setMarkdown(
                            "```" +
                            this.LanguageList[this.submissionInfo.language] +
                            "\n" +
                            this.submissionInfo.code +
                            "\n```"
                        );
                    }
                });
        },
        openCode(item) {

            if (this.checkState(this.contest) != 2) {
                // 报错
                this.$message.error('比赛期间，不能查看');
                return;
            }
            if (item.isAccept == false) {
                return;
            }
            if(item.submissionId==null)
            {
                return ;
            }
            this.submissionId = item.submissionId;
            this.fetchData();
            // api.getSubmissionById({
            //         submissionId: item.submissionId
            //     })
            //     .then((response) => {
            //         if (response.data.code === "200") {
            //             console.log(response.data.data);

            //         }
            //     })
            this.isModalVisible = true;
        },
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
        handleSearch() {
            this.getRankList();
        },
        remoteMethod(query) {
            if (query !== '') {
                this.loading = true;
                setTimeout(() => {
                    this.loading = false;
                    this.schoolOptions = [];
                    api.getSchoolList({
                        schoolName: query
                    }).then((response) => {
                        if (response.data.code === "200") {
                            for (let i = 0; i < response.data.data.length; i++) {
                                this.schoolOptions.push({
                                    value: response.data.data[i].schoolId,
                                    label: response.data.data[i].schoolName,
                                });
                            }
                        }
                    });
                    // this.options = this.list.filter(item => {
                    //   return item.label.toLowerCase()
                    //     .indexOf(query.toLowerCase()) > -1;
                    // });
                }, 200);
            } else {
                this.schoolOptions = [];
            }
        },
        getColor(row) {
            if (row.isAccept == null) {
                return "#ffffff";
            } else if (row.isAccept) {
                return "#67c23a";
            } else {
                return "#FFECEC";
            }
        },
        getContestProblemMap() {
            if (this.checkState(this.contest) != 0) {
                api.getContestProblemMap(this.contest.contestId).then((response) => {
                    if (response.data.code === "200") {
                        for (let i = 0; i < response.data.data.length; i++) {
                            this.columns.push({
                                prop: response.data.data[i].problemId,
                                label: response.data.data[i].text
                            });
                        }
                    }
                });
            }

        },
        // handleRowClick(row) {
        //     // 根据实际情况处理点击事件
        //     window.open(this.generateLink(row), '_blank');
        // },
        // generateLink(row) {
        //     // 根据实际情况生成链接地址
        //     return "/problem/" + this.contest.contestId + "/" + row.problemId;
        // },
        getRankList() {
            if (this.checkState(this.contest) != 0) {
                api.getRankList(this.formData).then(res => {
                    if (res.data.code == "200") {
                        this.rankList = res.data.data;
                    }
                });
            }

        }
    }
}
</script>

<style scoped>
.centered-box {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    flex-direction: column;
}

::v-deep .el-dialog {
    border-radius: 20px;
}

::v-deep .el-table .el-table__row:hover {
    cursor: pointer;
}

::v-deep .el-table .el-table__cell {
    padding: 0px;
    margin: 0px;
}

::v-deep .el-table .cell {
    padding: 0px;
    margin: 0px;
}
</style>
