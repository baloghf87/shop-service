package hu.ferencbalogh.shopservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class MockClock extends Clock {

    private Clock clock;

    public MockClock(Clock clock) {
        this.clock = clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public ZoneId getZone() {
        return clock.getZone();
    }

    public Clock withZone(ZoneId zone) {
        return clock.withZone(zone);
    }

    public Instant instant() {
        return clock.instant();
    }

    @TestConfiguration
    @Profile("test")
    public static class Configuration {

        @Bean
        public Clock mockClock() {
            return new MockClock(Clock.fixed(Instant.now(), ZoneId.of("UTC")));
        }
    }

}
