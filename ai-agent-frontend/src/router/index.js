import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PetConsult from '../views/PetConsult.vue'
import SuperAgent from '../views/SuperAgent.vue'

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/pet', name: 'pet', component: PetConsult },
  { path: '/agent', name: 'agent', component: SuperAgent },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

