package com.wind.entity.hdfs;

import com.wind.utils.database.DatabaseUtil;
import lombok.Data;

import java.util.List;

@Data
public class WriterBlock {
    private String name, type;

    public boolean validColumn(List<WriterBlock> column) {
        if (column == null || column.size() == 0) {
            return false;
        }
        for (WriterBlock b: column) {
            if (!DatabaseUtil.isStringExist(b.getName()) || !DatabaseUtil.isStringExist(b.getType())) {
                return false;
            }
        }
        return true;
    }
}
