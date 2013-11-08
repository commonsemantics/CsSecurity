<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Commonsemantics.org: <g:meta name="app.name"/></title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}
            
			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
	</head>
	<body style="padding:0px; border:0px; margin: 0px; font-family:courier,Georgia,Serif; font-size: 90%">
		<div style="background:#3b3b3b; border-bottom: 2px solid gray;">
			<a href="http://www.commonsemantics.com">
				<img src="http://www.commonsemantics.com/imgs/img02b.jpg"/>
			</a>
		</div>
		<div id="status" role="complementary">			
			<h1>Plugin Status</h1>
			<ul>
				<li>Grails version: <g:meta name="app.grails.version"/></li>
				<li>Groovy version: ${GroovySystem.getVersion()}</li>
				<li>JVM version: ${System.getProperty('java.version')}</li>
				<li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
				<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
				<li>Domains: ${grailsApplication.domainClasses.size()}</li>
				<li>Services: ${grailsApplication.serviceClasses.size()}</li>
				<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
			</ul>
			<h1>Installed Plugins</h1>
			<ul>
				<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
					<li>${plugin.name} - ${plugin.version}</li>
				</g:each>
			</ul>
		</div>
		<div id="page-body" role="main">
			<h1><g:meta name="app.name"/> - Commonsemantics.org Grails Plugin</h1>
			<p>You are running the <font style="font-weight:bold;"><g:meta name="app.name"/></font> Grails Plugin in application mode. 
			<br/>
			<g:meta name="app.description"/>
			<br/>
			
			<div id="controller-list" role="navigation">
				<h2>Common Semantics  Plugins Dependencies</h2>
				<ul>
					<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
						<g:if test="${plugin.name.startsWith('cs')}">
							<li>${plugin.name} - ${plugin.version}</li>
						</g:if>
					</g:each>
				</ul>			
			</div>
			
			
			<div id="controller-list" role="navigation">
				<h2>Provided Controllers:</h2>
				<ul>
					<g:if test="${grailsApplication.controllerClasses.size()>0}">
						<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
							<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
						</g:each>
					</g:if>
					<g:else>
						<li>None</li>
					</g:else>
				</ul>
			</div>	
					
			<div id="controller-list" role="navigation">
				<h2>Provided Services:</h2>
				<ul>
					<g:if test="${grailsApplication.serviceClasses.size()>0}">
						<g:each var="c" in="${grailsApplication.serviceClasses.sort { it.fullName } }">
							<li class="controller">${c.fullName}</li>
						</g:each>
					</g:if>
					<g:else>
						<li>None</li>
					</g:else>
				</ul>
			</div>			
					
			<div id="controller-list" role="navigation">
				<h2>Provided Classes:</h2>
				<ul>
					<g:if test="${grailsApplication.getArtefacts("Domain")*.clazz.size()>0}">
						<g:each var="c" in="${grailsApplication.getArtefacts("Domain")*.clazz.sort{it.name}.reverse()}">
							<li class="controller">${c.name}</li>
						</g:each>
					</g:if>
					<g:else>
						<li>None</li>
					</g:else>
				</ul>
			</div>			
	
			<h2>About</h2>
			<blockquote>
			The <font style="font-weight:bold;"><g:meta name="app.name"/></font> 
			Plugin has been originally coded by <a href="https://paolociccarese.info">Dr. Paolo Ciccarese</a>.<br/>
			The code is available on <a href="https://github.com/commonsemantics">GitHub</a>.
			</blockquote>
		</div>
		<div style="clear:both"></div>
		<br/>
		<div style="background:#3b3b3b; border-top: 2px solid gray; color: white; text-align: right; padding: 5px; padding-right: 10px;">
			&copy; 2013 COMMON SEMANTICS 
		</div>
	</body>
</html>
