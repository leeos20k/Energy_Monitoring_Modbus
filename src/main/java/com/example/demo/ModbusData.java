package com.example.demo;

import com.example.demo.repository.ModbusRepository;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class ModbusData {

//    private static ModbusRepository repository = null;
//
//    public ModbusData(ModbusRepository repository) {
//        ModbusData.repository = repository;
//    }
//
//
////    @Scheduled(cron = "0 0/10 * * * *") // <- 동작 x
//    public static void dataSaveDelete() {
//
//        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));//현재시각
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//시간 포맷
//
//        Map<String, Object> paramsMap = new HashMap<>();
//        paramsMap.put("sdTime", nowSeoul.format(formatter)); //쿼리수행을 위한 파라미터 맵 설정
//
//        List<Map<String, Object>> saveList = repository.saveData(paramsMap); //스케줄이 발생한 시간(yyyy-MM-dd HH:mm:ss)을 기준으로 쿼리 조회데이터 저장
//
//        //데이터저장*************************************************************************************************************************************
//        String csvFilePath = "C:\\csv\\csvDATA2.csv"; // CSV 파일 경로 <- 해당 경로에 폴더가 있어야 한다. 경로 폴더는 자동생성 x
//                                                     // 데이터를 추가하는 경우, 이 로직이 수행될 때 해당 파일을 켜놓으면 데이터 추가 x
//        // String fileName = nowSeoul.format(formatter) + ".csv"; // 파일이름을 동적으로(시간) 설정하고자 하는 경우, 새로운 Formatter 선언해야 함.
//        // String csvFilePath = "C:\\csv\\" + fileName;           // yyyy-MM-dd HH:mm:ss <- 띄어쓰기, ':' 은 파일이름으로 사용 불가.
//
//        File csvFile = new File(csvFilePath); //앞서 설정한 CSV 파일을 나타내는 객체
//        boolean fileExists = csvFile.exists() && csvFile.length() > 0; //해당 파일이 존재하는지, 데이터가 있는지 확인
//
//        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) { //해당 경로에 CSV 파일 생성할 것, 데이터 append 되도록 허용
//            // 헤더 쓰기
//            Map<String, Object> firstMap = saveList.get(0); //saveList 의 첫 번째 맵
//            String[] headers = firstMap.keySet().toArray(new String[0]); // firstMap 의 키 집합을 배열로 변환, 초기 크기가 0인 String 배열을 toArray 로 크기조정
//            // 첫 번째 맵의 key 값을 header(column)로 설정 <- 모든 맵의 key 는 동일하므로
//            if (!fileExists) {
//                writer.writeNext(headers); // fileExists 가 false <- 앞선 데이터가 없을때만 헤더를 추가
//            }
//                                           // csvDATA.csv
//                                           // ---------------헤더----------------
//                                           // ---------------데이터--------------
//                                           // ---------------헤더----------------
//                                           // ---------------데이터--------------
//                                           // ---------------헤더----------------
//                                           // ---------------데이터--------------    <- 데이터가 이렇게 쌓이는 것을 방지
//            // row 쓰기
//            for (Map<String, Object> map : saveList) { // saveList 의 모든 맵 데이터 차례대로 수행
//                String[] row = new String[headers.length]; //헤더의 길이와 동일한 길이의 문자열 형태 배열 선언
//                for (int i = 0; i < headers.length; i++) { //한 row 의 각 데이터 형변환 (CSV 파일은 String 과 ',' 로 이루어지므로)
//                    Object value = map.get(headers[i]);
//                    if (value == null) {
//                        row[i] = "null"; // null 값 처리
//                    } else {
//                        row[i] = String.valueOf(value); // null 이 아니면 String 으로 변환
//                    }
//                }
//                writer.writeNext(row); // row 데이터 추가
//            }
//            log.info("CSV 파일이 생성되었습니다.");
//        } catch (Exception e) {
//            log.warn("CSV 파일 생성 중 에러 발생 : " + e);
//        }//*******************************************************************************************************************************************
//
//        //데이터 삭제
//        //repository.deleteData(map);
//
//    }


}
