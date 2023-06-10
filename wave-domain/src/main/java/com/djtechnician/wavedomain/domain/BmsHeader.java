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
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@Getter
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BmsHeader {
  private String player;
  private String genre;
  private String title;
  private String artist;
  private double startBpm;
  private int playLevel;
  private int rank;
  private int total;
  private String stageFile;
  private String bmp01;
  private String[] wav;
  private Double[] bpm;
  private Integer[] stop;
  private Integer difficulty;
  private Integer random;

  private BmsHeader(String header, boolean parseWav) throws IOException {
    if (parseWav) {
      wav = new String[36 * 36];
      bpm = new Double[36 * 36];
      stop = new Integer[36 * 36];
    }
    process(header, parseWav);
  }

  public static BmsHeader fromInfo(String bms) throws IOException {
    return new BmsHeader(bms, false);
  }

  public static BmsHeader fromAll(String bms) throws IOException {
    return new BmsHeader(bms, true);
  }

  public static List<BmsData> preProcess(String header) throws IOException {
    BufferedReader randomReader = new BufferedReader(new StringReader(header));
    BufferedReader reader = new BufferedReader(new StringReader(header));
    String line;
    boolean isRead = false;
    List<BmsData> bmsData = new ArrayList<>();
    Integer random = -1;
    // set random
    while ((line = randomReader.readLine()) != null) {
      if (!line.startsWith("#")) {
        continue;
      }
      String[] tokens = Bms.isLineDataType(line) ? line.split(":") : line.split(" ");
      String key = tokens[0].toUpperCase();
      String value = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)).toUpperCase();
      if ("#RANDOM".equals(key)) {
        random = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(value) + 1);
        break;
      }
    }
    randomReader.close();
    while ((line = reader.readLine()) != null) {
      if (!line.startsWith("#")) {
        continue;
      }
      String[] tokens = Bms.isLineDataType(line) ? line.split(":") : line.split(" ");
      String key = tokens[0].toUpperCase();
      String value = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)).toUpperCase();
      isRead = processRandomAndIfState(key, value, isRead, bmsData, random);
    }
    reader.close();
    return bmsData;
  }

  private static boolean processRandomAndIfState(
      String key, String value, boolean isRead, List<BmsData> bmsData, Integer random) {
    switch (key) {
      case "#IF":
        if (Integer.parseInt(value) != random) {
          return false;
        }
        return true;
      case "#ELSE":
        if (isRead) {
          return false;
        }
        return true;
      case "#ENDIF":
        return false;
    }

    if (!isRead) {
      return false;
    }
    bmsData.addAll(BmsData.of(key, value));

    return true;
  }

  private void process(String header, boolean parseWav) throws IOException {
    BufferedReader reader = new BufferedReader(new StringReader(header));
    String line;
    while ((line = reader.readLine()) != null) {
      if (!line.startsWith("#")) {
        continue;
      }
      String[] tokens = Bms.isLineDataType(line) ? line.split(":") : line.split(" ");
      String key = tokens[0].toUpperCase();
      String value = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)).toUpperCase();
      setByKey(key, value, parseWav);
    }
  }

  private void setByKey(String key, String value, boolean parseWav) {
    switch (key) {
      case "#PLAYER":
        this.player = value;
        break;
      case "#GENRE":
        this.genre = value.toLowerCase(Locale.ROOT);
        break;
      case "#TITLE":
        this.title = value;
        break;
      case "#ARTIST":
        this.artist = value.toLowerCase(Locale.ROOT);
        break;
      case "#BPM":
        this.startBpm = Double.parseDouble(value);
        break;
      case "#PLAYLEVEL":
        this.playLevel = Integer.parseInt(value);
        break;
      case "#RANK":
        this.rank = Integer.parseInt(value);
        break;
      case "#TOTAL":
        this.total = Integer.parseInt(value);
        break;
      case "#STAGEFILE":
        this.stageFile = value;
        break;
      case "#BMP01":
        this.bmp01 = value;
        break;
      case "#DIFFICULTY":
        this.difficulty = Integer.parseInt(value);
        break;
      default:
        if (parseWav) {
          setWav(key, value);
          setBpm(key, value);
          setStop(key, value);
        }
    }
  }

  private void setWav(String key, String value) {
    if (!StringUtils.startsWithIgnoreCase(key, "#WAV")) {
      return;
    }

    int keyNo = WaveUtils.base36ToDecimal(key.substring(4));
    wav[keyNo] = value;
  }

  private void setBpm(String key, String value) {
    if (!StringUtils.startsWithIgnoreCase(key, "#BPM")) {
      return;
    }

    int keyNo = WaveUtils.base36ToDecimal(key.substring(4));
    bpm[keyNo] = Double.parseDouble(value);
  }

  private void setStop(String key, String value) {
    if (!StringUtils.startsWithIgnoreCase(key, "#STOP")) {
      return;
    }

    int keyNo = WaveUtils.base36ToDecimal(key.substring(5));
    stop[keyNo] = Integer.parseInt(value);
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
