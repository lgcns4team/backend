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

## Redis
<details>
  
  <summary> Redis 설치 </summary> 
  
- WSL Ubuntu 환경 설치 및 실행 가이드
```bash
# 설치
$ curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg

$ echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list

$ sudo apt-get update
$ sudo apt-get install redis

# 실행
$ redis-server --daemonize yes

# Redis 연결
$ redis-cli

# 연결 테스트
127.0.0.1:6379> ping
PONG
```

</details>


## 스프링부트 관련 가이드
<details>
  <summary>Local 환경에서 IDE(InteliJ) YML Profiles</summary>
  
  - Local, STG, Production 마다 필요한 YML 정보가 다르다.
  - 해당 문제는 Profiles를 지정하여 특정 YML만 사용할 수 있다.

  1. 1번 메뉴를 선택한다.
  2. -Dspring.profiles.active=local (applicationXXX.yml의 XXX를 입력한다.)   
     2-1. 프로그램 인수 칸이 없다면 옵션 수정 -> VM 인수 추가를 누른다.
  <img width="1920" height="1022" alt="profile설정" src="https://github.com/user-attachments/assets/03f5cdf6-aef4-4002-8bf4-7544d91e988e" />

</details>

<details>
  <summary>InteliJ 테스트에 Lombok 정상 작동 설정</summary>

  - 테스트를 진행할 때 Lombok을 사용하지 못하는 경우가 있다.
  - InteliJ에서 어노테이션 활성화를 체크해야 한다.

  1. Ctrl + Alt + s로 설정으로 들어가서 검색란에 complie 검색
  2. 어노테이션 처리 활성화 클릭
<img width="1536" height="816" alt="어노테이션" src="https://github.com/user-attachments/assets/2d51f7d6-9fd7-49c8-8ddc-30fb2e38293e" />

</details>
  






