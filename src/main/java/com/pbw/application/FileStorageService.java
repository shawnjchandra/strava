package com.pbw.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Service
public class FileStorageService {
    private static final String FILE_PATH = "data.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void save(String id, String hashedId) throws IOException {

        List<PersistentNode> nodes = readAll(); // Read existing nodes
        for(PersistentNode node : nodes){
            if (hashedId.equals(node.getHashedId())) {
                return;
            }
        }
        nodes.add(new PersistentNode(id, hashedId)); // Add new node

       
        objectMapper.writeValue(new File(FILE_PATH), nodes);
    }

    public List<PersistentNode> readAll() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(); 
            
        }
        return objectMapper.readValue(file, new TypeReference<List<PersistentNode>>() {});
    }

     
     public String getIdByHashedId(String hashedId) throws IOException {
        ////System.out.println(hashedId);
        List<PersistentNode> nodes = readAll(); 
        for (PersistentNode node : nodes) {
            ////System.out.println(node);
            if (node.getHashedId().equals(hashedId)) {
                return node.getId(); 
            }
        }
        return null; 
    }

    @Getter
    @Setter
    @Data
    public static class PersistentNode {
        private String id;
        private String hashedId;

        public PersistentNode() {}

        public PersistentNode(String id, String hashedId) {
            this.id = id;
            this.hashedId = hashedId;
        }

  
    }
}
