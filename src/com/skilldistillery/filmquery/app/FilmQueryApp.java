package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//    app.test();
		app.launch();
	}

//  private void test() throws SQLException {
//    Film film = db.findFilmById(1);
//    Actor actor = db.findActorById(1);
//    System.out.println(actor);
//    System.out.println(film);
//  }

	private void launch() {
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to the Film Query App!");

		startUserInterface(input);
		input.close();

		System.out.println("Thanks for using the Film Query App! \nGoodbye!");
	}

	private void startUserInterface(Scanner input) {
		loop: while (true) {
			System.out.println();
			System.out.println(
					"Would you like to (1. look up a film by its ID) or (2. look up a film by a KEYWORD search) or (3. exit)?");
			String choice = input.nextLine().toLowerCase();

			switch (choice) {
			case "1":
			case "id":
			case "look up a film by its id":
				callFilmById(input);
				break;
			case "2":
			case "keyword":
			case "look up a film by a keyword search":
				callFilmByKeyword(input);
				break;
			case "3":
			case "quit":
			case "exit":
				break loop;
			default:
				System.out.println("Please pick a valid option");
				break;
			}
		}

	}

	private void callFilmById(Scanner input) {
		Film film = null;
		int Id = 0;
		loop: while (true) {
			try {
				System.out.println("Please enter an ID");
				Id = input.nextInt();
				input.nextLine();
				break loop;
			} catch (Exception e) {
				input.nextLine();
				System.out.println("The ID contains only numbers");
				continue loop;
			}

		}
		System.out.println(Id);
		try {
			film = db.findFilmById(Id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (film == null) {
			System.out.println("This movie does not exist");
		} else {
			System.out.println("Title: " + film.getTitle() + "\nYear Released: " + film.getReleaseYear() + "\nRated: "
					+ film.getRating() + "\nDescription: " + film.getDescription());
			try {
				System.out.println("Language: " + ((DatabaseAccessorObject) db).getLanguage(film.getLanguageID()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			printFilmActors(film);
		}
	}

	private void callFilmByKeyword(Scanner input) {
		List<Film> films = null;
		String keyword = input.nextLine();
		try {
			films = (((DatabaseAccessorObject) db).findFilmWithKeyword(keyword));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Film film : films) {
			System.out.println("Title: " + film.getTitle() + "\nYear Released: " + film.getReleaseYear() + "\nRated: "
					+ film.getRating() + "\nDescription: " + film.getDescription());
			try {
				System.out.println("Language: " + ((DatabaseAccessorObject) db).getLanguage(film.getLanguageID()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			printFilmActors(film);
		}
		if (films.isEmpty()) {
			System.out.println("Movies with the keyword '" + keyword + "', do not exist.");
		}
	}

	private void printFilmActors(Film film) {
		List<Actor> actors;
		actors = film.getActors();
		System.out.print("Actors: ");
		for (Actor actor : actors) {
			if (actor == actors.get(actors.size() - 1)) {
				System.out.print(actor.getFirstName() + " " + actor.getLastName());
			} else {

				System.out.print(actor.getFirstName() + " " + actor.getLastName() + ", ");
			}
		}
	}

}
