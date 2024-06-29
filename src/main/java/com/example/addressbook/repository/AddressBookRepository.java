package com.example.addressbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.addressbook.entity.AddressBook;

@Repository
public interface AddressBookRepository extends CrudRepository<AddressBook, Long> {

}
