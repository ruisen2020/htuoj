<template>
  <div>
    <div style="width: 100%; height: 100%">
      <div style="height: 980px; overflow-y: scroll" ref="chatContainer">
        <div v-for="item in chatList" :key="item.num" ref="messageItems">
          <span v-if="item.type == 0">
            <el-card style="background-color: #f5f7fa; margin-top: 20px">
              <div style="color: #000000; display: flex; align-items: center">
                <div>
                  <i style="font-size: 30px" class="el-icon-user"></i>
                </div>
                <div style="font-size: 16px; margin-left: 10px">
                  {{ item.content }}
                </div>
              </div>
            </el-card>
          </span>
          <span v-else>
            <div style="color: #000000">
              <el-card style="margin-top: 20px">
                <div style="color: #000000; display: flex; align-items: center">
                  <div>
                    <img
                      style="width: 30px; height: 30px"
                      class="ModelIcon__middle--yKSXc efm_ai-mr12"
                      src="https://img.alicdn.com/imgextra/i3/O1CN016WYX5b22OR1llCmjK_!!6000000007110-2-tps-64-64.png" />
                  </div>
                  <div style="font-size: 16px; margin-left: 10px">
                    <AnsMarkdown
                      :id="item.num"
                      :content="item.content"></AnsMarkdown>
                  </div>
                </div>
              </el-card>
            </div>
          </span>
        </div>
      </div>
      <div
        style="
          width: 45%;
          display: flex;
          justify-content: center;
          position: fixed;
          bottom: 100px;
        ">
        <div style="width: 95%">
          <el-input
            style="font-size: 16px; width: 100%"
            type="textarea"
            :autosize="{ minRows: 2 }"
            placeholder="请输入您想问的问题"
            v-model="question">
          </el-input>
        </div>

        <div
          style="width: 60px; height: 60px; margin-left: 10px; cursor: pointer"
          @click="sendQuestion">
          <i
            style="width: 50px; height: 50px; font-size: 40px"
            class="el-icon-search"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/common/api";
import router from "../../../public/router";
// import Cherry from "cherry-markdown/dist/cherry-markdown.core";
import { useUserStore } from "/public/stores/modules/user.js";
import AnsMarkdown from "./AnsMarkdown.vue";
let num = 1;
export default {
  components: { AnsMarkdown },
  data() {
    return {
      chatList: [
        // { num: 1, type: 0, content: "User message 1" },
        // { num: 2, type: 1, content: "AI response 1" },
        // { num: 3, type: 0, content: "User message 2" },
        // { num: 4, type: 1, content: "AI response 2" },
      ],
      question: "",
    };
  },
  mounted() {
    api.checkLogin().then((response) => {
      if (response.data.code === "200" && response.data.data === true) {
        console.log("login");
      } else {
        this.$message({
          message: "请先登录",
          type: "warning",
        });
        router.push("/login");
      }
    });
  },
  methods: {
    async sendQuestion() {
      if (this.question == "") {
        this.$message.error("请输入问题");
        return;
      }

      this.chatList.push({ num: num, type: 0, content: this.question });
      num++;
      const userStore = useUserStore();
      const token = userStore.getToken;

      try {
        const response = await fetch("https://htuoj.cn:9001/ai/send", {
          method: "POST",
          headers: {
            Accept: "text/event-stream",
            "content-type": "application/json",
            Authorization: token,
          },
          body: JSON.stringify({ question: this.question }),
        });

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const reader = response.body.getReader();
        const decoder = new TextDecoder("utf-8");
        this.chatList.push({ num: num, type: 1, content: "" });
        num++;
        for (;;) {
          const { done, value } = await reader.read();
          if (done) break;

          const chunk = decoder.decode(value, { stream: true });
          console.log("Received chunk:", chunk);

          // 将接收到的数据块添加到 chatList 中
          this.chatList[this.chatList.length - 1].content = chunk;
          console.log("this.chatList", this.chatList[this.chatList.length - 1]);
          // this.$nextTick(() => {
          //   this.$refs.chatContainer.scrollTop =
          //     this.$refs.chatContainer.scrollHeight;
          // });
          this.$nextTick(() => {
            this.$refs.messageItems[
              this.$refs.messageItems.length - 1
            ].scrollIntoView({ behavior: "smooth" });
          });
        }
      } catch (error) {
        console.error("Fetch error:", error);
      }

      this.question = "";
    },
  },
};
</script>

<style></style>
