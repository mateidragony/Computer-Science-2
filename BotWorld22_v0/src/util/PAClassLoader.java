package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import world.BotWorld;

/**
 *
 * @author pangle
 * @version March 13, 2013
 */
public class PAClassLoader {
    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Class[] getClasses(String packageName, BotWorld world) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
// world.setMessage("Package name: "+packageName);
            String path = packageName.replace('.', '/');
//            String path = packageName;
//  world.setMessage("Path: "+path);
           Enumeration<URL> resources = classLoader.getResources(path);
            List<String> dirs = new ArrayList<String>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
//  world.setMessage("URL:" + resource);
                dirs.add(resource.getFile());
            }
            TreeSet<String> classes = new TreeSet<String>();
            for (String directory : dirs) {
//    System.out.println("dir:"+directory);
                classes.addAll(findClasses(directory, packageName, world));
            }
            ArrayList<Class> classList = new ArrayList<Class>();
            for (String clazz : classes) {
//    System.out.println("class:"+clazz);                
                classList.add(Class.forName(clazz));
            }
            return classList.toArray(new Class[classes.size()]);
        } catch (Exception e) {
            //e.printStackTrace();    // TODO: Remove error statement, pass to super?
            return null;
        }
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * sub directories. This code will be needed if external Jars are ever supported
     *
     * @param directory The base directory
     * @param packageName The package name for classes found inside the base
     * directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    public static TreeSet<String> findClasses(String directory, String packageName, BotWorld world) throws Exception {

//    if(directory.contains("BotWorld17"))
//    {
//        int loc = directory.indexOf("BotWorld17");
//        String newDir = directory.substring(0, loc)+"brain";
//        directory = newDir;
//    }
//    world.setMessage("in findClasses: (dir)"+directory+"  (pkg)"+packageName);
        TreeSet<String> classes = new TreeSet<String>();
        if (directory.startsWith("file:") && directory.contains("!")) {
            String[] split = directory.split("!");
            URL jar = new URL(split[0]);
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
//                System.out.println("In the while loop in findClasses()");
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replaceAll("[$].*", "").replaceAll("[.]class", "").replace('/', '.');
//    world.setMessage("className: "+className);
                    classes.add(className);
                }
            }
        }
        //Windows fixes
        directory = directory.replaceAll("%20"," ");
        if(directory.startsWith("/"))
            directory = directory.substring(1);
        
        File dir = new File(directory);
        if (!dir.exists()) {
            return classes;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file.getAbsolutePath(), packageName + "." + file.getName(), world));
            } else if (file.getName().endsWith(".class")) {
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        return classes;
    }
}
