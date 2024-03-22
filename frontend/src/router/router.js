// Vuex 때 처럼 create* 함수를 제공한다.
import { createWebHistory, createRouter } from 'vue-router';


const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'), // 동적 import
    },
    {
        path: '/event',
        name: 'Event',
        component: () => import('@/views/Event.vue'),
    },
    {
        path: '/eventList',
        name: 'EventList',
        component: () => import('@/views/EventListView.vue'),
    },
    {
        path: '/eventDetail/:eventId',
        name: 'EventDetail',
        component: () => import('@/views/EventDetailView.vue'),
    },
    {
        path: '/ticket-viewer', // 경로 설정
        name: 'TicketViewer', // 이름 설정
        component: () => import('@/views/TicketViewerView.vue'),
    },
    {
        path: '/myTicketList', // 경로 설정
        name: 'MyTicketList', // 이름 설정
        component: () => import('@/views/MyTicketListView.vue'),
        meta: { requiresAuth: true } // 로그인 필요 여부를 메타 필드로 추가
    },
    // {
    //     path: '/reservation',
    //     name: 'Reservation',
    //     component: () => import('@/views/ReservationView.vue'),
    // },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue'),
    },
    {
        path: '/sign-up',
        name: 'SignUp',
        component: () => import('@/views/SignUpView.vue'),
    },
    {
        path: '/admin',
        name: 'BackOffice',
        component: () => import('@/views/BackOffice.vue'),
        meta: { requiresAuth: true } // 로그인 필요 여부를 메타 필드로 추가
    },
];

export const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const isAuthenticated = localStorage.getItem('token'); // 로그인 여부 확인
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth); // 해당 경로가 인증 필요한지 확인

    if (requiresAuth && !isAuthenticated) {
        // 사용자가 인증이 필요한 페이지로 이동하려고 하지만 로그인하지 않은 경우
        next('/login'); // 로그인 페이지로 리다이렉트
    } else {
        next(); // 그 외의 경우에는 이동 허용
    }
});

//export default router;