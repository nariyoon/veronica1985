import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.schedulers.TimeInterval;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by nari.yoon on 2017-07-06.
 */
public class Chapter7 {

    public static void main(String[] args) {

        testTimeout();


        sleep(5000);
    }


    private class Confirmation {
        String name = "Confirmation";
    }


    private static void testScheduler() {
        TestScheduler sched = Schedulers.test();
        Observable<String> fast = Observable
                .interval(10, TimeUnit.MILLISECONDS, sched)
                .map(x -> "F" + x)
                .take(3);

        Observable<String> slow = Observable
                .interval(50, TimeUnit.MILLISECONDS, sched)
                .map(x -> "S" + x);

        Observable<String> stream = Observable.concat(fast, slow);
        stream.subscribe(System.out::print);

        System.out.println("subscribed");
    }

    private static Observable<LocalDate> nextSolarEclipse(LocalDate after) {
        return Observable.just(
                LocalDate.of(2016, Month.MARCH, 9),
                LocalDate.of(2016, Month.SEPTEMBER, 1),
                LocalDate.of(2017, Month.FEBRUARY, 26),
                LocalDate.of(2017, Month.AUGUST, 12),
                LocalDate.of(2018, Month.FEBRUARY, 15),
                LocalDate.of(2018, Month.JULY, 13),
                LocalDate.of(2018, Month.AUGUST, 11),
                LocalDate.of(2019, Month.JANUARY, 16),
                LocalDate.of(2019, Month.JULY, 2),
                LocalDate.of(2019, Month.DECEMBER, 26))
                .skipWhile(date -> !date.isAfter(after))
                .zipWith(
                        Observable.interval(500, 50, TimeUnit.MILLISECONDS),
                        (date, x) -> date);
    }


    private static void testSolar() {
        nextSolarEclipse(LocalDate.of(2016, Month.SEPTEMBER, 1))
                .timeout(() -> Observable.timer(1000, TimeUnit.MILLISECONDS),
                        date -> Observable.timer(100, TimeUnit.MILLISECONDS));
    }

    private static void testSolarTimeInterval() {
        Observable<TimeInterval<LocalDate>> intervals =
                nextSolarEclipse(LocalDate.of(2016, Month.SEPTEMBER, 1))
                        .timeInterval();
        intervals.subscribe(interval -> System.out.println(interval.getValue() + ", " + interval.getIntervalInMilliseconds()));
    }

    public static void testRetry() {
        Observable retry = Observable.timer(1, TimeUnit.SECONDS)
                .timeout(1, TimeUnit.SECONDS)
                .retry((attemp, e) ->
                        attemp <= 10 && !(e instanceof TimeoutException));

        Observable retryWhen01 = Observable.timer(1, TimeUnit.SECONDS)
                .timeout(1, TimeUnit.SECONDS)
                .retryWhen(failures -> failures.take(10));


        final int ATTEMPTS = 11;

        Observable retryWhen02 = Observable.timer(1, TimeUnit.SECONDS)
                .timeout(1, TimeUnit.SECONDS)
                .retryWhen(failures -> failures
                        .zipWith(Observable.range(1, ATTEMPTS), (error, attempt) ->
                                attempt < ATTEMPTS ?
                                        Observable.timer(1, TimeUnit.SECONDS) :
                                        Observable.error(error))
                        .flatMap(x -> x));

    }

    private static Observable<String> confirmation() {
        Observable<String> delayBeforeCompletion =
                Observable
                        .<String>empty()
                        .delay(200, TimeUnit.MILLISECONDS);
        return Observable.just("Test")
                .delay(100, TimeUnit.MILLISECONDS)
                .concatWith(delayBeforeCompletion);
    }

    private static void testTimeout() {
        confirmation()
                .timeout(190, TimeUnit.MILLISECONDS)
                .forEach(System.out::println,
                        th -> {
                            if (th instanceof TimeoutException) {
                                System.out.println("too long");
                            } else {
                                th.printStackTrace();
                            }
                        });
    }


    private static void testExcpeption() {
        Observable a = Observable.create(subscriber -> {
            try {
                subscriber.onNext(1 / 0);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

        Observable b = Observable.create(subscriber -> subscriber.onNext(1 / 0));
        Observable c = Observable.fromCallable(() -> 1 / 0);

        Observable.just(1, 0)
                .flatMap(x -> (x == 0) ?
                        Observable.error(new ArithmeticException("Zero : -(")) :
                        Observable.just(10 / x));

        c.subscribe(s -> System.out.println(s));
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
