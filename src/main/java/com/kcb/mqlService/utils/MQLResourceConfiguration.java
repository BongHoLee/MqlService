package com.kcb.mqlService.utils;

import com.kcb.mqlService.mqlFactory.MQLQueryContextFactory;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.SyntaxValidator;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MQLResourceConfiguration {
    private static List<String> mqlFilePaths = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(MQLResourceConfiguration.class);

    public static void addFilePath(String path) {
        mqlFilePaths.add(path);
    }

    public static List<String> getMqlFilePaths() {
        if (mqlFilePaths.isEmpty()) {
            logger.error("MQL script path was not defined");
            throw new RuntimeException("MQL script path was not defined");
        }
        return mqlFilePaths;
    }
}
