package com.wind.entity.textfile;

import lombok.Data;

import java.util.ArrayList;

@Data
public abstract class TextFileHandler {
    protected ArrayList<String> path;
    protected String compress, encoding, nullFormat;
}
