package com.pbw.application.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pbw.application.custom.CustomResponse;

@Service
public class ImageService {

    public static String UPLOAD_DIRECTORY;

    public ImageService(){
        
    }
 
    public static void setUPLOAD_DIRECTORY(int id) {

        File mainDirs = new File (System.getProperty("user.dir") + "/uploads");
        if(!mainDirs.exists()){
            mainDirs.mkdirs();
            mainDirs.setExecutable(true,false);
            mainDirs.setWritable(true,false);
            mainDirs.setReadable(true,false);
        }

        UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads" + "/"+id;

        File dirs = new File(UPLOAD_DIRECTORY);
        if(!dirs.exists()){
            dirs.mkdirs();
            dirs.setExecutable(true,false);
            dirs.setWritable(true,false);
            dirs.setReadable(true,false);
        }
    }

    public Path getDirectory(int id){
        setUPLOAD_DIRECTORY(id);

            File dirs = new File(UPLOAD_DIRECTORY);
            System.out.println("DIRECTORY FILE : "+UPLOAD_DIRECTORY);
            System.out.println("folder exists: "+dirs.exists());

            if(!dirs.exists()){
                dirs.mkdirs();
                dirs.setExecutable(true,false);
                dirs.setWritable(true,false);
                dirs.setReadable(true,false);
            }
            
            Path directory = Paths.get(UPLOAD_DIRECTORY);
        return directory;
    }

    public List<String> setAllImages(Path directory,int id) throws IOException{
        List<String> paths = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)){
            for(Path file : stream){

                String pathName = "/uploads/" + id + "/" + file.getFileName().toString();
                paths.add(pathName);
            }
        }

        return paths;
    }

    // untuk nanti pas lagi set activity
    public String getActualImagePath(int id, String pathName){
        return "/uploads/" + id + "/" + pathName;
    }

    /**
     * @param id = id dari runner
     * @param type = tipe activity training (T) atau race (R)
    */
    public CustomResponse<String> addImage(MultipartFile file,int id, int nextIdActivity,String type){
   
        try {
            setUPLOAD_DIRECTORY(id);


            StringBuilder fileNames = new StringBuilder();
            
            // TODO: nanti harus ganti si nama filenya jadi sesuai si
            
            String pathName = type+"-"+nextIdActivity+"."+file.getOriginalFilename().split("\\.")[1];
    
    
            fileNames.append(pathName);
    
            
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, pathName);
            Files.write(fileNameAndPath, file.getBytes());

            File uploadedFile = fileNameAndPath.toFile();
            uploadedFile.setReadable(true,false);    
            uploadedFile.setWritable(true,false);    

            return new CustomResponse<String>(true, "Image is added", pathName);
        } catch (IOException e) {
            
            e.printStackTrace();
            
            return new CustomResponse<String>(false, "Something went wrong", null);
            
            
        }

        
        
    }
    
}