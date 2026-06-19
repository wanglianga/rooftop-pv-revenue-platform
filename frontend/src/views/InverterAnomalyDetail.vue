<template>
  <div class="inverter-anomaly-detail">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>异常详情 #{{ anomaly.id }}</span>
          <div>
            <el-tag :type="getStatusTagType(anomaly.status)" size="large">{{ getStatusLabel(anomaly.status) }}</el-tag>
            <el-button style="margin-left: 12px" @click="handleBack">返回</el-button>
          </div>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="逆变器ID">{{ anomaly.inverterId }}</el-descriptions-item>
        <el-descriptions-item label="异常类型">
          <el-tag :type="getAnomalyTagType(anomaly.anomalyType)">{{ getAnomalyLabel(anomaly.anomalyType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发现时间">{{ anomaly.discoveryTime }}</el-descriptions-item>
        <el-descriptions-item label="运维人员">{{ anomaly.operator }}</el-descriptions-item>
        <el-descriptions-item label="影响楼栋">{{ anomaly.affectedBuilding || '-' }}</el-descriptions-item>
        <el-descriptions-item label="停机时长">{{ anomaly.downtimeHours || 0 }} 小时</el-descriptions-item>
        <el-descriptions-item label="涉及组件串" :span="2">{{ anomaly.affectedPvStrings || '-' }}</el-descriptions-item>
        <el-descriptions-item label="受影响日期" :span="2">
          <span v-if="anomaly.affectedDates" class="affected-dates">{{ anomaly.affectedDates }}</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="异常描述" :span="2">{{ anomaly.description }}</el-descriptions-item>
        <el-descriptions-item label="排查过程" :span="2">
          <div class="process-content">{{ anomaly.troubleshootProcess || '-' }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="anomaly.status === 'RESOLVED' || anomaly.status === 'CLOSED'" style="margin-top: 20px">
      <template #header>
        <span>维修信息</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="维修时间">{{ anomaly.repairTime }}</el-descriptions-item>
        <el-descriptions-item label="维修结果" :span="2">{{ anomaly.repairResult || '-' }}</el-descriptions-item>
        <el-descriptions-item label="修复前对比日期">{{ anomaly.beforeCurveDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="修复后对比日期">{{ anomaly.afterCurveDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="曲线对比结果" :span="2">{{ anomaly.curveComparisonResult || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>发电曲线对比</span>
          <el-button type="primary" size="small" @click="loadCurveComparison" :loading="curveLoading">加载对比数据</el-button>
        </div>
      </template>

      <el-form :inline="true" class="curve-form" v-if="!curveData">
        <el-form-item label="修复前日期">
          <el-date-picker v-model="compareBeforeDate" type="date" placeholder="选择修复前日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="修复后日期">
          <el-date-picker v-model="compareAfterDate" type="date" placeholder="选择修复后日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadCurveComparison" :loading="curveLoading">对比</el-button>
        </el-form-item>
      </el-form>

      <div v-if="curveData" ref="chartRef" class="chart"></div>

      <el-empty v-if="!curveData && !curveLoading" description="选择修复前后日期，对比发电曲线变化" />
    </el-card>

    <el-card v-if="relatedAllocations.length > 0" style="margin-top: 20px">
      <template #header>
        <span>关联收益分配记录</span>
      </template>
      <el-table :data="relatedAllocations" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="period" label="周期" width="120" />
        <el-table-column prop="buildingNo" label="楼栋" width="100" />
        <el-table-column prop="ownerName" label="业主" width="100" />
        <el-table-column prop="amount" label="分配金额" width="120" />
        <el-table-column prop="delayedFlag" label="延后标记" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.delayedFlag === 1" type="danger">已延后</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="affectedDates" label="受影响日期" min-width="200" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const anomaly = ref({})
const curveData = ref(null)
const curveLoading = ref(false)
const chartRef = ref(null)
const relatedAllocations = ref([])
const compareBeforeDate = ref('')
const compareAfterDate = ref('')

const fetchDetail = async () => {
  try {
    const res = await request.get(`/inverter-anomalies/${route.params.id}`)
    anomaly.value = res.data
    if (res.data.beforeCurveDate) compareBeforeDate.value = res.data.beforeCurveDate
    if (res.data.afterCurveDate) compareAfterDate.value = res.data.afterCurveDate
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const fetchRelatedAllocations = async () => {
  try {
    const res = await request.get(`/revenue-allocations/by-anomaly/${route.params.id}`)
    relatedAllocations.value = res.data
  } catch (error) {
    relatedAllocations.value = []
  }
}

const loadCurveComparison = async () => {
  if (!compareBeforeDate.value || !compareAfterDate.value) {
    ElMessage.warning('请选择修复前后的对比日期')
    return
  }
  curveLoading.value = true
  try {
    const res = await request.get('/power-curves/compare', {
      params: {
        inverterId: anomaly.value.inverterId,
        beforeDate: compareBeforeDate.value,
        afterDate: compareAfterDate.value
      }
    })
    curveData.value = res.data
    await nextTick()
    renderChart(res.data)
  } catch (error) {
    ElMessage.error('加载对比数据失败')
  } finally {
    curveLoading.value = false
  }
}

const renderChart = (data) => {
  if (!chartRef.value) return
  const chart = echarts.init(chartRef.value)

  const beforePoints = (data.beforeData || []).map(item => {
    const d = new Date(item.time)
    return [d.getHours() + ':' + String(d.getMinutes()).padStart(2, '0'), item.power]
  })
  const afterPoints = (data.afterData || []).map(item => {
    const d = new Date(item.time)
    return [d.getHours() + ':' + String(d.getMinutes()).padStart(2, '0'), item.power]
  })

  const allX = [...new Set([...beforePoints.map(p => p[0]), ...afterPoints.map(p => p[0])])].sort()

  chart.setOption({
    title: {
      text: `逆变器 #${data.inverterId} 发电曲线对比`,
      subtext: `修复前: ${data.beforeDate} | 修复后: ${data.afterDate} | 功率恢复率: ${data.powerRecoveryRate?.toFixed(1)}%`
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: [`修复前 (${data.beforeDate})`, `修复后 (${data.afterDate})`]
    },
    xAxis: {
      type: 'category',
      data: allX,
      name: '时间'
    },
    yAxis: {
      type: 'value',
      name: '功率(kW)'
    },
    series: [
      {
        name: `修复前 (${data.beforeDate})`,
        type: 'line',
        smooth: true,
        data: allX.map(x => {
          const found = beforePoints.find(p => p[0] === x)
          return found ? found[1] : null
        }),
        lineStyle: { type: 'dashed' },
        itemStyle: { color: '#E6A23C' }
      },
      {
        name: `修复后 (${data.afterDate})`,
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.1 },
        data: allX.map(x => {
          const found = afterPoints.find(p => p[0] === x)
          return found ? found[1] : null
        }),
        itemStyle: { color: '#67C23A' }
      }
    ]
  })
}

const handleBack = () => {
  router.back()
}

const getAnomalyLabel = (type) => {
  const map = { OFFLINE: '离线', POWER_DROP: '发电量突降', SHADING_INCREASE: '遮挡增多', OTHER: '其他' }
  return map[type] || type
}

const getAnomalyTagType = (type) => {
  const map = { OFFLINE: 'danger', POWER_DROP: 'warning', SHADING_INCREASE: '', OTHER: 'info' }
  return map[type] || ''
}

const getStatusLabel = (status) => {
  const map = { OPEN: '待处理', IN_PROGRESS: '处理中', RESOLVED: '已修复', CLOSED: '已关闭' }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = { OPEN: 'danger', IN_PROGRESS: 'warning', RESOLVED: 'success', CLOSED: 'info' }
  return map[status] || ''
}

onMounted(() => {
  fetchDetail()
  fetchRelatedAllocations()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.process-content {
  white-space: pre-wrap;
}
.affected-dates {
  color: #409EFF;
  font-weight: 500;
}
.chart {
  height: 400px;
  width: 100%;
}
.curve-form {
  margin-bottom: 20px;
}
</style>
