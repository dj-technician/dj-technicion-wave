package com.djtechnician.wavedomain.domain;

import static com.djtechnician.wavedomain.domain.Bms.HEADER_FIELD;
import static com.djtechnician.wavedomain.domain.Bms.MAIN_DATA_FIELD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class BmsTest {

  @Test
  @DisplayName("bms header 파싱 성공")
  void parseHeader() throws IOException {
    String bms = readTestBms();

    BmsHeader header = BmsHeader.fromAll(bms);
    log.info("header : {}", Arrays.toString(header.wav()));
  }

  @Test
  @DisplayName("bms 전처리 파싱 성공")
  void parsePreProcess() throws IOException {
    String bms = readTestBms();

    List<BmsData> bmsDataList = BmsHeader.preProcess(bms);
    log.info("bmsDataList : {}", bmsDataList);
  }

  @Test
  @DisplayName("bms All 파싱 성공")
  void parseAll() throws IOException {
    String bmsStr = readTestBms();
    Bms bms = Bms.parse(getHeaderSection(bmsStr), getDataSection(bmsStr));
    log.info("bms : {}", bms);
  }

  private String getHeaderSection(String bms) {
    return bms.split(MAIN_DATA_FIELD)[0].split(HEADER_FIELD)[1];
  }

  private String getDataSection(String bms) {
    return bms.split(MAIN_DATA_FIELD)[1];
  }

  private String readTestBms() {
    try {
      File file =
          new File(
              "/Users/leo/Workspace/project/dj-technician/bms/[clover]LeaF_Aleph0/_7ANOTHER.bms");
      if (!file.exists() || file.isDirectory()) {
        throw new RuntimeException("this bms file does not exists! : " + file.getAbsolutePath());
      }
      return read(file);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("fail to read bms");
    }
  }

  private String read(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    StringBuilder sb = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    return sb.toString();
  }
}
