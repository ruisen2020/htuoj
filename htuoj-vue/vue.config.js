const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    client: {
      overlay: false,
    },
    // proxy: {
    //   "/": {
    //     target: "http://localhost:9001",
    //     changeOrigin: true,
    //     pathRewrite: {
    //       "^/": "",
    //     },
    //   },
    // },
  },
});
