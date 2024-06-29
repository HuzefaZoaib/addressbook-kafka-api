package com.example.addressbook.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.addressbook.kafka.entity.AddressBookKafka;

@Service
public class AddressBookKafkaProducer {

	@Value("${KAFKA_TOPIC}")
	private String topic;
	
	@Autowired
	private Producer<Long, AddressBookKafka> producer;
	
	public void push(AddressBookKafka addressBook) {
		
		// Publish 20 messages at 2 second intervals, with a random key
        try{
            // Create a producer Record
            ProducerRecord<Long, AddressBookKafka> kafkaRecord =
                    new ProducerRecord<Long, AddressBookKafka>(
                            this.topic,    				//Topic name
                            addressBook.getId(),        //Key for the message
                            addressBook         		//Message Content
                    );

            //System.out.println("Sending Message : "+ kafkaRecord.toString());

            // Publish to Kafka
            producer.send(kafkaRecord);
        }finally {
            //producer.close();
        }

		return;
	}	
}
