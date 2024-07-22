package com.study.dvd.main;

import com.study.dvd.dao.DVDDao;
import com.study.dvd.entity.DVD;
import com.study.dvd.views.DVDAddView;

import java.util.List;
import java.util.Scanner;

public class DVDApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. DVD 추가");
            System.out.println("2. 전체 조회");
            System.out.println("3. 단건 조회 ");
            System.out.println("4. DVD 삭제");
            System.out.println("5. 프로그램 종료");
            System.out.println("메뉴 선택: ");
            String selectedMenu = scanner.nextLine();

            if("1".equals(selectedMenu)) {
                DVDAddView.show();
            } else if("2".equals(selectedMenu)) {
                List<DVD> dvdList = DVDDao.findAll(30);
                dvdList.forEach(dvd -> System.out.println(dvd));
            } else if("3".equals(selectedMenu)) {

            } else if("4".equals(selectedMenu)) {

            } else if("5".equals(selectedMenu)) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }
        }
    }
}
