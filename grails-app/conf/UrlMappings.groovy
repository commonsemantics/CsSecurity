class UrlMappings {

	static mappings = {

		/*
		 * Authentication
		 */
		"/login/auth" {
			controller = 'openId'
			action = 'auth'
		}
		"/login/openIdCreateAccount" {
			controller = 'openId'
			action = 'createAccount'
		}
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/info"(view:"/info")
		"/index"(view:"/index")
		"/"(view:"/info")
		"500"(view:'/error')
	}
}
