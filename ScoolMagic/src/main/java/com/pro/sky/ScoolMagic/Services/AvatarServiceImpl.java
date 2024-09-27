package com.pro.sky.ScoolMagic.Services;

import com.pro.sky.ScoolMagic.Exception.ExceptionApp;
import com.pro.sky.ScoolMagic.Models.Avatar;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Repository.AvatarRepository;
import com.pro.sky.ScoolMagic.Repository.StudentRepository;
import org.hibernate.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class AvatarServiceImpl {
    @Value(value = "${avatars.dir.path}")
    private String avatarsDir;
    private static final Logger logger= LoggerFactory.getLogger(AvatarServiceImpl.class);
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;


    public AvatarServiceImpl(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long idStudent, MultipartFile file) throws IOException {
        logger.info("Вызван метод uploadAvatar");
        Student student = studentRepository.findById(idStudent).orElseThrow(()->{
            logger.warn("Нет студента с id "+idStudent);
            new ExceptionApp("Студент не найден");
            return null;
        });
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
        System.out.println(student);

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setFilePath(pathFile.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setPreview(smallImg(pathFile));
        avatarRepository.save(avatar);


    }



    private byte[] smallImg(Path filePath) throws IOException {
        logger.info("Вызван метод smallImg");
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

    public List<Avatar> getAllAvatar(Integer pageNumber, Integer pageSize){
        logger.info("Вызван метод getAllAvatar");
        PageRequest pageRequest=PageRequest.of(pageNumber-1,pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private String getExe(String nameFile){
        logger.info("Вызван метод nameFile");
        return nameFile.substring(nameFile.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long id) {
        logger.info("Вызван метод findAvatar");
        return avatarRepository.findByStudentId(id).orElse(null);
    }
}
