import {createRouter, createWebHistory} from 'vue-router'
import Home from "@/views/Home.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home,
            children: [
                {
                    path: '/chat',
                    name: 'chat',
                    component: () => import("@/views/chat/Chat.vue")
                },
                {
                    path: '/contact',
                    name: 'contact',
                    component: () => import("@/views/contact/Contact.vue")
                },
                {
                    path: '/group',
                    name: 'group',
                    component: () => import("@/views/group/Group.vue")
                },
                {
                    path: '/file',
                    name: 'file',
                    component: () => import("@/views/chat/Chat.vue")
                },
                {
                    path: '/life',
                    name: 'life',
                    component: () => import("@/views/chat/Chat.vue")
                },
            ]
        },
        {
            path: '/test',
            name: 'test',
            component: () => import("@/views/Test.vue")
        },
        {
            path: '/login',
            name: 'login',
            component: () => import("@/views/Login.vue")
        },
        {
            path: '/register',
            name: 'register',
            component: () => import("@/views/Register.vue")
        },
        {
            path: '/tieba',
            name: 'tieba',
            component: import("@/views/Tieba.vue")
        }
    ]
})

export default router
