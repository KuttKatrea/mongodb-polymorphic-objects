package net.katrea.polymorphic.basic;

import java.io.IOException;
import java.time.LocalDate;

import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class BasicExample {
	private static final Logger logger = LoggerFactory.getLogger(BasicExample.class);
	
	@JsonTypeInfo(property="type", use=Id.NAME, include=As.EXISTING_PROPERTY, visible=true)
	@JsonSubTypes({
		@JsonSubTypes.Type(name="SSN", value=SsnIdentityDocument.class),
		@JsonSubTypes.Type(name="INE", value=IneIdentityDocument.class),
		@JsonSubTypes.Type(name="PASSPORT", value=PassportIdentityDocument.class)
	})
	interface BaseIdentityDocument {
		@JsonProperty("type")
		String getType();
		
		@JsonProperty("number")
		String getNumber();
	}
	
	interface DatedIdentityDocument extends BaseIdentityDocument {
		@JsonProperty("issuing_date")
		LocalDate getIssuingDate();
		
		@JsonProperty("expiration_date")
		LocalDate getExpirationDate();
	}
	
	@Value.Immutable
	@JsonSerialize(as=ImmutableSsnIdentityDocument.class)
	@JsonDeserialize(as=ImmutableSsnIdentityDocument.class)
	abstract static class SsnIdentityDocument implements BaseIdentityDocument {
		@Value.Default
		public String getType() {
			return "SSN";
		}
	}
	
	@Value.Immutable
	@JsonSerialize(as=ImmutableIneIdentityDocument.class)
	@JsonDeserialize(as=ImmutableIneIdentityDocument.class)
	interface IneIdentityDocument extends DatedIdentityDocument {}
	
	@Value.Immutable
	@JsonSerialize(as=ImmutablePassportIdentityDocument.class)
	@JsonDeserialize(as=ImmutablePassportIdentityDocument.class)
	interface PassportIdentityDocument extends DatedIdentityDocument {}
	
	public static void main(String[] args) throws IOException {
		
		ImmutableSsnIdentityDocument ssn = ImmutableSsnIdentityDocument.builder().number("12345678").build();
		
		logger.info(ssn.toString());
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		logger.info(
			mapper.writeValueAsString(ssn)
		);
		
		BaseIdentityDocument document = mapper.readValue(
				"{\"type\":\"SSN\",\"number\":\"87654321\"}",
				BaseIdentityDocument.class
			);
		
		logger.info("Document: {}", document);
		
		
		document = mapper.readValue(
				"{\"type\":\"INE\",\"number\":\"87654321\", \"issuing_date\": \"2015-02-02\", \"expiration_date\": \"2015-02-02\"}",
				BaseIdentityDocument.class
			);
		
		logger.info("Document: {}", document);
		
	}
}
