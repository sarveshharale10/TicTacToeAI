import java.util.*;
import java.io.*;
class Node{
	StringBuilder state;
	ArrayList<Node> childArr=new ArrayList<Node>();
	char lastUsed;
	Node(String S,char used){
		state=new StringBuilder(S);
		lastUsed=used;
	}
}
class QueueWrapper{
	ArrayDeque<Integer> q;
	QueueWrapper(){
		q=new ArrayDeque<Integer>();
	}
}
class TreeGenerator{
	Stack<Node> S=new Stack<Node>();
	int flag=0;
	char computer,human;
	String moveState=new String();
	TreeGenerator(char human,char computer){
		this.human=human;
		this.computer=computer;
	}
	boolean XWins(String S){
		for(int i=0;i<3;i++){
			if(S.charAt(i)=='X' && S.charAt(i+3)=='X' && S.charAt(i+6)=='X'){
				return true;
			}
		}
		int i=0;
		while(i<9){
			if(S.charAt(i)=='X' && S.charAt(i+1)=='X' && S.charAt(i+2)=='X'){
				return true;
			}
			i=i+3;
		}
		if(S.charAt(0)=='X' && S.charAt(4)=='X' && S.charAt(8)=='X'){
			return true;
		}
		if(S.charAt(2)=='X' && S.charAt(4)=='X' && S.charAt(6)=='X'){
			return true;
		}
		return false;
	}
	boolean OWins(String S){
		for(int i=0;i<3;i++){
			if(S.charAt(i)=='O' && S.charAt(i+3)=='O' && S.charAt(i+6)=='O')
			{
				return true;
			}
		}
		int i=0;
		while(i<9){
			if(S.charAt(i)=='O' && S.charAt(i+1)=='O' && S.charAt(i+2)=='O'){
				return true;
			}
			i=i+3;
		}
		if(S.charAt(0)=='O' && S.charAt(4)=='O' && S.charAt(8)=='O'){
			return true;
		}
		if(S.charAt(2)=='O' && S.charAt(4)=='O' && S.charAt(6)=='O'){
			return true;
		}
		return false;
	}	
	boolean boardFilled(String S){
		for(int i=0;i<S.length();i++){
			if(S.charAt(i)=='-')
			{
				return false;
			}
		}
		return true;
	}	
	boolean gameDraw(String S){
		if(boardFilled(S) && !XWins(S) && !OWins(S)){
			return true;
		}
		return false;
	}
	boolean gameEnds(String S){
		if(XWins(S) || OWins(S) || gameDraw(S)){
			return true;
		}
		return false;
	}
	char getOppSymbol(char c){
		if(c=='X'){
			return 'O';
		}
		else{
			return 'X';
		}
	}
	QueueWrapper getBlanks(Node X){
		QueueWrapper blankSpaces=new QueueWrapper();
		String S=X.state.toString();
		for(int i=0;i<S.length();i++){
			if(S.charAt(i)=='-'){
				blankSpaces.q.add(i);
			}
		}
		return blankSpaces;
	}	
	void generateTree(Node X){
		QueueWrapper blankSpaces;
		Stack<QueueWrapper> qStack=new Stack<QueueWrapper>();
		Stack<Integer> temp=new Stack<Integer>();
		if(gameEnds(X.state.toString())){
			if(!qStack.empty()){
				qStack.pop();
			}
			if(!temp.empty()){
				temp.pop();
			}
		}
		else{
			blankSpaces=getBlanks(X);
			int size=blankSpaces.q.size();
			for(int i=0;i<size;i++){
				Node newNode=new Node(X.state.toString(),X.lastUsed);
				newNode.state.setCharAt(blankSpaces.q.peekFirst(),getOppSymbol(X.lastUsed));
				newNode.lastUsed=getOppSymbol(X.lastUsed);
				blankSpaces.q.removeFirst();
				X.childArr.add(newNode);
				qStack.push(blankSpaces);
				temp.push(size);
				generateTree(newNode);
			}
		}
	}
	int utility(String S){
		if(XWins(S)){
			if(computer=='X')
				return 1;
			else
				return -1;
		}
		else if(OWins(S)){
			if(computer=='X')
				return -1;
			else
				return 1;
		}
		return 0;
	}
	int miniMaxX(Node X){
		int max=-1;
		if(gameEnds(X.state.toString())){
			return utility(X.state.toString());
		}
		else if(X.lastUsed=='X'){
			int min=1;
			int mmVal;
			for(Node N:X.childArr){
				mmVal=miniMaxX(N);
				if(min>mmVal){
					min=mmVal;
				}
			}
			return min;
		}
		else if(X.lastUsed=='O'){
			int mmVal;
			for(Node N:X.childArr){
				mmVal=miniMaxX(N);
				if(max<mmVal){
					max=mmVal;
				}
			}
		}
		return max;
		
	}
	int miniMaxO(Node X){
		int min=1;
		if(gameEnds(X.state.toString())){
			return utility(X.state.toString());
		}
		else if(X.lastUsed=='X'){
			int max=-1;
			int mmVal;
			for(Node N:X.childArr){
				mmVal=miniMaxO(N);
				if(max<mmVal){
					max=mmVal;
				}
			}
			return max;
		}
		else if(X.lastUsed=='O'){
			int mmVal;
			for(Node N:X.childArr){
				mmVal=miniMaxO(N);
				if(min>mmVal){
					min=mmVal;
				}
			}
		}
		return min;
	}
}
class Game{
	StringBuilder board;
	TreeGenerator TGen;
	char human,computer;
	Game(){
		board=new StringBuilder("---------");
	}
	void decideFirstTurn(){
		System.out.print("Do you want to play first(Y/N):");
		Console c=System.console();
		String result=c.readLine();
		if(result.compareToIgnoreCase("Y")==0){
			human='X';
			computer='O';
		}
		else if(result.compareToIgnoreCase("N")==0){
			human='O';
			computer='X';
		}
		else{
			decideFirstTurn();
		}
		TGen=new TreeGenerator(human,computer);
	}
	int getMove(String current,String newState){
		for(int i=0;i<current.length();i++){
			if(current.charAt(i)=='-' && newState.charAt(i)!='-'){
				return i;
			}
		}
		return -1;
	}
	void playerMove(){
		System.out.print("Your move:");
		Console c=System.console();
		int pos=Integer.parseInt(c.readLine());
		while(board.charAt(pos)!='-'){
			System.out.print("Your move:");
			pos=Integer.parseInt(c.readLine());
		}
		board.setCharAt(pos,human);
		displayBoard();
	}
	void computerMove(){
		Node N=new Node(board.toString(),human);
		TGen.generateTree(N);
		int best=0;
		if(computer=='X'){
			for(int i=0;i<(N.childArr.size()-1);i++){
				if(TGen.miniMaxX(N.childArr.get(i))<TGen.miniMaxX(N.childArr.get(i+1))){
					best=i+1;
				}
			}
		}
		if(computer=='O'){
			for(int i=0;i<(N.childArr.size()-1);i++){
				if(TGen.miniMaxO(N.childArr.get(i))<TGen.miniMaxO(N.childArr.get(i+1))){
					best=i+1;
				}
			}
		}
		int pos=getMove(N.state.toString(),N.childArr.get(best).state.toString());
		board.setCharAt(pos,computer);
		System.out.println("Computer's move:"+pos);
		displayBoard();
	}
	void displayBoard(){
		for(int i=0;i<board.length();i++){
			if(i%3==0 && i!=0){
				System.out.println();
			}
			System.out.print(board.charAt(i)+" ");
		}
		System.out.println();
		System.out.println();
	}
	void playX(){
		if(computer=='X'){
			computerMove();
		}
		else{
			playerMove();
		}
	}
	void playO(){
		if(computer=='O'){
			computerMove();
		}
		else{
			playerMove();
		}
	}
	void playGame(){
		decideFirstTurn();
		while(true){
			playX();
			if(TGen.gameEnds(board.toString())){
				if(computer=='X' && TGen.XWins(board.toString())){
					System.out.println("Computer Wins");
				}
				else{
					System.out.println("Game Draw");
				}
				break;
			}
			playO();
			if(TGen.gameEnds(board.toString())){
				if(computer=='O' && TGen.OWins(board.toString())){
					System.out.println("Computer Wins");
				}
				else{
					System.out.println("Game Draw");
				}
				break;
			}
		}
	}
}
class TicTacToeAI{
	public static void main(String args[]){
		Game TicTac=new Game();
		TicTac.playGame();
	}
}
