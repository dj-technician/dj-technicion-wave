package com.djtechnician.wavedomain.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Bms {

  public static final String HEADER_FIELD = "\\*---------------------- HEADER FIELD";
  public static final String MAIN_DATA_FIELD = "\\*---------------------- MAIN DATA FIELD";
  public static final String[] SOUND_EXTENSIONS = new String[] {"wav", "ogg"};

  private final BmsHeader bmsHeader;
  private final List<BmsData> bmsData;

  private Bms(String header, String data) throws IOException {
    this.bmsData = new ArrayList<>();
    this.bmsData.addAll(BmsHeader.preProcess(header));
    this.bmsHeader = BmsHeader.fromAll(header);
    this.bmsData.addAll(BmsData.of(data));
    sortData();
  }

  public static Bms parse(String header, String data) throws IOException {
    return new Bms(header, data);
  }

  public static boolean isLineDataType(String line) {
    return line.length() > 6 && line.charAt(6) == ':';
  }

  private void sortData() {
    this.bmsData.sort(
        Comparator.comparing(BmsData::bar, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(BmsData::position, Comparator.nullsFirst(Comparator.naturalOrder())));
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
