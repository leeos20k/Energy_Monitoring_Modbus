package com.example.demo;

import com.example.demo.repository.ModbusRepository;
import com.intelligt.modbus.jlibmodbus.Modbus;
import lombok.extern.log4j.Log4j;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
public class ModbusAlarm {

//    private static ModbusRepository repository = null;
//    public static int orgId = 1;
//    public ModbusAlarm(ModbusRepository repository) {
//        ModbusAlarm.repository = repository;
//    }
//
//    public static List<List<Map<String, Object>>> alarmMap() {//알람 맵 설정
//        String mIp = null;
//        int pNo = 0;
//        List<Map<String, Object>> list = repository.getMasterIPInfoVO(orgId);//등록되어있는 마스터 정보 조회, 저장
//        List<List<Map<String, Object>>> alarmMapsList = new ArrayList<>();//리턴할 리스트 선언
//
//        for (int i = 0; i < list.size(); i++) {//리스트의 사이즈 만큼 = 마스터 한줄한줄에 대한 작업을 수행
//            if (list.get(i).get("masterIp") != null) {
//                mIp = String.valueOf(list.get(i).get("masterIp"));//해당 순번 마스터의 IP 값 저장
//            }
//            if (list.get(i).get("masterPortNo") != null) {
//                pNo = Integer.parseInt(String.valueOf(list.get(i).get("masterPortNo")));//해당 순번 마스터의 Port 값 저장
//            }
//
//            Map<String, Object> testMap = new HashMap<>();//아래 쿼리를 수행시키기 위해 파라미터를 담을 맵 리스트 선언
//            testMap.put("orgId", orgId);
//            testMap.put("masterIp", mIp);
//            testMap.put("portNo", pNo);
//            List<Map<String, Object>> searchList = repository.getSlaveAlarm(testMap);//'해당 순번 마스터'의 모든 슬레이브 정보 조회, 저장
//
//            List<Map<String, Object>> alarmMaps = new ArrayList<>();//alarmMapsList <-에 담을 리스트 선언
//
//            for (int j = 0; j < searchList.size(); j++) {//'해당 순번 마스터'의 슬레이브 리스트 갯수만큼 수행
//                int sId = Integer.parseInt((String.valueOf(searchList.get(j).get("slaveId"))));
//                int seq2 = Integer.parseInt((String.valueOf(searchList.get(j).get("seq"))));
//                Map<String, Object> alarmMap = new HashMap<>();//슬레이브의 정보를 담을 맵 생성
//                alarmMap.put("slaveId", sId);
//                alarmMap.put("seq", seq2);
//                alarmMap.put("startTime", null);
//                alarmMap.put("isAlarm", false);
//                alarmMap.put("lastTime", null);
//                alarmMaps.add(alarmMap);//슬레이브 정보 맵에 put
//            }
//            alarmMapsList.add(alarmMaps);//alarmMaps 를, 리턴할 alarmMapsList 의 인덱스로 추가
//        }
//        //이 과정들이 끝나게되면 등록되어있는 모든 슬레이브의 알람정보를 가지고있는 alarmMapsList 가 생성이된다.
//        //ex) alarmMapsList 0: alarmMaps 0: alarmMap{slaveId : 1, seq : 1, startTime : null, isAlarm : false, lastTime : null}
//        //                               1: alarmMap{slaveId : 1, seq : 2, startTime : null, isAlarm : false, lastTime : null}
//        //                               2: alarmMap{slaveId : 1, seq : 2, startTime : null, isAlarm : false, lastTime : null}
//        //                  1: alarmMaps 0: alarmMap{slaveId : 1000, seq : 1, startTime : null, isAlarm : false, lastTime : null}
//        //                  1: alarmMaps 1: alarmMap{slaveId : 1000, seq : 2, startTime : null, isAlarm : false, lastTime : null}
//        //                  1: alarmMaps 2: alarmMap{slaveId : 1000, seq : 3, startTime : null, isAlarm : false, lastTime : null}
//        return alarmMapsList;
//    }
//
//
//    public static void alarmCheck(Map<String, Object> map, List<Map<String, Object>> alarmMaps, ZonedDateTime nowSeoul, DateTimeFormatter formatter) {
//        int roofNum=0; //사용변수들을 선언
//        int slaveId=0;
//        int seq=0;
//        String useFlag = "";
//        String slaveTp = "";
//        int maxValue=0;
//        int minValue=0;
//        int alarmTm=0;
//        ZonedDateTime startTime;
//        boolean isAlarm;
//
//        if(map.get("roofNum")!=null){ roofNum = Integer.parseInt(String.valueOf(map.get("roofNum")));} //null 이 아니라면, map 으로부터 추출한 값을 변수에 담기
//        if(map.get("slaveId")!=null){ slaveId = Integer.parseInt(String.valueOf(map.get("slaveId")));}
//        if(map.get("seq")!=null){ seq = Integer.parseInt(String.valueOf(map.get("seq")));}
//        if(map.get("useFlag")!=null){ useFlag = String.valueOf(map.get("useFlag"));}
//        if(map.get("slaveTp")!=null){ slaveTp = String.valueOf(map.get("slaveTp"));}
//        if(map.get("maxValue")!=null){ maxValue = Integer.parseInt(String.valueOf(map.get("maxValue")));}
//        if(map.get("minValue")!=null){ minValue = Integer.parseInt(String.valueOf(map.get("minValue")));}
//        if(map.get("alarmTm")!=null){ alarmTm = Integer.parseInt(String.valueOf(map.get("alarmTm")));}
//
//
//        if (alarmMaps.get(roofNum).get("startTime") != null) { startTime = (ZonedDateTime) alarmMaps.get(roofNum).get("startTime"); //알람맵의 값은 초기값이 null 이라서,
//        } else { startTime = (ZonedDateTime) alarmMaps.get(roofNum).get("startTime");}                                              //정상적으로 읽어도 null 인 경우가 있으므로
//        if (alarmMaps.get(roofNum).get("isAlarm") != null) { isAlarm = (Boolean) alarmMaps.get(roofNum).get("isAlarm");             //if 조건이 이러한 형태로 이루어져있다.
//        } else { isAlarm = (Boolean) alarmMaps.get(roofNum).get("isAlarm");}
//
//        if (slaveTp.equals("V")) {//전류 슬레이브 일 경우
//            if (map.get("rValue") != null && map.get("sValue") != null && map.get("tValue") != null && useFlag.equals("Y")) {//값을 못받아오면 -9999 -> null 로 변환하므로, null 이 아니면서 useFlag="Y"일 때만 수행
//
//                if ((int) map.get("rValue") > maxValue || (int) map.get("sValue") > maxValue || (int) map.get("tValue") > maxValue
//                        || (int) map.get("rValue") < minValue || (int) map.get("sValue") < minValue || (int) map.get("tValue") < minValue) {//측정값이 정상범위를 벗어났음(r s t Value 중 어떤값이라도 알람 상한선보다 높은 값이거나 어떤값이라도 알람 하한선보다 낮은 값이면)
//                    if (startTime == null) {//이전에 이상 값이 측정된 적이 있는지 여부(null 이면 없음.)
//                        alarmMaps.get(roofNum).put("startTime", nowSeoul);//이상값 최초 발생시각 startTime 에 기록
//                    } else {//이전 루프에서 이상 값이 측정된 적이 있음(연속적)
//                        Duration duration = Duration.between(startTime, nowSeoul);//이상값 최초 발생시각과 현재시각의 차이 duration(=이상값이 몇초간 지속되고 있는가)
//                        log.info("slaveId:"+slaveId+", seq:"+seq+" 범위 밖 값 발생 "+duration.toSeconds() + "초"+" minValue:"+minValue+" r:"+map.get("rValue")+",s:"+map.get("sValue")+",t:"+map.get("tValue")+" maxValue:"+maxValue);
//                        if (alarmTm <= duration.toSeconds()) {//duration 이 고장판별시간보다 크다(=알람발생)
//                            alarmMaps.get(roofNum).put("isAlarm", true);//해당 슬레이브에 대한 알람맵에 isAlarm true 적용
//                            alarmMaps.get(roofNum).put("lastTime", nowSeoul.format(formatter));//해당 슬레이브에 대한 알람맵에 현재시간 적용
//                            log.info("slaveId:"+slaveId+", seq:"+seq+" 알람 발생 "+duration.toSeconds() + "초"+" minValue:"+minValue+" r:"+map.get("rValue")+",s:"+map.get("sValue")+",t:"+map.get("tValue")+" maxValue:"+maxValue);
//                        }
//                    }
//                } else {//측정값이 정상범위 일 때
//                    log.info("정상값");
//                    alarmMaps.get(roofNum).put("startTime", null);//startTime 초기화
//                    if (isAlarm == true) {//알람이 발생되는 상태에서 -> 다시 정상값으로 인식되었을 때 알람정보 저장 (계속 이상값만 나오면 알람정보 저장 안 됨.)
//                        map.put("alarmStartTm", startTime.format(formatter));//해당 슬레이브에 대한 알람 시작시간
//                        map.put("alarmEndTm", alarmMaps.get(roofNum).get("lastTime"));//해당 슬레이브에 대한 마지막 이상값 측정시간
//                        repository.insertAlarm(map);//알람데이터 DB에 저장
//                        repository.updateAlarm(map);//해당 슬레이브의 알람 Flag "Y"로 업데이트
//                        alarmMaps.get(roofNum).put("isAlarm", false);//해당 슬레이브의 isAlarm 초기화
//                        log.info("알람정보 저장");
//                        //postTest("over value waring " + lastOverTime.format(formatter) + "~" + nowSeoul.format(formatter) + "(" + duration.toSeconds() + "초)");
//                    }
//                }
//
//            } else {//범위값 밖의  값을 못받아와서 파라미터 맵에 [-9999 -> null]로 저장된 경우, useFlag 가 "N"인경우
//                    //*알람이 발생되고 있는 상태에서 값을 못읽어오거나, useFlag 를 "N"로 바꾼경우, 발생되던 알람정보를 저장해야 하므로.*
//                log.warn("slaveId : " + slaveId + ", seq : " + seq + " 알람발생 도중 이상 발생");
//                alarmMaps.get(roofNum).put("startTime", null); //위와 같은 내용 수행
//                if (isAlarm == true) {
//                    map.put("alarmStartTm", startTime.format(formatter));
//                    map.put("alarmEndTm", alarmMaps.get(roofNum).get("lastTime"));
//                    repository.insertAlarm(map);
//                    repository.updateAlarm(map);
//                    alarmMaps.get(roofNum).put("isAlarm", false);
//                    log.warn("알람정보 저장");
//                    //postTest("over value waring " + lastOverTime.format(formatter) + "~" + nowSeoul.format(formatter) + "(" + duration.toSeconds() + "초)");
//                }
//            }
//        } else {//전류 슬레이브가 아닐 경우
//            if (map.get("lValue") != null && useFlag.equals("Y")) {//값을 못받아오면 -9999 -> null 로 변환하므로, null 이 아니면서 useFlag="Y"일 때만 수행
//                if ((int) map.get("lValue") > maxValue || (int) map.get("lValue") < minValue) {//측정값이 정상범위를 벗어났음(lValue 값이 알람 상한선보다 높은 값이거나 알람 하한선보다 낮은 값이면)
//                    if (startTime == null) {//이전에 이상 값이 측정된 적이 있는지 여부(null 이면 없음.)
//                        alarmMaps.get(roofNum).put("startTime", nowSeoul);//이상값 최초 발생시각 startTime 에 기록
//                    } else {//이전 루프에서 이상 값이 측정된 적이 있음(연속적)
//                        Duration duration = Duration.between(startTime, nowSeoul);//이상값 최초 발생시각과 현재시각의 차이 duration(=이상값이 몇초간 지속되고 있는가)
//                        log.info("slaveId:"+slaveId+", seq:"+seq+" 범위 밖 값 발생 "+duration.toSeconds() + "초"+" minValue:"+minValue+" l:"+map.get("lValue")+" maxValue:"+maxValue);
//                        if (alarmTm <= duration.toSeconds()) {//duration 이 고장판별시간보다 크다(=알람발생)
//                            alarmMaps.get(roofNum).put("isAlarm", true);//해당 슬레이브에 대한 알람맵에 isAlarm true 적용
//                            alarmMaps.get(roofNum).put("lastTime", nowSeoul.format(formatter));//해당 슬레이브에 대한 알람맵에 현재시간 적용
//                            log.info("slaveId:"+slaveId+", seq:"+seq+" 알람 발생 "+duration.toSeconds() + "초"+" minValue:"+minValue+" l:"+map.get("lValue")+" maxValue:"+maxValue);
//                        }
//                    }
//                } else {//측정값이 정상범위 일 때
//                    log.info("정상값");
//                    alarmMaps.get(roofNum).put("startTime", null);//startTime 초기화
//                    if (isAlarm == true) {//알람이 발생되는 상태에서 -> 다시 정상값으로 인식되었을 때 알람정보 저장 (계속 이상값만 나오면 알람정보 저장 안 됨.)
//                        map.put("alarmStartTm", startTime.format(formatter));//해당 슬레이브에 대한 알람 시작시간
//                        map.put("alarmEndTm", alarmMaps.get(roofNum).get("lastTime"));//해당 슬레이브에 대한 마지막 이상값 측정시간
//                        repository.insertAlarm(map);//알람데이터 DB에 저장
//                        repository.updateAlarm(map);//해당 슬레이브의 알람 Flag "Y"로 업데이트
//                        alarmMaps.get(roofNum).put("isAlarm", false);//해당 슬레이브의 isAlarm 초기화
//                        log.info("알람 발생");
//                        //postTest("over value waring " + lastOverTime.format(formatter) + "~" + nowSeoul.format(formatter) + "(" + duration.toSeconds() + "초)");
//                    }
//                }
//            } else {//범위값 밖의  값을 못받아와서 파라미터 맵에 [-9999 -> null]로 저장된 경우, useFlag 가 "N"인경우
//                    //*알람이 발생되고 있는 상태에서 값을 못읽어오거나, useFlag 를 "N"로 바꾼경우, 발생되던 알람정보를 저장해야 하므로.*
//                log.warn("slaveId : " + slaveId + ", seq : " + seq + " 알람발생 도중 이상 발생");
//                alarmMaps.get(roofNum).put("startTime", null);//위와 같은 내용 수행
//                if (isAlarm == true) {
//                    map.put("alarmStartTm", startTime.format(formatter));
//                    map.put("alarmEndTm", alarmMaps.get(roofNum).get("lastTime"));
//                    repository.insertAlarm(map);
//                    repository.updateAlarm(map);
//                    alarmMaps.get(roofNum).put("isAlarm", false);
//                    log.warn("알람 발생");
//                    //postTest("over value waring " + lastOverTime.format(formatter) + "~" + nowSeoul.format(formatter) + "(" + duration.toSeconds() + "초)");
//                }
//            }
//        }
//    }
}
