<template>
<div class="commentApp">
    <!-- 评论 -->
    <el-card style="margin-top: 10px">
        <div>
            <div style="height: 24px; display: flex; align-items: center">
                <div style="cursor: pointer" @click="skipUser(commentInfo.userId)">
                    <el-avatar :src="commentInfo.avatar" style="width: 24px; height: 24px"></el-avatar>
                </div>
                <div style="
              font-size: 16px;
              font-weight: 500;
              color: #8c8c8c;
              line-height: 24px;
              margin-left: 10px;
              cursor: pointer;
            " @click="skipUser(commentInfo.userId)">
                    {{ commentInfo.userName }}
                </div>

                <div style="margin-left: auto; font-size: 14px; color: #bfbfbf">
                    {{ formatDate(new Date(commentInfo.createTime)) }}
                </div>
            </div>
            <div :id="childMarkdownId" style="margin-top: 10px"></div>
            <div style="height: 30px; display: flex">
                <!-- 点赞 -->
                <div style="
              display: flex;
              justify-content: center;
              align-items: center;
              cursor: pointer;
            " @click="like(commentInfo)">
                    <div>
                        <span v-if="commentInfo.isLike" style="
                  color: #2db55d;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                ">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em" fill="#2db55d" class="h-4 w-4 text-green-s dark:text-dark-green-s hover:text-green-s dark:hover:text-dark-green-s">
                                <path fill-rule="evenodd" d="M7.04 9.11l3.297-7.419a1 1 0 01.914-.594 3.67 3.67 0 013.67 3.671V7.33h4.028a2.78 2.78 0 012.78 3.2l-1.228 8.01a2.778 2.778 0 01-2.769 2.363H5.019a2.78 2.78 0 01-2.78-2.78V11.89a2.78 2.78 0 012.78-2.78H7.04zm-2.02 2a.78.78 0 00-.781.78v6.232c0 .431.35.78.78.78H6.69V11.11H5.02z" clip-rule="evenodd"></path>
                            </svg>
                        </span>
                        <span v-else style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em" fill="currentColor" class="h-4 w-4 text-gray-6 dark:text-dark-gray-6 hover:text-green-s dark:hover:text-dark-green-s">
                                <path fill-rule="evenodd" d="M7.04 9.11l3.297-7.419a1 1 0 01.914-.594 3.67 3.67 0 013.67 3.671V7.33h4.028a2.78 2.78 0 012.78 3.2l-1.228 8.01a2.778 2.778 0 01-2.769 2.363H5.019a2.78 2.78 0 01-2.78-2.78V11.89a2.78 2.78 0 012.78-2.78H7.04zm-2.02 2a.78.78 0 00-.781.78v6.232c0 .431.35.78.78.78H6.69V11.11H5.02zm12.723 7.793a.781.781 0 00.781-.666l1.228-8.01a.78.78 0 00-.791-.898h-5.04a1 1 0 01-1-1V4.77c0-.712-.444-1.32-1.07-1.56L8.69 10.322v8.58h9.053z" clip-rule="evenodd"></path>
                            </svg>
                        </span>
                    </div>
                    <div :style="{
                marginLeft: '3px',
                fontSize: '13px',
                color: commentInfo.isLike ? '#2DB55D' : '#8c8c8c',
              }">
                        {{ commentInfo.likeCount }}
                    </div>
                </div>
                <!-- 回复 -->
                <div style="
              display: flex;
              justify-content: center;
              align-items: center;
              cursor: pointer;
              margin-left: 20px;
            " @click="clickOpenComment">
                    <div>
                        <span v-if="!openComment" style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                ">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em" fill="currentColor" class="css-1rhb60f-Svg ea8ky5j0">
                                <path fill-rule="evenodd" d="M11.997 21.5a9.5 9.5 0 01-8.49-5.251A9.38 9.38 0 012.5 11.997V11.5c.267-4.88 4.12-8.733 8.945-8.999L12 2.5a9.378 9.378 0 014.25 1.007A9.498 9.498 0 0121.5 12a9.378 9.378 0 01-.856 3.937l.838 4.376a1 1 0 01-1.17 1.17l-4.376-.838a9.381 9.381 0 01-3.939.856zm3.99-2.882l3.254.623-.623-3.253a1 1 0 01.09-.64 7.381 7.381 0 00.792-3.346 7.5 7.5 0 00-4.147-6.708 7.385 7.385 0 00-3.35-.794H11.5c-3.752.208-6.792 3.248-7.002 7.055L4.5 12a7.387 7.387 0 00.794 3.353A7.5 7.5 0 0012 19.5a7.384 7.384 0 003.349-.793 1 1 0 01.639-.09z" clip-rule="evenodd"></path>
                            </svg>
                        </span>
                        <span v-else style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                ">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em" fill="currentColor" class="css-1rhb60f-Svg ea8ky5j0">
                                <path fill-rule="evenodd" d="M17.707 11.707a1 1 0 01-1.414 0L12 7.414l-4.293 4.293a1 1 0 01-1.414-1.414l5-5a1 1 0 011.414 0l5 5a1 1 0 010 1.414zm-1.414 7L12 14.414l-4.293 4.293a1 1 0 01-1.414-1.414l5-5a1 1 0 011.414 0l5 5a1 1 0 01-1.414 1.414z" clip-rule="evenodd"></path>
                            </svg>
                        </span>
                    </div>
                    <div :style="{
                marginLeft: '3px',
                fontSize: '13px',
                color: '#8c8c8c',
              }">
                        回复 {{ commentInfo.childCount }}
                    </div>
                </div>
                <!-- 删除 -->
                <el-popconfirm title="您确定删除该评论吗？" v-if="userInfo != null && userInfo.userId === commentInfo.userId" style="display: flex; justify-content: center; align-items: center" @confirm="deleteComment(commentInfo.commentId)">
                    <div slot="reference" style="
                display: flex;
                justify-content: center;
                align-items: center;
                cursor: pointer;
                margin-left: 20px;
              ">
                        <div>
                            <span style="
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 16px;
                    height: 16px;
                  ">
                                <svg t="1730115399333" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4244" width="200" height="200">
                                    <path d="M840 288H688v-56c0-40-32-72-72-72h-208C368 160 336 192 336 232V288h-152c-12.8 0-24 11.2-24 24s11.2 24 24 24h656c12.8 0 24-11.2 24-24s-11.2-24-24-24zM384 288v-56c0-12.8 11.2-24 24-24h208c12.8 0 24 11.2 24 24V288H384zM758.4 384c-12.8 0-24 11.2-24 24v363.2c0 24-19.2 44.8-44.8 44.8H332.8c-24 0-44.8-19.2-44.8-44.8V408c0-12.8-11.2-24-24-24s-24 11.2-24 24v363.2c0 51.2 41.6 92.8 92.8 92.8h358.4c51.2 0 92.8-41.6 92.8-92.8V408c-1.6-12.8-12.8-24-25.6-24z" fill="#272636" p-id="4245"></path>
                                    <path d="M444.8 744v-336c0-12.8-11.2-24-24-24s-24 11.2-24 24v336c0 12.8 11.2 24 24 24s24-11.2 24-24zM627.2 744v-336c0-12.8-11.2-24-24-24s-24 11.2-24 24v336c0 12.8 11.2 24 24 24s24-11.2 24-24z" fill="#272636" p-id="4246"></path>
                                </svg>
                            </span>
                        </div>
                        <div :style="{
                  marginLeft: '3px',
                  fontSize: '13px',
                  color: '#8c8c8c',
                }">
                            删除
                        </div>
                    </div>
                </el-popconfirm>
                <div style="
              display: flex;
              justify-content: center;
              align-items: center;
              margin-left: auto;
            ">
                    <el-button size="mini" icon="el-icon-edit" @click="toggleModal">回复评论</el-button>
                </div>
            </div>
        </div>
        <div v-show="openComment">
            <div v-for="item in commentChildList" :key="item.commentId">
                <CommentChild @like-event="likeEvent" :commentInfo="item" :parentCommentId="commentInfo.commentId"></CommentChild>
            </div>
        </div>
    </el-card>
    <DialogMarkdown :commentInfo="commentInfo" :dialogMarkdonwVisible="dialogMarkdonwVisible" @toggleModal-event="toggleModal"></DialogMarkdown>
</div>
</template>

<script>
import {
    useUserStore
} from "../../public/stores/modules/user";
import {
    mapState
} from "pinia";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import api from "@/common/api";
import CommentChild from "../components/CommentChild.vue";
import DialogMarkdown from "../components/DialogMarkdown.vue";
export default {
    name: "CommentInfo11",
    props: {
        commentInfo: Object,
    },
    components: {
        CommentChild,
        DialogMarkdown
    },
    computed: {
        ...mapState(useUserStore, ["userInfo"]),
    },
    data() {
        return {
            dialogMarkdonwVisible: false,
            childMarkdownId: "child" + this.commentInfo.commentId,
            openComment: false,
            commentChildList: null,
            commentCherry: null,
            commentFormData: {
                commentId: this.commentInfo.commentId,
                current: 1,
                size: 5,
                articleId: this.commentInfo.articleId,
                targetType: 1,
            },
        };
    },
    created() {},
    mounted() {
        new Cherry({
            id: this.childMarkdownId,
            value: this.commentInfo.content,
            themeSettings: {
                codeBlockTheme: "one dark",
            },
            editor: {
                defaultModel: "previewOnly",
            },
        });
    },
    methods: {
        skipUser(userId) {
            window.open("/home/stuInfo/" + userId, "_blank");
        },
        deleteComment(commentId) {
            api
                .deleteComment({
                    commentId: commentId,
                })
                .then((response) => {
                    if (response.data.code == "200") {
                        this.$message.success("删除成功");
                        // 刷新页面
                        window.location.reload();
                    }
                });
        },
        clickOpenComment() {
            this.openComment = !this.openComment;
            if (this.commentChildList == null) {
                this.fetchData();
            }
        },
        likeEvent(data) {
            if (data.isLike == true) {
                data.likeCount = data.likeCount - 1;
            } else {
                data.likeCount = data.likeCount + 1;
            }
            data.isLike = !data.isLike;
            api.addLike({
                targetId: data.commentId,
                targetType: 1,
            });
        },
        toggleModal() {
            this.dialogMarkdonwVisible = !this.dialogMarkdonwVisible;
        },
        like(data) {
            this.$emit("like-event", data);
        },
        fetchData() {
            api.getCommentList(this.commentFormData).then((response) => {
                if (response.data.code === "200") {
                    this.commentChildList = response.data.data.records;
                }
            });
        },
        formatDate(date) {
            const year = date.getFullYear().toString().slice(-2); // 获取年份的后两位
            const month = (date.getMonth() + 1).toString().padStart(2, "0"); // 月份从0开始，需要加1
            const day = date.getDate().toString().padStart(2, "0");
            const hours = date.getHours().toString().padStart(2, "0");
            const minutes = date.getMinutes().toString().padStart(2, "0");
            const seconds = date.getSeconds().toString().padStart(2, "0");

            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        },
    },
};
</script>

<style></style>
