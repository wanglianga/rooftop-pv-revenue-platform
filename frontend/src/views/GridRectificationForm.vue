<template>
  <div class="grid-rectification-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>提交整改记录</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" class="form-content">
        <el-form-item label="并网申请ID">
          <el-input :model-value="form.gridApplicationId" disabled />
        </el-form-item>

        <el-form-item label="整改类型" prop="rectificationType">
          <el-select v-model="form.rectificationType" placeholder="请选择整改类型" style="width: 100%">
            <el-option label="接线图补充" value="接线图补充" />
            <el-option label="保护装置完善" value="保护装置完善" />
            <el-option label="计量箱整改" value="计量箱整改" />
            <el-option label="资料补充" value="资料补充" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item label="整改说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细说明整改内容" maxlength="1000" show-word-limit />
        </el-form-item>

        <el-form-item label="整改照片">
          <el-upload
            v-model:file-list="fileList"
            action="#"
            :auto-upload="false"
            list-type="picture-card"
            multiple
            :limit="9"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="文件版本" prop="fileVersion">
          <el-input v-model="form.fileVersion" placeholder="请输入文件版本号，如 V2.0" />
        </el-form-item>

        <el-form-item label="复测结果" prop="retestResult">
          <el-input v-model="form.retestResult" type="textarea" :rows="3" placeholder="请输入复测结果" />
        </el-form-item>

        <el-form-item label="提交人" prop="submitter">
          <el-input v-model="form.submitter" placeholder="请输入提交人姓名" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">提交整改</el-button>
          <el-button @click="handleBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const fileList = ref([])

const form = reactive({
  gridApplicationId: route.query.gridApplicationId || '',
  rectificationType: '',
  description: '',
  photos: '',
  fileVersion: '',
  retestResult: '',
  submitter: '',
  status: 'SUBMITTED'
})

const rules = {
  rectificationType: [
    { required: true, message: '请选择整改类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入整改说明', trigger: 'blur' }
  ],
  fileVersion: [
    { required: true, message: '请输入文件版本', trigger: 'blur' }
  ],
  retestResult: [
    { required: true, message: '请输入复测结果', trigger: 'blur' }
  ],
  submitter: [
    { required: true, message: '请输入提交人', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await request.post('/grid-rectifications', form)
        ElMessage.success('整改提交成功')
        router.back()
      } catch (error) {
        ElMessage.error('提交失败')
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
  if (!form.gridApplicationId) {
    ElMessage.warning('缺少并网申请ID')
    router.back()
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
