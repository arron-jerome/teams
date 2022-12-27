package com.disney.teams.utils.type;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class PackageUtils implements Serializable {

    //PROTOCOL http, https, ftp, file, and jar
    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";

    private static void fillClassesByFile(List<Class<?>> classes, String packageName, File dir,
        final boolean recursive) {
        //仅限目录
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                fillClassesByFile(classes, packageName + "." + file.getName(), file, recursive);
            } else {
                //如果是java类文件去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    //添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void fillClassesByJar(List<Class<?>> classes, final String packageDirName, JarFile jar, final boolean recursive) {
        //如果是jar包文件
        //定义一个JarFile

        //从此jar包 得到一个枚举类
        Enumeration<JarEntry> entries = jar.entries();
        //同样的进行循环迭代
        while (entries.hasMoreElements()) {
            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (entry.isDirectory() || !name.endsWith(".class")) {
                continue;
            }

            //如果是以/开头的
            if (name.charAt(0) == '/') {
                //获取后面的字符串
                name = name.substring(1);
            }
            //过滤不同包名的类
            if (!name.startsWith(packageDirName)) {
                continue;
            }

            //如果不获取子目录的类，则忽略
            if(!recursive && name.contains("/")) {
                continue;
            }

            //去掉.class后缀，并将/改为.
            String className = name.substring(0, name.length() - 6).replace('/', '.');
            try {
                //添加到classes
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 扫描获取包下面的类
     * @param pack 包
     * @return
     * @throws IOException
     */
    public static List<Class<?>> findClasses(Package pack) throws IOException {
        return findClasses(pack, true);
    }

    /**
     * 扫描获取包下面的类
     * @param pack 包
     * @param recursive 是否获取子目录中的类
     * @return
     * @throws IOException
     */
    public static List<Class<?>> findClasses(Package pack, boolean recursive) throws IOException {
        if (pack == null) {
            return null;
        }

        List<Class<?>> classes = new ArrayList<>();

        //获取包的名字 并进行替换
        String packageDirName = pack.getName().replace('.', '/');

        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if (FILE_PROTOCOL.equals(protocol)) {
                String filePath = StringUtils.urlDecode(url.getFile());
                fillClassesByFile(classes, pack.getName(), new File(filePath), recursive);
            } else if (JAR_PROTOCOL.equals(protocol)) {
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                fillClassesByJar(classes, packageDirName, jar, recursive);
            }
        }
        return classes;
    }

    /**
     * 扫描获取包下面的类
     * @param packageName 包名
     * @return
     */
    public static List<Class<?>> findClassesByPackageName(String packageName) throws IOException {
        return findClassesByPackageName(packageName, true);
    }

    /**
     * 扫描获取包下面的类
     * @param packageName 包名
     * @param recursive 是否获取子目录中的类
     * @return
     * @throws IOException
     */
    public static List<Class<?>> findClassesByPackageName(String packageName, boolean recursive) throws IOException {
        if (StringUtils.isBlank(packageName)) {
            return null;
        }
        Package pack = Package.getPackage(packageName);
        return findClasses(pack, recursive);
    }


}
