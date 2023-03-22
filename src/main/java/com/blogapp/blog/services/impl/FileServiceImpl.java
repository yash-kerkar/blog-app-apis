package com.blogapp.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blogapp.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService{
	
	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		
		String name = file.getOriginalFilename();
		String randomId = UUID.randomUUID().toString();
		String fileName = randomId.concat(name.substring(name.lastIndexOf(".")));
		String filePath = path + File.separator +fileName;
		File file2 = new File(path);
		if(!file2.exists()) file2.mkdir();
		Files.copy(file.getInputStream(), Paths.get(filePath));
		return fileName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException{
		String fullPath = path+File.separator+fileName;
		InputStream ip = new FileInputStream(fullPath);
		return ip;
 	}
	
	public String replaceImage(String path, MultipartFile file,String deleteImage) throws IOException {
		String name = file.getOriginalFilename();
		String randomId = UUID.randomUUID().toString();
		String fileName = randomId.concat(name.substring(name.lastIndexOf(".")));
		String filePath = path + File.separator +fileName;
		File file2 = new File(path);
		if(!file2.exists()) file2.mkdir();
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		String filePath2 = path + File.separator + deleteImage;
		File temp_file = new File(filePath2); 
	     if (temp_file.delete()) {
			      System.out.println(temp_file.getName() + " is successfully deleted");
	     } else {
			      System.out.println("Failed to delete " + temp_file.getName() + " file");
	     }
		
		return fileName;
	}
}
