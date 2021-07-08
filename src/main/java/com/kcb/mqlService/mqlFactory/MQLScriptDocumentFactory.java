package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.SyntaxValidator;
import com.kcb.mqlService.utils.MQLResourceConfiguration;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MQLScriptDocumentFactory {

    private static MQLScriptDocumentFactory instance;
    private final String tagName = "mql";
    private static final Logger logger = LoggerFactory.getLogger(MQLScriptDocumentFactory.class);


    private MQLScriptDocumentFactory() {
    }

    public synchronized static MQLScriptDocumentFactory getInstance() {
        if (instance == null) {
            instance = new MQLScriptDocumentFactory();
        }

        return instance;
    }

    public Map<String, String> create() throws Exception {

        Map<String, String> scriptDocumentMap = new HashMap<>();


        // 1. Create DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        for (String resourcesDir : MQLResourceConfiguration.getMqlFilePaths()) {
            Set<String> fileNames = getFileNameSet(resourcesDir);

            // 2. CreateDocument object from xml file
            fileNames.stream().forEach(fileName -> {
                try {

                    Document document = builder.parse(new File(resourcesDir + "/" + fileName));
                    document.getDocumentElement().normalize();

                    NodeList nodelist = document.getElementsByTagName(tagName);

                    IntStream.range(0, nodelist.getLength())
                            .mapToObj(nodelist::item)
                            .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                            .map(node -> (Element) node)
                            .forEach(element -> {
                                if (scriptDocumentMap.containsKey(element.getAttribute("id"))) {
                                    logger.error("MQL ID IS DUPLICATED!! {}", element.getAttribute("id") );
                                    throw new MQLQueryNotValidException();
                                }

                                scriptDocumentMap.put(element.getAttribute("id"), element.getElementsByTagName("script").item(0).getTextContent());
                            });


                } catch (SAXException | IOException e) {
                    e.printStackTrace();
                }

            });
        }

        return scriptDocumentMap;

    }

    private Set<String> getFileNameSet(String resourcesDir) {
        return Stream.of(new File(resourcesDir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

}
