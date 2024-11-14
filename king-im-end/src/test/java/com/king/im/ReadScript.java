package com.king.im;

import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.utils.JwtUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReadScript {

    // 遍历文件，输入流
    public static void main(String[] args) throws IOException {
        Date date = new Date(1725984000000L);
        System.out.println(date);
//        String userInfo = JwtUtils.getUserInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyIiwiZXhwIjoyMTU4MDQ2NzY0LCJpbmZvIjoie1widWlkXCI6MixcIm5pY2tuYW1lXCI6XCJ3XCIsXCJ1c2VybmFtZVwiOlwid1wiLFwidGVybWluYWxcIjoxfSJ9.O0aI3zzP94YoDkqvL5FSKw3RNJK1jQuT9I86-kIbPz4");
//        System.out.println(userInfo);
        // 读取文件
        // 写入目标文件
    }
}
