﻿<template>
  <div class="pv-string-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>组件串列表</span>
          <el-button type="primary" @click="handleAdd">新增组件串</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="串编号">
          <el-input v-model="searchForm.stringNo" placeholder="请输入串编号" clearable />
        </el-form-item>
        <el-form-item label="所属区域">
          <el-select v-model="searchForm.roofAreaId" placeholder="请选择区域" clearable>
            <el-option v-for="area in roofAreas" :key="area.id" :label="area.name" :value="area.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属逆变器">
          <el-select v-model="searchForm.inverterId" placeholder="请选择逆变器" clearable>
            <el-option v-for="inverter in inverters" :key="inverter.id" :label="inverter.name" :value="inverter.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="normal" />
            <el-option label="异常" value="abnormal" />
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
        <el-table-column prop="stringNo" label="串编号" width="150" />
        <el-table-column prop="roofAreaName" label="所属区域" width="150" />
        <el-table-column prop="inverterName" label="所属逆变器" width="150" />
        <el-table-column prop="moduleCount" label="组件数量" width="120" />
        <el-table-column prop="moduleType" label="组件类型" width="150" />
        <el-table-column prop="ratedPower" label="额定功率(kW)" width="150" />
        <el-table-column prop="currentPower" label="当前功率(kW)" width="150" />
        <el-table-column prop="temperature" label="温度(°C)" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="installDate" label="安装日期" width="120" />
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
const roofAreas = ref([])
const inverters = ref([])

const searchForm = reactive({
  stringNo: '',
  roofAreaId: '',
  inverterId: '',
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

const fetchInverters = async () => {
  try {
    const res = await request.get('/inverter/list', { params: { pageNum: 1, pageSize: 100 } })
    inverters.value = res.data.list
  } catch (error) {
    ElMessage.error('获取逆变器列表失败')
  }
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await request.get('/pv/string/list', {
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
  searchForm.stringNo = ''
  searchForm.roofAreaId = ''
  searchForm.inverterId = ''
  searchForm.status = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/pv-string/create')
}

const handleEdit = (row) => {
  router.push(`/pv-string/edit/${row.id}`)
}

const handleDetail = (row) => {
  router.push(`/pv-string/edit/${row.id}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该组件串吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/pv/string/${row.id}`)
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
    normal: '正常',
    abnormal: '异常',
    offline: '离线'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    normal: 'success',
    abnormal: 'warning',
    offline: 'danger'
  }
  return map[status] || ''
}

onMounted(() => {
  fetchRoofAreas()
  fetchInverters()
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
