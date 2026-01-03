<template>
  <div style="width: 1200px; margin: 0 auto">
    <el-main>
      <div style="display: flex; justify-content: center">
        <el-card style="width: 800px; height: 100%">
          <div style="display: flex">
            <el-tabs
              v-model="activeNameRight"
              type="card"
              style="font-size: 18px; width: 760px"
              @tab-click="handleClickRight">
              <el-tab-pane label="题单简介" name="trainingProfile">
                <div
                  style="flex: 1; float: right"
                  v-if="
                    userInfo != null && userInfo.userId == trainingInfo.userId
                  ">
                  <el-button size="mini" @click="handleEdit()">编辑</el-button>
                  <el-popconfirm title="确定删除吗？" @confirm="handleDelete()">
                    <el-button
                      size="mini"
                      slot="reference"
                      type="danger"
                      style="margin-left: 10px"
                      >删除</el-button
                    >
                  </el-popconfirm>
                </div>
                <div>
                  <div id="markdown"></div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="题目列表" name="problemList">
                <div
                  style="flex: 1; float: right"
                  v-if="
                    userInfo != null && userInfo.userId == trainingInfo.userId
                  ">
                  <el-button size="mini" @click="handleEditProblem()"
                    >编辑</el-button
                  >
                </div>
                <el-table :data="problemList" stripe style="width: 100%">
                  <el-table-column prop="status" align="center" label="状态">
                    <template slot-scope="scope">
                      <div
                        style="
                          text-align: center;
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        ">
                        <span v-if="scope.row.status === null"></span>
                        <span
                          v-else-if="scope.row.status === 1"
                          style="
                            text-align: center;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                          ">
                          <!-- <i class="el-icon-circle-check"></i> -->
                          <svg
                            width="16"
                            height="16"
                            viewBox="0 0 16 16"
                            fill="none">
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M8 16a8 8 0 01-8-8 8 8 0 018-8 8 8 0 018 8 8 8 0 01-8 8zM5.552 7.948L7 9.397l3.948-3.949a.78.78 0 111.104 1.104l-4.5 4.5a.78.78 0 01-1.104 0l-2-2a.78.78 0 111.104-1.104z"
                              fill="#00AA54" />
                          </svg>
                        </span>
                        <span
                          v-else-if="scope.row.status === 2"
                          style="
                            text-align: center;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                          ">
                          <!-- <i class="el-icon-circle-close"></i> -->
                          <svg
                            width="16"
                            height="16"
                            viewBox="0 0 16 16"
                            fill="none">
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M0 8a8 8 0 008 8 8 8 0 008-8 8 8 0 00-8-8 8 8 0 00-8 8zm9.593-2.65a.75.75 0 111.06 1.061l-1.59 1.591 1.59 1.591a.75.75 0 11-1.06 1.06l-1.591-1.59-1.591 1.59a.75.75 0 11-1.06-1.06l1.59-1.591-1.59-1.591a.75.75 0 011.06-1.06l1.591 1.59 1.591-1.59z"
                              fill="#F04142" />
                          </svg>
                        </span>
                      </div>
                    </template>
                    <!-- <i class="el-icon-circle-check"></i> -->

                    <!-- <el-icon-circle-close></el-icon-circle-close>  -->
                    <!-- </template> -->
                  </el-table-column>

                  <el-table-column prop="problemId" align="center" label="编号">
                  </el-table-column>

                  <el-table-column prop="title" align="center" label="标题">
                    <template slot-scope="scope">
                      <el-link
                        target="_blank"
                        :underline="false"
                        :href="generateLink(scope.row.problemId)">
                        {{ scope.row.title }}
                      </el-link>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="souresList"
                    align="center"
                    label="来源">
                    <template slot-scope="scope">
                      <span
                        v-for="(item, index) in scope.row.souresList"
                        :key="index">
                        <el-tag
                          :key="index"
                          style="margin-right: 5px"
                          type="success"
                          size="mini">
                          {{ item }}
                        </el-tag>
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="labelList"
                    align="center"
                    label="算法标签">
                    <!-- 遍历标签集合 -->
                    <template slot-scope="scope">
                      <span
                        v-for="(item, index) in scope.row.labelList"
                        :key="index">
                        <el-tag
                          :key="index"
                          style="margin-right: 5px"
                          type="primary"
                          size="mini">
                          {{ item }}
                        </el-tag></span
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="passRate"
                    align="center"
                    label="通过率">
                  </el-table-column>
                  <el-table-column
                    prop="difficultyLevel"
                    align="center"
                    label="难度">
                    <template slot-scope="scope">
                      <span
                        v-if="scope.row.difficultyLevel === 0"
                        style="color: #5cb85c">
                        简单
                      </span>
                      <span
                        v-else-if="scope.row.difficultyLevel === 1"
                        style="color: #f0ad4e">
                        中等
                      </span>
                      <span v-else style="color: #d9534f"> 困难 </span>
                    </template>
                  </el-table-column>
                  <!-- <el-table-column prop="problemId" align="center" label="编号">
                      <template slot-scope="scope">
                        <el-button>
                          {{ scope.row.problemId }}
                        </el-button>
                      </template>
                    </el-table-column> -->
                </el-table>
                <el-pagination
                  @current-change="handleCurrentChangeRight"
                  :current-page.sync="problemFormData.current"
                  background
                  :page-size="20"
                  layout="prev, pager, next"
                  :total="totalRight"
                  style="margin-top: 10px">
                </el-pagination>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
        <el-card style="margin-left: 20px; width: 300px">
          <div
            style="
              font-size: 24px;
              font-weight: bold;
              display: flex;
              align-items: center;
              justify-content: center;
            ">
            {{ trainingInfo.title }}
            <span
              @click="collect(trainingInfo.trainingId)"
              v-if="trainingInfo.isCollect"
              style="
                color: #ffa116;
                display: flex;
                justify-content: center;
                align-items: center;
                cursor: pointer;
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
              @click="collect(trainingInfo.trainingId)"
              v-else
              style="
                cursor: pointer;
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
            style="
              display: flex;
              align-items: center;
              text-align: center;
              justify-content: center;
              gap: 30px;
              margin-top: 20px;
              /* line-height: 30px; */
            ">
            <div style="">
              <div style="font-size: 14px; color: #262626bf">创建者</div>
              <div style="color: #000">
                <el-link
                  style="font-size: 16px; line-height: 16px; color: #3172cc"
                  :href="'/home/stuInfo/' + trainingInfo.userId"
                  target="_blank"
                  :underline="false"
                  >{{ trainingInfo.userName }}</el-link
                >
              </div>
            </div>
            <div>
              <div style="font-size: 14px; color: #262626bf">题单类型</div>
              <div style="font-size: 16px; color: #3172cc">
                <span v-if="trainingInfo.type == 1">官方题单</span>
                <span v-else> 个人题单 </span>
              </div>
            </div>
            <div>
              <div style="font-size: 14px; color: #262626bf">收藏次数</div>
              <div style="font-size: 16px; color: #3172cc">
                <span> {{ trainingInfo.collectCount }}</span>
              </div>
            </div>
          </div>
          <div
            class="left"
            style="display: flex; align-items: center; justify-content: center">
            <div id="chartLeft" style="width: 260px; height: 260px"></div>
          </div>
          <!-- <div
            style="
              text-align: center;
              font-size: 20px;
              margin-top: 20px;
              color: #5eadff;
            ">
            已完成
          </div>
          <div
            class="left"
            style="display: flex; align-items: center; justify-content: center">
             <div
              id="container"
              style="width: 260px; height: 260px; margin-top: 20px"></div>
          </div>  -->
        </el-card>
      </div>
      <div v-show="false">{{ trainingInfo.trainingId }}</div>
    </el-main>
    <el-dialog
      title="编辑题单"
      :visible.sync="dialogVisible"
      width="50%"
      @open="initializeEditor"
      @close="handleDialogClose"
      :before-close="handleClose">
      <span style="align-items: center">
        <el-form
          ref="trainingUpdateInfo"
          :rules="rules"
          :model="trainingUpdateInfo"
          label-width="80px">
          <el-form-item prop="title" label="题单名称">
            <el-input
              v-model="trainingUpdateInfo.title"
              placeholder="请输入题单名称"></el-input>
          </el-form-item>
          <el-form-item label="题单描述">
            <div
              style="height: 60%; min-height: 500px; margin-top: 20px"
              id="markdownTraining"></div>
          </el-form-item>
        </el-form>
      </span>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateTraining">修 改</el-button>
      </span>
    </el-dialog>
    <el-dialog
      @open="rowDrop"
      title="题目编排"
      :key="dialogKey"
      @close="handleDialogClose"
      :visible.sync="dialogVisibleProblem"
      width="50%">
      <div style="align-items: center; font-size: 16px">
        <span>搜索题目</span>
        <el-input
          v-model="problemId"
          placeholder="请输入确切的题目编号"
          style="width: 40%; margin-left: 20px">
        </el-input>
        <span>
          <el-button
            @click="addProblem"
            style="margin-left: 20px"
            type="primary"
            >添加</el-button
          >
        </span>
      </div>
      <div style="align-items: center; font-size: 16px; margin-top: 20px">
        <div>
          <span>题目排序</span>
          <span>
            <el-table
              ref="table"
              class="draggable-table"
              :data="problemList"
              stripe
              style="width: 100%"
              :key="problemList.order">
              <el-table-column width="50" align="center">
                <template slot="default" slot-scope="scope">
                  <i class="el-icon-rank drag-handle" :key="scope.$index"></i>
                </template>
              </el-table-column>
              <!-- <el-table-column align="center" label="序号">
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column> -->

              <el-table-column prop="problemId" align="center" label="编号">
              </el-table-column>

              <el-table-column prop="title" align="center" label="标题">
                <template slot-scope="scope">
                  <el-link
                    target="_blank"
                    :underline="false"
                    :href="generateLink(scope.row.problemId)">
                    {{ scope.row.title }}
                  </el-link>
                </template>
              </el-table-column>
              <el-table-column prop="souresList" align="center" label="来源">
                <template slot-scope="scope">
                  <span
                    v-for="(item, index) in scope.row.souresList"
                    :key="index">
                    <el-tag
                      :key="index"
                      style="margin-right: 5px"
                      type="success"
                      size="mini">
                      {{ item }}
                    </el-tag>
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="labelList" align="center" label="算法标签">
                <!-- 遍历标签集合 -->
                <template slot-scope="scope">
                  <span
                    v-for="(item, index) in scope.row.labelList"
                    :key="index">
                    <el-tag
                      :key="index"
                      style="margin-right: 5px"
                      type="primary"
                      size="mini">
                      {{ item }}
                    </el-tag></span
                  >
                </template>
              </el-table-column>
              <el-table-column prop="passRate" align="center" label="通过率">
              </el-table-column>
              <el-table-column
                prop="difficultyLevel"
                align="center"
                label="难度">
                <template slot-scope="scope">
                  <span
                    v-if="scope.row.difficultyLevel === 0"
                    style="color: #5cb85c">
                    简单
                  </span>
                  <span
                    v-else-if="scope.row.difficultyLevel === 1"
                    style="color: #f0ad4e">
                    中等
                  </span>
                  <span v-else style="color: #d9534f"> 困难 </span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                  <el-button
                    type="danger"
                    size="mini"
                    icon="el-icon-delete"
                    @click="deleteProblem(scope.$index)"
                    >删除</el-button
                  >
                </template>
              </el-table-column>
              <!-- <el-table-column prop="problemId" align="center" label="编号">
                      <template slot-scope="scope">
                        <el-button>
                          {{ scope.row.problemId }}
                        </el-button>
                      </template>
                    </el-table-column> -->
            </el-table></span
          >
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleProblem = false">取 消</el-button>
        <el-button type="primary" @click="updateTrainingProblemList"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from "echarts";
import api from "@/common/api";
import { useUserStore } from "../../public/stores/modules/user";
import { mapState } from "pinia";
import Sortable from "sortablejs";
// import { Liquid } from "@antv/g2plot";
import Cherry from "cherry-markdown/dist/cherry-markdown.core";
export default {
  data() {
    return {
      dialogKey: false,
      rules: {
        title: [{ required: true, message: "请输入题单名称", trigger: "blur" }],
      },
      dialogVisibleProblem: false,
      tainingCherry: null,

      trainingUpdateInfo: {
        trainingId: "",
        title: "",
        description: "",
      },
      dialogVisible: false,
      cherry: null,
      activeNameRight: "trainingProfile",
      trainingInfo: null,
      totalRight: 0,
      problemFormData: {
        current: 1,
        size: 20,
        trainingId: null,
      },
      problemId: null,
      trainingId: this.$route.params.trainingId,
      problemList: [],
      isInitChart: false,
      liquidChart: null,
    };
  },
  computed: {
    ...mapState(useUserStore, ["userInfo"]),
  },

  mounted() {
    this.getTrainingInfo();
    this.rowDrop();
  },
  updated() {
    this.initChart();

    if (this.cherry == null) {
      this.cherry = new Cherry({
        id: "markdown",
        value: this.trainingInfo.description,
        themeSettings: {
          codeBlockTheme: "one dark",
        },
        editor: {
          defaultModel: "previewOnly",
        },
      });
    } else {
      this.cherry.setMarkdown(this.trainingInfo.description);
    }
  },
  methods: {
    collect(trainingId) {
      this.trainingInfo.isCollect = !this.trainingInfo.isCollect;
      api
        .addCollect({ targetType: 2, targetId: trainingId })
        .then((response) => {
          if (response.data.code == "200") {
            // this.trainingInfo.isCollect = !this.trainingInfo.isCollect;
          } else {
            this.$message({
              message: "收藏失败",
              type: "error",
            });
          }
        });
    },
    deleteProblem(index) {
      console.log(index);
      this.problemList.splice(index, 1);
    },
    updateTrainingProblemList() {
      var trainingProblemList = [];
      for (var i = 0; i < this.problemList.length; i++) {
        trainingProblemList.push(this.problemList[i].problemId);
      }
      api
        .updateTrainingProblemList({
          problemIds: trainingProblemList,
          trainingId: this.trainingId,
        })
        .then((response) => {
          if (response.data.code == "200") {
            this.$message({
              message: "更新成功",
              type: "success",
            });

            this.dialogVisibleProblem = false;
            this.getTrainingInfo();
            this.getProblemListByTrainingId();
            this.rowDrop();
            this.initChart();
          }
        });
    },
    addProblem() {
      api.getProblemSimpleById(this.problemId).then((response) => {
        if (response.data.code == "200") {
          var problem = response.data.data;
          problem.souresList = problem.sources
            ? problem.sources.split(",")
            : [];
          problem.labelList = problem.labels ? problem.labels.split(",") : [];
          this.problemList.push(problem);
          this.problemId = null;
        }
      });
    },
    rowDrop() {
      const tbody = document.querySelector(
        ".draggable-table  .el-table__body-wrapper tbody"
      );
      Sortable.create(tbody, {
        animation: 150,
        // chosenClass: "chosen",
        ghostClass: "sortable-ghost", //拖拽样式
        handle: ".drag-handle",
        onEnd: ({ newIndex, oldIndex }) => {
          var temp = this.problemList[oldIndex];
          var i;
          for (i = oldIndex + 1; i < this.problemList.length; i++) {
            this.problemList[i - 1] = this.problemList[i];
          }
          for (i = this.problemList.length - 2; i >= newIndex; i--) {
            this.problemList[i + 1] = this.problemList[i];
          }
          this.problemList[newIndex] = temp;
          console.log(
            "Updated problemList:",
            this.problemList,
            newIndex,
            oldIndex
          ); // 添加日志
          // this.$nextTick(() => {
          //   this.$refs.table.doLayout(); // 强制更新表格布局
          //   this.$forceUpdate(); // 强制更新组件
          //   // this.$refs.table.bodyWrapper.scrollTop = 0; // 重置滚动条位置
          //   this.rowDrop();
          // });
        },
      });
    },
    handleDialogClose() {
      this.getProblemListByTrainingId();
    },
    updateTraining() {
      this.$refs["trainingUpdateInfo"].validate(async (valid) => {
        if (valid) {
          this.trainingUpdateInfo.description =
            this.tainingCherry.getMarkdown();
          await api.updateTraining(this.trainingUpdateInfo).then((response) => {
            if (response.data.code === "200") {
              this.$message({
                type: "success",
                message: "修改成功",
              });
              this.dialogVisible = false;
              this.getTrainingInfo();
            }
          });
        }
      });
    },
    initializeEditor() {
      this.$nextTick(() => {
        if (this.tainingCherry) {
          return;
        }
        this.tainingCherry = new Cherry({
          id: "markdownTraining",
          value: this.trainingUpdateInfo.description,
          themeSettings: {
            codeBlockTheme: "one dark",
          },
        });
      });
    },
    handleEditProblem() {
      this.dialogKey = !this.dialogKey;
      this.dialogVisibleProblem = true;
      this.$nextTick(() => {
        this.getTrainingInfo();
        this.getProblemListByTrainingId();
        this.rowDrop();
        this.initChart();
      });
    },
    handleDelete() {
      api.deleteTraining({ trainingId: this.trainingId }).then((response) => {
        if (response.data.code === "200") {
          this.$message({
            type: "success",
            message: "删除成功",
          });
          this.$router.push({
            path: "/home/trainingInfo",
          });
        }
      });
    },
    handleEdit() {
      this.dialogVisible = true;
    },
    async getTrainingInfo() {
      await api
        .getTrainingInfo({ trainingId: this.trainingId })
        .then((response) => {
          this.trainingInfo = response.data.data;
          this.trainingUpdateInfo.trainingId = this.trainingInfo.trainingId;
          this.trainingUpdateInfo.title = this.trainingInfo.title;
          this.trainingUpdateInfo.description = this.trainingInfo.description;
        });
    },
    initChart() {
      this.isInitChart = true;
      const myChartLeft = echarts.init(document.getElementById("chartLeft"));
      const optionLeft = {
        title: {
          text: "题单统计",
          subtext: "总共" + this.trainingInfo.problemCount + "道",

          top: "20%",
          x: "center",
          y: "center",
          textStyle: {
            color: "#5cb85c",
            fontSize: 20,
          },
        },
        tooltip: null,
        legend: {
          // orient: "vertical",
          x: "center",
          y: "bottom",
          itemWidth: 30,
        },

        color: ["#5cb85c", "#f0ad4e", "#d9534f"],
        series: [
          {
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
            },
            padAngle: 5,
            name: "Access From",
            type: "pie",
            radius: ["40%", "70%"],
            center: ["50%", "80%"],
            // adjust the start and end angle
            startAngle: 180,
            endAngle: 360,
            labelLine: {
              show: false,
            },
            data: [
              {
                value: this.trainingInfo.easyCount,
                name: "简单" + this.trainingInfo.easyCount,
              },
              {
                value: this.trainingInfo.mediumCount,
                name: "中等" + this.trainingInfo.mediumCount,
              },
              {
                value: this.trainingInfo.hardCount,
                name: "困难" + this.trainingInfo.hardCount,
              },
            ],
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: "bold",
              },
            },
          },
        ],
      };
      myChartLeft.setOption(optionLeft);
    },
    generateLink(userId) {
      return "/problem/" + userId;
    },
    handleCurrentChangeRight(current) {
      this.problemFormData.current = current;
      this.getOfficialTrainingList();
    },

    getProblemListByTrainingId() {
      this.problemFormData.trainingId = this.trainingId;
      api.getProblemListByTrainingId(this.problemFormData).then((response) => {
        this.problemList = response.data.data.records;
        this.totalRight = response.data.data.total;
        for (let i = 0; i < this.problemList.length; i++) {
          this.problemList[i].souresList = this.problemList[i].sources
            ? this.problemList[i].sources.split(",")
            : [];
          this.problemList[i].labelList = this.problemList[i].labels
            ? this.problemList[i].labels.split(",")
            : [];
        }
      });
    },
    handleClickRight(tab) {
      if (tab.name === "problemList") {
        this.getProblemListByTrainingId();
      }
    },
  },
};
</script>

<style scoped>
.drag-handle {
  cursor: move;
}
::v-deep .el-dialog {
  border-radius: 20px;
}
::v-deep #tab-userShare1 {
  float: right;
  margin-left: 200px;
}
::v-deep .cherry {
  box-shadow: none;
}
::v-deep .cherry-previewer {
  border: none;
  padding: 10px;
}
::v-deep .cherry-drag {
  width: 5px;
  background-color: #ebedee;
}
::v-deep .cherry-markdown a.anchor:before {
  content: "" !important;
}
::v-deep .el-table .cell {
  cursor: pointer;
}

::v-deep .el-progress-bar__innerText {
  color: #fff !important;
}

::v-deep .el-card {
  border-radius: 13px;
  /* box-shadow: none; */
  border: none;
}
::v-deep .el-card__body {
  border-radius: 13px;
}
::v-deep .el-card .is-always-shadow {
  box-shadow: none;
}
::v-deep .el-tabs__item {
  color: #262626bf;
  border-radius: 8px;
  /* //margin-left: 10px; border: 0 solid; */
}

::v-deep .el-tabs--card > .el-tabs__header .el-tabs__item {
  font-size: 16px;
  border-left: none;
}

::v-deep .el-tabs--card > .el-tabs__header {
  border: none;
}

/**选中 */
::v-deep .el-tabs__item.is-active {
  background-color: #000a200d;
  border: none;
}

::v-deep .el-table--border .el-table__cell {
  border-right: none !important;
}

::v-deep .el-tabs__nav,
.is-top {
  border: none !important;
}
</style>
