package com.djtechnician.waveapi.bms.controller;

import com.djtechnician.waveapi.bms.service.FileDownloadService;
import com.djtechnician.waveapi.bms.service.FileService;
import com.djtechnician.wavecommon.response.WaveResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@CrossOrigin
@RequestMapping("/download/bms")
@RequiredArgsConstructor
public class FileDownloadController {

  private final FileService fileService;
  private final FileDownloadService fileDownloadService;

  @GetMapping("/bmp01/{nodeId}")
  public WaveResponse<Resource> downloadBmp01(@PathVariable Long nodeId) {
    String path = fileService.getBms01Path(nodeId);
    Resource resource = fileDownloadService.loadFileAsResource(path);
    String extension = Objects.requireNonNull(resource.getFilename()).split("\\.")[1];
    return WaveResponse.download(resource, nodeId + "." + extension);
  }

  @GetMapping("/sound/{nodeId}/{fileName}")
  public WaveResponse<Resource> downloadWav(
      @PathVariable Long nodeId, @PathVariable String fileName) {
    String path = fileService.getAvailableSoundPath(nodeId, fileName);
    Resource resource = fileDownloadService.loadFileAsResource(path);
    return WaveResponse.download(resource, resource.getFilename());
  }
}
