package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import actor.BotBrain;
import gui.RatBotsColorAssigner;
import gui.RatBotsWorldFrame;
import world.BotWorld;

/**
 *
 * @author pangle
 * @date March 13, 2013
 */
public class RatBotManager {
    /**
     * Present a standard JFileChooser, let the user select a folder of BotBrain Java source files, and load those source files into the world.
     * @param world
     * @param silenceStackTraces 
     */
    public static void loadRatBotsFromDirectoryIntoWorld(BotWorld world, boolean silenceStackTraces) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Open BotBrain source files from directory...");
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            RatBotManager.loadRatBotsFromDirectoryIntoWorld(chooser.getSelectedFile(), world, silenceStackTraces);
        }
    }
    
    /**
     * Present a standard JFileChooser, let the user select a BotBrain Java source file, and load that source file into the world.
     * @param world
     * @param silenceStackTraces 
     */
    public static void loadRatBotFromFileIntoWorld(BotWorld world, boolean silenceStackTraces) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Open BotBrain source file...");
        chooser.setFileFilter(new FileNameExtensionFilter("Java Source File (.java)", "java"));
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            RatBotManager.loadRatBotsFromDirectoryIntoWorld(chooser.getSelectedFile(), world, silenceStackTraces);
        }
    }
    
    
    /**
     * Load RatBots from the given package into the world.
     * @param packageName
     * @param world 
     */
    public static void loadRatBotsFromClasspath(String packageName, BotWorld world) {
        world.setMessage("BotWorld Manager is loading packaged BotBrains...");
        
        Class[] classes = PAClassLoader.getClasses(packageName, world);
        
        for (Class c : classes) {
            try {
                Object o = c.newInstance();
//        System.out.println("Class :"+c);        
                if (o instanceof BotBrain) 
                {
                    RatBotsWorldFrame bob = (RatBotsWorldFrame)world.getFrame();
                    bob.addRatBotToMenu(c);

//                    world.add((BotBrain) o);
                }
            } catch (InstantiationException ex) {
                
                
            } catch (IllegalAccessException ex) {
                //ex.printStackTrace();
                // Silence, this just means there is no package from which to load RatBots
            }
        }
        
        RatBotsColorAssigner.resetCount();

        world.setMessage("BotWorld Manager finished loading.");
    }

    
    public static void loadRatBotsFromRootDirectoryIntoWorld(BotWorld world)
    {
        JFileChooser chooser = new JFileChooser();
        loadRatBotsFromDirectoryIntoWorld(chooser.getCurrentDirectory(),world,true);
    }
    

    /**
     * Load RatBots Java source files from the given directory into the given world.
     * @param sourceDirectory
     * @param world
     * @param silenceStackTraces 
     */
    public static void loadRatBotsFromDirectoryIntoWorld(File sourceDirectory, BotWorld world, boolean silenceStackTraces) {
        assert sourceDirectory.isDirectory();

        ArrayList<BotBrain> ratbots = new ArrayList<BotBrain>();

        for (File f : sourceDirectory.listFiles()) {
            if (f.getName().toLowerCase().endsWith(".java")) {
                loadRatBotFromFileIntoWorld(f, world, silenceStackTraces);
            }
        }
        
//        world.setMessage("BotBrain Manager finished loading RatBots from '" + sourceDirectory.getPath() + File.separator + "'.");
    }

    /**
     * Load the given BotBrain Java source file into the given world.
     * @param javaSourceFile
     * @param world
     * @param silenceStackTraces 
     */
    public static void loadRatBotFromFileIntoWorld(File javaSourceFile, BotWorld world, boolean silenceStackTraces) {
        try {
            world.setMessage("BotWorld Manager is loading '" + javaSourceFile.getName() + "'...");
            // Prepare source.
            String source = PAFileUtil.readFile(javaSourceFile);

            // Save source in .java file.
            File rbTemp = new File(System.getProperty("user.home") + "/.rb_tmp"); // Get user path and set our tmp directory, and make sure its hidden in unix.
            rbTemp.deleteOnExit();
            File root = new File(rbTemp, "/java");
            File sourceFile = new File(root, "/ratbot/" + javaSourceFile.getName());
            sourceFile.getParentFile().mkdirs();
            new FileWriter(sourceFile).append(source).close();

            // Compile source file.
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            compiler.run(null, null, null, sourceFile.getPath());

            // Load and instantiate compiled class.
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
            Class<?> cls = Class.forName("ratbot." + javaSourceFile.getName().replace(".java", ""), true, classLoader);
            Object instance = cls.newInstance();

            // Add it to the world if it's a BotBrain.
            if (instance instanceof BotBrain) {
                    RatBotsWorldFrame bob = (RatBotsWorldFrame)world.getFrame();
                    bob.addRatBotToMenu(cls);
                
//                world.add((BotBrain) instance);
                world.setMessage("BotWorld Manager finished loading '" + javaSourceFile.getName() + "'.");
            } else {
                world.setMessage("BotWorld Manager was unable to load '" + javaSourceFile.getName() + "'. The source file did not define a RatBot Object.");
            }
            
            
        } catch (InstantiationException ex) {
            if (silenceStackTraces) {
                world.setMessage("RatBot Manager was unable to load '" + javaSourceFile.getName() + "'. An Instantiation Exception was thrown.");
            } else {
                showErrorDialog(javaSourceFile.getName(), "Instantiation Exception", ex.getStackTrace());
            }
        } catch (IllegalAccessException ex) {
            if (silenceStackTraces) {
                world.setMessage("RatBot Manager was unable to load '" + javaSourceFile.getName() + "'. You may not have write access to the current user directory, or you may not have read access to the file.");
            } else {
                showErrorDialog(javaSourceFile.getName(), "Illegal Access Exception", ex.getStackTrace());
            }
        } catch (ClassNotFoundException ex) {
            if (silenceStackTraces) {
                world.setMessage("RatBot Manager was unable to load '" + javaSourceFile.getName() + "'. It may not be a RatBot, or it may import objects not available in the current Java Virtual Machine.");
            } else {
                showErrorDialog(javaSourceFile.getName(), "Class Not Found Exception", ex.getStackTrace());
            }
        } catch (IOException ex) {
            if (silenceStackTraces) {
                world.setMessage("RatBot Manager was unable to load '" + javaSourceFile.getName() + "'. You may not have write access to the current user directory, or you may not have read access to the file.");
            } else {
                showErrorDialog(javaSourceFile.getName(), "IO Exception", ex.getStackTrace());
            }
        } catch (NoClassDefFoundError ex) {
            if (silenceStackTraces) {
                world.setMessage("RatBot Manager was unable to load '" + javaSourceFile.getName() + "'. It may not be a RatBot, or it may not have a valid package decleration.");
            } else {
                showErrorDialog(javaSourceFile.getName(), "No Class Definition Found", ex.getStackTrace());
            }
        }
    }

    /**
     * Display an error dialog for the given file, error type, and stack trace.
     *
     * @param fileName
     * @param errorType
     * @param stackTrace
     */
    private static void showErrorDialog(String fileName, String errorType, StackTraceElement[] stackTrace) {
        String stackTraceString = "";

        for (StackTraceElement e : stackTrace) {
            stackTraceString += "     " + e.toString() + "\n";
        }

        JOptionPane.showMessageDialog(null,
                "Ratbot Manager failed to load " + fileName + ".\n\nTechnical Issue: "
                + errorType
                + "\n\nStack trace:\n"
                + stackTraceString,
                "Ratbot Manager Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
