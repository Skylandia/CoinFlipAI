package com.skylandia.coinflipai.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class CurrencyFormat {
	public static String format(double value) {
		// Create a custom DecimalFormatSymbols with space as the grouping separator
		DecimalFormatSymbols customSymbols = new DecimalFormatSymbols();
		customSymbols.setGroupingSeparator(' ');

		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance();

		// Set the number of decimal places to 0
		decimalFormat.setMaximumFractionDigits(0);
		decimalFormat.setMinimumFractionDigits(0);

		// Set the custom DecimalFormatSymbols
		decimalFormat.setDecimalFormatSymbols(customSymbols);

		return decimalFormat.format(value);
	}
}