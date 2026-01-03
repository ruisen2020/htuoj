import axios from "axios";
import { Message } from "element-ui";
import { useUserStore } from "../../public/stores/modules/user";
import router from "../../public/router";
const api = axios.create({
  // baseURL: "http://htuoj.cn:9001",
  baseURL: "http://localhost:9001",
  // headers: {
  //   "Content-Type"Y: "application/json",
  // },
});
api.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    const token = userStore.getToken;
    if (token) {
      config.headers.satoken = `${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    if (response.data.code === "401") {
      Message.error(response.data.message);
      const userStore = useUserStore();
      userStore.reset();
    } else if (response.data.code === "B000002") {
      Message.error(response.data.message);
      window.open("/login", "_blank");
    } else if (response.data.code != "200") {
      Message.error(response.data.message);
    }
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      router.push("/");
    }
    return Promise.reject(error);
  }
);
const Api = {
  login(params) {
    return api.post("/user/login", params);
  },
  logout() {
    return api.get("/user/logout");
  },
  register(params) {
    return api.post("/user/register", params);
  },
  addTask(params) {
    return api.post("/task/addTask", params);
  },
  getAllTask(params) {
    return api.post("/task/getAll", params);
  },
  completedTask(taskId) {
    return api.post("/task/completedTask", taskId);
  },

  getUserPreferencesInfo(userId) {
    return api.get("/user-preferences/getUserPreferencesInfo?userId=" + userId);
  },
  getRatingInfo(userId) {
    return api.get("/user-preferences/getRatingInfo?userId=" + userId);
  },
  getCaptcha(number) {
    return api.get("/user/getCaptcha?number=" + number);
  },
  resetPassword(params) {
    return api.post("/user/resetPassword", params);
  },
  getCaptchaRegister(params) {
    return api.post("/user/getCaptchaRegister", params);
  },
  getUserInfo(userId) {
    return api.get("/user/getUserInfo?userId=" + userId);
  },
  updateUserAllInfo(params) {
    return api.post("/user/updateUserAllInfo", params);
  },

  getSumissionList(params) {
    return api.post("/submission/getSubmissionList", params);
  },

  getUserList(params) {
    return api.post("/user/getAll", params);
  },
  getUserTopList(params) {
    return api.post("/user/getUserTopList", params);
  },
  getArticleList(params) {
    return api.post("/article/getArticleList", params);
  },

  addLike(params) {
    return api.post("/like/addLike", params);
  },
  addCollect(params) {
    return api.post("/collect/addCollect", params);
  },
  getArticleById(params) {
    return api.get("/article/getArticleById?articleId=" + params);
  },
  uploadArticlePictureAndFile(param) {
    return api.post("/file/uploadArticlePictureAndFile", param);
  },
  follow(param) {
    return api.post("/follow/followOther", param);
  },
  cancelFollow(param) {
    return api.post("/follow/cancelFollow", param);
  },
  getCommentList(params) {
    return api.post("/comment/getCommentList", params);
  },
  addComment(params) {
    return api.post("/comment/addComment", params);
  },
  getTopWatchArticleByUserID(params) {
    return api.post("/article/getTopWatchArticleByUserID", params);
  },
  deleteArticle(params) {
    return api.post("/article/deleteArticle", params);
  },

  deleteComment(params) {
    return api.post("/comment/deleteComment", params);
  },
  getFollowCount(params) {
    return api.get("/follow/getFollowCount?id=" + params);
  },
  getFollowInfo(params) {
    return api.get("/follow/getFollowInfo?userId=" + params);
  },
  getAwardInfoByUserId(param) {
    return api.get("/award-info/getByUserId?studentNumber=" + param);
  },
  updateAvatar(params) {
    return api.post("/user/updateAvatar", params);
  },
  getTrainingList(params) {
    return api.post("/training/getTrainingList", params);
  },
  getOfficialTrainingList(params) {
    return api.post("/training/getOfficialTrainingList", params);
  },
  getProblemListByTrainingId(params) {
    return api.post("/training-problem/getProblemListByTrainingId", params);
  },
  getProblemById(params) {
    return api.get("/problem/getProblemById?problemId=" + params);
  },
  uploadArticleCover(params) {
    return api.post("/file/uploadArticleCover", params);
  },
  addArticle(params) {
    return api.post("/article/addArticle", params);
  },
  getArticleListByUserId(params) {
    return api.post("/article/getArticleListByUserId", params);
  },
  getCollectListByUseId(params) {
    return api.post("/collect/getCollectListByUseId", params);
  },
  getTrainingListByCollect(params) {
    return api.post("/training/getTrainingListByCollect", params);
  },
  getTrainingInfo(params) {
    return api.post("/training/getTrainingInfo", params);
  },

  getArticleListByCategory(params) {
    return api.post("/article/getArticleListByCategory", params);
  },
  getSubmissionListByProblemId(params) {
    return api.post("/submission/getSubmissionListByProblemId", params);
  },
  getSubmissionById(params) {
    return api.post("/submission/getSubmissionById", params);
  },
  addSubmission(params) {
    return api.post("/submission/addSubmission", params);
  },
  getLabelAll() {
    return api.get("/label/getAll");
  },
  getSourceAll() {
    return api.get("/source/getAll");
  },
  getProblemList(params) {
    return api.post("/problem/getProblemList", params);
  },
  getAwardInfoList(params) {
    return api.post("/award-info/getAll", params);
  },
  getLastSubmissionByProblemId(params) {
    return api.post("/submission/getLastSubmissionByProblemId", params);
  },
  getSampleByProbelmId(params) {
    return api.post("/sample/getSampleByProblemId", params);
  },
  addTestSubmission(params) {
    return api.post("/submission/addTestSubmission", params);
  },
  getContestListByOtherOJ() {
    return api.get("/contest/getContestListByOtherOJ");
  },
  sendAI(params) {
    return api.post("/ai/send", params);
  },
  getAvatar() {
    return api.get("/user/getAvatar");
  },
  checkLogin() {
    return api.get("/user/checkLogin");
  },
  sendHeartbeat() {
    return api.get("/user/sendHeartbeat");
  },
  getNoticeList(params) {
    return api.post("/notice/getNoticeList", params);
  },
  clearRead(params) {
    return api.post("/notice/clearRead", params);
  },
  getRedPoint() {
    return api.post("/notice/getRedPoint");
  },
  addTraining(params) {
    return api.post("/training/addTraining", params);
  },
  updateTraining(params) {
    return api.post("/training/updateTraining", params);
  },
  deleteTraining(params) {
    return api.post("/training/deleteTraining", params);
  },
  getProblemSimpleById(params) {
    return api.get("/problem/getProblemSimpleById?problemId=" + params);
  },
  updateTrainingProblemList(params) {
    return api.post("/training-problem/updateTrainingProblemList", params);
  },
  updateArticle(params) {
    return api.post("/article/updateArticle", params);
  },
  getTrackList() {
    return api.get("/award-info/getTrackList");
  },
  getCompetitionList() {
    return api.get("/award-info/getCompetitionList");
  },
  getContestList(params) {
    return api.post("/contest/getContestList", params);
  },
  handleRegister(params) {
    return api.post("/contestRegister/addRegister", params);
  },
  getContestById(params) {
    return api.post("/contest/getContestById", params);
  },
  getContestProblemList(params) {
    return api.post("/contestProblem/getContestProblemList", params);
  },
  checkRegister(params) {
    return api.get("/contestRegister/checkRegister/" + params);
  },
  getSubmissionListByContestId(params) {
    return api.post("/submission/getSubmissionListByContestId", params);
  },
  getContestProblemMap(params) {
    return api.get("/contestProblem/getContestProblemMap?contestId=" + params);
  },
  getRankList(params) {
    return api.post("/contestAccept/getRankList", params);
  },
  getSchoolList(params) {
    return api.post("/school/getSchoolList", params);
  },
  searchArticle(params) {
    return api.post("/search/searchArticle", params);
  },
};
let AApi = Object.assign(Api);
// export const login = (url, params) => {
//   return api.post(url, { params });
// };
export default AApi;
