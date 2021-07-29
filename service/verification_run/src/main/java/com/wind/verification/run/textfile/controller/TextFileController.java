package com.wind.verification.run.textfile.controller;

import com.wind.base.exception.NoloseException;
import com.wind.doamin.R;
import com.wind.doamin.ResultCode;
import com.wind.entity.textfile.TextFileReader;
import com.wind.entity.textfile.TextFileWriter;
import com.wind.utils.database.FileUtils;
import com.wind.verification.run.textfile.service.TextFileService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@CrossOrigin
public class TextFileController {

    @Autowired
    TextFileService textFileService;

    @PostMapping("/readTextFile")
    public R readTextFile(@RequestBody TextFileReader textFileReader) {
        JSONObject result = textFileService.readTextFile(textFileReader);
        return (result == null) ? R.error().data("error", "参数缺失") : R.ok().data("json", result);
    }

    @PostMapping("/writeTextFile")
    public R writeTextFile(@RequestBody TextFileWriter textFileWriter) {
        JSONObject result = textFileService.writeTextFile(textFileWriter);
        return (result == null) ? R.error().data("error", "参数缺失") : R.ok().data("json", result);
    }
    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file){
        if (Objects.isNull(file) || file.isEmpty()) {
            //判断非空
            return R.error().data("msg","文件为空，请重新上传");
        }
        try {
            byte[] bytes = file.getBytes();
            //要存入本地的地址放到path里面
            Path path = Paths.get( FileUtils.UPLOAD_FOLDER + "/");
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(path);
            }
            String extension = FileUtils.getFileExtension(file);  //获取文件后缀
            String fileName  = UUID.randomUUID() + extension;
            FileUtils.getFileByBytes(bytes, FileUtils.UPLOAD_FOLDER, fileName);
            return R.ok().data("msg","上传成功").data("path",path+ "\\" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error().data("msg","上传失败");
    }

    @PostMapping ("/createFile")
    public R createFile(@RequestParam String fileName) {
        File file = new File("./"+ fileName);
        if (file.exists()) {
            return R.error().data("msg","同名文件已被创建");
        }
        try {
            file.createNewFile();
        } catch (IOException exp) {
            return R.error().data("msg","文件创建失败");
        }
        Path path = Paths.get( FileUtils.UPLOAD_FOLDER + "/");
        return R.ok().data("msg","文件创建成功").data("path", path+ "\\" + fileName);
    }

    @DeleteMapping("/deleteFile")
    public R deleteFile(@RequestBody String fileName) {
        File file = new File(fileName);
        boolean isDeleted = false;
        if (file.exists() && file.isFile()) {
            isDeleted = file.delete();
        } else {
            return R.error().data("msg","未找到要删除的文件");
        }
        if (isDeleted) {
            return R.ok().data("msg","文件已删除");
        } else {
            return R.error().data("msg","文件删除失败");
        }
    }
}
