<template>
  <el-container class="content">
    <el-aside width="150px" class="aside">
      <el-menu default-active="1"
               text-color="#909399"
               active-text-color="#303133"
               style="height: 100%"
      >
        <el-menu-item index="1">
          <svg t="1730039974086" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1001" width="14" height="14">
            <path d="M288.256 216.064a51.2 51.2 0 0 0-72.192 8.192l-204.8 256a51.2 51.2 0 0 0 0 64l204.8 256a51.2 51.2 0 0 0 79.872-64L116.736 512l179.2-224.256a51.2 51.2 0 0 0-7.68-71.68zM1012.736 480.256l-204.8-256a51.2 51.2 0 0 0-79.872 64l179.2 223.744-179.2 224.256a51.2 51.2 0 0 0 79.872 64l204.8-256a51.2 51.2 0 0 0 0-64zM599.552 102.4a51.2 51.2 0 0 0-60.928 39.424l-153.6 716.8A51.2 51.2 0 0 0 424.448 921.6h10.752a51.2 51.2 0 0 0 51.2-40.448l153.6-716.8A51.2 51.2 0 0 0 599.552 102.4z" p-id="1002"></path>
          </svg>
          <span slot="title" style="margin-left: 5px">代码编辑器</span>
        </el-menu-item>
        <el-menu-item index="2">

        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main class="main">
      <div style="display: flex;justify-content: space-between;">
        <span>代码风格</span>
        <el-dropdown trigger="click" @command="handleChange">
          <div class="box">
            <span class="el-dropdown-link">{{ codeStyle }}</span>
            <i class="el-icon-arrow-down el-icon--right"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="style in styles" :key="style" :command="style">
              {{style}}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import eventBus from "@/assets/js/eventBus";

export default {
  name: "CodeStyle",
  props:{
  },
  data(){
    return {
      // 风格列表
      styles:['idea','3024-day','ayu-mirage','monokai','rubyblue'],
      codeStyle:'idea'
    }
  },
  methods:{
    handleChange(val) {
      this.codeStyle = val;
      eventBus.$emit("change-style",val);
    }
  },
  mounted() {
    eventBus.$on("curr-style",(val)=>{
      this.codeStyle = val
    });
  }
}
</script>

<style scoped>
.content{
  height: 450px;
}
.aside{

}
.main{

}
.el-dropdown-link {
  color: #909399;
  font-weight: bold;
}
.el-dropdown-link:hover{
  cursor: pointer;
}
.box{
  background-color: #DCDFE6;
  width: 100px;
  height: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 8px;
  padding: 5px;
}
</style>