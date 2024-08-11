package java.com.king.netty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReadScript {

    // 遍历文件，输入流
    public static void main(String[] args) throws IOException {

        String path = "E:\\Project\\java\\own\\mini-project\\mini-netty";
        String newPath = "E:\\Project\\java\\own\\mini-project\\source";
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            throw new RuntimeException("路径不存在");
        }
        if (!Files.isDirectory(p)) {
            throw new RuntimeException("指定路径不是目录文件");
        }
        List<Path> files = Files.walk(p)
                .filter(Files::isRegularFile)
                .filter(file -> !file.startsWith("E:\\Project\\java\\own\\mini-project\\mini-netty\\target"))
                .filter(file -> !file.toString().endsWith("jpg"))
                .collect(Collectors.toList());

        for (Path file : files) {
            try {
                File sourceFile = file.toFile();
                String absolutePath = sourceFile.getAbsolutePath();
                String newAbsPath = absolutePath.replace(path, newPath);
                int idx = newAbsPath.lastIndexOf(".");
                String target = newAbsPath.substring(0, idx) + ".txt";
                List<String> strings = Files.readAllLines(file);
                Path pa = Paths.get(target);
                if (!Files.exists(pa.getParent())) {
                    Files.createDirectories(pa.getParent());
                }
                Files.write(pa, strings);
            } catch (Exception e) {
                System.out.println("异常文件：" + file);
            }
        }


        // 读取文件
        // 写入目标文件
    }
}
