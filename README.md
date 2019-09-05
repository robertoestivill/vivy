**Developer notes**

- Before building, replace the following constants in `BasicAuthorizationInterceptor` class.

```
private const val HTTP_BASIC_AUTH_USERNAME = "REDACTED"
private const val HTTP_BASIC_AUTH_PASSWORD = "REDACTED"
```

- Feature splash
  - Checks if we have an access token stored or not.
  - Redirect to Login if token is not present, otherwise redirect to SearchDoctor.
  - Single activity, not much logic needed.
 
- Feature login 
  - Credentials are hardcoded for this exercise.
  - Validations on both fields with delay coroutine.
  - Implemented by using a ViewModel as a controller, therefore supports orientation changes.

- Feature permission
  - Checks if location permissions have been granted.
  - Request them if needed, display rational screen if ignored by the user.
  - Avoid the user to continue without location permissions if desired.
  - Single activity, not much logic needed.
  - Permissions functions are extracted to extension functions for re utilization on SearchDoctor

- Feature search doctor
  - Redirects to Permission if needed.
  - Implemented with a light MVP abstraction.
  - Location details view is just for debug and make sure GPS works correctly
  - Pagination implemented with ScrollListener
  - Takes care of starting the LocationListener