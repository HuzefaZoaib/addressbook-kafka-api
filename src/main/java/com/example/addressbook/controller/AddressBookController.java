package com.example.addressbook.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.addressbook.kafka.entity.AddressBookKafka;
import com.example.addressbook.kafka.service.AddressBookKafkaService;

@RestController
public class AddressBookController {

	@Autowired
	private AddressBookKafkaService service;

	@GetMapping("/")
	public ResponseEntity<Object> _default() {
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/load")
	public ResponseEntity<Object> loadAddressBookIntoKakfa() {
		
		System.out.println("Address Book Service is going to be started.");
		new Thread(() -> service.generateAndPushToKafka()).start();
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("/consume")
	public List<AddressBookKafka> getKafkaAddressess() throws InterruptedException, ExecutionException {
		return service.consume();
	}
}
