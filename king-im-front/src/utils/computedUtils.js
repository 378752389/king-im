import {computed} from "vue";

export const useComputed = (fn) => {
    const cache = new Map();
    function compare(args1, args2) {
        return (
            args1.length === args2.length &&
            args1.every((item, index) => Object.is(item, args2[index]))
        )
    }

    function getCache(args) {
        const keys = [...cache.keys()]
        const key = keys.find(item => compare(item, args))
        if (key) {
            return cache.get(key)
        }
    }

    return function (...args) {
        const cachedResult = getCache(args);
        if (cachedResult) {
            return cachedResult.value;
        }
        const result = computed(() => {
            fn(...args)
        })
        cache.set(args, result)
        return result.value
    }
}