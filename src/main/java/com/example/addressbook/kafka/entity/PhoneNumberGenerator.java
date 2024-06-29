package com.example.addressbook.kafka.entity;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class PhoneNumberGenerator implements Iterable<String> {

	private static final int TOTAL = 10000;

    @Override
    public Iterator<String> iterator() {
        return new PhoneNumberIterator();
    }
    
    private class PhoneNumberIterator implements Iterator<String> {
        
    	private static final AtomicInteger count = new AtomicInteger();

        @Override
        public boolean hasNext() {
            return count.get() < TOTAL;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            count.incrementAndGet();
            return generateRandomPhoneNumber();
        }
    }

    private String generateRandomPhoneNumber() {
        int areaCode = 100 + ThreadLocalRandom.current().nextInt(800); // Ensures the area code is between 100 and 899
        int centralOfficeCode = 100 + ThreadLocalRandom.current().nextInt(800); // Ensures the central office code is between 100 and 899
        int lineNumber = 1000 + ThreadLocalRandom.current().nextInt(9000); // Ensures the line number is between 1000 and 9999

        return String.format("(%03d) %03d-%04d", areaCode, centralOfficeCode, lineNumber);
    }
}
