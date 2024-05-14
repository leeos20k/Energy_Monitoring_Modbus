package com.example.demo;

import com.example.demo.repository.ModbusRepository;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Log4j
public class ModbusMain {
    private static ModbusRepository repository = null;
    public static int orgId = 1;

    public ModbusMain(ModbusRepository repository) {
        ModbusMain.repository = repository;
    }

    public static void main() throws Exception {
        List<Map<String, Object>> list = repository.GetEnergyMasterIPInfoVO(orgId);
        try {
            for (int i = 0; i < list.size(); i++) {//리스트의 사이즈 만큼 = 마스터 한줄한줄에 대한 작업을 수행
                int period = Integer.parseInt(String.valueOf(list.get(i).get("collInterval"))) * 1000;//각 마스터마다 가지고 있는 수행간격 데이터((ex) period 3000 = 3초)
                int finalI = i;
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            modbus(list.get(finalI), timer);//한 마스터에 대하여, 그 마스터에 해당하는 알람정보 리스트를 참조시키고, 지금 수행시키는 타이머 객체의 주소를 참조시켜 모드버스실행
                        } catch (Exception e) {                                        //               ex) alarmMapsList 0: <- 조회되는 마스터 첫 번째에 해당
                            throw new RuntimeException(e);
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 1000, period);// 1000ms 후에 period 간격으로 task 실행
                //ex) [timer1 period:3]-1000ms-[timer2 period:5]-1000ms-[timer3 period:2]-1000ms-[timer4 period:10] ~~~
            }
            log.info("최초 Timer "+list.size()+"개가 정상적으로 실행됨");
        }catch(Exception e){
            log.error("최초 Timer 실행 중 오류발생 : "+e);
        }
    }


    public static void modbus(Map<String, Object> paramsMap, Timer timer){//한 마스터에 대한 모드버스 동작 수행

        //타이머에 의해 모드버스 수행 중, 마스터의 collInterval 값이 변경되는경우, 기존 타이머를 종료시키고, 변경된 collInterval 값으로 새로운 타이머를 생성
        List<Map<String, Object>> list = repository.GetEnergyMasterIPInfoVO(orgId);//등록되어있는 마스터 정보 조회, 저장
        Optional<Map<String, Object>> option = list.stream().filter(x -> x.get("masterIp").equals(paramsMap.get("masterIp"))).findAny();//조회한 마스터 목록 중, 현재 paramsMap 에 해당하는(=같은) 마스터정보 조회,저장
        if (!paramsMap.get("collInterval").equals(option.get().get("collInterval"))) {//기존 타이머의 collInterval 값과, 지금 조회한 collInterval 값을 비교했을때 다르다면,
            timer.cancel(); //타이머 정지

            ModbusTimer modbusTimer = new ModbusTimer(repository);
            try {
                modbusTimer.restartTimer(option);//지금 조회한 collInterval 값으로 타이머 다시 시작
            }catch (Exception e){
                log.error("collInterval 재시작 에러 :"+e);
            }finally {
                modbusTimer = null;//객체 초기화
            }
        }

        //CRS 에서 사용할 변수 선언
        int masterId = 0;
        String masterIp = null;
        int portNo = 0;
        String socketMth = null;
        boolean reconnect = false;
        int timeout = 0;
        if (paramsMap.get("masterId") != null) {
            masterId = Integer.parseInt(String.valueOf(paramsMap.get("masterId"))); //현재 수행중인 마스터의 ID 저장
        }
        if (paramsMap.get("masterIp") != null) {
            masterIp = String.valueOf(paramsMap.get("masterIp")); //현재 수행중인 마스터의 IP 저장
        }
        if (paramsMap.get("masterPortNo") != null) {
            portNo = Integer.parseInt(String.valueOf(paramsMap.get("masterPortNo"))); //현재 수행중인 마스터의 Port 저장
        }
        if (paramsMap.get("socketMth") != null) {
            socketMth = String.valueOf(paramsMap.get("socketMth")); //현재 수행중인 마스터의 socketMth 방식 저장
        }
        if (paramsMap.get("reconnect") != null) {
            int zeroOne = Integer.parseInt(String.valueOf(paramsMap.get("reconnect")));
            if(zeroOne == 0){
                reconnect = false;
            }else{
                reconnect = true;
            }
        }
        if (paramsMap.get("timeout") != null) {
            timeout = Integer.parseInt(String.valueOf(paramsMap.get("timeout"))); //현재 수행중인 마스터의 timeout 시간 저장
        }

        ModbusCRS modbusCRS = new ModbusCRS(repository);
        try {
            ModbusTCPMaster connectedMaster = modbusCRS.Connect(masterIp, portNo, socketMth, reconnect, timeout);//modbus connect: 연결된 모드버스 객체를 리턴받음

            ArrayList<Integer> readArray = modbusCRS.Read(connectedMaster, masterId, masterIp);//modbus read: 그 객체로부터 읽은 값을 array 에 저장, 그 값을 리턴받음

            modbusCRS.Save(readArray, masterId, masterIp);//modbus save: readArray 에 저장되어있는 값을 분배하여 DB에 insert

            connectedMaster.disconnect(); //다음 task 에서 다시 연결할 수 있도록 disconnect
        }catch (Exception e){
            log.error("Modbus CRS 중 에러 발생 :"+e);
        }finally {
            modbusCRS = null;//객체 초기화
        }
    }
}