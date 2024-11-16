package com.example.batch.demo.meteorology.runner;

import com.example.batch.demo.meteorology.model.MeteorologyCsvModel;
import com.example.batch.demo.mybatis.entity.Meteorology;
import com.example.batch.demo.mybatis.entity.MeteorologyExample;
import com.example.batch.demo.mybatis.mapper.MeteorologyMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "batch.execute", name = "target",
    havingValue = "MeteorologyDataImportRunner")
@RequiredArgsConstructor
public class MeteorologyDataImportRunner
    implements CommandLineRunner, ExitCodeGenerator, ExitCodeExceptionMapper {

  private int exitCode = 0;
  private final MeteorologyMapper meteorologyMapper;

  @Override
  public void run(String... args) throws Exception {
    Resource resource = new ClassPathResource("sample-data.csv");
    log.info("気象データのインポート処理を実行します");

    CsvMapper csvMapper = new CsvMapper();
    csvMapper.findAndRegisterModules();
    CsvSchema csvSchema =
        csvMapper.schemaFor(MeteorologyCsvModel.class).withHeader().withLineSeparator("\r\n");

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(Files.newInputStream(Paths.get(resource.getURI())), "Shift_JIS"))) {
      List<MeteorologyCsvModel> list = csvMapper.readerFor(MeteorologyCsvModel.class)
          .with(csvSchema).<MeteorologyCsvModel>readValues(br).readAll();
      list.forEach(e -> log.info("data: {}", ToStringBuilder.reflectionToString(e)));

      insertOrUpdate(list);
    }

    log.info("気象データのインポート処理を終了します");
  }

  public void insertOrUpdate(List<MeteorologyCsvModel> list) {
    list.forEach(e -> {
      Meteorology meteorology = this.getByDate(e.getDate());
      if (meteorology == null)
        meteorology = new Meteorology();
      LocalDateTime now = LocalDateTime.now();
      meteorology.setMeasureDate(e.getDate());
      meteorology.setPrefecture(e.getPrefecture());
      meteorology.setHeighestTemperature(e.getHeighestTemperature());
      meteorology.setLowestTemperature(e.getLowestTemperature());
      meteorology.setAverageTemperature(e.getAverageTemperature());
      meteorology.setUpdatedAt(now);
      meteorology.setDeleteFlag(false);

      if (meteorology.getId() == null) {
        meteorology.setCreatedAt(now);
        meteorologyMapper.insert(meteorology);
      } else {
        meteorologyMapper.updateByPrimaryKey(meteorology);
      }
    });
  }

  public Meteorology getByDate(LocalDate date) {
    MeteorologyExample example = new MeteorologyExample();
    example.createCriteria().andMeasureDateEqualTo(date);
    return meteorologyMapper.selectByExample(example).stream().findFirst().orElse(null);
  }

  @Override
  public int getExitCode() {
    return this.exitCode;
  }

  @Override
  public int getExitCode(Throwable exception) {
    return 1;
  }
}
