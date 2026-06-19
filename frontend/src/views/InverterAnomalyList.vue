<template>
  <div class="inverter-anomaly-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>逆变器异常追踪</span>
          <el-button type="primary" @click="handleAdd">记录异常</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="异常类型">
          <el-select v-model="searchForm.anomalyType" placeholder="请选择" clearable>
            <el-option label="离线" value="OFFLINE" />
            <el-option label="发电量突降" value="POWER_DROP" />
            <el-option label="遮挡增多" value="SHADING_INCREASE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待处理" value="OPEN" />
            <el-option label="处理中" value="IN_PROGRESS" />
            <el-option label="已修复" value="RESOLVED" />
            <el-option label="已关闭" value="CLOSED" />
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
        <el-table-column prop="anomalyType" label="异常类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getAnomalyTagType(row.anomalyType)">{{ getAnomalyLabel(row.anomalyType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="异常描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="affectedBuilding" label="影响楼栋" width="100" />
        <el-table-column prop="affectedPvStrings" label="涉及组件串" width="180" />
        <el-table-column prop="downtimeHours" label="停机时长(h)" width="120" />
        <el-table-column prop="affectedDates" label="受影响日期" min-width="180" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="运维人员" width="100" />
        <el-table-column prop="discoveryTime" label="发现时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="warning" link v-if="row.status === 'OPEN'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link v-if="row.status === 'OPEN' || row.status === 'IN_PROGRESS'" @click="handleCompleteRepair(row)">完成维修</el-button>
            <el-button type="info" link v-if="row.status === 'RESOLVED'" @click="handleClose(row)">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="repairDialogVisible" title="完成维修" width="600px">
      <el-form :model="repairForm" label-width="120px">
        <el-form-item label="维修结果">
          <el-input v-model="repairForm.repairResult" type="textarea" :rows="4" placeholder="请描述维修结果" />
        </el-form-item>
        <el-form-item label="修复前日期">
          <el-date-picker v-model="repairForm.beforeCurveDate" type="date" placeholder="选择修复前对比日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="修复后日期">
          <el-date-picker v-model="repairForm.afterCurveDate" type="date" placeholder="选择修复后对比日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="曲线对比结果">
          <el-input v-model="repairForm.curveComparisonResult" type="textarea" :rows="3" placeholder="系统将自动对比发电曲线，您也可以手动补充说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="repairDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRepair" :loading="submitting">确认维修完成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const repairDialogVisible = ref(false)
const submitting = ref(false)
const currentRow = ref({})

const searchForm = reactive({
  anomalyType: '',
  status: ''
})

const repairForm = reactive({
  repairResult: '',
  beforeCurveDate: '',
  afterCurveDate: '',
  curveComparisonResult: ''
})

const filteredData = computed(() => {
  return tableData.value.filter(item => {
    if (searchForm.anomalyType && item.anomalyType !== searchForm.anomalyType) return false
    if (searchForm.status && item.status !== searchForm.status) return false
    return true
  })
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await request.get('/inverter-anomalies')
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
  searchForm.anomalyType = ''
  searchForm.status = ''
}

const handleAdd = () => {
  router.push('/inverter-anomaly/create')
}

const handleEdit = (row) => {
  router.push(`/inverter-anomaly/edit/${row.id}`)
}

const handleDetail = (row) => {
  router.push(`/inverter-anomaly/${row.id}`)
}

const handleCompleteRepair = (row) => {
  currentRow.value = row
  repairForm.repairResult = ''
  repairForm.beforeCurveDate = ''
  repairForm.afterCurveDate = ''
  repairForm.curveComparisonResult = ''
  repairDialogVisible.value = true
}

const submitRepair = async () => {
  if (!repairForm.repairResult) {
    ElMessage.warning('请填写维修结果')
    return
  }
  submitting.value = true
  try {
    await request.put(`/inverter-anomalies/${currentRow.value.id}/complete-repair`, repairForm)
    ElMessage.success('维修完成，状态已更新')
    repairDialogVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

const handleClose = async (row) => {
  try {
    await ElMessageBox.confirm('确认关闭该异常记录吗？关闭前请确认发电曲线对比正常。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/inverter-anomalies/${row.id}/close`)
    ElMessage.success('已关闭')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('关闭失败')
    }
  }
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
</style>
