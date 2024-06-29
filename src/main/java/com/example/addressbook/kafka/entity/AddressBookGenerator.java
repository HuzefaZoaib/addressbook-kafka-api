package com.example.addressbook.kafka.entity;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AddressBookGenerator implements Iterable<AddressBookKafka> {

	private Iterator<String> addressIterator;
	
	private Iterator<String> zipCodeIterator;
	
	private Iterator<String> phoneNumberIterator;
	
	private Iterator<String> nameIterator;
	
	private static final AtomicLong ID = new AtomicLong(0);

	private static final int TOTAL = 10000;

	@Autowired
	public void setAddressGenerator(AddressGenerator generator) {
		addressIterator = generator.iterator();
	}

	@Autowired
	public void setNameGenerator(NameGenerator generator) {
		nameIterator = generator.iterator();
	}

	@Autowired
	public void setPhoneNumberGenerator(PhoneNumberGenerator generator) {
		phoneNumberIterator = generator.iterator();
	}

	@Autowired
	public void setZipCodeGenerator(ZipCodeGenerator generator) {
		zipCodeIterator = generator.iterator();
	}

	@Override
    public Iterator<AddressBookKafka> iterator() {
        return new AddressBookIterator();
    }
	
	private class AddressBookIterator implements Iterator<AddressBookKafka> {
    	
    	private static final AtomicInteger count = new AtomicInteger();

        @Override
        public boolean hasNext() {
            return count.get() < TOTAL;
        }

        @Override
        public AddressBookKafka next() {
            
        	if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            count.incrementAndGet();
            return generateRandomAddressBook();
        }
    }
	
	private AddressBookKafka generateRandomAddressBook() {
    	
		return AddressBookKafka.builder()
    			.id(ID.incrementAndGet())
    			.name(nameIterator.next())
    			.phone(phoneNumberIterator.next())
    			.address(addressIterator.next())
    			.zipCode(zipCodeIterator.next()).build();
    }
}
