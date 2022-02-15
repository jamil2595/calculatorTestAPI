# "Bigbank" Calculator Test Task

The task is completed by the widespread and handy library of 
Java - Rest Assured. Testing and validating REST services in Java is harder than in dynamic languages such as Ruby and Groovy. REST Assured brings the simplicity of using these languages into the Java domain.

While developing the assigned task, I fully covered endpoint tests of calculator API. 

### API verification of calculate endpoint

    I created test set for different "calculate" endpoint outcomes for monthly payment and APRC. 
    As well as, I used rest Assured library to test loan/calculate. 
    The request body and also response body is validated in various scenarious. 
    Both of success and fail test cases are covered in the test suite.

### Automated tests of the feature

    Automation project  generates a report for the tests.
    After executing the tests, report can be found here: build/reports/tests/test/index.html 
    When running the automation project, no extra drivers or dependencies is needed to add manually.
    All the required dependencies is inside the project.
    
 
![tests](https://user-images.githubusercontent.com/51953125/154144359-26adc3f0-3c10-46ab-a047-af61ec9a895c.png)

### Detected odds
   
    1. Loan amount & loan period is not validated in the back-end server. While front-end validates it, 
    API does not validate limits on given fields.
    2. Error response from invalid currency is a pure HTML format. Proper error message should be come as an output.
    3. Error response from invalid maturity is a pure HTML format. Proper error message should be come as an output.

### Test execution
 #### Linux:
 - Install gradle: `sudo apt install gradle`
 - Run tests: `gradle test`

#### Windows:
- To install gradle use following [guide](https://docs.gradle.org/current/userguide/installation.html)
- Run tests: `gradlew test`
