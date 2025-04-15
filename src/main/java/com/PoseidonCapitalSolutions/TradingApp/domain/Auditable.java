package com.PoseidonCapitalSolutions.TradingApp.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class Auditable {

    @CreatedBy
    @Column(name = "creation_name", updatable = false)
    protected String creationName;

    @CreatedDate
    @Column(name = "creation_date", updatable = false)
    protected LocalDateTime creationDate;

    @LastModifiedBy
    @Column(name = "revision_name")
    protected String revisionName;

    @LastModifiedDate
    @Column(name = "revision_date")
    protected LocalDateTime revisionDate;

}
