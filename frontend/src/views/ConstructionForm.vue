﻿<template>
  <div class="construction-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑施工记录' : '新增施工记录' }}</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" class="form-content">
        <el-form-item label="施工编号" prop="constructionNo">
          <el-input v-model="form.constructionNo" placeholder="请输入施工编号" />
        </el-form-item>

        <el-form-item label="施工阶段" prop="stage">
          <el-select v-model="form.stage" placeholder="请选择施工阶段" style="width: 100%">
            <el-option label="准备阶段" value="prepare" />
            <el-option label="基础施工" value="foundation" />
            <el-option label="支架安装" value="bracket" />
            <el-option label="组件安装" value="module" />
            <el-option label="电气安装" value="electrical" />
            <el-option label="调试阶段" value="debugging" />
            <el-option label="竣工验收" value="acceptance" />
          </el-select>
        </el-form-item>

        <el-form-item label="施工区域" prop="roofAreaId">
          <el-select v-model="form.roofAreaId" placeholder="请选择施工区域" style="width: 100%">
            <el-option v-for="area in roofAreas" :key="area.id" :label="area.name" :value="area.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="施工日期" prop="constructionDate">
          <el-date-picker
            v-model="form.constructionDate"
            type="date"
            placeholder="请选择施工日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item label="施工内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入施工内容" maxlength="1000" show-word-limit />
        </el-form-item>

        <el-form-item label="施工人数" prop="workerCount">
          <el-input-number v-model="form.workerCount" :min="1" :max="50" style="width: 100%" />
        </el-form-item>

        <el-form-item label="工时(h)" prop="workHours">
          <el-input-number v-model="form.workHours" :min="1" :max="24" :step="0.5" style="width: 100%" />
        </el-form-item>

        <el-form-item label="使用材料">
          <el-input v-model="form.materials" type="textarea" :rows="3" placeholder="请输入使用材料明细" />
        </el-form-item>

        <el-form-item label="质量检查" prop="qualityCheck">
          <el-radio-group v-model="form.qualityCheck">
            <el-radio :value="true">合格</el-radio>
            <el-radio :value="false">不合格</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>

        <el-form-item label="施工照片">
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

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
          <el-button @click="handleReset">重置</el-button>
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
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const fileList = ref([])
const roofAreas = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  id: null,
  constructionNo: '',
  stage: '',
  roofAreaId: '',
  constructionDate: '',
  content: '',
  workerCount: 1,
  workHours: 8,
  materials: '',
  qualityCheck: true,
  remark: ''
})

const rules = {
  constructionNo: [
    { required: true, message: '请输入施工编号', trigger: 'blur' }
  ],
  stage: [
    { required: true, message: '请选择施工阶段', trigger: 'change' }
  ],
  roofAreaId: [
    { required: true, message: '请选择施工区域', trigger: 'change' }
  ],
  constructionDate: [
    { required: true, message: '请选择施工日期', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入施工内容', trigger: 'blur' },
    { min: 5, max: 1000, message: '长度在 5 到 1000 个字符', trigger: 'blur' }
  ],
  workerCount: [
    { required: true, message: '请输入施工人数', trigger: 'change' }
  ],
  workHours: [
    { required: true, message: '请输入工时', trigger: 'change' }
  ],
  qualityCheck: [
    { required: true, message: '请选择质量检查结果', trigger: 'change' }
  ]
}

const fetchRoofAreas = async () => {
  try {
    const res = await request.get('/roof/area/list', { params: { pageNum: 1, pageSize: 100 } })
    roofAreas.value = res.data.list
  } catch (error) {
    ElMessage.error('获取屋面区域失败')
  }
}

const fetchDetail = async (id) => {
  try {
    const res = await request.get(`/construction/${id}`)
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
          await request.put(`/construction/${form.id}`, form)
          ElMessage.success('编辑成功')
        } else {
          await request.post('/construction', form)
          ElMessage.success('创建成功')
        }
        router.push('/construction')
      } catch (error) {
        ElMessage.error(isEdit.value ? '编辑失败' : '创建失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleReset = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  fileList.value = []
}

const handleBack = () => {
  router.back()
}

onMounted(() => {
  fetchRoofAreas()
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
