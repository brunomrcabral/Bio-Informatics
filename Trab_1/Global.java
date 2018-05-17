public class Global {

	String sequenceA;
	String sequenceB;
	Matriz matriz[][];
	
	int match = 1, substitution = -1, gap = -2; 
	
	Global(String s1, String s2){
		sequenceA = "_"+s1;
		sequenceB = "_"+s2;
		inicializarMatriz();
		calcularScores();
	//	imprimirMatriz();
	}
	

	private void inicializarMatriz() {

		
		matriz = new Matriz[sequenceA.length()][sequenceB.length()];
		
		matriz[0][0] = new Matriz("ORIGEM", 0);

		// PREENCHER PRIMEIRA LINHA
		for (int j = 1; j < sequenceB.length(); j++)
			matriz[0][j] = new Matriz("", gap*j);

		// PREENCHER PRIMEIRA COLUNA
		for (int i = 1; i < sequenceA.length(); i++)
			matriz[i][0] = new Matriz("", gap*i);
	}
	
	
/*
* 
* */

	private void calcularScores() {

		int scoreLeft, scoreUp, scoreDiag;

		for (int i = 1; i < sequenceA.length(); i++) {
			for (int j = 1; j < sequenceB.length(); j++) {
			
				scoreLeft = matriz[i][j - 1].getValue() + gap;
				scoreUp = matriz[i - 1][j].getValue() + gap;
				
				if (sequenceA.charAt(i) == sequenceB.charAt(j))
					scoreDiag = matriz[i - 1][j - 1].getValue() + match;
				else
					scoreDiag = matriz[i - 1][j - 1].getValue() + substitution;
					
				//qual escolher em caso de empate ???
				if(scoreLeft>scoreUp && scoreLeft>scoreDiag)
					matriz[i][j]=new Matriz("left",scoreLeft);
				
				else if(scoreUp>=scoreLeft && scoreUp>=scoreDiag)
					matriz[i][j]=new Matriz("up",scoreUp);
				
				else if(scoreDiag>=scoreLeft && scoreDiag>scoreUp)
					matriz[i][j]=new Matriz("diag",scoreDiag);
				}
			}
		}
		
/*
 * 
*/

	public void alinhamento(){
		
		int line = sequenceA.length()-1;
		int row = sequenceB.length()-1;
		
		StringBuilder r1, r2;
		r1 = new StringBuilder();
		r2 = new StringBuilder();

		while(line>0 || row>0){
			//System.out.printf("Line: %d \t Row: %d Posiçao atual %s\n",line,row,matriz[line][row].getDirection());
			if (matriz[line][row].getDirection().isEmpty()) {
				r1.insert(0, sequenceA.charAt(line));
				r2.insert(0, sequenceB.charAt(row));
				row  = 0 ; 
				line = 0 ; 
			}
			
			else if(matriz[line][row].getDirection().equals("diag") ){
				r1.insert(0, sequenceA.charAt(line));
				r2.insert(0, sequenceB.charAt(row));
				line--;
				row--;
			}
			
			else if(matriz[line][row].getDirection().equals("up")){
				r1.insert(0, sequenceA.charAt(line));
				r2.insert(0, "_");
				line--;
			}
			
			else if(matriz[line][row].getDirection().equals("left")){
				r1.insert(0, "_");
				r2.insert(0, sequenceB.charAt(row));
				row--;
			}
		

		}
		
		int size_r1=0 , size_r2=0, score=0;
		for ( int i = 0 ; i < r1.length(); i++) { 
			if ( r1.charAt(i) != '_') {
				size_r1++;
			}
			if ( r2.charAt(i) != '_') {
				size_r2++;
			}
			
// MARTELO DO 5 
			if ( r1.charAt(i) == r2.charAt(i)) {
				score = score + 5;
			}
			else score = score - 5;
		}
		
		System.out.println();
		
		//----------------------------------------------------------
		StringBuilder allign;
		allign = new StringBuilder();
		for(int i=0 ; i < r1.toString().length() ; i++){
			if(r1.toString().charAt(i) == r2.toString().charAt(i))
				allign.insert(i , "|");
			else 
				allign.insert(i, " " );
		}
		
		//----------------------------------------------------------
		System.out.println();
		System.out.println("RESULTADO DO ALINHAMENTO Global:");
		
		int levels = r1.length()/50 ;
		int start=0, end=50;
		
		if(levels==0){
			System.out.println("\t"+r1.toString()+"\t\t\t"+size_r1);
			System.out.println("\t"+allign.toString());
			System.out.println("\t"+r2.toString()+"\t\t\t"+size_r2);
		}
		
		for(int l=0 ; l<levels ; l++) {
			
			CharSequence cSeq1 = r1.subSequence(start, end);
			System.out.println("\t"+cSeq1+"\t\t\t"+ 50*(l+1) );
			
			CharSequence cAllign = allign.subSequence(start, end);
			System.out.println("\t"+cAllign);
		
			CharSequence cSeq2 = r2.subSequence(start, end);
			System.out.println("\t"+cSeq2+"\t\t\t"+ 50*(l+1) );
			
			start=end;
			end+=50;
			
			if(end > size_r1){
				end=size_r1;
				
				cSeq1 = r1.subSequence(start, end);
				System.out.println("\t"+cSeq1+"\t\t\t"+ end);
				
				cAllign = allign.subSequence(start, end);
				System.out.println("\t"+cAllign);
			
				cSeq2 = r2.subSequence(start, end);
				System.out.println("\t"+cSeq2+"\t\t\t"+ end );
				
			}
			System.out.println();
			
		}
		
		System.out.println("Score:\t"+score);
	}
	
/*
 * 
*/
public void imprimirMatriz() {
	System.out.print(String.format("%5s", ""));
	for (int i = 0; i < sequenceB.length(); i++) {
		
		System.out.print(String.format("%5s" , sequenceB.charAt(i)+ " "));
	}
	
	System.out.println();
	for (int i = 0; i < sequenceA.length(); i++) {
		System.out.print(String.format("%5s" , sequenceA.charAt(i)+ " "));
		for (int j = 0; j < sequenceB.length(); j++)
			
			if (matriz[i][j] != null)
				System.out.print(String.format("%5s" , matriz[i][j].getValue() + " "));
		System.out.println();
	}
}
}

