﻿<template>
  <div class="construction-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>施工记录列表</span>
          <el-button type="primary" @click="handleAdd">新增施工记录</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="施工编号">
          <el-input v-model="searchForm.constructionNo" placeholder="请输入施工编号" clearable />
        </el-form-item>
        <el-form-item label="施工阶段">
          <el-select v-model="searchForm.stage" placeholder="请选择阶段" clearable>
            <el-option label="准备阶段" value="prepare" />
            <el-option label="基础施工" value="foundation" />
            <el-option label="支架安装" value="bracket" />
            <el-option label="组件安装" value="module" />
            <el-option label="电气安装" value="electrical" />
            <el-option label="调试阶段" value="debugging" />
            <el-option label="竣工验收" value="acceptance" />
          </el-select>
        </el-form-item>
        <el-form-item label="施工区域">
          <el-select v-model="searchForm.roofAreaId" placeholder="请选择区域" clearable>
            <el-option v-for="area in roofAreas" :key="area.id" :label="area.name" :value="area.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="施工日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="constructionNo" label="施工编号" width="150" />
        <el-table-column prop="stage" label="施工阶段" width="120">
          <template #default="{ row }">
            <el-tag :type="getStageTagType(row.stage)">{{ getStageLabel(row.stage) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roofAreaName" label="施工区域" width="150" />
        <el-table-column prop="content" label="施工内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="constructionDate" label="施工日期" width="120" />
        <el-table-column prop="workerCount" label="施工人数" width="100" />
        <el-table-column prop="workHours" label="工时(h)" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link v-if="row.status === 'draft'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link v-if="row.status === 'draft'" @click="handleDelete(row)">删除</el-button>
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
const roofAreas = ref([])

const searchForm = reactive({
  constructionNo: '',
  stage: '',
  roofAreaId: '',
  dateRange: []
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const fetchRoofAreas = async () => {
  try {
    const res = await request.get('/roof/area/list', { params: { pageNum: 1, pageSize: 100 } })
    roofAreas.value = res.data.list
  } catch (error) {
    ElMessage.error('获取屋面区域失败')
  }
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    delete params.dateRange
    const res = await request.get('/construction/list', { params })
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
  searchForm.constructionNo = ''
  searchForm.stage = ''
  searchForm.roofAreaId = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleAdd = () => {
  router.push('/construction/create')
}

const handleEdit = (row) => {
  router.push(`/construction/edit/${row.id}`)
}

const handleDetail = (row) => {
  router.push(`/construction/edit/${row.id}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该施工记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/construction/${row.id}`)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getStageLabel = (stage) => {
  const map = {
    prepare: '准备阶段',
    foundation: '基础施工',
    bracket: '支架安装',
    module: '组件安装',
    electrical: '电气安装',
    debugging: '调试阶段',
    acceptance: '竣工验收'
  }
  return map[stage] || stage
}

const getStageTagType = (stage) => {
  const map = {
    prepare: 'info',
    foundation: '',
    bracket: 'warning',
    module: 'primary',
    electrical: 'success',
    debugging: 'danger',
    acceptance: 'success'
  }
  return map[stage] || ''
}

const getStatusLabel = (status) => {
  const map = {
    draft: '草稿',
    in_progress: '进行中',
    completed: '已完成'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    draft: 'info',
    in_progress: 'warning',
    completed: 'success'
  }
  return map[status] || ''
}

onMounted(() => {
  fetchRoofAreas()
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
