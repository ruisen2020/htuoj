// 导入Vue
import Vue from "vue";
// 导入App.vue,他是跟组件，是整个应用最上层的组件，包裹所有普通小组件
// 基于App.vue 创建结构渲染 index.html
import App from "./App.vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import VueRouter from "vue-router";
import axios from "axios";
import "codemirror/lib/codemirror.css";
import "codemirror/mode/javascript/javascript";
import "cherry-markdown/dist/cherry-markdown.css";
import router from "../public/router";
import ECharts from "vue-echarts";
import elTableInfiniteScroll from "el-table-infinite-scroll";
// import MathJax from "./utils/MathJax";
import { useUserStore } from "../public/stores/modules/user";
import { createPinia, PiniaVuePlugin } from "pinia";
import piniaPluginPersisted from "pinia-plugin-persistedstate";
import VueDragResize from "vue-drag-resize";
import splitPane from "vue-splitpane";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import CherryMermaidPlugin from "cherry-markdown/dist/addons/cherry-code-block-mermaid-plugin";
import mermaid from "mermaid";
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Cherry.usePlugin(CherryMermaidPlugin, {
  mermaid, // 传入mermaid引用
  // mermaidAPI: mermaid.mermaidAPI, // 也可以传入mermaid API
  // 同时可以在这里配置mermaid的行为，可参考mermaid官方文档
  // theme: 'neutral',
  // sequence: { useMaxWidth: false, showSequenceNumbers: true }
});
// 全局安装
Vue.component("split-pane", splitPane);

Vue.component("vue-drag-resize", VueDragResize); // 拖拽工具
// Vue.prototype.MathJax = MathJax; // 数学公式工具
Vue.use(PiniaVuePlugin);
Vue.use(mavonEditor);
const pinia = createPinia();
pinia.use(piniaPluginPersisted);
axios.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    const token = userStore.getToken;
    if (token) {
      config.headers.Authorization = `${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
Vue.component("v-chart", ECharts);
Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.use(elTableInfiniteScroll);
// 提示：当前处于什么环境，true:表示开发环境，false表示生成环境
// false  关闭Vue的生产提示
Vue.config.productionTip = false;

// 创建Vue实例 提供render函数，将App.vue渲染到index.html中
new Vue({
  // el: '#app', 和$mount('#app')作用一致，用于指定管理的容器
  render: (h) => h(App),
  // render:(createElement)=>{
  //   // 基于App创建元素结构,渲染到index页面中
  //   return createElement(App)
  // }
  router,
  pinia,
}).$mount("#app");
