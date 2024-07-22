package com.study.dvd.dao;

import com.study.dvd.db.DBConnectionMgr;
import com.study.dvd.entity.DVD;
import com.study.dvd.entity.Producer;
import com.study.dvd.entity.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DVDDao {
    public static int addDvd(DVD dvd) {
        DBConnectionMgr pool = DBConnectionMgr.getInstance();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int successCount = 0;

        try {
            connection = pool.getConnection(); // 데이터베이스 연결
            String sql = "insert into dvd_tb values(0, ?, ?, ?, ?, ?, now())";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dvd.getRegistrationNumber());
            preparedStatement.setString(2, dvd.getTitle());
            preparedStatement.setInt(3, dvd.getProducer().getProducerId());
            preparedStatement.setInt(4, dvd.getPublisher().getPublisherId());
            preparedStatement.setInt(5, dvd.getPublicationYear());
            successCount = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(connection, preparedStatement);
        }

        return successCount;
    }



    public static int addProducer(Producer producer) {
        DBConnectionMgr pool = DBConnectionMgr.getInstance();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int successCount = 0;

        try {
            connection = pool.getConnection(); // 데이터베이스 연결
            String sql = "insert into producer_tb values(0, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // 미완성된 쿼리 실행시킬 준비, 방금 입력 된(insert) 키 값 RETURN_GENERATED_KEYS 는 첫번째 키 값을 가져오기 위해 사용 (=preparedStatement.getGeneratedKeys()을 하기 위해)
            preparedStatement.setString(1, producer.getProducerName()); // 첫번째 ? 자리에다가 producename 데이터 채우기
            successCount = preparedStatement.executeUpdate();  // 쿼리 실행 > 실행값 만큼의 숫자??????
            generatedKeys = preparedStatement.getGeneratedKeys(); // autoincrement 된 키 값 바로 가져오는 용도 > 표의 행 > ( = PreparedStatement.RETURN_GENERATED_KEYS 랑 세트 ? )
            generatedKeys.next(); // generatedKeys(Resultset은 맨 처음 호출하면 맨 위에 아무것도 없는 부분을 가르키고 있음 그러니까 next !!!) >> 첫번째 줄 키 값
            producer.setProducerId(generatedKeys.getInt(1)); // 첫번째 줄 키 값 중 1번 열에 있는 int ! >> 방금 만든 producerId값
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(connection, preparedStatement, generatedKeys); // 데이터베이스 연결 해지 ?
        }

        return successCount;
    }


    public static int addPublisher(Publisher publisher) {
        DBConnectionMgr pool = DBConnectionMgr.getInstance();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int successCount = 0;

        try {
            connection = pool.getConnection(); // 데이터베이스 연결
            String sql = "insert into publisher_tb values(0, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // 미완성된 쿼리 실행시킬 준비
            preparedStatement.setString(1, publisher.getPublisherName()); // ? 자리에 데이터 채우기
            successCount = preparedStatement.executeUpdate();  // 쿼리 실행
            generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next(); // 다음 키 값
            publisher.setPublisherId(generatedKeys.getInt(1));
        } catch (Exception e) {

        } finally {
            pool.freeConnection(connection, preparedStatement, generatedKeys); // 데이터베이스 연결 해지 ?
        }

        return successCount;
    }

    public static List<DVD> findAll(int count) {
        DBConnectionMgr pool = DBConnectionMgr.getInstance();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<DVD> dvdList = new ArrayList<>();

        try {
            connection = pool.getConnection();
            String sql = "select * from dvd_view limit 0, ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, count); // 첫번째 ? 에 count 갯수 넣기
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) { // 다음 행이 있으면 true
                Producer producer = Producer.builder()
                        .producerId(resultSet.getInt(4))
                        .producerName(resultSet.getString(5))
                        .build();
                Publisher publisher = Publisher.builder()
                        .publisherId(resultSet.getInt(6))
                        .publisherName(resultSet.getString(7))
                        .build();
                DVD dvd = DVD.builder()
                        .dvdId(resultSet.getInt(1))
                        .registrationNumber(resultSet.getString(2))
                        .title(resultSet.getString(3))
                        .producerId(producer.getProducerId())
                        .publisherId(publisher.getPublisherId())
                        .producer(producer)
                        .publisher(publisher)
                        .publicationYear(resultSet.getInt(8))
                        .databaseDate(resultSet.getDate(9).toLocalDate()).build();

                dvdList.add(dvd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(connection);
        }

        return dvdList;
    }

}
