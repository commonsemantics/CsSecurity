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
package org.commonsemantics.grails.security

import org.commonsemantics.grails.security.commands.UserAccountRequestCommand
import org.commonsemantics.grails.security.model.UserAccountRequest
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.model.UserRole
import org.commonsemantics.grails.users.utils.DefaultUsersRoles

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class UserAccuntRequestController {

	def springSecurityService;
	def securityEmailingService;
	def userAccountRequestService;
	
	protected def injectUserProfile() {
		def principal = springSecurityService.principal
		if(principal.equals("anonymousUser")) {
			redirect(controller: "login", action: "index");
		} else {
			String username = principal.username
			def user = User.findByUsername(username);
			if(user==null) {
				log.error "Error:User not found for username: " + username
				render (view:'error', model:[message: "User not found for username: "+username]);
			}
			user
		}
	}
	
	def submitAccountRequest = {
		UserAccountRequestCommand userSignupCommand->
	        def appBase = request.getContextPath();
			if(userSignupCommand.hasErrors()) {
				println 'errors'
				userSignupCommand.errors.allErrors.each { println it }
				render(view:'/public/signup', model:[item:userSignupCommand])
			} else {
				if(userSignupCommand.isPasswordValid()) {
					if(userSignupCommand.isUsernameAvailable()) {
						if(userSignupCommand.isEmailAvailable()) {
							def accountRequest = userSignupCommand.createAccountRequest()
							if(accountRequest)  {
								if(!accountRequest.save(flush:true)) {
									// Failure in saving
									println 'failure in saving'
									accountRequest.errors.allErrors.each { println it }
									render(view:'signup', model:[item:userSignupCommand, 
												msgError: 'The request has not been saved successfully'])
								} else {
									println 'success in saving'
									try {
	                                    securityEmailingService.sendAccountRequestConfirmation(appBase, userSignupCommand);
	                                    securityEmailingService.sendApprovalRequest(appBase, userSignupCommand);
									} catch(Exception e) {
										log.error(e.getMessage());
									}
									redirect (action:'signupConfirmation',id: accountRequest.id, model: [
												msgSuccess: 'Signup completed successfully!']);
								}
							} else {
								// User already existing
								println ' User already existing'
								render(view:'/public/signup', model:[item:userSignupCommand, errorCode: "2"])
							}
						} else {
							println 'Email in use ' + userSignupCommand.email
							render(view:'/public/signup', model:[item:userSignupCommand, errorCode: "3"])
						}
					} else {
						println 'Username in use'
						render(view:'/public/signup', model:[item:userSignupCommand, errorCode: "2"])
					}
				} else {
					render(view:'/public/signup', model:[item:userSignupCommand, errorCode: "1"])
				}
		}
	}
	
	def moderateUserAccountsRequests = {
		def user = injectUserProfile()
		
		if (!params.max) params.max = 10
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "username"
		if (!params.order) params.order = "asc"
	
		def users = userAccountRequestService.moderateAccountRequests(user, params.max, params.offset, params.sort, params.order);
		render (view:'/dashboard/moderateUserAccountRequests', model:[user: user, "users" : users, "accountRequestsTotal": UserAccountRequest.count(), "usersroles": UserRole.list(), "roles" : Role.list(), "menuitem" : "moderateUserAccountRequests"])
	}
	
	def signupConfirmation = {
		render(view:'/shared/notification', model:[ title: 'Signup completed successfully!',
			message: ' Check your email, you should have received a confirmation email. <br/>If you haven\'t, it might be still ok, your request should be now awaiting for approval. <br/>Thank you for signing up!'])	
	}

	def approveAccountRequest = {
		def appBase = request.getContextPath();
		def user = injectUserProfile()
		def accountRequest = UserAccountRequest.findById(params.accountRequest);
		if(accountRequest) {
			User newUser = new User(firstName:accountRequest.firstName, lastName:accountRequest.lastName,
				displayName:accountRequest.displayName, country:accountRequest.country,
				affiliation:accountRequest.affiliation, username:accountRequest.username,
				password:accountRequest.password, email:accountRequest.email, enabled: 'true');
			if(!newUser.save(flush:true)) {
				newUser.errors.allErrors.each { render it }
			} else {
				accountRequest.moderated=true;
				accountRequest.moderatedBy=user;
				accountRequest.approved=true;
				accountRequest.userId=newUser.id;
				updateUserRole(newUser, Role.findByAuthority(DefaultUsersRoles.USER.value()), 'on')
				
				try {
					securityEmailingService.sendAccountConfirmation(appBase, accountRequest)
				} catch(Exception e) {
					log.error(e.getMessage());
				}
				redirect(controller:'dashboard', action:'showUser', params:[id: newUser.id])
				//render (view:'/administrator/showUser', model:[user: newUser]);
			}
		}
	}
	
	protected def updateUserRole(def user, def role, def value) {
		if(value=='on') {
			UserRole ur = UserRole.create(user, role)
			ur.save(flush:true)
		} else {
			def ur = UserRole.findByUserAndRole(user, role)
			if(ur!=null) {
				ur.delete(flush:true)
			}
		}
	}
	
	def pastAccountsRequests = {
		def user = injectUserProfile()
		
		if (!params.max) params.max = 10
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "username"
		if (!params.order) params.order = "asc"
	
		def users = userAccountRequestService.pastAccountRequests(user, params.max, params.offset, params.sort, params.order);
	
		render (view:'/dashboard/pastAccountRequests', model:[user: user, "users" : users, "accountRequestsTotal": UserAccountRequest.count(), "usersroles": UserRole.list(), "roles" : Role.list(), "menuitem" : "pastUserAccountRequests"])
	}
	
	def declineAccountRequest = {
		def user = injectUserProfile()
		def accountRequest = UserAccountRequest.findById(params.accountRequest);
		if(accountRequest) {
			accountRequest.moderated=true;
			accountRequest.moderatedBy=user;
			accountRequest.approved=false;
			redirect (action: 'moderateUserAccountsRequests');
		}
	}
	

	/*
	
	
	def editAccountRequest = {
		def accountRequest = UserAccountRequest.findById(params.accountRequest);
		if(accountRequest) {
			render (view:'editAccountRequest', model:[item: accountRequest]);
		}
	}
	
	def updateAccountRequest = { AccountRequestEditCommand accountRequestEditCommand->
		if(accountRequestEditCommand.hasErrors()) {
			accountRequestEditCommand.errors.allErrors.each { println it }
			render(view:'editAccountRequest', model:[item:accountRequestEditCommand])
		} else {
			def user = injectUserProfile()
			def accountRequest = AccountRequest.findById(accountRequestEditCommand.id);
			if(accountRequest) {
				accountRequest.firstName = accountRequestEditCommand.firstName
				accountRequest.lastName = accountRequestEditCommand.lastName
				accountRequest.displayName = accountRequestEditCommand.displayName
				accountRequest.email = accountRequestEditCommand.email
				accountRequest.affiliation = accountRequestEditCommand.affiliation
				accountRequest.country = accountRequestEditCommand.country
			}
			render(view:'editAccountRequest', model:[item:accountRequestEditCommand])
		}
	}
	*/
}
