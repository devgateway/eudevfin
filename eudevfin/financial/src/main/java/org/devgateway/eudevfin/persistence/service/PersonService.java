/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devgateway.eudevfin.persistence.service;

import java.util.List;

import org.devgateway.eudevfin.financial.Person;
import org.springframework.integration.annotation.Payload;

/**
 * The Service is used to create Person instance in database.
 * 
 * @author Amol Nayak
 * @author Gunnar Hillert
 * 
 */
public interface PersonService {

	/**
	 * Creates or updates a {@link Person} instance from the {@link Person} instance
	 * passed.
	 * 
	 * @param person
	 *            the created person instance, it will contain the generated
	 *            primary key and the formatted name.
	 * @return The persisted Entity
	 */
	Person updatePerson(Person person);

	/**
	 * 
	 * @return the matching {@link Person} record(s)
	 */
	@Payload("new java.util.Date()")
	List<Person> findPeople();

}
