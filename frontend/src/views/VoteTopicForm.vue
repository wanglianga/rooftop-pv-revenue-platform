﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<template>
  <div class="vote-topic-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑议题' : '创建议题' }}</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" class="form-content">
        <el-form-item label="议题标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入议题标题" maxlength="100" show-word-limit />
        </el-form-item>

        <el-form-item label="议题类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择议题类型" style="width: 100%">
            <el-option label="施工方案" value="construction" />
            <el-option label="设备采购" value="equipment" />
            <el-option label="财务预算" value="finance" />
            <el-option label="规章制度" value="regulation" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>

        <el-form-item label="议题描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="6" placeholder="请输入议题描述" maxlength="2000" show-word-limit />
        </el-form-item>

        <el-form-item label="表决方式" prop="voteType">
          <el-radio-group v-model="form.voteType">
            <el-radio value="simple">简单多数</el-radio>
            <el-radio value="two-thirds">三分之二多数</el-radio>
            <el-radio value="unanimous">一致通过</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="截止时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="请选择截止时间"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>

        <el-form-item label="附件">
          <el-upload
            v-model:file-list="fileList"
            action="#"
            :auto-upload="false"
            multiple
            :limit="5"
          >
            <el-button type="primary">上传附件</el-button>
            <template #tip>
              <div class="el-upload__tip">最多上传5个文件，单个文件不超过10MB</div>
            </template>
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
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const fileList = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  id: null,
  title: '',
  type: '',
  description: '',
  voteType: 'simple',
  endTime: null
})

const rules = {
  title: [
    { required: true, message: '请输入议题标题', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择议题类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入议题描述', trigger: 'blur' },
    { min: 10, max: 2000, message: '长度在 10 到 2000 个字符', trigger: 'blur' }
  ],
  voteType: [
    { required: true, message: '请选择表决方式', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择截止时间', trigger: 'change' }
  ]
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const fetchDetail = async (id) => {
  try {
    const res = await request.get(`/vote-topics/${id}`)
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
          await request.put(`/vote-topics`, form)
          ElMessage.success('编辑成功')
        } else {
          await request.post('/vote-topics', form)
          ElMessage.success('创建成功')
        }
        router.push('/vote-topic')
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
