<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>控制面板</h2>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #409EFF;">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-title">用户总数</div>
              <div class="stat-value">{{ userCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #67C23A;">
              <i class="el-icon-s-order"></i>
            </div>
            <div class="stat-info">
              <div class="stat-title">题目总数</div>
              <div class="stat-value">{{ problemCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #E6A23C;">
              <i class="el-icon-s-cooperation"></i>
            </div>
            <div class="stat-info">
              <div class="stat-title">竞赛总数</div>
              <div class="stat-value">{{ contestCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #F56C6C;">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-title">文章总数</div>
              <div class="stat-value">{{ articleCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>用户注册趋势</span>
          </div>
          <div style="height: 350px;">
            <v-chart :options="userRegTrend" autoresize></v-chart>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>题目提交统计</span>
          </div>
          <div style="height: 350px;">
            <v-chart :options="submissionStats" autoresize></v-chart>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 最近活动 -->
    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span>最近活动</span>
          </div>
          <el-table :data="recentActivities" style="width: 100%">
            <el-table-column prop="time" label="时间" width="180"></el-table-column>
            <el-table-column prop="user" label="用户"></el-table-column>
            <el-table-column prop="activity" label="活动"></el-table-column>
            <el-table-column prop="ip" label="IP地址"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "AdminDashboard",
  data() {
    return {
      // 统计数据
      userCount: 1258,
      problemCount: 856,
      contestCount: 48,
      articleCount: 325,
      
      // 用户注册趋势图表配置
      userRegTrend: {
        title: {
          text: '近30天用户注册趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.getLast30Days()
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [10, 15, 8, 12, 7, 9, 13, 17, 21, 16, 15, 14, 18, 20, 16, 12, 11, 9, 13, 16, 19, 21, 23, 15, 12, 10, 14, 16, 19, 22],
          type: 'line',
          smooth: true
        }]
      },
      
      // 题目提交统计图表配置
      submissionStats: {
        title: {
          text: '题目提交结果分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['通过', '编译错误', '答案错误', '超时', '内存溢出', '运行错误']
        },
        series: [
          {
            name: '提交结果',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 458, name: '通过' },
              { value: 182, name: '编译错误' },
              { value: 234, name: '答案错误' },
              { value: 135, name: '超时' },
              { value: 87, name: '内存溢出' },
              { value: 124, name: '运行错误' }
            ]
          }
        ]
      },
      
      // 最近活动数据
      recentActivities: [
        { time: '2024-06-01 10:23:45', user: '张三', activity: '登录系统', ip: '192.168.1.1' },
        { time: '2024-06-01 09:45:23', user: '李四', activity: '提交题目#1024', ip: '192.168.1.2' },
        { time: '2024-06-01 09:30:10', user: '王五', activity: '发布文章《算法入门》', ip: '192.168.1.3' },
        { time: '2024-06-01 09:15:39', user: '赵六', activity: '参加竞赛#42', ip: '192.168.1.4' },
        { time: '2024-06-01 08:55:22', user: '孙七', activity: '注册账号', ip: '192.168.1.5' }
      ]
    };
  },
  methods: {
    // 获取最近30天的日期
    getLast30Days() {
      const days = [];
      for (let i = 29; i >= 0; i--) {
        const date = new Date();
        date.setDate(date.getDate() - i);
        days.push(`${date.getMonth() + 1}/${date.getDate()}`);
      }
      return days;
    }
  }
};
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-header {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  font-size: 32px;
  margin-right: 15px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-title {
  font-size: 14px;
  color: #909399;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-top: 5px;
}
</style> 