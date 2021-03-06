This is API for cinema with Fast & Furious movies.  

Swagger/OpenAPI documentation available in `docs/furious.yaml`

As preconfiguration:
* MongoDB database need to be run `scripts/PullDatabase.sh` will pull docker image of database
* applications properties need to be supplied with
  * `omdapi.token`
  * `spring.security.user.name` 
  * `spring.security.user.password`
* Run application with `dev` profile

Assumptions:
* Generally API have 2 collections. Timetable and movie reviews.
* Usually cinemas have different prices for tickets depending on time of screening, not movie title so price is part of the screening timetable. There is
  however no problem with setting any price to any movie as screened movies are also part of timeslot.
* Movie rating is part of review from moviegoer, however there is no problem leave just score without comment. Comment is nullable.
* There is one account for cinema owner who can update timetable.
* Task was requiring to create cinema for Fast & Furious movies, but for real it will work with any other movies with IMBD id 

Things that should be done on the way to deployment but weren't yet.

* Enable cors if API is meant to be used by web browser
* More test coverage, use mockito wherever needed
* More fuzzing, some can ve found in `scripts/boofuzz`
* More input validation (missing corner cases could come out from fuzzing)
* Exceptions should be logged, not thrown to console output.
* Perhaps some improvements to data persistence layer
* DB configuration (authentication)
* Create and use dockerfile in deployment
* Security threat model
* Deployment (DNS, Hosting, TLS... )