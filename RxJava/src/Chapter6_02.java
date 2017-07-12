import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

/**
 * Created by nari.yoon on 2017-06-27.
 */
public class Chapter6_02 {

    static class Dish {
        private final byte[] oneKb = new byte[1_024];
        private final int id;

        Dish(int id) {
            this.id = id;
            System.out.println("Created: " + id);
        }

        public String toString() {
            return String.valueOf(id);
        }
    }

    static Observable<Dish> dishes = Observable
            .range(1, 1_000_000_000)
            .map(Dish::new);

    static Observable<Integer> myRange(int from, int count) {
        return Observable.create(subscriber -> {
            int i = from;
            while (i < from + count) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(i++);
                } else {
                    return;
                }
            }
            subscriber.onCompleted();
        });
    }

    public static void main(String[] args) {

//        dishes
//                .observeOn(Schedulers.io())
//                .subscribe(x -> {
//                    System.out.println("Washing: " + x);
//                    sleepMillis(50);
//                }, Throwable::printStackTrace);

//        myRange(1, 1_000_000_000)
//                .map(Dish::new)
//                .observeOn(Schedulers.io())
//                .subscribe(x -> {
//                    System.out.println("Washing: " + x);
//                    sleepMillis(50);
//                }, Throwable::printStackTrace);

        Observable
                .range(1, 10)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onStart() {
                        request(1);
                    }

                    @Override
                    public void onCompleted() {

                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        request(1);
                        System.out.println("onNext: " + integer);
                    }
                });


        sleepMillis(60000);
    }

    private static void sleepMillis(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
