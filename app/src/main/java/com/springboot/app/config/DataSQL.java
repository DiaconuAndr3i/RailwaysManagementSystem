package com.springboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DataSQL {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Bean
    public void insertData(){
        try(Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement()
        ) {
            //CUSTOMER
            String sql = "insert into customer values (1, 'email1@email1.com', 'first_name_1', 'last_name_1')";
            statement.executeUpdate(sql);
            sql = "insert into customer values (2, 'email2@email2.com', 'first_name_2', 'last_name_2')";
            statement.executeUpdate(sql);
            sql = "insert into customer values (3, 'email3@email3.com', 'first_name_3', 'last_name_3')";
            statement.executeUpdate(sql);

            //STATION
            sql = "insert into station values (1, 'Station1', 'City1')";
            statement.executeUpdate(sql);
            sql = "insert into station values (2, 'Station2', 'City2')";
            statement.executeUpdate(sql);
            sql = "insert into station values (3, 'Station3', 'City3')";
            statement.executeUpdate(sql);
            sql = "insert into station values (4, 'Station4', 'City4')";
            statement.executeUpdate(sql);
            sql = "insert into station values (5, 'Station5', 'City5')";
            statement.executeUpdate(sql);

            //ROUTE
            sql = "insert into route values (1, 'City1', 'City4')";
            statement.executeUpdate(sql);
            sql = "insert into route values (2, 'City3', 'City5')";
            statement.executeUpdate(sql);

            //ROUTE_STATION
            sql = "insert into route_station values (1, 1)";
            statement.executeUpdate(sql);
            sql = "insert into route_station values (1, 2)";
            statement.executeUpdate(sql);
            sql = "insert into route_station values (1, 3)";
            statement.executeUpdate(sql);
            sql = "insert into route_station values (1, 4)";
            statement.executeUpdate(sql);
            sql = "insert into route_station values (2, 3)";
            statement.executeUpdate(sql);
            sql = "insert into route_station values (2, 4)";
            statement.executeUpdate(sql);
            sql = "insert into route_station values (2, 5)";
            statement.executeUpdate(sql);

            //TRAIN
            sql = "insert into train values (1, 4, '2:30', 40, 185, 1, 1)";
            statement.executeUpdate(sql);
            sql = "insert into train values (2, 7, '1:50', 70, 230, 0, 1)";
            statement.executeUpdate(sql);
            sql = "insert into train values (3, 5, '2:00', 20, 270, 2, 2)";
            statement.executeUpdate(sql);

            //SCHEDULE
            sql = "insert into schedule values (1, '2023-01-25 15:40:00', 35, 100, '2023-01-25 13:10:00', 1)";
            statement.executeUpdate(sql);
            sql = "insert into schedule values (2, '2023-01-24 22:00:00', 50, 90, '2023-01-24 20:10:00', 2)";
            statement.executeUpdate(sql);
            sql = "insert into schedule values (3, '2023-01-29 07:20:00', 5, 200, '2023-01-29 05:20:00', 3)";
            statement.executeUpdate(sql);

            //BOOKING
            sql = "insert into booking values (1, 'C1', 55, 0, 1, 1)";
            statement.executeUpdate(sql);
            sql = "insert into booking values (2, 'C2', 71, 0, 1, 2)";
            statement.executeUpdate(sql);
            sql = "insert into booking values (3, 'C5', 220, 1, 2, 2)";
            statement.executeUpdate(sql);
            sql = "insert into booking values (4, 'C2', 19, 0, 2, 3)";
            statement.executeUpdate(sql);
            sql = "insert into booking values (5, 'C4', 119, 1, 3, 2)";
            statement.executeUpdate(sql);

            //DATA INSERTED
            System.out.println("Data inserted into tables!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
