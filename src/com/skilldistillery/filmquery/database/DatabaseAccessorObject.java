package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static final String USERNAME = "student";
	private static final String PASSWD = "student";
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;

		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWD);

		String sql = "SELECT * FROM film WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			// Here is our mapping of query columns to our object fields:
			String title = rs.getString("title");
			String desc = rs.getString("description");
			short releaseYear = rs.getShort("release_year");
			int langId = rs.getInt("language_id");
			int rentDur = rs.getInt("rental_duration");
			double rate = rs.getDouble("rental_rate");
			int length = rs.getInt("length");
			double repCost = rs.getDouble("replacement_cost");
			String rating = rs.getString("rating");
			String features = rs.getString("special_features");
			film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating, features);

			film.setActors(findActorsByFilmId(filmId));
		}
		rs.close();
		stmt.close();
		conn.close();// ...
		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		// ...

		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWD);

		String sql = "SELECT * FROM actor WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		if (actorResult.next()) {
			actor = new Actor(); // Create the object
			// Here is our mapping of query columns to our object fields:
			actor.setId(actorResult.getInt("id"));
			actor.setFirstName(actorResult.getString("first_name"));
			actor.setLastName(actorResult.getString("last_name"));

			actor.setFilms(findFilmsByActorId(actorId));
		}
		actorResult.close();
		stmt.close();
		conn.close();// ...
		return actor;
	}

	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		// Check if size is greater than zero
		try {
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWD);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, ";
			sql += " rental_rate, length, replacement_cost, rating, special_features "
					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id " + " WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				// Change Numbers to column names
				int filmId = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				short releaseYear = rs.getShort("release_year");
				int langId = rs.getInt("language_id");
				int rentDur = rs.getInt("rental_duration");
				double rate = rs.getDouble("rental_rate");
				int length = rs.getInt("length");
				double repCost = rs.getDouble("replacement_cost");
				String rating = rs.getString("rating");
				String features = rs.getString("special_features");
				Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);
				films.add(film);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();

		try {
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWD);
			String sql = "SELECT id, first_name, last_name";
			sql += " FROM actor JOIN film_actor ON actor.id = film_actor.actor_id " + " WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int actorId = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				Actor actor = new Actor(actorId, firstName, lastName);
				actors.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	public List<Film> findFilmWithKeyword(String keyword) throws SQLException {
		int counter = 1;
		List<Film> films = new ArrayList<>();
		keyword = "%" + keyword + "%";

		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWD);

		String sql = "SELECT * FROM film WHERE description Like ? or title like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, keyword);
		stmt.setString(2, keyword);
		ResultSet rs = stmt.executeQuery();
		// Here is our mapping of query columns to our object fields:
		while (rs.next()) {		
			int filmId = rs.getInt("id");
			String title = rs.getString("title");
			String desc = rs.getString("description");
			short releaseYear = rs.getShort("release_year");
			int langId = rs.getInt("language_id");
			int rentDur = rs.getInt("rental_duration");
			double rate = rs.getDouble("rental_rate");
			int length = rs.getInt("length");
			double repCost = rs.getDouble("replacement_cost");
			String rating = rs.getString("rating");
			String features = rs.getString("special_features");
			Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
					features);

			film.setActors(findActorsByFilmId(filmId));
			films.add(film);
			counter += 1;

		
		}
		rs.close();
		stmt.close();
		conn.close();// ...
		return films;
	}
	public String getLanguage(int langNumber) throws SQLException {
	String lang = "";
	try {
		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWD);
		String sql = "SELECT name";
		sql += " FROM language JOIN film ON film.language_id = language.id " + " WHERE language_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, langNumber);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			lang = rs.getString("name");

		}
		rs.close();
		stmt.close();
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return lang;
	}

}
