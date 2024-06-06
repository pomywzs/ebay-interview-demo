package com.ebay.interview.demo.service;

import com.ebay.interview.demo.domain.UserResourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.ebay.interview.demo.util.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import static com.ebay.interview.demo.constant.CommonConstant.USER_RESOURCE_SAVING;

@Slf4j
@Service
public class FileService {

    private final String filePath = System.getProperty("user.dir").concat(FileSystems.getDefault().getSeparator()).concat(USER_RESOURCE_SAVING);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    /**
     * 添加新用户
     * @param userResourceDTO 用户资源
     * @return 是否成功
     * @throws Exception
     */
    public boolean addUserToFile(UserResourceDTO userResourceDTO) throws Exception{
        File file = new File(filePath);
        if (file.exists()) {
            Map<Integer, UserResourceDTO> userResource = readUserResourceGroupById();
            userResource.compute(userResourceDTO.getUserId(), (key, value) -> Optional.ofNullable(value)
                    .map(UserResourceDTO::getEndpoint)
                    .map(ov -> {
                        ov.addAll(userResourceDTO.getEndpoint());
                        return value;
                    }).orElse(userResourceDTO));
            return writeOldFile(userResource, file);
        } else {
            return writeNewFile(userResourceDTO, file);
        }
    }

    /**
     * 读取用户按Id分组
     * @return 分组结果
     * @throws Exception
     */
    public Map<Integer, UserResourceDTO> readUserResourceGroupById() throws Exception {
        Map<Integer, UserResourceDTO> userResourceDTO = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))){
            readLock.lock();
            lines.forEach(line -> Optional.ofNullable(line).map(l -> JsonUtil.jsonStringToObject(l, UserResourceDTO.class))
                    .ifPresent(r -> userResourceDTO.put(r.getUserId(), r)));

        } catch (Exception e) {
            log.error("文件读取失败", e);
            throw e;
        } finally {
            readLock.unlock();
        }
        return userResourceDTO;
    }

    private boolean writeNewFile(UserResourceDTO userResourceDTO, File file)  {
        boolean writeSuccess = false;
        try (FileWriter writer = new FileWriter(file)) {
            writeLock.lock();
            String value = JsonUtil.objectToJsonString(userResourceDTO);
            if (StringUtils.hasText(value)) {
                writer.write(value);
            }
            writeSuccess = true;
        } catch (Exception e) {
            log.error("文件写入失败", e);
        } finally {
            writeLock.unlock();
        }
        return writeSuccess;
    }

    private boolean writeOldFile(Map<Integer, UserResourceDTO> userResMap, File file) {
        boolean writeSuccess = false;
        try (FileWriter writer = new FileWriter(file)) {
            writeLock.lock();
            for (Map.Entry<Integer, UserResourceDTO> entry : userResMap.entrySet()) {
                String resource;
                if (entry != null && entry.getKey() != null && entry.getValue() != null
                        && (resource = JsonUtil.objectToJsonString(entry.getValue())) != null) {
                    writer.write(resource);
                    writer.write(System.lineSeparator());
                }

            }
            writeSuccess = true;
        } catch (Exception e) {
            log.error("文件追加失败", e);
        } finally {
            writeLock.unlock();
        }
        return writeSuccess;
    }


}
