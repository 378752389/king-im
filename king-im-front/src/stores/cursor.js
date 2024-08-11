import {defineStore} from "pinia";
import {ref} from "vue";

export const useCursorStore = defineStore('cursor', () => {
    // 定义状态
    const x = ref(0);
    const y = ref(0);

    return {}
})