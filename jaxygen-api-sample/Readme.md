Therea are many ways to run the jaxygen-api-sample, but the easiest one is just to run embedded jetty server 
using maven command:
```mvn jetty:run```

Tips:
* List all modules using api function: http://localhost:8080/api/ServiceBrowser/getModules
* List module methods from APIGINModule module:  http://localhost:8080/api/ServiceBrowser/getModuleServices?moduleName=APIGINModule