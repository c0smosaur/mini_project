## KDT-BE7-Mini-Project 1조
### **miniBnB**
배포 url: [https://minibnb.shop](https://minibnb.shop)

api 문서: [swagger 링크](https://api.minibnb.shop/swagger-ui/index.html#/)

작업 레포지토리: [https://github.com/c0smosaur/mini_project](https://github.com/c0smosaur/mini_project)

-------
### 역할분담
|이름|역할|
|--|--|
|강서진|기능 구현 + Security|
|나도윤|기능 구현 + 배포|
|박하은|기능 구현 + 테스트 코드|

-------
### [필수 구현 사항]
![](https://velog.velcdn.com/images/c0smosaur/post/d0bd5d27-984d-4689-a14b-98b2002dd645/image.png)
  - 회원가입
    - 이메일 주소, 비밀번호, 이름으로 회원가입
  - 회원 로그인 기능
    - 이메일과 비밀번호로 로그인
    - DB에 저장된 정보와 비교, 일치하면 JWT 발급
    - 상품 조회(전체, 개별), 회원가입은 로그인 없이 사용 가능
  - 전체 상품 목록 조회
    - 데이터베이스에서 전체 상품 목록을 가져옵니다.
    - 이미지, 상품명, 상품 가격을 기본으로 출력
    - 페이징 단위: 20개
  - 개별 상품 조회
    - 이미지, 상품명, 상품 가격, 상품 상세 소개 (1줄 이상)을 기본으로 출력
    - (품절 처리) 이미 예약된 날짜는 예약 불가능으로 표시 
  - 상품 옵션 선택
    - 상품 상세 소개 페이지에서 상품 옵션(날짜, 숙박 인원)을 선택
  - 결제하기
    - 주문 페이지에서 결제하기 버튼 클릭 시 결제 절차 없이 주문 정보를 DB에 저장
  - 주문 결과 확인
    - 결제 처리 시, 주문한 상품에 대한 주문 결과를 출력

-------
### [선택 구현 사항]
  - 카테고리별 상품 목록 조회
    - 한국관광공사 TourAPI의 소분류에 따라 카테고리별로 조회 가능
  - 장바구니
    - 장바구니에 담긴 상품, 옵션 데이터에 따른 합계 금액 출력
    - 장바구니 삭제 가능
    - 주문하기 버튼을 통해 주문/결제 화면으로 이동
  - 주문 내역 확인
    - 마이페이지에서 예약 내역 출력

-------
### [KPT 보완점]
  - 코드 최적화
    - Fetch join을 이용해 숙소 조회에서 발생하는 N + 1 문제 해결
  - DB 동시성 제어
  - CI / CD 적용
    - 배포 자동화
    - 중요 데이터 GitHub secrets에 저장

-------
### ERD
![](https://velog.velcdn.com/images/c0smosaur/post/a2ef1b85-8195-48b5-8f08-2da33d204d5e/image.PNG)

### 사용 스택
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbKg7Gz%2FbtsG2nll7C0%2FpQzr4h97OgGJWS3NXKReH1%2Fimg.png)
