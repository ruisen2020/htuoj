<template>
<div>
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
            <div id="markdown"></div>
        </el-card>
    </div>
</div>
</template>

<script>
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import api from "@/common/api";
import moment from "moment";
export default {
    props: {
        submissionId: {
            type: Number,
        },
        count: {
            type: Number,
            default: 0,
        },
    },
    watch: {
        count() {
            this.fetchData();
        },
        submissionId() {
            this.fetchData();
        },
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
            markdown: null,
            submissionInfo: null,
            LanguageList: {
                0: "C",
                1: "C++",
                2: "Java",
                3: "Python",
            },
        };
    },
    mounted() {
        // this.initMarkdown();
        this.fetchData();
    },
    create() {
        // this.initMarkdown();
        this.fetchData();
    },
    updated() {
        this.initMarkdown();
    },
    methods: {
        formTime(time) {
            if (time == null) {
                return "N/A";
            }
            if (time >= 1000) return (time / 1000).toFixed(2) + " s";
            return time + " ms";
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
        initMarkdown() {
            this.$nextTick(() => {
                if (this.markdown != null) return;
                this.markdown = new Cherry({
                    id: "markdown",
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

                        this.markdown.setMarkdown(
                            "```" +
                            this.LanguageList[this.submissionInfo.language] +
                            "\n" +
                            this.submissionInfo.code +
                            "\n```"
                        );
                    }
                });
        },
    },
};
</script>

<style></style>
