### Curl commands to REST API
##### MealRestController
Get all meals of currently logged in user
`$ curl http://localhost:8080/topjava/rest/v1/meals/`
Get meals between dates ('dd-MM-yyyy') and/or times ('HH:ss')
Parameters: startDate, endDate, startTime, endTime. All parameters are optional.
`$ curl "http://localhost:8080/topjava/rest/v1/meals/filter?startDate=31-05-2015&endDate=&startTime=&endTime=14%3A00"`
Add a new meal
`$ curl -H "Content-Type: application/json" -X POST -d '{"dateTime":"2015-06-01T11:00:00","description":"Breakfast","calories":400}' http://localhost:8080/topjava/rest/v1/meals`
Update a meal
`$ curl -H "Content-Type: application/json" -X PUT -d '{"dateTime":"2015-06-01T11:00:00","description":"Breakfast","calories":450}' http://localhost:8080/topjava/rest/v1/meals/100010`
Delete a meal
`$ curl -X DELETE http://localhost:8080/topjava/rest/v1/meals/100010`

