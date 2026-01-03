<template>
  <CollapseTransition>
    <div class="comment" v-show="showComment">
      <el-avatar :size="size"/>
      <el-form style="flex:1; margin-left: 10px;" :model="formData">
        <el-form-item>
          <el-input v-model="formData.comment" type="textarea" resize="none" :rows="3" show-word-limit maxlength="200"/>
          <div class="operative">
            <div>
              <CommentImage/>
            </div>
            <el-upload name="file"
                       :show-file-list="false"
                       accept=".png, .PNG, .jpg, .JPG, .jpeg, .JPEG, .gif, .GIF, .bmp, .BMP"
                       :multiple="false"
                       :http-request="selectImg"
                       action=""
            >
              <el-icon class="el-icon-picture" style="color: #909399"/>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
      <el-button size="medium" type="primary" style="width: 65px; height: 75px; margin-left: 10px; text-align: center" @click="submit">发布</el-button>
    </div>
  </CollapseTransition>
</template>

<script>
import CollapseTransition from 'element-ui/lib/transitions/collapse-transition';
import CommentImage from "@/components/Discuss/CommentImage.vue";
export default {
  name: "PostComment",
  components:{CommentImage, CollapseTransition},
  props:{
    // 评论层级
    level:{
      type:Number,
      required:true
    },
    showComment:{
      type:Boolean,
      required:true
    },
    size:{
      type:String,
      required:false
    },
  },
  data(){
    return {
      formData: {
        comment: '',
      }
    }
  },
  methods:{
    selectImg(){

    },
    submit(){
      this.$emit("level-submit",this.formData)
      // 一级评论和二级评论传给后端
    }
  }
}
</script>

<style scoped>
.comment{
  margin-top: 20px;
  display: flex;
}
</style>