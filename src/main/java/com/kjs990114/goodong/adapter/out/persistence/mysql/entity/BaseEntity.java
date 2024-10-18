package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

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
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column
    private LocalDateTime deletedAt;

    /**
     alter table user add is_available BOOLEAN GENERATED ALWAYS AS ( CASE WHEN deleted_at IS NULL THEN 1 ELSE NULL END);
     **/
    @Column(insertable = false, updatable = false)
    private Boolean isAvailable;

    public void softDelete(){
        deletedAt = LocalDateTime.now();
    }

    public void undoDelete(){
        deletedAt = null;
    }
}