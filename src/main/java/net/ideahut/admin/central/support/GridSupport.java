package net.ideahut.admin.central.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ArrayNode;

import net.ideahut.admin.central.object.View;
import net.ideahut.springboot.grid.GridAdditional;
import net.ideahut.springboot.grid.GridOption;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.mapper.DataMapperImpl;
import net.ideahut.springboot.object.Option;

public final class GridSupport {
	
	private GridSupport() {}
	
	private static DataMapper mapper = new DataMapperImpl();
	
	/*
	 * OPTIONS
	 */
	public static Map<String, GridOption> getOptions() {
		Map<String, GridOption> options = new HashMap<>();
		options.put("YES_NO", StaticOption.YES_NO);
		options.put("VIEW_NAME", StaticOption.VIEW_NAME);
		return options;
	}
	public static class StaticOption {
		private StaticOption() {}
		
		// YES / NO
		public static final GridOption YES_NO = context ->
		Arrays.asList(
			new Option("Y", "Yes"),
			new Option("N", "No")
		);
		
		// VIEW NAME
		public static final GridOption VIEW_NAME = context -> {
			List<Option> options = new ArrayList<>();
			for (View view : View.values()) {
				options.add(new Option(view.name(), view.name()));
			}
			return options;
		};
	}
	
	/*
	 * ADDITIONALS
	 */
	public static Map<String, GridAdditional> getAdditionals() {
		Map<String, GridAdditional> additionals = new HashMap<>();
		additionals.put("DAYS", StaticAdditional.DAYS);
		additionals.put("MONTHS", StaticAdditional.MONTHS);
		return additionals;
	}
	public static class StaticAdditional {
		private StaticAdditional() {}
		
		// MONTHS
		public static final GridAdditional MONTHS = context -> {
			String str = "[\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\", \"Jan\", \"Feb\", \"Mar\", \"Apr\", \"May\", \"Jun\", \"Jul\", \"Aug\", \"Sep\", \"Oct\", \"Nov\", \"Dec\"]";
			return mapper.read(str, ArrayNode.class);
		};
		
		
		// DAYS
		public static final GridAdditional DAYS = context -> {
			String str = "[\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\", \"Sun\", \"Mon\", \"Tue\", \"Wed\", \"Thu\", \"Fri\", \"Sat\"]";
			return mapper.read(str, ArrayNode.class);
		};
		
	}

}
