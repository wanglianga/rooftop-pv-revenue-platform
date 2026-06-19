<template>
  <div class="grid-rectification-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>整改记录</span>
          <el-button type="primary" @click="handleAdd" v-if="gridApplicationId">提交整改</el-button>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-timeline v-if="tableData.length > 0">
        <el-timeline-item v-for="record in tableData" :key="record.id" :timestamp="formatDate(record.submitTime)" placement="top">
          <el-card>
            <div class="record-header">
              <el-tag :type="record.status === 'APPROVED' ? 'success' : 'warning'">
                {{ record.status === 'APPROVED' ? '已审核' : '待审核' }}
              </el-tag>
              <el-tag type="info" style="margin-left: 10px">{{ record.rectificationType }}</el-tag>
            </div>
            <p class="record-desc"><strong>整改说明：</strong>{{ record.description }}</p>
            <p><strong>文件版本：</strong>{{ record.fileVersion }}</p>
            <p><strong>复测结果：</strong>{{ record.retestResult }}</p>
            <p><strong>提交人：</strong>{{ record.submitter }}</p>
            <p><strong>整改照片：</strong>{{ record.photos || '暂无' }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无整改记录" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const tableData = ref([])
const gridApplicationId = ref(route.query.gridApplicationId || '')

const fetchList = async () => {
  if (!gridApplicationId.value) return
  try {
    const res = await request.get('/grid-rectifications', {
      params: { gridApplicationId: gridApplicationId.value }
    })
    tableData.value = res.data
  } catch (error) {
    ElMessage.error('获取整改记录失败')
  }
}

const handleAdd = () => {
  router.push(`/grid-rectification/create?gridApplicationId=${gridApplicationId.value}`)
}

const handleBack = () => {
  router.back()
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
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
.record-header {
  margin-bottom: 12px;
}
.record-desc {
  margin: 8px 0;
}
</style>
