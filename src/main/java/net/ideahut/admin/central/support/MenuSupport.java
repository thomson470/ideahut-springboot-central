package net.ideahut.admin.central.support;

import java.util.Comparator;

import net.ideahut.admin.central.object.Menu;

public final class MenuSupport {

	private MenuSupport() {}
	
	/*
	 * SORT
	 */
	public static final class Sort {
		private Sort() {}
		public static final Comparator<Menu> TITLE = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
		public static final Comparator<Menu> ORDER = (o1, o2) -> o1.getOrder().compareTo(o2.getOrder());
		
	}
	
}
