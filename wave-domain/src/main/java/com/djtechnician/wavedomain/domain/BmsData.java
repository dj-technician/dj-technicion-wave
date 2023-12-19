package com.djtechnician.wavedomain.domain;

import com.djtechnician.wavecommon.utils.WaveUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BmsData {
  private Integer bar;
  private BmsChannel bmsChannel;
  private Double position;
  private Double value;

  public static List<BmsData> of(String data) throws IOException {
    BufferedReader reader = new BufferedReader(new StringReader(data));
    String line;
    List<BmsData> bmsData = new ArrayList<>();
    while ((line = reader.readLine()) != null) {
      if (!line.startsWith("#")) {
        continue;
      }
      String[] tokens = Bms.isLineDataType(line) ? line.split(":") : line.split(" ");
      String key = tokens[0].toUpperCase();
      String value = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)).toUpperCase();
      bmsData.addAll(of(key, value));
    }
    return bmsData;
  }

  public static List<BmsData> of(String parseKey, String parseValue) {
    int bar = Integer.parseInt(parseKey.substring(1, 4));
    BmsChannel bmsChannel = BmsChannel.from(parseKey.substring(4));

    if (BmsChannel.BAR_SHORTEN == bmsChannel) {
      Double value = Double.parseDouble(parseValue);
      return List.of(new BmsData(bar, bmsChannel, null, value));
    }

    if (parseValue.length() % 2 != 0) {
      parseValue = parseValue.substring(0, parseValue.length() - 1);
    }

    int total = parseValue.length() / 2;
    String finalParseValue = parseValue;
    return IntStream.range(0, total)
        .mapToObj(
            i -> {
              Integer value;
              if (BmsChannel.BPM == bmsChannel) { // bpm 채널은 16진수
                value = WaveUtils.base16ToDecimal(finalParseValue.substring(i * 2, (i + 1) * 2));
              } else {
                value = WaveUtils.base36ToDecimal(finalParseValue.substring(i * 2, (i + 1) * 2));
              }
              if (value == 0) {
                return null;
              }
              return new BmsData(bar, bmsChannel, i / (double) total, Double.valueOf(value));
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
