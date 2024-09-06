/********************************* gif emoji  *******************************/
// const emojiTextList = ['憨笑', '媚眼', '开心', '坏笑', '可怜', '爱心', '笑哭', '拍手', '惊喜', '打气',
//     '大哭', '流泪', '饥饿', '难受', '健身', '示爱', '色色', '眨眼', '暴怒', '惊恐',
//     '思考', '头晕', '大吐', '酷笑', '翻滚', '享受', '鼻涕', '快乐', '雀跃', '微笑',
//     '贪婪', '红心', '粉心', '星星', '大火', '眼睛', '音符', "叹号", "问号", "绿叶",
//     "燃烧", "喇叭", "警告", "信封", "房子", "礼物", "点赞", "举手", "拍手", "点头",
//     "摇头", "偷瞄", "庆祝", "疾跑", "打滚", "惊吓", "起跳"
// ];

/********************************* tieba emoji  *******************************/
const emojiTextList = ['吐舌', '开心', '委屈', '礼物', '啊', '咖啡', '疑问', '生气', '大拇指',
    '吐', '坏', '太开心', '鄙视', '弱', '真棒', '赚钱', '勉强', '汗', '睡觉', '鄙视', '发亮', '喷',
    '阴险', '酷', '怒', '玫瑰', '笑眼', '滑稽', '花心', '冷', '黑线', '狂汗', '惊哭', '泪', '钱币',
    '哈哈', '音乐', '不高兴', '乖乖', '胜利', '哦耶', '彩虹', '呵呵', '咦', '惊讶']
const transform = (content) => {
    if (content == null) {
        return "";
    }
    return content.replace(/\#[\u4E00-\u9FA5]{1,3}\;/gi, textToImg);
}

let modules = import.meta.glob("@/assets/tieba_emoji/*.png", {eager: true})
// let modules = import.meta.glob("@/assets/emoji/*.gif", {eager: true})
let emojiList = Object.values(modules).map(module => module.default).sort((a, b) => {
    const regex = /(\d+).png$/;
    // const regex = /(\d+).gif$/;
    const matchA = a.match(regex)
    const matchB = b.match(regex)

    if (matchA && matchB) {
        const numberA = parseInt(matchA[1], 10);
        const numberB = parseInt(matchB[1], 10);

        return numberA - numberB;
    }
    return a.localeCompare(b);
})
// 将匹配结果替换表情图片
const textToImg = (emoText) => {
    let word = emoText.replace(/\#|\;/gi, '');
    let idx = emojiTextList.indexOf(word);
    if (idx == -1) {
        return emoText;
    }
    let url = emojiList[idx];
    return `<img src="${url}" style="width:30px;height:30px;vertical-align:bottom;"/>`
}

const textToUrl = (emoText) => {
    let word = emoText.replace(/\#|\;/gi, '');
    let idx = emojiTextList.indexOf(word);
    if (idx == -1) {
        return "";
    }
    let url = emojiList[idx];
    return url;
}

export default {textToUrl, textToImg, emojiTextList, transform, emojiList}