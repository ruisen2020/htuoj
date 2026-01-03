<template>
  <div class="article-body">
    <el-container>
      <el-main style="display: flex; justify-content: center">
        <div class="article-info">
          <div class="article-info-content">
            <el-card>
              <div>
                <div style="height: 32px; display: flex; align-items: center">
                  <div style="cursor: pointer" @click="skipUser(articleInfo.userId)">
                    <el-avatar :src="articleInfo.avatar" style="width: 32px; height: 32px"></el-avatar>
                  </div>
                  <div style="
                      font-size: 18px;
                      font-weight: 500;
                      line-height: 32px;
                      margin-left: 10px;
                    ">
                    {{ articleInfo.title }}
                  </div>
                  <div>
                    <span v-if="
                      articleInfo.categoryId == 2 ||
                      articleInfo.categoryId == 4
                    ">
                      <el-button type="warning" size="mini" style="font-size: 14px; margin-left: 20px"
                        @click="skipProblem(articleInfo.problemId)">原题链接</el-button></span>
                  </div>
                  <div style="margin-left: auto" v-if="
                    userInfo != null && userInfo.userId != articleInfo.userId
                  ">
                    <span v-if="articleInfo.isFollow">
                      <el-button type="info" size="mini" plain style="font-size: 14px"
                        @click="cancelFollow(articleInfo.userId)">已关注</el-button></span>
                    <span v-else>
                      <el-button type="primary" size="mini" plain style="font-size: 14px"
                        @click="follow(articleInfo.userId)">关注TA</el-button>
                    </span>
                  </div>
                </div>
                <div style="
                    font-size: 12px;
                    color: #bfbfbf;
                    margin-top: 10px;
                    height: 20px;
                    display: flex;
                    align-items: center;
                    cursor: pointer;
                  " @click="skipUser(articleInfo.userId)">
                  <div>{{ articleInfo.userName }}</div>
                  <div style="margin-left: 10px">
                    发起于 {{ formatDate(new Date(articleInfo.createTime)) }}
                  </div>

                  <div style="margin-left: 10px">
                    最近编辑于
                    {{ formatDate(new Date(articleInfo.updateTime)) }}
                  </div>
                </div>
                <div id="markdown" style="margin-top: 10px"></div>
                <div style="height: 30px; display: flex">
                  <!-- 点赞 -->
                  <div @click="like(articleInfo)" style="
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      cursor: pointer;
                    ">
                    <div>
                      <span v-if="articleInfo.isLike" style="
                          color: #2db55d;
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        ">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em"
                          fill="#2db55d"
                          class="h-4 w-4 text-green-s dark:text-dark-green-s hover:text-green-s dark:hover:text-dark-green-s">
                          <path fill-rule="evenodd"
                            d="M7.04 9.11l3.297-7.419a1 1 0 01.914-.594 3.67 3.67 0 013.67 3.671V7.33h4.028a2.78 2.78 0 012.78 3.2l-1.228 8.01a2.778 2.778 0 01-2.769 2.363H5.019a2.78 2.78 0 01-2.78-2.78V11.89a2.78 2.78 0 012.78-2.78H7.04zm-2.02 2a.78.78 0 00-.781.78v6.232c0 .431.35.78.78.78H6.69V11.11H5.02z"
                            clip-rule="evenodd"></path>
                        </svg>
                      </span>
                      <span v-else style="
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        "><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em"
                          fill="currentColor"
                          class="h-4 w-4 text-gray-6 dark:text-dark-gray-6 hover:text-green-s dark:hover:text-dark-green-s">
                          <path fill-rule="evenodd"
                            d="M7.04 9.11l3.297-7.419a1 1 0 01.914-.594 3.67 3.67 0 013.67 3.671V7.33h4.028a2.78 2.78 0 012.78 3.2l-1.228 8.01a2.778 2.778 0 01-2.769 2.363H5.019a2.78 2.78 0 01-2.78-2.78V11.89a2.78 2.78 0 012.78-2.78H7.04zm-2.02 2a.78.78 0 00-.781.78v6.232c0 .431.35.78.78.78H6.69V11.11H5.02zm12.723 7.793a.781.781 0 00.781-.666l1.228-8.01a.78.78 0 00-.791-.898h-5.04a1 1 0 01-1-1V4.77c0-.712-.444-1.32-1.07-1.56L8.69 10.322v8.58h9.053z"
                            clip-rule="evenodd"></path>
                        </svg>
                      </span>
                    </div>
                    <div :style="{
                      marginLeft: '3px',
                      fontSize: '13px',
                      color: articleInfo.isLike ? '#2DB55D' : '#8c8c8c',
                    }">
                      {{ articleInfo.likeCount }}
                    </div>
                  </div>
                  <!-- 收藏 -->
                  <div @click="collectEvent(articleInfo)" style="
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      cursor: pointer;
                      margin-left: 20px;
                    ">
                    <div>
                      <span v-if="articleInfo.isCollect" style="
                          color: #ffa116;
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        ">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em"
                          fill="currentColor" class="pointer-events-none h-4 w-4">
                          <path fill-rule="evenodd"
                            d="M11.394 2.074a2.5 2.5 0 011.212 0c.723.181 1.185.735 1.526 1.262.342.528.703 1.259 1.131 2.127l.392.795c.302.61.348.667.386.7a.498.498 0 00.086.063c.043.025.11.052.786.15l.877.128c.958.139 1.764.256 2.372.418.606.162 1.276.43 1.671 1.062a2.5 2.5 0 01.375 1.152c.052.744-.333 1.354-.728 1.841-.397.489-.98 1.058-1.674 1.733l-.634.619c-.489.476-.527.537-.548.583a.5.5 0 00-.033.101c-.01.05-.015.122.1.794l.15.873c.164.954.302 1.758.335 2.386.034.627-.014 1.346-.493 1.918-.263.314-.6.558-.98.712-.692.279-1.39.102-1.976-.124-.588-.226-1.309-.605-2.165-1.056l-.785-.412c-.603-.317-.674-.335-.724-.34a.497.497 0 00-.106 0c-.05.005-.12.023-.724.34l-.785.412c-.856.45-1.577.83-2.165 1.056-.585.226-1.284.403-1.976.124a2.5 2.5 0 01-.98-.712c-.48-.572-.527-1.291-.493-1.918.033-.628.171-1.431.335-2.386l.15-.873c.115-.672.11-.745.1-.794a.5.5 0 00-.033-.101c-.02-.046-.06-.107-.548-.583l-.634-.619c-.694-.675-1.277-1.244-1.674-1.733-.395-.487-.78-1.097-.728-1.841a2.5 2.5 0 01.375-1.152c.395-.633 1.065-.9 1.67-1.062.61-.162 1.415-.28 2.373-.418l.877-.128c.675-.098.743-.125.786-.15a.5.5 0 00.086-.062c.038-.034.084-.09.386-.701l.392-.795c.428-.868.789-1.599 1.131-2.127.341-.527.803-1.08 1.526-1.262z"
                            clip-rule="evenodd"></path>
                        </svg>
                      </span>
                      <span v-else style="
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        ">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em"
                          fill="currentColor" class="pointer-events-none h-4 w-4">
                          <path fill-rule="evenodd"
                            d="M11.394 2.074a2.5 2.5 0 011.212 0c.723.181 1.185.735 1.526 1.262.342.528.703 1.259 1.131 2.127l.392.795c.302.61.348.667.386.7a.502.502 0 00.086.063c.043.025.11.052.786.15l.877.128c.958.139 1.764.256 2.372.418.606.162 1.276.43 1.671 1.062a2.5 2.5 0 01.375 1.152c.052.744-.333 1.354-.728 1.841-.397.489-.98 1.058-1.674 1.733l-.634.619c-.489.476-.527.537-.548.583a.506.506 0 00-.033.101c-.01.05-.015.122.1.794l.15.873c.164.954.302 1.758.335 2.386.034.627-.014 1.346-.493 1.918a2.5 2.5 0 01-.98.712c-.692.279-1.39.102-1.976-.124-.588-.226-1.309-.605-2.165-1.056l-.785-.412c-.603-.317-.674-.335-.724-.34a.496.496 0 00-.106 0c-.05.005-.12.023-.724.34l-.785.412c-.856.45-1.577.83-2.165 1.056-.585.226-1.284.403-1.976.124a2.501 2.501 0 01-.98-.712c-.48-.572-.527-1.291-.493-1.918.033-.628.171-1.431.335-2.386l.15-.873c.115-.672.11-.745.1-.794a.5.5 0 00-.033-.101c-.02-.046-.06-.107-.548-.583l-.634-.619c-.694-.675-1.277-1.244-1.674-1.733-.395-.487-.78-1.097-.728-1.841a2.5 2.5 0 01.375-1.152c.395-.633 1.065-.9 1.67-1.062.61-.162 1.415-.28 2.373-.418l.877-.128c.675-.098.743-.125.786-.15a.5.5 0 00.086-.062c.038-.034.084-.09.386-.701l.392-.795c.428-.868.789-1.599 1.131-2.127.341-.527.803-1.08 1.526-1.262zm.493 1.939c-.023.013-.132.089-.34.41-.271.418-.58 1.042-1.045 1.982l-.364.738-.05.103c-.213.434-.428.872-.788 1.197a2.5 2.5 0 01-.43.312c-.42.241-.903.31-1.381.379a52.6 52.6 0 00-.114.016l-.815.119c-1.037.15-1.725.252-2.207.38-.37.099-.476.18-.495.197a.5.5 0 00-.07.216c.005.025.044.153.285.45.314.386.811.874 1.562 1.605l.59.575.082.08c.346.336.697.676.895 1.118.072.162.127.332.164.506.1.474.016.955-.067 1.431l-.02.113-.138.811c-.178 1.033-.294 1.72-.32 2.217-.02.382.023.508.034.532.05.058.113.103.183.133.026.003.16.006.516-.132.465-.18 1.082-.502 2.01-.99l.728-.382.102-.054c.427-.226.859-.454 1.34-.505.177-.02.355-.02.532 0 .481.051.913.28 1.34.505l.102.054.728.383c.928.487 1.545.81 2.01.99.357.137.49.134.516.13a.499.499 0 00.183-.132c.01-.024.055-.15.034-.532-.026-.497-.142-1.184-.32-2.217l-.139-.81-.02-.114c-.082-.476-.166-.957-.066-1.431.037-.174.092-.344.164-.506.198-.442.549-.782.895-1.118a20.8 20.8 0 00.083-.08l.59-.575c.75-.731 1.247-1.219 1.561-1.606.241-.296.28-.424.285-.45a.5.5 0 00-.07-.215c-.02-.017-.126-.098-.495-.196-.482-.129-1.17-.23-2.207-.381l-.815-.119-.113-.016c-.479-.068-.963-.138-1.382-.379a2.5 2.5 0 01-.43-.312c-.36-.325-.575-.763-.788-1.197a31.757 31.757 0 00-.05-.103l-.364-.738c-.464-.94-.774-1.564-1.045-1.982-.208-.321-.317-.397-.34-.41a.5.5 0 00-.226 0zm8.326 6.044v.002-.002zm-3.246 9.575h-.002.002zm-9.934 0h.002-.002zm-3.246-9.575v.002-.002z"
                            clip-rule="evenodd"></path>
                        </svg>
                      </span>
                    </div>
                    <div :style="{
                      marginLeft: '3px',
                      fontSize: '13px',
                      color: articleInfo.isCollect ? '#ffa116' : '#8c8c8c',
                    }">
                      {{ articleInfo.collectCount }}
                    </div>
                  </div>
                  <!-- 删除 -->
                  <el-popconfirm title="您确定删除该文章吗？" v-if="
                    userInfo != null && userInfo.userId === articleInfo.userId
                  " style="
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    " @confirm="deleteArticle(articleInfo.articleId)">
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
                          <svg t="1730115399333" class="icon" viewBox="0 0 1024 1024" version="1.1"
                            xmlns="http://www.w3.org/2000/svg" p-id="4244" width="200" height="200">
                            <path
                              d="M840 288H688v-56c0-40-32-72-72-72h-208C368 160 336 192 336 232V288h-152c-12.8 0-24 11.2-24 24s11.2 24 24 24h656c12.8 0 24-11.2 24-24s-11.2-24-24-24zM384 288v-56c0-12.8 11.2-24 24-24h208c12.8 0 24 11.2 24 24V288H384zM758.4 384c-12.8 0-24 11.2-24 24v363.2c0 24-19.2 44.8-44.8 44.8H332.8c-24 0-44.8-19.2-44.8-44.8V408c0-12.8-11.2-24-24-24s-24 11.2-24 24v363.2c0 51.2 41.6 92.8 92.8 92.8h358.4c51.2 0 92.8-41.6 92.8-92.8V408c-1.6-12.8-12.8-24-25.6-24z"
                              fill="#272636" p-id="4245"></path>
                            <path
                              d="M444.8 744v-336c0-12.8-11.2-24-24-24s-24 11.2-24 24v336c0 12.8 11.2 24 24 24s24-11.2 24-24zM627.2 744v-336c0-12.8-11.2-24-24-24s-24 11.2-24 24v336c0 12.8 11.2 24 24 24s24-11.2 24-24z"
                              fill="#272636" p-id="4246"></path>
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



                  <!-- 编辑 -->
                  <div v-if="
                    userInfo != null && userInfo.userId === articleInfo.userId" @click="updateEvent()" style="
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      cursor: pointer;
                      margin-left: 20px;
                    ">
                    <i style="width: 16px;height: 16px;font-size: 14px;" class="el-icon-edit"></i>
                    <div :style="{
                      marginLeft: '3px',
                      fontSize: '13px',
                      color: '#8c8c8c',
                    }">
                      编辑
                    </div>
                  </div>
                  <!-- 评论 -->
                  <div style="
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      margin-left: auto;
                    ">
                    <el-button size="small" icon="el-icon-edit" type="success"
                      @click="toggleModal(true)">发表评论</el-button>
                  </div>
                </div>
              </div>
            </el-card>
            <div v-for="item in commentList" :key="item.commentId">
              <CommentInfo :commentInfo="item" @like-event="likeEvent"></CommentInfo>
            </div>
            <el-pagination @current-change="handleCurrentChange" :current-page.sync="commentFormData.current" background
              :page-size="10" layout="prev, pager, next" :total="total" style="margin-top: 10px" v-show="total > 10">
            </el-pagination>
          </div>

          <div style="width: 300px" class="Right">
            <el-card style="width: 300px; height: 300px">
              <div>
                <div style="
                    display: flex;
                    color: #fff;
                    border-radius: 10px;
                    line-height: 34px;
                    background-color: rgb(118, 202, 246);
                  ">
                  <span style="font-size: 18px; font-weight: bold; margin: 0 auto">TA的热门文章</span>
                </div>
                <div v-for="(item, index) in articleTopWatchList" :key="item.articleId">
                  <div style="
                      display: flex;
                      justify-content: space-between;
                      margin-top: 18px;
                      cursor: pointer;
                    " @click="skipArticle(item.articleId)">
                    <el-tooltip v-show="true" class="item" effect="dark" :content="item.title" placement="bottom">
                      <div style="
                          width: 200px;
                          height: 20px;
                          white-space: nowrap;
                          overflow: hidden;
                          text-overflow: ellipsis;
                          color: #555666;
                        ">
                        {{ index + 1 }} {{ item.title }}
                      </div>
                    </el-tooltip>

                    <div style="
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        color: #8c8c8c;
                      ">
                      <div style="
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        ">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em" height="1em"
                          fill="currentColor" class="css-kfex1q-Svg ea8ky5j0">
                          <path fill-rule="evenodd"
                            d="M1.104 12.444a1 1 0 010-.888c.13-.26.37-.693.722-1.241A18.85 18.85 0 013.88 7.652C6.184 5.176 8.896 3.667 12 3.667s5.816 1.509 8.119 3.985c.79.85 1.475 1.756 2.055 2.663.352.548.593.98.722 1.24a1 1 0 010 .89c-.13.26-.37.692-.722 1.24a18.848 18.848 0 01-2.055 2.663c-2.303 2.476-5.015 3.985-8.119 3.985s-5.816-1.509-8.119-3.985a18.846 18.846 0 01-2.055-2.663c-.352-.548-.593-.98-.722-1.24zm2.406.162a16.87 16.87 0 001.836 2.38c1.959 2.106 4.19 3.347 6.654 3.347 2.465 0 4.695-1.24 6.654-3.347A16.87 16.87 0 0020.86 12a16.871 16.871 0 00-2.206-2.986C16.695 6.908 14.464 5.667 12 5.667c-2.465 0-4.695 1.24-6.654 3.347A16.87 16.87 0 003.14 12c.108.188.232.391.37.607zM12 15.75c-2.06 0-3.727-1.68-3.727-3.75 0-2.07 1.667-3.75 3.727-3.75 2.06 0 3.727 1.68 3.727 3.75 0 2.07-1.667 3.75-3.727 3.75zm0-2c.952 0 1.727-.782 1.727-1.75s-.775-1.75-1.727-1.75c-.952 0-1.727.782-1.727 1.75s.775 1.75 1.727 1.75z"
                            clip-rule="evenodd"></path>
                        </svg>
                      </div>
                      <div style="margin-left: 3px; font-size: 13px">
                        {{ item.watchCount }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
            <!-- <el-card
              style="
                width: 300px;
                height: 300px;
                background-color: gold;
                margin-top: 20px;
              "
            ></el-card> -->
          </div>
        </div>
      </el-main>
    </el-container>
    <el-dialog :visible.sync="dialogMarkdonwVisible" width="1200px" @open="initializeEditor">
      <div style="display: flex">
        <div style="margin-left: auto; margin-bottom: 10px" @click="addComment">
          <el-button size="small" type="success"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em"
              height="1em" fill="currentColor" class="css-1rhb60f-Svg ea8ky5j0">
              <path fill-rule="evenodd"
                d="M22.707 1.293a1 1 0 01.237 1.037l-7 20a1 1 0 01-1.858.076l-3.844-8.648-8.648-3.844a1 1 0 01.076-1.858l20-7a1 1 0 011.037.237zM12.193 13.22l2.696 6.068 4.72-13.483-7.416 7.416zm6.001-8.83L4.711 9.111l6.067 2.696 7.416-7.416z"
                clip-rule="evenodd"></path>
            </svg>
            <span>发表评论</span>
          </el-button>
        </div>
      </div>
      <div style="height: 500px" id="dialogMarkdown"></div>
    </el-dialog>
    <el-dialog :visible.sync="updateArticleVisible" width="1200px" @open="initializeEditorUpdate">
      <div style="display: flex">
        <div style="margin-left: auto; margin-bottom: 10px" @click="updateArticle(articleInfo.articleId)">
          <el-button size="small" type="success"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1em"
              height="1em" fill="currentColor" class="css-1rhb60f-Svg ea8ky5j0">
              <path fill-rule="evenodd"
                d="M22.707 1.293a1 1 0 01.237 1.037l-7 20a1 1 0 01-1.858.076l-3.844-8.648-8.648-3.844a1 1 0 01.076-1.858l20-7a1 1 0 011.037.237zM12.193 13.22l2.696 6.068 4.72-13.483-7.416 7.416zm6.001-8.83L4.711 9.111l6.067 2.696 7.416-7.416z"
                clip-rule="evenodd"></path>
            </svg>
            <span>确认修改</span>
          </el-button>
        </div>
      </div>
      <div style="height: 500px" id="updateMarkdown"></div>
    </el-dialog>
  </div>
</template>
<script>
import { useUserStore } from "../../public/stores/modules/user";
import { mapState } from "pinia";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import CommentInfo from "@/components/CommentInfo.vue";
import api from "@/common/api";
export default {
  name: "ArticleInfo",
  components: {
    // MarkdownEditor,
    CommentInfo,
  },
  watch: {
    commentText: function (newValue, oldValue) {
      console.log("commentText", newValue, oldValue);
    },
  },
  computed: {
    ...mapState(useUserStore, ["userInfo"]),
    articleId() {
      return this.$route.params.articleId;
    },
  },

  mounted() {
    window.scrollTo(0, 0);
  },
  created() {
    this.fetchData();
  },
  data: function () {
    return {
      articleTopWatchList: null,
      total: 0,
      dialogMarkdonwVisible: false,
      updateArticleVisible: false,
      imageUrl: "",
      articleInfo: "",
      commentList: [],
      cherry: null,
      commentCherry: null,
      updateCherry: null,

      commentText: "",
      commentFormData: {
        commentId: null,
        current: 1,
        size: 10,
        targetType: 1,
        articleId: this.$route.params.articleId,
      },
    };
  },
  methods: {
    updateEvent() {
      this.updateArticleVisible = true;
    },
    updateArticle(articleId) {
      const content = this.updateCherry.getMarkdown();
      api.updateArticle({
        articleId: articleId,
        content: content,
      })
        .then((response) => {
          if (response.data.code === "200") {
            this.$message.success("修改成功");
            this.updateArticleVisible = false;

            window.location.reload();
          }
        });
    },
    deleteArticle(articleId) {
      api.deleteArticle({ articleId: articleId }).then((response) => {
        if (response.data.code == "200") {
          this.$message.success("删除成功");
          this.$router.push("/home/indexInfo");
        }
      });
    },
    skipProblem(problemId) {
      window.open("/problem/" + problemId, "_blank");
    },
    skipUser(userId) {
      window.open("/home/stuInfo/" + userId, "_blank");
    },
    skipArticle(articleId) {
      window.open("/home/articleInfo/" + articleId, "_blank");
    },
    handleCurrentChange() {
      this.fetchData();
    },
    addComment() {
      const content = this.commentCherry.getMarkdown();
      if (content == null || content.trim() == "") {
        this.$message.error("内容不能为空");
        return;
      }
      api
        .addComment({ articleId: this.articleInfo.articleId, content: content })
        .then((response) => {
          if (response.data.code === "200") {
            this.getCommentList();
            this.$message.success("评论成功");
            this.dialogMarkdonwVisible = false;
            this.commentCherry.setMarkdown("");
          }
        });
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
    like(data) {
      if (data.isLike == true) {
        data.likeCount = data.likeCount - 1;
      } else {
        data.likeCount = data.likeCount + 1;
      }
      data.isLike = !data.isLike;
      api.addLike({
        targetId: data.articleId,
        targetType: 0,
      });
    },
    collectEvent(data) {
      if (data.isCollect == true) {
        data.collectCount = data.collectCount - 1;
      } else {
        data.collectCount = data.collectCount + 1;
      }
      data.isCollect = !data.isCollect;
      api.addCollect({
        targetType: 0,
        targetId: data.articleId,
        isCollect: data.isCollect,
      });
    },
    initializeEditorUpdate() {
      this.$nextTick(() => {
        if (this.updateCherry) {
          return;
        }
        this.updateCherry = new Cherry({
          id: "updateMarkdown",
          value: this.articleInfo.content,
          themeSettings: {
            codeBlockTheme: "one dark",
          },
        });
      });
    },
    initializeEditor() {
      this.$nextTick(() => {
        if (this.commentCherry) {
          return;
        }
        this.commentCherry = new Cherry({
          id: "dialogMarkdown",
          value: this.commentText,
          themeSettings: {
            codeBlockTheme: "one dark",
          },
        });
      });
    },
    toggleModal(type) {
      this.dialogMarkdonwVisible = type;
    },
    cancelFollow(userId) {
      api.follow({ userId: userId }).then((response) => {
        if (response.data.code === "200") {
          this.articleInfo.isFollow = false;
        }
      });
    },
    follow(userId) {
      api.follow({ userId: userId }).then((response) => {
        if (response.data.code === "200") {
          this.articleInfo.isFollow = true;
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
    getCommentList() {
      api.getCommentList(this.commentFormData).then((response) => {
        if (response.data.code === "200") {
          this.commentList = response.data.data.records;
          this.total = response.data.data.total;
        }
      });
    },
    fetchData: async function () {
      this.getCommentList();
      await api
        .getArticleById(this.$route.params.articleId)
        .then((response) => {
          this.articleInfo = response.data.data;
          this.imageUrl = response.data.data.avatar;
          const markdownText = response.data.data.content;
          if (this.cherry == null) {
            this.cherry = new Cherry({
              id: "markdown",
              value: markdownText,
              themeSettings: {
                codeBlockTheme: "one dark",
              },
              editor: {
                defaultModel: "previewOnly",
              },
            });
          }
        });
      await api
        .getTopWatchArticleByUserID({
          userId: this.articleInfo.userId,
          size: 6,
        })
        .then((response) => {
          if (response.data.code === "200") {
            this.articleTopWatchList = response.data.data.records;
          }
        });

      // 确保数据渲染完成后滚动到顶部
      this.$nextTick(() => {
        window.scrollTo(0, 0);
      });
    },
  },
};
</script>
<style scoped>
::v-deep .el-dialog {
  border-radius: 20px;
}

::v-deep .cherry {
  box-shadow: none;
}

::v-deep .cherry-markdown h2,
.cherry-markdown .h2 {
  font-size: 20px;
}

::v-deep .cherry-markdown a.anchor:before {
  content: "" !important;
}

::v-deep .el-button {
  border-radius: 10px;
}

::v-deep .cherry-previewer {
  border: none;
  padding: 10px;
}

::v-deep .cherry-drag {
  width: 5px;
  background-color: #ebedee;
}

.article-body {
  width: 100%;
  min-height: 1000px;
  background-color: #f7f8fa;
}

.article-info {
  width: 1200px;

  /* background-color: pink; */
  display: flex;
  justify-content: center;
}

::v-deep .Right>.el-card {
  margin-left: 20px;
}

::v-deep .el-card {
  border-radius: 20px;
}

.article-info-content {
  width: 800px;
}
</style>
