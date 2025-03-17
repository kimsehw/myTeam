package com.kimsehw.myteam.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originFileName, byte[] fileData) {

        UUID uuid = UUID.randomUUID();

        String extension = originFileName.substring(originFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileUploadFullUrl);

            fos.write(fileData);
            fos.close();
        } catch (FileNotFoundException e) {
            log.info(fileUploadFullUrl);
            throw new RuntimeException("파일을 찾을 수 없음");
        } catch (IOException e) {
            log.info("파일 업로드 1");
            throw new RuntimeException("파일 업로드 중 오류 발생");
        }
        return savedFileName;
    }

    public void deleteFiles(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info(deleteFile.getAbsolutePath() + "파일 삭제");
            return;
        }
        log.info("파일이 존재하지 않습니다.");
    }
}
