﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<template>
  <div class="vote-topic-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>议题列表</span>
          <el-button type="primary" @click="handleAdd">发布议题</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="议题标题">
          <el-input v-model="searchForm.title" placeholder="请输入议题标题" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待表决" value="pending" />
            <el-option label="表决中" value="voting" />
            <el-option label="已通过" value="passed" />
            <el-option label="已否决" value="rejected" />
            <el-option label="已关闭" value="closed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="议题标题" min-width="200" />
        <el-table-column prop="description" label="议题描述" min-width="300" show-overflow-tooltip />
        <el-table-column prop="type" label="议题类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeLabel(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voteCount" label="表决人数" width="100" />
        <el-table-column prop="endTime" label="截止时间" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="success" link v-if="row.status === 'pending'" @click="handlePublish(row)">发布</el-button>
            <el-button type="warning" link v-if="row.status === 'voting'" @click="handleClose(row)">关闭</el-button>
            <el-button type="primary" link v-if="row.status === 'pending'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link v-if="row.status === 'pending'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchList"
        @current-change="fetchList"
        class="pagination"
      />
    </el-card>
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

const searchForm = reactive({
  title: '',
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await request.get('/vote-topics', {
      params: {
        ...searchForm,
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize
      }
    })
    tableData.value = res.data.list
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchList()
}

const handleReset = () => {
  searchForm.title = ''
  searchForm.status = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/vote-topic/create')
}

const handleEdit = (row) => {
  router.push(`/vote-topic/edit/${row.id}`)
}

const handleDetail = (row) => {
  router.push(`/vote-topic/${row.id}`)
}

const handlePublish = async (row) => {
  try {
    await ElMessageBox.confirm('确认发布该议题吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/vote-topics/${row.id}/publish`)
    ElMessage.success('发布成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发布失败')
    }
  }
}

const handleClose = async (row) => {
  try {
    await ElMessageBox.confirm('确认关闭该议题吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.put(`/vote-topics/${row.id}/close`)
    ElMessage.success('关闭成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('关闭失败')
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该议题吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/vote-topics/${row.id}`)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getTypeLabel = (type) => {
  const map = {
    construction: '施工方案',
    equipment: '设备采购',
    finance: '财务预算',
    regulation: '规章制度',
    other: '其他'
  }
  return map[type] || type
}

const getTypeTagType = (type) => {
  const map = {
    construction: '',
    equipment: 'success',
    finance: 'warning',
    regulation: 'danger',
    other: 'info'
  }
  return map[type] || ''
}

const getStatusLabel = (status) => {
  const map = {
    pending: '待表决',
    voting: '表决中',
    passed: '已通过',
    rejected: '已否决',
    closed: '已关闭'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    pending: 'info',
    voting: 'warning',
    passed: 'success',
    rejected: 'danger',
    closed: ''
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
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
