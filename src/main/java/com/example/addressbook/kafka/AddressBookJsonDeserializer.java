package com.example.addressbook.kafka;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.addressbook.kafka.entity.AddressBookKafka;

public class AddressBookJsonDeserializer extends JsonDeserializer<AddressBookKafka> {

}
