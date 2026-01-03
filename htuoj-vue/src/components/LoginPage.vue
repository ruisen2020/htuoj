<template>
  <div class="container">
    <div id="appLogin">
      <div class="loginLeft"></div>
      <el-divider direction="vertical" content-position="center"></el-divider>
      <div v-show="flag === 1" class="loginRight">
        <div class="loginDiv">
          <div
            style="
              text-align: center;
              font-size: 24px;
              margin-bottom: 20px;
              color: #222;
              font-weight: 600;
              font-family: PingFangSC-Light, sans-serif;
            ">
            账户登录
          </div>
          <el-form :model="LoginRuleForm" :rules="rules" ref="LoginRuleForm">
            <el-form-item prop="number">
              <el-input
                placeholder="请输入学号"
                v-model="LoginRuleForm.number"
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                type="password"
                placeholder="请输入密码"
                v-model="LoginRuleForm.password"
                show-password
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                style="
                  width: 300px;
                  height: 50px;
                  color: #333333;
                  border-radius: 10px;
                  margin-top: 50px;
                  font-size: 16px;
                "
                type="primary"
                @click="submitForm('LoginRuleForm')"
                >登录
              </el-button>
            </el-form-item>
          </el-form>
          <div
            class="loginBottom"
            style="font-size: 14px; cursor: pointer; font-weight: 500">
            <span @click="toggleForm(2)">忘记密码</span>
            <el-divider
              direction="vertical"
              content-position="center"></el-divider>
            <span @click="toggleForm(3)">立即注册</span>
          </div>
        </div>
      </div>
      <div v-show="flag === 2" class="loginRight">
        <div class="loginDiv" style="height: 400px">
          <div
            style="
              display: flex;
              align-items: center;
              cursor: pointer;
              color: rgba(22, 24, 35, 0.75);
            "
            @click="toggleForm(1)">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 16 16"
              fill="none">
              <path
                fill-rule="evenodd"
                clip-rule="evenodd"
                d="M11.138 2.19526C10.8776 1.93491 10.4555 1.93491 10.1952 2.19526L4.86185 7.5286C4.6015 7.78895 4.6015 8.21106 4.86185 8.4714L10.1952 13.8047C10.4555 14.0651 10.8776 14.0651 11.138 13.8047C11.3983 13.5444 11.3983 13.1223 11.138 12.8619L6.27606 8L11.138 3.13807C11.3983 2.87772 11.3983 2.45561 11.138 2.19526Z"
                fill="#161823"
                fill-opacity="0.75"></path>
            </svg>
            返回
          </div>
          <div
            style="
              text-align: center;
              font-size: 24px;
              margin-bottom: 20px;
              color: #222;
              font-weight: 600;
              font-family: PingFangSC-Light, sans-serif;
            ">
            重置密码
          </div>
          <el-form
            :model="changePasswordForm"
            :rules="changePasswordRules"
            ref="changePasswordForm">
            <el-form-item prop="number">
              <el-input
                placeholder="请输入学号"
                v-model="changePasswordForm.number"
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="newPassword">
              <el-input
                type="password"
                placeholder="请输入新密码"
                v-model="changePasswordForm.newPassword"
                show-password
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <div class="captcha">
                <div>
                  <el-input
                    placeholder="请输入验证码"
                    v-model="changePasswordForm.captcha"
                    show-password
                    autocomplete="off"></el-input>
                </div>

                <div style="width: 80px; margin-left: 10px">
                  <el-button
                    :disabled="isDisabled"
                    style="
                      color: #fff;
                      border-radius: 10px;
                      height: 50px;
                      width: 120px;
                      font-size: 16px;
                    "
                    type="primary"
                    @click="getCaptcha('changePasswordForm')">
                    <template>
                      <span v-if="isDisabled">倒计时: {{ countDown }}</span>
                      <span v-else>获取验证码</span>
                    </template>
                  </el-button>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button
                class="captchaButton"
                style="
                  width: 300px;
                  height: 50px;
                  color: #fff;
                  border-radius: 10px;
                  margin-top: 20px;
                  font-size: 16px;
                "
                type="primary"
                @click="submitchangePasswordForm('changePasswordForm')"
                >提交
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div v-show="flag === 3" class="loginRight">
        <div class="registerDiv" style="height: 450px">
          <div
            style="
              display: flex;
              align-items: center;
              cursor: pointer;
              color: rgba(22, 24, 35, 0.75);
            "
            @click="toggleForm(1)">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 16 16"
              fill="none">
              <path
                fill-rule="evenodd"
                clip-rule="evenodd"
                d="M11.138 2.19526C10.8776 1.93491 10.4555 1.93491 10.1952 2.19526L4.86185 7.5286C4.6015 7.78895 4.6015 8.21106 4.86185 8.4714L10.1952 13.8047C10.4555 14.0651 10.8776 14.0651 11.138 13.8047C11.3983 13.5444 11.3983 13.1223 11.138 12.8619L6.27606 8L11.138 3.13807C11.3983 2.87772 11.3983 2.45561 11.138 2.19526Z"
                fill="#161823"
                fill-opacity="0.75"></path>
            </svg>
            返回
          </div>

          <div
            style="
              text-align: center;
              font-size: 24px;
              margin-bottom: 20px;
              color: #222;
              font-weight: 600;
              font-family: PingFangSC-Light, sans-serif;
            ">
            欢迎注册
          </div>
          <el-form
            :model="RegisterForm"
            :rules="RegisterRules"
            ref="RegisterForm"
            class="demo-ruleForm">
            <el-form-item prop="name">
              <el-input
                placeholder="请输入用户名"
                v-model="RegisterForm.name"
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="number">
              <el-input
                placeholder="请输入学号"
                v-model="RegisterForm.number"
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                type="password"
                placeholder="请输入密码"
                v-model="RegisterForm.password"
                show-password
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="mail">
              <el-input
                placeholder="请输入邮箱"
                v-model="RegisterForm.mail"
                autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <div class="captcha">
                <div>
                  <el-input
                    placeholder="请输入验证码"
                    v-model="RegisterForm.captcha"
                    show-password
                    autocomplete="off"></el-input>
                </div>

                <div style="width: 80px; margin-left: 10px">
                  <el-button
                    :disabled="isDisabled"
                    style="
                      color: #fff;
                      border-radius: 10px;
                      height: 40px;
                      width: 120px;
                      font-size: 16px;
                    "
                    type="primary"
                    @click="getCaptchaRegister('RegisterForm')">
                    <template>
                      <span v-if="isDisabled">倒计时: {{ countDown }}</span>
                      <span v-else>获取验证码</span>
                    </template>
                  </el-button>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button
                class="captchaButton"
                style="
                  width: 300px;
                  height: 50px;
                  color: #fff;
                  border-radius: 10px;
                  font-size: 16px;
                "
                type="primary"
                @click="register('RegisterForm')"
                >提交
              </el-button>
            </el-form-item>
          </el-form>
          <!-- <div
            class="loginBottom"
            style="font-size: 14px; cursor: pointer; font-weight: 500"
          >
            <span @click="toggleForm(2)">忘记密码</span>
            <el-divider
              direction="vertical"
              content-position="center"
            ></el-divider>
            <span @click="toggleForm(3)">立即注册</span>
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import axios from "axios";
import "element-ui/lib/theme-chalk/index.css";
import { useUserStore } from "../../public/stores/modules/user";
import { mapState, mapActions } from "pinia";
import router from "../../public/router";
import api from "@/common/api";
import { Message } from "element-ui";
export default {
  // setup() {
  //   const userStore = useUserStore();
  //   return { userStore };
  // },
  computed: {
    ...mapState(useUserStore, ["token"]),
  },
  data: function () {
    const validateNumber = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入学号"));
      } else {
        if (this.LoginRuleForm.password !== "") {
          this.$refs.LoginRuleForm.validateField("password");
        }
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    };
    return {
      timer: null,
      countDown: 60,
      isDisabled: false,
      flag: 1,
      isLoginFormVisible: true,
      displayFlag: false,
      LoginRuleForm: {
        number: "",
        password: "",
      },
      changePasswordForm: {
        number: "",
        newPassword: "",
        captcha: "",
      },
      RegisterForm: {
        name: "",
        number: "",
        password: "",
        mail: "",
        captcha: "",
      },
      rules: {
        number: [{ validator: validateNumber, trigger: "blur" }],
        password: [{ validator: validatePassword, trigger: "blur" }],
      },
      RegisterRules: {
        name: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        number: [{ required: true, message: "请输入学号", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        captcha: [{ required: true, message: "请输入验证码", trigger: "blur" }],
        mail: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
      },
      changePasswordRules: {
        number: [{ required: true, message: "请输入学号", trigger: "blur" }],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "change" },
        ],
        captcha: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
    };
  },
  methods: {
    ...mapActions(useUserStore, ["setUserInfo", "setToken"]),

    toggleForm(flag) {
      this.flag = flag;
    },
    getCaptcha(formName) {
      let ok = false;
      this.$refs[formName].validateField(
        ["number", "newPassword"],
        (errors) => {
          if (errors) {
            console.log("校验失败", errors);
          } else {
            ok = true;
          }
        }
      );
      if (ok === true) {
        api.getCaptcha(this.changePasswordForm.number).then((response) => {
          if (response.data.code === "200") {
            this.isDisabled = true;
            Message.success("验证码已发送");
            this.startCountdown();
          }
        });
      }
    },
    startCountdown() {
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.countDown = 60;
      this.timer = setInterval(() => {
        this.countDown--;
        console.log(this.countDown);

        if (this.countDown === 0) {
          clearInterval(this.timer);
          this.timer = null;
          this.isDisabled = false;
        }
      }, 1000);
    },
    getCaptchaRegister(formName) {
      console.log("aaaa");
      let ok = true;
      this.$refs[formName].validateField(
        ["name", "number", "password", "mail"],
        (errors) => {
          if (errors) {
            ok = false;
          }
        }
      );
      if (ok === true) {
        api
          .getCaptchaRegister({
            number: this.RegisterForm.number,
            mail: this.RegisterForm.mail,
            userName: this.RegisterForm.name,
          })
          .then((response) => {
            if (response.data.code === "200") {
              Message.success("验证码已发送");
              this.isDisabled = true;
              this.startCountdown();
            }
          });
      }
    },
    register(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          api.register(this.RegisterForm).then((response) => {
            if (response.data.code === "200") {
              this.flag = 1;
              Message.success("注册成功");
            }
          });
        }
      });
    },
    submitchangePasswordForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          api
            .resetPassword(this.changePasswordForm)
            .then((response) => {
              if (response.data.code === "200") {
                Message.success("修改密码成功");
                this.flag = 1;
              }
            })
            .catch((error) => {
              console.log(error);
              this.displayFlag = true;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          api
            .login(this.LoginRuleForm)
            .then((response) => {
              if (response.data.code === "200") {
                // window.localStorage.setItem("token", response.data.data.token);
                this.setToken(response.data.data.token);
                this.setUserInfo(response.data.data.userInfo);
               
                router.push("/home");
              } else {
                // localStorage.removeItem("token");
                this.displayFlag = true;
              }
            })
            .catch((error) => {
              console.log(error);
              this.displayFlag = true;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.loginBottom {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
.loginDiv {
  width: 300px;
  height: 300px;
}
.loginLeft {
  /* background-color: red; */
  background: url("https://xiaoxin18.oss-cn-hangzhou.aliyuncs.com/thinkcoder/logo.png");
  border-radius: 20px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  /* margin-top:20px;
  margin-bottom: ; */
  width: 500px;
  height: 500px;
}
.loginRight {
  /* background-color: green; */
  width: 500px;
  height: 500px;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
#appLogin {
  border-radius: 20px;
  /* background-color: #fff; */
  width: 1000px;

  align-items: center;
  height: 500px;
  margin: 0 auto;
  display: flex;

  /* margin-top: 900px; */
  background-color: #ffffff;
}

.container {
  top: 0;
  left: 0;
  font-family: Arial, serif;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  /* background: url("@/assets/bg.jpg"); */
  background: #f5f6fa !important;
  position: fixed;
  width: 100%;
  height: 100%;
  /* background-repeat: no-repeat;
  background-position: center;
  background-size: cover; */
}
/* .el-form-item__label {
  color: red; 
}
  */
.captcha {
  display: flex;
}
::v-deep .captcha .el-input__inner {
  width: 170px;
  height: 50px;
}
::v-deep .el-input__inner {
  /* color: black; */
  /* background-color: rgba(24, 23, 23, 0.1) !important; */
  width: 300px;
  height: 50px;
  border-radius: 10px;
  font-size: 16px;
}
::v-deep .registerDiv .el-input__inner {
  /* width: 200px; */
  height: 40px;
}
.loginBottom > .el-divider--vertical {
  height: 15px;
}
.el-divider--vertical {
  height: 400px;
}
/* ::v-deep .el-input__inner::placeholder {
  font-family: Arial, serif;
  color: rgb(128, 128, 128);
} */
</style>
