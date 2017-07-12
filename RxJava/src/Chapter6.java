import rx.Observable;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by nari.yoon on 2017-06-26.
 */
public class Chapter6 {

    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);

    static private Boolean isBusinessHour() {
        ZoneId zone = ZoneId.of("Europe/warsaw");
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        LocalTime localTime = zdt.toLocalTime();
        return !localTime.isBefore(BUSINESS_START) && !localTime.isAfter(BUSINESS_END);
    }

    static Observable<Duration> insideBusinessHours = Observable
            .interval(1, SECONDS)
            .filter(x -> isBusinessHour())
            .map(x -> Duration.ofMillis(100));

    static Observable<Duration> outsideBusinessHours = Observable
            .interval(5, SECONDS)
            .filter(x -> !isBusinessHour())
            .map(x -> Duration.ofMillis(200));

    static Observable<Duration> openings = Observable.merge(insideBusinessHours, outsideBusinessHours);

    public static void main(String[] args) {
        System.out.print("chapter6 backpressure");

        openings.subscribe(s -> System.out.print(s));
    }
}
