```
ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
프로젝트 명 : Energy_Monitoring_System
프로젝트 개발인원 : 3명(DBA 1명, Publisher+Frontend Developer 1명, Frontend+Backend Developer 1명)
프로젝트 개발기간 : 약 2개월
ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

세부 프로젝트 명 : Energy_Monitoring_Modbus

개발목적 :
물리적으로 세팅된 센서의 데이터(16비트로 나누어진 상태)를 Modbus Protocol을 사용하여 읽은 후,
알맞게 변환하고 계산하여 실시간 생산량/전력량/가스량을 구하고 데이터베이스에 저장   

기술스택 :
Java: 프로그래밍 언어
IntelliJ IDEA: 통합 개발 환경(IDE)
Maven: 프로젝트 관리 및 이해 도구
MyBatis: SQL 매핑 프레임워크
MariaDB: 데이터베이스
Jamod: Modbus 프로토콜 라이브러리
Modbus Poll: Modbus 통신 프로토콜 검증 응용 프로그램
ezManager: Serial -> TCP 통신 변환장치 제어 프로그램(솔내시스템 CSE-H55N)

목표달성 : o

본인(Frontend+Backend Developer)기여내용 :
쿼리를 제외한 IOT_Monitoring_Modbus 내용전체
(Jamod 라이브러리 활용 Modbus 통신 연결, 데이터 수집, 저장시스템 구축)
```