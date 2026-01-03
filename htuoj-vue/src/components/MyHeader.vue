<template>
  <el-header class="elHeader" style="height: 60px">
    <el-menu class="menu-custom" :default-active="activeIndex" mode="horizontal" style="height: 60px" router>
      <el-menu-item @click="GotoHome">
        <img src="../assets/logo.png" style="height: 50px" />
      </el-menu-item>
      <el-menu-item index="/home/indexInfo">首页</el-menu-item>
      <el-menu-item index="/home/problemInfo">题库</el-menu-item>
      <el-menu-item index="/home/trainingInfo">题单</el-menu-item>
      <el-menu-item index="/home/contest">竞赛</el-menu-item>
      <el-menu-item index="/home/awardInfo"> 获奖 </el-menu-item>
      <el-menu-item index="/home/memberInfo"> 成员 </el-menu-item>
      <el-menu-item index="/home/chat"> 聊天 </el-menu-item>
      <el-menu-item index="/home/LLM"> 大模型 </el-menu-item>
      <el-menu-item class="avatar" v-if="userInfo == null" @click="login()">
        登录 / 注册
      </el-menu-item>
      <el-menu-item v-if="userInfo != null" :index="'/home/stuInfo/' + userInfo.userId" class="avatar">
        <el-popover placement="bottom" width="100" trigger="hover" content="" popper-class="logout-popover">
          <div>
            <div class="colorItem" @click="logout">
              <div style="
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  margin-left: 10px;
                ">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18" fill="currentColor"
                  role="none">
                  <path fill-rule="evenodd"
                    d="M18.586 13h-8.083c-.523 0-.947-.448-.947-1s.424-1 .947-1h8.083l-2.738-2.737a1 1 0 011.415-1.415l4.444 4.445a1 1 0 010 1.414l-4.444 4.445a1 1 0 01-1.415-1.415L18.586 13zM9 5H6a1 1 0 00-1 1v12a1 1 0 001 1h3a1 1 0 110 2H6a3 3 0 01-3-3V6a3 3 0 013-3h3a1 1 0 010 2z"
                    clip-rule="evenodd" role="none"></path>
                </svg>
              </div>
              <div style="margin-left: 10px">退出</div>
            </div>
          </div>
          <div slot="reference">
            <el-avatar :src="imageUrl"></el-avatar>
          </div>
        </el-popover>
      </el-menu-item>

      <el-menu-item style="float: right" @click="handleNotice()">
        <i class="el-icon-message-solid"></i>
        <el-badge v-if="
          redPointList != null &&
          redPointList.length > 0 &&
          redPointList[0] > 0
        " :value="redPointList[0]" :max="99" style="
            width: 10px !important;
            height: 10px;
            line-height: 10px;
            border-radius: 50%;
            position: absolute;
            top: 10px;
            padding: 0px;
            margin: 0px;
            font-size: 8px;
            right: 30px;
          ">
        </el-badge>
      </el-menu-item>

      <el-menu-item style="float: right; border: 0px">
        <el-button style="font-size: 14px" round size="small" @click="writeArticle">发布</el-button>
      </el-menu-item>

      <el-menu-item style="float: right; border: 0px">

        <div class="search">
          <el-input v-model="search" placeholder="请输入搜索内容" @keydown.native.enter="searchArticle()"
            style="width: 100%;background-color: transparent;  border: none;" prefix-icon="el-icon-search" />
        </div>
        <!-- <div class="search">
       
      </div> -->
      </el-menu-item>
    </el-menu>
    <el-dialog style="height: 100%; margin-top: 0px" @open="openNotice" @close="closeNotice" height="600px"
      width="900px" title="消息中心" :visible.sync="dialogNoticeVisible">
      <div>
        <el-row class="tac">
          <el-col style="width: 100px">
            <el-menu @select="selectNotice" :default-active="activeNoticeIndex" class="el-menu-vertical-demo">
              <el-menu-item index="1">
                全部
                <el-badge v-if="
                  redPointList != null &&
                  redPointList.length > 0 &&
                  redPointList[0] > 0
                " :value="redPointList[0]" :max="99" class="item">
                </el-badge>
              </el-menu-item>
              <el-menu-item index="2">
                关注
                <el-badge v-if="
                  redPointList != null &&
                  redPointList.length > 0 &&
                  redPointList[1] > 0
                " :value="redPointList[1]" :max="99" class="item">
                </el-badge>
              </el-menu-item>
              <el-menu-item index="3">
                点赞
                <el-badge v-if="
                  redPointList != null &&
                  redPointList.length > 0 &&
                  redPointList[2] > 0
                " :value="redPointList[2]" :max="99" class="item">
                </el-badge>
              </el-menu-item>
              <el-menu-item index="4">
                收藏
                <el-badge v-if="
                  redPointList != null &&
                  redPointList.length > 0 &&
                  redPointList[3] > 0
                " :value="redPointList[3]" :max="99" class="item"></el-badge>
              </el-menu-item>
              <el-menu-item index="5">
                评论
                <el-badge v-if="
                  redPointList != null &&
                  redPointList.length > 0 &&
                  redPointList[4] > 0
                " :value="redPointList[4]" :max="99" class="item"></el-badge>
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col style="width: 760px">
            <div style="
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-size: 16px;
              ">
              <div style="display: flex; align-items: center">
                <div @click="readChange(false)" :style="{
                  cursor: 'pointer',
                  color: readFlag == false ? '#262626' : '#585A5A',
                  fontWeight: readFlag == false ? 'bold' : 'normal',
                  marginLeft: '20px',
                }">
                  未读
                </div>
                <div @click="readChange(true)" :style="{
                  cursor: 'pointer',
                  color: readFlag == true ? '#585A5A' : '#262626',
                  fontWeight: readFlag == true ? 'bold' : 'normal',
                  marginLeft: '20px',
                }" style="margin-left: 10px">
                  已读
                </div>
              </div>

              <div v-if="
                readFlag == false &&
                noticeList != null &&
                noticeList.length > 0
              " @click="clearRead" style="float: right; margin-right: 20px; cursor: pointer">
                <i class="el-icon-brush"></i> 全部已读
              </div>
            </div>
            <el-empty style="height: 500px" v-if="noticeList == null || noticeList.length == 0"
              description="暂无数据"></el-empty>
            <div v-if="noticeList != null && noticeList.length > 0" style="overflow: auto; height: 500px">
              <ul v-infinite-scroll="load" infinite-scroll-distance="10" :infinite-scroll-disabled="disabled"
                style="list-style-type: none; padding: 0px; margin-top: 20px" class="task-list">
                <li v-for="data in noticeList" :key="data.noticeId">
                  <div class="blogger-item">
                    <!-- <el-skeleton :rows="3" animated :loading="loading" /> -->
                    <div class="blogger-item-inner">
                      <div class="avatar-info" style="margin-left: 15px; cursor: pointer"
                        @click.stop="skipUser(data.senderId)">
                        <el-avatar :src="data.avatar" style="margin-left: 15px" slot="reference" />
                      </div>
                      <div class="blogger-main">
                        <div style="font-size: 16px">
                          <span style="
                              color: #262626;
                              font-weight: 600;
                              cursor: pointer;
                            " @click.stop="skipUser(data.senderId)">
                            {{ data.userName }}</span>
                          <span v-if="data.targetType == 4" style="font-size: 14px">
                            关注了我
                            <span style="float: right; margin-right: 15px">
                              <el-button size="mini" v-if="data.isFollowedByMe == true" type="info">
                                已互关
                              </el-button>
                              <el-button size="mini" v-if="data.isFollowedByMe == false" type="success"
                                @click="follow(data)">
                                回关
                              </el-button></span>
                          </span>

                          <span v-if="data.targetType == 0">
                            <span style="font-size: 14px"> 赞了我的文章 </span>
                            <span @click="skipAtricle(data.articleId)" style="
                                color: #262626;
                                font-weight: 600;
                                cursor: pointer;
                              ">
                              {{ data.articleTitle }}
                            </span>
                          </span>
                          <span v-if="data.targetType == 1">
                            <span style="font-size: 14px"> 赞了我的评论 </span>
                            <div @click="skipAtricle(data.articleId)" style="
                                cursor: pointer;
                                font-size: 14px;
                                margin-top: 5px;
                              ">
                              原评论： {{ data.commentContent }}
                            </div>
                          </span>

                          <span v-if="data.targetType == 2">
                            <span style="font-size: 14px">
                              评论了我的文章
                            </span>
                            <span @click="skipAtricle(data.articleId)" style="
                                color: #262626;
                                font-weight: 600;
                                cursor: pointer;
                              ">
                              {{ data.articleTitle }}
                            </span>
                            <div @click="skipAtricle(data.articleId)" style="cursor: pointer; margin-top: 5px">
                              {{ data.commentContent }}
                            </div>
                          </span>
                          <span v-if="data.targetType == 3">
                            <span style="font-size: 14px">
                              回复了我的评论
                            </span>
                            <div @click="skipAtricle(data.articleId)" style="cursor: pointer; margin-top: 5px">
                              {{ data.commentContent }}
                            </div>
                          </span>

                          <span v-if="data.targetType == 5">
                            <span style="font-size: 14px">
                              收藏了我的文章
                            </span>
                            <span @click="skipAtricle(data.articleId)" style="
                                color: #262626;
                                font-weight: 600;
                                cursor: pointer;
                              ">
                              {{ data.articleTitle }}
                            </span>
                          </span>
                          <span v-if="data.targetType == 6">
                            <span style="font-size: 14px">
                              收藏了我的题单
                            </span>
                            <span @click="skipTraining(data.trainingId)" style="
                                color: #262626;
                                font-weight: 600;
                                cursor: pointer;
                              ">
                              {{ data.trainingTitle }}
                            </span>
                          </span>
                        </div>

                        <div style="margin-top: 1px" class="descriptionInfo">
                          {{ formatDate(new Date(data.createTime)) }}
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
              </ul>
            </div>

            <p v-if="loading" v-loading="loading" element-loading-text="加载中" element-loading-spinner="el-icon-loading"
              style="font-size: 20px" />
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogMarkdonwVisible" width="50%" style="height: 100%; margin-top: 0px"
      @open="initializeEditor" @close="handleDialogClose">
      <div style="display: flex">
        <div style="width: 80%">
          <el-form :model="form" :rules="rules" ref="formRef" style="display: flex; height: 40px">
            <!-- <el-form-item label="" prop="categoryId">
              <el-select style="width: 150px" v-model="form.categoryId" placeholder="请选择文章类型">
                <el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="" prop="title" style="margin-left: 10px; width: 100%">
              <el-input style="width: 100%" placeholder="请输入标题" v-model="form.title"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <div style="margin-left: 10px">
          <el-upload class="upload-demo" action="" :http-request="uploadCover" :limit="1" :show-file-list="false">
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </div>
        <div style="margin-left: auto; margin-bottom: 10px; line-height: 40px" @click="addArticle">
          <el-button size="small" type="success" round><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
              width="1em" height="1em" fill="currentColor" class="css-1rhb60f-Svg ea8ky5j0">
              <path fill-rule="evenodd"
                d="M22.707 1.293a1 1 0 01.237 1.037l-7 20a1 1 0 01-1.858.076l-3.844-8.648-8.648-3.844a1 1 0 01.076-1.858l20-7a1 1 0 011.037.237zM12.193 13.22l2.696 6.068 4.72-13.483-7.416 7.416zm6.001-8.83L4.711 9.111l6.067 2.696 7.416-7.416z"
                clip-rule="evenodd"></path>
            </svg>
            <span>发布文章</span>
          </el-button>
        </div>
      </div>
      <div style="height: 60%; min-height: 800px; margin-top: 20px" id="dialogMarkdown"></div>
      <!-- <MarkdownEditor></MarkdownEditor> -->
    </el-dialog>
  </el-header>
</template>

<script>
import { useUserStore } from "../../public/stores/modules/user";
import { mapState, mapActions } from "pinia";
// import MarkdownEditor from "@/views/MarkdownEditor.vue";
import api from "@/common/api";
import router from "../../public/router";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
export default {
  components: {},
  data: function () {
    return {
      activeNoticeIndex: "1",
      loading: false,
      readFlag: false,
      articleCherry: null,
      form: {
        coverUrl: null,
        categoryId: 3,
        title: null,
        content: null,
      },
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
      },
      noticeForm: {
        current: 1,
        size: 10,
        isRead: false,
        targetTypeList: null,
      },
      search: "",
      total: 0,
      count: 10,
      dialogNoticeVisible: false,
      dialogMarkdonwVisible: false,
      imageUrl: null,
      noticeList: null,
      activeIndex: "/home/indexInfo",
      heartbeatInterval: null,
      redPointList: null,
    };
  },
  computed: {
    ...mapState(useUserStore, ["userInfo"]),
    noMore() {
      return this.count >= this.total;
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  watch: {
    $route: {
      immediate: true,
      handler() {
        const path = this.$route.path;
        if (path.startsWith("/home/indexInfo")) {
          this.activeIndex = "/home/indexInfo";
        } else if (path.startsWith("/home/problemInfo")) {
          this.activeIndex = "/home/problemInfo";
        } else if (path.startsWith("/home/awardInfo")) {
          this.activeIndex = "/home/awardInfo";
        } else if (path.startsWith("/home/memberInfo")) {
          this.activeIndex = "/home/memberInfo";
        } else if (path.startsWith("/home/stuInfo")) {
          this.activeIndex = "/home/stuInfo/" + this.$route.params.userId;
        } else if (path.startsWith("/home/trainingInfo")) {
          this.activeIndex = "/home/trainingInfo";
        } else if (path.startsWith("/home/contest")) {
          this.activeIndex = "/home/contest";
        }
      },
    },
  },
  methods: {
    searchArticle() {
      console.log(this.search);
      if (!this.search || this.search.trim() === '') {
        return;
      }
      window.open("/home/search/" + encodeURIComponent(this.search.trim()), "_blank");
    },
    ...mapActions(useUserStore, ["reset"]),
    skipTraining(trainingId) {
      window.open("/home/training/" + trainingId, "_blank");
    },
    initData() {
      this.noticeList = null;
      this.total = 0;
      this.count = 0;
      this.readFlag = false;
      this.noticeForm = {
        current: 1,
        size: 10,
        isRead: false,
        targetTypeList: null,
      };
    },
    load() {
      if (
        this.noticeForm.current * this.noticeForm.size < this.total &&
        !this.loading
      ) {
        this.loading = true;
        setTimeout(() => {
          this.count += 10;
          this.noticeForm.current++;
          this.getNoticeList();
          this.loading = false;
        }, 500);
      }
    },
    selectNotice(index) {
      this.initData();
      this.activeNoticeIndex = index;
      if (index == 1) {
        this.noticeForm.targetTypeList = null;
      } else if (index == 2) {
        this.noticeForm.targetTypeList = [4];
      } else if (index == 3) {
        this.noticeForm.targetTypeList = [0, 1];
      } else if (index == 4) {
        this.noticeForm.targetTypeList = [5, 6];
      } else if (index == 5) {
        this.noticeForm.targetTypeList = [2, 3];
      }
      this.getNoticeList();
    },
    clearRead() {
      // 当前类型中的都要置为已读
      api
        .clearRead({ targetTypeList: this.noticeForm.targetTypeList })
        .then((response) => {
          if (response.data.code === "200") {
            this.noticeList = null;
            this.getRedPoint();
          }
        });
    },

    arraysEqualStringify(arr1, arr2) {
      return JSON.stringify(arr1) === JSON.stringify(arr2);
    },
    readChange(flag) {
      this.noticeList = null;
      this.total = 0;
      this.count = 0;
      this.readFlag = flag;
      this.noticeForm.isRead = flag;
      this.noticeForm.current = 1;
      this.noticeForm.size = 10;
      this.getNoticeList();
    },
    skipUser(userId) {
      window.open("/home/stuInfo/" + userId, "_blank");
    },
    skipAtricle(articleId) {
      window.open("/home/articleInfo/" + articleId, "_blank");
    },
    follow(data) {
      api.follow({ userId: data.senderId }).then((response) => {
        if (response.data.code === "200") {
          data.isFollowedByMe = true;
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
    openNotice() {
      this.getNoticeList();
    },
    closeNotice() {
      this.initData();
    },
    getNoticeList() {
      var index = this.activeNoticeIndex;
      if (index == 1) {
        this.noticeForm.targetTypeList = null;
      } else if (index == 2) {
        this.noticeForm.targetTypeList = [4];
      } else if (index == 3) {
        this.noticeForm.targetTypeList = [0, 1];
      } else if (index == 4) {
        this.noticeForm.targetTypeList = [5, 6];
      } else if (index == 5) {
        this.noticeForm.targetTypeList = [2, 3];
      }
      api.getNoticeList(this.noticeForm).then((response) => {
        if (response.data.code === "200") {
          if (this.noticeList == null) this.noticeList = [];
          this.noticeList = this.noticeList.concat(response.data.data.records);
          this.total = response.data.data.total;
        }
      });
    },
    handleNotice() {
      api.checkLogin().then((response) => {
        if (response.data.code === "200" && response.data.data === true) {
          console.log("writeArticle");
        } else {
          this.$message({
            message: "请先登录",
            type: "warning",
          });
          router.push("/login");
        }
      });
      this.dialogNoticeVisible = !this.dialogNoticeVisible;
    },
    addArticle() {
      let isValid = true;
      this.$refs["formRef"].validate((valid) => {
        if (!valid) {
          isValid = false;
          return false;
        }
      });
      if (!isValid) {
        return;
      }
      this.form.content = this.articleCherry.getMarkdown();
      this.dialogMarkdonwVisible = false;
      api.addArticle(this.form).then((response) => {
        if (response.data.code === "200") {
          this.$message({
            message: "发布成功",
            type: "success",
          });
        }
      });
    },
    uploadCover(options) {
      const { file } = options;
      // 创建 FormData 对象
      const formData = new FormData();
      formData.append("file", file);
      api.uploadArticleCover(formData).then((response) => {
        if (response.data.code === "200") {
          this.$message({
            message: "上传成功",
            type: "success",
          });
          this.form.coverUrl = response.data.data;
        }
      });
    },
    initCherryEditor() {
      if (this.articleCherry) {
        return;
      }
      this.articleCherry = new Cherry({
        id: "dialogMarkdown", // 绑定到指定 DOM 节点
        value: "", // 初始化的 Markdown 内容
        fileUpload: this.myFileUpload,
        toolbars: {
          // 定义顶部工具栏
          toolbar: [
            "undo",
            "redo",
            "|",
            {
              bold: [
                "bold",
                "italic",
                "underline",
                "strikethrough",
                "sub",
                "sup",
                "ruby",
              ],
            },
            "|",
            "color",
            "header",
            "|",
            "list",
            "panel",
            "detail",

            {
              insert: [
                "image",
                "pdf",
                "word",
                "file",
                "link",
                "hr",
                "br",
                "code",
                "formula",
                "toc",
                "table",
              ],
            },
            "graph",
            "settings",
          ],

          // 定义侧边栏，默认为空
          sidebar: ["mobilePreview", "copy"],
          // 定义顶部右侧工具栏，默认为空
          toolbarRight: ["fullScreen", "export", "wordCount"],
          // 定义选中文字时弹出的“悬浮工具栏”，默认为 ['bold', 'italic', 'underline', 'strikethrough', 'sub', 'sup', 'quote', '|', 'size', 'color']
          bubble: [
            "bold",
            "italic",
            "underline",
            "strikethrough",
            "sub",
            "sup",
            "quote",
            "|",
            "size",
            "color",
          ],

          // toc: true, // 不展示悬浮目录

          // toc: {
          //   updateLocationHash: false, // 要不要更新URL的hash
          //   defaultModel: "full", // pure: 精简模式/缩略模式，只有一排小点； full: 完整模式，会展示所有标题
          //   showAutoNumber: false, // 是否显示自增序号
          //   position: "fixed", // 悬浮目录的悬浮方式。当滚动条在cherry内部时，用absolute；当滚动条在cherry外部时，用fixed
          //   cssText: "", // 自定义样式
          // },

          // 定义光标出现在行首位置时出现的“提示工具栏”，默认为 ['h1', 'h2', 'h3', '|', 'checklist', 'quote', 'table', 'code']
          float: ["h1", "h2", "h3", "|", "checklist", "quote", "table", "code"],
        },
        engine: {
          syntax: {
            mathBlock: {
              engine: "MathJax", // katex或MathJax
              src: "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-svg.js", // 如果使用MathJax plugins，则需要使用该url通过script标签引入
            },
            inlineMath: {
              engine: "MathJax", // katex或MathJax
            },
          },
        },
        themeSettings: {
          codeBlockTheme: "one light",
        },
        drawioIframeUrl:
          window.location.origin + "/cherryMarkdown/drawio_demo.html",
        drawioIframeStyle: "border: none; width: 100%; height: 100%;",
      });
    },
    myFileUpload(file1, callback) {
      const formData = new FormData();
      formData.append("file", file1);
      // 先把文件上传到服务端，上传文件的具体代码需要自行实现
      api.uploadArticlePictureAndFile(formData).then((response) => {
        if (response.data.code == "200") {
          callback(response.data.data);
        }
      });
      // putFile(file, function (err, url) {
      //   if (err) {
      //     // 上传失败
      //   } else {
      //     callback(url);
      //   }
      // });
    },
    initializeEditor() {
      this.$nextTick(() => {
        this.initCherryEditor();
      });
    },
    handleDialogClose() {
      this.dialogMarkdonwVisible = false;
    },
    writeArticle() {
      api.checkLogin().then((response) => {
        if (response.data.code === "200" && response.data.data === true) {
          console.log("writeArticle");
        } else {
          this.$message({
            message: "请先登录",
            type: "warning",
          });
          router.push("/login");
        }
      });
      this.dialogMarkdonwVisible = true;
    },
    GotoHome() {
      if (this.$route.path !== "/home/indexInfo")
        router.push("/home/indexInfo");
    },
    logout() {
      api.logout().then((response) => {
        if (response.data.code == "200") {
          this.reset();
          this.$router.push("/login");
        }
      });
    },
    login() {
      this.$router.push("/login");
    },
    fetchData: function () {
      api.getAvatar().then((response) => {
        if (response.data.code == "200") {
          this.imageUrl = response.data.data;
        }
      });
      this.sendHeartbeat();
      this.heartbeatInterval = setInterval(() => {
        this.sendHeartbeat();
      }, 300000); // 每5分钟发送一次心跳包
      this.getRedPoint();
    },
    getRedPoint() {
      api.getRedPoint().then((response) => {
        if (response.data.code == "200") {
          this.redPointList = response.data.data;
        }
      });
    },
    sendHeartbeat() {
      api.sendHeartbeat().then((response) => {
        if (response.data.code == "200") {
          console.log("发送成功");
        }
      });
    },
    stopHeartbeat() {
      if (this.heartbeatInterval) {
        clearInterval(this.heartbeatInterval);
        this.heartbeatInterval = null;
      }
    },
  },
  beforeDestroy() {
    this.stopHeartbeat();
  },
  created() {
    this.fetchData();
  },
};
</script>
<style>
.logout-popover {
  padding: 10px;
  /* background-color: #f0f0f0;  */
  border-radius: 20px;
}
</style>
<style scoped>
::v-deep .el-input__inner {
  border: 1px solid #ebeef5;
  border-radius: 20px;
  height: 35px;
}

.search {
  border: none;
  /* margin-bottom: 10px; */
  display: flex;
  /* border: 1px solid #ebeef5; */
  width: 200px;
  /* height: 40px; */
  /* background-color: pink; */
  align-items: center;
  justify-content: center;
  font-size: 14px;
  /* line-height: 24px; */
}

.item {
  width: 10px !important;
  height: 10px;
  line-height: 10px;
  border-radius: 50%;
  position: absolute;
  top: 10px;
  padding: 0px;
  margin: 0px;
  font-size: 8px;
  right: 45px;
}

.redPoint {
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: inline-block;
  text-align: center;
  line-height: 18px;
  position: absolute;
  font-size: 12px;
  top: 15px;
  left: 30px;
}

::v-deep .el-dialog {
  margin-top: 0px;
}

::v-deep .el-dialog {
  border-radius: 20px;
}

::v-deep .el-icon-edit:before {
  color: #fff;
}

.blogger-item {
  /* height: 125px; */
  margin-top: 10px;
  border-bottom: 1px solid #ddd;
  border-color: rgb(0 0 0 / 8%);
  /* border-bottom: 1px; */
  background-color: white;
}

.blogger-item-inner {
  display: flex;
  /* cursor: pointer; */
}

.blogger-main {
  /* overflow: visible;
    text-overflow: ellipsis;
    word-wrap: break-word; */
  /* white-space: nowrap; */

  flex: 1;

  /* height: 140px; */
  margin-left: 20px;
  display: flex;
  flex-direction: column;
}

.colorItem {
  width: 150px;
  display: flex;
  align-items: center;
  height: 40px;
  cursor: pointer;
  border-radius: 10px;
}

.colorItem:hover {
  background-color: #f7f7f8;
}

.el-header {
  width: 50%;
  min-width: 1240px;
  margin-top: 0;
  margin: 0 auto;
}

.elHeader>.el-menu--horizontal>.el-menu-item {
  height: 60px;
  line-height: 60px;
}

/* ::v-deep .el-menu-item {
  color: #909399;
} */
::v-deep.el-menu.el-menu--horizontal {
  border-bottom: none;
}

.menu-custom .avatar {
  /* margin-left: 500px; */
  /* padding-left: 50%; */
  float: right;
  /* display: grid;
  justify-items: end; */
}

a {
  /* padding: 10px 20px; 增加 padding */
  display: inline-block;
  text-decoration: none;
  color: #ffffff;
}
</style>
