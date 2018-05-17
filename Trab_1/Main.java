import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	static Scanner input = new Scanner(System.in);
	static String sequenceA;
	static String sequenceB;
	static Global global = null;
	static Local local = null;
//	static SemiGlobal semiglobal = null;

	/**
	 * Menu para a introdução das sequencias 
	 */
	public static void menuInicial() throws IOException{
		System.out.println();
		System.out.println("************************************************");
		System.out.println("Qual destas opções pretende?");
		System.out.println("1 - Introduzir sequências manualmente");
		System.out.println("2 - Pesquisa  Online");
		System.out.println("3 - Comparar sequências a partir de ficheiro");
		System.out.println("************************************************");
		System.out.print("A opção é: ");
		int escolha=input.nextInt();
		switch(escolha){
		case 1:
			introduzirSequencias();
			menuAlinhamentos_Offline();
			break;

		case 2:
			System.out.println("************************************************");
			System.out.println("Escolha o que pretende pesquisar.");
			System.out.println("1 - Proteina");
			System.out.println("2 - Gene");
			System.out.print("A opção é: ");
			int escolha_2=input.nextInt();
			switch(escolha_2){
			case 1:
				Client_Pesquisa pesq_prot = new Client_Pesquisa();
				pesq_prot.procura("Proteina");
				break;
			case 2: 
				Client_Pesquisa pesq_gene = new Client_Pesquisa();
				pesq_gene.procura("Gene");
				break;
			}
			break;
			
		case 3:
			System.out.println("************************************************");
			System.out.print("Indique o nome do ficheiro 1: ");
			String ficheiro1 = input.next();
			
			System.out.print("Indique o nome do ficheiro 2: ");
			String ficheiro2 = input.next();

			final String caminho =  System.getProperty("user.dir")+"/";
			File arquivo1 = new File(caminho+ficheiro1);
			File arquivo2 = new File(caminho+ficheiro2);
			
			if(arquivo1.exists()){
				FileReader fr = new FileReader( arquivo1 );
				BufferedReader br = new BufferedReader( fr );

				sequenceA = br.readLine();

				br.close();
				fr.close();
			}
			
			
			if(arquivo2.exists()){
				FileReader fr = new FileReader( arquivo2 );
				BufferedReader br = new BufferedReader( fr );

				sequenceB = br.readLine();

				br.close();
				fr.close();
			}
			

			else{
				System.out.println("O ficheiro não existe!!");
			}
			System.out.println("************************************************");
			System.out.println("Qual destas opções pretende?");
			System.out.println("1 - Comparação Offline");
			System.out.println("2 - Comparação Online");
			System.out.println("************************************************");
			System.out.print("A opção é: ");
			escolha_2=input.nextInt();
			
			if ( escolha_2 == 1) {
				menuAlinhamentos_Offline();
			}
			else {
				System.out.println("************************************************");
				System.out.println("Os Ficheiros escolhidos contêm ...");
				System.out.println("1 - Proteinas");
				System.out.println("2 - Gene");
				System.out.println("************************************************");
				System.out.print("A opção é: ");
				escolha_2=input.nextInt();
				if ( escolha_2 == 1 ) {
					menuAlinhamentos_Online(sequenceA,sequenceB,"protein");
				}
				else {
					menuAlinhamentos_Online(sequenceA,sequenceB,"dna");
				}
				
			}
			break;

		default:
			System.out.println("Opção inválida");
		}

	}


	/*
	//Menu para a escolha do alinhamento e consequente resultado
	*/

	public static void menuAlinhamentos_Offline() throws IOException{
		System.out.println("Qual é o alinhamente que pretende fazer?");
		System.out.println("1 - Alinhamento global");
		System.out.println("2 - Alinhamento local");
		System.out.print("O alinhamento é: ");
		int alinhamento = input.nextInt();
		long startTime = System.nanoTime();															//initiate timer
		switch(alinhamento){
			case 1:
				System.out.println("************************************************");
				alinhamentoGlobal();
				System.out.println("");
				//System.out.println("MATRIZ:");
				//global.imprimirMatriz();
				System.out.println("************************************************");
				break;
			
			case 2:			
				System.out.println("************************************************");
				alinhamentoLocal();
				System.out.println("");
				//System.out.println("MATRIZ:");
				//local.imprimirMatriz();
				System.out.println("************************************************");
				break;
				
			default:
			System.out.println("Opção inválida");
		}
		long endTime = System.nanoTime();														//end timer
		long totalTime = endTime - startTime;	
		double seconds = (double)totalTime / 1000000000.0;												
		System.out.println(seconds +" segundos");
	}
	
	public static void menuAlinhamentos_Online(String sequenceA, String sequenceB,String tipo) throws IOException{
		try {
		Client_Comparar align = new Client_Comparar();
		long startTime = System.nanoTime();	
		align.comparar(sequenceA,sequenceB,tipo) ;
		long endTime = System.nanoTime();														//end timer
		long totalTime = endTime - startTime;	
		double seconds = (double)totalTime / 1000000000.0;												
		System.out.println(seconds +" segundos");
	}
		catch(Exception e){
		}
	}
	



	/**
	 * Introdução das sequências manualmente
	 */
	private static void introduzirSequencias(){
		System.out.print("1a Sequencia: ");
		sequenceA=input.next();
		System.out.print("2a Sequencia: ");
		sequenceB=input.next();
	}

	/**
	 * Função para o alinhamento global
	 */
	private static void alinhamentoGlobal(){
		global = new Global(sequenceA,sequenceB);
		System.out.println("MATRIZ:");
		global.imprimirMatriz();
		global.alinhamento();
	}

	/**
	 * Função para o alinhamento local
	 */
	private static void alinhamentoLocal(){
		local = new Local(sequenceA,sequenceB);
		System.out.println("MATRIZ:");
		local.imprimirMatriz();
		local.alinhamento();
	}
	
	public static void main(String args[]) throws IOException{
		menuInicial();
	}
}
