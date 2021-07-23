package com.autot.log.service;

import org.springframework.stereotype.Service;

import com.autot.log.mapper.LogMapper;

import java.util.Map;
import java.util.List;

@Service
public class LogService {
    private LogMapper logMapper;

    public LogService(LogMapper logMapper){
        this.logMapper = logMapper;
    }

    public List<Map<String, Object>> getLogs(Map<String, Object> m) {
        return logMapper.selectLogs(m);
    }

    public int insetLog(Map<String, Object> m) {
        return logMapper.insetLog(m);
    }
}