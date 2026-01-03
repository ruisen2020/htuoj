<template>
<div>
    <div class="body">
        <div id="appUserInfo">
            <div class="userMainInfo">
                <div>
                    <span v-if="userInfo != null && userInfo.userId == userId">
                        <el-upload class="avatar-uploader" action="" :http-request="updateAvatar" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                            <el-tooltip content="更新头像" placement="top" effect="light">
                                <!-- <img
                    :src="imageUrl"
                    class="avatar"
                    width="300px"
              
                  /> -->
                                <el-avatar class="avatar" shape="square" :size="150" fit="cover" :src="imageUrl"></el-avatar>
                            </el-tooltip>
                        </el-upload>
                    </span>
                    <span v-else>
                        <img v-if="imageUrl" :src="imageUrl" class="avatar" width="300px" />
                    </span>
                </div>
                <div class="UserSelfInfo">
                    <div style="
                text-align: center;
                font-size: 20px;
                color: #262626;
                font-weight: 600;
              ">
                        {{ userAllInfo.userInfo.userName }}
                    </div>
                    <div style="
                text-align: center;
                font-size: 16px;
                margin-top: 5px;
                color: #3c3c4399;
              ">
                        {{ userAllInfo.userInfo.schoolName }}
                    </div>
                    <div style="
                text-align: center;
                font-size: 16px;
                margin-top: 3px;
                color: #3c3c4399;
              ">
                        {{ userAllInfo.userInfo.number }}
                    </div>
                </div>
                <div class="followInfo" style="
              display: flex;
              justify-content: center;
              align-items: center;
              color: #262626bf;
            ">
                    <div class="follow" @click="toggleModal(false)" style="width: 90px">
                        <div style="text-align: center">关注了</div>
                        <div style="text-align: center; margin-top: 5px">
                            {{ followCount }}
                        </div>
                    </div>
                    <el-divider direction="vertical" content-position="center"></el-divider>
                    <div class="follower" @click="toggleModal(true)" style="width: 90px">
                        <div style="text-align: center">关注者</div>
                        <div style="text-align: center; margin-top: 5px">
                            {{ fanCount }}
                        </div>
                    </div>
                </div>
                <div v-if="userInfo != null && userInfo.userId == userId" @click="changeModalUserInfo()" style="
              width: 268px;
              height: 35px;
              background-color: #2db55d14;
              color: #2db55d;
              display: flex;
              justify-content: center;
              align-items: center;
              border-radius: 0.5rem;
              margin: 0 auto;
              margin-top: 10px;
              cursor: pointer;
            ">
                    编辑个人资料
                </div>
                <div v-else>
                    <div v-if="userAllInfo.userInfo.isFollowedByMe" @click="cancelFollow(userId)" style="
                width: 268px;
                height: 35px;
                background-color: #000a200d;
                color: #262626bf;
                display: flex;
                justify-content: center;
                align-items: center;
                border-radius: 0.5rem;
                margin: 0 auto;
                margin-top: 10px;
                cursor: pointer;
              ">
                        取消关注
                    </div>
                    <div v-else @click="follow(userId)" style="
                width: 268px;
                height: 35px;
                background-color: #2db55d;
                color: #fff;
                display: flex;
                justify-content: center;
                align-items: center;
                border-radius: 0.5rem;
                margin: 0 auto;
                margin-top: 10px;
                cursor: pointer;
              ">
                        关注TA
                    </div>
                </div>

                <div style="
              color: #262626;
              font-weight: 600;
              font-size: 16px;
              margin: 0 auto;
              margin-top: 20px;
              width: 268px;
              height: 22px;
            ">
                    个人简介
                </div>
                <div style="
              width: 268px;
              height: 60px;
              margin: 0 auto;
              margin-top: 20px;
              color: #262626bf;
              font-size: 14px;
            ">
                    {{ userAllInfo.userInfo.profile }}
                </div>
                <div style="width: 268px; display: flex; margin: 0 auto">
                    <div style="width: 134px; height: 120px">
                        <div class="blogInfo" style="height: 20px; display: flex; margin: 0 auto">
                            <div style="font-size: 20px">
                                <img style="width: 20px; height: 20px" src="@/assets/csdn.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="userAllInfo.userPreferencesInfo.blog" target="_blank" :underline="false">博客</el-link>
                            </div>
                        </div>
                        <div style="
                  height: 20px;
                  display: flex;
                  margin: 0 auto;
                  margin-top: 10px;
                ">
                            <div style="">
                                <img style="width: 20px; height: 20px" src="@/assets/github.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="userAllInfo.userPreferencesInfo.github" target="_blank" :underline="false">Github</el-link>
                            </div>
                        </div>
                        <div style="
                  height: 20px;
                  display: flex;
                  margin: 0 auto;
                  margin-top: 10px;
                ">
                            <div style="">
                                <img style="width: 20px; height: 20px" src="@/assets/cf.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="'https://codeforces.com/profile/' +
                    userAllInfo.userPreferencesInfo.codeforcesUserName
                    " target="_blank" :underline="false">Codeforces</el-link>
                            </div>
                        </div>
                        <div style="
                  height: 20px;
                  display: flex;
                  margin: 0 auto;
                  margin-top: 10px;
                ">
                            <div style="">
                                <img style="width: 20px; height: 20px" src="@/assets/nowcoder.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="'https://ac.nowcoder.com/acm/contest/profile/' +
                    userAllInfo.userPreferencesInfo.nowcoderUserId
                    " target="_blank" :underline="false">牛客</el-link>
                            </div>
                        </div>
                    </div>
                    <div style="width: 134px; height: 120px">
                        <div class="blogInfo" style="height: 20px; display: flex; margin: 0 auto">
                            <div style="font-size: 20px">
                                <img style="width: 20px; height: 20px" src="@/assets/leetcode.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="'https://leetcode.cn/u/' +
                    userAllInfo.userPreferencesInfo.leetcodeUserName
                    " target="_blank" :underline="false">力扣</el-link>
                            </div>
                        </div>
                        <div style="
                  height: 20px;
                  display: flex;
                  margin: 0 auto;
                  margin-top: 10px;
                ">
                            <div style="">
                                <img style="width: 20px; height: 20px" src="@/assets/acwing.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="'https://www.acwing.com/user/myspace/index/' +
                    userAllInfo.userPreferencesInfo.acwingUserId
                    " target="_blank" :underline="false">AcWing</el-link>
                            </div>
                        </div>
                        <div style="
                  height: 20px;
                  display: flex;
                  margin: 0 auto;
                  margin-top: 10px;
                ">
                            <div style="">
                                <img style="width: 20px; height: 20px" src="@/assets/luogu.png" />
                            </div>
                            <div style="margin-left: 10px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="'https://www.luogu.com.cn/user/' +
                    userAllInfo.userPreferencesInfo.luoguUserId
                    " target="_blank" :underline="false">洛谷</el-link>
                            </div>
                        </div>
                        <div style="
                  height: 20px;
                  display: flex;
                  margin: 0 auto;
                  margin-top: 10px;
                ">
                            <div style="">
                                <img style="width: 20px; height: 20px" src="@/assets/atcoder.png" />
                            </div>
                            <div style="margin-left: 10px; height: 20px">
                                <el-link style="font-size: 16px; color: #3172cc" :href="'https://atcoder.jp/users/' +
                    userAllInfo.userPreferencesInfo.atcoderUserName
                    " target="_blank" :underline="false">AtCoder</el-link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <el-dialog class="dialog-box" title="编辑个人资料" :visible.sync="isModalVisibleUserInfo" width="740px">
                <div style="align-items: center">
                    <el-form :model="userAllInfo" label-width="120px">
                        <el-form-item label="用户名">
                            <el-input v-model="userAllInfo.userInfo.userName" placeholder="请输入用户名"></el-input>
                        </el-form-item>
                        <el-form-item label="真实姓名">
                            <el-input v-model="userAllInfo.userInfo.realName" placeholder="请输入真实姓名(该项仅要求河师大学生)"></el-input>
                        </el-form-item>
                        <el-form-item label="学校">
                            <el-select v-model="userAllInfo.userInfo.schoolName" @change="handleSelectChange" filterable remote reserve-keyword placeholder="请输入学校名称" :remote-method="remoteMethod" :loading="loading">
                                <el-option v-for="item in schoolOptions" :key="item.value" :label="item.label" :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="个人简介">
                            <el-input v-model="userAllInfo.userInfo.profile" placeholder="请输入个人简介"></el-input>
                        </el-form-item>
                        <el-form-item label="codeforces">
                            <el-input placeholder="codeforces 用户名(codeforces个人主页的URL地址中最后一串字母)" v-model="userAllInfo.userPreferencesInfo.codeforcesUserName
                  "></el-input>
                        </el-form-item>
                        <el-form-item label="牛客">
                            <el-input placeholder="牛客 用户ID(牛客个人主页的URL地址中最后一串数字)" v-model="userAllInfo.userPreferencesInfo.nowcoderUserId
                  "></el-input>
                        </el-form-item>
                        <el-form-item label="力扣">
                            <el-input placeholder="力扣 用户名(力扣个人主页的URL地址中最后一串字母)" v-model="userAllInfo.userPreferencesInfo.leetcodeUserName
                  "></el-input>
                        </el-form-item>
                        <el-form-item label="洛谷">
                            <el-input placeholder="洛谷 用户ID(洛谷个人主页的URL地址中最后一串数字)" v-model="userAllInfo.userPreferencesInfo.luoguUserId
                  "></el-input>
                        </el-form-item>
                        <el-form-item label="AcWing">
                            <el-input placeholder="AcWing 用户ID(AcWing个人主页的URL地址中最后一串数字)" v-model="userAllInfo.userPreferencesInfo.acwingUserId
                  "></el-input>
                        </el-form-item>
                        <el-form-item label="AtCoder">
                            <el-input placeholder="AtCoder 用户名(AtCoder个人主页的URL地址中最后一串字母)" v-model="userAllInfo.userPreferencesInfo.atcoderUserName
                  "></el-input>
                        </el-form-item>
                        <el-form-item label="博客">
                            <el-input placeholder="你的博客地址(可以是CSDN,也可以是个人网站等)" v-model="userAllInfo.userPreferencesInfo.blog"></el-input>
                        </el-form-item>
                        <el-form-item label="github">
                            <el-input placeholder="你的代码仓库地址(github、gitee等)" v-model="userAllInfo.userPreferencesInfo.github"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
                <div style="text-align: right; margin: 0">
                    <el-button type="text" @click="isModalVisibleUserInfo = false">取消</el-button>
                    <el-button type="primary" @click="updateUserAllInfo()">确定</el-button>
                </div>
            </el-dialog>
            <el-dialog title="" :visible.sync="isModalVisible" width="840px">
                <div class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="modal-follow" @click="changeSelect(false)" :class="{ selectedClass: selected }">
                                关注了 {{ followCount }}
                            </div>
                            <el-divider direction="vertical" content-position="center"></el-divider>
                            <div class="modal-follower" @click="changeSelect(true)" :class="{ selectedClass: !selected }">
                                关注者 {{ fanCount }}
                            </div>
                        </div>
                    </div>
                    <FollowList :tableData="tableData" />
                </div>
            </el-dialog>
            <div class="userOtherInfo">
                <!-- <div class="userAwardInfo">
            <AwardList :userInfo="userAllInfo.userInfo"></AwardList>
          </div> -->
                <div class="userProblemAndRatingInfo">
                    <div class="userProblemCountInfo">
                        <ProblemCount :userPreferencesInfo="userAllInfo.userPreferencesInfo
                "></ProblemCount>
                    </div>
                    <div class="userRatingInfo">
                        <RatingInfo :userPreferencesInfo="userAllInfo.userPreferencesInfo
                "></RatingInfo>
                    </div>
                </div>
                <div class="userInfoCategoryList" style="border-radius: 13px">
                    <UserInfoCategoryList :userId="userId"></UserInfoCategoryList>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import FollowList from "../components/FollowList.vue";
// import AwardList from "@/components/category/AwardList.vue";
import ProblemCount from "@/components/ProblemCount.vue";
import RatingInfo from "@/components/RatingInfo.vue";
import UserInfoCategoryList from "@/components/category/UserInfoCategoryList.vue";
import api from "@/common/api";
import {
    useUserStore
} from "../../public/stores/modules/user";
import {
    mapState,
    mapActions
} from "pinia";
export default {
    components: {
        UserInfoCategoryList,
        RatingInfo,
        FollowList,
        // AwardList,
        ProblemCount,
    },
    computed: {
        ...mapState(useUserStore, ["userInfo"]),
    },
    data() {
        return {
            schoolOptions: [],
            loading: false,
            userId: this.$route.params.userId,
            visible: true,
            // true : 选择粉丝，false：选择关注者
            selected: true,
            imageUrl: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
            followCount: null,
            fanCount: null,
            isModalVisible: false,
            isModalVisibleUserInfo: false,
            tableData: null,
            followList: null,
            fansList: null,
            userAllInfo: {
                userPreferencesInfo: null,
                userInfo: null
            },
        };
    },
    methods: {
        handleSelectChange(value) {
            this.userAllInfo.userInfo.schoolId = value;
        },
        remoteMethod(query) {
            if (query !== '') {
                this.loading = true;
                setTimeout(() => {
                    this.loading = false;
                    this.schoolOptions = [];
                    api.getSchoolList({
                        schoolName: query
                    }).then((response) => {
                        if (response.data.code === "200") {
                            for (let i = 0; i < response.data.data.length; i++) {
                                this.schoolOptions.push({
                                    value: response.data.data[i].schoolId,
                                    label: response.data.data[i].schoolName,
                                });
                            }
                        }
                    });
                    // this.options = this.list.filter(item => {
                    //   return item.label.toLowerCase()
                    //     .indexOf(query.toLowerCase()) > -1;
                    // });
                }, 200);
            } else {
                this.schoolOptions = [];
            }
        },
        ...mapActions(useUserStore, ["setUserInfo"]),
        follow(userId) {
            api.follow({
                userId: userId
            }).then((response) => {
                if (response.data.code === "200") {
                    this.$message({
                        message: "关注成功",
                        type: "success",
                    });
                    this.userAllInfo.userInfo.isFollowedByMe = true;
                }
            });
        },
        cancelFollow(userId) {
            api.follow({
                userId: userId
            }).then((response) => {
                if (response.data.code === "200") {
                    this.$message({
                        message: "取关成功",
                        type: "success",
                    });
                    this.userAllInfo.userInfo.isFollowedByMe = false;
                }
            });
        },
        updateAvatar(options) {
            const {
                file
            } = options;
            // 创建 FormData 对象
            const formData = new FormData();
            formData.append("file", file);
            api.updateAvatar(formData).then((response) => {
                if (response.data.code === "200") {
                    this.$message({
                        message: "上传成功",
                        type: "success",
                    });
                    this.imageUrl = response.data.data;
                    this.userInfo.avatar = response.data.data;
                    this.setUserInfo(this.userInfo);
                }
            });
        },
        updateUserAllInfo() {

            api.updateUserAllInfo(this.userAllInfo).then((response) => {
                if (response.data.code === "200") {
                    this.changeModalUserInfo();
                    this.fetchData();
                    this.getUserPreferencesInfo();
                }
            });
        },
        changeModalUserInfo() {
            this.isModalVisibleUserInfo = !this.isModalVisibleUserInfo;
        },
        async getUserPreferencesInfo() {
            api.getUserPreferencesInfo(this.userId).then((response) => {
                if (response.data.code === "200") {
                    this.userAllInfo.userPreferencesInfo = response.data.data;
                }
            });
        },
        changeSelect(type) {
            if (type === false) {
                this.selected = false;
                this.tableData = this.followList;
            } else {
                this.selected = true;
                this.tableData = this.fansList;
            }
        },

        async toggleModal(type) {
            if (type === false) {
                this.selected = false;
            } else {
                this.selected = true;
            }
            if (this.followList == null || this.fansList == null) {
                await api.getFollowInfo(this.userId).then((response) => {
                    this.followList = response.data.data.followList;
                    this.fansList = response.data.data.fansList;
                });
            }
            if (type == false) {
                this.tableData = this.followList;
            } else {
                this.tableData = this.fansList;
            }
            this.isModalVisible = !this.isModalVisible;
        },
        handleAvatarSuccess(res, file) {
            console.log(file);
            this.imageUrl = res.data;
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === "image/jpeg";
            const isPNG = file.type === "image/png";
            const isLt2M = file.size / 1024 / 1024 < 10;

            if (!isJPG && !isPNG) {
                this.$message.error("上传头像图片只能是 JPG 或者 PNG 格式!");
            }
            if (!isLt2M) {
                this.$message.error("上传头像图片大小不能超过 10MB!");
            }
            return (isJPG || isPNG) && isLt2M;
        },
        fetchData: function () {
            api.getUserInfo(this.userId).then((response) => {
                this.userAllInfo.userInfo = response.data.data;
                this.imageUrl = response.data.data.avatar;
            });
        },
        getFllowCount: function () {
            api.getFollowCount(this.userId).then((response) => {
                this.followCount = response.data.data.followCount;
                this.fanCount = response.data.data.fanCount;
            });
        },
    },
    created() {
        this.fetchData();
        this.getFllowCount();
        this.getUserPreferencesInfo();
    },
};
</script>

<style scoped>
::v-deep .el-dialog {
    border-radius: 20px;
}

/* ::v-deep .el-input__inner::placeholder {
  font: 1em sans-serif;
  font-family: Arial, serif; 
  color: rgb(128, 128, 128);
} */

::v-deep .dialog-box>.el-input__inner {
    /* color: black; */
    /* background-color: rgba(24, 23, 23, 0.1) !important; */
    width: 550px;
    height: 50px;
    border-radius: 10px;
    font-size: 16px;
}

.userInfoCategoryList {
    width: 850px;
    /* height: 800px; */
    background-color: rgb(255 255 255);
    margin-top: 20px;
}

.followInfo>.el-divider--vertical {
    height: 30px;
}

.selectedClass {
    /* color: #262626bf; */
    color: #3c3c434d;
}

.modal {
    /* position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%; */
    /* display: flex;
  justify-content: center;
  align-items: center; */
    color: #262626bf;
    width: 800px;
    height: 640px;
}

.modal-header {
    margin-bottom: 20px;
    font-size: 16px;
    /* width: 840px;
  height: 42px; */
    display: flex;
    justify-content: center;
    align-items: center;
}

/* .modal-header-content {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
} */
.modal-follow {
    display: inline-block;
    cursor: pointer;
}

.modal-follower {
    display: inline-block;
    cursor: pointer;
}

/* .modal-content {
  position: relative;
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  border: 2px solid #ccc;
} */

.body {
    background-color: #f7f8fa;
    width: 100%;
    height: 1500px;
}

#appUserInfo {
    margin: 0 auto;
    width: 1200px;
    padding-top: 30px;
    display: flex;
}

.userOtherInfo {
    width: 860px;
    height: 1000px;
    margin-left: 20px;
}

.userAwardInfo {
    width: 850px;
    height: 200px;
}

.userProblemAndRatingInfo {
    width: 850px;
    height: 180px;

    display: flex;
}

.userProblemCountInfo {
    width: 560px;
    height: 180px;

    /* margin-top: 10px; */
}

.userRatingInfo {
    width: 280px;
    height: 180px;

    margin-left: 10px;
    /* margin-top: 10px; */
}

.userMainInfo {
    /* padding-top: 200px; */

    background-color: rgb(255 255 255);
    width: 300px;
    height: 1000px;
    border-radius: 8px;
}

.followInfo {
    font-size: 14px;
    display: flex;
    width: 300px;
    height: 42px;
    margin-top: 10px;
}

.follow {
    width: 50px;

    /* margin-left: 90px; */
    /* background-color: pink; */
    display: inline-block;
    cursor: pointer;
}

.follower {
    width: 50px;
    /* margin-left: 5px; */
    /* background-color: greenyellow; */
    display: inline-block;
    cursor: pointer;
}

.avatar {
    margin-left: 75px;
    margin-top: 30px;
    width: 150px;
    border-radius: 10px;
}
</style>
