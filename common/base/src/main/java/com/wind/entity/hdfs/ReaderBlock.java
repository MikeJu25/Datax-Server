package com.wind.entity.hdfs;

import com.wind.utils.database.DatabaseUtil;
import lombok.Data;

import java.util.List;

@Data
public class ReaderBlock {
    private int index;
    private String type;

    public boolean validColumn(List<ReaderBlock> column) {
        if (column == null || column.size() == 0) {
            return false;
        }
        for (ReaderBlock b: column) {
            if (!DatabaseUtil.isStringExist(b.getType()) || !DatabaseUtil.integerChecker(b.getIndex())) {
                return false;
            }
        }
        return true;
    }
}
