package com.kcb.mqlService.utils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public class DomParserTest {
    private File mqlFile;

    @Before
    public void makeFileInstance() {
        mqlFile = new File("src/test/resources/mqlscript.mql");
    }


    @Test
    public void domParserTest() {


        try {
            // 1. Create DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 2. CreateDocument object from xml file
            Document document = builder.parse(mqlFile);

            // 3. Normalize the XML Structure
            document.getDocumentElement().normalize();

            // root Node
            Element root = document.getDocumentElement();
            System.out.println(root.getNodeName());

            // get all node list
            NodeList list = document.getElementsByTagName("mql");

            IntStream.range(0, list.getLength())
                    .mapToObj(list::item)
                    .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                    .map(node -> (Element)node)
                    .forEach(element -> {
                        System.out.println("mql query id : "  + element.getAttribute("id"));
                        System.out.println("mql script : " + element.getElementsByTagName("script").item(0).getTextContent());
                    });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
