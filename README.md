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
<img width="803" height="510" alt="image" src="https://github.com/user-attachments/assets/c2a5ffaa-4fb2-4d1b-8ed7-88240a3293ca" />
</div>

## 5. ERD

<details>
<summary> ERD 보기 </summary>
<p align="center">
<img width="934" height="683" alt="image" src="https://github.com/user-attachments/assets/9678810c-ce03-4532-9116-7ca3e73f2fc3" />
</p>
</details>

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
<details>
<summary> 결과 이미지 보기 </summary>
<p align="center">
<img width="1179" height="609" alt="image" src="https://github.com/user-attachments/assets/cb384491-05fe-491b-8df7-a4a643a76816" />
<img width="412" height="120" alt="image" src="https://github.com/user-attachments/assets/ce5ebe46-935e-4f22-ab95-c23cf095ad4f" />
</p>
</details>

| 구분      | 표본 수    | 평균 (ms)  | 최소값 (ms)    | 최대값 (ms) | p(90)(ms)  | p(95)(ms) |  오류 %   |
|-----------|---------|-----------|--------------|-----------|-------------|-----------|----------|
| HTTP 요청  | 200     | 656       | 138          | 973       | 910         | 926       |  95.0%   |


- **도입 후**: 동시성 제어에 성공하여 정확한 강의 인원 관리, 요청 실패율 0%.
<details>
<summary> 결과 이미지 보기 </summary>
<p align="center">
<img width="1188" height="614" alt="image" src="https://github.com/user-attachments/assets/cabe400f-c585-4cde-8165-1651eb993e25" />
<img width="426" height="153" alt="image" src="https://github.com/user-attachments/assets/bdc22e9b-d041-4113-bc2d-919de782f83b" />
</p>
</details>

| 구분      | 표본 수    | 평균 (ms)  | 최소값 (ms)    | 최대값 (ms) | p(90)(ms)  | p(95)(ms) |  오류 %   |
|-----------|---------|-----------|--------------|-----------|-------------|-----------|----------|
| HTTP 요청  | 200     | 1008      | 178          | 1720      | 1610        |1980       |  0%      |

#### 🌟 성능 개선 요약
- **동시 요청 처리**: 정확한 강의 수강 인원 유지.
- **오류율**: 95% → 0% (완전 제거)
