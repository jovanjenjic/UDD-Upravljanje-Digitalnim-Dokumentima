package com.example.udd.handlers;

import com.example.udd.model.Book;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Word2007Handler extends DocumentHandler {
    @Override
    public Book getIndexUnit(File file) {
        return null;
    }

    @Override
    public String getText(File file) {
        String text = null;
        try {
            XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
            XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
            text = we.getText();
            we.close();
        }catch (Exception e) {
            System.out.println("Problem pri parsiranju docx fajla");
        }
        return text;
    }
}
