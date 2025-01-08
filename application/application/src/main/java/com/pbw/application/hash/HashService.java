package com.pbw.application.hash;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbw.application.FileStorageService;
import com.pbw.application.FileStorageService.PersistentNode;

@Service
public class HashService {
    
    @Autowired
    private FileStorageService fsService;

    private static final Map<String, String> hashMap = new HashMap<>();

    public  String hashId(String id) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(id.getBytes());

        StringBuilder hexString = new StringBuilder();

        for(byte b: hashBytes){
            hexString.append(String.format("%02x",b));
        }

        return hexString.toString();
    }

    public String hashIdToInt(String id) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hashBytes = digest.digest(id.getBytes());

        int hashValue = 0;
        for (int i = 0; i < hashBytes.length; i++) {
            hashValue = (hashValue << 8) | (hashBytes[i] & 0xFF);
        }



        String hashedId = String.valueOf(hashValue);
        hashMap.put(hashedId, id); // Store the mapping

        fsService.save(id, hashedId);

        return hashedId;


    }

    public String getIdByHashedId(String hashedId) throws IOException {
        return fsService.getIdByHashedId(hashedId);
    }


    public String getOriginalId(String hashedId) {
        return hashMap.get(hashedId);
    }

}