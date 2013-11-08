// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
	
	info   'grails.app', // Necessary for Bootstrap logging
		   'org.springframework.security'
}

// Spring Security Configuration
// The following have to be defined in the Config.groovy of the main application in order for Spring Security to work properly
// -------------------------------------------------------------------------------------------------------------------------------------------
grails.plugin.springsecurity.userLookup.userDomainClassName 			= 'org.commonsemantics.grails.users.model.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName 			= 'org.commonsemantics.grails.users.model.UserRole'
grails.plugin.springsecurity.authority.className 						= 'org.commonsemantics.grails.users.model.Role'
grails.plugin.springsecurity.openid.domainClass 						= 'org.commonsemantics.grails.users.model.OpenId'

grails.plugin.springsecurity.rememberMe.persistent 						= true
grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName = 'org.commonsemantics.grails.security.model.PersistentLogin'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/info'				: ['permitAll'],
]
// -------------------------------------------------------------------------------------------------------------------------------------------


grails.views.default.codec	= "none" // none, html, base64
grails.views.gsp.encoding	= "UTF-8"
