package com.example.CodeFest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.CodeFest.model.Languages;
import com.example.CodeFest.services.LanguageService;
import com.example.CodeFest.util.PdfContentUtil;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/api/languages")
@RestController
@CrossOrigin
public class languageController {

    @Autowired
    private LanguageService languageService;

    @PostMapping("/insert")
    public ResponseEntity<String> insertLanguage(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("files") MultipartFile[] files, @RequestParam("pageTitle") String pageTitle,
            @RequestParam("pageSubTitle") String pageSubTitle,
            @RequestParam(value = "coverImage", required = false) MultipartFile[] coverImage) {

        List<String> errorMessages = new ArrayList<>();
        Languages languageObj = new Languages();
        String uploadDir = "LanguageImages";

        // language image upload

        Arrays.asList(files).forEach(file -> {
            long timestamp = System.currentTimeMillis();
            String fileName = timestamp + "_"
                    + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            if (isValidFileType(file)) {
                try {
                    PdfContentUtil.saveFile(uploadDir, uploadDir, fileName, file);
                    languageObj.setImageUrl(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessages.add("Error saving Image: " + fileName);
                }
            } else {
                errorMessages.add("Invalid file type in thumbnail: " + fileName + " ------ Images only..!------");
            }
        });

        // cover image upload

        if (coverImage == null) {
            languageObj.setCoverImageUrl("");
        } else {
            Arrays.asList(coverImage).forEach(file -> {
                long timestamp = System.currentTimeMillis();
                String fileName = timestamp + "_"
                        + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                if (isValidFileType(file)) {
                    try {
                        PdfContentUtil.saveFile(uploadDir, uploadDir, fileName, file);
                        languageObj.setCoverImageUrl(fileName);

                    } catch (Exception e) {
                        e.printStackTrace();
                        errorMessages.add("Error saving Cover Image: " + fileName);
                    }
                } else {
                    errorMessages.add("Invalid file type in coverImage: " + fileName + " ------ Images only..!------");
                }
            });
        }
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join("\n", errorMessages));
        }

        languageObj.setName(name);
        languageObj.setDescription(description);
        languageObj.setPageTitle(pageTitle);
        languageObj.setPageSubTitle(pageSubTitle);

        languageService.save(languageObj);
        return ResponseEntity.ok("Image uploaded successfully.");
    }

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.startsWith("image"));
    }

    @GetMapping("/getAll")
    public List<Languages> getAll() {
        return languageService.retriveLanguages();
    }

    @GetMapping("/getById/{id}")
    public Languages getById(@PathVariable("id") String id) {
        return languageService.getOne(id);
    }

    @DeleteMapping("/delete/{id}/{language}")
    public List<Languages> languageDelete(@PathVariable("id") String id, @PathVariable("language") String language) {
        return languageService.deleteLanguages(id, language);
    }

    @PutMapping("/updateLanguage")
    public Languages updateLanguage(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam("pageTitle") String pageTitle,
            @RequestParam("pageSubTitle") String pageSubTitle,
            @RequestParam(value = "coverImage", required = false) MultipartFile[] coverImage) {

        Languages languageObj = new Languages();
        String uploadDir = "LanguageImages";
        String uploadDir2 = "LanguageImages";

        // Check if there are new language images in the request
        if (files != null) {
            MultipartFile file = files[0]; // Assuming only one file is expected
            long timestamp = System.currentTimeMillis();
            String fileName = timestamp + "_"
                    + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            if (isValidFileType(file)) {
                try {
                    PdfContentUtil.saveFile(uploadDir, uploadDir2, fileName, file);
                    languageObj.setImageUrl(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid file type for language image");
            }
        } else {
            Languages data = languageService.getOne(id);
            languageObj.setImageUrl(data.getImageUrl());
        }

        if (coverImage != null) {
            MultipartFile coverFile = coverImage[0];
            long timestamp = System.currentTimeMillis();
            String coverFileName = timestamp + "_"
                    + StringUtils.cleanPath(Objects.requireNonNull(coverFile.getOriginalFilename()));

            if (isValidFileType(coverFile)) {
                try {
                    PdfContentUtil.saveFile(uploadDir, uploadDir, coverFileName, coverFile);
                    languageObj.setCoverImageUrl(coverFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid file type for cover image");
            }
        } else {
            Languages data = languageService.getOne(id);
            languageObj.setCoverImageUrl(data.getCoverImageUrl());
        }

        languageObj.setId(id);
        languageObj.setName(name);
        languageObj.setDescription(description);
        languageObj.setPageTitle(pageTitle);
        languageObj.setPageSubTitle(pageSubTitle);

        return languageService.updateLanguage(languageObj);
    }

}
