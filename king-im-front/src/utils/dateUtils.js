import {ShowToast} from "@/components/common/func/toast.js";

export function getSendTimeNormalize(sendTimeStamp) {
    let compareTime = new Date(sendTimeStamp)
    let compareHour = compareTime.getHours()
    let compareMinute = compareTime.getMinutes()

    let timeStr = `${compareTime.getHours()}:${compareTime.getMinutes()}`

    if (isYesterday(compareTime)) {
        return `昨天 ${zeroize(compareHour)}:${zeroize(compareMinute)}`
    } else if (isLastYesterday(compareTime)) {
        return `前天 ${zeroize(compareHour)}:${zeroize(compareMinute)}`
    } else {
        return `${compareTime.getFullYear()}-${zeroize(compareTime.getMonth() + 1)}-${zeroize(compareTime.getDate())} ${timeStr}`
    }
}

export function getDateDiff(dateTimeStamp) {
    // 要比较的时间变量
    let compareTime = new Date(dateTimeStamp)
    let timestamp = compareTime.getTime();
    let compareHour = compareTime.getHours()
    let compareMinute = compareTime.getMinutes()

    //计算单位常量
    let minute = 1000 * 60;
    let hour = minute * 60;
    let day = hour * 24;
    let halfamonth = day * 15;
    let month = day * 30;
    let year = day * 365;

    //今天的时间
    let now = new Date().getTime();

    //时间差
    let diffValue = now - timestamp;

    //结果
    let result;

    if (diffValue < 0) {
        // 误差不超过三秒允许接受
        if (Math.ceil(diffValue / 3000) === 0) {
            return '刚刚';
        } else {
            console.warn("请同步一下系统时间为UTC-8, 否则部分功能将无法正常显示")
            return '-';
        }
    }
    let yearC = diffValue / year;
    let monthC = diffValue / month;
    let weekC = diffValue / (7 * day);
    let dayC = diffValue / day;
    let hourC = diffValue / hour;
    let minC = diffValue / minute;

    if (isLastYesterday(timestamp)) {
        result = `前天 ${zeroize(compareHour)}:${zeroize(compareMinute)}`
    } else if (isYesterday(timestamp)) {
        result = `昨天 ${zeroize(compareHour)}:${zeroize(compareMinute)}`
    } else if (yearC >= 1) {
        result = `${parseInt(yearC)} 年前`
    } else if (monthC >= 1) {
        result = `${parseInt(monthC)} 月前`
    } else if (weekC >= 1) {
        result = `${parseInt(weekC)} 周前`
    } else if (dayC >= 1) {
        result = `${parseInt(dayC)} 天前`
    } else if (hourC >= 1) {
        result = `${parseInt(hourC)} 小时前`
    } else if (minC >= 1) {
        result = `${parseInt(minC)} 分钟前`
    } else {
        result = "刚刚"
    }
    return result;
}

// 补0
function zeroize(num) {
    return num < 10 ? "0" + num : num
}

// 判断是否是昨天
function isYesterday(time) {
    const date = new Date();
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const today = `${year}/${month}/${day}`;
    const todayTime = new Date(today).getTime(); // 当天凌晨的时间
    const yesterdayTime = new Date(todayTime - 24 * 60 * 60 * 1000).getTime(); // 昨天凌晨的时间
    return time < todayTime && yesterdayTime <= time;
}


//是否是前天
function isLastYesterday(time) {
    const twentyFourHours = 24 * 60 * 60 * 1000;
    const fortyEightHours = 24 * 60 * 60 * 1000 * 2;
    const date = new Date();
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const today = `${year}/${month}/${day}`;
    const todayTime = new Date(today).getTime(); // 当天凌晨的时间
    const yesterdayTime = new Date(todayTime - twentyFourHours).getTime(); // 昨天凌晨的时间
    const lastYesterdayTime = new Date(todayTime - fortyEightHours).getTime(); // 昨天凌晨的时间
    return time < yesterdayTime && lastYesterdayTime <= time;
}