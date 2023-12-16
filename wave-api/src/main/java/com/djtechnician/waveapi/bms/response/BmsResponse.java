package com.djtechnician.waveapi.bms.response;

import com.djtechnician.wavedomain.domain.Bms;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.google.gson.Gson;
import lombok.Builder;

@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BmsResponse {
  private boolean isParsed;
  private Bms bms;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
