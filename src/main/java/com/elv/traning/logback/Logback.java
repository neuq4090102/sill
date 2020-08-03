package com.elv.traning.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 11
 * 22
 *
 * @author lxh
 * @since 2020-04-08
 */
public class Logback {
    public static void main(String[] args) {

        debug();

        printInnerStatus();

        testLevel();
    }

    /**
     * 注意先不要给出配置文件classpath:logback-test.xml | logback.groovy |logback.xml
     */
    private static void debug() {
        Logger logger = LoggerFactory.getLogger("com.elv.traning.logback.Rudiment");
        logger.debug("Rudiment logback.");

    }

    /**
     * 打印内部的状态
     */
    private static void printInnerStatus() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    private static void testLevel() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.elv");
        logger.setLevel(Level.INFO);

        logger.warn(">>> This is warn.");
        logger.debug(">>> This is debug."); //debug < info

        Logger childLogger = LoggerFactory.getLogger("com.elv.traning.logback");
        childLogger.debug(">>> This is child debug."); // debug < info
        childLogger.info(">>> This is child info.");

    }

}
