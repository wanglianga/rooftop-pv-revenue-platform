<template>
  <div class="generation-forecast-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>发电量预测管理</span>
          <div>
            <el-button type="primary" @click="handleGenerate">生成未来7天预测</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="逆变器ID">
          <el-input-number v-model="searchForm.inverterId" :min="1" clearable placeholder="请输入" />
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
        <el-form-item label="异常标记">
          <el-select v-model="searchForm.isAnomaly" placeholder="全部" clearable>
            <el-option label="异常" :value="1" />
            <el-option label="正常" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="filteredData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="inverterId" label="逆变器ID" width="100" />
        <el-table-column prop="forecastDate" label="预测日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.forecastDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="weatherType" label="天气" width="100">
          <template #default="{ row }">
            <el-tag :type="getWeatherTagType(row.weatherType)">{{ getWeatherLabel(row.weatherType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="温度范围" width="120">
          <template #default="{ row }">
            {{ row.weatherTempLow?.toFixed(1) }}℃ ~ {{ row.weatherTempHigh?.toFixed(1) }}℃
          </template>
        </el-table-column>
        <el-table-column prop="degradationFactor" label="衰减系数" width="100">
          <template #default="{ row }">
            {{ (row.degradationFactor * 100).toFixed(2) }}%
          </template>
        </el-table-column>
        <el-table-column prop="forecastGeneration" label="预测发电量(kWh)" width="140" />
        <el-table-column prop="actualGeneration" label="实际发电量(kWh)" width="140">
          <template #default="{ row }">
            <span v-if="row.actualGeneration != null">{{ row.actualGeneration.toFixed(2) }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="deviationRate" label="偏差率" width="110">
          <template #default="{ row }">
            <span v-if="row.deviationRate != null" :class="{ 'text-danger': row.deviationRate > 15 }">
              {{ row.deviationRate.toFixed(2) }}%
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.isAnomaly === 1" type="danger">异常</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="historicalReference" label="历史参考" min-width="200" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])

const searchForm = reactive({
  inverterId: null,
  dateRange: [],
  isAnomaly: null
})

const filteredData = computed(() => {
  return tableData.value.filter(item => {
    if (searchForm.isAnomaly !== null && searchForm.isAnomaly !== '' && item.isAnomaly !== searchForm.isAnomaly) return false
    return true
  })
})

const fetchList = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.inverterId) params.inverterId = searchForm.inverterId
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const res = await request.get('/generation-forecasts', { params })
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
  searchForm.dateRange = []
  searchForm.isAnomaly = null
  fetchList()
}

const handleGenerate = async () => {
  loading.value = true
  try {
    const res = await request.post('/generation-forecasts/generate-all')
    ElMessage.success('预测生成成功，共生成 ' + res.data.length + ' 条预测记录')
    fetchList()
  } catch (error) {
    ElMessage.error('生成预测失败')
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toISOString().split('T')[0]
}

const getWeatherLabel = (type) => {
  const map = { SUNNY: '晴天', CLOUDY: '多云', RAINY: '雨天' }
  return map[type] || type
}

const getWeatherTagType = (type) => {
  const map = { SUNNY: 'success', CLOUDY: 'warning', RAINY: 'info' }
  return map[type] || ''
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
</style>
