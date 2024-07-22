package com.study.dvd.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class DVD {
    private int dvdId;
    private String registrationNumber;
    private String title;
    private int producerId;
    private int publisherId;
    private int publicationYear;
    private LocalDate databaseDate;

    private Producer producer;
    private Publisher publisher;
}
