import { ListSchedules } from './component/scriptListSchedule.js';
import { Schedules } from './component/scriptSchedule.js';
import { Actualite } from './component/scriptActualite.js';
import { Aide } from './component/scriptAide.js';
import { UserProfile } from './component/scriptUserProfile.js';
import { Administration } from './component/scriptAdministration.js';
import { PrivateMessage } from './component/scriptPrivateMessage.js';
import { ListPrivateMessages } from './component/scriptListPrivateMessage.js';
import { Navigation } from './component/navigation.js';
import { Foot } from './component/footer.js';

const routes = [
  { path: '/', component: ListSchedules, props: { default: true } },
  { path: '/actualite', component: Actualite },
  { path: '/aide', component: Aide },
  { path: '/schedules/:id', component: Schedules, props: { default: true }},
  { path: '/users/:username/profile', component: UserProfile },
  { path: '/admin', component: Administration, props: { default: true } },
  { path: '/pm/:other', component: PrivateMessage, props: { default: true } },
  { path: '/pm', component: ListPrivateMessages }
]

const router = VueRouter.createRouter({
  history: VueRouter.createWebHashHistory(),
  routes,
})

const app = Vue.createApp({
  components: {
    'navigation': Navigation,
    'foot': Foot,
  }
})

app.use(router)
app.mount('#app')