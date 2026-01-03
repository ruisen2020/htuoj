<template>
<div>
    <el-dialog class="dialog-container-comment" :visible="dialogMarkdonwVisible" width="1200px" @open="initializeEditor" @close="handleDialogClose">
        <div style="display: flex">
            <div style="margin-left: auto; margin-bottom: 10px" @click="addComment">
                <el-button size="small" type="success"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em" fill="currentColor" class="css-1rhb60f-Svg ea8ky5j0">
                        <path fill-rule="evenodd" d="M22.707 1.293a1 1 0 01.237 1.037l-7 20a1 1 0 01-1.858.076l-3.844-8.648-8.648-3.844a1 1 0 01.076-1.858l20-7a1 1 0 011.037.237zM12.193 13.22l2.696 6.068 4.72-13.483-7.416 7.416zm6.001-8.83L4.711 9.111l6.067 2.696 7.416-7.416z" clip-rule="evenodd"></path>
                    </svg>
                    <span>发表评论</span>
                </el-button>
            </div>
        </div>
        <div style="height: 500px" :id="dialogMarkdownId"></div>
    </el-dialog>
</div>
</template>

<script>
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import api from "@/common/api";
export default {
    props: {
        commentInfo: Object,
        dialogMarkdonwVisible: Boolean,
    },
    data() {
        return {
            dialogMarkdownId: "childdialogMarkdownId" + this.commentInfo.commentId,
            commentCherry: null,
        };
    },
    methods: {
        handleDialogClose() {
            if (this.dialogMarkdonwVisible == false) return;

            this.$emit("toggleModal-event");
        },
        initializeEditor() {
            this.$nextTick(() => {
                if (this.commentCherry) {
                    return;
                }
                this.commentCherry = new Cherry({
                    id: this.dialogMarkdownId,
                    value: this.commentText,

                    themeSettings: {
                        codeBlockTheme: "one dark",
                    },
                });
            });
        },
        addComment() {
            const content = this.commentCherry.getMarkdown();
            if (content == null || content.trim() == "") {
                this.$message.error("内容不能为空");
                return;
            }

            api
                .addComment({
                    articleId: this.commentInfo.articleId,
                    content: content,
                    parentId: this.commentInfo.commentId,
                })
                .then((response) => {
                    if (response.data.code === "200") {
                        this.$message.success("评论成功");
                        this.$emit("toggleModal-event");
                        this.commentCherry.setMarkdown("");
                    }
                });
        },
    },
};
</script>

<style scoped>
::v-deep .dialog-container-comment>.cherry.theme__light .cherry-previewer {
    background-color: none;
}
</style>
