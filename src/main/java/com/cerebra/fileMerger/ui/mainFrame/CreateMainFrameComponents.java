package com.cerebra.fileMerger.ui.mainFrame;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.cerebra.fileMerger.ui.AboutDialog;
import com.cerebra.fileMerger.util.SharedInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URL;

@Component("createMainFrameComponents")
public class CreateMainFrameComponents {
    private SharedInformation sharedInformation;
    private MainPane mainPane;
    /**
     * Below components are initialized here.
     */
    private AboutDialog aboutDialog;

    @Autowired
    private void setSharedInformation(SharedInformation sharedInformation) {
        this.sharedInformation = sharedInformation;
    }

    @Autowired
    private void setHomeTab(MainPane homeTab) {
        this.mainPane = homeTab;
    }

    @Autowired
    public void setAboutDialog(AboutDialog aboutDialog) {
        this.aboutDialog = aboutDialog;
    }

    /**
     * Sets frame properties for main-frame
     */
    void setFrameProperties() {
        Frame frame = sharedInformation.getMainFrame();
        frame.setMinimumSize(new Dimension(sharedInformation.getXSize(), sharedInformation.getYSize()));
        //Position to the center of the screen
        frame.setLocation(0, 0);
        frame.setResizable(false);
        frame.setTitle("File Merger ++");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo-cerebra.png")));
        frame.setBackground(Color.ORANGE);
        frame.setLayout(new BorderLayout());
    }

    /**
     * Creates the body of the main-frame.
     * Main frame is a splitPane with a tree on the left side and tabbedPane on right-side.
     */
    void createBody() {
        sharedInformation.getMainFrame().add(this.mainPane.createHomeTab(sharedInformation));
    }

    void createLogger(URL path) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        String logDir = sharedInformation.getWorkingDirectory() + "/";
        System.setProperty("log.dir", logDir);
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        try {
            jc.doConfigure(path);
        } catch (JoranException e) {
            e.printStackTrace();
        }
        Logger logger = LoggerFactory.getLogger(this.getClass());
        sharedInformation.setLogger(logger);
        logger.info("Application Started");
    }

    public void initialize() {
        setFrameProperties();
        createBody();
        createLogger(getClass().getResource("/logback.xml"));
        aboutDialog.createDialog(sharedInformation.getMainFrame());
    }
}
