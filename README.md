# Chatbot

#### Created in April 2019

#### This app is not officially affiliated with the DHBW in any way, but was merely created as a project during my studies. 

<p> &nbsp </p>

This app is a chatbot named Charlie. He analyses your hobbies and favorite school subjects to recommend you a study programm at the Cooperative State University (DHBW). 
It uses Firebase Authentification for the login and Firebase Realtime Database for the AI.


<p> &nbsp </p>

## How it works

### Overall functioning:

The app labels all hobbies and favorite subjects with a topic e.g. is the hobby "dancing" labeled with the topic "sports". Common hobbies and favorite subjects are already labeled,
but if the user has a rare one, he have to label it himself with one of the existing labels. This will be safed in an extra database. If another user enters the same hobby or favorite subject again,
he will be asked to label it too. If both labels are equal, it will be saved to the known labels database and then the labeling will be done automatically. 
If the labels are not the same the word will be deleted from the database and the process will start again.

<img src="/Screenshots/LearningSystem.png" height="400">
<p> &nbsp </p>


### Loading screen and login:

The app starts with a loading screen, which lasts 3 seconds. After that you can see the login page. Here you have the choice to login or skip it. If you enter your login 
details it will be saved to Firebase Authentification, so you can login with them again. If you skip the login the app shows an AlertDialog, which warns you that you can't save your result then.

<img src="/Screenshots/LoadingScreen.png" height="400">  <img src="/Screenshots/LoginScreen.png" height="400">

<p> &nbsp </p>

### Chat

First of all the chatbot introduces himself and asks for your hobbies. Then you can enter as much hobbies as you want separeted by comma. The user input is checked by its spelling with an 
online dictionary API. If the spelling is wrong, the user will get the possibility to correct it. Otherwise the Chatbot checks whether the word is already stored in the Firebase Realtime Database.
After that the user gets asked for his favorite subject and the same procedure starts. Then the chatbot asks you whether you are interested in a few activities (e.g. programming), which were chosen based on the topics
of the users previous input. Every activity is connect to one study program, so you can choose up to 5 activities. 
In the end the chatbot wishes the user good luck and you can switch to the result activity.

<img src="/Screenshots/Activity.png" height="400">

<p> &nbsp </p>

### Result

In the result page the user can see up to 5 recommend study programms. He can click on them to see the website of the DHBW dedicated to the spefic study program.
Additionally the user can click on the DHBW-Logo to get to the website of the DHBW or he can click on the questionmark button.


<img src="/Screenshots/result.png" height="400"> <img src="/Screenshots/WebView.png" height="400">

<p> &nbsp </p>

### Help

If the user has questions he can contact the DHBW directly. This activity shows the contact information.


<img src="/Screenshots/help.png" height="400">

