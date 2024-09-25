package com.pro.sky.ScoolMagic.Controllers;

import com.pro.sky.ScoolMagic.Models.Avatar;
import com.pro.sky.ScoolMagic.Services.AvatarServiceImpl;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarServiceImpl avatarService;

    public AvatarController(AvatarServiceImpl avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@PathVariable Long studentId, @RequestParam MultipartFile multipartFile) throws IOException {
        avatarService.uploadAvatar(studentId, multipartFile);
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<Avatar>> getAvatar(@RequestParam("page") Integer pageNumber
            , @RequestParam("size") Integer pageSize){

        return ResponseEntity.ok(avatarService.getAllAvatar(pageNumber, pageSize));

    }


}
