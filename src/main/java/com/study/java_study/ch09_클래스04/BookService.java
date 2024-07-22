package com.study.java_study.ch09_클래스04;

// 정보 가공 ? 기능 !
import java.util.Scanner;

public class BookService { // 기능 클래스
    private Scanner scanner; // 같은 클래스 안에서 scanner 객체를 계속 사용 ㄱㅏ능
    private BookRepository bookRepository;

    public BookService() { // 객체 생성
        scanner = new Scanner(System.in);
        bookRepository = new BookRepository();
    }


    // 메서드 1.
    private String selectMenu() {
        String menus = "1234q";
        String selectedMenu = null;

        while (true) {
            System.out.print("메뉴 선택: ");
            selectedMenu = scanner.nextLine();
            if(menus.contains(selectedMenu)) { // menus 에 있는 값을 입력하면 if문 끝!
                break; // return selectedMenu; 로 ~
            }
            System.out.println("잘못된 입력입니다. 다시 입력하세요."); // 다시 while문!
        }
        return selectedMenu;
    }

    // 메서드 2.
    public boolean run() {
        boolean isRun = true;

        System.out.println("[ 도서 관리 프로그램 ]");
        System.out.println("1. 도서 등록");
        System.out.println("2. 도서 검색");
        System.out.println("3. 도서 수정");
        System.out.println("4. 도서 삭제");
        System.out.println("q. 프로그램 종료");

        String selectedMenu = selectMenu(); // 메서드1 호출 : selectedMenu는 변수 ! selectMenu()는 메서드
        // selected 로 리턴 받은 걸 사용해야한까 그냥 selectMenu() 가 아닌 String sele~~~

        switch (selectedMenu) {
            case "q":
                isRun = false;
                break;
            case "1": // 등록
                registerBook(); // 호출 !
                break;
            case "2": // 검색
                search();
                break;
            case "3": // 수정
                modify(); // modify() 메서드 호출
                break;
            case "4": // 삭제
                remove(); // remove 메서드 호출
                break;
            default:
                System.out.println("입력 오류");
        }
        System.out.println();

        return isRun;
    }


    // 메서드 3. 공백체크
    // String 으로 리턴됨
    private String validateValue(String title) {
        String value = null;
        while(true) {
            System.out.print(title + "명 입력 : ");
            value = scanner.nextLine();
            if (!value.isBlank()) { // blank : 띄어쓰기 포함_띄어쓰기도 공백으로 본다 (문자열객체에서 사용) , empty : 띄어쓰기 미포함
                break; // return value; 로 감
            }
            System.out.println(title + "명은 공백일 수 없습니다. 다시 입력하세요"); // 다시 while문으로 감
        }
        return value; // value = 내가 입력한 거
    }

    // 메서드 4. 중복체크
    private String duplicateBookName() {
        String bookName = null;
        while (true) { // validateValue("도서") = value 인데 그걸 bookName에 넣기
            bookName = validateValue("도서"); // validateValue() 메서드 호출! 내가 입력한 도서명(value)을 bookName에 넣기
            // 도서명 중복체크
            if (bookRepository.findBookByBookName(bookName) == null) {
                // blank : 띄어쓰기 미포함_띄어쓰기도 공백으로 본다 (문자열객체에서 사용) , empty : 띄어쓰기 포함
                break;
            }
            System.out.println("해당 도서명이 이미 존재합니다. 다시 입력하세요");
        }
        return bookName;
    }



    // 메서드 5. 도서 등록
    private void registerBook() {
        System.out.println("[ 도서 등록 ]");
        // 4개의 변수들에 값 넣기
        int bookId = bookRepository.autoIncrementBookId();
        String bookName = duplicateBookName(); // 중복안되게!
        String author = validateValue("저자"); // validateValue 에서 키보드로 입력받음
        String publisher = validateValue("출판사"); // validateValue 에서 키보드로 입력받음

        BookEntity book = new BookEntity(bookId, bookName, author, publisher); // book 객체 생성 ! > BookEntity클래스의 생성자 사용 ~
        bookRepository.saveBook(book); // 저장, 도서 등록 !!
        System.out.println("새로운 도서를 등록하였습니다.");
    }


    // 메서드 6. 도서검색
    private void search() {
        System.out.println("[ 도서 검색 ]");
        System.out.println("1. 통합검색");
        System.out.println("2. 도서명검색");
        System.out.println("3. 저자명검색");
        System.out.println("4. 출판사검색");
        System.out.print("옵션 선택 : ");
        int option = scanner.nextInt(); // int 정수만 옵션으로 입력
        scanner.nextLine(); // 공백 버퍼 날리기
        System.out.print("검색어 입력 : ");
        String searchText = scanner.nextLine();
        BookEntity[] searchBooks = bookRepository.searchBooks(option, searchText);

        System.out.println("[ 검색 결과 ]");

        if (searchBooks.length == 0){
            System.out.println("검색 결과가 없습니다.");
            return;
        }
        for (BookEntity book : searchBooks) {
            System.out.println(book.toString());
        }
    }

    // 메서드 7. 도서 삭제
    private void remove() {
        System.out.println("[ 도서 삭제 ]");
        search();
        System.out.print("삭제할 도서번호를 입력 :");
        int removeBookId = scanner.nextInt(); // removeBookId라는 변수를 생성해서 키보드로 입력받기
        scanner.nextLine();
        BookEntity book = bookRepository.findBookByBookId(removeBookId); // removeBookId 로 조회해서 book에 넣기
        if (book == null) {
            System.out.println("해당 도서번호는 존재하지 않습니다!");
            return;
        }
        bookRepository.deleteBookByBookId(removeBookId); // deleteBookByBookId 메서드 호출 !!
    }


    // 메서드
    private void modify() {
        System.out.println(" [ 도서 수정] ");
        search(); // 메서드 호출
        System.out.print("수정 할 번호 입력 : ");
        int modifyBookId = scanner.nextInt(); // modifyBookId 변수 생성
        scanner.nextLine();
        BookEntity book = bookRepository.findBookByBookId(modifyBookId); // bookRepository.findBookByBookId(modifyBookId); = findBook
        if (book == null) {
            System.out.println("해당 도서번호는 존재하지 않습니다!");
            return;
        }
        System.out.println("<< 도서 수정 정보 입력 >>");
        for(int i = 0; i < 3; i++) {
            String selected = null;

            switch (i) {
                case 0:
                    System.out.print("도서명을 수정하시겠습니까?(y/n)");
                    selected = scanner.nextLine();
                    if (selected.equalsIgnoreCase("y")) { // equalsIgnoreCase = 대소문자 구분 없이 !!
                        String bookName = duplicateBookName(); // value ( 내가 입력한 거.. ) ???, duplicateBookName() = bookName = value = 내가 입력한거
                        book.setBookName(bookName); // book = books[]의 인덱스 하나(주소)니까 주소 안에 값을 바꾸고있는거다 ??? 그래서 set사용..
                        break;
                    }
                    break;


                case 1:
                    System.out.print("저자명을 수정하시겠습니까?(y/n)");
                    selected = scanner.nextLine();
                    if (selected.equalsIgnoreCase("y")) { // equalsIgnoreCase = 대소문자 구분 없이 !!
                        String author = validateValue("저자");
                        book.setAuthor(author);
                        break;
                    }
                    break;


                case 2:
                    System.out.print("출판명을 수정하시겠습니까?(y/n)");
                    selected = scanner.nextLine();
                    if (selected.equalsIgnoreCase("y")) { // equalsIgnoreCase = 대소문자 구분 없이 !!
                        String publisher = validateValue("출판사");
                        book.setAuthor(publisher);
                        break;
                    }
                    break;
            }
        }
    }
}



