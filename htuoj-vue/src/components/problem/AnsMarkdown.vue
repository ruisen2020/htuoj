<template>
  <div>
    <div :id="cherryId"></div>
  </div>
</template>

<script>
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
export default {
  props: {
    id: {
      type: Number,
    },
    content: {
      type: String,
    },
  },
  data() {
    return {
      cherryId: "cherry" + this.id,
      markdown: null,
    };
  },
  watch: {
    content: function () {

      this.markdown.setMarkdown(this.content);
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.markdown = new Cherry({
        id: this.cherryId,
        value: this.content,
        themeSettings: {
          codeBlockTheme: "one dark",
        },
        editor: {
          defaultModel: "previewOnly",
        },
      });
    });
  },
};
</script>

<style></style>
