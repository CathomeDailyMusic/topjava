### Curl commands to REST API
##### MealRestController


| Path              |  Method | Description                               |
|-------------------|---------|-------------------------------------------|
| [`/rest/v1/meals`]|   `GET` | Get all meals of currently logged in user |
* **URL Params** 
    None
* **Data Params**
    None

* **Success Response:**

    * **Code:** 200 <br />
        **Content-Type: application/json**
      ```json
	  {
	      "id": "int",
	      "dateTime": "yyyy-MM-ddTHH:mm:ss",
	      "description": "String",
	      "calories": "int",
	      "excess": "boolean"
	  }
	  ```
* **Example:**

`$ curl http://localhost:8080/topjava/rest/v1/meals/`

| Path                     |  Method | Description                                                   |
|--------------------------|---------|---------------------------------------------------------------|
| [`/rest/v1/meals/filter`]|   `GET` | Get meals between dates ('dd-MM-yyyy') and/or times ('HH:ss') |
* **URL Params**\
    startDate=dd-MM-yyyy,\
     endDate=dd-MM-yyyy,\
    startTime=HH:mm,\
     endTime=HH:mm
* **Data Params**
    None

* **Success Response:**

    * **Code:** 200 <br />
        **Content-Type: application/json**
      ```json
	  {
	      "id": "int",
	      "dateTime": "yyyy-MM-ddTHH:mm:ss",
	      "description": "String",
	      "calories": "int",
	      "excess": "boolean"
	  }
	  ```
* **Example:**

`$ curl "http://localhost:8080/topjava/rest/v1/meals/filter?startDate=31-05-2015&endDate=&startTime=&endTime=14%3A00"`

| Path               |  Method | Description   |
|--------------------|---------|---------------|
| [`/rest/v1/meals`]|  `POST` | Add a new meal |
* **URL Params**
    None
* **Data Params**
    json

* **Success Response:**

    * **Code:** 201 <br />
        **Content-Type: application/json**
      ```json
	  {
	      "id": "int",
	      "dateTime": "yyyy-MM-ddTHH:mm:ss",
	      "description": "String",
	      "calories": "int",
	      "excess": "boolean"
	  }
	  ```
* **Example:**

`$ curl -H "Content-Type: application/json" -X POST -d '{"dateTime":"2015-06-01T11:00:00","description":"Breakfast","calories":400}' http://localhost:8080/topjava/rest/v1/meals`

| Path                   | Method | Description   |
|------------------------|--------|---------------|
| [`/rest/v1/meals/{id}`]|  `PUT` | Update a meal |

* **URL Params**
    None
* **Data Params**
    json

* **Success Response:**

    * **Code:** 204 <br />
* **Example:**

`$ curl -H "Content-Type: application/json" -X PUT -d '{"dateTime":"2015-06-01T11:00:00","description":"Breakfast","calories":450}' http://localhost:8080/topjava/rest/v1/meals/100010`

| Path                   | Method    | Description   |
|------------------------|-----------|---------------|
| [`/rest/v1/meals/{id}`]|  `DELETE` | Delete a meal |

* **URL Params**
    None
* **Data Params**
    None

* **Success Response:**

    * **Code:** 204 <br />
* **Example:**

`$ curl -X DELETE http://localhost:8080/topjava/rest/v1/meals/100010`

