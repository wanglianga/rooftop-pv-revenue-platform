import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'House' }
      },
      {
        path: 'roof-area',
        name: 'RoofArea',
        component: () => import('@/views/RoofAreaList.vue'),
        meta: { title: '屋顶面积管理', icon: 'HomeFilled' }
      },
      {
        path: 'inverter',
        name: 'Inverter',
        component: () => import('@/views/InverterList.vue'),
        meta: { title: '逆变器管理', icon: 'Lightning' }
      },
      {
        path: 'pv-string',
        name: 'PvString',
        component: () => import('@/views/PvStringList.vue'),
        meta: { title: '光伏组串管理', icon: 'Connection' }
      },
      {
        path: 'construction',
        name: 'Construction',
        component: () => import('@/views/ConstructionList.vue'),
        meta: { title: '施工记录管理', icon: 'Tools' }
      },
      {
        path: 'construction/create',
        name: 'ConstructionCreate',
        component: () => import('@/views/ConstructionForm.vue'),
        meta: { title: '新增施工记录', icon: 'Plus', hidden: true }
      },
      {
        path: 'construction/edit/:id',
        name: 'ConstructionEdit',
        component: () => import('@/views/ConstructionForm.vue'),
        meta: { title: '编辑施工记录', icon: 'Edit', hidden: true }
      },
      {
        path: 'grid-application',
        name: 'GridApplication',
        component: () => import('@/views/GridApplicationList.vue'),
        meta: { title: '并网申请管理', icon: 'Connection' }
      },
      {
        path: 'grid-rectification',
        name: 'GridRectification',
        component: () => import('@/views/GridRectificationList.vue'),
        meta: { title: '整改记录', icon: 'Document', hidden: true }
      },
      {
        path: 'grid-rectification/create',
        name: 'GridRectificationCreate',
        component: () => import('@/views/GridRectificationForm.vue'),
        meta: { title: '提交整改', icon: 'Plus', hidden: true }
      },
      {
        path: 'inverter-anomaly',
        name: 'InverterAnomaly',
        component: () => import('@/views/InverterAnomalyList.vue'),
        meta: { title: '逆变器异常追踪', icon: 'Warning' }
      },
      {
        path: 'inverter-anomaly/create',
        name: 'InverterAnomalyCreate',
        component: () => import('@/views/InverterAnomalyForm.vue'),
        meta: { title: '记录异常', icon: 'Plus', hidden: true }
      },
      {
        path: 'inverter-anomaly/edit/:id',
        name: 'InverterAnomalyEdit',
        component: () => import('@/views/InverterAnomalyForm.vue'),
        meta: { title: '编辑异常', icon: 'Edit', hidden: true }
      },
      {
        path: 'inverter-anomaly/:id',
        name: 'InverterAnomalyDetail',
        component: () => import('@/views/InverterAnomalyDetail.vue'),
        meta: { title: '异常详情', icon: 'Document', hidden: true }
      },
      {
        path: 'revenue-allocation',
        name: 'RevenueAllocation',
        component: () => import('@/views/RevenueAllocationList.vue'),
        meta: { title: '收益分配管理', icon: 'Money' }
      },
      {
        path: 'generation-forecast',
        name: 'GenerationForecast',
        component: () => import('@/views/GenerationForecastList.vue'),
        meta: { title: '发电量预测', icon: 'DataAnalysis' }
      },
      {
        path: 'revenue-settlement',
        name: 'RevenueSettlement',
        component: () => import('@/views/RevenueSettlementList.vue'),
        meta: { title: '收益预结算', icon: 'Wallet' }
      },
      {
        path: 'generation-anomaly',
        name: 'GenerationAnomaly',
        component: () => import('@/views/GenerationAnomalyList.vue'),
        meta: { title: '发电异常窗口', icon: 'Warning' }
      },
      {
        path: 'owner-notice',
        name: 'OwnerNotice',
        component: () => import('@/views/OwnerNoticeList.vue'),
        meta: { title: '业主公告管理', icon: 'Bell' }
      },
      {
        path: 'owner-notice/create',
        name: 'OwnerNoticeCreate',
        component: () => import('@/views/OwnerNoticeForm.vue'),
        meta: { title: '新增公告', icon: 'Plus', hidden: true }
      },
      {
        path: 'owner-notice/edit/:id',
        name: 'OwnerNoticeEdit',
        component: () => import('@/views/OwnerNoticeForm.vue'),
        meta: { title: '编辑公告', icon: 'Edit', hidden: true }
      },
      {
        path: 'vote-topic',
        name: 'VoteTopic',
        component: () => import('@/views/VoteTopicList.vue'),
        meta: { title: '投票管理', icon: 'List' }
      },
      {
        path: 'vote-topic/create',
        name: 'VoteTopicCreate',
        component: () => import('@/views/VoteTopicForm.vue'),
        meta: { title: '新增投票', icon: 'Plus', hidden: true }
      },
      {
        path: 'vote-topic/edit/:id',
        name: 'VoteTopicEdit',
        component: () => import('@/views/VoteTopicForm.vue'),
        meta: { title: '编辑投票', icon: 'Edit', hidden: true }
      },
      {
        path: 'vote-topic/:id',
        name: 'VoteDetail',
        component: () => import('@/views/VoteDetail.vue'),
        meta: { title: '投票详情', icon: 'Document', hidden: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token
  if (to.path === '/login') {
    next()
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
