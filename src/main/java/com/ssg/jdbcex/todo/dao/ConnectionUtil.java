package com.ssg.jdbcex.todo.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

// [1] DB 연결을 위한 HikariCP 커넥션 풀 설정 클래스
public enum ConnectionUtil {

    INSTANCE;

    private HikariDataSource ds;

    // [2] 생성자에서 DB 설정
    ConnectionUtil()  {
        HikariConfig config = new HikariConfig();

        // [3] MySQL 드라이버, URL, 계정 설정
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/ssgdb?serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("admindb");

        // [4] 성능 최적화용 설정
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    // [5] 커넥션 요청 메서드 (DAO에서 호출되는 메서드)
    public Connection getConnection()throws Exception {
        return ds.getConnection();
    }

}
