package com.example.addressbook.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddressBookKafka {

	private Long id;
	private String name;
	private String phone;
	private String address;
	private String zipCode;
	private String generator = "";
	private String consumer = "";
}
