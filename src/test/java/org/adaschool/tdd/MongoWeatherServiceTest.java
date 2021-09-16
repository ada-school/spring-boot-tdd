package org.adaschool.tdd;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.exception.WeatherReportNotFoundException;
import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.service.MongoWeatherService;
import org.adaschool.tdd.service.WeatherService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class MongoWeatherServiceTest
{
    WeatherService weatherService;

    @Mock
    WeatherReportRepository repository;

    @BeforeEach
    public void setup()
    {
        weatherService = new MongoWeatherService( repository );
    }

    @Test
    void createWeatherReportCallsSaveOnRepository()
    {
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation( lat, lng );
        WeatherReportDto weatherReportDto = new WeatherReportDto( location, 35f, 22f, "tester", new Date() );
        weatherService.report( weatherReportDto );
        verify( repository ).save( any( WeatherReport.class ) );
    }

    @Test
    void weatherReportIdFoundTest()
    {
        String weatherReportId = "awae-asd45-1dsad";
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation( lat, lng );
        WeatherReport weatherReport = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        when( repository.findById( weatherReportId ) ).thenReturn( Optional.of( weatherReport ) );
        WeatherReport foundWeatherReport = weatherService.findById( weatherReportId );
        Assertions.assertEquals( weatherReport, foundWeatherReport );
    }

    @Test
    void weatherReportIdNotFoundTest()
    {
        String weatherReportId = "dsawe1fasdasdoooq123";
        when( repository.findById( weatherReportId ) ).thenReturn( Optional.empty() );
        Assertions.assertThrows( WeatherReportNotFoundException.class, () -> weatherService.findById( weatherReportId ));
    }

    @Test
    void shouldGetAllReportsOfReporter()
    {
        String reporter = "Eduard";
        List<WeatherReport> reports = new ArrayList<>();
        reports.add(new WeatherReport(new GeoLocation(10.0, 20.0), 10, 2.3, reporter, new Date()));
        reports.add(new WeatherReport(new GeoLocation(10.0, 40.0), 30, 0.2, reporter, new Date()));

        when(repository.findByReporter(reporter)).thenReturn(reports);
        Assertions.assertEquals(reports, weatherService.findWeatherReportsByName(reporter));

    }

    @Test
    void shouldIgnoreNullReporter()
    {
        when(repository.findByReporter(nullable(String.class))).thenReturn(new ArrayList<>());
        List<?> reports = weatherService.findWeatherReportsByName(null);
        Assertions.assertNotNull(reports);
        Assertions.assertFalse(reports.size() > 0);

        verify(repository).findByReporter(nullable(String.class));
    }

    @Test
    void shouldGetNearbyGeolocations()
    {
        GeoLocation geoLocation = new GeoLocation(20.1, 4.555);
        List<WeatherReport> reports = new ArrayList<>();
        reports.add(new WeatherReport(geoLocation, 10.22, 4.09, "This guy", new Date()));
        reports.add(new WeatherReport(geoLocation, 32.22, 9.0, "This guy", new Date()));
        reports.add(new WeatherReport(new GeoLocation(10.1, 0), 2,2,"222", new Date()));

        List<WeatherReport> reportClone = new ArrayList<>();
        reportClone.addAll(reports);

        when(repository.findAll()).thenReturn(reportClone);
        reports.remove(2);
        Assertions.assertEquals(reports, weatherService.findNearLocation(geoLocation, 0));
    }

}
