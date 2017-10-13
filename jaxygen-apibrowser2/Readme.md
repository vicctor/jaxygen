Accesing APIBroker module services:
* http://localhost:8080/api-broker/ServiceBrowser/getModuleServices?moduleName=JaxygenAPIBroker

Sample call to the login method:
* http://localhost:8080/api-broker/org.jaxygen.apibroker.services.AuthorizatonService/login

Services repository:
* Obtaining all servers registered by the user: http://localhost:8080/api-broker/org.jaxygen.apibroker.services.ServersRepostoryService/getServers
* Obtaining all from the given project with id 1: http://localhost:8080/api-broker/org.jaxygen.apibroker.services.ServersRepostoryService/getServices?projectId=1