﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<template>
  <div class="owner-notice-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑告知' : '发布告知' }}</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" class="form-content">
        <el-form-item label="告知标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入告知标题" maxlength="100" show-word-limit />
        </el-form-item>

        <el-form-item label="告知类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择告知类型" style="width: 100%">
            <el-option label="系统通知" value="system" />
            <el-option label="施工通知" value="construction" />
            <el-option label="财务通知" value="finance" />
            <el-option label="会议通知" value="meeting" />
            <el-option label="其他通知" value="other" />
          </el-select>
        </el-form-item>

        <el-form-item label="重要程度" prop="priority">
          <el-radio-group v-model="form.priority">
            <el-radio value="normal">普通</el-radio>
            <el-radio value="important">重要</el-radio>
            <el-radio value="urgent">紧急</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="告知内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="请输入告知内容" maxlength="5000" show-word-limit />
        </el-form-item>

        <el-form-item label="接收范围" prop="targetScope">
          <el-radio-group v-model="form.targetScope">
            <el-radio value="all">全部业主</el-radio>
            <el-radio value="building">指定楼栋</el-radio>
            <el-radio value="owner">指定业主</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="指定楼栋" v-if="form.targetScope === 'building'" prop="buildingNos">
          <el-select v-model="form.buildingNos" multiple placeholder="请选择楼栋" style="width: 100%">
            <el-option label="1号楼" value="1" />
            <el-option label="2号楼" value="2" />
            <el-option label="3号楼" value="3" />
            <el-option label="4号楼" value="4" />
            <el-option label="5号楼" value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="指定业主" v-if="form.targetScope === 'owner'" prop="ownerIds">
          <el-select v-model="form.ownerIds" multiple filterable placeholder="请选择业主" style="width: 100%">
            <el-option label="张三 - 1-101" value="1" />
            <el-option label="李四 - 1-102" value="2" />
            <el-option label="王五 - 2-201" value="3" />
          </el-select>
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
          <el-button type="success" @click="handleSaveDraft" :loading="submitting">保存草稿</el-button>
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

const isEdit = computed(() => !!route.query.id)

const form = reactive({
  id: null,
  title: '',
  type: '',
  priority: 'normal',
  content: '',
  targetScope: 'all',
  buildingNos: [],
  ownerIds: []
})

const rules = {
  title: [
    { required: true, message: '请输入告知标题', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择告知类型', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择重要程度', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入告知内容', trigger: 'blur' },
    { min: 10, max: 5000, message: '长度在 10 到 5000 个字符', trigger: 'blur' }
  ],
  targetScope: [
    { required: true, message: '请选择接收范围', trigger: 'change' }
  ],
  buildingNos: [
    { required: true, message: '请选择楼栋', trigger: 'change' }
  ],
  ownerIds: [
    { required: true, message: '请选择业主', trigger: 'change' }
  ]
}

const fetchDetail = async (id) => {
  try {
    const res = await request.get(`/owner-notices/${id}`)
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
        const data = { ...form, status: 'published' }
        if (isEdit.value) {
          await request.put(`/owner-notices`, data)
          ElMessage.success('编辑成功')
        } else {
          await request.post('/owner-notices', data)
          ElMessage.success('发布成功')
        }
        router.push('/owner-notice')
      } catch (error) {
        ElMessage.error(isEdit.value ? '编辑失败' : '发布失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleSaveDraft = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const data = { ...form, status: 'draft' }
        if (isEdit.value) {
          await request.put(`/owner-notices`, data)
          ElMessage.success('保存成功')
        } else {
          await request.post('/owner-notices', data)
          ElMessage.success('保存草稿成功')
        }
        router.push('/owner-notice')
      } catch (error) {
        ElMessage.error('保存失败')
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
