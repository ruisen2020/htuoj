<template>
    <div class="blogger-item">
      <!-- <el-skeleton :rows="3" animated :loading="loading" /> -->
      <div class="blogger-item-inner" @click="skipAtricle(data.articleId)">
        <div
          class="avatar-info"
          style="margin-left: 15px"
          @click.stop="skipUser(data.userId)">
          <el-avatar
            fit="contain"
            :src="data.avatar"
            style="margin-left: 15px"
            slot="reference" />
          <!-- <el-popover placement="bottom" trigger="hover">
            <div
              style="display: flex; width: 250px; border-bottom: 1px solid #ddd"
            >
              <el-avatar
                :size="65"
                :src="'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'"
                style="margin-left: 10px"
                shape="square"
              />
              <div
                style="
                  display: flex;
                  flex-direction: column;
                  margin-left: 10px;
                  margin-bottom: 20px;
                "
              >
                <span style="font-size: 18px">gao</span>
                <el-tag
                  size="mini"
                  :style="{ width: 45 + 'px' }"
                  type="success"
                  effect="dark"
                  >排名:1</el-tag
                >
                <span style="font-size: 12px">description</span>
              </div>
            </div>
            <el-button
              size="small"
              icon="el-icon-plus"
              type="primary"
              style="width: 250px; margin-top: 10px"
              >关注</el-button
            >
          </el-popover> -->
        </div>
        <div class="blogger-main">
          <div
            style="font-size: 14px; color: grey"
            @click.stop="skipUser(data.userId)">
            <span v-html="data.userName"></span>
          </div>
          <div
            style="
              font-size: 16px;
              font-weight: 600;
              color: #1a1a1a;
              margin-top: 5px;
            ">
            <span v-if="data.categoryId == 1"> 公告 | </span>
            <span v-else-if="data.categoryId == 2"> 讨论 | </span>
            <span v-else-if="data.categoryId == 3"> 分享 | </span>
            <span v-else-if="data.categoryId == 4"> 题解 | </span>
            <span v-html="data.title"></span>
          </div>
          <div style="margin-top: 5px" class="descriptionInfo">
            <span v-html="data.content"></span>
          </div>
          <div
            style="
              height: 20px;
              color: #8c8c8c;
              margin-top: 10px;
              display: flex;
              gap: 20px;
              margin-bottom: 10px;
            ">
            <!-- 点赞 -->
            <div class="bottom-info" @click.stop="like(data)">
              <div>
                <span
                  v-if="data.isLike"
                  style="
                    color: #2db55d;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  ">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="1em"
                    height="1em"
                    fill="#2db55d"
                    class="h-4 w-4 text-green-s dark:text-dark-green-s hover:text-green-s dark:hover:text-dark-green-s">
                    <path
                      fill-rule="evenodd"
                      d="M7.04 9.11l3.297-7.419a1 1 0 01.914-.594 3.67 3.67 0 013.67 3.671V7.33h4.028a2.78 2.78 0 012.78 3.2l-1.228 8.01a2.778 2.778 0 01-2.769 2.363H5.019a2.78 2.78 0 01-2.78-2.78V11.89a2.78 2.78 0 012.78-2.78H7.04zm-2.02 2a.78.78 0 00-.781.78v6.232c0 .431.35.78.78.78H6.69V11.11H5.02z"
                      clip-rule="evenodd"></path>
                  </svg>
                </span>
                <span
                  v-else
                  style="
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  "
                  ><svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="1em"
                    height="1em"
                    fill="currentColor"
                    class="h-4 w-4 text-gray-6 dark:text-dark-gray-6 hover:text-green-s dark:hover:text-dark-green-s">
                    <path
                      fill-rule="evenodd"
                      d="M7.04 9.11l3.297-7.419a1 1 0 01.914-.594 3.67 3.67 0 013.67 3.671V7.33h4.028a2.78 2.78 0 012.78 3.2l-1.228 8.01a2.778 2.778 0 01-2.769 2.363H5.019a2.78 2.78 0 01-2.78-2.78V11.89a2.78 2.78 0 012.78-2.78H7.04zm-2.02 2a.78.78 0 00-.781.78v6.232c0 .431.35.78.78.78H6.69V11.11H5.02zm12.723 7.793a.781.781 0 00.781-.666l1.228-8.01a.78.78 0 00-.791-.898h-5.04a1 1 0 01-1-1V4.77c0-.712-.444-1.32-1.07-1.56L8.69 10.322v8.58h9.053z"
                      clip-rule="evenodd"></path>
                  </svg>
                </span>
              </div>
              <div
                :style="{
                  marginLeft: '3px',
                  fontSize: '13px',
                  color: data.isLike ? '#2DB55D' : '#8c8c8c',
                }">
                {{ data.likeCount }}
              </div>
            </div>
            <!-- 评论 -->
            <div class="bottom-info" @click.stop="articleInfo(data.articleId)">
              <div
                style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                ">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  width="1em"
                  height="1em"
                  fill="currentColor"
                  class="h-4.5 w-4.5 text-gray-6 dark:text-dark-gray-6">
                  <path
                    fill-rule="evenodd"
                    d="M11.997 21.5a9.5 9.5 0 01-8.49-5.251A9.38 9.38 0 012.5 11.997V11.5c.267-4.88 4.12-8.733 8.945-8.999L12 2.5a9.378 9.378 0 014.25 1.007A9.498 9.498 0 0121.5 12a9.378 9.378 0 01-.856 3.937l.838 4.376a1 1 0 01-1.17 1.17l-4.376-.838a9.381 9.381 0 01-3.939.856zm3.99-2.882l3.254.623-.623-3.253a1 1 0 01.09-.64 7.381 7.381 0 00.792-3.346 7.5 7.5 0 00-4.147-6.708 7.385 7.385 0 00-3.35-.794H11.5c-3.752.208-6.792 3.248-7.002 7.055L4.5 12a7.387 7.387 0 00.794 3.353A7.5 7.5 0 0012 19.5a7.384 7.384 0 003.349-.793 1 1 0 01.639-.09z"
                    clip-rule="evenodd"></path>
                </svg>
              </div>
              <div style="margin-left: 3px; font-size: 13px">
                {{ data.commentCount }}
              </div>
            </div>
            <!-- 收藏 -->
            <div class="bottom-info" @click.stop="collect(data)">
              <div>
                <span
                  v-if="data.isCollect"
                  style="
                    color: #ffa116;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  ">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="1em"
                    height="1em"
                    fill="currentColor"
                    class="pointer-events-none h-4 w-4">
                    <path
                      fill-rule="evenodd"
                      d="M11.394 2.074a2.5 2.5 0 011.212 0c.723.181 1.185.735 1.526 1.262.342.528.703 1.259 1.131 2.127l.392.795c.302.61.348.667.386.7a.498.498 0 00.086.063c.043.025.11.052.786.15l.877.128c.958.139 1.764.256 2.372.418.606.162 1.276.43 1.671 1.062a2.5 2.5 0 01.375 1.152c.052.744-.333 1.354-.728 1.841-.397.489-.98 1.058-1.674 1.733l-.634.619c-.489.476-.527.537-.548.583a.5.5 0 00-.033.101c-.01.05-.015.122.1.794l.15.873c.164.954.302 1.758.335 2.386.034.627-.014 1.346-.493 1.918-.263.314-.6.558-.98.712-.692.279-1.39.102-1.976-.124-.588-.226-1.309-.605-2.165-1.056l-.785-.412c-.603-.317-.674-.335-.724-.34a.497.497 0 00-.106 0c-.05.005-.12.023-.724.34l-.785.412c-.856.45-1.577.83-2.165 1.056-.585.226-1.284.403-1.976.124a2.5 2.5 0 01-.98-.712c-.48-.572-.527-1.291-.493-1.918.033-.628.171-1.431.335-2.386l.15-.873c.115-.672.11-.745.1-.794a.5.5 0 00-.033-.101c-.02-.046-.06-.107-.548-.583l-.634-.619c-.694-.675-1.277-1.244-1.674-1.733-.395-.487-.78-1.097-.728-1.841a2.5 2.5 0 01.375-1.152c.395-.633 1.065-.9 1.67-1.062.61-.162 1.415-.28 2.373-.418l.877-.128c.675-.098.743-.125.786-.15a.5.5 0 00.086-.062c.038-.034.084-.09.386-.701l.392-.795c.428-.868.789-1.599 1.131-2.127.341-.527.803-1.08 1.526-1.262z"
                      clip-rule="evenodd"></path>
                  </svg>
                </span>
                <span
                  v-else
                  style="
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  ">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="1em"
                    height="1em"
                    fill="currentColor"
                    class="pointer-events-none h-4 w-4">
                    <path
                      fill-rule="evenodd"
                      d="M11.394 2.074a2.5 2.5 0 011.212 0c.723.181 1.185.735 1.526 1.262.342.528.703 1.259 1.131 2.127l.392.795c.302.61.348.667.386.7a.502.502 0 00.086.063c.043.025.11.052.786.15l.877.128c.958.139 1.764.256 2.372.418.606.162 1.276.43 1.671 1.062a2.5 2.5 0 01.375 1.152c.052.744-.333 1.354-.728 1.841-.397.489-.98 1.058-1.674 1.733l-.634.619c-.489.476-.527.537-.548.583a.506.506 0 00-.033.101c-.01.05-.015.122.1.794l.15.873c.164.954.302 1.758.335 2.386.034.627-.014 1.346-.493 1.918a2.5 2.5 0 01-.98.712c-.692.279-1.39.102-1.976-.124-.588-.226-1.309-.605-2.165-1.056l-.785-.412c-.603-.317-.674-.335-.724-.34a.496.496 0 00-.106 0c-.05.005-.12.023-.724.34l-.785.412c-.856.45-1.577.83-2.165 1.056-.585.226-1.284.403-1.976.124a2.501 2.501 0 01-.98-.712c-.48-.572-.527-1.291-.493-1.918.033-.628.171-1.431.335-2.386l.15-.873c.115-.672.11-.745.1-.794a.5.5 0 00-.033-.101c-.02-.046-.06-.107-.548-.583l-.634-.619c-.694-.675-1.277-1.244-1.674-1.733-.395-.487-.78-1.097-.728-1.841a2.5 2.5 0 01.375-1.152c.395-.633 1.065-.9 1.67-1.062.61-.162 1.415-.28 2.373-.418l.877-.128c.675-.098.743-.125.786-.15a.5.5 0 00.086-.062c.038-.034.084-.09.386-.701l.392-.795c.428-.868.789-1.599 1.131-2.127.341-.527.803-1.08 1.526-1.262zm.493 1.939c-.023.013-.132.089-.34.41-.271.418-.58 1.042-1.045 1.982l-.364.738-.05.103c-.213.434-.428.872-.788 1.197a2.5 2.5 0 01-.43.312c-.42.241-.903.31-1.381.379a52.6 52.6 0 00-.114.016l-.815.119c-1.037.15-1.725.252-2.207.38-.37.099-.476.18-.495.197a.5.5 0 00-.07.216c.005.025.044.153.285.45.314.386.811.874 1.562 1.605l.59.575.082.08c.346.336.697.676.895 1.118.072.162.127.332.164.506.1.474.016.955-.067 1.431l-.02.113-.138.811c-.178 1.033-.294 1.72-.32 2.217-.02.382.023.508.034.532.05.058.113.103.183.133.026.003.16.006.516-.132.465-.18 1.082-.502 2.01-.99l.728-.382.102-.054c.427-.226.859-.454 1.34-.505.177-.02.355-.02.532 0 .481.051.913.28 1.34.505l.102.054.728.383c.928.487 1.545.81 2.01.99.357.137.49.134.516.13a.499.499 0 00.183-.132c.01-.024.055-.15.034-.532-.026-.497-.142-1.184-.32-2.217l-.139-.81-.02-.114c-.082-.476-.166-.957-.066-1.431.037-.174.092-.344.164-.506.198-.442.549-.782.895-1.118a20.8 20.8 0 00.083-.08l.59-.575c.75-.731 1.247-1.219 1.561-1.606.241-.296.28-.424.285-.45a.5.5 0 00-.07-.215c-.02-.017-.126-.098-.495-.196-.482-.129-1.17-.23-2.207-.381l-.815-.119-.113-.016c-.479-.068-.963-.138-1.382-.379a2.5 2.5 0 01-.43-.312c-.36-.325-.575-.763-.788-1.197a31.757 31.757 0 00-.05-.103l-.364-.738c-.464-.94-.774-1.564-1.045-1.982-.208-.321-.317-.397-.34-.41a.5.5 0 00-.226 0zm8.326 6.044v.002-.002zm-3.246 9.575h-.002.002zm-9.934 0h.002-.002zm-3.246-9.575v.002-.002z"
                      clip-rule="evenodd"></path>
                  </svg>
                </span>
              </div>
              <div
                :style="{
                  marginLeft: '3px',
                  fontSize: '13px',
                  color: data.isCollect ? '#ffa116' : '#8c8c8c',
                }">
                {{ data.collectCount }}
              </div>
            </div>
            <!-- 浏览 -->
            <div class="bottom-info" @click.stop="articleInfo(data.articleId)">
              <div
                style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                ">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  width="1em"
                  height="1em"
                  fill="currentColor"
                  class="css-kfex1q-Svg ea8ky5j0">
                  <path
                    fill-rule="evenodd"
                    d="M1.104 12.444a1 1 0 010-.888c.13-.26.37-.693.722-1.241A18.85 18.85 0 013.88 7.652C6.184 5.176 8.896 3.667 12 3.667s5.816 1.509 8.119 3.985c.79.85 1.475 1.756 2.055 2.663.352.548.593.98.722 1.24a1 1 0 010 .89c-.13.26-.37.692-.722 1.24a18.848 18.848 0 01-2.055 2.663c-2.303 2.476-5.015 3.985-8.119 3.985s-5.816-1.509-8.119-3.985a18.846 18.846 0 01-2.055-2.663c-.352-.548-.593-.98-.722-1.24zm2.406.162a16.87 16.87 0 001.836 2.38c1.959 2.106 4.19 3.347 6.654 3.347 2.465 0 4.695-1.24 6.654-3.347A16.87 16.87 0 0020.86 12a16.871 16.871 0 00-2.206-2.986C16.695 6.908 14.464 5.667 12 5.667c-2.465 0-4.695 1.24-6.654 3.347A16.87 16.87 0 003.14 12c.108.188.232.391.37.607zM12 15.75c-2.06 0-3.727-1.68-3.727-3.75 0-2.07 1.667-3.75 3.727-3.75 2.06 0 3.727 1.68 3.727 3.75 0 2.07-1.667 3.75-3.727 3.75zm0-2c.952 0 1.727-.782 1.727-1.75s-.775-1.75-1.727-1.75c-.952 0-1.727.782-1.727 1.75s.775 1.75 1.727 1.75z"
                    clip-rule="evenodd"></path>
                </svg>
              </div>
              <div style="margin-left: 3px; font-size: 13px">
                {{ data.watchCount }}
              </div>
            </div>
          </div>
        </div>
  
        <div class="photo-info">
          <el-image
            @click.stop=""
            :style="{
              width: 150 + 'px',
              height: 90 + 'px',
              'border-radius': 5 + 'px',
            }"
            :preview-src-list="[data.coverUrl]"
            :src="data.coverUrl"
            fit="contain"
            lazy />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  // import router from "../../../public/router";
  
  export default {
    name: "BloggerListItem",
    props: {
      data: {
        type: Object,
      },
    },
    data() {
      return { loading: true };
    },
    updated() {
      this.loading = false;
    },
    methods: {
      skipUser(userId) {
        window.open("/home/stuInfo/" + userId, "_blank");
      },
      skipAtricle(articleId) {
        window.open("/home/articleInfo/" + articleId, "_blank");
      },
      articleInfo(articleId) {
        // console.log("aaaa", articleId);
        // router.push({
        //   path: "/home/articleInfo/" + articleId,
        // });
        window.open("/home/articleInfo/" + articleId, "_blank");
      },
      like(data) {
        this.$emit("like-event", data);
      },
      collect(data) {
        this.$emit("collect-event", data);
      },
    },
  };
  </script>
  
  <style scoped>
  .bottom-info {
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }
  .descriptionInfo {
    font-size: 14px;
    color: #0000008c;
    width: 540px;
  
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
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
    cursor: pointer;
  }
  .blogger-main {
    /* overflow: visible;
    text-overflow: ellipsis;
    word-wrap: break-word; */
    /* white-space: nowrap; */
  
    flex: 1;
    width: 540px;
    /* height: 140px; */
    margin-left: 20px;
    display: flex;
    flex-direction: column;
  }
  .photo-info {
    /* width: 100px;
    height: 100px; */
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 5px;
  }
  </style>
  