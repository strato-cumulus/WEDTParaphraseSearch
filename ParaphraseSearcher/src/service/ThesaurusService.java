package service;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import orm.ThesaurusEntry;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@Singleton
public class ThesaurusService {
    private static final String SYNONYMS_COLUMN = "synonyms";
    private static final Gson gson = new Gson();
    private static ThesaurusService theInstance;

    static {
        try {
            theInstance = new ThesaurusService();
        }
        catch (Exception e) {
            theInstance = null;
        }
    }

    private HttpClient httpClient = new DefaultHttpClient();
    private HttpHost target;
    private String apiUrl;
    private String requestUrl;
    private Connection connection;
    private String retrievalQuery = "SELECT synonyms FROM thesaurus_entry WHERE word = ?";
    private String insertionQuery = "INSERT INTO thesaurus_entry VALUES (?, ?)";

    private ThesaurusService() throws IOException, SQLException, ClassNotFoundException, IllegalAccessException {
        Properties apiProperties = new Properties();
        apiProperties.load(new FileInputStream("ParaphraseSearcher/connection.properties"));
        apiUrl = apiProperties.getProperty("thesaurus.api");
        requestUrl = apiProperties.getProperty("thesaurus.request");
        target = new HttpHost(apiUrl, 80, "http");


        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream("ParaphraseSearcher/database.properties"));
        connection = DriverManager.getConnection(databaseProperties.getProperty("url"), databaseProperties);
    }

    public ThesaurusEntry get(String word) {
        PreparedStatement statement = null;
        String contents = null;
        try {
            statement = connection.prepareStatement(retrievalQuery);
            statement.setString(1, word);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new ThesaurusEntry(gson.fromJson(resultSet.getString(SYNONYMS_COLUMN), ThesaurusEntry.contentsType));
            }

            HttpGet get = new HttpGet(URIUtil.encodeQuery(String.format(requestUrl, word)));
            HttpResponse response = httpClient.execute(target, get);
            HttpEntity entity = response.getEntity();
            contents = EntityUtils.toString(entity);

            PreparedStatement insertStatement = connection.prepareStatement(insertionQuery);
            insertStatement.setString(1, word);
            insertStatement.setString(2, contents);
        } catch (SQLException | IOException  e) {
            e.printStackTrace();
           /// System.exit(7777);
        }
        return new ThesaurusEntry(gson.fromJson(contents, ThesaurusEntry.contentsType));
    }

    public static ThesaurusService getInstance() {
        return ThesaurusService.theInstance;
    }
}
