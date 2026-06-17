﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<template>
  <div class="vote-detail">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>议题详情</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <div v-loading="loading" class="detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="议题标题" :span="2">{{ detail.title }}</el-descriptions-item>
          <el-descriptions-item label="议题类型">
            <el-tag :type="getTypeTagType(detail.type)">{{ getTypeLabel(detail.type) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(detail.status)">{{ getStatusLabel(detail.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="表决方式">{{ getVoteTypeLabel(detail.voteType) }}</el-descriptions-item>
          <el-descriptions-item label="表决人数">{{ detail.voteCount }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
          <el-descriptions-item label="截止时间">{{ detail.endTime }}</el-descriptions-item>
          <el-descriptions-item label="议题描述" :span="2">
            <div class="description-content">{{ detail.description }}</div>
          </el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">表决结果统计</el-divider>

        <div class="chart-container">
          <div ref="chartRef" class="chart"></div>
        </div>

        <el-divider content-position="left">表决记录</el-divider>

        <el-table :data="voteRecords" border stripe>
          <el-table-column prop="ownerName" label="业主姓名" width="120" />
          <el-table-column prop="buildingNo" label="楼栋号" width="100" />
          <el-table-column prop="roomNo" label="房间号" width="100" />
          <el-table-column prop="voteOption" label="表决选项" width="100">
            <template #default="{ row }">
              <el-tag :type="getVoteOptionTagType(row.voteOption)">{{ getVoteOptionLabel(row.voteOption) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="voteTime" label="表决时间" width="180" />
          <el-table-column prop="remark" label="备注" />
        </el-table>

        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchVoteRecords"
          @current-change="fetchVoteRecords"
          class="pagination"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const chartRef = ref(null)
let chartInstance = null

const detail = reactive({
  id: null,
  title: '',
  type: '',
  status: '',
  description: '',
  voteType: '',
  voteCount: 0,
  createTime: '',
  endTime: ''
})

const voteRecords = ref([])
const voteStats = reactive({
  agree: 0,
  disagree: 0,
  abstain: 0
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const initChart = () => {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  const option = {
    title: {
      text: '表决结果分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['赞成', '反对', '弃权']
    },
    series: [
      {
        name: '表决结果',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: voteStats.agree, name: '赞成', itemStyle: { color: '#67C23A' } },
          { value: voteStats.disagree, name: '反对', itemStyle: { color: '#F56C6C' } },
          { value: voteStats.abstain, name: '弃权', itemStyle: { color: '#909399' } }
        ]
      }
    ]
  }
  chartInstance.setOption(option)
}

const fetchDetail = async (id) => {
  loading.value = true
  try {
    const res = await request.get(`/vote-topics/${id}`)
    Object.assign(detail, res.data)
  } catch (error) {
    ElMessage.error('获取详情失败')
  } finally {
    loading.value = false
  }
}

const fetchVoteStats = async (id) => {
  try {
    const res = await request.get(`/vote-records/result/${id}`)
    Object.assign(voteStats, res.data)
    await nextTick()
    initChart()
  } catch (error) {
    ElMessage.error('获取统计数据失败')
  }
}

const fetchVoteRecords = async () => {
  try {
    const res = await request.get(`/vote-records`, {
      params: {
        topicId: route.params.id,
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize
      }
    })
    voteRecords.value = res.data.list
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('获取表决记录失败')
  }
}

const handleResize = () => {
  chartInstance?.resize()
}

const handleBack = () => {
  router.back()
}

const getTypeLabel = (type) => {
  const map = {
    construction: '施工方案',
    equipment: '设备采购',
    finance: '财务预算',
    regulation: '规章制度',
    other: '其他'
  }
  return map[type] || type
}

const getTypeTagType = (type) => {
  const map = {
    construction: '',
    equipment: 'success',
    finance: 'warning',
    regulation: 'danger',
    other: 'info'
  }
  return map[type] || ''
}

const getStatusLabel = (status) => {
  const map = {
    pending: '待表决',
    voting: '表决中',
    passed: '已通过',
    rejected: '已否决',
    closed: '已关闭'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    pending: 'info',
    voting: 'warning',
    passed: 'success',
    rejected: 'danger',
    closed: ''
  }
  return map[status] || ''
}

const getVoteTypeLabel = (type) => {
  const map = {
    simple: '简单多数',
    'two-thirds': '三分之二多数',
    unanimous: '一致通过'
  }
  return map[type] || type
}

const getVoteOptionLabel = (option) => {
  const map = {
    agree: '赞成',
    disagree: '反对',
    abstain: '弃权'
  }
  return map[option] || option
}

const getVoteOptionTagType = (option) => {
  const map = {
    agree: 'success',
    disagree: 'danger',
    abstain: 'info'
  }
  return map[option] || ''
}

onMounted(() => {
  const id = route.params.id
  if (id) {
    fetchDetail(id)
    fetchVoteStats(id)
    fetchVoteRecords()
  }
  window.addEventListener('resize', handleResize)
})

watch(() => route.params.id, (newId) => {
  if (newId) {
    fetchDetail(newId)
    fetchVoteStats(newId)
    pagination.pageNum = 1
    fetchVoteRecords()
  }
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.detail-content {
  padding: 20px 0;
}
.description-content {
  line-height: 1.8;
  white-space: pre-wrap;
}
.chart-container {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}
.chart {
  width: 600px;
  height: 400px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
