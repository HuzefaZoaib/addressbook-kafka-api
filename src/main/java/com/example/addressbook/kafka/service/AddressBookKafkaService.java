package com.example.addressbook.kafka.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.addressbook.kafka.AddressBookKafkaProducer;
import com.example.addressbook.kafka.consumergroup.AddressBookKafkaConsumer1;
import com.example.addressbook.kafka.consumergroup.AddressBookKafkaConsumer2;
import com.example.addressbook.kafka.consumergroup.AddressBookKafkaConsumer3;
import com.example.addressbook.kafka.entity.AddressBookGenerator;
import com.example.addressbook.kafka.entity.AddressBookKafka;

@Service
public class AddressBookKafkaService {
	
	@Autowired
	private AddressBookGenerator addressBookGenerator;
	
	@Autowired
	private AddressBookKafkaProducer kafkaProducer;

	@Autowired
	private AddressBookKafkaConsumer1 addressBookKafkaConsumer1;

	@Autowired
	private AddressBookKafkaConsumer2 addressBookKafkaConsumer2;

	@Autowired
	private AddressBookKafkaConsumer3 addressBookKafkaConsumer3;

	private static final int TOTAL = 10000;
	
	public synchronized void generateAndPushToKafka() {

		long startTime = System.currentTimeMillis();
		// this call "this.addressBookGenerator.iterator()" is not re-triggering the messages.
		// Because, iterators inside the addressBookGenerator has already been consumed.
		// Need to device a logic so that they also be refreshed.
		// The simplest way is just get the addressBookGenerator as prototypical instance will
		// solve the problem.
		Iterator<AddressBookKafka> addressBookIterator = this.addressBookGenerator.iterator(); 
		CountDownLatch countDownLatch = new CountDownLatch(TOTAL);
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		// Submitting 10,000 Task to be executed by 8 Threads in parallel
		for(int i=0; i<TOTAL; i++) {
			threadPool.execute(new Task(countDownLatch, addressBookIterator));
		}
		
		long endTime = System.currentTimeMillis();
		try {
			countDownLatch.await(5, TimeUnit.MINUTES);
			endTime = System.currentTimeMillis();
			
			shutdownAndAwaitTermination(threadPool);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Shutdown Successfully, Address Booked Pushed to Kafka. Time Taken is " + (Math.round((endTime-startTime)/1000) + " seconds."));
	}
	
	private void shutdownAndAwaitTermination(ExecutorService pool) {
	   pool.shutdown(); // Disable new tasks from being submitted
	   try {
	     // Wait a while for existing tasks to terminate
	     if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
	       pool.shutdownNow(); // Cancel currently executing tasks
	       // Wait a while for tasks to respond to being cancelled
	       if (!pool.awaitTermination(60, TimeUnit.SECONDS))
	           System.err.println("Pool did not terminate");
	     }
	   } catch (InterruptedException ex) {
	     // (Re-)Cancel if current thread also interrupted
	     pool.shutdownNow();
	     // Preserve interrupt status
	     Thread.currentThread().interrupt();
	   }
	}
	
	public Iterator<AddressBookKafka> getAddressBookGenerator() {
		return addressBookGenerator.iterator();
	}
	
	private class Task implements Runnable {

		private final CountDownLatch countDownLatch;
		private final Iterator<AddressBookKafka> addressBookIterator;
		public Task(CountDownLatch countDownLatch, Iterator<AddressBookKafka> addressBookIterator) {
			this.addressBookIterator = addressBookIterator;
			this.countDownLatch = countDownLatch;
		}
		
		@Override
		public void run() {
			
			try {
				// Make it every every a bit slow
				try { Thread.sleep(100); } catch(InterruptedException ex) {}
			
			
				if(addressBookIterator.hasNext()) {
					AddressBookKafka addressBook = addressBookIterator.next();
					addressBook.setGenerator(Thread.currentThread().getName());
					kafkaProducer.push(addressBook);
					//System.out.println(addressBook);
				}
			} finally {
				this.countDownLatch.countDown();
			}
		}
	}
	
	
	public List<AddressBookKafka> consume() throws InterruptedException, ExecutionException {
		
		int totalThreads = 3;
		List<AddressBookKafka> addressBooks = new ArrayList<>(totalThreads*10);
		ExecutorService exeService = Executors.newFixedThreadPool(totalThreads); 
		try {
			List<Future<List<AddressBookKafka>>> results = new ArrayList<>(totalThreads);
			results.add(exeService.submit(() -> addressBookKafkaConsumer1.consume()));
			//results.add(exeService.submit(() -> addressBookKafkaConsumer2.consume()));
			//results.add(exeService.submit(() -> addressBookKafkaConsumer3.consume()));
			
			while(results.size() > 0) {
				for(Iterator<Future<List<AddressBookKafka>>> items=results.iterator(); items.hasNext();) {
					Future<List<AddressBookKafka>> item = items.next();
					if(item.isDone()) {
						addressBooks.addAll(item.get());
						items.remove();
					}
				}
			}
		} finally {
			shutdownAndAwaitTermination(exeService);
		}
		
		return addressBooks;
	}
}
