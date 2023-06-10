package com.djtechnician.waveapi.bms.response;

import com.djtechnician.wavedomain.domain.BmsHeader;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.google.gson.Gson;
import lombok.Builder;

@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BmsHeaderResponse {
  private Long id;
  private BmsHeader bmsHeader;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
