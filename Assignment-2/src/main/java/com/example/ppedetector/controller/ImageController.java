package com.example.ppedetector.controller;

import com.example.ppedetector.service.PpeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
public class ImageController {

    private final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PpeAnalysisService ppeAnalysisService;

    public ImageController() {
        new File(UPLOAD_DIR).mkdirs(); // Create folder if not exist
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFromGallery(@RequestParam("file") MultipartFile file) throws IOException {
        return saveImage(file);
    }

    @PostMapping("/capture")
    public ResponseEntity<String> captureFromCamera(@RequestParam("file") MultipartFile file) throws IOException {
        return saveImage(file);
    }

    @GetMapping("/analyze/{filename}")
    public ResponseEntity<String> analyzeImage(@PathVariable String filename) {
        boolean result = ppeAnalysisService.analyzePpePresence(filename);
        return ResponseEntity.ok("PPE Kit Detected: " + result);
    }

    private ResponseEntity<String> saveImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty or null");
            }

            // Make sure upload folder exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generate safe filename
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + originalName;

            // Save the file
            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Uploaded Successfully: " + filename);
        } catch (Exception e) {
            e.printStackTrace(); // <-- THIS WILL SHOW YOU THE REAL ERROR IN CONSOLE
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

}
