﻿<template>
  <div class="inverter-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>逆变器列表</span>
          <el-button type="primary" @click="handleAdd">新增逆变器</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="设备名称">
          <el-input v-model="searchForm.name" placeholder="请输入设备名称" clearable />
        </el-form-item>
        <el-form-item label="设备型号">
          <el-input v-model="searchForm.model" placeholder="请输入设备型号" clearable />
        </el-form-item>
        <el-form-item label="所属区域">
          <el-select v-model="searchForm.roofAreaId" placeholder="请选择区域" clearable>
            <el-option v-for="area in roofAreas" :key="area.id" :label="area.name" :value="area.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="运行中" value="running" />
            <el-option label="待机" value="standby" />
            <el-option label="故障" value="fault" />
            <el-option label="离线" value="offline" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="设备名称" width="150" />
        <el-table-column prop="model" label="设备型号" width="150" />
        <el-table-column prop="serialNo" label="序列号" width="180" />
        <el-table-column prop="roofAreaName" label="所属区域" width="150" />
        <el-table-column prop="ratedPower" label="额定功率(kW)" width="150" />
        <el-table-column prop="currentPower" label="当前功率(kW)" width="150" />
        <el-table-column prop="dayEnergy" label="日发电量(kWh)" width="160" />
        <el-table-column prop="totalEnergy" label="总发电量(kWh)" width="160" />
        <el-table-column prop="efficiency" label="转换效率(%)" width="140" />
        <el-table-column prop="temperature" label="温度(°C)" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="installDate" label="安装日期" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button type="success" link @click="handleViewCurve(row)">发电曲线</el-button>
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
const roofAreas = ref([])

const searchForm = reactive({
  name: '',
  model: '',
  roofAreaId: '',
  status: ''
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
    const res = await request.get('/inverter/list', {
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
  searchForm.model = ''
  searchForm.roofAreaId = ''
  searchForm.status = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/inverter/create')
}

const handleEdit = (row) => {
  router.push(`/inverter/edit/${row.id}`)
}

const handleDetail = (row) => {
  router.push(`/inverter/edit/${row.id}`)
}

const handleViewCurve = (row) => {
  router.push(`/power-curve?inverterId=${row.id}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该逆变器吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/inverter/${row.id}`)
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
    running: '运行中',
    standby: '待机',
    fault: '故障',
    offline: '离线'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    running: 'success',
    standby: 'warning',
    fault: 'danger',
    offline: 'info'
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
