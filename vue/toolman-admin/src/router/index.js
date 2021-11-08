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
        redirect: '/admin',
        children: [{
            path: 'admin ',
            name: 'Dashboard',
            component: () => import('@/views/admin/dashboard/index'),
            meta: {title: '工具人后台', icon: 'el-icon-s-help'}
        }]
    },
    {
        path: '/admin',
        component: Layout,
        name: 'Admin',
        meta: {icon: 'el-icon-s-help'},
        children: [
            {
                path: 'mobilePhoneList',
                name: 'Table',
                component: () => import('@/views/admin/mobilePhone/mobilePhoneList'),
                meta: {title: '产品列表', icon: 'mobilePhone'}
            },
            {
                path: 'detailsFrom',
                name: 'DetailsFrom',
                component: () => import('@/views/admin/mobilePhone/detailsForm'),
                meta: {title: '产品详情', icon: 'detailsFrom'},
                hidden: true
            },
            {
                path: 'updateMpForm',
                name: 'UpdateMpForm',
                component: () => import('@/views/admin/mobilePhone/updateMpForm'),
                meta: {title: '编辑产品', icon: 'updateMpForm'},
                hidden: true
            },
            {
                path: 'statistics',
                name: 'Statistics',
                component: () => import('@/views/admin/statistics/chart'),
                meta: {title: '统计分析', icon: 'statistics'},
            }
        ]
    },
    {
        path: '/index',
        name: 'user',
        component: ()=>import("../views/user/index")
    },

    {
        path: '/admin/login',
        component: () => import('../views/admin/login/index'),
        meta: {title: 'login', icon: 'el-icon-s-help'},
        hidden: true

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