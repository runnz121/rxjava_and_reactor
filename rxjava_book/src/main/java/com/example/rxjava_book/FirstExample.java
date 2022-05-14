package com.example.rxjava_book;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.util.concurrent.ListenableFutureCallbackRegistry;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.functions.Consumer;

public class FirstExample<T> {

	public void emit() {
		Observable.just("Hello", "RxJAva 2!!")
			.subscribe(System.out::println);// () -> System.out.println(data)
	}

	/**
	 * just() 사용
	 */
	public void emit1() {
		Observable.just(1,2,3,4,5,6)
			.subscribe(System.out::println);
	}

	/**
	 * create() 사용
	 */
	public void emit2() {
		Observable<Integer> source = Observable.create(
			(ObservableEmitter<Integer> emitter) -> {
				emitter.onNext(100);
				emitter.onNext(200);
				emitter.onNext(300);
				emitter.onComplete();
			});
		//만약 subscribe()을 호출하지 않으면 출력이 되지 않음(발행이 안됨)
		source.subscribe(System.out::println);
		//source.subscribe(data -> System.out.println(data));
	}

	/**
	 * 위의 메소드를 source 원형을 빼서 다시 작성
	 * 람다를 안쓰는 경우
	 * @param args
	 */
	public void emit3() {
		Observable<Integer> source = Observable.create(
			(ObservableEmitter<Integer> emitter) -> {
				emitter.onNext(100);
				emitter.onNext(200);
				emitter.onNext(300);
				emitter.onComplete();
			}
		);

		source.subscribe(new Consumer<Integer>() {
			@Override
			public void accept(Integer data) throws Throwable {
				System.out.println("Result : " + data);
			}
		});
	}

	/**
	 * fromArray 이용
	 */
	public void emit4() {
		Integer[] arr = {100, 200, 300};
		Observable<Integer> source = Observable.fromArray(arr);
		source.subscribe(System.out::println);
	}

	/**
	 * 위 메소드를 int[] 로 받을 경우
	 * Observable은 Integer로 받아야 함으로 stream을 통해 int -> Integer로 변환
	 * @param args
	 */
	public void emit5() {
		int[] arr = {100, 200, 300};
		Observable<Integer> source = Observable.fromArray(toIntegerArray(arr));
		source.subscribe(System.out::println);
	}

	//Intstream으로 int[] -> Integer[]로 변환
	private static Integer[] toIntegerArray(int[] intArray) {
		return IntStream.of(intArray).boxed().toArray(Integer[]::new);
	}

	/**
	 * fromIterable 이용
	 */
	public void emit6() {
		List<String> names = new ArrayList<>();
		BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
		names.add("Jerry");
		names.add("William");
		names.add("Bob");

		queue.add("que Jerry");
		queue.add("que william");
		queue.add("que Bob");

		Observable<String> source = Observable.fromIterable(names);
		Observable<String> source1 = Observable.fromIterable(queue);

		source.subscribe(s -> System.out.println(s));
		source1.subscribe(System.out::println);
	}

	/**
	 * fromCallable 이용
	 * @param args
	 */
	public void emit7() {
		Callable<String> callable = () -> {
			Thread.sleep(1000);
			return "Hello Callable";
		};

		Observable<String> source = Observable.fromCallable(callable);
		source.subscribe(System.out::println);
	}

	/**
	 * 위와 똑같은 표현식(위는 람다 아래는 오버라이드 처리)
	 * @param args
	 */
	public void emit7_1() {
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				Thread.sleep(1000);
				return "Hello Callable";
			}
		};
		Observable<String> source = Observable.fromCallable(callable);
		source.subscribe(System.out::println);
	}

	/**
	 * fromFuture 사용
	 * @param args
	 */
	public void emit8() {
		//여기서는 Callable 객체(future)가 생성됨
		//1초를 쉬고 반환
		Future<String> future = Executors.newSingleThreadExecutor().submit(() -> {
			Thread.sleep(1000);
			return "Hello Future";
		});

		Observable<String> source = Observable.fromFuture(future);
		source.subscribe(System.out::println);
	}

	/**
	 * fromPublisher 사용
	 * @param args
	 */
	public void emit9() {
		Publisher<String> publisher = (Subscriber<? super String> s) -> {
			s.onNext("Hello Observable.fromPublisher()");
			s.onComplete();
		};

		Observable<String> source = Observable.fromPublisher(publisher);
		source.subscribe(System.out::println);
	}

	/**
	 * 위와 같음(람다 사용 안함)
	 * @param args
	 */
	public void emit9_1() {
		Publisher<String> publisher = new Publisher<String>() {
			@Override
			public void subscribe(Subscriber<? super String> s) {
				s.onNext("Hello Observable.fromPublisher()");
				s.onComplete();
			}
		};
		Observable<String> source = Observable.fromPublisher(publisher);
		source.subscribe(System.out::println);
	}




	public static void main(String[] args) {
		FirstExample demo = new FirstExample();
		// demo.emit();
		// demo.emit1();
		// demo.emit2();
		// demo.emit3();
		// demo.emit4();
		// demo.emit5();
		// demo.emit6();
		// demo.emit7();
		// demo.emit7_1();
		//demo.emit8();
		demo.emit9();
	}
}
