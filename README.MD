## Chapter 1

### 리엑티브 프로그래밍
1. 기존 pull 방식의 프로그래밍 개념을 push 방식으로 바꿈
   1. 기존의 방식은 데이터 변경시 pull 방식이였음
   2. 환경이 변하면 이벤트를 받아 동작(프로그램이 주도하는 것이 아님)
2. 함수형 지원을 받음

Observable : 데이터 변화가 발생하는 데이터 소스
just : Observable 선언 방식
subscribe : Observable를 구독함 이 함수를 호출해야 변화한 데이터를 구독자에게 발이
emit : 우리가 커스텀으로 만든 메소드이지만 일반적으로 emit 으로 호출함을 표현 

````
import rx.Observable;

public class FirstExample {

	public void emit() {
		Observable.just("Hello", "RxJAva 2!!")
			.subscribe(System.out::println);// () -> System.out.println(data)
	}

	public static void main(String[] args) {
		FirstExample demo = new FirstExample();
		demo.emit();
	}
}
````

학습 목표
1. Observable 클래스 명확히 이해하기(Hot, Cold 개념 확실히 이해하기)
2. map, filter, reduce의 흐름 이해하기,  함수 사용법을 확실히 이해
3. 생성연산자, 결합 연산자, 변환 연산자 등의 카테고리 주요함수 공부
4. 스케쥴러, subscribeOn(), observeOn() 함수 차이 이해하기
5. 디버깅, 흐름 제어 함수 익히기

### 공식 홈페이지(마블 다이어그램)
https://reactivex.io/documentation/ko/observable.html


### 책에서는 rxjava2를 사용하였지만 본인은 rxjava3을 사용하여 진행

Observer Class는 상황에 따라 3가지로 구분해서 사용
1. Observable
2. Maybe -> reduce(), firstElement() : 데이터가 발행될 수 있거나 발행되지 않고도 완료되는 경우
3. Flowable -> Observable 에서 데이터가 발행되는 속도가 구독자 처리 속도보다 빠르게 발생하는 배입
    이슈 대응

## 1. Observer 클래스
1. 옵서버 패턴 구현됨
2. 객체 상태 변화를 관찰하는 관찰자 목록을 객체에 등록
3. 상태 변화시 메서드 호출

### Observer 가 알림하는 3가지
1. onNext : 데이터 발행알림
2. onComplete: 모든 데이터 발행 완료를 알림, 딱 한번만 발생, 발생 후 onNext는 더이상 발생해선 안됨
3. 에러 발생알림, 알람시 onNext, onComplete 이벤트 발생안함

### just() 함수
이 함수 안에 넣은 데이터를 차례로 발행(observable 생성)
여러개 넣을 수 있음(10개, 그러나 모두 타입 같아야)

### subscribe() 함수, Disposalbe 객체
just() : 함수로 데이터 흐름 정의 후
subscribe() : 이 함수를 호출해야 실제로 데이터를 발행
dispose() : Observable 에게 더 이상 데이터 발행하지 않도록 구독 해지하는 함수
정상 종료시(onComplete()) 호출할 필요 없음 

### 선언형 vs 명령형 프로그래밍
rxJava : 선언형
선언형은 목표만 명시, 실행할 알고리즘은 명시하지 않음

### create() 함수
just()는 데이터를 인자로 넣으면 자동으로 알림 이벤트 발생
반면 create()는 onNext, onComplete, onError 알림을직접 호출

### fromArray() 함수
데이터가 여러개일 경우
fromXXX 함수 이용

### fromIterable() 함수
이터레이터 페턴으로 이루어짐
차례대로 하나씩 진행

### fromCallable() 함수
동시성 인터페이스 callable
비동기 실행 후 결과를 반환하는 call() 메서드를 정의

### fromFuture() 함수
Executor 인터페이스를 구현한 클래스에 Callable 객체를 인자로 넣어 Future 객체를 반환
get() 메서드 호출 시 Callable 객체에서 구현한 계산 결과가 나올 떄 까지 블록킹 됨

### fromPublisher() 함수
