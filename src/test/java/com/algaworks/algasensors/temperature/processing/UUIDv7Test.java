package com.algaworks.algasensors.temperature.processing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UUIDv7Test {

    @Test
    void shouldGenerateUUIDv7() {
        UUID uuid1 = IdGenerator.generateTimeBasedUUID();
        UUID uuid2 = IdGenerator.generateTimeBasedUUID();
        UUID uuid3 = IdGenerator.generateTimeBasedUUID();
        UUID uuid4 = IdGenerator.generateTimeBasedUUID();

        System.out.println("uuid1: " + uuid1);
        System.out.println("uuid2: " + uuid2);
        System.out.println("uuid3: " + uuid3);
        System.out.println("uuid4: " + uuid4);

        //Extraíndo o timestamp do UUID v7
        System.out.println("Timestamp1: " + UUIDv7Utils.extractOffsetDateTime(uuid1));
        System.out.println("Timestamp2: " + UUIDv7Utils.extractOffsetDateTime(uuid2));
        System.out.println("Timestamp3: " + UUIDv7Utils.extractOffsetDateTime(uuid3));
        System.out.println("Timestamp4: " + UUIDv7Utils.extractOffsetDateTime(uuid4));

    }

    @Test
    void shouldGenerateUUIDv7Compareted() {
        UUID uuid1 = IdGenerator.generateTimeBasedUUID();
        System.out.println("uuid1: " + uuid1);
        //Extraíndo o timestamp do UUID v7
        System.out.println("Timestamp1: " + UUIDv7Utils.extractOffsetDateTime(uuid1));

        OffsetDateTime uuidDateTime = UUIDv7Utils.extractOffsetDateTime(uuid1).truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime curDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Assertions.assertEquals(uuidDateTime, curDateTime);
    }


}
