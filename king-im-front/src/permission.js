import router from '@/router/index.js'

import {useUserStore} from "@/stores/user.js";

router.beforeEach(async (to, from) => {
    const userStore = useUserStore();
    if (!userStore.auth?.accessToken && to.name !== 'login') {
        return {name: 'login'}
    }
})