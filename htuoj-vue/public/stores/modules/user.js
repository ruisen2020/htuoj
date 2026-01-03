import { defineStore } from "pinia";

export const useUserStore = defineStore("userStore", {
  state: () => {
    return {
      userInfo: null,
      token: null,
    };
  },
  getters: {
    getToken() {
      return this.token;
    },
    getUserId() {
      return this.userInfo.userId;
    },
    getUserName() {
      return this.userInfo.userName;
    },
    getUserAvatar() {
      return this.userInfo.avatar;
    },
    getUserNumber() {
      return this.userInfo.number;
    },
  },
  actions: {
    setUserInfo(userInfo) {
      this.userInfo = userInfo;
    },
    setToken(token) {
      this.token = token;
    },

    reset() {
      this.userInfo = null;
      this.token = null;
    },
  },
  persist: {
    enabled: true,
    storage: localStorage,
  },
});
