# FilmQueryProject

# Description
The purpose of this project is to dig into a database and retrieve the data of films and actors from the sdvid database. When entering the Film Query App, the user will be given three options. They will be able to look up films by their id or look up films by a certain keyword or phrase, or exit the app. When the user decides to look up a film, the film(s) will display its title, release year, rating, description, language, and actors.
# Technologies Used
- Switch statments were used to pass data so that the user is able to make decisions within the app
-Drivers were used to retrieve data within the sdvid database
-A Scanner was used to allow the user to pass in their input.
- An interface was used to set methods that were meant to be implemented and overridden. 
-Git and github was used to push the App and store it.


# Lessons Learned
I learned that with this code, there was a unique ongoing error which resulted in null actors or null films. This is due to the fact that when the films were retrieved,the actors were not, and it is also true for the opposite. How did I combat this? I only got the actors names when the films were called through mapping.