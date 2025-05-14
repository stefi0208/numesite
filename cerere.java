import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TravelGeniusFlights {

    private static final String API_KEY = "96bc953e96mshc77154f855b2963p16085ejsn3a690d8c9274";
    private static final String API_HOST = "skyscanner44.p.rapidapi.com";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicită informații utilizatorului
        System.out.print("Introduceți orașul de plecare (ex: Bucharest): ");
        String origin = scanner.nextLine();

        System.out.print("Introduceți orașul de destinație (ex: Paris): ");
        String destination = scanner.nextLine();

        System.out.print("Introduceți data de plecare (YYYY-MM-DD): ");
        String departureDate = scanner.nextLine();

        try {
            // Construiește URL-ul cererii
            String url = String.format(
                "https://skyscanner44.p.rapidapi.com/search?adults=1&origin=%s&destination=%s&departureDate=%s&currency=EUR",
                origin, destination, departureDate
            );

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("X-RapidAPI-Key", API_KEY);
            con.setRequestProperty("X-RapidAPI-Host", API_HOST);

            int responseCode = con.getResponseCode();
            System.out.println("\nCod răspuns API: " + responseCode);

            // Dacă cererea este validă
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parsare JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

                // Verifică dacă există itinerarii
                JsonArray flights = jsonResponse.getAsJsonArray("itineraries");

                if (flights != null && flights.size() > 0) {
                    System.out.println("\n--- Zboruri găsite ---");

                    // Afișăm maxim 5 zboruri
                    int count = Math.min(flights.size(), 5);
                    for (int i = 0; i < count; i++) {
                        JsonObject flight = flights.get(i).getAsJsonObject();

                        // Verificăm dacă există prețuri
                        JsonArray pricingOptions = flight.getAsJsonObject("pricing_options").getAsJsonArray("pricing_options");
                        if (pricingOptions != null && pricingOptions.size() > 0) {
                            String price = pricingOptions.get(0).getAsJsonObject().get("price").getAsString();

                            // Verifică dacă există informații despre compania aeriană și legături
                            JsonArray legs = flight.getAsJsonArray("legs");
                            if (legs != null && legs.size() > 0) {
                                JsonObject leg = legs.get(0).getAsJsonObject();
                                JsonArray carriers = leg.getAsJsonArray("carriers");
                                if (carriers != null && carriers.size() > 0) {
                                    String airline = carriers.get(0).getAsJsonObject().get("name").getAsString();
                                    String departure = leg.get("departure").getAsString();
                                    String arrival = leg.get("arrival").getAsString();

                                    // Afișează detalii zbor
                                    System.out.println("\nZbor #" + (i + 1));
                                    System.out.println("Companie aeriană: " + airline);
                                    System.out.println("Preț: " + price + " EUR");
                                    System.out.println("Plecare: " + departure);
                                    System.out.println("Sosire: " + arrival);
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Nu s-au găsit zboruri pentru criteriile date.");
                }

            } else {
                System.out.println("Cererea API a eșuat! Cod eroare: " + responseCode);
            }

        } catch (Exception e) {
            // Tratează excepțiile
            e.printStackTrace();
            System.out.println("A apărut o eroare la efectuarea cererii.");
        }

        scanner.close();
    }
}
