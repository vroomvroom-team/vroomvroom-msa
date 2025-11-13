# 부릉부릉 🚚

> 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼
> 

## 🎯 프로젝트 소개

### 개요

**MSA(Microservices Architecture) 기반의 B2B 물류 관리 및 배송 시스템**입니다.

전국 각 지역의 허브센터를 통해 기업 간 물류를 효율적으로 관리하고, 배송 프로세스를 자동화합니다.

### 주요 기능

- 🏢 **허브 관리**: 공급 허브를 통한 지역별 물류 관리
- 📦 **재고 관리**: 허브별 상품 재고 추적 및 관리
- 🚛 **배송 관리**: 허브 간 물품 이동 및 최종 배송 처리
- 👥 **업체 관리**: 공급업체 및 수령업체 정보 관리
- 📊 **주문 처리**: 주문 접수 및 처리 자동화

### 배송 프로세스 예시

```
1. 부산 수산물 도매업체가 마른오징어 가공품 50개 주문
2. 경기도 일산 허브에서 재고 확인
3. 경기도 허브 → 부산 허브로 물품 이동 (허브 배송담당자)
4. 부산 허브 → 수산물 도매업체로 최종 배송 (업체 배송담당자)
```

### 개발 기간

- **기간**: 14일
- **인원**: 6명

## 👥 팀원 및 역할

| 이름 | 담당 업무 |
| --- | --- |
| 김민선 (리더) | 업체 서비스 및 상품 서비스 |
| 이원규 | 주문 서비스 |
| 김현수 | Slack 알림 서비스 및 Eureka 서비스 |
| 유민아 | 배송 서비스 및 배송 경로 구축 |
| 소진영 | 유저 서비스 및 게이트웨이 인증/인가 |
| 최은서 | 허브 서비스, 허브 경로 구축 (Hub to Hub Relay) |

## 🛠 기술 스택

### Framework

- **Java 17**
- **Spring Boot 3.3.2**: MSA 구축을 위한 핵심 프레임워크
- **Gradle**: 빌드 자동화 및 의존성 관리

### Data Layer

- **PostgreSQL 17**: 주요 데이터 저장소
- **Redis**: 세션 관리 및 캐싱

### Inter-Service Communication

- **Kafka**: 비동기 이벤트 기반 통신
    - 주문 생성, 재고 변동, 배송 상태 변경 등의 이벤트 처리
- **OpenFeign**: 동기식 REST API 통신

### Security

- **JWT (JSON Web Token)**: 사용자 인증 및 권한 관리

### API Documentation

- **Swagger (OpenAPI)**: REST API 문서 자동 생성 및 테스트

### Infrastructure

- **Docker**: 컨테이너 기반 서비스 배포 및 관리

## 🏗 시스템 아키텍처

<img width="1364" height="958" alt="image" src="https://github.com/user-attachments/assets/c7914d14-8103-43fa-8710-150a4216c594" />


### 애그리거트 루트 구성

<img width="1729" height="361" alt="image" src="https://github.com/user-attachments/assets/e1479ebf-e19b-4f88-be1d-eb6f2c1f5001" />


## 📊 ERD

<img width="3630" height="1652" alt="image" src="https://github.com/user-attachments/assets/bf48f65a-0ce5-49af-b2cc-1c28cf077129" />


## 📁 프로젝트 구조

```
vroomvroom-msa/
├── common-lib/             
├── eureka-server/          # Eureka 서버
├── gateway-service/        # API Gateway
├── user-service/           # 사용자 서비스
├── hub-service/            # 허브 서비스
├── company-service/        # 업체 서비스
├── order-service/          # 주문 서비스
├── delivery-service/       # 배송 서비스
├── slack-service/          # Slack 알림 서비스
├── docker-compose.yml      # Docker 구성
└── README.md

```

각 서비스는 다음과 같은 구조를 가집니다:

```
service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/vroomvroom/service/
│   │   │       ├── application/
│   │   │       ├── domain
│   │   │       ├── repository/
│   │   │       ├── infrastructure/
│   │   │       ├── presentation/
│   │   │       ├── exception/
│   │   │       └── Application.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
└── build.gradle

service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/vroomvroom/service/
│   │   │       ├── application/
│   │   │       │   ├── service/
│   │   │       │   └── port/UserClient.java
│   │   │       ├── domain/
│   │   │       │   └── repository/OrderRepository.java
│   │   │       ├── infrastructure/
│   │   │       │   ├── repository/JpaOrderRepository.java
│   │   │       │   ├── persistence/
│   │   │       │   ├── external/userFeignClient.java
│   │   │       │   │   └── adapter/UserClientImpl.java
│   │   │       │   └── config/
│   │   │       ├── presentation/
│   │   │       │   ├── controller/
│   │   │       │   └── dto/
│   │   │       ├── exception/
│   │   │       └── Application.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
└── build.gradle
```

## 🚀 실행 방법

### 사전 요구사항

- Docker & Docker Compose
- Java 17 이상
- Gradle 8.0 이상

# 프로젝트 실행 방법

### 환경 설정

1. **데이터베이스 설정**
    
    ```sql
    # MySQL 데이터베이스 생성
    CREATE DATABASE service_database;
    ```
    
2. **환경변수 설정** (`src/main/resources/properties/env.properties`)
    
    ```
    DB_URL=jdbc:mysql://localhost:3306/database_name
    DB_USERNAME=your_username
    DB_PASSWORD=your_password
    
    JWT_SECRET_KEY=your_jwt_secret_key
    JWT_ACCESS_TOKEN_EXPIRATION=900000
    JWT_REFRESH_TOKEN_EXPIRATION=1209600000
    ```
    
3. Docker Compose 등 실행에 필요한 환경 구성
    
    ```jsx
    version: '3.8'
    
    services:
      # ======================
      # PostgreSQL
      # ======================
      postgres:
        image: postgres:17
        container_name: postgres
        restart: always
        ports:
          - "5432:5432"
        environment:
          POSTGRES_USER: postgres
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: order_db
        volumes:
          - postgres_data:/var/lib/postgresql/data
        networks:
          - msa-network
    
      # ======================
      # Redis
      # ======================
      redis:
        image: redis:7
        container_name: redis
        restart: always
        ports:
          - "6379:6379"
        networks:
          - msa-network
    
      # ======================
      # Kafka + Zookeeper
      # ======================
      zookeeper:
        image: wurstmeister/zookeeper:latest
        container_name: zookeeper
        restart: always
        ports:
          - "2181:2181"
        networks:
          - msa-network
    
      kafka:
        image: wurstmeister/kafka:2.13-2.8.1
        container_name: kafka
        restart: always
        ports:
          - "9092:9092"
        environment:
          KAFKA_BROKER_ID: 1
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
          KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
          KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
          KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
        depends_on:
          - zookeeper
        networks:
          - msa-network
    
    volumes:
      postgres_data:
    
    networks:
      msa-network:
        driver: bridge
    ```
    
    ```bash
    $ docker compose up -d # Spring Boot Application 실행에 필요한 인프라 실행
    ```
    
4. **애플리케이션 실행**
    
    ```bash
    $ ./gradlew bootRun # Spring Boot Application 실행
    ```
    

### 4. 서비스 접속 정보

| 서비스 | 포트 | URL |
| --- | --- | --- |
| Eureka | 19090 | http://localhost:19090 |
| Gateway | 19089 | http://localhost:19089 |
| User Service | 19091 | http://localhost:19091 |
| Hub Service | 19092 | http://localhost:19092 |
| Order Service | 19093 | http://localhost:19093 |
| Delivery Service | 19094 | http://localhost:19094 |
| Company Service | 19095 | http://localhost:19095 |
| Slack Service | 19096 | http://localhost:19096 |

## 📚 API 문서

### Swagger를 통한 API 문서 확인

서비스 실행 후 다음 URL로 접속:

- **전체 API**: http://localhost:19090/swagger-ui.html

### 주요 API 엔드포인트

### 인증 API

```
POST /api/v1/auth/register    # 회원가입
POST /api/v1/auth/login       # 로그인
POST /api/v1/auth/logout      # 로그아웃
```

### 허브 API

```
GET    /api/v1/hubs              # 허브 목록 조회
GET    /api/v1/hubs/{id}         # 허브 상세 조회
POST   /api/v1/hubs              # 허브 생성
PATCH  /api/v1/hubs/{id}         # 허브 수정
DELETE /api/v1/hubs/{id}         # 허브 삭제
```

### 허브 경로 API

```
POST   /api/v1/hub-routes      # 경로 목록 조회
POST   /api/v1/hub-routes      # 경로 생성
PATCH  /api/v1/hub-routes/{id} # 허브 수정
DELETE /api/v1/hub-routes/{id} # 허브 삭제
```

### 허브 재고 API

```
GET    /api/v1/hubs/{hubId}/stocks           # 재고 조회
POST   /api/v1/hubs/{hubId}/stocks           # 재고 생성
PUT    /api/v1/hubs/{hubId}/stocks/decrease  # 재고 감소
PUT    /api/v1/hubs/{hubId}/stocks/increase  # 재고 증가
DELETE /api/v1/hubs/{hubId}/stocks/{stockId} # 재고 삭제
```

### 업체 API

```
GET    /api/v1/companies         # 업체 목록 조회
GET    /api/v1/companies/{id}    # 업체 상세 조회
POST   /api/v1/companies         # 업체 등록
PATCH  /api/v1/companies/{id}    # 업체 정보 수정
DELETE /api/v1/companies/{id}    # 업체 삭제
```

### 상품 API

```
GET    /api/v1/products          # 상품 목록 조회
GET    /api/v1/products/{id}     # 상품 상세 조회
POST   /api/v1/products          # 상품 등록
PATCH  /api/v1/products/{id}     # 상품 수정
DELETE /api/v1/products/{id}     # 상품 삭제
```

### 주문 API

```
GET    /api/v1/orders            # 주문 목록 조회
GET    /api/v1/orders/{id}       # 주문 상세 조회
POST   /api/v1/orders            # 주문 생성
PATCH  /api/v1/orders/{id}       # 주문 수정
DELETE /api/v1/orders/{id}       # 주문 취소
```

### 배송 API

```
GET    /api/v1/deliveries              # 배송 목록 조회
GET    /api/v1/deliveries/{id}         # 배송 상세 조회
POST   /api/v1/deliveries              # 배송 생성
PATCH  /api/v1/deliveries/{id}         # 배송 정보 수정
PATCH  /api/v1/deliveries/{id}/status  # 배송 상태 변경
```

### Slack API

```
GET    /api/v1/slack/messages          # Slack 메시지 목록 조회
GET    /api/v1/slack/messages/{id}     # Slack 메시지 상세 조회
POST   /api/v1/slack/messages          # Slack 메시지생성
PATCH  /api/v1/slack/messages/{id}     # Slack 메시지 수정
DELETE /api/v1/slack/messages/{id}     # Slack 메시지 삭제
```

## 🔑 주요 기능 설명

### 1. MSA 기반 아키텍처

- 독립적인 서비스 배포 및 확장 가능
- 서비스 간 느슨한 결합으로 유지보수성 향상
- API Gateway를 통한 단일 진입점 제공

### 2. 비동기 이벤트 처리

- Kafka를 활용한 이벤트 기반 아키텍처
- 주문 생성, 재고 변동, 배송 상태 변경을 이벤트로 처리
- 서비스 간 의존성 최소화

### 3. 동기 통신

- OpenFeign을 통한 서비스 간 REST API 호출
- 실시간 데이터 조회 및 검증

### 4. 보안

- JWT 기반 인증/인가
- Redis를 활용한 토큰 관리
- 역할 기반 접근 제어 (RBAC)
