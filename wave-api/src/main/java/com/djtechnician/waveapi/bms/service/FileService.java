package com.djtechnician.waveapi.bms.service;

import com.djtechnician.waveapi.bms.response.BmsHeaderResponse;
import com.djtechnician.waveapi.bms.response.BmsResponse;
import com.djtechnician.wavedomain.domain.Bms;
import com.djtechnician.wavedomain.domain.BmsHeader;
import com.djtechnician.wavedomain.entity.BmsNode;
import com.djtechnician.wavedomain.repository.BmsNodeRepository;
import io.micrometer.common.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  private final BmsService bmsService;
  private final BmsNodeRepository bmsNodeRepository;

  public List<BmsHeaderResponse> getBmsListAll() {
    List<BmsNode> bmsNodes = bmsNodeRepository.findAll();
    return bmsNodes.stream()
        .map(
            bmsNode -> {
              try {
                String bms = readBms(bmsNode);
                return BmsHeaderResponse.builder()
                    .id(bmsNode.id())
                    .bmsHeader(bmsService.parseHeaderInfo(bms))
                    .build();
              } catch (Exception e) {
                e.printStackTrace();
              }
              return null;
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public String readBms(BmsNode bmsNode) {
    try {
      File file = new File(bmsNode.fullPath());
      if (!file.exists() || file.isDirectory()) {
        throw new RuntimeException("this bms file does not exists! : " + file.getAbsolutePath());
      }
      return read(file);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("fail to read bms");
    }
  }

  private String read(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    StringBuilder sb = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    return sb.toString();
  }

  public List<BmsNode> findAllBmsInDirectory() throws IOException {
    log.info(System.getProperty("user.dir"));
    File root = new File("../../bms");
    List<BmsNode> bmsNodes = new ArrayList<>();
    treeTraversal(root, bmsNodes);
    return bmsNodes;
  }

  private void treeTraversal(File file, List<BmsNode> bmsNodes) throws IOException {
    if (file.isDirectory()) {
      File[] subFiles = file.listFiles();
      assert subFiles != null;
      for (File subFile : subFiles) {
        treeTraversal(subFile, bmsNodes);
      }
      return;
    }

    String fileName = file.getName();
    if (fileName.endsWith(".bms") || fileName.endsWith(".bme")) {
      BmsNode bmsNode = BmsNode.of(fileName, file.getCanonicalFile().getParent());
      bmsNodes.add(bmsNode);
    }
  }

  public void syncWithDb() {
    try {
      List<BmsNode> bmsNodes = findAllBmsInDirectory();
      bmsNodeRepository.deleteAll();
      bmsNodeRepository.saveAllAndFlush(bmsNodes);
    } catch (Exception e) {
      throw new RuntimeException("fail to sync : " + e.getMessage());
    }
  }

  public String getBms01Path(Long nodeId) {
    try {
      BmsNode bmsNode = findBmsByNodeId(nodeId);

      String bms = read(new File(bmsNode.fullPath()));
      BmsHeader bmsHeader = bmsService.parseHeaderInfo(bms);

      if (StringUtils.isBlank(bmsHeader.bmp01())) {
        return "/Users/leo/Workspace/project/dj-technician/resource/sample.jpeg";
      }

      return bmsNode.rootPath() + "/" + bmsService.parseHeaderInfo(bms).bmp01();
    } catch (IOException e) {
      throw new RuntimeException("bms 파일을 읽을 수 없습니다. : " + nodeId);
    }
  }

  public BmsResponse parseBms(Long nodeId) {
    try {
      BmsNode bmsNode = findBmsByNodeId(nodeId);

      String bmsStr = readBms(bmsNode);
      Bms bms = bmsService.parse(bmsStr);
      return BmsResponse.builder().isParsed(true).bms(bms).build();
    } catch (Exception e) {
      return BmsResponse.builder().isParsed(false).build();
    }
  }

  public String getAvailableSoundPath(Long nodeId, String fileName) {
    BmsNode bmsNode = findBmsByNodeId(nodeId);
    Path soundPath = Paths.get(bmsNode.rootPath(), fileName);
    boolean found = Files.exists(soundPath);
    if (found) {
      return soundPath.toString();
    }

    for (String extension : Bms.SOUND_EXTENSIONS) {
      int extDotIndex = fileName.lastIndexOf(".");
      String anotherName = fileName.substring(0, extDotIndex) + "." + extension;
      soundPath = Paths.get(bmsNode.rootPath(), anotherName);
      found = Files.exists(soundPath);
      if (found) {
        return soundPath.toString();
      }
    }

    throw new RuntimeException("해당 fileName 의 사운드 파일을 찾을 수 없습니다. : " + fileName);
  }

  private BmsNode findBmsByNodeId(Long nodeId) {
    return bmsNodeRepository
        .findById(nodeId)
        .orElseThrow(() -> new RuntimeException("nodeId 에 해당하는 bms 를 찾을 수 없습니다. : " + nodeId));
  }
}
