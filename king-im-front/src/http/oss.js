import request from "@/http/request.js";
import axios from "axios";
import {ShowToast} from "@/components/common/func/toast.js";
export const getPreSignedAPI = ({filename, businessType}) => {
    return request({
        url: '/oss/upload',
        method: 'post',
        data: {
            filename,
            businessType,
        }
    })
}

export const minioUploadAPI = ({url, file}) => {
    return axios({
        url: url,
        method: 'put',
        headers: {
          "Content-Type": "application/octet-stream",
        },
        data: file
    })
}

export const ossUploadAPI = async ({file, businessType}) => {
    let signResponse = {};
    try {
        signResponse = await getPreSignedAPI({
            filename: file.name,
            businessType,
        })
    } catch (e) {
        ShowToast({
            message: "获取预上传链接失败",
            type: 'danger'
        })
        console.error(e);
    }

    try {
        const resp = await minioUploadAPI({
            url: signResponse.uploadUrl,
            file
        })
        if (resp.status === 200) {
            return signResponse.downloadUrl
        }
        throw new Error("文件上传oss失败")
    } catch (e) {
        ShowToast({
            message: "文件上传oss失败",
            type: 'danger'
        })
        console.error(e);
    }



}