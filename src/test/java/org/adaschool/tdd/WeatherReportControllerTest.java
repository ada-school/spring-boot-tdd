package org.adaschool.tdd;

import org.adaschool.tdd.controller.weather.dto.NearByWeatherReportsQueryDto;
import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Date;
import java.util.List;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class WeatherReportControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnWeatherReportById()
    {
        String url = "http://localhost:"+port+"/v1/weather";
        Assertions.assertNotNull(restTemplate.getForObject(url+"/123", WeatherReport.class));
    }

    @Test
    public void shouldReturnWeatherReportsOfReporterByName()
    {
        String reporter = "Eduard";
        String url = "http://localhost:"+port+"/v1/weather";
        Assertions.assertNotNull(restTemplate.getForObject(url+"/reporter/"+reporter, List.class));
    }

    @Test
    public void shouldCreateNewReport()
    {
        String url = "http://localhost:"+port+"/v1/weather";
        GeoLocation geoLocation = new GeoLocation(20,1);
        WeatherReportDto weatherDto = new WeatherReportDto(geoLocation, 10.2, 1.2,"Eduard", new Date());

        Assertions.assertNotNull(restTemplate.postForEntity(url, weatherDto, WeatherReport.class));
    }

    @Test
    public void shouldFindNearbyReports()
    {
        String url = "http://localhost:"+port+"/v1/weather";
        GeoLocation geoLocation = new GeoLocation(20,1);

        Assertions.assertNotNull(restTemplate.postForObject(url+"/nearby", new NearByWeatherReportsQueryDto(geoLocation, 0), List.class));
    }


}
