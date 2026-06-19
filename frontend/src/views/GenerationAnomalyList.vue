<template>
  <div class="generation-anomaly-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>发电异常窗口管理</span>
          <div>
            <el-button type="primary" @click="handleDetect">检测异常</el-button>
            <el-button type="success" @click="handleShowStatistics">统计数据</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="逆变器ID">
          <el-input-number v-model="searchForm.inverterId" :min="1" clearable placeholder="请输入" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="已处理" value="RESOLVED" />
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
        <el-table-column prop="anomalyNo" label="异常编号" width="200" />
        <el-table-column prop="inverterId" label="逆变器ID" width="100" />
        <el-table-column label="异常日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.anomalyDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="forecastGeneration" label="预测发电量(kWh)" width="150" />
        <el-table-column prop="actualGeneration" label="实际发电量(kWh)" width="150" />
        <el-table-column prop="deviationAmount" label="偏差量(kWh)" width="130">
          <template #default="{ row }">
            <span class="text-danger">{{ row.deviationAmount?.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="deviationRate" label="偏差率" width="110">
          <template #default="{ row }">
            <el-tag type="danger" size="small">{{ row.deviationRate?.toFixed(2) }}%</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deviationReason" label="偏差原因" min-width="150" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.deviationReason">{{ row.deviationReason }}</span>
            <span v-else class="text-muted">待填写</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="warning" link v-if="row.status === 'PENDING'" @click="handleResolve(row)">
              处理
            </el-button>
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="resolveDialogVisible" title="处理发电异常" width="600px">
      <el-form :model="resolveForm" label-width="120px">
        <el-form-item label="偏差原因" required>
          <el-select v-model="resolveForm.deviationReasonCategory" placeholder="请选择偏差原因类别" style="width: 100%">
            <el-option label="天气突变" value="WEATHER_CHANGE" />
            <el-option label="设备故障" value="EQUIPMENT_FAILURE" />
            <el-option label="遮挡新增" value="NEW_SHADING" />
            <el-option label="电网异常" value="GRID_ISSUE" />
            <el-option label="设备维护" value="MAINTENANCE" />
            <el-option label="其他原因" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="详细说明" required>
          <el-input
            v-model="resolveForm.detailDescription"
            type="textarea"
            :rows="4"
            placeholder="请详细描述偏差原因和排查情况"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resolveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResolve" :loading="submitting">确认处理</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="异常详情" width="700px">
      <div v-if="currentDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="异常编号">{{ currentDetail.anomalyNo }}</el-descriptions-item>
          <el-descriptions-item label="逆变器ID">{{ currentDetail.inverterId }}</el-descriptions-item>
          <el-descriptions-item label="异常日期">{{ formatDate(currentDetail.anomalyDate) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentDetail.status)">{{ getStatusLabel(currentDetail.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预测发电量">{{ currentDetail.forecastGeneration }} kWh</el-descriptions-item>
          <el-descriptions-item label="实际发电量">{{ currentDetail.actualGeneration }} kWh</el-descriptions-item>
          <el-descriptions-item label="偏差量">{{ currentDetail.deviationAmount }} kWh</el-descriptions-item>
          <el-descriptions-item label="偏差率">
            <el-tag type="danger" size="small">{{ currentDetail.deviationRate }}%</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="偏差原因类别" v-if="currentDetail.deviationReasonCategory">
            {{ getReasonCategoryLabel(currentDetail.deviationReasonCategory) }}
          </el-descriptions-item>
          <el-descriptions-item label="处理时间" v-if="currentDetail.handleTime">
            {{ formatDateTime(currentDetail.handleTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="偏差原因详情" :span="2" v-if="currentDetail.detailDescription">
            {{ currentDetail.detailDescription }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ formatDateTime(currentDetail.createTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <el-dialog v-model="showStatistics" title="异常统计数据" width="500px">
      <div v-if="statisticsData">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-label">异常总数</div>
                <div class="stat-value">{{ statisticsData.totalCount }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-label">待处理</div>
                <div class="stat-value text-danger">{{ statisticsData.pendingCount }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-label">已处理</div>
                <div class="stat-value text-success">{{ statisticsData.resolvedCount }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-label">平均偏差率</div>
                <div class="stat-value text-warning">{{ statisticsData.avgDeviationRate }}%</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
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
const resolveDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const submitting = ref(false)
const currentRow = ref({})
const currentDetail = ref(null)
const showStatistics = ref(false)
const statisticsData = ref(null)

const searchForm = reactive({
  inverterId: null,
  status: '',
  dateRange: []
})

const resolveForm = reactive({
  deviationReasonCategory: '',
  detailDescription: ''
})

const fetchList = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.inverterId) params.inverterId = searchForm.inverterId
    if (searchForm.status) params.status = searchForm.status
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const res = await request.get('/generation-anomalies', { params })
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
  searchForm.inverterId = null
  searchForm.status = ''
  searchForm.dateRange = []
  fetchList()
}

const handleShowStatistics = async () => {
  await fetchStatistics()
  showStatistics.value = true
}

const handleDetect = async () => {
  try {
    const { value: dateRange } = await ElMessageBox.prompt(
      '请输入检测日期范围（格式：开始日期,结束日期，如 2026-06-01,2026-06-19）',
      '异常检测',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入日期范围'
      }
    )
    const dates = dateRange.split(',')
    if (dates.length !== 2) {
      ElMessage.warning('请输入正确的日期范围格式')
      return
    }
    loading.value = true
    const res = await request.post('/generation-anomalies/detect', null, {
      params: { startDate: dates[0].trim(), endDate: dates[1].trim() }
    })
    ElMessage.success(`检测完成，发现 ${res.data.length} 个异常窗口`)
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('检测失败')
    }
  } finally {
    loading.value = false
  }
}

const handleResolve = (row) => {
  currentRow.value = row
  resolveForm.deviationReasonCategory = ''
  resolveForm.detailDescription = ''
  resolveDialogVisible.value = true
}

const submitResolve = async () => {
  if (!resolveForm.deviationReasonCategory) {
    ElMessage.warning('请选择偏差原因类别')
    return
  }
  if (!resolveForm.detailDescription) {
    ElMessage.warning('请填写详细说明')
    return
  }
  submitting.value = true
  try {
    await request.put(`/generation-anomalies/${currentRow.value.id}/handle`, {
      ...resolveForm,
      handledBy: 1
    })
    ElMessage.success('处理成功，偏差原因已关联至预结算单修正记录')
    resolveDialogVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('处理失败')
  } finally {
    submitting.value = false
  }
}

const handleDetail = async (row) => {
  try {
    const res = await request.get(`/generation-anomalies/${row.id}`)
    currentDetail.value = res.data
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const fetchStatistics = async () => {
  try {
    const res = await request.get('/generation-anomalies/statistics')
    statisticsData.value = res.data
  } catch (error) {
    ElMessage.error('获取统计数据失败')
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
  const map = { PENDING: '待处理', RESOLVED: '已处理' }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = { PENDING: 'danger', RESOLVED: 'success' }
  return map[status] || ''
}

const getReasonCategoryLabel = (category) => {
  const map = {
    WEATHER_CHANGE: '天气突变',
    EQUIPMENT_FAILURE: '设备故障',
    NEW_SHADING: '遮挡新增',
    GRID_ISSUE: '电网异常',
    MAINTENANCE: '设备维护',
    OTHER: '其他原因'
  }
  return map[category] || category
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
.text-warning {
  color: #E6A23C;
  font-weight: bold;
}
.stat-item {
  text-align: center;
  padding: 10px 0;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
</style>
