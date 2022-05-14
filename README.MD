# Chapter 1

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


## Single 클래스
Single 클래스는 1개의 데이터만 발행하도록 한정함(결과가 유일한 서버에 사용)
발행과 동시에 종료됨(onSuccess) // 라이프 사이클 관점에서 onNext() + onComplete()가 통합됨

### just() 함수
기존의 observable 과 비슷한 방법으로 활용 -> single 객체 생성 

### single.fromObservable()
첫번쨰 값 발행후 onSuccess 이벤트 발행 후 종료

### single(default value)
single 내부에 기본 값을 갖음 observable에서 발행이 되지 않을 떄도 인자를 넣은 기본값을 대신 발행

### fromArray(array).first()
first() 를 호출시 single 객체로 변환됨
첫번째 데이터 발행후 onSuccess 이벤트 발생 종료

### empty()
empty()함수를 통해서 onSuccess 이벤트 발생
Defautl value 를 설정할 수 있음

## Maybe 클래스
최대 데이터 하나를 가질 수 있지만, 데이터 발행 없이 바로 데이터 발생 완료 할 수 있음
Single 클래스에 onComplete 이벤트가 추가됨

## 뜨거운 Observable
차가운 Observable은 subscribe() 함수를 호출하여 구독하지 않으면 데이터를 발행하지 않음(게으른 접근법)
반면
뜨거운 Observable은 구독자 존재 여부와 관계 없이 데이터를 발행 -> 여러 구독자를 고려할 수 있음 

차가운 Observable : 구독자가 구독하면 준비된 데이터를 '처음 부터 발행' (ex: 웹 요청, DB 쿼리 읽기)
뜨거운 Observable : 구독자가 구독하면 구독한 시점부터의 데이터를 발행받음 (ex: 마우스 이벤트, 키보드 이벤트 등)
배압 : Observable 에서 데이터를 발행하느느 속도와 구독자가 처리하는 속도의 차가 클 떄 발행

## Subject 클래스
차가운 Observable 를 뜨거운 Observable로 바궈줌

### AsyncSubject 클래스
Observable 에서 발행한 마지막 데이터를 얻어올 수 있는 subject 클래스
완료되기 전 마지막 데이터에만 관심이 있으며, 이전데이터는 무시

### BehaviorSubject 클래스
구독을 하면 가장 최근 값 혹은 기본값을 넘겨주는 클래스 

### PublishSubject 클래스
subscribe() 함수 호출시 값을 발행하기 시작
해당 시간에 발생한 데이터를 그대로 구독자에게 전달 

### ReplaySubject 클래스
구독자가 생기면 항상 데이터의 처음부터 끝까지 발행하는 것을 보장해 줌
모둔 데이터 내용을 저장해두는 과정 중 메모리 누수가 발생할 가능성이 있음
뜨거움을 사용하지만 차가움처럼 동작(처음 부터 전체 발행)

### ConnectableObservable 클래스
차가운 -> 뜨거운으로 변경(구독시점부터 발행)
subscribe() 를 호출해도 동작이 일어나지 않음
connect() 함수는 호출 시점부터 subscribe() 함수를 호출한 구독자에게 데이터 발행

무조건 connect() 함수를 호출해야 그때까지 구독했던 구독자 모두에게 데이터 발행
