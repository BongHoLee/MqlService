package com.kcb.mqlService.mqlFactory;

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
    private String resourcesDir = "src/test/resources";
    private String tagName = "mql";


    private MQLScriptDocumentFactory(){ }

    public synchronized static  MQLScriptDocumentFactory getInstance() {
        if (instance == null) {
            instance = new MQLScriptDocumentFactory();
        }

        return instance;
    }

    public Map<String, String> create() throws Exception {
        Map<String, String> scriptDocumentMap = new HashMap<>();
        Set<String> fileNames = getFileNameSet();

        // 1. Create DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        // 2. CreateDocument object from xml file
        fileNames.stream().forEach(fileName -> {
            try {
                Document document = builder.parse(new File(resourcesDir + "/" + fileName));
                document.getDocumentElement().normalize();

                NodeList nodelist = document.getElementsByTagName(tagName);

                IntStream.range(0, nodelist.getLength())
                        .mapToObj(nodelist::item)
                        .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                        .map(node -> (Element)node)
                        .forEach(element -> {
                            if (scriptDocumentMap.containsKey(element.getAttribute("id"))) {
                                throw new RuntimeException("MQL ID IS DUPLICATED!!" + element.getAttribute("id"));
                            }

                            scriptDocumentMap.put(element.getAttribute("id"), element.getElementsByTagName("script").item(0).getTextContent());
                        });

            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }

        });

        return scriptDocumentMap;

    }

    private Set<String> getFileNameSet() {
        return   Stream.of(new File(resourcesDir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

}
