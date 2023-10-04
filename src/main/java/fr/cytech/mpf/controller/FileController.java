package fr.cytech.mpf.controller;

import fr.cytech.mpf.dto.FileGetDTO;
import fr.cytech.mpf.dto.MsgGetDTO;
import fr.cytech.mpf.entity.MsgInfo;
import fr.cytech.mpf.repository.FileRepository;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import fr.cytech.mpf.entity.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileRepository fileRepository;

    private String uploadDir = "C:\\Users\\CYTech Student\\IdeaProjects\\monPapiFinito\\src\\main\\resources\\img";

    ModelMapper modelMapper;

    FileController() {
        modelMapper = new ModelMapper();
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){
        try{
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetPath = Paths.get(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setFileType(file.getContentType());
            fileInfo.setFileSize(file.getSize());
            fileRepository.save(fileInfo);

            return ResponseEntity.ok("File uploaded successfully");
        }catch (IOException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<FileGetDTO> getFileById(@RequestParam Long id) {
        FileInfo fileInfo = fileRepository.getReferenceById(id);
        FileGetDTO fileGetDTO = modelMapper.map(fileInfo, FileGetDTO.class);
        return ResponseEntity.ok(fileGetDTO);
    }

    @GetMapping(value = "/famille")
    public ResponseEntity<List<FileGetDTO>> getMsgByConvId(@RequestParam Long conv) {
        List<FileInfo> fileInfos = fileRepository.findByConv(conv);
        List<FileGetDTO> msgGetDTO = fileInfos.stream()
                .map(fileInfo -> modelMapper.map(fileInfo, FileGetDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(msgGetDTO);
    }
}
