package com.rahi.azurestorage;

import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AzureBlobAdapter {

    private final BlobClientBuilder client;

    public AzureBlobAdapter( BlobClientBuilder client ) {
        this.client = client;
    }


    public String upload( MultipartFile file, String prefixName) {

        if(file != null && file.getSize() > 0) {
            try {
                //implement your own file name logic.
                String fileName = prefixName+ UUID.randomUUID() + ".png";
                client.blobName(fileName).buildClient()
                        .upload(file.getInputStream(),file.getSize());
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] getFile(String name) {
        try {
            File temp = new File("/temp/"+name);
            BlobProperties properties = client.blobName(name).buildClient().downloadToFile(temp.getPath());
            byte[] content = Files.readAllBytes(Paths.get(temp.getPath()));
            temp.delete();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String name) {
        try {
            client.blobName(name).buildClient().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
