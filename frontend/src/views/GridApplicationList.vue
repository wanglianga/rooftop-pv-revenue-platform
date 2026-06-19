<template>
  <div class="grid-application-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>并网申请管理</span>
          <el-button type="primary" @click="handleAdd">新增申请</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待审核" value="PENDING" />
            <el-option label="已通过" value="已通过" />
            <el-option label="审核中" value="审核中" />
            <el-option label="已退回" value="RETURNED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="applicant" label="申请人" width="120" />
        <el-table-column prop="applicationDate" label="申请日期" width="180" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gridNo" label="并网编号" width="160" />
        <el-table-column prop="acceptanceOpinion" label="验收意见" min-width="200" show-overflow-tooltip />
        <el-table-column prop="returnReason" label="退回原因" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.returnReason" class="return-reason">{{ row.returnReason }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="returnTime" label="退回时间" width="180">
          <template #default="{ row }">
            {{ row.returnTime ? formatDate(row.returnTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="resubmitTime" label="重新提交时间" width="180">
          <template #default="{ row }">
            {{ row.resubmitTime ? formatDate(row.resubmitTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="danger" link v-if="row.status === 'PENDING' || row.status === '审核中'" @click="handleReturn(row)">退回</el-button>
            <el-button type="warning" link v-if="row.status === 'RETURNED'" @click="handleResubmit(row)">重新提交</el-button>
            <el-button type="success" link v-if="row.status === 'RETURNED'" @click="handleViewRectification(row)">整改记录</el-button>
            <el-button type="success" link v-if="row.status === 'RETURNED'" @click="handleAddRectification(row)">提交整改</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="returnDialogVisible" title="退回申请" width="500px">
      <el-form :model="returnForm" label-width="100px">
        <el-form-item label="退回原因">
          <el-select v-model="returnForm.reasonCategory" placeholder="请选择退回原因类别" style="width: 100%; margin-bottom: 10px">
            <el-option label="接线图缺失" value="接线图缺失" />
            <el-option label="保护装置不合格" value="保护装置不合格" />
            <el-option label="计量箱问题" value="计量箱问题" />
            <el-option label="资料缺失" value="资料缺失" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-input v-model="returnForm.returnReason" type="textarea" :rows="4" placeholder="请详细说明退回原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="returnDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReturn" :loading="submitting">确认退回</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="申请详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请人">{{ currentRow.applicant }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ formatDate(currentRow.applicationDate) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentRow.status)">{{ getStatusLabel(currentRow.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="并网编号">{{ currentRow.gridNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="验收意见" :span="2">{{ currentRow.acceptanceOpinion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="退回原因" :span="2" v-if="currentRow.returnReason">
          <span class="return-reason">{{ currentRow.returnReason }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="退回时间" v-if="currentRow.returnTime">{{ formatDate(currentRow.returnTime) }}</el-descriptions-item>
        <el-descriptions-item label="重新提交时间" v-if="currentRow.resubmitTime">{{ formatDate(currentRow.resubmitTime) }}</el-descriptions-item>
      </el-descriptions>

      <div v-if="currentRow.status === 'RETURNED'" style="margin-top: 20px">
        <el-divider content-position="left">整改记录</el-divider>
        <el-timeline v-if="rectificationRecords.length > 0">
          <el-timeline-item v-for="record in rectificationRecords" :key="record.id" :timestamp="formatDate(record.submitTime)" placement="top">
            <el-card>
              <p><strong>整改类型：</strong>{{ record.rectificationType }}</p>
              <p><strong>整改说明：</strong>{{ record.description }}</p>
              <p><strong>文件版本：</strong>{{ record.fileVersion }}</p>
              <p><strong>复测结果：</strong>{{ record.retestResult }}</p>
              <p><strong>提交人：</strong>{{ record.submitter }}</p>
              <el-tag :type="record.status === 'APPROVED' ? 'success' : 'warning'" size="small">
                {{ record.status === 'APPROVED' ? '已审核' : '待审核' }}
              </el-tag>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无整改记录" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const returnDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const submitting = ref(false)
const currentRow = ref({})
const rectificationRecords = ref([])

const searchForm = reactive({
  status: ''
})

const returnForm = reactive({
  returnReason: '',
  reasonCategory: ''
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await request.get('/grid-applications', {
      params: { ...searchForm }
    })
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
  fetchList()
}

const handleAdd = () => {
  router.push('/grid-application/create')
}

const handleDetail = async (row) => {
  currentRow.value = row
  if (row.status === 'RETURNED') {
    try {
      const res = await request.get('/grid-rectifications', {
        params: { gridApplicationId: row.id }
      })
      rectificationRecords.value = res.data
    } catch (error) {
      rectificationRecords.value = []
    }
  }
  detailDialogVisible.value = true
}

const handleReturn = (row) => {
  currentRow.value = row
  returnForm.returnReason = ''
  returnForm.reasonCategory = ''
  returnDialogVisible.value = true
}

const submitReturn = async () => {
  if (!returnForm.reasonCategory) {
    ElMessage.warning('请选择退回原因类别')
    return
  }
  if (!returnForm.returnReason) {
    ElMessage.warning('请填写退回原因详情')
    return
  }
  submitting.value = true
  try {
    const reason = returnForm.reasonCategory + '：' + returnForm.returnReason
    await request.put(`/grid-applications/${currentRow.value.id}/return`, {
      returnReason: reason
    })
    ElMessage.success('退回成功')
    returnDialogVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('退回失败')
  } finally {
    submitting.value = false
  }
}

const handleResubmit = async (row) => {
  try {
    await ElMessageBox.confirm('确认重新提交该申请吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/grid-applications/${row.id}/resubmit`)
    ElMessage.success('重新提交成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重新提交失败')
    }
  }
}

const handleViewRectification = (row) => {
  router.push(`/grid-rectification?gridApplicationId=${row.id}`)
}

const handleAddRectification = (row) => {
  router.push(`/grid-rectification/create?gridApplicationId=${row.id}`)
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

const getStatusLabel = (status) => {
  const map = {
    'PENDING': '待审核',
    '已通过': '已通过',
    '审核中': '审核中',
    'RETURNED': '已退回'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    'PENDING': 'info',
    '已通过': 'success',
    '审核中': 'warning',
    'RETURNED': 'danger'
  }
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
.return-reason {
  color: #F56C6C;
  font-weight: 500;
}
</style>
