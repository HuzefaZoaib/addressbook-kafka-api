package com.example.addressbook.kafka.entity;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class AddressGenerator implements Iterable<String> {

    private static final String[] STREET_NAMES = {
        "Main St", "High St", "Maple Ave", "Park Ave", "Oak St", "Pine St", "Cedar St", "Elm St", "Washington St", "Lake St",
        "Hill St", "Walnut St", "Sunset Blvd", "Birch St", "Willow St", "2nd St", "3rd St", "4th St", "5th St", "1st Ave",
        "2nd Ave", "3rd Ave", "4th Ave", "5th Ave", "Broadway", "Market St", "Center St", "Ridge Rd", "Valley Rd", "Front St",
        "Madison St", "Jefferson St", "Lincoln St", "Adams St", "Jackson St", "Roosevelt St", "Franklin St", "Kennedy St", "Johnson St", "Grant St"
    };

    private static final String[] CITY_NAMES = {
        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
        "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte", "San Francisco", "Indianapolis", "Seattle", "Denver", "Washington",
        "Boston", "El Paso", "Nashville", "Detroit", "Oklahoma City", "Portland", "Las Vegas", "Memphis", "Louisville", "Baltimore",
        "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Mesa", "Sacramento", "Atlanta", "Kansas City", "Colorado Springs", "Miami"
    };

    private static final String[] STATE_ABBREVIATIONS = {
        "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
        "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
        "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
        "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
        "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
    };

    private static final int TOTAL = 10000;
    
    public static void main(String[] args) {
        AddressGenerator generator = new AddressGenerator();
        Iterator<String> iterator = generator.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new AddressIterator();
    }

    private class AddressIterator implements Iterator<String> {
    	
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
            return generateRandomAddress();
        }
    }

    private String generateRandomAddress() {
    	
        int streetNumber = 1 + ThreadLocalRandom.current().nextInt(9999); // Random street number between 1 and 9999
        String streetName = STREET_NAMES[ThreadLocalRandom.current().nextInt(STREET_NAMES.length)];
        String cityName = CITY_NAMES[ThreadLocalRandom.current().nextInt(CITY_NAMES.length)];
        String stateAbbreviation = STATE_ABBREVIATIONS[ThreadLocalRandom.current().nextInt(STATE_ABBREVIATIONS.length)];
        String zipCode = String.format("%05d", 10000 + ThreadLocalRandom.current().nextInt(90000)); // Random zip code between 10000 and 99999

        return String.format("%d %s, %s, %s %s", streetNumber, streetName, cityName, stateAbbreviation, zipCode);
    }
}
