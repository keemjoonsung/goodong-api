package com.kjs990114.goodong.common.time;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column
    private LocalDateTime deletedAt;

    public void updateModifiedAt(){
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void softDelete(){
        deletedAt = LocalDateTime.now();
    }

    public void undoDelete(){
        deletedAt = null;
    }
}