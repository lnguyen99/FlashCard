
## LAB 1

- **minSDK version** specify the minimum version of the Android Operating System required to run your application

- **Activity** - Java files; the screen in your App that user can interact with 
- **Layout** - XML files composed of multiple views
- [**Event Listener**](https://guides.codepath.org/android/Basic-Event-Listeners#edittext-common-listeners) - listen to certain user interactions and respond to them

- **GitHub** : VCS

- **XML** is a markup language to implement UI layouts in Android apps 
- **View Layout** : 
    - **RelativeLayout** : elements need to be positioned relative to other elements
    - **ConstraintLayout** : most advanced and flexible layout; more info [here](https://constraintlayout.com/basics/)
- **View** : contents that are drawn onto the screen of an Android device, usually created in XML file
    - **TextView** : allows for many customizations such as font, font size, font color
    - **ImageView** 
    - **EditText** : allows user input, customizations such as how many lines of text the user can input, what is shown when there is no input yet, the text color
- **id** : used in Android's xml layout files to identify views; + to create new; call id name to reference
- **dp / sp** : units of measurement
    - **dp** (density pixel) used for views' height, width, margin, and padding
    - **sp** (scale pixel) used for font sizes
- **match_parent** : the view wants its width to be as big as its parents view

## LAB 2

- **Intent objects** : specify what the app wants to happen, can pass data along 
    - Example: 
    ```
    // Activity A 
    ...
    Intent intent = new Intent(ActivityA.this, ActivityB.class);
    intent.putExtra("key", "info to pass"); 
    startActivity(intent); 
    ...
    
    // Activity B
    ...
    String data = getIntent().getStringExtra("key"); // data = "info to pass"
    ...

    ```
    - **Explicit intents** are intents where we know exactly what we want to do 
        - ie : start a certain activity within our app
        - mainly for in-app activities
    - **Implicit intents** are intents where we simply have a certain action we want to perform 
        - ie : start other apps on a user's phone to complete an action like sending a message. This is how when you press the 'Share' button in some apps, you get a variety of choices of how you want to share your content.
        - mainly for interacting with other apps outside of our own (sharing local/remote contents)


- **Resources** : 
    - Drawable : Bitmap files (PNG, JPG, SVG -- vector based files) or XML files that we can use as icons or backgrounds for views
    - Layout
    - Values (colors, strings, styles)

- **Android's Activity Lifecycle** : 
    `onCreate   --->   call finish()    ---> onDestroy (explicitly dismiss)`

- **Toast** : small popup message that automatically goes away after a specified amount of time
- **Snackbar** : shows a fleeting message, more customizable than Toasts in its ability to style it in certain ways and add click listeners to it
  
## LAB 3

- **Gradle** : dependency management system for Android

- [**Persistence in Android**](https://guides.codepath.org/android/Persisting-Data-to-the-Device) : app's ability to save data, which means that the app will have the same information stored in it even if the user restarts the app, or restarts their phone. This can be done through a couple different ways: SharedPreferences, Text Files, and Databases.
    - **Empty States** : app doesn't have any data to display yet (ie : first launch or clear all information on app)
    - [**Room persistence library**](https://guides.codepath.org/android/Room-Guide) : Writing SQL queries for databases can be hard and complicated. So to make development easier, Google introduced a persistence library called Room. Room makes SQL queries easier to implement and use.
                                     
- **SQL** (Structured Query Language) : add, update, delete and retrieve information from databases

## LAB 4

- [**AnimationListener**](https://guides.codepath.org/android/animations#1-using-xml) : gives us callbacks for knowing when an animation starts / ends, so we can hide and show the appropriate views when necessary
    - For example, if we wanted to animate a view disappearing, after the animation finishes we would need to set the visibility of the view to `INVISIBLE`, and this can be more easily done with an `AnimationListener`

- **View Properties / Property Animators**

- **Activity Transitions** : 
