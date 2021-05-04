package com.example.udd.handlers;

import com.example.udd.model.Book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.hwpf.extractor.WordExtractor;

public class WordHandler extends DocumentHandler {


    @Override
    public Book getIndexUnit(File file) {
        return null;
    }

    @Override
    public String getText(File file) {
        String text = null;
        try {
            WordExtractor we = new WordExtractor(new FileInputStream(file));
            text = we.getText();
            we.close();
        } catch (FileNotFoundException e1) {
            System.out.println("Dokument ne postoji");
        } catch (Exception e) {
            System.out.println("Problem pri parsiranju doc fajla");
        }
        return text;
    }
}
