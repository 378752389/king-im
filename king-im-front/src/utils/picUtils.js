export const genPicture = (text, width, height) => {
    if (text == null) {
        return ''
    }
    const firstLetter = text.substring(0, 1)
    const canvas = document.createElement('canvas')
    canvas.width = width || 100
    canvas.height = height || 100
    // const canvas = document.getElementById('myCanvas');
    const ctx = canvas.getContext('2d');

    // 设置字体样式
    ctx.font = 'bold 80px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    // todo 是否需要随机彩色填充
    // ctx.fillStyle = getRandomLightColor();

    ctx.fillStyle = 'rgba(255, 215, 0, 1)'
    // 离线颜色
    // ctx.fillStyle = 'rgba(128, 128, 128, 0.7)'
    ctx.fillRect(0, 0, canvas.width, canvas.height);


    // 绘制文字
    ctx.fillStyle = '#000';
    ctx.fillText(firstLetter, canvas.width / 2, canvas.height / 2);
    return canvas.toDataURL();
}

export const genDefaultPicture = () => {

}


function getRandomLightColor() {
    let r, g, b;
    do {
        r = Math.floor(Math.random() * 256);
        g = Math.floor(Math.random() * 256);
        b = Math.floor(Math.random() * 256);
    } while ((r + g + b) / 3 < 150); // 亮度阈值设为150
    return `rgb(${r}, ${g}, ${b})`;
}
