<template>
    <div class="question-content">
      <el-tabs type="card" class="root">
        <el-tab-pane class="pane">
          <el-row style="flex-direction: column; height: 100%">
            <el-col style="height: 10%">
              <div class="question-header">
                <div style="display: flex; justify-content: space-between">
                  <span style="font-size: 22px; font-weight: bold">{{
                    title
                  }}</span>
                  <div>
                    <i
                      v-if="isCollected"
                      @click="isCollected = false"
                      class="el-icon-star-on"
                    ></i>
                    <i
                      v-popover:collect
                      v-if="!isCollected"
                      @click="isCollected = true"
                      class="el-icon-star-off"
                    ></i>
                  </div>
                </div>
                <div class="question-tag">
                  <el-tag
                    :type="getItem.type"
                    style="border-radius: 30px"
                    size="small"
                    effect="plain"
                    >{{ getItem.degree }}</el-tag
                  >
                </div>
              </div>
              <el-popover ref="collect" placement="bottom" trigger="click">
                <div v-if="!isCreate" style="width: 220px; height: 235px">
                  <div class="collect-pop">
                    <div
                      style="
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        margin-bottom: 10px;
                      "
                    >
                      <span style="font-size: 14px; font-weight: bolder"
                        >我的收藏夹</span
                      >
                      <i class="el-icon-right" style="font-size: 18px"></i>
                    </div>
                    <el-checkbox
                      :indeterminate="isIndeterminate"
                      v-model="selectAll"
                      @change="handleSelectAllChange"
                      >全选</el-checkbox
                    >
                    <el-checkbox-group
                      class="select-collect"
                      fill="#303133"
                      v-model="selectedCollectFile"
                      @change="handleSelectedFilesChange"
                    >
                      <el-checkbox
                        class="collect-item"
                        v-for="collect in collectFiletData"
                        :label="collect.data.id"
                        :key="collect.data.id"
                      >
                        {{ collect.data.file }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                  <div class="add-collect" @click="isCreate = true">
                    <i
                      class="el-icon-plus"
                      style="display: flex; align-items: center"
                    ></i>
                    创建收藏夹
                  </div>
                </div>
                <el-row
                  v-if="isCreate"
                  style="width: 220px; height: 235px; flex-direction: column"
                >
                  <el-col style="height: 20%">
                    <div style="margin-bottom: 20px">
                      <i class="el-icon-arrow-left" @click="isCreate = false"></i>
                      <span
                        style="
                          font-size: 14px;
                          font-weight: bolder;
                          margin-left: 5px;
                        "
                        >创建收藏夹</span
                      >
                    </div>
                  </el-col>
                  <el-col style="height: 60%">
                    <el-form :model="createCollect" size="small">
                      <el-form-item>
                        <el-input
                          v-model="createCollect.title"
                          placeholder="请输入收藏夹名称"
                        ></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-checkbox v-model="createCollect.isPrivate"
                          >设为私密</el-checkbox
                        >
                      </el-form-item>
                    </el-form>
                  </el-col>
                  <el-col style="height: 20%">
                    <el-button @click="create" size="small" style="width: 100%"
                      >创建</el-button
                    >
                  </el-col>
                </el-row>
              </el-popover>
            </el-col>
            <el-col style="height: 90%">
              <div class="question-main">
                <div class="question">
                  <div v-html="'<span>' + question + '</span>'"></div>
                </div>
                <div class="example-input">
                  <h4>输入示例：</h4>
                  <pre class="ei"><code>{{inputCode}}</code></pre>
                </div>
                <div class="example-output">
                  <h4>输出示例：</h4>
                  <pre class="ei"><code>{{outputCode}}</code></pre>
                </div>
                <div class="dataRange" v-if="dataRange">
                  <h4>数据范围：</h4>
                  <div v-html="'<span>' + dataRange + '</span>'"></div>
                </div>
              </div>
            </el-col>
          </el-row>
          <span slot="label"><i class="el-icon-tickets"></i> 问题</span>
        </el-tab-pane>
        <el-tab-pane class="pane">
          <span slot="label"><i class="el-icon-chat-line-square"></i> 题解</span>
        </el-tab-pane>
        <el-tab-pane class="pane">
          <span slot="label"><i class="el-icon-time"></i> 提交记录</span>
        </el-tab-pane>
      </el-tabs>
      <div class="question-footer">
        <span style="font-size: small">运行时间：{{ runTime }}ms </span>
        <span style="font-size: small">通过人数：{{ pastNumber }} </span>
        <span style="font-size: small">通过率：{{ pastPercentage }}%</span>
      </div>
    </div>
  </template>
  
  <script>
  import mathJax from "@/utils/MathJax";
  let count = 0;
  export default {
    name: "QuestionPart",
    data() {
      return {
        title: "题目", // 题目名称
        selectedDegree: "简单", // 后端传来的难易程度
        question: "我是题目\\(\\frac{(a+b)\\times c}{(d+e)\\times f}\\)", // 后端传来的题目
        inputCode: "5\n1123", // 示例输入
        outputCode: "1234", // 示例输出
        dataRange: "\\(1 \\leq n \\leq 100000\\)", // 数据范围
        items: [
          { type: "success", degree: "简单" },
          { type: "warning", degree: "中等" },
          { type: "danger", degree: "困难" },
        ],
        isCollected: false,
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
    // \\(...\\)中包括公式
    mounted() {
      this.setLatex("question");
      this.setLatex("dataRange");
    },
    watch: {
      question() {
        this.setLatex("question");
      },
      dataRange() {
        this.setLatex("dataRange");
      },
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
            id: count++,
            file: "这里时收藏夹名",
          },
        }));
      },
    },
  };
  </script>
  
  <style scoped>
  .question-content {
    width: 100%;
    height: 1000px;
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
    min-height: 100%; /* 设置最小高度为100%，确保内容不会溢出 */
  }
  pre {
    font-size: 0.8rem; /* 可以根据需要调整字体大小 */
    background-color: #f8f8f8; /* 背景颜色 */
    border-radius: 5px; /* 圆角 */
    overflow-x: auto; /* 水平滚动条 */
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
    overflow-y: scroll;
  }
  .pane {
    height: 100%;
    padding: 5px;
  }
  .question-footer {
  }
  </style>