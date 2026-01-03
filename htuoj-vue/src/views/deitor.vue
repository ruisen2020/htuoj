<template>
  <codemirror
      v-model="code"

      :style="codemirrorStyle"
      :options="cmOptions"
  ></codemirror>
</template>

<script>
import { codemirror } from "vue-codemirror";
import "codemirror/mode/clike/clike.js";
import "codemirror/mode/python/python.js";
import "codemirror/mode/pascal/pascal.js"; //pascal
import "codemirror/mode/go/go.js"; //go
import "codemirror/mode/d/d.js"; //d
import "codemirror/mode/haskell/haskell.js"; //haskell
import "codemirror/mode/mllike/mllike.js"; //OCaml
import "codemirror/mode/perl/perl.js"; //perl
import "codemirror/mode/php/php.js"; //php
import "codemirror/mode/ruby/ruby.js"; //ruby
import "codemirror/mode/rust/rust.js"; //rust
import "codemirror/mode/javascript/javascript.js"; //javascript
import "codemirror/mode/fortran/fortran.js"; //fortran
import "codemirror/lib/codemirror.css";
import "codemirror/addon/lint/lint";
import "codemirror/addon/lint/lint.css";
import "codemirror/addon/lint/json-lint";
import "codemirror/addon/lint/javascript-lint";
import "codemirror/mode/javascript/javascript.js";
import "codemirror/theme/base16-dark.css";
import "codemirror/addon/fold/foldgutter.css";
import "codemirror/addon/fold/foldcode";
import "codemirror/addon/fold/foldgutter";
import "codemirror/addon/fold/brace-fold";
import "codemirror/addon/fold/comment-fold";
import "codemirror/addon/fold/markdown-fold";
import "codemirror/addon/fold/xml-fold";
import "codemirror/addon/fold/indent-fold";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint.js";
import "codemirror/addon/hint/javascript-hint";
import "codemirror/addon/hint/xml-hint";
import "codemirror/addon/hint/sql-hint";
import "codemirror/addon/hint/anyword-hint";
import "codemirror/addon/search/match-highlighter";
import "codemirror/addon/edit/matchbrackets";
import "codemirror/addon/edit/closebrackets";
import "codemirror/mode/css/css.js";
import "codemirror/mode/vue/vue.js";
export default {
  name: "CodeEditor",
  components: {
    codemirror,
  },
  data() {
    return {
      code: "",
      cmOptions: {
        mode: "text/x-csrc",
        gutters: [
          "CodeMirror-lint-markers",
          "CodeMirror-linenumbers",
          "CodeMirror-foldgutter",
        ],
        theme: "base16-dark",
        lineNumbers: true,
        line: true,
        lint: true,
        lineWrapping: true,
        autofocus: true,
        autoCloseBrackets: true,
        foldGutter: true, // 块槽
        hintOptions: { completeSingle: true },
        matchTags: { bothTags: true },
        matchBrackets: true,
        showCursorWhenSelecting: true,
        styleSelectedText: true,
        styleActiveLine: true,
        autoRefresh: true,
        highlightSelectionMatches: {
          minChars: 2,
          trim: true,
          style: "matchhighlight",
          showToken: false,
        },
      },
      codemirrorStyle: {
        fontSize: "18px",
        lineHeight: "150%",
        height: "450px",
        border: "1px solid #EBEEF5",
      },
    };
  },
  methods: {
    onCodeChange(newCode) {
      this.code = newCode;
    },
  },
};
</script>