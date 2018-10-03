import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


public class Arvores_UPGMA {

	static Scanner in = new Scanner(System.in);
	static int matriz [][];
	static String seq [];

	static int cont = 1;

	static String seqInicial[];
	static int tamSeqInicial;
	static int matrizInicial[][];
	final static float taxaSucesso = (float) 0.30;

	static FileReader fr;
	static BufferedReader br;
	static String caminho;
	static File arquivo;
	static String ficheiro;
	
	static LinkedList <String> resultado = new LinkedList<String>();

	private static void menuInicial() throws IOException {
		System.out.println("Introduza os caracteres usados separados por '-': ");
		String tmp = in.next();
		seq = tmp.split("-");
		seqInicial = seq.clone();
		tamSeqInicial = seqInicial.length;
		System.out.println("Introduza a matriz de distâncias que pretende analisar: ");
		matriz = new int [seq.length][seq.length];

		for (int linha = 0 ; linha < seq.length ; linha++){
			for ( int coluna = 0 ; coluna < seq.length; coluna++){
				int aux = in.nextInt();
				matriz[linha][coluna] = aux;
			}
		}
		matrizInicial=matriz.clone();
		inserir_Folhas();
		imprimir_Niveis();
		System.out.println();
		System.out.println("*************Passo Inicial*****************************");
		imprimirMatriz();
		System.out.println();
		menuEscolha();
		
	}

	public static void menuEscolha() throws IOException{
		System.out.println("Escolha uma das seguintes opcoes:");
		System.out.println("1 - Construir arvore (Algoritmo UPGMA)");
		System.out.println("2 - Verificar distancias ultrametricas");
		System.out.print("Escolha: ");
		int escolha=in.nextInt();
		long startTime = System.nanoTime();               //start timer
		switch(escolha){
		case 1:
			algoritmo_UPGMA();
			imprimir_Niveis();
			System.out.println();
			break;

		case 2:
			distancia_Ultrametrica();
			System.out.println();
			break;
		}
		long endTime = System.nanoTime();	   			  //end timer
		long totalTime = endTime - startTime;	
		double seconds = (double)totalTime / 1000000000.0;												
		System.out.println("Demorou: " + seconds +" segundos");
		System.out.println();
		
	}

	// Algoritmo de UPGMA
	 
	public static void algoritmo_UPGMA(){

		while(seq.length>1){
			// Encontrar o Minimo

			int min = Integer.MAX_VALUE;
			int linha = -1, coluna = -1;

			for(int i=0; i<seq.length; i++){
				for(int j=0; j<seq.length; j++){
					if(matriz[i][j]!=0 && matriz[i][j]<min){
						min=matriz[i][j];
						linha=i;
						coluna=j;
					}
				}
			}
			// System.out.println("Min: "+min+"\nLinha: "+linha+"\nColuna: "+coluna);

			if(linha!=-1 && coluna!=-1){
				seq[linha]+=seq[coluna];
				seq[coluna]="-";

				String copia[] = new String[seq.length-1];
				int j=0; 

				for(int i=0; i<seq.length;i++){

					if(!seq[i].equals("-") && i!=coluna){
						copia[j]=seq[i];
						j++;
					}
				}
				seq=copia;

				for ( int k = 0 ; k < matriz.length; k++) {

					if ( k != linha && k != coluna) {
						// System.out.println("Numero "+k+ " k: "+k+" Elim "+coluna);
						int tmp = (matriz[linha][k]+matriz[coluna][k])/2;
						
						matriz[k][linha] = tmp;
						matriz[linha][k] = tmp;

					}
				}

				gerarNovaMatriz(coluna);
				inserir_Folhas();
				System.out.println();
				System.out.println("*************Matriz *****************************");
				System.out.println("*************Passo "+cont+"*****************************");
				cont++;
				imprimirMatriz();
			}
		}
	}

	
	//  A cada iteração um nova matriz é gerada 
	//  Parametro eliminar - Coluna e linha a eliminar
	 
	public static void gerarNovaMatriz(int eliminar){

		int i=0, j=0;

		int novaMatriz[][] = new int [seq.length][seq.length];

		for(int linha=0; linha<novaMatriz.length; linha++){
			for(int coluna=0; coluna<novaMatriz.length; coluna++){

				if(i==eliminar)
					i++;

				if(j==eliminar)
					j++;

				novaMatriz[linha][coluna]=matriz[i][j];
			//	System.out.print(novaMatriz[linha][coluna]+" ");
				j++;
			}
		//	System.out.println("");
			i++;
			j=0;
		}
		matriz=novaMatriz;
	}

	
	// Calcula as distancias ultrametricas e o respectivo sucesso do calculo
	 
	public static void distancia_Ultrametrica(){

		LinkedList <int[]> tripletos = new LinkedList<>();

		int totalCalculos = 0;
		int sucesso = 0;

		// Calcula os tripleto e adiciona-os a lista tripletos
		for(int i=0; i<tamSeqInicial; i++)
			for(int j=0; j<tamSeqInicial; j++)
				for(int k=0; k<tamSeqInicial; k++){

					if(i!=j && i!=k && j!=k){
						int aux [] = {i,j,k};
						tripletos.add(aux);
					}
				}

				System.out.println();


		// Obtem todos os pares para cada tripleto e descobre qual a distância mínima na matriz

				for(int n[] : tripletos){

					int distanciaMin=Integer.MAX_VALUE;
					int indiceMin=-1;

					int distMax1=-1, distMax2=-1;

			int distancias[]= new int[3]; //0-(1,2) 1-(1,3) 2-(2,3)

			if(matrizInicial[n[0]][n[1]]==0)
				distancias[0] = matrizInicial[n[1]][n[0]];
			else
				distancias[0] = matrizInicial[n[0]][n[1]];

			if(matrizInicial[n[0]][n[2]]==0)
				distancias[1] = matrizInicial[n[2]][n[0]];
			else
				distancias[1] = matrizInicial[n[0]][n[2]];

			if(matrizInicial[n[1]][n[2]]==0)
				distancias[2] = matrizInicial[n[2]][n[1]];
			else
				distancias[2] = matrizInicial[n[1]][n[2]];

			for(int i=0; i<distancias.length;i++){
				if(distancias[i]<distanciaMin){
					distanciaMin=distancias[i];
					indiceMin = i;
				}
			}

			for(int i=0; i<distancias.length;i++){
				if(i!=indiceMin){
					distMax1=distMax2;
					distMax2=distancias[i];
				}	
			}

			int divisor;
			if(distMax1>distMax2) divisor=distMax1;
			else if(distMax2>distMax1) divisor=distMax2;
			else divisor=distMax1;
			
			float modulo = Math.abs(distMax1-distMax2)/(float)divisor;
			totalCalculos++;
			if(modulo<=taxaSucesso){
				sucesso++;
			}
		}

		System.out.println("Total de Calculos: "+totalCalculos);
		System.out.println("Sucesso: "+sucesso);
		System.out.println("Para uma taxa de "+taxaSucesso+" o resultado foi: "+(float)sucesso/(float)totalCalculos);

	}

	//      Prints 
	
	// Insere as folhas no nivél de profundidade da arvore correspondente 
	 
	public static void inserir_Folhas(){
		String folhas="";
		for(String s: seq){
			folhas+=s;
			folhas+="-";
		}

		folhas=folhas.substring(0,folhas.length()-1);
		resultado.add(folhas);
	}

	// Imprime a ligação das folhas para cada nível
	 
	public static void imprimir_Niveis(){
		int nivel=0;

		System.out.println("\n\nResultado Final.\n\n");
		for(String s: resultado){
			System.out.print("Nivel "+nivel+": ");
			System.out.println(s);
			nivel++;
		}
	}

	// Imprime a matriz de distâncias
	 
	public static void imprimirMatriz(){
		for(String s : seq)
			System.out.print(s+"\t  ");

		System.out.println();

		for(int l[] : matriz){
			for(int n : l)
				System.out.print(n+"\t  ");
			System.out.println();
		}
	}

	public static void main (String args[]) throws IOException{
		menuInicial();
	}

}
