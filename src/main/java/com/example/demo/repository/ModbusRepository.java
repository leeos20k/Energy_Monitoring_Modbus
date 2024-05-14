package com.example.demo.repository;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class ModbusRepository {
    private final SqlSessionTemplate sqlSessionTemplate;

    public ModbusRepository(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public List<Map<String, Object>> GetEnergyMasterIPInfoVO(int orgId) {
        return sqlSessionTemplate.selectList("Modbus.GetEnergyMasterIPInfoVO", orgId);
    }

    public List<Map<String, Object>> GetEnergySlaveUnitIDInfoVO(Map<String, Object> map){
        return sqlSessionTemplate.selectList("Modbus.GetEnergySlaveUnitIDInfoVO",map);
    }

    public List<Map<String, Object>> GetEnergySlaveSeqInfoVO(int slaveId) {
        return sqlSessionTemplate.selectList("Modbus.GetEnergySlaveSeqInfoVO", slaveId);
    }

    public int insertPumpVO(Map<String, Object> map) {
        return sqlSessionTemplate.insert("Modbus.insertPumpVO", map);
    }






    public List<Map<String, Object>> GetEnergyTenInfo(int orgId) {
        return sqlSessionTemplate.selectList("Modbus.GetEnergyTenInfoVO", orgId);
    }
    public List<Map<String, Object>> GetEnergyDayInfo(int orgId) {
        return sqlSessionTemplate.selectList("Modbus.GetEnergyDayInfoVO", orgId);
    }

    public int insertAggre011(Map<String, Object> map) {
        return sqlSessionTemplate.insert("Modbus.insertAggre011VO", map);
    }

    public int DeleteAggre011 (){
        return sqlSessionTemplate.delete("Modbus.DeleteAggre011VO");
    }














//    public int CreateEnergyMinCollect() {
//        return sqlSessionTemplate.insert("Modbus.CreateEnergyMinCollectVO");
//    }
//
//    public int CreateEnergyHourCollect() {
//        return sqlSessionTemplate.insert("Modbus.CreateEnergyHourCollectVO");
//    }
//
//    public int CreateEnergyDayCollect() {
//        return sqlSessionTemplate.insert("Modbus.CreateEnergyDayCollectVO");
//    }
//
//    public int DeletePump010 (){
//        return sqlSessionTemplate.delete("Modbus.DeletePump010VO");
//    }
//
//    public int DeleteSlave030 (){
//        return sqlSessionTemplate.delete("Modbus.DeleteSlave030");
//    }





}
