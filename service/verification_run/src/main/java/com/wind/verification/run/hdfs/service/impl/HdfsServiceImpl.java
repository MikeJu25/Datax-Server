package com.wind.verification.run.hdfs.service.impl;

import com.wind.base.exception.NoloseException;
import com.wind.doamin.ResultCode;
import com.wind.entity.hdfs.*;
import com.wind.entity.mongodb.UpsertInfo;
import com.wind.utils.database.DatabaseUtil;
import com.wind.verification.run.hdfs.service.HdfsService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Service
public class HdfsServiceImpl extends HdfsServiceAbs implements HdfsService {
    JSONObject csvReaderConfigs = new JSONObject();
    @Override
    public JSONObject readHdfs(HdfsReader hdfsReader) {
        ReaderBlock block = new ReaderBlock();

        boolean columnProvided = block.validColumn(hdfsReader.getColumn());
        boolean fieldDelimiterProvided = DatabaseUtil.isStringExist(hdfsReader.getFileDelimiter());
        boolean nullFormatProvided = DatabaseUtil.isStringExist(hdfsReader.getNullFormat());

        String fieldDelimiter = ",";

        if (!columnProvided) {
            throw new NoloseException(ResultCode.ERROR, "column参数缺失");
        }

        JSONObject result = hdfsProcessor(hdfsReader, true);
        JSONObject reader = result.getJSONObject("reader");
        JSONObject parameter = reader.getJSONObject("parameter");

        if (fieldDelimiterProvided) {
            fieldDelimiter = hdfsReader.getFileDelimiter();
        }

        List<JSONObject> blocksJson = new ArrayList<>();
        List<ReaderBlock> column = hdfsReader.getColumn();

        for (ReaderBlock b: column) {
            JSONObject blockJson = new JSONObject();
            blockJson.put("index", b.getIndex());
            blockJson.put("type", b.getType());
            blocksJson.add(blockJson);
        }

        parameter.put("column", blocksJson);
        parameter.put("fieldDelimiter", fieldDelimiter);
        if (nullFormatProvided) {
            parameter.put("nullFormat", hdfsReader.getNullFormat());
        }

        if (hdfsReader.getCsvReaderConfigs() != null) {
            CsvReaderConfig retrieved = hdfsReader.getCsvReaderConfigs();
            addToCSVChar(retrieved.getComment(), "comment");
            addToCSVChar(retrieved.getDelimiter(),"delimiter");
            addToCSVChar(retrieved.getRecordDelimiter(), "recordDelimiter ");
            addToCSVChar(retrieved.getTextQualifier(), "textQualifier");

            addToCSVBoolean(retrieved.getCaseSensitive(),"caseSensitive ");
            addToCSVBoolean(retrieved.getCaptureRawRecord(),"captureRawRecord");
            addToCSVBoolean(retrieved.getSafetySwitch(),"safetySwitch");
            addToCSVBoolean(retrieved.getTrimWhitespace(),"trimWhitespace");
            addToCSVBoolean(retrieved.getUseTextQualifier(),"useTextQualifier");
            addToCSVBoolean(retrieved.getUseComments(),"useComments");
            addToCSVBoolean(retrieved.getSkipEmptyRecords(),"skipEmptyRecords");

            addToCSVInt(retrieved.getEscapeMode(), "escapeMode");
        }

        parameter.put("csvReaderConfig", csvReaderConfigs);

        return result;
    }

    private void addToCSVChar(String str, String name) {
        if (DatabaseUtil.isStringExist(str)) {
            char c = DatabaseUtil.stringToChar(str);
            csvReaderConfigs.put(name, c + 0);
        }
    }

    private void addToCSVBoolean(Boolean b, String name) {
        if (DatabaseUtil.booleanChecker(b)) {
            csvReaderConfigs.put(name, b);
        }
    }

    private void addToCSVInt(Integer i, String name) {
        if (DatabaseUtil.integerChecker(i)) {
            csvReaderConfigs.put(name, i);
        }
    }

    @Override
    public JSONObject writeHdfs(HdfsWriter hdfsWriter) {

        JSONObject result = hdfsProcessor(hdfsWriter, false);
        JSONObject writer = result.getJSONObject("writer");
        JSONObject parameter = writer.getJSONObject("parameter");

        boolean fileNameProvided = DatabaseUtil.isStringExist(hdfsWriter.getFileName());
        boolean writeModeProvided = DatabaseUtil.isStringExist(hdfsWriter.getWriteMode());
        boolean compressProvided = DatabaseUtil.isStringExist(hdfsWriter.getCompress());
        boolean fieldDelimiterProvided = DatabaseUtil.isStringExist(hdfsWriter.getFieldDelimiter());

        List<JSONObject> blocksJson = new ArrayList<>();
        List<WriterBlock> column = hdfsWriter.getColumn();

        for (WriterBlock b: column) {
            JSONObject blockJson = new JSONObject();
            blockJson.put("name", b.getName());
            blockJson.put("type", b.getType());
            blocksJson.add(blockJson);
        }

        if (fileNameProvided) {
            parameter.put("fileName",hdfsWriter.getFileName());
        }
        if (writeModeProvided) {
            parameter.put("writeMode",hdfsWriter.getWriteMode());
        }
        if (compressProvided) {
            parameter.put("compress",hdfsWriter.getCompress());
        }
        if (fieldDelimiterProvided) {
            parameter.put("fieldDelimiter",hdfsWriter.getFieldDelimiter());
        }

        parameter.put("column", blocksJson);

        return result;
    }

}
