package com.cerebra.fileMerger;

import com.cerebra.fileMerger.ui.mainFrame.CreateMainFrameComponents;
import com.cerebra.fileMerger.util.SharedInformation;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;
import java.util.Properties;


public class FileMergerApplication extends SingleFrameApplication {
    private static AnnotationConfigApplicationContext context;
    private JFrame mainFrame;
    private SharedInformation sharedInformation;

    @Override
    protected void initialize(String[] args) {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
        sharedInformation = context.getBean("sharedInformation", SharedInformation.class);
        super.initialize(args);
    }

    @Override
    protected void startup() {
        setLookAndFeelOfApplication();
        mainFrame = getMainFrame();
        sharedInformation.setMainFrame(getMainFrame());
        CreateMainFrameComponents createMainFrameComponents = context.getBean("createMainFrameComponents", CreateMainFrameComponents.class);
        createMainFrameComponents.initialize();

        // Exit Listener.
        addExitListener(new ExitListener() {
            public boolean canExit(EventObject e) {
                return true;
            }

            public void willExit(EventObject event) {

            }
        });
        show(mainFrame);
    }

    @Override
    protected void shutdown() {
        context.close();
        super.shutdown();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        System.out.println("Close");
    }

    public static void main(String[] args) {
        Application.launch(FileMergerApplication.class, args);
    }

    private void setLookAndFeelOfApplication() {
        Properties props = new Properties();
        props.put("logoString", "FileMerger");
        props.put("dynamicLayout", "on");
        props.put("windowDecoration", "on");
        props.put("tooltipBackgroundColor", "");
        props.put("menuTextFont", "Arial BOLD 15");
        props.put("textAntiAliasing", "on");
        LunaLookAndFeel.setCurrentTheme(props);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 15));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            sharedInformation.getLogger().debug("Issue when setting the looking and feel of application." + ex.toString() + "\t" + ex.getMessage());
        }
    }
}
