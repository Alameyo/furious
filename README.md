This is API for cinema with Fast & Furious movies.  

Assumptions:
* Generally API have 2 collections. Timetable and movie reviews.
* Usually cinemas have different prices for tickets depending on time of screening, not movie title so price is part of the screening timetable. There is
  however no problem with setting any price to any movie as movies are also.
* Movie rating is part of review from moviegoer, however there is no problem leave just score without comment. Comment is nullable.
* There is one account for cinema owner who can update timetable.
* Task was requiring to create cinema for Fast & Furious movies, but for real it will work with any other movies with IMBD id 

Things that should be done on the way to deployment but weren't yet.

* Authentication for admin
* More test coverage, use mockito wherever needed
* Fuzzing
* More input validation (missing corner cases could come out from fuzzing)
* Exceptions should be logged, not thrown to console output.
* Perhaps some improvements to data persistence layer
* DB configuration (authentication)
* Create and use dockerfile in deployment
* Security threat model
* Deployment (DNS, Hosting, TLS... )