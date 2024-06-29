package com.example.addressbook.kafka.entity;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class ZipCodeGenerator implements Iterable<String> {

    private static final int TOTAL = 10000;

    @Override
    public Iterator<String> iterator() {
        return new ZipCodeIterator();
    }    
    
    private class ZipCodeIterator implements Iterator<String> {
        
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
            return generateRandomZipCode();
        }
    }
    
    private String generateRandomZipCode() {
        int zipCode = 10000 + ThreadLocalRandom.current().nextInt(90000);
        return String.format("%05d", zipCode);
    }
}
