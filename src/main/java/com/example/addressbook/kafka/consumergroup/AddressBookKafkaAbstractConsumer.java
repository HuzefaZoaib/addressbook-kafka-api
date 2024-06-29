package com.example.addressbook.kafka.consumergroup;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.example.addressbook.kafka.entity.AddressBookKafka;

public abstract class AddressBookKafkaAbstractConsumer {

	/*
	 * Multiple thread cannot access the consume
	 * because Consumer will throw Exception, 
	 * if access by multiple thread at once.
	 * Therefore lastOffset is also bind to the
	 * consumer, in a way.
	 * 
	 * Child Class class has to maintain Single Consumer in a Single Service
	 */
	
	protected abstract Consumer<Long, AddressBookKafka> getConsumer();
	
	public List<AddressBookKafka> consume() {
		
		Consumer<Long, AddressBookKafka> consumer = getConsumer();
		
		List<AddressBookKafka> addressBookKafkas = new ArrayList<>();

        int recCount=0;

    	System.out.print("What is assignment: ");
    	System.out.println(consumer.assignment());
        // As far as assignment is Empty, it is doing Handshaking and Re-balancing
        while(consumer.assignment().isEmpty()) {
			consumer.poll(Duration.ofMillis(1000));
			System.out.println("Is it stuck in assignment ....... ");
		}
			        
        //boolean isForLoopDone = false;
        ConsumerRecords<Long, AddressBookKafka> messages = consumer.poll(Duration.ofMillis(1000));
        System.out.println("-Message Empty : " + messages.isEmpty() +"- Class: " +this.getClass().getCanonicalName());

        for (ConsumerRecord<Long, AddressBookKafka> message : messages) {
        	
            System.out.println("Message fetched : " + message);
            recCount++;
            
            message.value().setConsumer(Thread.currentThread().getName());
            addressBookKafkas.add(message.value());
            
            /**********************************************************************
             *                  Do Manual commit asynchronously
             **********************************************************************/
            if (recCount % 10 == 0) {
                //Commit Async
                consumer.commitAsync();
            }
        }
	                
        return addressBookKafkas;
	}
}
