package com.example.addressbook.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import com.example.addressbook.kafka.entity.AddressBookKafka;

@Configuration
public class KafkaConsumerConfig {

	@Value("${KAFKA_BOOTSTRAP_SERVERS_CONFIG}")
	private String BOOTSTRAP_SERVERS_CONFIG;
	
	@Value("${KAFKA_KEY_DESERIALIZER_CLASS_CONFIG}")
	private String KEY_DESERIALIZER_CLASS_CONFIG;
	
	@Value("${KAFKA_VALUE_DESERIALIZER_CLASS_CONFIG}")
	private String VALUE_DESERIALIZER_CLASS_CONFIG;
	
	@Value("${KAFKA_CONSUMER_GROUP_ID_CONFIG}")
	private String GROUP_ID_CONFIG;
	
	@Value("${KAFKA_AUTO_OFFSET_RESET_CONFIG}")
	private String AUTO_OFFSET_RESET_CONFIG; 
	
	@Value("${KAFKA_FETCH_MIN_BYTES_CONFIG}")
	private Integer FETCH_MIN_BYTES_CONFIG;
	
	@Value("${KAFKA_FETCH_MAX_WAIT_MS_CONFIG}")
	private Integer FETCH_MAX_WAIT_MS_CONFIG;
	
	@Value("${KAFKA_MAX_PARTITION_FETCH_BYTES_CONFIG}")
	private Integer MAX_PARTITION_FETCH_BYTES_CONFIG;
	
	@Value("${KAFKA_ENABLE_AUTO_COMMIT_CONFIG}")
	private Boolean ENABLE_AUTO_COMMIT_CONFIG;

	@Value("${KAFKA_MAX_POLL_RECORDS_CONFIG}")
	private String MAX_POLL_RECORDS_CONFIG;

	@Value("${KAFKA_MAX_POLL_INTERVAL_MS_CONFIG}")
	private String MAX_POLL_INTERVAL_MS_CONFIG; 
	
	@Value("${KAFKA_TOPIC}")
	private String KAFKA_TOPIC;
	
	@Bean
	public Properties kafkaConsumerProperties() {
	    
		// Setup Properties for consumer
        Properties kafkaProps = new Properties();

        // List of Kafka brokers to connect to
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        		BOOTSTRAP_SERVERS_CONFIG);

        // Deserializer class to convert Keys from Byte Array to String
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        		KEY_DESERIALIZER_CLASS_CONFIG);

        // Deserializer class to convert Messages from Byte Array to String
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        		VALUE_DESERIALIZER_CLASS_CONFIG);

        // Consumer Group ID for this consumer
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG,
        		GROUP_ID_CONFIG);

        // Set to consume from the earliest message, on start when no offset is
        // available in Kafka
        kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        		AUTO_OFFSET_RESET_CONFIG);

        /**********************************************************************
         *                  Set Batching Parameters
         **********************************************************************/

        // Set min bytes to 10 bytes
        kafkaProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, FETCH_MIN_BYTES_CONFIG);

        // Set max wait timeout to 100 ms
        kafkaProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, FETCH_MAX_WAIT_MS_CONFIG);

        // Set max fetch size per partition to 2 MB. Note that this will depend on total
        // memory available to the process
        //kafkaProps.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, MAX_PARTITION_FETCH_BYTES_CONFIG );

        kafkaProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS_CONFIG );
        kafkaProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS_CONFIG );	// 5min

        
        /**********************************************************************
         *                  Set Autocommit Parameters
         **********************************************************************/

        // Set auto commit to false
        kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT_CONFIG);
	
        return kafkaProps;
	}
	
	@DependsOn("kafkaConsumerProperties")
	@Bean
	@Scope("prototype")
	public Consumer<Long, AddressBookKafka> addressBookKafkaConsumer(
			@Autowired @Qualifier("kafkaConsumerProperties") Properties kafkaConsumerProperties) {
		
		
        //Create a Consumer
        KafkaConsumer<Long, AddressBookKafka> consumer =
                new KafkaConsumer<Long, AddressBookKafka>(kafkaConsumerProperties);

        
        // Subscribe to the topic
        consumer.subscribe(Arrays.asList(KAFKA_TOPIC));

        return consumer;
	}
}
