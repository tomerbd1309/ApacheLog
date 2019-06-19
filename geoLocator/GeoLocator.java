import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;

public class GeoLocator implements AutoCloseable {
    private final DatabaseReader databaseReader;

    public GeoLocator() throws IOException {
        
    	final URL url = getClass().getClassLoader().getResource("GeoLite2-Country.mmdb");
        if (url == null) {
            throw new IOException("File not found!");
        }
        final String urlFile = url.getFile();
        final File file = new File(urlFile);
        databaseReader = new DatabaseReader
                .Builder(file)
                .build();
    }

    public String getCountryByIp(String ip) throws TaboolaException {
        try {
            final InetAddress inetAddress = InetAddress.getByName(ip);
            final CountryResponse countryResponse = databaseReader.country(inetAddress);
            return countryResponse.getCountry().getName();
        } catch (Exception e) {
            throw new TaboolaException(e);
        }
    }

    @Override
    public void close() throws IOException {
        databaseReader.close();
    }
}
