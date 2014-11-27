
[![Build Status](https://travis-ci.org/vicctor/jaxygen.svg)](https://travis-ci.org/vicctor/jaxygen) [![Coverage Status](https://img.shields.io/coveralls/vicctor/jaxygen.svg)](https://coveralls.io/r/vicctor/jaxygen)

## About JAXYGen
JAXYgen is a web-service building framework made by few people in the begining just for fun. Soon it turned out that the framework is enough usable to be productive, so we started to use it in our comercial projects. That's the story end.

## What is it good for.
If youre going to build a more or less complex application that utilizes some logic in the backend and you wish to create a nice view on the front end, then probaby you have different people doing these tasks. So the idea is to give the the backend people the tool that they could offer to the front end developers, and let them work without thinking too mych about the frontend details. So in my teams, backend people uses JAXygen to expose the business logic to frontend people. Frontend people uses the most modern tools like AngularJS or not that new like plain HTML/CSS that comunicates with JAXygen interfaces. And that's all. See more in samples.
That's actually the way how do I work, and I found it valuable and productive.

## How to start it
Well... sorry is not that easy. Probably you have to copy and past some parts of [sample programs](https://github.com/vicctor/jaxygen/tree/master/jaxygen-api-sample) to understand details. 

I'm working on some addons that helps to setup the basic project, but it takes some time, and because I don't realy need it, it still not there.

## About converters pattern
Because JAXygen becomes to my first class set of tools, I decided to publish some addons that makes my work much easier. The first addon is the [Typeconverter module](https://github.com/vicctor/jaxygen/tree/master/jaxygen-typeconverter).
What is it good for?
It actually was born after we realised, that in our programs all what we are doing is converting objects to object all the time. First when receiving object from the web (the DTO class) it is converter to database format. The database format is converted to data processing format, data processing format to network format and so one.

On the other hand, we used mocks to change some part of the app during testing. Soon we realised that probably the same task could be done, if we replace a converter by mocverter. And that's all.
So every time I have to convert data that comes from the web to the database entity class I write down like like this:

```java
MyDB db = Converter.convert(dtoObject, MyDB.class);
em.persist(db);
return Converter.convert(db, ObjectDTO.class);
```

The sample above is the most common pattern used when calling the create method.
