# Tech Forum Demo

This Android app was created to demo various Google Firebase features including In-App messaging, Remote Config, Firebase Analytics, Realtime DB, Crashlytics and Storage.

## Installation

If you want to grab the code for your own projects, I recommend installing by importing directly from version control in Android Studio. You will need to add your own google-services.json to the /app/ folder, and just make sure you use [.gitignore] to exclude that file from your own repo on GitHub.

## Special Feature: Shake Listener
This feature will launch an AlertDialog box when the user shakes their device so that the user can submit a Riddle for evaluation. Look for the SendFeedback.class for this, and the ShakeDetector.java is used with the MainActivity.class to detect when a user shakes the device. 

![Image of Shake Detection](https://www.ecspan.com/techforumdemo/tech-forum-shake.gif)

Note that the RiddleFragment.class will show the daily riddle if the user equals a value. Look in the comments to see more. To customize your own admin value, look for the string 'hermosa' in the res/values/strings file.

## Screens
Here's a quick view of all the screens in this version.

![Image of Shake Detection](https://www.ecspan.com/techforumdemo/techforumdemo.gif)

## Contributors
I'm learning how to code, so wherever possible, I tried to put where I found examples in comments or blocks. I have several 'TODO' comments, so if you would like to contribute or improve upon this app in any way, please feel free to participate! 

## License
[MIT](https://choosealicense.com/licenses/mit/)
