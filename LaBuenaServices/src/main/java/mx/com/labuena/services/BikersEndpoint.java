/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package mx.com.labuena.services;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import mx.com.labuena.services.responses.BikersResponse;
import mx.com.labuena.services.tos.Biker;
import mx.com.labuena.services.tos.Location;

@Api(
        name = "bikers",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "services.labuena.com.mx",
                ownerName = "services.labuena.com.mx",
                packagePath = ""
        )
)
public class BikersEndpoint {
    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Biker biker) {

    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BikersResponse getAll() {
        List<Biker> bikers = getBikers();
        return new BikersResponse(bikers);
    }


    private List<Biker> getBikers() {
        List<Biker> bikers = new ArrayList<>();
        Connection conn = null;

        try {
            String url;
            if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
                url = System.getProperty("ae-cloudsql.cloudsql-database-url");
                try {
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                } catch (ClassNotFoundException exception) {
                    throw new ServletException("Error loading Google JDBC Driver", exception);
                }
            } else {
                url = System.getProperty("ae-cloudsql.local-database-url");
            }

            String bikersQuery = "select email, phone, stock from biker";


            conn = DriverManager.getConnection(url);
            ResultSet rs = conn.prepareStatement(bikersQuery).executeQuery();
            while (rs.next()) {
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int stock = rs.getInt("stock");

                Location lastLocation = new Location();
                bikers.add(new Biker("Clemente", email, phone, lastLocation, stock));
            }
            rs.close();
            conn.close();
        } catch (ServletException e) {
            e.printStackTrace();
            Biker biker = new Biker();
            biker.setName(e.getMessage());
            bikers.add(biker);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            Biker biker = new Biker();
            biker.setName(e.getMessage());
            bikers.add(biker);
        }
        return bikers;
    }

}
