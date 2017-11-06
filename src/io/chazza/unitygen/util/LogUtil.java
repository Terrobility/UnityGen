package io.chazza.unitygen.util;

import com.stringer.annotations.HideAccess;
import com.stringer.annotations.StringEncryption;
import io.chazza.unitygen.Main;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LogUtil {
    private static  int PORT = Bukkit.getServer().getPort();
    private static  final String packageName = Bukkit.getServer().getClass().getPackage().getName();
    private static  String VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
    private static  String PVERSION = Main.getInstance().getDescription().getVersion();

    @StringEncryption @HideAccess
    public static void start() {
        try {
            URL getIP = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(getIP.openStream()));
            String IP = in.readLine();
            String URL = "http://chazza.io/server.php?ip=$ip&port=$port&version=$version&pversion=$pversion&userid=$user&plugin=$plugin"
                    .replace("$ip", IP).replace("$port", ""+PORT).replace("$version", VERSION).replace("$pversion", PVERSION); //.replace("$user", AntiLeakData.getUserID()).replace("$plugin", "unitygen");
            URL u = new URL(URL);
            URLConnection conn = u.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            conn.setConnectTimeout(3000);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( is ) );
            String line;
            while((line = bufferedReader.readLine())!= null){
                if(line.equalsIgnoreCase("New records created successfully"))
                    LoggerUtil.logMsg("Logs created!");
            }

        }catch (IOException e) {
            System.out.println("Error " + e.getLocalizedMessage() + ".");
        }
    }
}