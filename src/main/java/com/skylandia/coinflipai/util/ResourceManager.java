package com.skylandia.coinflipai.util;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.util.coinflip.CoinFlipHeaders;
import com.skylandia.coinflipai.util.coinflip.CoinFlipRecord;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class ResourceManager {

	private static final String path = "logs/coinflip/history.csv";
	private static CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
		.setHeader(CoinFlipHeaders.class)
		.setSkipHeaderRecord(true)
		.build();


	public static void saveRecord(CoinFlipRecord record) {
		CoinFlip.history.add(record);
		try {
			CSVPrinter printer;
			CSVFormat format;

			File file = new File(path);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				format = CSVFormat.DEFAULT.builder().setHeader(CoinFlipHeaders.class).build();
			} else {
				format = CSVFormat.DEFAULT;
			}

			printer = new CSVPrinter(new FileWriter(path, true), format);
			printer.printRecord(record.username(), record.wager(), record.win());

			printer.close(true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void loadHistory() {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}

		CoinFlip.history.clear();
		try {
			for (CSVRecord record : csvFormat.parse(new FileReader(file))) {
				String username = record.get("username");
				double wager = Double.parseDouble(record.get("wager"));
				boolean win = Boolean.parseBoolean(record.get("win"));

				CoinFlip.history.add(new CoinFlipRecord(username, wager, win));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
