import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FetchingData {

    public static void main(String[] args) throws IOException {
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("login", "*****");
        mapParams.put("password", "*****");

        String loginURL = "https://corner-stats.com/index.php?route=account/login&amp;red_route=";

        String[] englandPremierLeague = {"/bournemouth/england/team/1682", "/al-nasar/kuwait/team/8347", "/arsenal/england/team/8", "/aston-villa/england/team/17", "/aswan-fc/egypt/team/8338", "/brighton/england/team/1499", "/burnley/england/team/1629", "/chelsea/england/team/1", "/crystal-palace/england/team/1485", "/everton/england/team/6", "/masr/egypt/team/8337", "/happy-valley-aa/hong-kong/team/8287", "/leicester/england/team/1490", "/liverpool/england/team/18", "/man-city/england/team/4", "/man-utd/england/team/5", "/newcastle/england/team/10", "/norwich/england/team/15", "/sheff-utd/england/team/1718", "/southampton/england/team/20", "/tottenham/england/team/14", "/watford/england/team/1498", "/west-ham/england/team/7", "/wolverhampton/england/team/1903"};
        String[] germanyBundesliga = {"/cologne/germany/team/1517", "/union-berlin/germany/team/1515", "/augsburg/germany/team/36", "/bayer-leverkusen/germany/team/29", "/bayern-munich/germany/team/21", "/borussia-dortmund/germany/team/26", "/borussia-mgladbach/germany/team/27", "/eintracht-frankfurt/germany/team/22", "/fortuna-dusseldorf/germany/team/25", "/sc-freiburg/germany/team/34", "/hertha-berlin/germany/team/1506", "/tsg-hoffenheim/germany/team/37", "/mainz/germany/team/33", "/rb-leipzig/germany/team/1892", "/paderborn/germany/team/1630", "/schalke/germany/team/24", "/werder-bremen/germany/team/30", "/wolfsburg/germany/team/32"};
        String[] italySeriaA = {"/atalanta/italy/team/57", "/bologna/italy/team/54", "/brescia/italy/team/1616", "/cagliari/italy/team/52", "/fiorentina/italy/team/49", "/genoa/italy/team/46", "/verona/italy/team/1523", "/inter-milan/italy/team/47", "/juventus/italy/team/39", "/lazio/italy/team/41", "/ac-milan/italy/team/48", "/napoli/italy/team/40", "/parma/italy/team/51", "/roma/italy/team/43", "/sampdoria/italy/team/42", "/real-societa/italy/team/796", "/udinese/italy/team/53", "/lecce/italy/team/334", "/sassuolo/italy/team/1524"};
        String[] spainLigaBBVA = {"/valencia/spain/team/75", "/villarreal/spain/team/1550"};


        Connection.Response response = Jsoup.connect(loginURL)
                .data(mapParams)
                .method(Connection.Method.POST)
                .execute();
        Map<String, String> cookies = response.cookies();

        String baseUrl = "https://corner-stats.com";
        for (int i = 0; i < spainLigaBBVA.length; i++) {
            String url = baseUrl + spainLigaBBVA[i];

            Document document = Jsoup.connect(url)
                    .data("show_input", "1000")
                    .cookies(cookies)
                    .post();

            Element table = document.getElementById("table_corners");

            if (table == null) continue;

            Elements home_stats = table.getElementsByClass("teamstats");
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<String> tournaments = new ArrayList<>();
            ArrayList<String> home_teams = new ArrayList<>();
            ArrayList<String> home_corners = new ArrayList<>();
            ArrayList<String> away_corners = new ArrayList<>();
            ArrayList<String> away_teams = new ArrayList<>();
            for (Element home_stat : home_stats) {
                dates.add(home_stat.getElementsByTag("td").get(0).text());
                tournaments.add(home_stat.getElementsByTag("td").get(1).text());
                home_teams.add(home_stat.getElementsByTag("td").get(3).text());
                home_corners.add(home_stat.getElementsByClass("team_1_corners_quantity").text());
                away_corners.add(home_stat.getElementsByClass("team_2_corners_quantity").text());
                away_teams.add(home_stat.getElementsByTag("td").get(7).text());
            }

            for (int j = 0; j < dates.size(); j++) {
                if (home_corners.get(j).equals("?") || away_corners.get(j).equals("?")) continue;
                SQLite.insert(dates.get(j), tournaments.get(j), home_teams.get(j), Integer.valueOf(home_corners.get(j)), Integer.valueOf(away_corners.get(j)), away_teams.get(j));
                System.out.println(dates.get(j) + "   " + tournaments.get(j) + "   " + home_teams.get(j) + "   " + home_corners.get(j) + "   " + away_corners.get(j) + "   " + away_teams.get(j));
            }
        }
    }
}
