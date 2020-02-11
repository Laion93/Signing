import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.ProxySelector;


class Signer{
	
	// one instance, reuse
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .proxy(ProxySelector.of(new InetSocketAddress("zen.es.hphis.com", 8080)))
            .cookieHandler(new CookieManager())
            .build();

    public static void main(String[] args) throws Exception {

        Signer obj = new Signer();

        System.out.println("starting");
        obj.start();

        Thread.sleep(ThreadLocalRandom.current().nextInt(3000,8000));

        System.out.println("logging in");
        obj.logIn();

        Thread.sleep(ThreadLocalRandom.current().nextInt(3000,8000));

        System.out.println("adding registry");
        obj.sign();

    }

    private void start() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://extranet.experienceis.com"))
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36") // add request header
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

    }

    private void logIn() throws Exception {

        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("log", "log");
        data.put("pwd", "pwd");
        data.put("submit", "Acceder");
        data.put("redirect_to", "https://extranet.experienceis.com/wp-admin/");
		data.put("testcookie", "1");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("https://extranet.experienceis.com/login/"))
				.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36") // add request header
                .header(
						"Content-Type", "application/x-www-form-urlencoded"
						)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response headers
        System.out.println(response.headers());

        // print response body
        System.out.println(response.body());


    }

	private void sign() throws Exception{
		
		Map<Object, Object> data = new HashMap<>();
        data.put("action", "add_registry");

		HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("https://extranet.experienceis.com/wp-admin/admin-ajax.php"))
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36") // add request header
                .headers(
						"Content-Type", "application/x-www-form-urlencoded"
						)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response headers
        System.out.println(response.headers());

        // print response body
        System.out.println(response.body());

	}

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}