<template>
    <div class="code-content">
      <div class="title">
        <i class="el-icon-edit-outline" />
        <span class="title-text">代码区</span>
      </div>
      <div class="code-header">
        <div style="display: flex; flex: 1">
          <el-dropdown trigger="click" @command="handleChange">
            <span class="el-dropdown-link"
              >{{ mode }}<i class="el-icon-arrow-down el-icon--right"></i
            ></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="C++">C++</el-dropdown-item>
              <el-dropdown-item command="C">C</el-dropdown-item>
              <el-dropdown-item command="Java">Java</el-dropdown-item>
              <el-dropdown-item command="Go">Go</el-dropdown-item>
              <el-dropdown-item command="Python">Python</el-dropdown-item>
              <el-dropdown-item command="JavaScript">JavaScript</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <i class="el-icon-refresh" />
          <i class="el-icon-s-tools" @click="handleShow" />
        </div>
        <div style="display: flex">
          <div
            class="test"
            style="display: flex; justify-content: center; margin-right: 10px"
          >
            <svg
              t="1729477428323"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1666"
              width="16"
              height="16"
            >
              <path
                d="M832.0064 512 192 896 192 128 832.0064 512 832.0064 512zM832.0064 512"
                fill="#499c54"
                p-id="1667"
              ></path>
            </svg>
            <span style="font-size: 12px; color: #499c54; font-weight: bold"
              >调试</span
            >
          </div>
          <div
            class="test"
            style="display: flex; justify-content: center; margin-right: 15px"
          >
            <svg
              t="1729478029979"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="2188"
              width="16"
              height="16"
            >
              <path
                d="M128 806.4l768 0 0 89.6-768 0 0-89.6ZM732.288 332.6272 555.7888 146.4896C544.8384 135.2832 529.376 128 512 128s-32.832 7.2832-43.6032 18.6048L291.5264 332.4928c-9.2288 10.0224-15.1104 22.9696-15.1104 37.3248 0 30.816 26.3808 55.8016 58.8992 55.8016 0.4544 0 0.896-0.0576 1.344-0.0704l0 0.352L416 425.9008l0 306.7264 192 0L608 425.9008l77.1904 0 0-0.448c1.1648 0.064 2.3104 0.1792 3.5008 0.1792 32.5248 0 58.8992-24.992 58.8992-55.8144C747.5904 355.4624 741.7024 342.5216 732.288 332.6272z"
                fill="#707070"
                p-id="2189"
              ></path>
            </svg>
            <span style="font-size: 12px; color: #707070; font-weight: bold"
              >提交</span
            >
          </div>
        </div>
      </div>
      <CodeEditor :selected-mode="mode" style="flex: 1" />
      <el-dialog
        :visible.sync="showTool"
        destroy-on-close
        :close-on-click-modal="false"
        :title="'设置'"
      >
        <CodeStyle />
      </el-dialog>
    </div>
  </template>
  
  <script>
  import CodeEditor from "@/components/Code/CodeEditor.vue";
  import CodeStyle from "@/components/Code/CodeStyle.vue";
  
  export default {
    name: "CodeMain",
    components: { CodeStyle, CodeEditor },
    data() {
      return {
        mode: "C++",
        showTool: false,
        currStyle: "",
      };
    },
    methods: {
      handleChange(val) {
        this.mode = val;
      },
      handleShow() {
        this.showTool = true;
      },
    },
  };
  </script>
  
  <style scoped>
  .code-content {
    width: 100%;
    height: 800px;
    background-color: white;
    border-radius: 5px;
    overflow: hidden;
    border: #dcdfe6 2px solid;
    box-sizing: border-box;
  }
  .title {
    background-color: #f8f8f8;
    padding: 5px 2px 5px 2px;
  }
  .el-icon-edit-outline {
    color: #4ec769;
    font-weight: bold;
  }
  .title-text {
    font-size: 14px;
    font-weight: bold;
    color: #606266;
    margin-left: 5px;
  }
  .code-header {
    border: 2px solid #f8f8f8;
    padding: 2px 2px 2px 15px;
    display: flex;
  }
  .el-dropdown-link {
    color: #909399;
    font-weight: bold;
  }
  .el-dropdown-link:hover {
    cursor: pointer;
  }
  .el-icon-refresh {
    margin-left: 10px;
    color: #909399;
    font-weight: bold;
  }
  .el-icon-refresh:hover {
    cursor: pointer;
  }
  .el-icon-s-tools {
    margin-left: 10px;
    color: #909399;
  }
  .el-icon-s-tools:hover {
    cursor: pointer;
  }
  .test:hover {
    cursor: pointer;
  }
  ::v-deep .el-dialog__body {
    padding: 0;
  }
  ::v-deep .el-dialog__header {
    height: 18px;
    background-color: #eeeeee;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }
  ::v-deep .el-dialog {
    border-radius: 8px;
  }
  </style>