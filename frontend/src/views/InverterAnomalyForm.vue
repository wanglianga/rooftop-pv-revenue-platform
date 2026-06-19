<template>
  <div class="inverter-anomaly-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑异常记录' : '记录逆变器异常' }}</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" class="form-content">
        <el-form-item label="逆变器" prop="inverterId">
          <el-select v-model="form.inverterId" placeholder="请选择逆变器" style="width: 100%">
            <el-option v-for="inv in inverters" :key="inv.id" :label="inv.inverterCode + ' - ' + inv.model" :value="inv.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="异常类型" prop="anomalyType">
          <el-select v-model="form.anomalyType" placeholder="请选择异常类型" style="width: 100%">
            <el-option label="离线" value="OFFLINE" />
            <el-option label="发电量突降" value="POWER_DROP" />
            <el-option label="遮挡增多" value="SHADING_INCREASE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>

        <el-form-item label="异常描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述异常情况" maxlength="1000" show-word-limit />
        </el-form-item>

        <el-form-item label="发现时间" prop="discoveryTime">
          <el-date-picker v-model="form.discoveryTime" type="datetime" placeholder="请选择发现时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>

        <el-form-item label="涉及组件串">
          <el-input v-model="form.affectedPvStrings" placeholder="如 STR-001,STR-006" />
        </el-form-item>

        <el-form-item label="影响楼栋">
          <el-input v-model="form.affectedBuilding" placeholder="如 1号楼" />
        </el-form-item>

        <el-form-item label="停机时长(h)">
          <el-input-number v-model="form.downtimeHours" :min="0" :precision="1" style="width: 100%" />
        </el-form-item>

        <el-form-item label="排查过程" prop="troubleshootProcess">
          <el-input v-model="form.troubleshootProcess" type="textarea" :rows="5" placeholder="请记录排查过程、检查步骤和发现" maxlength="2000" show-word-limit />
        </el-form-item>

        <el-form-item label="受影响日期">
          <el-input v-model="form.affectedDates" placeholder="如 2024-06-15至2024-06-17" />
        </el-form-item>

        <el-form-item label="运维人员" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入运维人员姓名" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">{{ isEdit ? '保存' : '提交' }}</el-button>
          <el-button @click="handleBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const inverters = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  id: null,
  inverterId: '',
  anomalyType: '',
  description: '',
  discoveryTime: '',
  affectedPvStrings: '',
  affectedBuilding: '',
  downtimeHours: 0,
  troubleshootProcess: '',
  affectedDates: '',
  operator: '',
  status: 'OPEN'
})

const rules = {
  inverterId: [
    { required: true, message: '请选择逆变器', trigger: 'change' }
  ],
  anomalyType: [
    { required: true, message: '请选择异常类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入异常描述', trigger: 'blur' }
  ],
  troubleshootProcess: [
    { required: true, message: '请输入排查过程', trigger: 'blur' }
  ],
  operator: [
    { required: true, message: '请输入运维人员', trigger: 'blur' }
  ]
}

const fetchInverters = async () => {
  try {
    const res = await request.get('/inverters')
    inverters.value = res.data
  } catch (error) {
    ElMessage.error('获取逆变器列表失败')
  }
}

const fetchDetail = async (id) => {
  try {
    const res = await request.get(`/inverter-anomalies/${id}`)
    Object.assign(form, res.data)
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEdit.value) {
          await request.put(`/inverter-anomalies/${form.id}`, form)
          ElMessage.success('编辑成功')
        } else {
          await request.post('/inverter-anomalies', form)
          ElMessage.success('创建成功')
        }
        router.push('/inverter-anomaly')
      } catch (error) {
        ElMessage.error(isEdit.value ? '编辑失败' : '创建失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleBack = () => {
  router.back()
}

onMounted(() => {
  fetchInverters()
  if (isEdit.value) {
    fetchDetail(route.params.id)
  }
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.form-content {
  max-width: 800px;
  margin: 0 auto;
}
</style>
