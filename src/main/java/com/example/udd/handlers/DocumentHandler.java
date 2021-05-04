package com.example.udd.handlers;

import com.example.udd.model.Book;

import java.io.File;

public abstract class DocumentHandler {

    public abstract Book getIndexUnit(File file);

    public abstract String getText(File file);

}
