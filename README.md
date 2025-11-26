# 🍔 AI Smart Kiosk (Backend)
>대상인식과 실시간 대화 기능이 탑재된 스마트 키오스크 프론트엔드 프로젝트입니다.

---

## 🛠️ 협업 버전 관리
이 프로젝트는 팀원 간의 개발 환경 통일과 배포 안정성을 위해 아래 명시된 버전을 엄격히 준수합니다.
라이브러리 추가 시 반드시 --save-exact 옵션을 사용하여 버전을 고정해 주세요.
|기술 스택|Version|비고|
|:---:|:---:|:---:|
|Java|JDK 17||
|Spring Server|Spring Boot 4.0.0|Gradle, Jar 사용|
|Python|Python 3.11.0||
|Python Server|Fast API 0.111.0||
|DB|MariaDB 10.11.15|AWS 프리티어 동일|
|NoSQL|Redis 7.0.15||
|Linux|Ubuntu 24.04 LTS|AWS 프리티어 동일|
|Jenkins|2.528.2 LTS||
|Docker|28.5.1||
|LLM|?||
|음성처리 AI|?||
|영상처리 AI|Opencv, Deep Face, Mediapipe[Hand Landmarker]||
|Camera|Intel Realsense D415 Depth Camera||

---

## 스프링부트 관련 가이드
<details>
  <summary>YML Profiles 사용 방법</summary>
  
  - Local, STG, Production 마다 필요한 YML 정보가 다르다.
  - 해당 문제는 Profiles를 지정하여 특정 YML만 사용할 수 있다.
  1. 1번 메뉴를 선택한다.
  2-1. -Dspring.profiles.active=local (applicationXXX.yml의 XXX를 입력한다.)
  2-2. 프로그램 인수 칸이 없다면 옵션 수정 -> VM 인수 추가를 누른다.
  <img width="1920" height="1022" alt="profile설정" src="https://github.com/user-attachments/assets/03f5cdf6-aef4-4002-8bf4-7544d91e988e" />

</details>
  






