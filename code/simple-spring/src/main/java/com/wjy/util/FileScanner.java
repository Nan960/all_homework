package com.wjy.util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class FileScanner {
    private static final String CLASS_SUFFIX = ".class";

    private String defaultClassPath;

    public FileScanner() {
        try {
            this.defaultClassPath = FileScanner.class.getResource("/").toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public FileScanner(String defaultClassPath) {
        this.defaultClassPath = defaultClassPath;
    }

    public String getDefaultClassPath() {
        return defaultClassPath;
    }

    public FileScanner setDefaultClassPath(String defaultClassPath) {
        this.defaultClassPath = defaultClassPath;
        return this;
    }

    public Set<Class<?>> search(String packageName) {
        return search(packageName, null);
    }

    public Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate) {
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = defaultClassPath;
        //然后把我们的包名basPack转换为路径名
        String basePackPath = packageName.replace(".", File.separator);
        String searchPath = classpath + basePackPath;
        return new ClassSearcher().doPath(new File(searchPath), packageName, predicate, true);
    }

    private static class ClassSearcher {

        private final Set<Class<?>> classPaths = new HashSet<>();

        private Set<Class<?>> doPath(File file, String packageName, Predicate<Class<?>> predicate, boolean flag) {

            if (file.isDirectory()) {
                //文件夹我们就递归
                File[] files = file.listFiles();
                if (!flag) {
                    packageName = packageName + "." + file.getName();
                }

                for (File f1 : files) {
                    doPath(f1, packageName, predicate, false);
                }
            } else {//标准文件
                //标准文件我们就判断是否是class文件
                if (file.getName().endsWith(CLASS_SUFFIX)) {
                    //如果是class文件我们就放入我们的集合中。
                    try {
                        Class<?> clazz = Class.forName(packageName + "." + file.getName().substring(0, file.getName().lastIndexOf(".")));
                        if (predicate == null || predicate.test(clazz)) {
                            classPaths.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
            return classPaths;
        }
    }

}
