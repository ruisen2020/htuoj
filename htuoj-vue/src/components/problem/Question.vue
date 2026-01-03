<template>
  <div class="question-content">
    <el-tabs type="card" class="root" v-model="activeName">
      <el-tab-pane class="pane" name="first">
        <span slot="label"><i class="el-icon-tickets"></i> 问题</span>
        <div>
          <div style="display: flex">
            <div
              class="title"
              style="
                font-size: 24px;
                height: 32px;
                line-height: 32px;
                font-weight: 600;
              ">
              {{ problemInfo.problemId }}.{{ problemInfo.title }}
            </div>
          </div>
          <div style="display: flex; gap: 5px; margin-top: 10px">
            <span v-if="problemInfo.difficultyLevel === 0">
              <el-tag size="medium" type="success"> 简单 </el-tag>
            </span>
            <span v-else-if="problemInfo.difficultyLevel === 1">
              <el-tag size="medium" type="warning"> 中等 </el-tag>
            </span>
            <span v-else>
              <el-tag size="medium" type="danger"> 困难 </el-tag>
            </span>

            <el-tag size="medium" type="info"
              >时间限制:{{ problemInfo.timeLimit }} ms
            </el-tag>
            <el-tag size="medium" type="info"
              >空间限制:{{ problemInfo.memoryLimit }} MB
            </el-tag>
            <el-tag size="medium" type="info"
              >提交:{{ problemInfo.submitCount }}
            </el-tag>
            <el-tag size="medium" type="info"
              >通过：{{ problemInfo.acceptCount }}
            </el-tag>
            <span v-for="(item, index) in problemInfo.labelList" :key="index">
              <el-tag size="medium" type="info">{{ item }}</el-tag>
            </span>
          </div>
          <div
            style="margin-top: 20px"
            id="problemDescriptionMarkdown"
            class="markdown"></div>
          <div style="font: 20px sans-serif; font-weight: 600">输入描述</div>
          <div id="inputDescriptionMarkdown"></div>
          <div style="font: 20px sans-serif; font-weight: 600">输出描述</div>
          <div id="outputDescriptionMarkdown"></div>
          <div v-for="(item, index) in problemInfo.sampleList" :key="index">
            <div style="margin-top: 20px">
              <div>示例 {{ index + 1 }}</div>
              <div></div>
              <div
                style="
                  margin-top: 20px;
                  display: flex;
                  align-items: stretch;
                  justify-content: space-between;
                ">
                <el-card style="width: 49%">
                  <div
                    style="
                      display: flex;
                      align-items: center;
                      justify-content: space-between;
                    ">
                    <div style="font-size: 16px; font-weight: 100">输入</div>
                    <div>
                      <el-button
                        type="info"
                        size="mini"
                        plain
                        round
                        class="copy-btn"
                        :data-clipboard-target="`#sampleInput${index}`"
                        @click="handleCopyClick">
                        复制
                      </el-button>
                    </div>
                  </div>
                  <div
                    :id="`sampleInput${index}`"
                    v-html="item.sampleInput"
                    style="margin-top: 10px; white-space: pre-wrap"></div>
                </el-card>
                <el-card style="width: 49%">
                  <div
                    style="
                      display: flex;
                      align-items: center;
                      justify-content: space-between;
                    ">
                    <div style="font-size: 16px; font-weight: 100">输出</div>
                    <div>
                      <el-button
                        type="info"
                        size="mini"
                        plain
                        round
                        class="copy-btn"
                        :data-clipboard-target="`#sampleOutput${index}`"
                        @click="handleCopyClick">
                        复制
                      </el-button>
                    </div>
                  </div>
                  <div
                    :id="`sampleOutput${index}`"
                    v-html="item.sampleOutput"
                    style="margin-top: 10px; white-space: pre-wrap"></div>
                </el-card>
              </div>
            </div>
          </div>

          <div>
            <div
              style="font: 20px sans-serif; font-weight: 600; margin-top: 20px">
              提示信息
            </div>
            <div>
              <el-card style="width: 100%; margin-top: 20px">
                <div id="noteMarkdown"></div>
              </el-card>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane class="pane" name="second">
        <span slot="label"><i class="el-icon-document"></i> 题解</span>
        <QuestionProblemSolveList
          :categoryId="4"
          v-if="activeName == 'second'"></QuestionProblemSolveList>
      </el-tab-pane>
      <el-tab-pane class="pane" name="third">
        <span slot="label"><i class="el-icon-chat-square"></i> 讨论</span>
        <QuestionProblemSolveList
          :categoryId="2"
          v-if="activeName == 'third'"></QuestionProblemSolveList>
      </el-tab-pane>
      <el-tab-pane class="pane" name="sixth">
        <span slot="label"><i class="el-icon-search"></i> AI助手</span>
        <AIAssistant
          v-if="activeName == 'sixth'"
          @click-event="handleClickRow"></AIAssistant>
      </el-tab-pane>
      <el-tab-pane class="pane" name="fourth">
        <span slot="label"><i class="el-icon-time"></i> 提交记录</span>
        <SubmissionList
          v-if="activeName == 'fourth'"
          @click-event="handleClickRow"></SubmissionList>
      </el-tab-pane>

      <el-tab-pane v-if="submissionInfoDisplayFlag" class="pane" name="fifth">
        <span slot="label"><i class="el-icon-reading"></i> 记录详情</span>
        <SubmissionDetail :submissionId="submissionId" :count="count">
        </SubmissionDetail>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import mathJax from "@/utils/MathJax";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import api from "@/common/api";
import ClipboardJS from "clipboard";
import QuestionProblemSolveList from "./QuestionProblemSolveList.vue";
import SubmissionList from "./SubmissionList.vue";
import SubmissionDetail from "./SubmissionDetail.vue";
import AIAssistant from "./AIAssistant.vue";
let count1 = 0;
// import "@utils/math.js";
export default {
  components: {
    QuestionProblemSolveList,
    SubmissionList,
    SubmissionDetail,
    AIAssistant,
  },

  name: "QuestionPart",
  props: {
    submissionInfoDisplayFlag: {
      type: Boolean,
    },
    submissionId: {
      type: Number,
    },
    count: {
      type: Number,
    },
  },
  data() {
    return {
      activeName: "first",
      problemInfo: null,
      problemDescriptionMarkdown: null,
      inputDescriptionMarkdown: null,
      outputDescriptionMarkdown: null,
      activateName: "problemDescription",
      noteMarkdown: null,
      problemId: this.$route.params.problemId,
      showCollectSelect: false,
      collectFiletData: this.test(), // 关于创建的所有收藏夹
      selectedCollectFile: [], // 选择保存到哪些收藏夹,存储id号
      isIndeterminate: true,
      selectAll: false,
      isCreate: false, // 是否创建收藏夹
      createCollect: {
        title: "",
        isPrivate: false,
      },
      runTime: 30,
      pastNumber: 100000,
      pastPercentage: 50,
    };
  },
  watch: {
    submissionInfoDisplayFlag(flag) {
      if (flag == true) {
        this.activeName = "fifth";
      }
    },
  },
  updated() {
    // this.$nextTick(() => {
    if (this.problemDescriptionMarkdown == null) {
      this.problemDescriptionMarkdown = new Cherry({
        id: "problemDescriptionMarkdown",
        value: this.problemInfo.content,
        editor: {
          defaultModel: "previewOnly",
        },
      });
    }

    if (this.inputDescriptionMarkdown == null) {
      this.inputDescriptionMarkdown = new Cherry({
        id: "inputDescriptionMarkdown",
        value: this.problemInfo.inputDescription,
        editor: {
          defaultModel: "previewOnly",
        },
      });
    }

    if (this.outputDescriptionMarkdown == null) {
      this.outputDescriptionMarkdown = new Cherry({
        id: "outputDescriptionMarkdown",
        value: this.problemInfo.outputDescription,
        editor: {
          defaultModel: "previewOnly",
        },
      });
    }
    if (this.noteMarkdown == null) {
      this.noteMarkdown = new Cherry({
        id: "noteMarkdown",
        value: this.problemInfo.note,
        editor: {
          defaultModel: "previewOnly",
        },
      });
    }
    //});
  },
  mounted() {
    this.getProblemById();

    // 初始化 ClipboardJS
    new ClipboardJS(".copy-btn");
  },

  computed: {
    getItem() {
      const item = this.items.find(
        (item) => item.degree === this.selectedDegree
      );
      return item ? item : "";
    },
  },

  methods: {
    handleClickRow(submissionId) {
      console.log("aaa", submissionId);

      this.$emit("submissionId-event", submissionId);
      this.$emit("submissionInfoDisplayFlag-event", true);
      // this.submissionInfoDisplayFlag = true;
      this.activeName = "fifth";
      // this.submissionId = submissionId;
    },
    handleCopyClick() {
      this.$message.success("复制成功");
    },
    async getProblemById() {
      await api.getProblemById(this.problemId).then((response) => {
        if (response.data.code == "200") {
          this.problemInfo = response.data.data;
          this.problemInfo.souresList = this.problemInfo.sources
            ? this.problemInfo.sources.split(",")
            : [];
          this.problemInfo.labelList = this.problemInfo.labels
            ? this.problemInfo.labels.split(",")
            : [];
          this.$nextTick(() => {
            window.scrollTo(0, 0);
          });
        }
      });
    },
    setLatex(elementId) {
      // 题目处理
      this.$nextTick(() => {
        if (mathJax.isMathjaxConfig) {
          //判断是否初始配置，若无则配置。
          mathJax.initMathjaxConfig();
        }
        mathJax.MathQueue(elementId); //传入组件class，让组件被MathJax渲染
      });
    },
    handleSelectAllChange(val) {
      const ids = this.collectFiletData.map((item) => item.data.id);
      this.selectedCollectFile = val ? ids : [];
      this.isIndeterminate = false;
    },
    handleSelectedFilesChange(value) {
      let selectedCount = value.length;
      this.selectAll = selectedCount === this.collectFiletData.length;
      this.isIndeterminate =
        selectedCount > 0 && selectedCount < this.collectFiletData.length;
      console.log(this.isIndeterminate);
    },
    create() {},
    // 收藏夹测试数据
    test() {
      return new Array(5).fill(null).map(() => ({
        data: {
          id: count1++,
          file: "这里时收藏夹名",
        },
      }));
    },
  },
};
</script>

<style scoped>
::v-deep .cherry {
  box-shadow: none;
}

::v-deep .cherry-markdown h2,
.cherry-markdown .h2 {
  font-size: 20px;
}

::v-deep .cherry-markdown a.anchor:before {
  content: "" !important;
}

::v-deep .el-tag {
  border-radius: 10px;
}

::v-deep .cherry-previewer {
  border: none;
  padding: 10px;
}

::v-deep .cherry-drag {
  width: 5px;
  background-color: #ebedee;
}

.question-content {
  overflow: auto;
  width: 100%;
  height: 100%;
  background-color: white;
  border-radius: 5px;
  padding: 16px;
  box-sizing: border-box;
}

.question-header {
  display: flex;
  flex-direction: column;
}

.question-tag {
  padding-top: 10px;
  padding-bottom: 15px;
}

.question-main {
  overflow-y: auto;
  overflow-x: hidden;
}

.ei {
  display: flex;
  align-items: center;
  min-height: 100%;
  /* 设置最小高度为100%，确保内容不会溢出 */
}

pre {
  font-size: 0.8rem;
  /* 可以根据需要调整字体大小 */
  background-color: #f8f8f8;
  /* 背景颜色 */
  border-radius: 5px;
  /* 圆角 */
  overflow-x: auto;
  /* 水平滚动条 */
  width: 100%;
  padding: 5px;
}

code {
  white-space: pre-wrap;
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

/*::v-deep .el-tabs--card {
  height: calc(100vh - 110px);
  !* overflow-y: auto; *!
}
::v-deep .el-tab-pane {
  height: calc(100vh - 110px);
  overflow-y: auto;
}*/
::v-deep .el-tabs--card > .el-tabs__header .el-tabs__item {
  font-size: 16px;
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

.el-icon-star-off:hover {
  cursor: pointer;
}

.el-icon-star-off {
  font-size: 20px;
}

.el-icon-star-on:hover {
  cursor: pointer;
}

.el-icon-star-on {
  color: #e6a23c;
  font-size: 24px;
}

.el-icon-right:hover {
  cursor: pointer;
}

.el-icon-arrow-left:hover {
  cursor: pointer;
}

.select-collect {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  margin-bottom: 10px;
}

.collect-pop {
  width: 220px;
  display: flex;
  flex-direction: column;
  border-bottom: #dddddd 1px solid;
}

.collect-item {
  width: 100%;
  padding: 3px;
}

.collect-item:hover {
  background-color: #dddddd;
  border-radius: 5px;
}

.add-collect {
  margin-top: 5px;
  padding: 5px 5px 2px 5px;
  display: flex;
  justify-content: center;
}

.add-collect:hover {
  cursor: pointer;
  background-color: #dddddd;
  border-radius: 5px;
}

.root {
  flex: auto;
  height: 97%;
  display: flex;
  flex-direction: column;
}

::v-deep .el-tabs__content {
  flex-grow: 1;
  /* overflow-y: scroll; */
}

.pane {
  height: 100%;
  padding: 5px;
}

.question-footer {
}
</style>
