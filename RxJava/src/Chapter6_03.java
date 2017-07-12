import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

/**
 * Created by nari.yoon on 2017-07-06.
 */
public class Chapter6_03 {

    public static void main(String[] args) {

//        interval().subscribe(t-> System.out.println(t));

        durationTimer().connect();
        durationTimer().subscribe(t -> System.out.println(t));

        sleep(10000);
    }

    private static ConnectableObservable<Long> durationTimer() {

        return interval()
//                .doOnNext(t-> System.out.println(t))
                .retry()
                .publish();
    }

    private static Observable<Long> interval() {
        return Observable.interval(-1, 1, TimeUnit.NANOSECONDS);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
