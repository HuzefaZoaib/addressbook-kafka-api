package com.example.addressbook.kafka.consumergroup;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.addressbook.kafka.entity.AddressBookKafka;

@Service
public class AddressBookKafkaConsumer3 extends AddressBookKafkaAbstractConsumer {

	/*
	 * Multiple thread cannot access the consume
	 * because Consumer will throw Exception, 
	 * if access by multiple thread at once.
	 * Therefore lastOffset is also bind to the
	 * consumer, in a way.
	 */
	
	@SuppressWarnings("unchecked")
	public AddressBookKafkaConsumer3(@Autowired ApplicationContext springApplicationContext) {
		// One consumer for the Consumer Group will be bind to this Singleton Service Instance
		consumer = (Consumer<Long, AddressBookKafka>)springApplicationContext.getBean("addressBookKafkaConsumer");
	}

	private final Consumer<Long, AddressBookKafka> consumer;
	
	protected Consumer<Long, AddressBookKafka> getConsumer() {
		return this.consumer;
	}	
}
