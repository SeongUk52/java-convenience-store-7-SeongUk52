# java-convenience-store-precourse

# 구현할 기능 목록

## 가격 계산
- [x] 각 상품의 가격을 계산하는 기능
- [x] 각 상품의 할인 가격을 계산하는 기능


## 재고
- [x] 각 상품의 수량을 저장한다.
- [x] src/main/resources/products.md의 파일 내용을 읽어온다.
- [x] src/main/resources/products.md에 저장된 내용을 기록한다.
- [x] 고객이 상품을 구매할 때마다, 결제된 수량만큼 재고에서 차감한다.
- [x] 프로모션 재고는 프로모션 대상일 경우 우선 차감하며 부족할 경우 일반 재고를 사용하는데
일반 재고는 프로모션 혜택이 적용되지 않는다.
- [x] 한 번에 여러 상품을 구매하는 기능

## 프로모션 할인

- [x] src/main/resources/promotions.md의 파일 내용을 읽어온다.
- [x] 오늘 날짜가 프로모션 기간 내에 포함되어 있는지 확인한다.
- [x] 프로모션 적용이 가능한 상품에 대해 해당 수량보다 적게 주문한 경우 필요한
수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
  - [x] 수락할 경우 재고를 추가로 차감한다.
- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우,
일부 수량에 대해 정가로 결제하게 됨을 안내한다.
  - [x] 거절할 경우 해당 수량은 제외하고 결제한다.
- [x] 프로모션 적용 할인액수를 계산한다.


## 멤버십 할인

- [x] 멤버십 회원인지 확인한다.
- [x] 할인 액수를 계산한다.


## 영수증

- [x] 구매 상품 내역(구매한 상품명, 수량, 가격)을 기록한다.
- [x] 증정 상품 내역을 기록한다.
- [x] 금액 정보를 기록한다.



## 입력
- [x] 구매할 상품과 수량을 입력받다.
- [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 
그 수량만큼 추가 여부를 입력받는다.
- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 
일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
- [x] 멤버십 할인 적용 여부를 입력 받는다.
- [x] 추가 구매 여부를 입력 받는다.





## 입력 검증

- [x] 프로모션 상품 추가 여부, 정가 결제 여부, 멤버십 할인 여부,
추가 구매 여부는 "Y", "N" 만 입력할 수 있다.
- [x] 구매할 상품과 수량을 형식에 맞춰 입력해야 한다.
- [x] 보유하고 있는 상품만 입력할 수 있다.
- [x] 중복된 상품을 입력한 경우 예외처리
- [ ] 수량은 숫자만 입력할 수 있다.
- [x] 수량은 양수만 입력할 수 있다.
- [x] 재고(일반 + 프로모션) 보다 많이 입력할 수 없다.
- [x] null은 입력할 수 없다.

## 출력

- [x] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 
만약 재고가 0개라면 재고 없음을 출력한다.
- [x] 영수증을 출력한다.
- [x] 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException 를 발생시키고, 
"[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력받는다.

## 파일 검증

- [ ] 프로모션 재고 가격와 일반 재고 가격이 다를 경우
- [ ] 잘못된 파일 형식일 경우


## 주목할 만한 요구사항

---
- 3항 연산자를 쓰지 않는다.
- 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
- 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며,
다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
  - 고객 한 명이 구매하고 나면 프로그램이 종료되므로 휘발성 인메모리는 부적합하다. 따라서
"파일에 재고를 기록"해서 프로그램이 종료되어도 변경된 재고가 유지되도록 해야한다.


## 4주차 개인 학습 목표

---
- 테스트 코드를 좀 더 잘 작성하자
- 한 클래스에 너무 많은 책임을 부여하지 말자
- 출력 문구까지 상수화 하진 말자
- EnumMap을 사용하자
- 함수형 인터페이스 활용해보자
- 예외 처리를 좀 더 꼼꼼하게 하자
  - 큰 수를 고려하자
  - 중복 입력 예외처리 안 했다
- 4주차 미션이 끝나고 코드리뷰를 통해 새로운 지식 배우기


## 배운 내용

---
- infrastructure 계층
- repository 계층의 역할 (CRUD)
- DTO


## 미흡한 점

---
- 파일의 쓰기를 절대경로를 기준으로 하였는데 이를 상대경로로 바꿀 수 있는 방법을 찾아야 한다.
- 긴 메서드들과 클래스를 정리해야한다.
- 테스트 코드 미흡
- 시간이 부족해서 TDD를 제대로 하지 못했다.