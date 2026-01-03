<template>
  <div class="code-content">
    <div style="height: 32px; line-height: 32px; display: flex">
      <div style="margin-left: 10px">
        <el-dropdown trigger="click" @command="handleChange" placement="bottom">
          <el-button type="info" size="mini" plain round>
            {{ mode }}<i class="el-icon-arrow-down el-icon--right"></i
          ></el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="C">C</el-dropdown-item>
            <el-dropdown-item command="C++">C++</el-dropdown-item>
            <el-dropdown-item command="Java">Java</el-dropdown-item>
            <el-dropdown-item command="Python">Python</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div style="margin-left: auto; display: flex">
        <i class="el-icon-s-tools" @click="handleShow" />
      </div>
    </div>

    <CodeEditor
      @code-change="codeChange"
      :code="code"
      :selected-mode="mode"
      style="flex: 1" />
    <el-dialog
      :visible.sync="showTool"
      destroy-on-close
      :close-on-click-modal="false"
      :title="'设置'">
      <CodeStyle />
    </el-dialog>
  </div>
</template>

<script>
import CodeEditor from "@/components/Code/CodeEditor.vue";
import CodeStyle from "@/components/Code/CodeStyle.vue";

export default {
  name: "CodeMain",
  props: {
    code: {
      type: String,
    },
    mode: {
      type: String,
    },
  },
  components: { CodeStyle, CodeEditor },
  data() {
    return {
      showTool: false,
      currStyle: "",
      isHovered: false,
    };
  },
  mounted() {},
  methods: {
    codeChange(newCode) {
      this.$emit("code-change", newCode);
    },
    handleChange(val) {
      this.$emit("mode-event", val);
    },
    handleShow() {
      this.showTool = true;
    },
    handleMouseEnter() {
      this.isHovered = true;
    },
    handleMouseLeave() {
      this.isHovered = false;
    },
  },
};
</script>

<style scoped>
.hovered {
  border-radius: 5px;
  background-color: #f0f0f0; /* 你可以自定义背景色 */
}
.code-content {
  width: 100%;
  height: 100%;
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
