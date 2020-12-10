**Code Paring Assignment - DBOI**

**Problem Statement**

There is a scenario where thousands of trades are flowing into one store, assume any way of transmission of trades. We
need to create a one trade store, which stores the trade in the following order

There are couples of validation, we need to provide in the above assignment

    1. During transmission if the lower version is being received by the store it will reject the trade and throw an exception. If the version is same it will override the existing record.
    2. Store should not allow the trade which has less maturity date then today date.
    3. Store should automatically update the expire flag if in a store the trade crosses the maturity date.

### **How To Test**

#### **1. Store Trade**

##### **POSTMAN**

    URL: http://localhost:8080/dboi/trade
    Method: POST
   
    Request Payload: 
       {
       "tradeId":"T1",
       "version":1,
       "counterPartyId":"C1",
       "bookId":"B1",
       "maturityDate":"2020-12-11",
       "createdDate":"2020-12-09",
       "expired":"N"
       }

#### **2. Fetch Trades**

##### **POSTMAN**

    URL: http://localhost:8080/dboi/trade
    Method: GET

### **Tools and Technologies used:**

1. Sprint Boot
2. JUnit5
3. Mockito
4. Lombok Project
5. H2 In memory DB

### **Key Points**

1. Development has been performed by following TDD approach.
2. Written regression test cases using JUnit5 and Mockito frameworks.
3. Written Controller level integration test cases to test e2e flow.
4. Maven is used to build the project.
5. Used H2 In memory DB to perform DB operations.
6. Written unit test cases to achieve 100% code coverage.