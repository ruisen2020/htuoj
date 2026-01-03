<template>
<div>
    <div v-if="checkState(contest) == 0">
        <el-empty :image-size="200" description="比赛尚未开始，敬请等待"></el-empty>
    </div>
    <div v-else>
        <el-table :data="problemList" stripe style="cursor: pointer;" @row-click="handleRowClick">
            <el-table-column prop="status" align="center" label="状态" width="200">
                <template slot-scope="scope">
                    <div style="
                    text-align: center;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  ">
                        <span v-if="scope.row.isAccepted === null"></span>
                        <span v-else-if="scope.row.isAccepted == true" style="
                      text-align: center;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    ">
                            <!-- <i class="el-icon-circle-check"></i> -->
                            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path fill-rule="evenodd" clip-rule="evenodd" d="M8 16a8 8 0 01-8-8 8 8 0 018-8 8 8 0 018 8 8 8 0 01-8 8zM5.552 7.948L7 9.397l3.948-3.949a.78.78 0 111.104 1.104l-4.5 4.5a.78.78 0 01-1.104 0l-2-2a.78.78 0 111.104-1.104z" fill="#00AA54" />
                            </svg>
                        </span>
                        <span v-else-if="scope.row.isAccepted == false" style="
                      text-align: center;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    ">
                            <!-- <i class="el-icon-circle-close"></i> -->
                            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path fill-rule="evenodd" clip-rule="evenodd" d="M0 8a8 8 0 008 8 8 8 0 008-8 8 8 0 00-8-8 8 8 0 00-8 8zm9.593-2.65a.75.75 0 111.06 1.061l-1.59 1.591 1.59 1.591a.75.75 0 11-1.06 1.06l-1.591-1.59-1.591 1.59a.75.75 0 11-1.06-1.06l1.59-1.591-1.59-1.591a.75.75 0 011.06-1.06l1.591 1.59 1.591-1.59z" fill="#F04142" />
                            </svg>
                        </span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="displayId" align="center" label="题号" width="200">
            </el-table-column>
            <el-table-column prop="problemTitle" align="center" label="标题">
                <template slot-scope="scope">
                    <el-link target="_blank" :underline="false" :href="generateLink(scope.row)">
                        {{ scope.row.problemTitle }}
                    </el-link>
                </template>
            </el-table-column>

            <el-table-column width="300" prop="passRate" align="center" label="通过率">
                <template slot-scope="scope">
                    {{ scope.row.acceptCount }}/{{ scope.row.submitCount }}
                </template>
            </el-table-column>

        </el-table>
    </div>

</div>
</template>

<script>
import api from "@/common/api";
export default {
    props: {
        contest: Object
    },
    data() {
        return {
            problemList: null,
        }
    },
    mounted: function () {
        this.getContestProbelmList();
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
        handleRowClick(row) {

            api.checkRegister(this.$route.params.contestId).then((response) => {
                if (response.data.message !== "该竞赛未报名") {
                    window.open(this.generateLink(row), '_blank');
                } 
            });

        },
        generateLink(row) {

            // 根据实际情况生成链接地址
            return "/problem/" + this.contest.contestId + "/" + row.problemId;
        },
        getContestProbelmList() {
            if (this.checkState(this.contest) != 0) {
                api.getContestProblemList({
                    contestId: this.contest.contestId
                }).then(res => {
                    if (res.data.code == "200") {
                        this.problemList = res.data.data;
                    }
                });
            }
        }
    }
}
</script>

<style scoped>
.el-table .el-table__row:hover {
    cursor: pointer;
}
</style>
