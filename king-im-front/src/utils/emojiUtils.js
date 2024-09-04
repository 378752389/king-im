const emojiTextList = ['憨笑', '媚眼', '开心', '坏笑', '可怜', '爱心', '笑哭', '拍手', '惊喜', '打气',
    '大哭', '流泪', '饥饿', '难受', '健身', '示爱', '色色', '眨眼', '暴怒', '惊恐',
    '思考', '头晕', '大吐', '酷笑', '翻滚', '享受', '鼻涕', '快乐', '雀跃', '微笑',
    '贪婪', '红心', '粉心', '星星', '大火', '眼睛', '音符', "叹号", "问号", "绿叶",
    "燃烧", "喇叭", "警告", "信封", "房子", "礼物", "点赞", "举手", "拍手", "点头",
    "摇头", "偷瞄", "庆祝", "疾跑", "打滚", "惊吓", "起跳"
];

const transform = (content) => {
    return content.replace(/\#[\u4E00-\u9FA5]{1,3}\;/gi, textToImg);
}

let modules = import.meta.glob("@/assets/emoji/*.gif", {eager: true})
let emojiList = Object.values(modules).map(module => module.default).sort((a, b) => {
    const regex = /(\d+).gif$/;
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
    return `<img src="${url}" style="width:35px;height:35px;vertical-align:bottom;"/>`
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