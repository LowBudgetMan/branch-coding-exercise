# Branch Coding Exercise
This repository is my submission for the Branch coding exercise. The application will serve up a single endpoint to return 
a simplified combination of a user's GitHub profile and list of repositories. The application is built using Java 23, Spring Boot 3.4.0, 
and Gradle. To get the application up and running, just use the standard `./gradlew bootRun`. The application runs on `port 8080`
by default and the profile route can be found at `http://localhost:8080/api/user-profile/{username}`.

## Explanations

### Architecture
The architecture of this project is pretty basic. At the high level, there are three packages:
* `config` is where configuration important for the whole application is stored. In this case that means the global RestClient
  bean as well as the cache configuration.
* `github` contains the GitHub client used to make calls to the GitHub API, and the domain and exceptions related to those calls.
* `userprofile` contains the service, controller, exceptions, and domain objects related to fetching the user's profile.

I laid the project out this way based on small areas of work. I think that in the future the GitHub client could potentially 
be used for more than just the user profile, so I kept that in a separate package to make it seem more like an independent concept.
Similarly, user profile exists in its own package since in the future, anything using a userprofile should go through the UserService.

I have the UserProfileController also located in the same userprofile package over a separate controller package because
I am a firm believer that a REST endpoint is just a different interface for the same root functionality contained in the 
UserProfileService, and so should be stored alongside it.

When it comes to building domain objects, I usually have two ways that I like to go about doing so. When it comes to classes 
like DTOs that tend to only be built in one direction (either to a domain object or from a domain object) I like to add a
static `from()` or `to()` method on them so the logic is contained with the non-core object, as in the case of the UserProfileDto.
On the other hand, when it's something more like a builder and take two different core objects, or multiple pieces, I 
like to make a separate builder class to keep the potentially complex translation logic separate from other business logic 
(such as when I convert the two GitHubClient responses into a single UserProfile object in the UserProfileService).

### Exceptions
When it comes to exceptions, I usually err on the side of checked exceptions. This is because I've seen far too often when
a runtime exception is missed and causes hard-to-trace bugs. While checked exceptions run the risk of making methods very 
difficult to use when there are too many, I've found that good discipline around root types and knowing when to break out
logic goes a long way to making them really usable. In the case of my `UserProfileNotFound` exception, however, I think 
the same functionality could be achieved by just returning an optional and handling that result in the controller. The 
exception in that case doesn't gain us much, and makes the API of the UserProfileService slightly more complicated than 
it needs to be. Also worth noting that while I catch the situation where my rate limit to GitHub has been exceeded, I 
decided to just rethrow it as a RuntimeException, since that will default to a 500 error, and it doesn't really make sense 
to return an empty response or a 404 like when the profile can't be found. Since it's not the User's fault, I think a 500 
status code makes sense, with the possibility that a 503 could also be used since we could tell the user to try again after
the rate limited timeframe is up.

### Caching
Given the limited amount of time, I chose to use the default implementation of caching when `@EnableCaching` is added to
the application. Since the GitHub docs state that an unauthenticated user is limited to 60 requests per hour, I just set 
the eviction rate to every hour. I did this because in my mind it makes sense to try and make sure we get the maximum number 
of unique calls while still providing opportunities to refresh an individual's data. If we increased the cache length, we 
could increase the number of users, but at the cost of longer delays between updates and a more complicated cache eviction 
strategy, since we'd want TTL per record, as opposed to the easy whole-cache-clear I'm doing right now. I also have decided
to cache the individual calls to GitHub through the client since those are the calls that are both rate limited, and the
slowest. While a cache on the UserDetailsService method would reduce the amount of memory, since the number of key-value 
pairs stored would be halved even if the data is the same, putting the cacheable annotations on the client's methods means 
that other future services can benefit from the prefetched data as well (assuming they want to use the same data for 
different reasons, otherwise we should go with the first option and move the client from the root level into the 
userprofile package). Putting the cacheable annotation on the client also negates a limitation directly related to the 
client's resources, which I think makes it more obvious when the solution is that closely placed.

### RestClient
With Spring putting the RestTemplate into maintenance mode, I figured this would be a fun opportunity to try out the new 
RestClient for external REST calls. Unfortunately, the result is a less-than-stellar lack of tests around the GitHubClient 
in the time I had to work on this project, and I should've thought about that before diving in. One of the main reasons 
for that is the change to chainable methods versus the old RestTemplate interface. Because of the way RestClient needs 
its methods chained, the old way I used to use Mockito to just mock the response object is much more complicated. On the 
one hand, it's probably a good thing to require a mock server instead to push the edge of the application further out in 
testing, but on the other it means a much, _much_ more complicated test setup to achieve the same level of thoroughness. 
I talked about this a bit in the chat on the pros and cons of Test Driven Development, and this is one of those times when
a lack of familiarity with a technology can cause the technique to negatively impact the speed of development (in other 
circumstances I would've spent much more time to get it working, and then I'd be much quicker going forward). The other
thing that was notable to me about the RestClient is the way that response statuses can now be handled in a couple 
different ways. One way is by calling `.toEntity()` and then the HttpStatus is available on the response object, and 
checks can be performed against it like with the old RestTemplate, but there is also the new `.onStatus()` which allows 
for inline status error handling which I thought was kinda neat and wanted to give a try. I'd probably stick with casting 
most things using `.toEntity()` going forward, but it was a fun experiment!