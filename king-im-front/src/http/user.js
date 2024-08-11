import request from "@/http/request.js";

export const infoAPI = () => {
    return request({
        url: '/user/info',
        method: 'get'
    })
}

export const uploadAPI = (file) => {
    if (!file) {
        console.error('未指定上传文件')
        return
    }
    let formData = new FormData()
    formData.append('file', file)// 这里的file就是你要上传的文件
    return request({
        url: '/file/upload',
        method: 'post',
        data: formData
    })
}