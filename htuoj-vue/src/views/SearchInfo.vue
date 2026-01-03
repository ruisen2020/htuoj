<template>
  <div>
    <div style="margin: 0 auto;width: 1200px;">
     
      <div style="width: 840px;margin: 0 auto;">
        <SearchTab :searchContent="search" />
      </div>
    </div>
  </div>
</template>

<script>
import SearchTab from '@/components/Search/SearchTab.vue';

export default {
  components: {
    SearchTab,
  },
  data() {
    return {
      search: this.$route.params.searchContent ,
    };
  },
  methods:{
    searchArticle(){
      console.log(this.search);                         
      
      if (this.$refs.searchTab && this.$refs.searchTab.fetchData) {
        this.$refs.searchTab.fetchData();
      } else {
        console.error('fetchData method not found on SearchTab component');
      }
    }
  }

}
</script>

<style scoped>
::v-deep .el-input__inner {
  border: 1px solid #ebeef5;
  border-radius: 20px;
  height: 48px;
}

.search {

  margin: 0 auto;
  margin-top: 30px;
  border: none;
  /* margin-bottom: 10px; */
  /* display: flex; */
  /* border: 1px solid #ebeef5; */
  width: 600px;
  height: 48px;
  /* height: 40px; */
  /* background-color: pink; */
  align-items: center;
  justify-content: center;
  font-size: 14px;
  /* line-height: 24px; */
}
</style>