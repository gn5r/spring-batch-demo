package com.example.batch.demo.meteorology.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "都道府県", "年月日", "最高気温(℃)", "最低気温(℃)", "平均気温(℃)" })
public class MeteorologyCsvModel {

  @JsonProperty("都道府県")
  private String prefecture;

  @JsonProperty("年月日")
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate date;

  @JsonProperty("最高気温(℃)")
  private Float heighestTemperature;

  @JsonProperty("最低気温(℃)")
  private Float lowestTemperature;

  @JsonProperty("平均気温(℃)")
  private Float averageTemperature;
}
