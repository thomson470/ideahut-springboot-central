package net.ideahut.admin.central;

import java.util.regex.Pattern;

public final class AppConstants {
	private AppConstants() {}
	
	public static final String PACKAGE = "net.ideahut.admin.central";
	
	// Default
	public static class Default {
		private Default() {}
		public static final String TIMEZONE = "Asia/Jakarta";
	}
	
	// Boolean
	public static class Boolean {
		private Boolean() {}
		public static final Character YES 	= 'Y';
		public static final Character NO 	= 'N';
	}
	
	// Regex
	public static class Regex {
		private Regex() {}
		public static final Pattern EMAIL	= Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
	}
	
	public static final class Prefix {
		private Prefix() {}
		public static final String AUTH = "ADMIN-CENTRAL-AUTH-";
		public static final String TOKEN = "ADMIN-CENTRAL-TOKEN-";
	}
	
}
