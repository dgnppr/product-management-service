# 상품 관리 서비스

---

## Table of Contents

- [상품 관리 서비스](#상품-관리-서비스)
    - [기술 스택](#기술-스택)
    - [터미널 실행 순서](#터미널-실행-순서)
    - [기능 및 요구 사항](#기능-및-요구-사항)
    - [DB 모델링](#db-모델링)
    - [백엔드 모듈 구성](#백엔드-모듈-구성)
    - [API 디자인 설계](#api-디자인-설계)
    - [APIs](#apis)
        - [회원가입](#1-회원가입)
        - [로그인](#2-로그인)
        - [로그아웃](#3-로그아웃)
        - [가게 등록](#4-가게-등록)
        - [카테고리 등록](#5-카테고리-등록)
        - [상품 등록](#6-상품-등록)
        - [상품 수정](#7-상품-수정)
        - [상품 삭제](#8-상품-삭제)
        - [상품 리스트 조회](#9-상품-리스트-조회)
        - [상품 상세 조회](#10-상품-상세-조회)
        - [상품 검색](#11-상품-검색)
    - [고민했던 부분](#고민했던-부분)

---

<br>

## 기술 스택

- Java 21, SpringBoot 3.2.2, JPA, Junit5, Mockito
- MySQL 5.7, Redis

<br><br>

## 터미널 실행 순서

> MAC OS 기준으로 작성되었다.
>
> **실행을 위해 Docker Desktop이 설치되어 있어야 한다.**

1. `git clone https://github.com/dgnppr/product-management-service.git` 실행
2. 루트 디렉토리에서 `chmod +x build.sh` 실행
3. 루트 디렉토리에서 `./build.sh` 실행 후 30초 간 대기 (MySQL 초기화 진행)

<br><br>

## 기능 및 요구 사항

- [x] 사장님은 시스템에 휴대폰번호와 비밀번호 입력을 통해서 회원 가입을 할 수 있다.
    - [x] 사장님의 휴대폰 번호를 올바르게 입력했는지 확인함.
    - [x] 비밀번호를 안전하게 보관.
- [x] 사장님은 회원 가입이후, 로그인과 로그아웃을 할 수 있다
    - [x] JWT 토큰을 발행해서 인증을 제어하는 방식으로 구현
- [x] 사장님은 로그인 이후 상품 관련 아래의 행동을 할 수 있다.
    - [x] 상품을 등록할 수 있다.
    - [x] 상품을 수정할 수 있다.
    - [x] 상품을 삭제할 수 있다.
    - [x] 사장님은 등록한 상품의 리스트를 볼 수 있다.
        - [x] cursor based pagination 기반으로, 1page 당 10개의 상품이 보이도록 구현
    - [x] 사장님은 등록한 상품의 상세 내역을 볼 수 있다.
    - [x] 사장님은 상품 이름을 기반으로 검색할 수 있다.
        - [x] 이름에 대해서 like 검색과 초성검색을 지원한다.
        - [x] eg. 슈크림 라떼 → 검색가능한 키워드 : 슈크림, 크림, 라떼, ㅅㅋㄹ, ㄹㄸ
- [x] 로그인하지 않은 사장님의 상품 관련 API에 대한 접근 제한 처리가 된다.

<br><br>

## DB 모델링

<br>

> ddl 파일은 `ddl` 디렉토리에 위치

![Screenshot 2024-02-12 at 02 25 18](https://github.com/dgnppr/product-management-service/assets/89398909/89ca2d8a-bed4-42d3-8f1c-ce31a5c77378)

- 사장님은 N개의 가게를 가질 수 있다.
- 가게에는 N개의 상품을 가질 수 있다.
- 가게에는 N개의 카테고리를 가질 수 있다.
- 상품은 N개의 이름을 가질 수 있다.
- 카테고리와 상품의 관계는 N:M 관계이다.
    - eg. `카테고리 : [사장 추천 메뉴, 음료, 디저트, 식사]`, `상품 : [아메리카노, 카페라떼, 케이크, 파스타]` 가 있을 때 카테고리와 상품의 관계는 다음과
      같다.
        - `사장 추천 메뉴 : [아메리카노, 카페라떼]`
        - `음료 : [아메리카노, 카페라떼]`
        - `디저트 : [케이크]`
        - `식사 : [파스타]`

<br>

개념적으로 위 모델링을 사용했고, **운영 및 개발 편의성을 위해 물리적으로 외래키를 사용하지 않았다.**
트랜잭션을 통해 데이터 정합을 보장하는 방식을 선택했다.

<br><br>

## 백엔드 모듈 구성

![Screenshot 2024-02-12 at 02 38 46](https://github.com/dgnppr/product-management-service/assets/89398909/6e7902bb-9ff4-4f10-96ca-ae68bc3bf9e0)

- `module-common`: 공통 모듈
    - 목적: 코드 재사용성


- `module-infrastructure`: 데이터 접근 모듈
    - 목적: 데이터 접근 캡슐화
    - 테스트 정책: `@DataJpaTest` 통합 테스트


- `module-domain`: 비즈니스 로직 모듈
    - 목적: 비즈니스 로직 독립성
    - 테스트 정책: `Mock` 단위 테스트


- `module-interface`: API 모듈
    - 목적: 사용자 인터페이스 정의
    - 테스트 정책: `MockMvc`로 진행하되, 입력값 `validation`만 수행

<br><br>

## API 디자인 설계

- `DELETE`, `PUT` 을 지원하지 않는 HTTP 클라이언트 모듈 또는 이미 사용 중인 레거시 클라이언트 모듈이 제대로 지원하지 않을 수 있음
    - `READ` -> `GET`
    - `CREATE` -> `POST`
    - `UPDATE` -> `POST`
    - `DELETE` -> `POST`
- `GET` 요청은 리소스에 상태 변화를 주지 않는 동작, `POST` 요청은 리소스에 상태 변화를 주는 동작으로 구분
- 특정 도메인에 대해 2개 이상의 상태 처리가 존재한다면 동사형 단어 사용 (단, 명사형 표현 하위에 사용 가능)
- `Path` 구조: Version + Resource (+ ID + Action)
    - eg. `GET /v1/products` -> 상품 리스트 조회
- `API`들은 최대한 같은 객체를 반환하도록 설계
- 응답 코드
    - 200: 성공
    - 400: 클라이언트 오류
    - 401: 인증 오류
    - 403: 권한 오류
    - 404: 리소스 없음
    - 500: 서버 오류

<br><br>

## APIs

<br>

### 1. 회원가입

<br>

#### 요청

```http request
POST http://localhost:8080/v1/managers
Content-Type: application/json

{
  "phoneNumber": "01012345678",
  "password": "password"
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 201,
    "message": "Created"
  },
  "data": null
}
```

<br>

### 2. 로그인

<br>

#### 요청

```http request
POST http://localhost:8080/v1/login
Content-Type: application/json

{
  "phoneNumber": "01012345678",
  "password": "password"
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NzQxMDIsImV4cCI6MTcwODUzODEwMn0.VvIeGoDTWsDFk53sQeY8xd7av_i1sRKwiA-NAiiBROo"
  }
}
```

<br>

### 3. 로그아웃

<br>

#### 요청

```http request
POST http://localhost:8080/v1/logout
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDI4ODAsImV4cCI6MTcwODUwNjg4MH0.JpgMmIFNmwONWTiDbHobSQRqdl-ZI--tWfAP4C4Fz8A

{

}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": null
}
```

<br>

### 4. 가게 등록

<br>

#### 요청

```http request
POST http://localhost:8080/v1/stores
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2Njk4NTMsImV4cCI6MTcwODUzMzg1M30.rZ031qNQy7tNSflR06NROdxBLu_ipkc_oM6HbntlqBU
# fill in token to Authorization header

{
  "companyRegistrationNumber": "861-81-01475",
  "businessName": "바닐라 카페"
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 201,
    "message": "Created"
  },
  "data": {
    "storeId": 1,
    "managerId": 1,
    "companyRegistrationNumber": "861-81-01475",
    "businessName": "바닐라 카페"
  }
}
```

<br>

### 5. 카테고리 등록

<br>

#### 요청

```http request
POST http://localhost:8080/v1/stores/1/categories
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDMwOTQsImV4cCI6MTcwODUwNzA5NH0.JarWSBPKMV3EV4bCEhCyCE-kXlK-LWZ3W9tR1DwR0TY

{
  "name": "음료"
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 201,
    "message": "Created"
  },
  "data": {
    "categoryId": 1,
    "storeId": 1,
    "name": "음료"
  }
}
```

<br>

### 6. 상품 등록

<br>

#### 요청

```http request
POST http://localhost:8080/v1/stores/1/products
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDMwOTQsImV4cCI6MTcwODUwNzA5NH0.JarWSBPKMV3EV4bCEhCyCE-kXlK-LWZ3W9tR1DwR0TY

{
  "price": 10000,
  "cost": 5000,
  "name": "아이스 카페 라떼",
  "description": "description",
  "barcode": "1234567890",
  "expirationDate": "2024-12-31T00:00:00",
  "size": "SMALL",
  "categoryIds": [
    1
  ]
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 201,
    "message": "Created"
  },
  "data": null
}
```

<br>

### 7. 상품 수정

<br>

#### 요청

```http request
POST http://localhost:8080/v1/stores/1/products/1/update
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDMwOTQsImV4cCI6MTcwODUwNzA5NH0.JarWSBPKMV3EV4bCEhCyCE-kXlK-LWZ3W9tR1DwR0TY

{
  "price": 20000,
  "cost": 10000,
  "name": "아이스 바닐라 라떼",
  "description": "description",
  "barcode": "1234567890",
  "expirationDate": "2024-12-31T00:00:00",
  "size": "LARGE",
  "categoryIds": [
    1
  ]
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": null
}
```

<br>

### 8. 상품 삭제

<br>

#### 요청

```http request
POST http://localhost:8080/v1/stores/1/products/1/delete
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2MzY1MTEsImV4cCI6MTcwODUwMDUxMX0.0jCQf8OS8lntlO3U3cmFuwPfoBeRyjVc9J4BNXGCUSM

{
}
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": null
}
```

<br>

### 9. 상품 리스트 조회

<br>

#### 요청

```http request
GET http://localhost:8080/v1/stores/1/products
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDMwOTQsImV4cCI6MTcwODUwNzA5NH0.JarWSBPKMV3EV4bCEhCyCE-kXlK-LWZ3W9tR1DwR0TY
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "content": [
      {
        "productId": 2,
        "storeId": 1,
        "price": {
          "amount": 10000.00,
          "currency": "KRW"
        },
        "cost": {
          "amount": 5000.00,
          "currency": "KRW"
        },
        "name": "아이스 카페 라떼",
        "description": "description",
        "barcode": "1234567890",
        "expirationDate": "2024-12-31T00:00:00",
        "size": "SMALL",
        "categoryNames": [
          "음료"
        ]
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "empty": false,
        "unsorted": false,
        "sorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "first": true,
    "size": 10,
    "number": 0,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "numberOfElements": 1,
    "empty": false
  }
}
```

<br>

### 10. 상품 상세 조회

<br>

#### 요청

```http request
GET http://localhost:8080/v1/stores/1/products/1
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDMwOTQsImV4cCI6MTcwODUwNzA5NH0.JarWSBPKMV3EV4bCEhCyCE-kXlK-LWZ3W9tR1DwR0TY
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": {
    "productId": 2,
    "storeId": 1,
    "price": {
      "amount": 10000.00,
      "currency": "KRW"
    },
    "cost": {
      "amount": 5000.00,
      "currency": "KRW"
    },
    "name": "아이스 카페 라떼",
    "description": "description",
    "barcode": "1234567890",
    "expirationDate": "2024-12-31T00:00:00",
    "size": "SMALL",
    "categoryNames": [
      "음료"
    ]
  }
}
```

<br>

### 11. 상품 검색

<br>

#### 요청

```http request
GET http://localhost:8080/v1/stores/1/products/search?name=ㄹㄸ
Content-Type: application/json
Authorization: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiYXV0aGVudGljYXRpb24iLCJpYXQiOjE3MDc2NDMwOTQsImV4cCI6MTcwODUwNzA5NH0.JarWSBPKMV3EV4bCEhCyCE-kXlK-LWZ3W9tR1DwR0TY
```

#### 응답

```http response
HTTP/1.1 200 OK
{
  "meta": {
    "code": 200,
    "message": "OK"
  },
  "data": [
    {
      "productId": 2,
      "storeId": 1,
      "price": {
        "amount": 10000.00,
        "currency": "KRW"
      },
      "cost": {
        "amount": 5000.00,
        "currency": "KRW"
      },
      "name": "아이스 카페 라떼",
      "description": "description",
      "barcode": "1234567890",
      "expirationDate": "2024-12-31T00:00:00",
      "size": "SMALL"
    }
  ]
}
```

<br><br>

## 고민했던 부분

<br>

### JWT 인증/인가 시스템 로그아웃 처리

클라이언트 단에서 JWT 토큰을 삭제하는 방법은 쉽게 구현할 수 있었지만, 서버 단에서 로그아웃 처리를 하는 방법에 대해 고민했다.

로그아웃 처리된 JWT를 서버에서 블랙리스트에 추가하는 방법을 생각했다. DB 테이블에 블랙리스트 테이블을 추가하고, 로그아웃 요청이 들어올 때마다 해당 토큰을 블랙리스트에
추가하는 방법이었다.

DB를 사용하여 문제를 해결할 수 있었지만, 이 방법은 여러가지 문제점이 존재했다.

- 비즈니스 로직과 상관없는 인증을 위한 테이블 생성
- 요청마다 DB에서 블랙리스트 테이블을 조회해야 하므로, Latency가 느려지고, DB 부하를 높일 수 있다.
- 블랙리스트 테이블이 커질 경우, 테이블의 크기가 커지고, 검색 속도가 느려질 수 있다.

DB에 블랙리스트를 추가하는 방법은 좋은 방법이 아니라고 생각했고, 다른 방법을 고안했다.

아래 3가지 조건을 충족해줄 수 있는 데이터베이스에 대해서 고민했다.

- 관심사 분리
- 빠른 조회 성능
- TTL 기능

고민한 결과 위 3가지 조건을 모두 충족해주는 데이터베이스는 `Redis`였다.

Redis는 메모리 기반의 데이터베이스로, 빠른 조회 성능을 제공한다. 또한, TTL 기능을 제공하여 특정 시간이 지나면 데이터를 삭제할 수 있다.

JWT의 만료 시간만큼 Redis에 토큰을 저장해두고, TTL 기능을 사용하여 만료 시간이 지나면 토큰을 삭제하는 방법을 사용했다.
JWT를 검증할 때 만료 시간이 지나면 레디스에서 토큰을 조회하는 단계까지 않아도 된다. 또한 블랙리스트를 RDB에서 관리하지 않아도 되고, 추후에 토큰이 쌓여서 검색 속도가
느려지는 문제를 예방할 수 있었다.


<br>

### 초성 검색 지원

초성 검색을 지원하기 위해서 MySQL에서 지원하는 기능이 있는지 찾아보았다. 내가 찾아본 결과, MySQL에서 초성 검색을 지원하는 기능은 없었다.

그래서 어플리케이션 단에서 초성 검색을 지원해야했고, 이를 위해서 어떻게 해야할까 고민했다.
고민한 결과 상품을 등록할 때, 상품 이름을 초성으로 분리해서 별도의 테이블로 저장하는 방법을 선택했다.

이 방법의 문제점은 사장님이 상품 이름을 수정할 때마다 별도의 테이블도 함께 수정해줘야 한다는 점이다. 그럼에도 이 방법을 선택한 이유는 상품을 수정하는 것보다 초성 검색과
like 조회 연산이 훨씬 더 많을 것이라고 생각했다.

읽기 성능을 위해서 쓰기 성능을 희생하는 방법을 선택했다. 읽기 성능을 최적화 하기 위해서 상품 아이디와 상품 이름을 인덱싱하였다.

<br>