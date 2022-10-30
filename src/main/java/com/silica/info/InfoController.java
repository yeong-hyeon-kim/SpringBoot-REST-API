package com.silica.info;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.silica.info.model.FileData;
import com.silica.info.model.Users;
import com.silica.storage.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class InfoController {
	private InfoService infoService;
	private StorageService storageService;

	public InfoController(InfoService  infoService, StorageService storageService) {
		this.infoService = infoService;
		this.storageService = storageService;
	}

	// PathVariable
	// http://localhost:8080/userList/1234
	@GetMapping("/userList/{id}")
	public Object GetUserList(@PathVariable("id") String id) {
		List<Users> userList = infoService.getUserList();
		return userList;
	}
	
	// RequestParam
	// http://localhost:8080/UserLists?id=1234
	@GetMapping("UserLists")
	public Object GetUserLists(@RequestParam("id") String id) {
		List<Users> userList = infoService.findIdentityUser(id);
		return userList;
	}
	
	@PostMapping(value="UserAdd")
	public ResponseEntity<Users> UserAdd(@RequestBody Users user) {
		try {
			return new ResponseEntity<>(infoService.Insert(user), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="UserEdit")
	public ResponseEntity<String> UserEdit(@RequestBody Users user) {
		try {
			Integer updatedCnt = infoService.Update(user);
			return new ResponseEntity<>(String.format("%d updated", updatedCnt), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ResponseBody
	@PostMapping(value="UserDelete")
	public ResponseEntity<String> UserDelete(@RequestParam(value="id") Integer id) {
		try {
			Integer deletedCnt = infoService.Delete(id);
			return new ResponseEntity<>(String.format("%d deleted", deletedCnt), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="uploadFile")
	public ResponseEntity<String> uploadFile(MultipartFile file) throws IllegalStateException, IOException{
	    
	    if( !file.isEmpty() ) {
	        file.transferTo(new File(file.getOriginalFilename()));
	    }
	    
	    return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@PostMapping(value="upload")
	public ResponseEntity<String> upload(MultipartFile file) throws IllegalStateException, IOException{
		storageService.store(file);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
    @GetMapping(value="download")
    public ResponseEntity<Resource> serveFile(@RequestParam(value="filename") String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    @PostMapping(value="deleteAll")
    public ResponseEntity<String> deleteAll(){
        storageService.deleteAll();;
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    
    @GetMapping("fileList")
    public ResponseEntity<List<FileData>> getListFiles() {
        List<FileData> fileInfos = storageService.loadAll()
          .map(path ->{
              FileData data = new FileData();
              String filename = path.getFileName().toString();
              data.setFilename(filename);
              data.setUrl(MvcUriComponentsBuilder.fromMethodName(InfoController.class,
                        "serveFile", filename).build().toString());
              try {
                data.setSize(Files.size(path));
            } catch (IOException e) {

            }
              return data;
          })
          .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
}