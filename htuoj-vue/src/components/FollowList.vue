<template>
  <div class="content">
    <el-empty
      :image-size="200"
      v-if="tableData === null || tableData.length === 0"></el-empty>
    <el-container v-else style="border: 1px solid #eee">
      <el-main>
        <el-table :data="tableData" height="580px">
          <el-table-column prop="avatar" align="center" label="头像">
            <template slot-scope="scope">
              <!-- <el-img :src="scope.row.avatar" class="avatar" height="50px" /> -->
              <el-image
                style=" height: 50px"
                :src="scope.row.avatar"
                :fit="contain"></el-image>
            </template>
            <!-- <img :src="avatar" class="avatar" width="300px" /> -->
          </el-table-column>
          <el-table-column prop="userName" align="center" label="姓名">
            <template slot-scope="scope">
              <div @click="skipUser(scope.row.userId)" style="cursor: pointer">
                {{ scope.row.userName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="number" align="center" label="学号">
          </el-table-column>
          <el-table-column
            prop="profile"
            align="center"
            label="个人简介"
            :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="profile" align="center" label="关注">
            <template slot-scope="scope">
              <el-button
                size="mini"
                v-if="
                  scope.row.isFollowedByMe == true &&
                  scope.row.isFollowingMe == false
                "
                type="info">
                已关注
              </el-button>
              <el-button
                size="mini"
                v-if="
                  scope.row.isFollowedByMe == false &&
                  scope.row.isFollowingMe == false
                "
                type="success"
                @click="follow(scope.row)">
                关注
              </el-button>
              <el-button
                size="mini"
                v-if="
                  scope.row.isFollowedByMe == true &&
                  scope.row.isFollowingMe == true
                "
                type="info">
                已互关
              </el-button>
              <el-button
                size="mini"
                v-if="
                  scope.row.isFollowedByMe == false &&
                  scope.row.isFollowingMe == true
                "
                type="success"
                @click="follow(scope.row)">
                回关
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-link target="_blank" :underline="false">
              
            </el-link> -->
      </el-main>
    </el-container>
  </div>
</template>

<script>
import api from "@/common/api";
export default {
  data() {
    return {};
  },
  props: {
    tableData: {
      type: Array,
    },
  },
  methods: {
    skipUser(userId) {
      // 根据实际情况生成链接地址
      const url = `/home/stuInfo/${userId}`;
      // this.$router.push("/home/stuInfo/" + userId);
      window.open(url, "_blank");
    },
    follow(row) {
      api.follow({ userId: row.userId }).then((response) => {
        if (response.data.code === "200") {
          row.isFollowedByMe = true;
        }
      });
    },
  },
};
</script>

<style scoped>
/* .el-table-column {
} */
</style>
