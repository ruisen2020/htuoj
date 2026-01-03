<!-- template : 结构 -->

<template>
  <div style="width:100%; height: 100%;  margin: 0; padding: 0; position: absolute;">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <!-- <LoginPage></LoginPage> -->
    <MyHeader></MyHeader>
    <router-view ></router-view>
  </div>
</template>
<!-- script : 行为-->
<script>
// import HelloWorld from './components/HelloWorld.vue'
// import LoginPage from "./components/LoginPage.vue";
import MyHeader from "./MyHeader.vue";
// import Main from "./Main.vue";
export default {
  name: "App",
  components: {
    // HelloWorld,
    // LoginPage,
    MyHeader,
    // Main,
  },
};
</script>
<!-- style : 样式-->
<style scoped>
#app {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  /* color: #2c3e50; */
  /* margin-top: 60px; */
/*  margin: 0;
  padding: 0;
  background-color: #f7f8fa;*/
}
</style>
