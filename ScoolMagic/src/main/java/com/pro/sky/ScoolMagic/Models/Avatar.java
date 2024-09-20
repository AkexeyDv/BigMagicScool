package com.pro.sky.ScoolMagic.Models;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String filePath;
    long fileSize;
    String mediaType;
    @Lob
    byte[] preview;
    @OneToOne
    Student student;

    public Avatar(){};

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public byte[] getPreview() {
        return preview;
    }

    public Student getStudent() {
        return student;
    }


}