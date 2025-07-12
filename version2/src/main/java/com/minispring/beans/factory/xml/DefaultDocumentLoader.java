package com.minispring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * 默认的XML文档加载器实现
 * 使用DOM4J的SAXReader解析XML文档
 */
public class DefaultDocumentLoader implements DocumentLoader {

    /**
     * 从输入流加载XML文档
     * 
     * @param inputStream XML输入流
     * @return 解析后的Document对象
     * @throws DocumentException 如果解析过程中发生错误
     */
    @Override
    public Document loadDocument(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(inputStream);
    }
} 