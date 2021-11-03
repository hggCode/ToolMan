import {
  createRouter,
  createWebHistory
} from 'vue-router'
/* Layout */
import Layout from '@/layout'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'el-icon-s-help' }
    }]
  },
  {
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: 'Example', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'Mp',
        name: 'Mp',
        component: () => import('@/views/MP/MpList'),
        meta: { title: 'Mp', icon: 'Mp' }
      },
      {
        path: 'a',
        name: 'a',
        component: () => import('@/views/MP/MpList'),
        meta: { title: 'a', icon: 'a' }
      }
    ]
  },
  {
    path: '/login',
    component:()=> import('../views/login/index'),
    meta: { title: 'login', icon: 'el-icon-s-help' },
    hidden:true
  },  

  // 404 要放在最后
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/views/404'),
    hidden: true
  }
]

const router = createRouter({
  mode: 'history',
  history: createWebHistory(),
  routes
})

export default router