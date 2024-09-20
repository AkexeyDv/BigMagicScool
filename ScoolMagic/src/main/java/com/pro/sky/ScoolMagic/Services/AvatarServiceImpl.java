package com.pro.sky.ScoolMagic.Services;

import com.pro.sky.ScoolMagic.Models.Avatar;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional

public class AvatarServiceImpl {
    @Value("avatars")
    private String avatarsDir;
    private final StudentServiceImpl studentService;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentServiceImpl studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long idStudent, MultipartFile file) throws IOException {
        Student student = studentService.findStudentById(idStudent);
        Path pathFile = Path.of(avatarsDir, idStudent + "." + getExe(file.getOriginalFilename()));
        Files.createDirectories(pathFile.getParent());
        Files.deleteIfExists(pathFile);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(pathFile, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);

        }

        Avatar avatar = findAvatar(idStudent);
        avatar.setStudent(student);
        avatar.setFilePath(pathFile.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setPreview(smallImg(pathFile));
        avatarRepository.save(avatar);


    }

    private byte[] smallImg(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);
            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics= preview.createGraphics();
            graphics.drawImage(image,0,0,100,height,null);
            graphics.dispose();
            ImageIO.write(preview,getExe(filePath.getFileName().toString()),baos);
            return baos.toByteArray();


        }
    }

    private String getExe(String nameFile){
        return nameFile.substring(nameFile.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findByStudentId(id).orElse(null);
    }
}
