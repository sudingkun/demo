package top.sudk.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.sudk.bean.MybatisMethod;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author sudingkun
 */
public class MybatisUtil {

    public static void setLine(MybatisMethod mybatisMethod, String xmlContent) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(xmlContent.getBytes()));
        org.dom4j.Element root = document.getRootElement();

        List<Element> elements = root.elements(mybatisMethod.getSqlCommandType().toString().toLowerCase());

        for (Element element : elements) {
            String id = element.attribute("id").getText();
            if (mybatisMethod.getId().equals(id)) {
                String[] split = element.asXML().split("\n");
                int sqlNodeLines = split.length;

                int startLine = findMybatisMethodLineNumber(split[0], xmlContent);
                int endLine = startLine + sqlNodeLines - 1;

                mybatisMethod.setStartLine(startLine);
                mybatisMethod.setEndLine(endLine);

            }

        }

    }


    private static int findMybatisMethodLineNumber(String method, String xml) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(xml));
        String line;
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (line.contains(method)) {
                return lineNumber;
            }


        }
        // 如果没有找到指定的方法，则返回 -1
        return -1;
    }

}
