package com.example.addressbook.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.example.addressbook.kafka.entity.AddressBookKafka;

@Configuration
public class KafkaProducerConfig {

	@Value("${KAFKA_BOOTSTRAP_SERVERS_CONFIG}")
	private String BOOTSTRAP_SERVERS_CONFIG;
	
	@Value("${KAFKA_KEY_SERIALIZER_CLASS_CONFIG}")
	private String KEY_SERIALIZER_CLASS_CONFIG;
	
	@Value("${KAFKA_VALUE_SERIALIZER_CLASS_CONFIG}")
	private String VALUE_SERIALIZER_CLASS_CONFIG;
	
	@Value("${KAFKA_ACKS_CONFIG}")
	private String ACKS_CONFIG;
	
	@Value("${KAFKA_COMPRESSION_TYPE_CONFIG}")
	private String COMPRESSION_TYPE_CONFIG;
	
	@Value("${KAFKA_BATCH_SIZE_CONFIG}")
	private String BATCH_SIZE_CONFIG;
	
	@Value("${KAFKA_LINGER_MS_CONFIG}")
	private String LINGER_MS_CONFIG;
	
	@Value("${KAFKA_TOPIC}")
	private String KAFKA_TOPIC;

	
	
	@Bean
	public Properties kafkaProducerProperties() {
        
		// Setup Properties for Kafka Producer
        Properties kafkaProps = new Properties();

        // List of brokers to connect to
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        		BOOTSTRAP_SERVERS_CONFIG);

        // Serializer class used to convert Keys to Byte Arrays
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        		KEY_SERIALIZER_CLASS_CONFIG);

        // Serializer class used to convert Messages to Byte Arrays
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        		VALUE_SERIALIZER_CLASS_CONFIG);

        // Set ACKS to all so all replicas needs to acknowlwedge
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG);

        // Set compression type to GZIP
        kafkaProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,
        		COMPRESSION_TYPE_CONFIG);

        // Batch to Push in one cyle
        kafkaProps.put(ProducerConfig.BATCH_SIZE_CONFIG,
        		BATCH_SIZE_CONFIG);

        // Accumulate message till MS before pushing to kafka
        kafkaProps.put(ProducerConfig.LINGER_MS_CONFIG,
        		LINGER_MS_CONFIG);

        return kafkaProps;
	}
	
	@DependsOn("kafkaProducerProperties")
	@Bean
	public Producer<Long,AddressBookKafka> kafkaProducer(@Autowired @Qualifier("kafkaProducerProperties") Properties kafkaProducerProperties) {
		
		// Create a Kafka producer from configuration
        return new KafkaProducer<Long,AddressBookKafka>(kafkaProducerProperties);
	}
}
