package com.example.demo;

import com.example.demo.repository.ModbusRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class Scheduler {

    private static int orgId = 1;
    private final ModbusRepository repository;
    public Scheduler(ModbusRepository repository) {this.repository=repository;}

    @Scheduled(cron = "0 2,12,22,32,42,52 * * * *")
    public void energyTenInfoSave() {
        log.info("aggre011 메서드 시작부분");

        try {
            List<Map<String, Object>> list = repository.GetEnergyTenInfo(orgId);
            log.info("aggre011 메서드 조회리스트 : "+list);
            Map<String, Object> insertMap = new HashMap<>();
            for(Map<String, Object> map : list){
                log.info("aggre011 메서드 리스트 속 맵 : "+map);
                insertMap.put("pumpEventTm",map.get("pumpEventTm"));
                insertMap.put("slaveId",map.get("slaveId"));
                insertMap.put("seq",map.get("seq"));
                insertMap.put("orgId",map.get("orgId"));
                insertMap.put("rValue",map.get("rValue"));
                insertMap.put("sValue",map.get("sValue"));
                insertMap.put("tValue",map.get("tValue"));
                insertMap.put("lValue",map.get("lValue"));
                try {
                    repository.insertAggre011(insertMap);
                    log.info("맵데이터:"+map+" 저장완료");
                }catch (Exception e){
                    log.warn("저장실패 : "+e);
                }
            }
        }catch (Exception e){
            log.warn("에러발생 : "+e);
        }


    }

    @Scheduled(cron = "0 5 7 * * *")
    public void energyDayInfoDelSave() {
        log.info("전날데이터 재저장 메서드 시작부분");
        List<Map<String, Object>> list = repository.GetEnergyDayInfo(orgId);
        log.info("전날데이터 재저장 메서드 조회리스트 : "+list);
        repository.DeleteAggre011();
        log.info("전날데이터 삭제완료");
        Map<String, Object> insertMap = new HashMap<>();
        for(Map<String, Object> map : list){
            insertMap.put("pumpEventTm",map.get("pumpEventTm"));
            insertMap.put("slaveId",map.get("slaveId"));
            insertMap.put("seq",map.get("seq"));
            insertMap.put("orgId",map.get("orgId"));
            insertMap.put("rValue",map.get("rValue"));
            insertMap.put("sValue",map.get("sValue"));
            insertMap.put("tValue",map.get("tValue"));
            insertMap.put("lValue",map.get("lValue"));
            repository.insertAggre011(insertMap);
        }
        log.info("전날데이터 재저장완료");
    }







}
