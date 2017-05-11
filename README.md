# PopularMovies
This is the first project of Udacity's Android Developer Fast Track: Popular Movies. The master branch contains the final code that has to be reviewed. I'll be developing stages 1 and 2 in separate branches, and then merge them into master.

In order to test it, you need to create an `api_keys.xml` XML file in `app/src/main/res/values/` to store the TheMovieDB api's key, like this example:
  
  app/src/main/res/values/api_keys.xml
  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
      <string name="THE_MOVIE_DB_API_TOKEN">XXXXX</string>
  </resources>