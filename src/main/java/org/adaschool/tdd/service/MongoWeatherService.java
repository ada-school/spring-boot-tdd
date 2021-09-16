package org.adaschool.tdd.service;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.exception.WeatherReportNotFoundException;
import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.util.WeatherReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MongoWeatherService
    implements WeatherService
{

    private final WeatherReportRepository repository;

    public MongoWeatherService( @Autowired WeatherReportRepository repository )
    {
        this.repository = repository;
    }

    @Override
    public WeatherReport report( WeatherReportDto weatherReportDto )
    {
        return repository.save(WeatherReportMapper.mapToWeatherReport(weatherReportDto));
    }

    @Override
    public WeatherReport findById( String id ) {
        return repository.findById(id)
                .orElseThrow(WeatherReportNotFoundException::new);
    }

    @Override
    public List<WeatherReport> findNearLocation( GeoLocation geoLocation, float distanceRangeInMeters )
    {
        return repository.findAll()
                .stream()
                .filter(weather -> Math.sqrt(Math.pow((geoLocation.getLat()-weather.getGeoLocation().getLat()),2)+Math.pow((geoLocation.getLng()-weather.getGeoLocation().getLng()),2)) <= distanceRangeInMeters)
                .collect(Collectors.toList());
    }

    @Override
    public List<WeatherReport> findWeatherReportsByName( String reporter )
    {
        return repository.findByReporter(reporter);
    }
}
