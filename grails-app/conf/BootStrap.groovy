import org.commonsemantics.grails.groups.model.GroupPrivacy
import org.commonsemantics.grails.groups.model.GroupRole
import org.commonsemantics.grails.groups.model.GroupStatus
import org.commonsemantics.grails.groups.model.UserStatusInGroup
import org.commonsemantics.grails.groups.utils.DefaultGroupPrivacy
import org.commonsemantics.grails.groups.utils.DefaultGroupRoles
import org.commonsemantics.grails.groups.utils.DefaultGroupStatus
import org.commonsemantics.grails.groups.utils.DefaultUserStatusInGroup
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.model.UserRole
import org.commonsemantics.grails.users.utils.DefaultUsersRoles

class BootStrap {

	def grailsApplication
	def springSecurityService
	
    def init = { servletContext ->

		String password = springSecurityService.encodePassword('password')
		
		separator();
		log.info  '>> INITIALIZING DEFAULTS'
		separator();
		log.info  '** Users Roles'
		
		DefaultUsersRoles.values().each {
			log.info  '** ' + it.value()
			log.info  '** ' + Role.first();
			if(!Role.findByAuthority(it.value())) {
				new Role(authority: it.value(), ranking: it.ranking(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			}
		}
		
		separator();
		log.info  '** Groups Roles'
		DefaultGroupRoles.values().each {
			if(!GroupRole.findByAuthority(it.value())) {
				new GroupRole(authority: it.value(), ranking: it.ranking(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			}
		}
		
		separator();
		log.info  '** Groups Status'
		DefaultGroupStatus.values().each {
			if(!GroupStatus.findByValue(it.value())) {
				new GroupStatus(value: it.value(), uuid: it.uuid(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			}
		}
		
		separator();
		log.info  '** Groups Privacy'
		DefaultGroupPrivacy.values().each {
			if(!GroupPrivacy.findByValue(it.value())) {
				new GroupPrivacy(value: it.value(), uuid: it.uuid(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			}
		}
		
		separator();
		log.info  '** User Status in Group'
		DefaultUserStatusInGroup.values().each {
			if(!UserStatusInGroup.findByValue(it.value())) {
				new UserStatusInGroup(value: it.value(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			}
		}
		
		separator();
		log.info  '>> USERS'
		separator();
		log.info  '** Users'
		
		def adminUsername = 'admin'
		log.info  "Initializing: " + adminUsername
		def admin = User.findByUsername(adminUsername);
		if(admin==null) {
			admin = new User(username: adminUsername,
				password: password, firstName: 'Jack', lastName: 'White',
				displayName: 'Dr. White', enabled: true, email:'paolo.ciccarese@gmail.com').save(failOnError: true)
			log.warn  "CHANGE PASSWORD for: " + adminUsername + "!!!"
		}
		UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.USER.value())
		UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.MANAGER.value())
		UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.ADMIN.value())




//        def firstApp =
//            new SystemApi(name: "My First App", shortName: "myfirstapp", description: "My First Application",
//                    apikey: "0cbfa370-b73c-4e3a-ae46-582df284b7c3", enabled: true, accessToPublicData: true, createdBy: admin).save(flush: true)
//
//
//        def secondApp =
//            new SystemApi(name: "My Second App", shortName: "mysecondapp", description: "My Second Application",
//                    apikey: "80404495-7196-4879-8719-54c21f44a31a", enabled: true, accessToPublicData: true, createdBy: admin).save(flush: true)
//
//        def thirdApp =
//            new SystemApi(name: "My Third App", shortName: "mythirddapp", description: "My Third Application",
//                    apikey: "4c7f4d1c-8ac4-4e9f-84c8-b271c57fcac4", enabled: true, accessToPublicData: true, createdBy: admin).save(flush: true)

		if(grailsApplication.config.af.security.initialize.user=='true') { 
			def userUsername = 'user'
			log.info "Initializing: " + userUsername
			def user = User.findByUsername(userUsername);
			if(user==null) {
				user = new User(username: userUsername,
					password: password, firstName: 'John', lastName: 'Smith', 
					displayName: 'Dr. Smith', enabled: true, email:'yo@yo.com').save(failOnError: true)
				log.warn  "CHANGE PASSWORD for: " + userUsername + "!!!"
			}
			UserRole.create user, Role.findByAuthority(DefaultUsersRoles.USER.value())		
		}	
		
		separator();
		log.info  '>> GROUPS'

    }
	def separator = {
		log.info  '------------------------------------------------------------------------';
	}
    def destroy = {
    }
}
