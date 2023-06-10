package com.djtechnician.waveapi.bms.service;

import static com.djtechnician.wavedomain.domain.Bms.HEADER_FIELD;
import static com.djtechnician.wavedomain.domain.Bms.MAIN_DATA_FIELD;

import com.djtechnician.wavedomain.domain.Bms;
import com.djtechnician.wavedomain.domain.BmsHeader;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BmsService {

  public BmsHeader parseHeaderInfo(@NotBlank String bms) {
    try {
      return BmsHeader.fromInfo(getHeaderSection(bms));
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("unable to parse: ", bms.substring(0, 50));
      throw new RuntimeException();
    }
  }

  public Bms parse(@NotBlank String bms) {
    try {
      String headerSection = getHeaderSection(bms);
      String dataSection = getDataSection(bms);
      return Bms.parse(headerSection, dataSection);
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("unable to parse: ", bms.substring(0, 50));
      throw new RuntimeException();
    }
  }

  public String getHeaderSection(String bms) {
    return bms.split(MAIN_DATA_FIELD)[0].split(HEADER_FIELD)[1];
  }

  public String getDataSection(String bms) {
    return bms.split(MAIN_DATA_FIELD)[1];
  }
}
