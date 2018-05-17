import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.util.Scanner ;
import javax.net.ssl.HttpsURLConnection;



public class Client_Comparar {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private Client_Comparar http;
	

public void comparar(String sequenceA, String sequenceB, String tipo) throws Exception {

		Client_Comparar http = new Client_Comparar();
		System.out.println("\nPedido Post - Obter Job_id ");
		String job_id = http.sendPost(sequenceA,sequenceB,tipo);
		
		System.out.println("\n Pedido Get - Obter o Status do Pedido");
		String resposta = http.sendGet_Status(job_id);
		
		while( !resposta.equals("FINISHED")){
			resposta = http.sendGet_Status(job_id);
		}
		
		System.out.println("\n Pedido Get - Obter o Resultado do Pedido");
		http.sendGet_Result(job_id);
	}
	
	private String sendPost(String sequenceA, String sequenceB,String tipo) throws Exception {

		String url = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/run";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "email=up201202369%40gmail.com&title=bioinfo&matrix=EBLOSUM62&gapopen=1&&alternatives=5&format=srspair&stype="+tipo+"&asequence="+sequenceA+"&bsequence="+sequenceB;
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nEnvio 'POST' request para URL : " + url);
		System.out.println("Post parametros : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
	
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String job_id = response.toString();
		//print result
		System.out.println(response.toString());
		return job_id;
	}
		
	
	
	//HTTP GET request
	private String sendGet_Status(String job_id) throws Exception {
		String url = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/status/"+job_id;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		int responseCode = con.getResponseCode();
		System.out.println("\nEnvio 'GET' request para URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		 in.close();
		
		System.out.println(response.toString());
		return response.toString();
	}
	
	//HTTP GET request
	private void sendGet_Result(String job_id) throws Exception {
		String url = "http://www.ebi.ac.uk/Tools/services/rest/emboss_needle/result/"+job_id+"/aln";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		int responseCode = con.getResponseCode();
		System.out.println("\nEnvio'GET' request para URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		BufferedWriter file = new BufferedWriter(new FileWriter("output.txt"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine+"\n");
			file.write(inputLine+"\n");
		}
		 file.close();
		 in.close();
		System.out.println("\n\n\nFicheiro output.txt gerado\n\n\n");
	}
}

