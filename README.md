
## Grid view app to load images by search from Flickr

That was hard exercise!

The solutions:
  * Kotlin as development language 
  * App is separated in two parts - common and feature "Images List"
  * Use internal classes as much as possible (for future purposes of the modularisation)
  * Use HttpsUrlConnection for simple networking
  * Use JSONObject for json parsing
  * Use threads pool for background loading, resizing with naive cancellation
  * Separate responsibilities in small classes - http, cache, async, use case, view model, view
  * Model is observable
  * View is around GridView with basic simple adapter
  * View model and cache are saved during configuration change in static variables
  * Activity and Adapter don't have tests, rest is covered with unit or integration tests
  * Use stubs instead of mocking 

There are still major issues:
  * The names are hard (what is async class doing?)
  * Some code is just copy/pasted from internet without deep understanding
  * The UI looks choppy - I need to profile app to understand the reason
  * The loading image cancellation looks like overlap with new image loading to the reused view - I have to debug to catch the reason
  * The image loading starts before view is attached to parent so it causes exception in bitmap resize and you see text placeholder instead of loaded image
  * After rotation app still loads some images from internet - I have to debug to find if it is correct (cache is full) or something wrong done with calculation or key
  * I decided not to write any have Espresso test for UI acceptance testing
  * Some stubs are looking sketchy
  
The assembled apk is [here](apk/app-debug.apk).
 
As conclusion - I'm not proud about the written code, unfortunately!
  
  
