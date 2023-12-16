package com.djtechnician.wavecommon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@JsonInclude(Include.NON_NULL)
public class WaveResponse<T> extends ResponseEntity<T> {
  private String status;
  private String message;
  private String data;

  public WaveResponse(HttpStatusCode status) {
    super(status);
  }

  public WaveResponse(T body, HttpStatusCode status) {
    super(body, status);
  }

  public WaveResponse(MultiValueMap<String, String> headers, HttpStatusCode status) {
    super(headers, status);
  }

  public WaveResponse(T body, MultiValueMap<String, String> headers, HttpStatusCode status) {
    super(body, headers, status);
  }

  public WaveResponse(T body, MultiValueMap<String, String> headers, int rawStatus) {
    super(body, headers, rawStatus);
  }

  public static WaveResponse<Void> success() {
    return new WaveResponse<>(HttpStatus.OK);
  }

  public static <T> WaveResponse<T> success(T body) {
    return new WaveResponse<>(body, HttpStatus.OK);
  }

  public static WaveResponse<Resource> download(Resource resource, String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    return new WaveResponse<>(resource, headers, HttpStatus.OK);
  }
}
