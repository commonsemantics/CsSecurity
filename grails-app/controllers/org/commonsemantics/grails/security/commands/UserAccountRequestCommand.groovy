/*
 * Copyright 2013 Common Semantics (commonsemantics.org)
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.commonsemantics.grails.security.commands

import grails.validation.Validateable

import org.commonsemantics.grails.security.model.UserAccountRequest
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.utils.UserStatus

/**
* Object command for User validation and creation.
*
* @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
*/
@Validateable
class UserAccountRequestCommand {

	def springSecurityService;
	
	public static final Integer NAME_MAX_SIZE = 255;
	
	// Users status values
	//---------------------
	String status
	
	String firstName
	String lastName
	String displayName
	String affiliation
	String country
	
	String email
	String username
	String password
	String passwordConfirmation
	
	static constraints = {
		//Users' data
		firstName (blank: false, maxSize:NAME_MAX_SIZE)
		lastName (blank: false, maxSize:NAME_MAX_SIZE)
		displayName (blank: false, maxSize:NAME_MAX_SIZE)
		email (blank: false, email: true,  maxSize:NAME_MAX_SIZE)
		affiliation (blank: false, maxSize:NAME_MAX_SIZE)
		country (blank: false, maxSize:NAME_MAX_SIZE)
		//Account credentials
		username (blank: false, unique: true, minSize:4, maxSize:60)
		password (blank: false, minSize:6, maxSize:NAME_MAX_SIZE)
		passwordConfirmation (blank: false, minSize:6, maxSize:NAME_MAX_SIZE)
	}
	
	boolean isEnabled() {
		return status.equals(UserStatus.ACTIVE_USER.value());
	}
	
	boolean isLocked() {
		return status.equals(UserStatus.LOCKED_USER.value());
	}
	
	boolean isPasswordValid() {
		log.debug("Checking password validity")
		return password.equals(passwordConfirmation);
	}	
	
	boolean isUsernameAvailable() {
		log.debug("Checking username (" + username + ") availability in users and account requests");
		return ((User.findByUsername(username)!=null || UserAccountRequest.findByUsername(username)!=null)? false : true);
	}
	
	boolean isEmailAvailable() {
		log.debug("Checking email (" + email + ") availability in users and account requests");
		return ((User.findByEmail(email)!=null || UserAccountRequest.findByEmail(email)!=null) ? false : true);
	}
	
	UserAccountRequest createAccountRequest() {
		log.debug("Creating account request")
		return UserAccountRequest.findByEmail(email) ? null:
			new UserAccountRequest(firstName: firstName, lastName: lastName, displayName: displayName, username: username, 
				validated:false, email: email, country: country, affiliation: affiliation, password: springSecurityService.encodePassword(password), enabled:isEnabled())
	}
	
}
