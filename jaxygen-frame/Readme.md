Features:
* @Inject TypeConverterFactory - will ingect converters registered in the app

Hints:
* Never register module from the same package twice - this is also valid for test, because JUnit test has the same package name as the main module

* Default converters:

  By default objects of classes DTOObject are converted to EntityObject and vice versa.
  When not registered jaxygen will automatically convert PartialArrayList objects to PaginableListResponseBaseDTO so there's no need to register the partial to paginable converters    
