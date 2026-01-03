<template>
  <div class="discuss-content">
    <DiscussMain/>
  </div>
</template>

<script>
import DiscussMain from "@/components/Discuss/DiscussMain.vue";

export default {
  name: "DiscussInfo",
  components: {DiscussMain}
}
</script>

<style scoped>
.discuss-content{
  width: 100%;
  min-height: 100%;
  background-color: #f7f8fa;
  display: flex;
  justify-content: center;
}
</style>