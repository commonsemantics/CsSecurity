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
package org.commonsemantics.grails.security.model

import grails.validation.Validateable

import org.commonsemantics.grails.users.model.OpenId
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.utils.UserUtils

/**
* @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
*/
@Validateable
class UserAccountRequest { 

	private static final int NAME_MAX_SIZE = 255;
	
	String firstName
	String lastName
	String displayName
	String country
	String affiliation
	
	String id
	String email
	String username
	String password
	
	Date dateCreated, lastUpdated // Grails automatic timestamping
	boolean validated
	
	boolean moderated
	User moderatedBy
	boolean approved
	
	String userId
	
	static hasMany = [openIds: OpenId]

	static transients = ['name','status']
	
	String getStatus() {
		return UserUtils.getStatusLabel(this);
	}
	
	String getName() {
		return lastName + " " + firstName;
	}
	
	static constraints = {
		//Users' data
		firstName (blank: false, maxSize:NAME_MAX_SIZE)
		lastName (blank: false, maxSize:NAME_MAX_SIZE)
		displayName (blank: true, maxSize:NAME_MAX_SIZE)
		affiliation (blank: true, maxSize:NAME_MAX_SIZE)
		country (blank: true, maxSize:NAME_MAX_SIZE)
		validated (blank: false)
		
		username (blank: false, unique: true, minSize:4, maxSize:60)
		password blank: false
		email blank: false, unique: true, email: true
		
		userId nullable:true, blank:true
		moderatedBy nullable:true, blank:true
	}

	static mapping = {
		password column: '`password`'
		id generator:'uuid'
	}
}
