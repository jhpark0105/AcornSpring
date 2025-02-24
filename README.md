# 웹기반 소상공인 ERP 시스템

➡️ FRONT-END 코드 : https://github.com/jhpark0105/AcornReact.git

➡️ 결과보고서 PDF : https://drive.google.com/file/d/1rsslIy2HQ5onx3GqyPF4-MNJzr5K7e1F/view

➡️ 시연 영상 : https://www.youtube.com/watch?v=paeIl-SJOf4

---


## 👩🏻‍💻 프로젝트 소개

* **프로젝트 이름** : 뷰티샵 관리자를 위한 ERP 시스템

* **프로젝트 기간** : 2024.12.02 - 2025.01.04

* **기획 목적** : 소상공인의 고객, 서비스, 예약 관리가 가능한 시스템 개발, 관리 외에 통계를 위한 대시보드를 개발하여 월별 매출 현황 확인

* **기대 효과** : 소상공인의 재고 관리 능력 향상 및 계획 생산체제 구축 가능, 필요정보의 공유화가 가능해지고 정보의 흐름 일원화

* **주제 선정 배경** : 여성의 경제활동이 증가됨에 따라 미용 서비스의 수요가 확대되고 있으나 복잡한 기존 시스템의 한계와 간편한 관리 프로그램의 필요성을 느껴 전문적인 ERP 시스템으로 시장 경쟁력을 강화하기 위함

* **팀원** : 윤예은(팀장), 김수민, 박제희, 이우진, 정소율 (5명)

-----

## 🪅 활용 기술

* **개발** : React, JavaScript, Java, Spring Boot, Spring Security, JWT, WebSocket

* **인프라** : MariaDB (CloudType)

* **개발 및 협업 도구** : IntelliJ IDEA, WebStorm, DataGrip, Slack, Postman, Swagger, GitHub, Fork

---

## 🔖 기능 요약

#### ⭐ 판매 상품의 재고 수량 확인
  * 상품 관리 페이지에서 판매되는 상품을 등록, 수정 및 삭제 할 수 있고 대/소분류 상품에 관한 상세 정보를 확인하여 관리할 수 있다.
  * 상품 관리 페이지에서 관리된 상품에 대하여 남은 재고가 10개 이하인 상품을 메인 페이지에서 확인할 수 있다.

#### ⭐ 월별 서비스 매출 현황 확인
  * 메인 페이지에서 월 별 서비스 매출 현황을 그래프 형태로 확인할 수 있다. 

#### ⭐ 일별 회원 예약 현황 확인
  * 메인 페이지의 캘린더에서 날짜별 예약 유무를 확인할 수 있다.
  * 캘린더에서 확인하고자 하는 날짜를 선택하여 그에 맞는 날짜의 예약 현황을 확인할 수 있다.

---

## 📺 화면 구성 

| 메인 페이지  |  로그인 페이지   |
| :-------------------------------------------: | :------------: |
|  <img width="329" src="https://github.com/user-attachments/assets/75d7908f-fcb9-471f-8670-5b2da521f3fd"/> |  <img width="329" src="https://github.com/user-attachments/assets/09fbd798-d9b3-4a28-8cb3-8da43cc9ccb0"/>|  
| 마이페이지  |  고객 관리 페이지   |  
| <img width="329" src="https://github.com/user-attachments/assets/230cc5c3-7468-40fe-a6bd-9f56dfbb0f77"/>   |  <img width="329" src="https://github.com/user-attachments/assets/f0006aaa-64c9-4196-a028-f37ae7ca7411"/>     |
|  예약 관리 > 예약 대기 페이지   |  예약 관리 > 예약 현황 페이지   |  
| <img width="329" src="https://github.com/user-attachments/assets/b507b83a-fac4-4392-a1cd-4ecfa3c120c4"/>   |  <img width="329" src="https://github.com/user-attachments/assets/4a6609d7-7b61-42f4-b1d3-bf196ead5360"/>     |
|  알림  |  서비스 관리 페이지  |  
| <img width="329" src="https://github.com/user-attachments/assets/d33281cd-1eba-4ca3-8e05-29375bbb9693"/>   |  <img width="329" src="https://github.com/user-attachments/assets/e590a04e-8c48-406a-b6c3-d76237268684"/>     |
|  직원 관리 페이지   |  상품 관리 페이지  |  
| <img width="329" src="https://github.com/user-attachments/assets/aaf8d428-5efe-414e-90db-0508e0a4fd18"/>   |  <img width="329" src="https://github.com/user-attachments/assets/a3e0b6bd-a8be-4a04-9850-42f397bb2669"/>     |
|  공지 사항 페이지   |  근태 관리 페이지  |  
| <img width="329" src="https://github.com/user-attachments/assets/02aaa8cf-e2f9-40b2-b3ad-b244974dcf65"/>   |  <img width="329" src="https://github.com/user-attachments/assets/90953e9c-d447-4978-a04f-f6547f17184d"/>     |


