# 🏫 수강신청 서비스

## 1. 팀원 소개

<div align="center">
  <table>
    <tbody>
      <tr>
        <td align="center" style="padding: 20px;">
          <img src="https://avatars.githubusercontent.com/u/79428775?v=4" width="120px" style="border-radius: 50%;" alt=""/>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀장</b><br />
            <a href="https://github.com/jh-01" style="font-size: 16px;">김지현</a>
            <div style="margin-top: 5px; font-size: 14px;">
              유저<br />동시성 제어 및 테스트<br />CI/CD<br />k6 부하 테스트<br />
            </div>
          </div>
        </td>
        <td align="center" style="padding: 20px;">
          <img src="https://avatars.githubusercontent.com/u/77458624?s=400&v=4" width="120px" style="border-radius: 50%;" alt=""/>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀원</b><br />
            <a href="https://github.com/ko-dongwon" style="font-size: 16px;">고동원</a>
            <div style="margin-top: 5px; font-size: 14px;">
              강의<br />동시성 제어 및 테스트<br />k6 부하 테스트<br />
            </div>
          </div>
        </td>
        <td align="center" style="padding: 20px;">
          <img src="https://avatars.githubusercontent.com/u/205297434?s=64&v=4" width="120px" style="border-radius: 50%;" alt=""/>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀원</b><br />
            <a href="https://github.com/dain391" style="font-size: 16px;">이다인</a>
            <div style="margin-top: 5px; font-size: 14px;">
              수강신청<br />동시성 제어 및 테스트<br />
            </div>
          </div>
        </td>
        <td align="center" style="padding: 20px;">
          <img src="https://avatars.githubusercontent.com/u/206229978?v=4" width="120px" style="border-radius: 50%;" alt=""/>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀원</b><br />
            <a href="https://github.com/syumz" style="font-size: 16px;">임서연</a>
            <div style="margin-top: 5px; font-size: 14px;">
              공지사항<br />동시성 제어 및 테스트<br />캐싱<br />
            </div>
          </div>
        </td>
        <td align="center" style="padding: 20px;">
          <img src="https://avatars.githubusercontent.com/u/206650036?v=4" width="120px" style="border-radius: 50%;" alt=""/>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀원</b><br />
            <a href="https://github.com/imtaehun" style="font-size: 16px;">임태훈</a>
            <div style="margin-top: 5px; font-size: 14px;">
              인증·인가 (JWT)<br />동시성 제어 및 테스트<br />CI/CD
            </div>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</div>

## 2. 프로젝트 개요.
**개발 기간:** 2025.07.07 ~ 2025.07.15

`ClassPath`는 Spring Boot 기반의 대학 수강신청 사이트입니다. 사용자는 강의 검색·조회 및 잔여 정원 확인, Redisson 분산 락을 통한 동시성 제어로 피크 타임에도 안정적인 성능을 제공합니다.

## 3. 주요 기술 스택
<div align="center">

### **애플리케이션**
<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">

### **인증 및 보안**
<img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> 
<img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white"> 

### **데이터베이스**

<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">

### **협업 도구**

![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) 
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
</div>

## 4. 아키텍쳐
<div align="center">
<img width="719" height="483" alt="image" src="https://github.com/user-attachments/assets/3bccbd5e-56ef-4844-b7a8-4f0ae0bd891d" />
</div>

## 5. ERD
<p align="center">
<img width="1041" height="768" alt="image" src="https://github.com/user-attachments/assets/9e32b150-2574-4be9-b6f0-77905fc8ee61" />
</p>


<h2>6. <a href="https://www.notion.so/teamsparta/2292dc3ef51480a0a5f8d705322edfdf?v=2292dc3ef5148019be7d000cbdc6a4d5&source=copy_link">API 명세</a></h2>

## 7. 트러블 슈팅 & 최적화 전략

### 1. 수강 신청 동시성 처리 (Redisson) 
#### 🔍 문제 원인
- 여러 사용자가 동시에 수강신청 시 실제 생성된 수강신청 데이터 개수와 현재 수강 인원 데이터 간의 불일치 문제 발생
- 단일 트랜잭션 내에서 INSERT(수강신청 레코드 생성)와 UPDATE(강의 현재 인원 갱신)를 연속 실행하면서 데드락 발생

#### 💡 문제 해결
- **Redisson**으로 분산 락 구현하여 동시성 문제 해결.

#### 📈 도입 전후 비교
시나리오 : 50명 정원의 강의에 대해 200명이 한번에 요청
- **도입 전**: 동시 요청으로 인해 데이터 불일치 발생, 데드락 발생, 요청 실패율 증가.

<p align="center">
<img width="1179" height="609" alt="image" src="https://github.com/user-attachments/assets/cb384491-05fe-491b-8df7-a4a643a76816" />
<img width="412" height="120" alt="image" src="https://github.com/user-attachments/assets/ce5ebe46-935e-4f22-ab95-c23cf095ad4f" />
</p>


| 구분      | 표본 수    | 평균 (ms)  | 최소값 (ms)    | 최대값 (ms) | p(90)(ms)  | p(95)(ms) |  오류 %   |
|-----------|---------|-----------|--------------|-----------|-------------|-----------|----------|
| HTTP 요청  | 200     | 656       | 138          | 973       | 910         | 926       |  95.0%   |

<br>

- **도입 후**: 동시성 제어에 성공하여 정확한 강의 인원 관리, 요청 실패율 0%.

<p align="center">
<img width="1188" height="614" alt="image" src="https://github.com/user-attachments/assets/cabe400f-c585-4cde-8165-1651eb993e25" />
<img width="426" height="153" alt="image" src="https://github.com/user-attachments/assets/bdc22e9b-d041-4113-bc2d-919de782f83b" />
</p>


| 구분      | 표본 수    | 평균 (ms)  | 최소값 (ms)    | 최대값 (ms) | p(90)(ms)  | p(95)(ms) |  오류 %   |
|-----------|---------|-----------|--------------|-----------|-------------|-----------|----------|
| HTTP 요청  | 200     | 1008      | 178          | 1720      | 1610        |1980       |  0%      |

#### 🌟 성능 개선 요약
- **동시 요청 처리**: 정확한 강의 수강 인원 유지.
- **오류율**: 95% → 0% (완전 제거)

<br>

### 2. 공지사항 조회수 캐싱 최적화 (Redis 캐시 활용)
#### 🔍 문제 원인
- 게시글 조회 시마다 DB에서 조회수를 즉시 증가시키면서 DB 부하 및 성능 저하 발생
- 동시 다발적인 조회 요청 시 DB 업데이트 경합과 느린 응답 문제 발생

#### 💡 문제 해결
- Redis 캐시를 사용해 조회수 캐싱
- 일정 시간 간격으로 캐시된 조회수를 DB에 일괄 업데이트하는 방식 도입
- 캐시 TTL 및 스케줄러(@Scheduled)를 활용해 데이터 일관성 유지

#### 📈 도입 전후 비교
시나리오 : 50명의 가상 사용자가 30초 동안 동시에 특정 공지사항 단일 조회 API를 반복 요청
- **도입 전**: 조회 요청 시마다 DB 업데이트 → 높은 부하, 응답 지연 및 데이터 경쟁 발생

<p align="center">
<img width="1174" height="595" alt="image" src="https://github.com/user-attachments/assets/ba1f9dbb-c7b1-427c-bcf6-beb0e4ce9ba1" />
</p>


| 구분      | 표본 수    | 평균 (ms)  | 최소값 (ms)    | 최대값 (s) | p(90)(ms)  | p(95)(ms) |
|-----------|---------|-----------|--------------|-----------|-------------|-----------|
| HTTP 요청  | 15839     | 141.87       | 6.83          | 2.59      | 199.43         | 222.47   | 

<br>

- **도입 후**: 조회수 증가 요청은 Redis 캐시에 기록 → DB 부하 감소, 응답 속도 개선, 동시성 문제 완화

<p align="center">
<img width="1198" height="481" alt="image" src="https://github.com/user-attachments/assets/9cfe0c32-54f9-42e0-b2c2-458e08c1fb6b" />
</p>


| 구분      | 표본 수    | 평균 (ms)  | 최소값 (ms)    | 최대값 (s) | p(90)(ms)  | p(95)(ms) |
|-----------|---------|-----------|--------------|-----------|-------------|-----------|
| HTTP 요청  | 26151     | 25.49       | 5.26          | 1.35       | 48.8         | 61.27       |

#### 🌟 성능 개선 요약
- **응답 속도 크게 향상**: 평균 응답 시간이 약 82% 감소
- **최대 응답 시간 단축**: 최대 응답 시간이 절반 이상 감소
- **90%, 95% 지연 시간(p90, p95) 대폭 개선**: p(90) 199.43ms → 48.8ms, p(95) 222.47ms → 61.27ms
- **처리량 증가**: 캐시 적용 후 표본 수가 증가해 더 많은 요청을 처리 가능
- **서비스 안정성 및 사용자 경험 개선**: 빠른 응답과 부하 완화로 오류 및 지연 감소 기대


### 3. CI/CD 배포 (AWS CODE PIPELINE 활용)
<img width="1479" height="827" alt="12조 ClassPath" src="https://github.com/user-attachments/assets/5bb24a45-d2a8-4b48-8ebb-aab7cd726fe9" />
- 깃허브에 코드를 푸시
- Pipeline에서 이를 트리거로 인식하여 작동
- 소스 코드를 Code Build 에서 빌드하고 아티팩트를 S3에 저장
- 아티팩트 저장까지 문제없이 끝났다면 Deploy에서 아티팩트를 확인하고 배포를 준비
- 준비가 완료되면 EC2에 배포
- 배포 중 발생하는 로그나 상태 정보는 CloudWatch에 기록


## 8. 발표 자료
<a href="https://www.canva.com/design/DAGs-GTp1Bw/BnUWMeOmn5xwtqex5vwJkg/edit?utm_content=DAGs-GTp1Bw&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton">![12조 ClassPath](https://github.com/user-attachments/assets/b53ab558-67f4-44db-b522-51c8ed16e52a)</a>
![12조 ClassPath](https://github.com/user-attachments/assets/d4b73357-cdff-4d5c-82b3-19fb09f0cb6e)
![12조 ClassPath (2)](https://github.com/user-attachments/assets/a5f1782d-2ba5-465f-97fc-8e6f5e23b224)

