<template>
  <div
    class="output-content"
    v-loading="loading2"
    element-loading-spinner="el-icon-loading">
    <div class="output-header" style="font-size: 16px">
      <div style="font-size: 16px; border-radius: 5px">
        <el-button
          :type="flag ? 'success' : 'info'"
          @click="flag = true"
          size="small"
          style="border-radius: 10px">
          测试用例</el-button
        >
      </div>

      <div style="font-size: 16px; border-radius: 5px; margin-left: 10px">
        <el-button
          :disabled="resultList == null || resultList.length == 0"
          :type="!flag ? 'success' : 'info'"
          @click="flag = false"
          size="small"
          style="border-radius: 10px">
          测试结果</el-button
        >
      </div>
      <div style="margin-left: auto; display: flex">
        <span
          @click="addTestSubmission"
          class="tag"
          style="
            width: 68px;
            font-size: 14px;
            border-radius: 5px;
            border-radius: 10px;
            background-color: #000a200d;
            display: flex;
            justify-content: center;
            align-items: center;
          ">
          运行</span
        >
        <span
          @click="addSubmission"
          class="tag"
          style="
            width: 68px;
            font-size: 14px;
            border-radius: 5px;
            margin-left: 10px;
            border-radius: 10px;
            background-color: #2db55d;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #fff;
          ">
          提交</span
        >
      </div>
    </div>
    <div class="output-main">
      <!-- 测试用例 -->
      <div v-if="flag" style="padding-top: 10px">
        <el-tag
          v-for="(sample, index) in sampleList"
          :key="index"
          type="info"
          @close="handleCloseIndex(index)"
          @click="handleChangeIndex(index)"
          closable
          :disable-transitions="false"
          style="border-radius: 10px; border: none"
          :style="{
            backgroundColor: currentIndex == index ? '#000a200d' : '#fff',
          }">
          Case {{ index + 1 }}
        </el-tag>
        <i class="el-icon-plus" @click="addSample" />
      </div>

      <div v-if="flag">
        <el-card style="border-radius: 10px; border: none; margin-top: 10px">
          <div style="margin-bottom: 5px">Input:</div>
          <el-input
            style="font-size: 16px"
            :autosize="autoSize"
            type="textarea"
            placeholder="请输入内容"
            :rows="3"
            v-model="currentSample.input">
          </el-input>
        </el-card>
        <el-card style="border-radius: 10px; border: none; margin-top: 10px">
          <div style="margin-bottom: 5px">Output:</div>
          <el-input
            style="font-size: 16px"
            :autosize="autoSize"
            type="textarea"
            placeholder="请输入内容"
            :rows="3"
            v-model="currentSample.output">
          </el-input>
        </el-card>
      </div>
      <!-- 运行结果 -->
      <div>
        <div v-if="!flag" style="padding-top: 10px">
          <el-tag
            v-for="(result, index) in resultList"
            :key="index"
            type="info"
            @click="handleChangeResultIndex(index)"
            :disable-transitions="false"
            style="border-radius: 10px; border: none"
            :style="{
              backgroundColor:
                currentResultIndex == index ? '#000a200d' : '#fff',
            }">
            <i
              v-if="result.status == 1"
              style="color: #2db55d; font-weight: 1000"
              class="el-icon-check"></i>
            <i
              v-else
              style="color: red; font-weight: 1000"
              class="el-icon-close"></i>
            Case {{ index + 1 }}
          </el-tag>
        </div>

        <div v-if="!flag">
          <el-card style="border-radius: 10px; border: none; margin-top: 10px">
            <div style="margin-bottom: 5px">Input:</div>
            <el-input
              style="font-size: 16px"
              :autosize="autoSize"
              type="textarea"
              :rows="3"
              v-model="currentResult.input">
            </el-input>
          </el-card>
          <el-card style="border-radius: 10px; border: none; margin-top: 10px">
            <div style="margin-bottom: 5px">Output:</div>
            <el-input
              style="font-size: 16px"
              :autosize="autoSize"
              type="textarea"
              :rows="3"
              v-model="currentResult.output">
            </el-input>
          </el-card>
          <el-card style="border-radius: 10px; border: none; margin-top: 10px">
            <div style="margin-bottom: 5px">Real Output:</div>
            <el-input
              style="font-size: 16px"
              :autosize="autoSize"
              type="textarea"
              :rows="3"
              v-model="currentResult.realOutput">
            </el-input>
          </el-card>
        </div>
      </div>
    </div>
    <div v-show="false">{{ Result }}</div>
  </div>
</template>

<script>
import api from "@/common/api";

export default {
  name: "TestOutPut",
  props: {
    Result: {
      type: Array,
    },
    loading2: {
      type: Boolean,
    },
  },
  data() {
    return {
      autoSize: {
        minRows: 2,
      },
      currentSample: null,
      currentoutput: "Case 1",
      flag: true,
      contents: [], //测试用例
      cases: [],
      output: [],
      currentResult: null,
      sampleList: [],
      resultList: [],
      currentResultIndex: 0,
      currentIndex: 0,
    };
  },
  mounted() {
    this.getSampleList();
  },
  updated() {
    console.log(this.Result);
  },

  methods: {
    addTestSubmission() {
      this.$emit("addTestSubmission", this.sampleList);
      this.waitForResultChange();
    },
    waitForResultChange() {
      const unwatch = this.$watch("Result", (newVal, oldVal) => {
        if (newVal !== oldVal) {
          this.flag = false;
          this.resultList = this.Result;
          this.currentResult = this.Result[0];
          // this.loading = false;
          unwatch(); // 停止监听
        }
      });
    },
    handleCloseIndex(index) {
      if (this.sampleList.length == 1) {
        this.$message.error("至少保留一个测试用例");
        return;
      }
      this.sampleList.splice(index, 1);
      if (index == this.sampleList.length) {
        this.currentSample = this.sampleList[index - 1];
        this.currentIndex = index - 1;
      } else {
        this.currentSample = this.sampleList[index];
        this.currentIndex = index;
      }
    },
    addSample() {
      this.sampleList.push({
        input: this.currentSample.input,
        output: this.currentSample.output,
      });
      this.currentSample = this.sampleList[this.sampleList.length - 1];
      this.currentIndex = this.sampleList.length - 1;
    },
    handleChangeIndex(index) {
      this.currentIndex = index;
      this.currentSample = this.sampleList[index];
    },
    handleChangeResultIndex(index) {
      this.currentResultIndex = index;
      this.currentResult = this.resultList[index];
    },
    getSampleList() {
      api
        .getSampleByProbelmId({ problemId: this.$route.params.problemId })
        .then((response) => {
          if (response.data.code === "200") {
            this.sampleList = response.data.data;
            if (this.sampleList.length > 0)
              this.currentSample = this.sampleList[0];
          }
        });
    },
    addSubmission() {
      this.$emit("add-submission");
    },
  },
};
</script>

<style scoped>
::v-deep .el-tag {
  /* background-color: red; */
}
.output-content {
  /* border: #dcdfe6 2px solid; */
  overflow: auto;
  border-radius: 5px;
  width: 100%;
  box-sizing: border-box;
}
.output-header {
  display: flex;
  background-color: #f8f8f8;
  padding: 5px;
}

.tag:hover {
  cursor: pointer;
}
.output-main {
  background-color: white;

  padding: 0px 10px 10px 10px;
}
.test {
  padding-top: 10px;
}
.el-icon-postcard {
  color: #4ec769;
  font-weight: bold;
}
.el-icon-document {
  color: #4ec769;
  font-weight: bold;
}
.test-input {
  width: 100%;
  /* min-height: 40px; */
  padding: 10px;
  box-sizing: border-box;
  resize: none; /* 禁止调整大小 */
  background-color: #f8f8f8; /* 背景颜色 */
  border-radius: 5px; /* 圆角 */
  border: #dcdfe6 1px solid;
  margin: 1em 0px;
}
pre {
  font-size: 0.8rem; /* 可以根据需要调整字体大小 */
  background-color: #f8f8f8; /* 背景颜色 */
  border-radius: 5px; /* 圆角 */
  overflow-x: auto; /* 水平滚动条 */
  width: 100%;
  /* min-height: 40px; */
  padding: 5px;
  border: #dcdfe6 1px solid;
}
code {
  white-space: pre-wrap;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.el-tag:hover {
  cursor: pointer;
}
.el-icon-plus {
  margin-left: 10px;
  /* height: 32px; */
  line-height: 32px;
}
.el-icon-plus:hover {
  cursor: pointer;
}
</style>
