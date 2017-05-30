# PopularMovies
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/abed075f59ad4642ab632d087a7c5857)](https://www.codacy.com/app/cmmata/PopularMovies?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cmmata/PopularMovies&amp;utm_campaign=Badge_Grade)
This is the first project of Udacity's Android Developer Fast Track: Popular Movies. The master branch contains the final code that has to be reviewed. I'll be developing stages 1 and 2 in separate branches, and then merge them into master.

In order to test it, you need to create an `api_keys.xml` XML file in `app/src/main/res/values/` to store the TheMovieDB api's key, like this example:
  
  app/src/main/res/values/api_keys.xml
  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
      <string name="THE_MOVIE_DB_API_TOKEN">XXXXX</string>
  </resources>