package org.adaschool.tdd.util;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.repository.document.WeatherReport;

public class WeatherReportMapper {

    public static WeatherReport mapToWeatherReport(WeatherReportDto weatherReportDto) {
        return new WeatherReport(
                weatherReportDto.getGeoLocation(),
                weatherReportDto.getTemperature(),
                weatherReportDto.getHumidity(),
                weatherReportDto.getReporter(),
                weatherReportDto.getCreatedAt()
        );
    }

    public static WeatherReportDto mapToWeatherReportDto(WeatherReport weatherReport) {
        return new WeatherReportDto(
                weatherReport.getGeoLocation(),
                weatherReport.getTemperature(),
                weatherReport.getHumidity(),
                weatherReport.getReporter(),
                weatherReport.getCreatedAt()
        );
    }


}
