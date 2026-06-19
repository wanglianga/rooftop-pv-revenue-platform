﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<template>
  <div class="roof-area-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>屋面区域列表</span>
          <el-button type="primary" @click="handleAdd">新增区域</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="区域名称">
          <el-input v-model="searchForm.name" placeholder="请输入区域名称" clearable />
        </el-form-item>
        <el-form-item label="所属楼栋">
          <el-select v-model="searchForm.buildingNo" placeholder="请选择楼栋" clearable>
            <el-option label="1号楼" value="1" />
            <el-option label="2号楼" value="2" />
            <el-option label="3号楼" value="3" />
            <el-option label="4号楼" value="4" />
            <el-option label="5号楼" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="未施工" value="pending" />
            <el-option label="施工中" value="working" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="区域名称" width="150" />
        <el-table-column prop="buildingNo" label="所属楼栋" width="120" />
        <el-table-column prop="roofType" label="屋面类型" width="120" />
        <el-table-column prop="area" label="面积(㎡)" width="120" />
        <el-table-column prop="capacity" label="装机容量(kW)" width="150" />
        <el-table-column prop="moduleCount" label="组件数量" width="120" />
        <el-table-column prop="stringCount" label="组件串数量" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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
  name: '',
  buildingNo: '',
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
    const res = await request.get('/roof-areas', {
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
  searchForm.name = ''
  searchForm.buildingNo = ''
  searchForm.status = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/roof-area/create')
}

const handleEdit = (row) => {
  router.push(`/roof-area/edit/${row.id}`)
}

const handleDetail = (row) => {
  router.push(`/roof-area/edit/${row.id}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该区域吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/roof-areas/${row.id}`)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getStatusLabel = (status) => {
  const map = {
    pending: '未施工',
    working: '施工中',
    completed: '已完成'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    pending: 'info',
    working: 'warning',
    completed: 'success'
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
