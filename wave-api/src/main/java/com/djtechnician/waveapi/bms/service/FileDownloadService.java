package com.djtechnician.waveapi.bms.service;

import org.springframework.core.io.Resource;

public interface FileDownloadService {
  Resource loadFileAsResource(String filePath);
}
