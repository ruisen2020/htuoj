<!-- 组件 -->
<template>
  <div id="content" style="margin-top: 10px">
    <!-- <button @click="formatCode">格式化代码</button> -->
    <!-- <codemirror
      v-model="localCode"
      :options="options"
      @input="formatCode"></codemirror> -->
    <codemirror v-model="localCode" :options="options"></codemirror>
    <div v-show="false">{{ code }}</div>
  </div>
</template>

<script>
// 文件内引入
import { codemirror } from "vue-codemirror";
// 引入样式、主题、代码风格等配置或样式文件
import "@/utils/cm-setting.js";
import eventBus from "@/assets/js/eventBus";
// import "codemirror-formatting"; // 引入代码格式化插件
// import Prettier from "prettier/standalone";
// import ParserBabel from "prettier/parser-babel";
export default {
  name: "CodeEditor",
  props: {
    selectedMode: {
      type: String,
      required: false,
    },
    code: {
      type: String,
      required: true,
    },
  },
  // 注册使用
  components: {
    codemirror,
  },
  data() {
    return {
      localCode: this.code,
      modes: [
        { mode: "text/x-java", label: "Java" },
        { mode: "text/x-csrc", label: "C" },
        { mode: "text/x-c++src", label: "C++" },
        { mode: "text/x-python", label: "Python" },
      ],
      options: {
        style: {
          fontSize: "16px", // 设置字体大小
        },
        autoCloseBrackets: true, // 启用自动补全括号

        theme: "monokai", // 主题
        indentUnit: 4,
        tabSize: 4, // 制表符的宽度

        firstLineNumber: 1, // 从哪个数字开始计算行数。默认值为 1。
        readOnly: false, // 只读
        autorefresh: true,
        smartIndent: true, // 上下文缩进
        lineNumbers: true, // 是否显示行号
        styleActiveLine: true, // 高亮选中行
        viewportMargin: Infinity, //处理高度自适应时搭配使用
        showCursorWhenSelecting: true, // 当选择处于活动状态时是否应绘制游标
        mode: "text/x-c++src",
        styleSelectedText: false, // 高亮选中文本
        lineWrapping: true, // 自动换行
      },
    };
  },
  updated() {
    this.localCode = this.code;
  },
  watch: {
    localCode(newVal) {
      this.$emit("code-change", newVal);
    },
    selectedMode(newVal) {
      this.options.mode = this.modes.find((item) => item.label === newVal).mode;
    },
    "options.theme": {
      handler(newVal) {
        eventBus.$emit("curr-style", newVal);
      },
    },
  },
  mounted() {
    eventBus.$on("change-style", (theme) => {
      this.options.theme = theme;
    });
  },
  methods: {
    // formatCode() {
    //   const formattedCode = Prettier.format(this.code, {
    //     parser: "babel",
    //     plugins: [ParserBabel],
    //   });
    //   // 更新 CodeMirror 编辑器的内容
    //   this.$refs.codemirror.codemirror.setValue(formattedCode);
    // },
  },
};
</script>

<style scoped>
::v-deep .CodeMirror {
  height: 58vh;
  /* min-height: 500px; */
  border-radius: 16px;
  font-size: 24px;
  font-family: Consolas, "Courier New", monospace;
}
</style>
