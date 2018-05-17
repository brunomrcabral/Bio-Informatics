import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.util.Scanner ;



public class Client_Pesquisa {

	public void procura(String tipo) {
		
		Scanner input = new Scanner(System.in);
		System.out.print("Introduza um Input: ");
		String param = input.next();
		URL url ;
		
	  try {
		  if ( tipo.equals("Proteina")){
			  url = new URL("https://www.uniprot.org/uniprot/"+param+".fasta ");
			}
		  else  {
			  url = new URL("https://www.ebi.ac.uk/ena/data/view/"+param+"&display=fasta ");
			}
		  
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			if ( tipo.equals("Proteina")){
				System.out.println("O input dado não corresponde a uma sequência de Proteina");
			}
		  else  {
				System.out.println("O input dado não corresponde a uma sequência de Gene");
			}
			
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));
		
		String output;
		FileWriter fileStream = new FileWriter(param+".txt");
		BufferedWriter out = new BufferedWriter(fileStream);
		System.out.println("\n\n\nFicheiro "+param+".txt gerado \n\n\n");
		br.readLine();												//ignore first line
		while ((output = br.readLine()) != null) {
			out.write(output);
		}
		
		out.close();
		conn.disconnect();

	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }

	}

}
