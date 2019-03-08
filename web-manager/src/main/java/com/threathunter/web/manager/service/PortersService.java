package com.threathunter.web.manager.service;


import com.threathunter.web.common.config.ConfigUtil;
import com.threathunter.web.manager.mysql.domain.Porter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Service
@Slf4j
public class PortersService {

    private static String dataDir = ConfigUtil.getString("platform.porters.data.dir", "/data/porters/upload");

    static {
        makeSure(dataDir);
    }

    @Autowired
    MysqlService service;

    private static void makeSure(String dataDir) {
        if(!dataDir.endsWith("/"))
            dataDir += "/";
        File file = new File(dataDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public File createFile(String name) {
        String path = String.format("%s/%s", dataDir, name);
        makeSure(dataDir);
        File file = new File(path);
        return file;
    }

    public int save(Porter porter) {
        return service.insertPorter(porter);
    }

    public List<Porter> getAll() {
        return service.queryPorters();
    }

    public void deletePorter(int id) {
        service.deletePorter(id);
    }

    public void deletePorter(Porter porter) {
        service.deletePorter(porter.getId());
    }
}
