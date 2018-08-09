package com.skplanet.nlp.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Donghun Shin | donghun.shin@rakuten.com | RIT | Rakuten Inc.
 * @since 2017/03/31
 */
public class RestfulClient {
	public static void main(String[] args) {
		String string = "";
		try {

			// Step1: Let's 1st read file from fileSystem
			// Change CrunchifyJSON.txt path here
			/*
			InputStream crunchifyInputStream = new FileInputStream("/Users/<username>/Documents/CrunchifyJSON.txt");
			InputStreamReader crunchifyReader = new InputStreamReader(crunchifyInputStream);
			BufferedReader br = new BufferedReader(crunchifyReader);
			String line;
			while ((line = br.readLine()) != null) {
				string += line + "\n";
			}
			*/

			Scanner scan = new Scanner(System.in);


			// Step2: Now pass JSON File Data to REST Service
			try {
				System.out.print("QUERY: ");
				String query;
				while ((query = scan.nextLine()) != null) {
					URL url = new URL("http://localhost:9200/search-query-train/_search?pretty");
					URLConnection connection = url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestProperty("Content-Type", "application/json");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

					List<String> itemList = new ArrayList<String>();
					String json="{ \"query\": { \"match\": { \"body\": \"" + query + "\" } }, \"_source\": [\"title\"] }";
					JSONObject jsonObject = new JSONObject(json);
					System.out.println(jsonObject);
					out.write(jsonObject.toString());

					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
						/*
						if (line.trim().replace("\"", "").startsWith("title")) {
							// "title" : "207018_12369387",
							System.out.println("item_id: " + line);
							itemList.add(line.trim().replace("\"title\" : \"", "").replace("\"", "").replace(",", ""));
						}
						*/
					}
					in.close();
					out.close();

					List<String> finalResult = new ArrayList<String>();
					for(String item : itemList) {
						URL url2 = new URL("http://localhost:9200/item_title/_search?pretty");
						URLConnection connection1 = url2.openConnection();
						connection1.setDoOutput(true);
						connection1.setRequestProperty("Content-type", "application/json");
						connection1.setConnectTimeout(5000);
						connection1.setReadTimeout(5000);
						OutputStreamWriter out2 = new OutputStreamWriter(connection1.getOutputStream());

						//System.out.println("itemID: " + item);
						json = "{ \"query\": { \"match\": { \"item_id\": \"" + item + "\" } }, \"_source\": [\"title\"] }";
						jsonObject = new JSONObject(json);
						out2.write(jsonObject.toString());
						BufferedReader in2 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));

						String temp = "";
						while ((line = in2.readLine()) != null) {
							if (line.trim().startsWith("\"item_id")) {
								temp = line.replace("\"", "").replace(",", "").replace(":", "").replace("item_id", "").trim();
							}

							if (line.trim().startsWith("\"title")) {
								//System.out.println("\t" + line.replace("\"", "").replace(",", "").replace(":", "").replace("title", "").trim());
								temp += "\t" + line.replace("\"", "").replace(",", "").replace(":", "").replace("title", "").trim();
								if (!finalResult.contains(temp)) {
									finalResult.add(temp);
								}
							}

						}
						in2.close();
						out2.close();
					}

					for (String r : finalResult) {
						System.out.println(r);
					}

					finalResult.clear();
					System.out.print("QUERY: ");
				}

			} catch (Exception e) {
				System.out.println(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
