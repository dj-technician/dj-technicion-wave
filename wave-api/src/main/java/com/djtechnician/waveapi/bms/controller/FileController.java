package com.djtechnician.waveapi.bms.controller;

import com.djtechnician.waveapi.bms.response.BmsHeaderResponse;
import com.djtechnician.wavecommon.response.WaveResponse;
import com.djtechnician.waveapi.bms.service.FileService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/bms")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @PostMapping("/sync")
  public WaveResponse<?> update() {
    fileService.syncWithDb();
    return WaveResponse.success();
  }

  @GetMapping("/list")
  public WaveResponse<List<BmsHeaderResponse>> listAll() {
    List<BmsHeaderResponse> response = fileService.getBmsListAll();
    log.info(response.toString());
    return WaveResponse.success(response);
  }

  @GetMapping("/{nodeId}")
  public WaveResponse<?> parseBms(@PathVariable @NotNull Long nodeId) {
    fileService.parseBms(nodeId);

    return WaveResponse.success();
  }
}
