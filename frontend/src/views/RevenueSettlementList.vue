<template>
  <div class="revenue-settlement-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收益预结算管理</span>
          <div>
            <el-button type="primary" @click="handleGenerate7Days">生成未来7天预结算</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已审核" value="REVIEWED" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="settlementNo" label="结算单号" width="200" />
        <el-table-column label="结算周期" width="220">
          <template #default="{ row }">
            {{ formatDate(row.startDate) }} 至 {{ formatDate(row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalForecastGeneration" label="预测总发电量(kWh)" width="160" />
        <el-table-column prop="totalActualGeneration" label="实际总发电量(kWh)" width="160">
          <template #default="{ row }">
            <span v-if="row.totalActualGeneration > 0">{{ row.totalActualGeneration.toFixed(2) }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="forecastRevenue" label="预测收益(元)" width="130" />
        <el-table-column prop="actualRevenue" label="实际收益(元)" width="130">
          <template #default="{ row }">
            <span v-if="row.actualRevenue > 0">{{ row.actualRevenue.toFixed(2) }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="deviationAmount" label="偏差金额(元)" width="130">
          <template #default="{ row }">
            <span :class="{ 'text-danger': row.deviationAmount < 0, 'text-success': row.deviationAmount > 0 }">
              {{ row.deviationAmount ? row.deviationAmount.toFixed(2) : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="weatherSummary" label="天气概况" width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="success" link v-if="row.status === 'DRAFT'" @click="handleReview(row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="预结算单详情" width="900px">
      <div v-if="currentDetail">
        <el-descriptions :column="2" border class="mb-20">
          <el-descriptions-item label="结算单号">{{ currentDetail.settlement.settlementNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentDetail.settlement.status)">
              {{ getStatusLabel(currentDetail.settlement.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="结算周期">
            {{ formatDate(currentDetail.settlement.startDate) }} 至 {{ formatDate(currentDetail.settlement.endDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="天气概况">{{ currentDetail.settlement.weatherSummary }}</el-descriptions-item>
          <el-descriptions-item label="预测总发电量">{{ currentDetail.settlement.totalForecastGeneration }} kWh</el-descriptions-item>
          <el-descriptions-item label="实际总发电量">{{ currentDetail.settlement.totalActualGeneration }} kWh</el-descriptions-item>
          <el-descriptions-item label="预测收益">{{ currentDetail.settlement.forecastRevenue }} 元</el-descriptions-item>
          <el-descriptions-item label="实际收益">{{ currentDetail.settlement.actualRevenue }} 元</el-descriptions-item>
          <el-descriptions-item label="偏差金额">{{ currentDetail.settlement.deviationAmount }} 元</el-descriptions-item>
          <el-descriptions-item label="偏差率">{{ currentDetail.settlement.deviationRate }}%</el-descriptions-item>
          <el-descriptions-item label="电价标准">
            自用 {{ currentDetail.settlement.selfUsePrice }}元/度({{ (currentDetail.settlement.selfUseRatio * 100).toFixed(0) }}%)
            上网 {{ currentDetail.settlement.gridPrice }}元/度({{ (currentDetail.settlement.gridRatio * 100).toFixed(0) }}%)
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(currentDetail.settlement.createTime) }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">预测依据</el-divider>
        <div class="forecast-basis">{{ currentDetail.settlement.forecastBasis }}</div>

        <el-divider content-position="left">每日发电量明细</el-divider>
        <el-table :data="currentDetail.forecasts" border size="small" max-height="250">
          <el-table-column prop="inverterId" label="逆变器ID" width="90" />
          <el-table-column label="日期" width="110">
            <template #default="{ row }">{{ formatDate(row.forecastDate) }}</template>
          </el-table-column>
          <el-table-column label="天气" width="80">
            <template #default="{ row }">{{ getWeatherLabel(row.weatherType) }}</template>
          </el-table-column>
          <el-table-column prop="forecastGeneration" label="预测(kWh)" width="100" />
          <el-table-column prop="actualGeneration" label="实际(kWh)" width="100">
            <template #default="{ row }">
              <span v-if="row.actualGeneration != null">{{ row.actualGeneration.toFixed(2) }}</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="偏差率" width="90">
            <template #default="{ row }">
              <span v-if="row.deviationRate != null" :class="{ 'text-danger': row.deviationRate > 15 }">
                {{ row.deviationRate.toFixed(2) }}%
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </el-table>

        <el-divider content-position="left">发电异常窗口" v-if="currentDetail.anomalies && currentDetail.anomalies.length > 0"></el-divider>
        <el-table :data="currentDetail.anomalies" border size="small" max-height="200" v-if="currentDetail.anomalies && currentDetail.anomalies.length > 0">
          <el-table-column prop="anomalyNo" label="异常编号" width="180" />
          <el-table-column prop="inverterId" label="逆变器ID" width="90" />
          <el-table-column label="日期" width="110">
            <template #default="{ row }">{{ formatDate(row.anomalyDate) }}</template>
          </el-table-column>
          <el-table-column prop="forecastGeneration" label="预测(kWh)" width="100" />
          <el-table-column prop="actualGeneration" label="实际(kWh)" width="100" />
          <el-table-column prop="deviationRate" label="偏差率" width="90" />
          <el-table-column prop="deviationReason" label="偏差原因" min-width="150" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag size="small" :type="getAnomalyStatusTagType(row.status)">
                {{ getAnomalyStatusLabel(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无异常记录" :image-size="60" />

        <el-divider content-position="left">修正记录" v-if="currentDetail.corrections && currentDetail.corrections.length > 0"></el-divider>
        <el-table :data="currentDetail.corrections" border size="small" max-height="200" v-if="currentDetail.corrections && currentDetail.corrections.length > 0">
          <el-table-column label="影响日期" prop="affectedDate" width="120" />
          <el-table-column label="原因分类" prop="reasonCategory" width="120" />
          <el-table-column label="原因详情" prop="reasonDetail" min-width="200" show-overflow-tooltip />
          <el-table-column label="原预测值" prop="originalForecast" width="100" />
          <el-table-column label="修正值" prop="correctedValue" width="100" />
          <el-table-column label="调整金额" prop="adjustmentAmount" width="110">
            <template #default="{ row }">{{ row.adjustmentAmount?.toFixed(2) }} 元</template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无修正记录" :image-size="60" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const detailDialogVisible = ref(false)
const currentDetail = ref(null)

const searchForm = reactive({
  status: '',
  dateRange: []
})

const fetchList = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.status) params.status = searchForm.status
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const res = await request.get('/revenue-settlements', { params })
    tableData.value = res.data
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchList()
}

const handleReset = () => {
  searchForm.status = ''
  searchForm.dateRange = []
  fetchList()
}

const handleGenerate7Days = async () => {
  loading.value = true
  try {
    const res = await request.post('/revenue-settlements/generate-7days')
    ElMessage.success('预结算单生成成功')
    fetchList()
  } catch (error) {
    ElMessage.error('生成预结算单失败')
  } finally {
    loading.value = false
  }
}

const handleDetail = async (row) => {
  try {
    const res = await request.get(`/revenue-settlements/${row.id}/detail`)
    currentDetail.value = res.data
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const handleReview = async (row) => {
  try {
    await ElMessageBox.confirm('确认审核通过该预结算单吗？', '审核确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/revenue-settlements/${row.id}/review`, { userId: 1 })
    ElMessage.success('审核成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('审核失败')
    }
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toISOString().split('T')[0]
}

const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toISOString().replace('T', ' ').substring(0, 19)
}

const getStatusLabel = (status) => {
  const map = { DRAFT: '草稿', REVIEWED: '已审核' }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = { DRAFT: 'info', REVIEWED: 'success' }
  return map[status] || ''
}

const getWeatherLabel = (type) => {
  const map = { SUNNY: '晴天', CLOUDY: '多云', RAINY: '雨天' }
  return map[type] || type
}

const getAnomalyStatusLabel = (status) => {
  const map = { PENDING: '待处理', RESOLVED: '已处理' }
  return map[status] || status
}

const getAnomalyStatusTagType = (status) => {
  const map = { PENDING: 'danger', RESOLVED: 'success' }
  return map[status] || ''
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search-form {
  margin-bottom: 20px;
}
.text-muted {
  color: #909399;
}
.text-danger {
  color: #F56C6C;
  font-weight: bold;
}
.text-success {
  color: #67C23A;
  font-weight: bold;
}
.mb-20 {
  margin-bottom: 20px;
}
.forecast-basis {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  line-height: 1.8;
  color: #606266;
}
</style>
