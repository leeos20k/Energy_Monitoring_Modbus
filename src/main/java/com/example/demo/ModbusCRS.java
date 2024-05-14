package com.example.demo;


import com.example.demo.repository.ModbusRepository;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import lombok.extern.log4j.Log4j;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j
public class ModbusCRS {//Connect, Read, save

    public static int orgId = 1;
    private static ModbusRepository repository = null;

    public ModbusCRS(ModbusRepository repository) {
        ModbusCRS.repository = repository;
    }

    public static ModbusTCPMaster Connect(String masterIp, int portNo, String socketMth, boolean reconnect, int timeout) {
        ModbusTCPMaster modbusTCPMaster = null;
        try {
            if(socketMth.equals("TCP")){
                modbusTCPMaster = new ModbusTCPMaster(masterIp, portNo, timeout, reconnect, false);
            }else if(socketMth.equals("RTU")){
                modbusTCPMaster = new ModbusTCPMaster(masterIp, portNo, timeout, reconnect, true);
            }
            if(modbusTCPMaster != null){
                modbusTCPMaster.connect();
            }
            System.out.println("masterIp:" + masterIp + ", portNo:" + portNo + " 연결 성공!");
        } catch (Exception e) {
            System.out.println("masterIp:" + masterIp + ", portNo:" + portNo + " 연결 실패");
        }

        return modbusTCPMaster;
    }


    public static ArrayList Read(ModbusTCPMaster modbusTCPMaster, int masterId, String masterIp) {

        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("orgId", orgId);
        unitMap.put("masterId", masterId);
        List<Map<String, Object>> unitList = repository.GetEnergySlaveUnitIDInfoVO(unitMap);

        ArrayList<Integer> array = new ArrayList<>();

        for (int q = 0; q < unitList.size(); q++) {
            int slaveId = 0;
            String register = "";
            int startNum = 0;
            int slaveCnt = 0;
            int slaveUnitId = 0;
            if (unitList.get(q).get("slaveId") != null && unitList.get(q).get("register") != null && unitList.get(q).get("startNum") != null &&
                    unitList.get(q).get("slaveCnt") != null && unitList.get(q).get("slaveUnitId") != null) {
                slaveId = Integer.parseInt(String.valueOf(unitList.get(q).get("slaveId")));
                register = String.valueOf(unitList.get(q).get("register"));
                startNum = Integer.parseInt(String.valueOf(unitList.get(q).get("startNum")));
                slaveCnt = Integer.parseInt(String.valueOf(unitList.get(q).get("slaveCnt")));
                slaveUnitId = Integer.parseInt(String.valueOf(unitList.get(q).get("slaveUnitId")));
            }

//            System.out.println("masterIp:"+masterIp+" slaveId:"+slaveId+" register:"+register+" startNum:"+startNum+" slaveCnt:"+slaveCnt+" slaveUnitId:"+slaveUnitId);
            try {
                switch (register) {
                    case "Holding":
                        startNum -= 40001;
                        Register[] HoldingRegisterData = modbusTCPMaster.readMultipleRegisters(slaveUnitId, startNum, slaveCnt);
                        for (Register data : HoldingRegisterData) {
                            int value = data.getValue();
                            if (value >= 65000 && value <= 65536) {//마이너스(-1 = 65535)가 나오면 값 변환
                                array.add((65536 - value) * -1);
                            } else {
                                array.add(value);//원래 이 한줄
                            }
                        }
                        break;
                    case "Input":
                        startNum -= 30001;
                        InputRegister[] InputRegisterData = modbusTCPMaster.readInputRegisters(slaveUnitId, startNum, slaveCnt);
                        for (InputRegister data : InputRegisterData) {
                            int value = data.getValue();
                            if (value >= 65000 && value <= 65536) {//마이너스(-1 = 65535)가 나오면 값 변환
                                array.add((65536 - value) * -1);
                            } else {
                                array.add(value);//원래 이 한줄
                            }
                        }
                        break;
                }
            }catch (ModbusException e){
                log.warn("masterIp:"+masterIp+" slaveId:"+slaveId+" Register read 중 에러 발생:" + e);
                //값을 못가져와서 에러발생시, slaveCnt 만큼 -99999추가
                for (int a = 0; a < slaveCnt; a++) {
                    array.add(-99999);
                }
            }



            //로컬 임의데이터생성
//            Random random = new Random();
//            for(int r=0; r<slaveCnt; r++){
//                if(masterIp.equals("192.168.1.228")){
//                    array.add(random.nextInt(5 - 2 + 1) + 2);
//                }else if (masterIp.equals("192.168.1.229")) {//온도slave
//                    array.add(random.nextInt(3 - 1 + 1) + 1);
//                }else if (masterIp.equals("192.168.1.230")) {//test마스터1
//                    array.add(random.nextInt(45 - 42 + 1) + 42);
//                }else {
//                    array.add(random.nextInt(27 - 22 + 1) + 22);
//                }
//            }
        }

        return array;
    }


    public static void Save(ArrayList<Integer> readArray, int masterId, String masterIp) {//readArray 에 저장되어있는 값을 분배하여 DB에 insert

        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));//현재시각
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//시간 포맷

        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("orgId", orgId);
        unitMap.put("masterId", masterId);
        List<Map<String, Object>> unitList = repository.GetEnergySlaveUnitIDInfoVO(unitMap);


        for (int q = 0; q < unitList.size(); q++) {
            int slaveId = 0;
            int slaveStartNum = 0;
            int slaveAllCnt = 0;
            if (unitList.get(q).get("slaveId") != null && unitList.get(q).get("startNum") != null && unitList.get(q).get("slaveCnt") != null){
                slaveId = Integer.parseInt(String.valueOf(unitList.get(q).get("slaveId")));
                slaveStartNum = Integer.parseInt(String.valueOf(unitList.get(q).get("startNum")));
                slaveAllCnt = Integer.parseInt(String.valueOf(unitList.get(q).get("slaveCnt")));
            }
            List<Integer> slaveArray = new ArrayList<>(readArray.subList(0, slaveAllCnt));
            readArray.subList(0, slaveAllCnt).clear();
            List<Map<String, Object>> slaveDetailList = repository.GetEnergySlaveSeqInfoVO(slaveId);

            for(int i=0; i<slaveDetailList.size(); i++){
                int seq=0;
                int startNum=0;
                double waterRate=0;
                String slaveTp="";
                double tValue=0;
                if(slaveDetailList.get(i).get("seq")!=null && slaveDetailList.get(i).get("startNum")!=null && slaveDetailList.get(i).get("waterRate")!=null &&
                        slaveDetailList.get(i).get("slaveTp")!=null) {
                    seq = Integer.parseInt(String.valueOf(slaveDetailList.get(i).get("seq")));
                    startNum = Integer.parseInt(String.valueOf(slaveDetailList.get(i).get("startNum")));
                    waterRate = Double.parseDouble(String.valueOf(slaveDetailList.get(i).get("waterRate")));
                    slaveTp = String.valueOf(slaveDetailList.get(i).get("slaveTp"));
                    tValue = Double.parseDouble(String.valueOf(slaveDetailList.get(i).get("tValue")));
                }
                Map<String, Object> insertMap = new HashMap<>();
                switch (slaveTp) {
                    case "O":
                        int HIGH = slaveArray.get(startNum-slaveStartNum);
                        int LOW = slaveArray.get(startNum-slaveStartNum+1);
                        insertMap.put("pumpEventTm", nowSeoul.format(formatter));
                        insertMap.put("slaveId", slaveId);
                        insertMap.put("seq", seq);
                        insertMap.put("orgId",orgId);

                        double calcRange = (HIGH*10000)+LOW;
                        double calcWaterRate = 1 - (waterRate/100);
                        double rangeRounding = 0;
                        double caluValue = 0;

                        switch (seq){
                            case 1:
                                insertMap.put("rValue",HIGH);
                                insertMap.put("sValue",LOW);
                                insertMap.put("tValue",String.format("%.2f",(calcRange/10)*calcWaterRate));
                                rangeRounding = Double.parseDouble(String.format("%.2f",(calcRange/10)*calcWaterRate));
                                caluValue = rangeRounding - tValue;

                                if(rangeRounding < tValue){
                                    caluValue = rangeRounding;
                                }

                                if(caluValue >= -0.005 && caluValue <= 0.005){
                                    insertMap.put("caluValue", 0.00);
                                }else{
                                    insertMap.put("caluValue", String.format("%.2f",caluValue));
                                }

                                break;
                            case 2:
                            case 3:
                                insertMap.put("rValue",HIGH);
                                insertMap.put("sValue",LOW);
                                insertMap.put("tValue",String.format("%.2f",(calcRange/1000*calcWaterRate)));
                                rangeRounding = Double.parseDouble(String.format("%.2f",(calcRange/1000)*calcWaterRate));
                                caluValue = rangeRounding - tValue;

                                if(rangeRounding < tValue){
                                    caluValue = rangeRounding;
                                }

                                if(caluValue >= -0.005 && caluValue <= 0.005){
                                    insertMap.put("caluValue", 0.00);
                                }else{
                                    insertMap.put("caluValue", String.format("%.2f",caluValue));
                                }
                                break;
                            case 4:
                                insertMap.put("rValue",HIGH);
                                insertMap.put("sValue",LOW);
                                insertMap.put("tValue",String.format("%.2f",(calcRange*calcWaterRate)));
                                rangeRounding = Double.parseDouble(String.format("%.2f",(calcRange)*calcWaterRate));
                                caluValue = rangeRounding - tValue;

                                if(rangeRounding < tValue){
                                    caluValue = rangeRounding;
                                }

                                if(caluValue >= -0.005 && caluValue <= 0.005){
                                    insertMap.put("caluValue", 0.00);
                                }else{
                                    insertMap.put("caluValue", String.format("%.2f",caluValue));
                                }
                                break;
                            case 5:
                                insertMap.put("rValue",HIGH);
                                insertMap.put("sValue",LOW);
                                insertMap.put("tValue",String.format("%.2f",(calcRange/100*calcWaterRate)));
                                rangeRounding = Double.parseDouble(String.format("%.2f",(calcRange/100)*calcWaterRate));
                                caluValue = rangeRounding - tValue;

                                if(rangeRounding < tValue){
                                    caluValue = rangeRounding;
                                }

                                if(caluValue >= -0.005 && caluValue <= 0.005){
                                    insertMap.put("caluValue", 0.00);
                                }else{
                                    insertMap.put("caluValue", String.format("%.2f",caluValue));
                                }
                                break;
                            default:
                                insertMap.put("rValue", null);
                                insertMap.put("sValue", null);
                                insertMap.put("tValue", null);
                                insertMap.put("caluValue", null);
                                break;
                        }
                        insertMap.put("lValue",null);
                        insertMap.put("waterRate",waterRate);
                        break;
                    default:
                        int A = slaveArray.get(startNum-slaveStartNum);
                        int B = slaveArray.get(startNum-slaveStartNum+1);
                        // 일반적인 modbus protocol에서는 register의 한 address에 전송되는 값이 4자리의 16진수 형태이다.
                        // modbus poll 에서 signed 로 표기 되는 값의 범위는 -32768 ~ 32767 까지이며,
                        // jamod 라이브러리를 사용한 이 프로그램에서는 unsigned로 나타나므로 0~65535 의 범위로 나타난다.
                        String hexA = String.format("%04X", A & 0xFFFF);
                        String hexB = String.format("%04X", B & 0xFFFF); // 0000~FFFF가 전체 범위 이므로, A와 B를 최소 4자리인 0xFFFF의 형태로 변환 해준다.
                        String combinedHex = hexA + hexB; // 16진수 문자열 합치기 (ex: 4D999088)
                        // 합친 문자열을 unsigned 32비트 정수로 변환
                        long unsigned32bitLong = Long.parseLong(combinedHex, 16);
                        // 32비트 float로 변환
                        float float32bit = Float.intBitsToFloat((int)unsigned32bitLong);
                        insertMap.put("pumpEventTm", nowSeoul.format(formatter));
                        insertMap.put("slaveId", slaveId);
                        insertMap.put("seq", seq);
                        insertMap.put("orgId",orgId);
                        insertMap.put("rValue",A);
                        insertMap.put("sValue",B);
                        double checkCalu = 0;
                        switch (slaveTp){
                            case "N":
                                checkCalu = unsigned32bitLong - tValue;
                                if(unsigned32bitLong < tValue){
                                    checkCalu = unsigned32bitLong;
                                }
                                insertMap.put("tValue",unsigned32bitLong);
                                insertMap.put("caluValue",checkCalu);
                                break;
                            case "V":
                                checkCalu = float32bit - tValue;
                                if(float32bit < tValue){
                                    checkCalu = float32bit;
                                }
                                insertMap.put("tValue",float32bit);
                                insertMap.put("caluValue",checkCalu);
                                break;
                            default:
                                insertMap.put("tValue",null);
                                insertMap.put("caluValue",null);
                                break;
                        }
                        insertMap.put("lValue",null);
                        insertMap.put("waterRate",null);
                        break;
                }
                repository.insertPumpVO(insertMap);
                System.out.println("정상값");
            }
        }

    }
}
