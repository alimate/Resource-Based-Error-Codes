# REST API Error Handling with Spring Boot
This is a sample project to show how to implement a robust error handling mechanism for REST APIs written with Spring Boot.
For more details on goals and motivations of this repository you can check [this post][blog-post-rest] out.

### HTTP Error Response Schema
The generated error response would have the following schema:

    {
      "status_code": // integer,
      "reason_phrase": // string,
      "errors": [
        {
          "error_code": // string,
          "message": // string
        }, ...
      ]
    }

### How it Works?
On Linux/Mac machines, use the following command:

    ./gradlew bootRun

and in Windows machines:

    gradlew.bat bootRun

Then send a `POST` request with an empty JSON payload, i.e. `{}`. You should get the following as the response:

    {
      "status_code": 400,
      "reason_phrase": "Bad Request",
      "errors": [
        {
          "error_code": "geeks-3",
          "message": "last_name is required"
        },
        {
          "error_code": "geeks-2",
          "message": "first_name is required"
        }
      ]
    }
You could also set the `Accept-Language` to `fa-IR` to get the localized error messages. Also, if send arbitrary `first_name`
and `last_name` in the request body, you would get:

    {
      "status_code": 400,
      "reason_phrase": "Bad Request",
      "errors": [
        {
          "error_code": "geeks-1",
          "message": "The geek already exists"
        }
      ]
    }


[blog-post-rest]: https://alidg.me/blog/2016/9/24/rest-api-error-handling