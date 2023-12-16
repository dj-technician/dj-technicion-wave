package com.djtechnician.waveapi.bms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"dev", "real"})
@RequiredArgsConstructor
public class S3FileDownloadService implements FileDownloadService {

  @Override
  public Resource loadFileAsResource(String filePath) {
    // todo - s3 스토리지 사용은 aws 배포 시점에.
    return null;
  }
}
