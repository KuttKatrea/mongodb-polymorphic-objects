package net.katrea.polymorphic.basic;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;

import org.immutables.value.Value;
import org.mongojack.JacksonDBCollection;
import org.mongojack.internal.MongoJackModule;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class BasicUsage {
	@Value.Immutable
	@JsonSerialize(as = ImmutableCustomer.class)
	@JsonDeserialize(as = ImmutableCustomer.class)
	interface Customer {
		@JsonProperty("_id")
		int getId();

		@JsonProperty("first_name")
		String getFirstName();

		@JsonProperty("last_name")
		String getLastName();

		@JsonProperty("birth_date")
		LocalDate getBirthDate();

		@JsonProperty("created_at")
		ZonedDateTime getCreatedAt();

		@JsonProperty("modified_at")
		ZonedDateTime getModifiedAt();
	}
	
	private static ObjectMapper getJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		mapper.registerModule(new JavaTimeModule());
		
		return mapper;
	}
	
	private static JacksonDBCollection<Customer, Object> getMongoJackCollection() {
		MongoClient mongoClient = new MongoClient("localhost:27017");
		DB mongoDB = mongoClient.getDB("polymorphicdemo");
		DBCollection mongoCollection = mongoDB.getCollection("basic");
		
		ObjectMapper mapper = getJsonMapper();
		mapper.registerModule(new MongoJackModule());

		JacksonDBCollection<Customer, Object> mongojackCollection =
				JacksonDBCollection.wrap(mongoCollection, Customer.class, Object.class, mapper);
		
		return mongojackCollection;
	}

	public static void main(String args[]) throws JsonProcessingException {
		
		Customer john = ImmutableCustomer.builder()
				.id(1)
				.firstName("John")
				.lastName("Smith")
				.birthDate(LocalDate.of(1980, Month.AUGUST, 25))
				.createdAt(ZonedDateTime.now())
				.modifiedAt(ZonedDateTime.now())
				.build();
		
		System.out.println(john.toString());
		
		ObjectMapper jsonMapper = getJsonMapper();
		
		String jsonString = jsonMapper.writeValueAsString(john);
		
		System.out.println(jsonString);
		
		JacksonDBCollection<Customer, Object> collection = getMongoJackCollection();
		
		collection.save(john);
		
		Customer johnDB = collection.findOneById(1);
		
		System.out.println(johnDB.toString());
	}
}