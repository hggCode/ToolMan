import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// axios
import axios from '@/utils/request'
import VueAxios from "vue-axios";

//element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import '@/styles/index.scss' // global css



createApp(App).use(store).use(router).use(VueAxios,axios).use(ElementPlus).mount('#app')
