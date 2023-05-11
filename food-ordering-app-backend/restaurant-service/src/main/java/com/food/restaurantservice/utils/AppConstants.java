package com.food.restaurantservice.utils;

public class AppConstants {
	public static String restaurantNotFoundHavingid(long id) {
		return "Restaurant with " + id + " id doesn't exist.";
	}

	public static String restaurantNotFoundHavingEmail(String email) {
		return "Restaurant with " + email + " email doesn't exist.";
	}

	public static String noRestaurantInSelectedCity(String city) {
		return "No Restaurants available in " + city;

	}
}
