package com.djtechnician.wavedomain.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Bms {

  public static final String HEADER_FIELD = "\\*---------------------- HEADER FIELD";
  public static final String MAIN_DATA_FIELD = "\\*---------------------- MAIN DATA FIELD";

  private BmsHeader bmsHeader;
  private List<BmsData> bmsData;

  private Bms(String header, String data) throws IOException {
    this.bmsData = new ArrayList<>();
    this.bmsData.addAll(BmsHeader.preProcess(header));
    this.bmsHeader = BmsHeader.fromAll(header);
    this.bmsData.addAll(BmsData.of(data));
  }

  public static Bms parse(String header, String data) throws IOException {
    return new Bms(header, data);
  }

  public static boolean isLineDataType(String line) {
    return line.length() > 6 && line.charAt(6) == ':';
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
