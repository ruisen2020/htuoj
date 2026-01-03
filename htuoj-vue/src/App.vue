<template>
  <div id="app">
    <router-view />
    <!-- <MyIcp /> -->
  </div>
</template>

<script>
// import MyIcp from "./views/MyIcp";

export default {
  name: "App",
  // components: {
  //   MyIcp,
  // },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  em {
            color: red;
            font-style: normal;
        }
}

</style>
