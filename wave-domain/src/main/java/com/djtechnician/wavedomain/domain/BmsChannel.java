package com.djtechnician.wavedomain.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum BmsChannel {
  BACKGROUND("01", "배경음 채널"),
  BAR_SHORTEN("02", "마디 단축 채널"),
  BPM("03", "BPM 채널"),
  BGA("04", "BGA 채널"),
  BM98("05", "BM98 확장 채널"),
  POOR_BGA("06", "Poor BGA 채널"),
  BGA_LAYER("07", "BGA 레이어 채널"),
  BPM_EXTENDED("08", "확장 BPM 채널"),
  SEQUENCE_STOP("09", "시퀀스 정지 채널"),
  PLAYER1_1("11", "PLAYER 1, 1번 건반"),
  PLAYER1_2("12", "PLAYER 1, 2번 건반"),
  PLAYER1_3("13", "PLAYER 1, 3번 건반"),
  PLAYER1_4("14", "PLAYER 1, 4번 건반"),
  PLAYER1_5("15", "PLAYER 1, 5번 건반"),
  PLAYER1_6("16", "PLAYER 1, 턴테이블"),
  PLAYER1_7("17", "PLAYER 1, 패달"),
  PLAYER1_8("18", "PLAYER 1, 6번 건반"),
  PLAYER1_9("19", "PLAYER 1, 7번 건반"),
  PLAYER2_1("21", "PLAYER 2, 1번 건반"),
  PLAYER2_2("22", "PLAYER 2, 2번 건반"),
  PLAYER2_3("23", "PLAYER 2, 3번 건반"),
  PLAYER2_4("24", "PLAYER 2, 4번 건반"),
  PLAYER2_5("25", "PLAYER 2, 5번 건반"),
  PLAYER2_6("26", "PLAYER 2, 턴테이블"),
  PLAYER2_7("27", "PLAYER 2, 패달"),
  PLAYER2_8("28", "PLAYER 2, 6번 건반"),
  PLAYER2_9("29", "PLAYER 2, 7번 건반"),

// todo 투명오브젝트, 롱노트 처리
;
  private final String value;
  private final String description;

  public static BmsChannel from(String value) {
    return Arrays.stream(BmsChannel.values())
        .filter(bmsChannel -> bmsChannel.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("no available channel : " + value));
  }

  public boolean isEventChannel() {
    return !this.name().startsWith("PLAYER");
  }
}
