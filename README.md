# 🍔 AI Smart Kiosk (Backend)
>대상 인식·주문·백엔드 API를 담당하는 Spring Boot 기반 **백엔드** 서비스입니다.

---

## 🛠️ 협업 버전 관리
이 프로젝트는 팀원 간의 개발 환경 통일과 배포 안정성을 위해 아래 명시된 버전을 준수합니다.  
의존성은 Gradle로 관리합니다. 새 의존성을 추가할 때는 버전을 명시적으로 지정하고, 가능한 경우 Gradle Wrapper(`./gradlew`)로 빌드하세요.
|기술 스택|Version|비고|
|:---:|:---:|:---:|
|Java|JDK 17||
|Spring Server|Spring Boot 4.0.0|Gradle, Jar 사용|
|Python|Python 3.11.0||
|Python Server|Fast API 0.111.0||
|DB|MariaDB 10.11.15|AWS 프리티어 동일|
|Linux|Ubuntu 24.04 LTS|AWS 프리티어 동일|
|Jenkins|2.528.2 LTS||
|Docker|28.5.1||
|LLM|GPT 4o-mini||
|음성처리 AI|faster-whisper||
|영상처리 AI|Opencv, Deep Face, Mediapipe[Hand Landmarker]||
|Camera|Intel Realsense D415 Depth Camera||

---

## ✅ 필수 도구 및 버전
- **Java**: JDK 17 (프로젝트 toolchain 설정)
- **Spring Boot**: 4.0.0
- **Gradle**: wrapper 사용 (gradle-9.2.1, see gradle/wrapper/gradle-wrapper.properties)
- **MariaDB**: 10.11.15
- **Docker/Compose**: Docker 28.x, docker compose V2

## ⚠️ 보안 · `.env` 관리
- 이 저장소의 `docker-compose.yml`은 루트의 `.env` 파일을 참조합니다. `.env.example`를 참고해주세요.
- 권장 `.env.example` 예시 (민감값은 **비워두기**):
```bash
# .env.example
DRIVER_NAME=org.mariadb.jdbc.Driver
DRIVER_PASSWORD=<YOUR_DB_PASSWORD>
DB_COMPOSE_ROOT_PASSWORD=<YOUR_ROOT_PASSWORD>
DB_COMPOSE_USER=noknok_user
```

---

## 🐳Docker Compose
<details>
  <summary>백엔드 DB 환경 자동 구성 </summary>
  
  1. 최상위 루트 디렉터리 backend에서 해당 명령어를 치면 끝납니다.   
    1-2. WSL 환경에서 mnt로 이동 시 속도가 느릴 수 있으니 WSL 홈에 .env docker-compose.yml dump.sql cp 명령어로 가져와서 실행 하시는 것도 좋습니다. 
  
  ``` bash
  # 해당 디렉토리로 이동 후 Docker Compose 데몬 실행
  $ docker compose up -d
  ```
  2. [부가 설명] DB에 데이터가 정상적으로 추가 됐는지 확인

``` bash
# Docker가 정상적으로 실행 됐는지 확인
$ docker ps

# mariadb 컨테이너 bash로 접속
$ docker exec -it mariadb /bin/bash

# mysql 접속
$ mysql -u root -p<env에 있는 자신의 PASSWORD>

# 데이터베이스 확인
$ show databases;

# 테이블 확인
$ use nok_nok;
$ show tables;

# SQL 명령어 COUNT
$ MariaDB [nok_nok]> select count(*) from orders;
+----------+
| count(*) |
+----------+
|       50 |
+----------+
1 row in set (0.002 sec)
```

3. Docker Compose 종료 및 볼륨 삭제

``` bash
  # 컴포즈 종료
  $ docker compose down

  # 컴포즈 종료와 함께 volume(데이터) 삭제
  $ docker compose down -v
  # 혹은 docker volume rm nok_nok_docker_compose_mariadb or docker volume prune  
```
</details>


## 스프링부트 관련 가이드
<details>
  <summary>Local 환경에서 IDE(IntelliJ) YML Profiles</summary>
  
  - Local, STG, Production 마다 필요한 YML 정보가 다르다.
  - 해당 문제는 Profiles를 지정하여 특정 YML만 사용할 수 있다.

  1. 1번 메뉴를 선택한다.
  2. -Dspring.profiles.active=local (applicationXXX.yml의 XXX를 입력한다.)   
     2-1. 프로그램 인수 칸이 없다면 옵션 수정 -> VM 인수 추가를 누른다.
  <img width="1920" height="1022" alt="profile설정" src="https://github.com/user-attachments/assets/03f5cdf6-aef4-4002-8bf4-7544d91e988e" />

</details>

<details>
  <summary>IntelliJ 테스트에 Lombok 정상 작동 설정</summary>

  - 테스트를 진행할 때 Lombok을 사용하지 못하는 경우가 있다.
  - IntelliJ에서 어노테이션 활성화를 체크해야 한다.

  1. Ctrl + Alt + s로 설정으로 들어가서 검색란에 complie 검색
  2. 어노테이션 처리 활성화 클릭
<img width="1536" height="816" alt="어노테이션" src="https://github.com/user-attachments/assets/2d51f7d6-9fd7-49c8-8ddc-30fb2e38293e" />
</details>


## 🐛트러블 슈팅
<details>
  <summary>테스트 YML ENV 파일을 못 읽는 문제</summary>
  
  ``` groovy
  $ Failed to load ApplicationContext for [WebMergedContextConfiguration@76fc5687 testClass = com.NOK_NOK.ApplicationTests, locations = [], classes = [com.NOK_NOK.Application], contextInitializerClasses = [], activeProfiles = ["test"], propertySourceDescriptors = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.micrometer.metrics.test.autoconfigure.MetricsContextCustomizerFactory$DisableMetricsExportContextCustomizer@1f, org.springframework.boot.webmvc.test.autoconfigure.WebDriverContextCustomizer@49b07ee3, org.springframework.boot.web.server.context.SpringBootTestRandomPortContextCustomizerFactory$Customizer@2424686b, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@6e78fcf5, org.springframework.boot.test.context.PropertyMappingContextCustomizer@0, org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@417ad4f3, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@7ff2b8d2, org.springframework.test.context.support.DynamicPropertiesContextCustomizer@0, org.springframework.boot.test.context.SpringBootTestAnnotation@4a5aac2], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]

 $ java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@76fc5687 testClass = com.NOK_NOK.ApplicationTests, locations = [], classes = [com.NOK_NOK.Application], contextInitializerClasses = [], activeProfiles = ["test"], propertySourceDescriptors = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.micrometer.metrics.test.autoconfigure.MetricsContextCustomizerFactory$DisableMetricsExportContextCustomizer@1f, org.springframework.boot.webmvc.test.autoconfigure.WebDriverContextCustomizer@49b07ee3, org.springframework.boot.web.server.context.SpringBootTestRandomPortContextCustomizerFactory$Customizer@2424686b, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@6e78fcf5, org.springframework.boot.test.context.PropertyMappingContextCustomizer@0, org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@417ad4f3, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@7ff2b8d2, org.springframework.test.context.support.DynamicPropertiesContextCustomizer@0, org.springframework.boot.test.context.SpringBootTestAnnotation@4a5aac2], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]

# 정확한 원인
 Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/jdbc/autoconfigure/DataSourceInitializationAutoConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/jdbc/autoconfigure/DataSourceConfiguration$Hikari.class]: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Cannot load driver class: ${DRIVER_NAME}
```

  - build를 실행하거나 Test 코드를 실행하면 발생하는 문제점이다.
  - 위에 로그는 수 많은 로그 중에 일부분을 가져왔다.
  - 현재 testClass와 profiles를 application-test.yml을 사용하는 것을 알 수 있다.
  - 마지막 Cause by를 보면 데이터베이스 작업 시작할 때 env로 설정한 환경변수 값이 없다는 문제이다.

## 해결 방법
- 해당 yml에 env 파일을 끌고 올 수 있게 한다.
``` groovy
# application-test.yml
spring:
  config:
    import: optional:file:.env[.properties]
```
</details>


