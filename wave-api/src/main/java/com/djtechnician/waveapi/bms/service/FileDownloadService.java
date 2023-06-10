package com.djtechnician.waveapi.bms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileDownloadService {

  private final ResourceLoader resourceLoader;

  public Resource loadFileAsResource(String filePath) {
    return resourceLoader.getResource("file:" + filePath);
  }
}
