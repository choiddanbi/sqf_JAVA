package com.study.dvd.views;

import com.study.dvd.dao.DVDDao;
import com.study.dvd.entity.DVD;
import com.study.dvd.entity.Producer;
import com.study.dvd.entity.Publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DVDAddView {
    public static void show() {
        Scanner scanner = new Scanner(System.in);
        String registrationNumber = null;
        String title = null;
        String producerName = null;
        String publisherName = null;
        String publicationYear = null;

        Map<String, Integer> successCount = new HashMap<>();

        System.out.println("[ DVD 추가 화면 ]");
        System.out.print("등록번호 입력 >>> ");
        registrationNumber = scanner.nextLine();

        System.out.print("제목 입력 >>> ");
        title = scanner.nextLine();

        System.out.print("제작사 입력 >>> ");
        producerName = scanner.nextLine();

        System.out.print("발행사 입력 >>> ");
        publisherName = scanner.nextLine();

        System.out.print("발행년도 입력 >>> ");
        publicationYear = scanner.nextLine();

        Producer producer = Producer.builder().producerName(producerName).build();
        successCount.put("producer", DVDDao.addProducer(producer));


        Publisher publisher = Publisher.builder().publisherName(publisherName).build();
        successCount.put("publisher", DVDDao.addPublisher(publisher));

        DVD dvd = DVD.builder()
                .registrationNumber(registrationNumber)
                .title(title)
                .publicationYear(Integer.parseInt(publicationYear))
                .producer(producer)
                .publisher(publisher)
                .build();
        successCount.put("dvd", DVDDao.addDvd(dvd));

        System.out.println("DVD 추가 정보");
        successCount.forEach((k, v) -> {
            System.out.println(k + " >> 성공 " + v + "건");
        });

        /* 람다식 안 쓸 경우
        for(Map.Entry<String, Integer> entry : successCount.entrySet()) {
            String k = entry.getKey();
            Integer v = entry.getValue();
            System.out.println(k + " >> 성공 " + v + "건");
        }*/
    }
}
