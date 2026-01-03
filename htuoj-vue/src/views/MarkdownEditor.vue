<template>
  <div>
    <!-- Markdown 编辑器容器 -->
    <div ref="markdownContainer"></div>
    <!-- 可选：Markdown 输入框，用于动态改变编辑器内容 -->
    <!-- <textarea ref="markdownTextarea" v-model="markdownText"></textarea> -->
  </div>
</template>

<script>
// import "cherry-markdown/dist/cherry-markdown.css";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import api from "@/common/api";
// import MathJax from "@/utils/MathJax";
// import {VueMathjax} from 'vue-mathjax'
export default {
  data() {
    return {
      markdownText: "$ N $", // 初始 Markdown 文本
    };
  },
  created() {
    this.initCherryEditor();
    //   MathJax.typeset([this.$el], {
    //   showProcessingMessages: false // 是否显示处理消息
    // });
  },
  methods: {
    initCherryEditor() {
      this.cherryInstance = new Cherry({
        id: this.$refs.markdownContainer, // 绑定到指定 DOM 节点
        value: this.markdownText, // 初始化的 Markdown 内容
        fileUpload: this.myFileUpload,
        toolbars: {
          // 定义顶部工具栏
          toolbar: [
            "undo",
            "redo",
            "|",
            {
              bold: [
                "bold",
                "italic",
                "underline",
                "strikethrough",
                "sub",
                "sup",
                "ruby",
              ],
            },
            "|",
            "color",
            "header",
            "|",
            "list",
            "panel",
            "detail",
            "drawIo",
            {
              insert: [
                "image",
                "pdf",
                "word",
                "file",
                "link",
                "hr",
                "br",
                "code",
                "formula",
                "toc",
                "table",
                "drawIo",
              ],
            },
            "graph",
            "settings",
            "codeTheme",
          ],

          // 定义侧边栏，默认为空
          sidebar: ["theme", "mobilePreview", "copy"],
          // 定义顶部右侧工具栏，默认为空
          toolbarRight: ["fullScreen", "export", "wordCount"],
          // 定义选中文字时弹出的“悬浮工具栏”，默认为 ['bold', 'italic', 'underline', 'strikethrough', 'sub', 'sup', 'quote', '|', 'size', 'color']
          bubble: [
            "bold",
            "italic",
            "underline",
            "strikethrough",
            "sub",
            "sup",
            "quote",
            "|",
            "size",
            "color",
          ],

          // toc: true, // 不展示悬浮目录

          // toc: {
          //   updateLocationHash: false, // 要不要更新URL的hash
          //   defaultModel: "full", // pure: 精简模式/缩略模式，只有一排小点； full: 完整模式，会展示所有标题
          //   showAutoNumber: false, // 是否显示自增序号
          //   position: "fixed", // 悬浮目录的悬浮方式。当滚动条在cherry内部时，用absolute；当滚动条在cherry外部时，用fixed
          //   cssText: "", // 自定义样式
          // },

          // 定义光标出现在行首位置时出现的“提示工具栏”，默认为 ['h1', 'h2', 'h3', '|', 'checklist', 'quote', 'table', 'code']
          float: ["h1", "h2", "h3", "|", "checklist", "quote", "table", "code"],
        },
        drawioIframeUrl:
          window.location.origin + "/cherryMarkdown/drawio_demo.html",
        drawioIframeStyle: "border: none; width: 100%; height: 100%;",
      });
    },

    myFileUpload(file1, callback) {
      const formData = new FormData();
      formData.append("file", file1);
      // 先把文件上传到服务端，上传文件的具体代码需要自行实现
      api.uploadArticlePictureAndFile(formData).then((response) => {
        if (response.data.code == "200") {
          callback(response.data.data);
        }
      });
      // putFile(file, function (err, url) {
      //   if (err) {
      //     // 上传失败
      //   } else {
      //     callback(url);
      //   }
      // });
    },
  },
  // beforeDestroy() {
  //   // 清理工作，例如销毁编辑器实例
  //   if (this.cherryInstance) {
  //     this.cherryInstance.destroy();
  //   }
  // },
};
</script>

<style>
::v-deep #cherry-markdown {
  /* width: 1000px;
  height: 500px; */
}
</style>
