<template>
  <div class="revenue-allocation-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收益分配管理</span>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="周期">
          <el-input v-model="searchForm.period" placeholder="如 2024-01" clearable />
        </el-form-item>
        <el-form-item label="延后标记">
          <el-select v-model="searchForm.delayedFlag" placeholder="全部" clearable>
            <el-option label="有延后" :value="1" />
            <el-option label="无延后" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="period" label="周期" width="120" />
        <el-table-column prop="buildingNo" label="楼栋" width="100" />
        <el-table-column prop="ownerName" label="业主" width="100" />
        <el-table-column prop="totalRevenue" label="总收益(元)" width="120" />
        <el-table-column prop="amount" label="分配金额(元)" width="120" />
        <el-table-column prop="areaRatio" label="面积比例" width="100" />
        <el-table-column label="延后标记" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.delayedFlag === 1" type="danger">已延后</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="delayedAmount" label="延后金额(元)" width="120">
          <template #default="{ row }">
            <span v-if="row.delayedFlag === 1 && row.delayedAmount" class="delayed-amount">{{ row.delayedAmount }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="delayedReason" label="延后原因" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.delayedFlag === 1" class="delayed-reason">{{ row.delayedReason }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="affectedDates" label="受影响日期" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.affectedDates" class="affected-dates">{{ row.affectedDates }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="warning" link v-if="row.delayedFlag !== 1" @click="handleMarkDelayed(row)">标记延后</el-button>
            <el-button type="info" link v-if="row.delayedFlag !== 1" @click="handleMarkAffected(row)">标记受影响日期</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="delayedDialogVisible" title="标记收益延后" width="500px">
      <el-form :model="delayedForm" label-width="100px">
        <el-form-item label="延后原因">
          <el-select v-model="delayedForm.reasonCategory" placeholder="请选择原因类别" style="width: 100%; margin-bottom: 10px">
            <el-option label="并网退回整改影响发电计划" value="并网退回整改影响发电计划" />
            <el-option label="逆变器异常导致发电受影响" value="逆变器异常导致发电受影响" />
            <el-option label="设备维护停机" value="设备维护停机" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-input v-model="delayedForm.reason" type="textarea" :rows="3" placeholder="请详细说明延后原因" />
        </el-form-item>
        <el-form-item label="延后金额(元)">
          <el-input-number v-model="delayedForm.delayedAmount" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="delayedDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDelayed" :loading="submitting">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="affectedDialogVisible" title="标记受影响日期" width="500px">
      <el-form :model="affectedForm" label-width="100px">
        <el-form-item label="受影响日期">
          <el-input v-model="affectedForm.affectedDates" placeholder="如 2024-01-15至2024-01-20" />
        </el-form-item>
        <el-form-item label="关联异常ID">
          <el-input-number v-model="affectedForm.anomalyId" :min="1" placeholder="逆变器异常记录ID，可选" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="affectedDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAffected" :loading="submitting">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const delayedDialogVisible = ref(false)
const affectedDialogVisible = ref(false)
const submitting = ref(false)
const currentRow = ref({})

const searchForm = reactive({
  period: '',
  delayedFlag: null
})

const delayedForm = reactive({
  reason: '',
  reasonCategory: '',
  delayedAmount: 0
})

const affectedForm = reactive({
  affectedDates: '',
  anomalyId: null
})

const fetchList = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.period) params.period = searchForm.period
    if (searchForm.delayedFlag !== null && searchForm.delayedFlag !== '') params.delayedFlag = searchForm.delayedFlag
    const res = await request.get('/revenue-allocations', { params })
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
  searchForm.period = ''
  searchForm.delayedFlag = null
  fetchList()
}

const handleMarkDelayed = (row) => {
  currentRow.value = row
  delayedForm.reason = ''
  delayedForm.reasonCategory = ''
  delayedForm.delayedAmount = 0
  delayedDialogVisible.value = true
}

const submitDelayed = async () => {
  if (!delayedForm.reasonCategory) {
    ElMessage.warning('请选择延后原因类别')
    return
  }
  submitting.value = true
  try {
    const reason = delayedForm.reasonCategory + (delayedForm.reason ? '：' + delayedForm.reason : '')
    await request.put(`/revenue-allocations/${currentRow.value.id}/mark-delayed`, {
      reason,
      delayedAmount: delayedForm.delayedAmount
    })
    ElMessage.success('标记延后成功')
    delayedDialogVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('标记失败')
  } finally {
    submitting.value = false
  }
}

const handleMarkAffected = (row) => {
  currentRow.value = row
  affectedForm.affectedDates = ''
  affectedForm.anomalyId = null
  affectedDialogVisible.value = true
}

const submitAffected = async () => {
  if (!affectedForm.affectedDates) {
    ElMessage.warning('请输入受影响日期')
    return
  }
  submitting.value = true
  try {
    await request.put(`/revenue-allocations/${currentRow.value.id}/mark-affected`, {
      affectedDates: affectedForm.affectedDates,
      anomalyId: affectedForm.anomalyId
    })
    ElMessage.success('标记受影响日期成功')
    affectedDialogVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('标记失败')
  } finally {
    submitting.value = false
  }
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
.delayed-amount {
  color: #E6A23C;
  font-weight: bold;
}
.delayed-reason {
  color: #F56C6C;
}
.affected-dates {
  color: #409EFF;
}
</style>
