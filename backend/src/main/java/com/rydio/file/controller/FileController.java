package com.rydio.file.controller;

import com.rydio.common.dto.ApiResponse;
import com.rydio.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subfolder", defaultValue = "general") String subfolder) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Please select a file to upload"));
        }

        try {
            String fileName = fileService.storeFile(file, subfolder);
            
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileDownloadUri", "/api/files/download/" + fileName);
            response.put("fileType", file.getContentType());
            response.put("size", String.valueOf(file.getSize()));
            
            return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", response));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Could not upload file: " + ex.getMessage()));
        }
    }

    @GetMapping("/download/{subfolder}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String subfolder,
            @PathVariable String fileName,
            HttpServletRequest request) {
        
        // Load file as Resource
        String fullFileName = subfolder + "/" + fileName;
        Resource resource = fileService.loadFileAsResource(fullFileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Could not determine file type
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{subfolder}/{fileName:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteFile(
            @PathVariable String subfolder,
            @PathVariable String fileName) {
        
        try {
            String fullFileName = subfolder + "/" + fileName;
            fileService.deleteFile(fullFileName);
            return ResponseEntity.ok(ApiResponse.success("File deleted successfully"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Could not delete file: " + ex.getMessage()));
        }
    }
}