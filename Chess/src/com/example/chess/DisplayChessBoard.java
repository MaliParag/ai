package com.example.chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import android.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayChessBoard extends ActionBarActivity {

	//All black must be odd 
	//All white must be even 
	
	protected static final int PAWN_BLACK = 99;
	protected static final int PAWN_WHITE = 98;
	protected static final int KNIGHT_BLACK = 97;
	protected static final int KNIGHT_WHITE = 96;
	protected static final int BISHOP_BLACK = 95;
	protected static final int BISHOP_WHITE = 94;
	protected static final int ROOK_BLACK = 93;
	protected static final int ROOK_WHITE = 92;
	protected static final int KING_BLACK = 91;
	protected static final int KING_WHITE = 90;
	protected static final int QUEEN_BLACK = 89;
	protected static final int QUEEN_WHITE = 88;
	protected static final int WHITE = 0;
	protected static final int BLACK = 1;
	protected static final int BLANK = -1;
	protected static final int INFINITY = Integer.MAX_VALUE;
	
	protected static final int SCORE_PAWN_BLACK = -100;
	protected static final int SCORE_PAWN_WHITE = 100;
	protected static final int SCORE_KNIGHT_BLACK = -330;
	protected static final int SCORE_KNIGHT_WHITE = 330;
	protected static final int SCORE_BISHOP_BLACK = -300;
	protected static final int SCORE_BISHOP_WHITE = 300;
	protected static final int SCORE_ROOK_BLACK = -500;
	protected static final int SCORE_ROOK_WHITE = 500;
	protected static final int SCORE_KING_BLACK = -20000;
	protected static final int SCORE_KING_WHITE = 20000;
	protected static final int SCORE_QUEEN_BLACK = -900;
	protected static final int SCORE_QUEEN_WHITE = 900;
	
	
	int randomNum;
	boolean blacksTurn=false; 
	int maxDepth=2;
	int numberOfMoves=0;
	int pieceToMove;
	int locationToMove;
	int phaseOfGame=0;
	int blackKingLocation,whiteKingLocation;
	boolean whiteFirst=false;
	
	
	ArrayList<ImageView> chessBoard;
	ArrayList<Integer> toRestore=new ArrayList<Integer>();
	ArrayList<Integer> toSaveKing;
	
	private final int[] views={
			R.id.a1,R.id.b1,R.id.c1,R.id.d1,R.id.e1,R.id.f1,R.id.g1,R.id.h1,
			R.id.a2,R.id.b2,R.id.c2,R.id.d2,R.id.e2,R.id.f2,R.id.g2,R.id.h2,
			R.id.a3,R.id.b3,R.id.c3,R.id.d3,R.id.e3,R.id.f3,R.id.g3,R.id.h3,
			R.id.a4,R.id.b4,R.id.c4,R.id.d4,R.id.e4,R.id.f4,R.id.g4,R.id.h4,
			R.id.a5,R.id.b5,R.id.c5,R.id.d5,R.id.e5,R.id.f5,R.id.g5,R.id.h5,
			R.id.a6,R.id.b6,R.id.c6,R.id.d6,R.id.e6,R.id.f6,R.id.g6,R.id.h6,
			R.id.a7,R.id.b7,R.id.c7,R.id.d7,R.id.e7,R.id.f7,R.id.g7,R.id.h7,
			R.id.a8,R.id.b8,R.id.c8,R.id.d8,R.id.e8,R.id.f8,R.id.g8,R.id.h8
							
	};
	
	
	// WHITE IS ON OTHER SIDE 
	
	private final int[] scoreKingPositions={
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-20,-30,-30,-40,-40,-30,-30,-20,
			-10,-20,-20,-20,-20,-20,-20,-10,
			 20, 20,  0,  0,  0,  0, 20, 20,
			 20, 30, 10,  0,  0, 10, 30, 20
	};
	private final int[] scoreKingPositionsEnd={
		-50,-40,-30,-20,-20,-30,-40,-50,
		-30,-20,-10,  0,  0,-10,-20,-30,
		-30,-10, 20, 30, 30, 20,-10,-30,
		-30,-10, 30, 40, 40, 30,-10,-30,
		-30,-10, 30, 40, 40, 30,-10,-30,
		-30,-10, 20, 30, 30, 20,-10,-30,
		-30,-30,  0,  0,  0,  0,-30,-30,
		-50,-30,-30,-30,-30,-30,-30,-50
	};
	
	private final int[] scorePawnPositions={
			 0,  0,  0,  0,  0,  0,  0,  0,
			 5, 10, 10,-20,-20, 10, 10,  5,
			 5, -5,-10,  0,  0,-10, -5,  5,
			 0,  0,  0, 20, 20,  0,  0,  0,
			 5,  5, 10, 25, 25, 10,  5,  5,
			 10, 10, 20, 30, 30, 20, 10, 10, 
			 50, 50, 50, 50, 50, 50, 50, 50,
			 0,  0,  0,  0,  0,  0,  0,  0
	};
	
	private final int[] scoreBishopPositions={
			-20,-10,-10,-10,-10,-10,-10,-20,
			-10,  5,  0,  0,  0,  0,  5,-10,
			-10, 10, 10, 10, 10, 10, 10,-10,
			-10,  0, 10, 10, 10, 10,  0,-10,
			-10,  5,  5, 10, 10,  5,  5,-10,
			-10,  0,  5, 10, 10,  5,  0,-10,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-20,-10,-10,-10,-10,-10,-10,-20
	};
	
	private final int[] scoreKnightPositions={
			-50,-40,-30,-30,-30,-30,-40,-50,
			-40,-20,  0,  0,  0,  0,-20,-40,
			-30,  0, 10, 15, 15, 10,  0,-30,
			-30,  5, 15, 20, 20, 15,  5,-30,
			-30,  0, 15, 20, 20, 15,  0,-30,
			-30,  5, 10, 15, 15, 10,  5,-30,
			-40,-20,  0,  5,  5,  0,-20,-40,
			-50,-40,-30,-30,-30,-30,-40,-50
	};
	
	private final int[] scoreRookPositions={
			 0,  0,  0,  5,  5,  0,  0,  0,
		    -5,  0,  0,  0,  0,  0,  0, -5, 
		    -5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			 5, 10, 10, 10, 10, 10, 10,  5,
			 0,  0,  0,  0,  0,  0,  0,  0
	};
	
	private final int[] scoreQueenPositions={
		-20,-10,-10, -5, -5,-10,-10,-20,
		-10,  0,  5,  0,  0,  0,  0,-10,
		-10,  5,  5,  5,  5,  5,  0,-10,
		  0,  0,  5,  5,  5,  5,  0, -5,
		 -5,  0,  5,  5,  5,  5,  0, -5,
		-10,  0,  5,  5,  5,  5,  0,-10,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-20,-10,-10, -5, -5,-10,-10,-20
	};
	
	char[] ChessBoard={
			'r','n','b','k','q','b','n','r', //black side lower values from 0 to 15
			'p','p','p','p','p','p','p','p',
			' ',' ',' ',' ',' ',' ',' ',' ',
			' ',' ',' ',' ',' ',' ',' ',' ',
			' ',' ',' ',' ',' ',' ',' ',' ',
			' ',' ',' ',' ',' ',' ',' ',' ',
			'P','P','P','P','P','P','P','P', //White side higher values
			'R','N','B','K','Q','B','N','R'
			
	};
	
	/*char[] ChessBoard={
			'r',' ',' ','k',' ','b','n','r', //black side lower values from 0 to 15
			'p','p','p',' ',' ',' ','p','p',
			' ',' ','b',' ',' ',' ',' ',' ',
			' ',' ',' ',' ',' ','p',' ',' ',
			' ',' ',' ','N',' ',' ',' ',' ',
			' ',' ',' ',' ',' ',' ',' ',' ',
			' ',' ',' ',' ',' ','P','P','P', //White side higher values
			' ',' ',' ',' ','K','B',' ','R'
			
	};*/
	
	
	int piece=-1;
	boolean selected=false;
	int previousSquare;
	//LongTimeConsumingOperation move=new LongTimeConsumingOperation();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_chess_board);
	//	Intent intent = getIntent();
	//	TextView textView = new TextView(this);
		
		initialize();
		createImageViewArray();
		
		Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    randomNum = rand.nextInt(3);
		analyze();
		CharSequence whenToPlay[] = new CharSequence[] {"Computer", "You"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Who will play first");
		builder.setItems(whenToPlay, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on whenToPlay[which]
		    					
		    	if(which == 0)
		    	{
		    		blacksTurn = false;
		    	}
		    	else
		    	{
		    		blacksTurn = true;
		    	}
		    	
		    	if(!blacksTurn)
				{
		    		dialog.cancel();
					whiteFirst=true;
		    		moveWhite();		
		    		
					TextView textView = (TextView)findViewById(R.id.textView1);
				    textView.setText("Black's Turn");
				    updateUI();
					
		    		blacksTurn = true;
				}
					
		    }
		});
		builder.show();
		
	}
	private void createImageViewArray() {
		// TODO Auto-generated method stub
		chessBoard = new ArrayList<ImageView>();
		
		for(int i=0;i<64;i++)
		{
			chessBoard.add((ImageView)findViewById(views[i]));
		}
		
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
		ImageView imageView =(ImageView) findViewById(R.id.a1);
		imageView.setOnClickListener(myListener);
		
		for(int i=1;i<64;i++)
		{
			imageView =(ImageView) findViewById(views[i]);
		 	imageView.setOnClickListener(myListener);
		}
		
	}
	
	private OnClickListener myListener = new OnClickListener() {
		
	    public void onClick(View v) {
	    	TextView textView = (TextView) findViewById(R.id.textView1);
	    	
	    	ArrayList<Integer> changedTags = new ArrayList<Integer>();
	    	restoreBoard();
	    	
	    	if( blacksTurn)
	    	{
	    		ImageView vtemp=(ImageView)v;
	    		changedTags = checkMoves(previousSquare);
	    		if(selected && checkPiece(previousSquare)%2==BLACK)
	    		{
	    		
	    			selected = false;
	    		
	    			int location=Integer.parseInt(vtemp.getTag().toString());
	    			
		    		if(changedTags.contains(location))
		    		{
		    			setBlank(previousSquare);
		    			movePiece(location);
			    		blacksTurn = false;
			    		
			    		analyze();
			    		textView.setText("White's Turn");
			    		updateUI();
		    		}
		    				    		
		    		changedTags.clear();
		    	}
	    			
				else
				{
					piece=checkPiece(Integer.parseInt(vtemp.getTag().toString()));
					changedTags=checkMoves(Integer.parseInt(vtemp.getTag().toString()));
					
					selected=true;
					highlight(changedTags);
					
				}
	    	}
	    	
	    	previousSquare = Integer.parseInt(v.getTag().toString());
	    	
	    	if(!blacksTurn)
	    	{
	    		
	    		moveWhite();
	    		updateUI();
	    		
	    		blacksTurn = true;

	    	}
	    }

	};
	
	private void restoreBoard() {
		// TODO Auto-generated method stub
    	ImageView temp;

		for(int i=0;i<toRestore.size();i++)
    	{	
    		temp = chessBoard.get(toRestore.get(i));
    		
    		if(temp.getBackground().getConstantState()==getResources().getDrawable(R.drawable.movable_white).getConstantState())
				temp.setBackgroundResource(R.drawable.white_box);
			else
				temp.setBackgroundResource(R.drawable.red_box);
    	}	
    	
		toRestore.clear();
	}
	
	private ArrayList<Integer> checkMoves(int location) {
		// TODO Auto-generated method stub
		TextView textView = (TextView)findViewById(R.id.textView1);
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		changedTags.clear();
		
		switch(checkPiece(location))
		{
			case PAWN_BLACK:
				textView.setText("Black Pawn");
				changedTags=checkMovesPawn(location);
				break;
			case BISHOP_BLACK:
				textView.setText("Black Bishop");
				changedTags=checkMovesBishop(location);
				break;
			case QUEEN_BLACK:
				textView.setText("Black Queen");
				changedTags=checkMovesQueen(location);
				break;
			case KNIGHT_BLACK:
				textView.setText("Black Knight");
				changedTags=checkMovesKnight(location);
				break;
			case KING_BLACK:
				textView.setText("Black King");
				changedTags=checkMovesKing(location);
				//changedTags.removeAll(checkMovesKingWhite(whiteKingLocation));
				break;
			case ROOK_BLACK:
				textView.setText("Black Rook");
				changedTags=checkMovesRook(location);
				break;
			case PAWN_WHITE:
				textView.setText("White Pawn");
				changedTags=checkMovesPawn(location);
				break;
			case BISHOP_WHITE:
				changedTags=checkMovesBishop(location);
				textView.setText("White Bishop");
				break;
			case QUEEN_WHITE:
				changedTags=checkMovesQueen(location);
				textView.setText("White Queen");
				break;
			case KNIGHT_WHITE:
				changedTags=checkMovesKnight(location);
				textView.setText("White Knight");
				break;
			case KING_WHITE:
				changedTags=checkMovesKing(location);
				//changedTags.removeAll(checkMovesKingBlack(blackKingLocation));
				textView.setText("White King");
				break;
			case ROOK_WHITE:
				changedTags=checkMovesRook(location);
				textView.setText("White Rook");
				break;
			
			default:
				if(blacksTurn)
				{
					textView.setText("Black's Turn");
				}
				else
				{
					textView.setText("White's Turn");
				}

		}
		return changedTags;
		

	}
	
	private ArrayList<Integer> getAttackedLocations(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		changedTags.clear();
		
		switch(checkPiece(location))
		{
			case PAWN_BLACK:
				changedTags=getAttackedLocationsPawn(location);
				break;
			case BISHOP_BLACK:
				changedTags=getAttackedLocationsBishop(location);
				break;
			case QUEEN_BLACK:
		
				changedTags=getAttackedLocationsQueen(location);
				break;
			case KNIGHT_BLACK:
		
				changedTags=getAttackedLocationsKnight(location);
				break;
			case KING_BLACK:
		
				changedTags=getAttackedLocationsKing(location);
				break;
			case ROOK_BLACK:
				
				changedTags=getAttackedLocationsRook(location);
				break;
		
			case PAWN_WHITE:
		
				changedTags=getAttackedLocationsPawn(location);
				break;
			case BISHOP_WHITE:
		
				changedTags=getAttackedLocationsBishop(location);
				break;
			case QUEEN_WHITE:
		
				changedTags=getAttackedLocationsQueen(location);
				break;
			case KNIGHT_WHITE:
				changedTags=getAttackedLocationsKnight(location);
				break;
			case KING_WHITE:
				changedTags=getAttackedLocationsKing(location);
				break;
			case ROOK_WHITE:
				
				changedTags=getAttackedLocationsRook(location);
				break;
			
		
		}
		return changedTags;
		

	}
	

	
	private ArrayList<Integer> safeKingMoves(int type) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();
		
		for(int i=0;i<64;i++)
		{
			
			if(getType(i)!=BLANK && getType(i)!=type)
			changedTags.addAll(getAttackedLocations(i));
	
		}
	
		return changedTags;
				
	}
	private int checkScore(int piece) {
		// TODO Auto-generated method stub
		
		switch(piece)
		{
			case PAWN_BLACK:
				return SCORE_PAWN_BLACK;
				
			case BISHOP_BLACK:
				return SCORE_BISHOP_BLACK;
				
			case QUEEN_BLACK:
				return SCORE_QUEEN_BLACK;
				
			case KNIGHT_BLACK:
				return SCORE_KNIGHT_BLACK;
				
			case KING_BLACK:
				return SCORE_KING_BLACK;
				
			case ROOK_BLACK:
				return SCORE_ROOK_BLACK;
				
			case PAWN_WHITE:
				return SCORE_PAWN_WHITE;
			
			case BISHOP_WHITE:
				return SCORE_BISHOP_WHITE;
				
			case QUEEN_WHITE:
				return SCORE_QUEEN_WHITE;
				
			case KNIGHT_WHITE:
				return SCORE_KNIGHT_WHITE;
				
			case KING_WHITE:
				return SCORE_KING_WHITE;
				
			case ROOK_WHITE:
				return SCORE_ROOK_WHITE;
			
			default:
				return 0;

		}
		

	}


	private void moveWhite() {
		// TODO Auto-generated method stub
		//currentDepth=0;
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		if(checkIfCheck(BLACK))
		{
			saveKing();
			
			changedTags.clear();
			return;
		}
		
		if(phaseOfGame==0 && whiteFirst==true)
		{
			opening();
			changedTags.clear();
			return;
		}
		
		pieceToMove = 0;
		locationToMove=0;
		
		max(0,Integer.MIN_VALUE,Integer.MAX_VALUE);
		bestMove();
		
		if(checkIfCheck(WHITE))
		{
			showAlert("OH!!","CHECK");
		}
		
		changedTags.clear();
		analyze();
	}
	
	private void analyze() {
		// TODO Auto-generated method stub
		
		int blackArmy[],whiteArmy[];
		blackArmy = new int[6];//PNBRKQ
		whiteArmy = new int[6];
		int piece;
		blackKingLocation=whiteKingLocation=-1;
		
		for(int i=0;i<64;i++)
		{
			piece=checkPiece(i);
			
			if(blackKingLocation==-1)
			{
				if(piece==KING_BLACK)
				{
					blackKingLocation=i;
				}
			}
			if(whiteKingLocation==-1)
			{
				if(piece==KING_WHITE)
				{
					whiteKingLocation=i;
				}
			}
			
			if(piece%2==WHITE)
			{
				whiteArmy[(PAWN_WHITE-piece)/2]++;
			}
			else if(piece%2==BLACK)
			{
				blackArmy[(PAWN_BLACK-piece)/2]++;
			}
		}
		
		if(blackArmy[4]<1)
		{
			showAlert("You Lost!!","Game Over");
		}
		if(whiteArmy[4]<1)
		{
			showAlert("You Won!!","Game Over");
		}
		
		//change the phase of game
		if(phaseOfGame!=3)
		{
			if(whiteArmy[5]<1 && blackArmy[5]<1)
			phaseOfGame = 3;
		}
		
	}
	
	private void showAlert(String chars,String title) {
		// TODO Auto-generated method stub
		CharSequence whenToPlay[] = new CharSequence[] {chars};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(whenToPlay, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on whenToPlay[which]
		    					
		    		dialog.cancel();
		    			
		    }
		});
		builder.show();

	}

	int[][] openings={
			{51,35,57,52,58,30},//Ruy Lopez
			{52,36,53,37},//The queens gambit
			{53,37},//The English opening
	};
	
	private void opening() {
		// TODO Auto-generated method stub
		
		int previous = openings[randomNum][numberOfMoves*2];
		
		//piece before modification
		piece = checkPiece(previous);
		setBlank(previous);
			
		movePiece(openings[randomNum][numberOfMoves*2+1]);
		numberOfMoves++;
		
		if(openings[randomNum].length<=numberOfMoves*2)
		{
			phaseOfGame = 1;
		}
		
	}
	
	private void saveKing() {
		// TODO Auto-generated method stub
		
		//Single check
		//Double check
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();
		int attackingPiece=BLANK;
		
		int kingAttackedBy=0;
		
		for(int i=0;i<64;i++)
		{
			changedTags.clear();
	
			if(getType(i)==BLACK)
			{
				changedTags=checkMoves(i);
			
				if(changedTags.contains(whiteKingLocation))
				{
					attackingPiece = i;
					kingAttackedBy++;
				}
			}
		}
		
		if(kingAttackedBy>1)
		{
			handleDoubleCheck();
		}
		else
		{
			handleSingleCheck(attackingPiece);
		}
	
	}
	

	private void handleDoubleCheck() {
		// TODO Auto-generated method stub
		
		showAlert("Double", "");
		
	}
	private void handleSingleCheck(int attackingPiece) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags=new ArrayList<Integer>();
		int v,l;
		v=Integer.MAX_VALUE;
		maxDepth=3;
		boolean flag=false;
		
		for(int i=0;i<64;i++)
		{
			if(getType(i)==WHITE)
			{
				changedTags.clear();
				changedTags=checkMoves(i);
				
				if(changedTags.contains(attackingPiece))
				{	
						
					 l=min(0,Integer.MIN_VALUE,Integer.MAX_VALUE);
					 
					 if(l<v)
					 {
						 flag=true;
						 v=l;
						 pieceToMove=i;
						 locationToMove=attackingPiece;
					 }
				}
			}
		}
		if(flag)
		{
			piece=checkPiece(pieceToMove);
			setBlank(pieceToMove);
			movePiece(attackingPiece);
		}
		else
		{
			changedTags.clear();
			changedTags=checkMoves(whiteKingLocation);
			v=Integer.MAX_VALUE;
			
			for(int k=0;k<changedTags.size();k++)
			{
				l=min(0,Integer.MIN_VALUE,Integer.MAX_VALUE);
				 
				 if(l<v)
				 {
					 flag=true;
					 v=l;
					 locationToMove=changedTags.get(k);
				 }
			}
			
			piece=KING_WHITE;
			setBlank(whiteKingLocation);
			pieceToMove=whiteKingLocation;
			movePiece(locationToMove);
		}
		
		
	}
	
	//Type is from which side you are thinking
	private boolean checkIfCheck(int type) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		for(int i=0;i<64;i++)
		{		
			if(checkPiece(i)%2==type)
			{
					changedTags=checkMoves(i);
			
					for(int j=0;j<changedTags.size();j++)
					{
		
						if(type==WHITE)
						{
							if(checkPiece(changedTags.get(j))==KING_BLACK)
							{
								blackKingLocation=changedTags.get(j);
								changedTags.clear();
								return true;
							}
						}
						else
						{
							if(checkPiece(changedTags.get(j))==KING_WHITE)
							{
								whiteKingLocation=changedTags.get(j);
								changedTags.clear();
								return true;
							}
				
						}
					}
			}
		}
	
		changedTags.clear();
		return false;
	}
	private int max(int currentDepth,int alpha,int beta)
	{
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		if(cutoffTest(currentDepth))
		{
			return evaluate();
		}
	
			
		int i=0,j=0;
		int v = Integer.MIN_VALUE;
		
		currentDepth++;
		int previous;		
		int oldPiece;
		int prevPiece;
		//For each piece
		for(i=0;i<64;i++)
		{
			if(checkPiece(i)%2==WHITE)
			{
				changedTags.clear();
				
				changedTags=checkMoves(i);
				
				//For each move
				for(j=0;j<changedTags.size();j++)
				{
					   
					previous = i;
					
					//piece before modification
					prevPiece=piece = checkPiece(previous);
					setBlank(previous);
					
					//before moving to temp which piece is present at temp
					int k=changedTags.get(j);
					oldPiece = checkPiece(k);
					
		    		movePiece(k);
					    		
		    		int l=min(currentDepth,alpha,beta);
		    		
		    		if(currentDepth==1)
		    		{
		    			if(v<l)
		    			{	v=l;
		    				pieceToMove = i;
		    				locationToMove=k;
		    			}
		    		}
		    		
		    		if(oldPiece==BLANK)
		    		{
						setBlank(k);
		    		}
		    		else
		    		{
		    			piece=oldPiece;
		    			movePiece(k);
		    		}
		    		
		    		piece=prevPiece;
		    		movePiece(previous);
		    		
		    		//Apply alpha beta pruning
		    		if(v>=beta)
		    		{
		    			changedTags.clear();
		    			return v;
		    		}
		    		alpha = Math.max(alpha, v);
				}	
			}
			
		}
		
		changedTags.clear();
		return v;
	}
	private int min(int currentDepth,int alpha,int beta) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		if(cutoffTest(currentDepth))
		{
			return evaluate();
		}
		
		currentDepth++;		
	
		int i=0,j=0;
		int v = Integer.MAX_VALUE;
		
				
		int previous;
		int oldPiece;
		int prevPiece;
		//For each piece
		for(i=0;i<64;i++)
		{
			
			if(checkPiece(i)%2==BLACK)
			{
				changedTags.clear();
	
				//For each move
				changedTags=checkMoves(i);
				//highlight();
			
				for(j=0;j<changedTags.size();j++)
				{
					previous = i;
					
					//piece before modification
					prevPiece=piece = checkPiece(previous);
					setBlank(previous);

					int k=changedTags.get(j);
					oldPiece = checkPiece(k);
	
					
		    		movePiece(k);
		    		
		    		v=Math.min(v,max(currentDepth,alpha,beta));
		    		
		    				    		
		    		if(oldPiece==BLANK)
		    		{
		    			setBlank(k);
		    		}
		    		else
		    		{
		    			piece=oldPiece;
		    			movePiece(k);
		    		}
		    	
		    		piece=prevPiece;
		    		movePiece(previous);
		    		
		    		//Apply alpha beta pruning
		    		
		    		if(v<=alpha)
		    		{
		    			changedTags.clear();
		    			return v;
		    		}
		    		
		    		beta = Math.min(beta, v);
				}
		
			}
		}
		
		
		changedTags.clear();
		return v;
		
	}
	private void setBlank(int previous) {
		// TODO Auto-generated method stub
		
		ChessBoard[previous]=' ';
		
	}
	private void bestMove() {
		// TODO Auto-generated method stub
		
		
		//get that white
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		//get moves
		changedTags.clear();
		changedTags=checkMoves(pieceToMove);
		
		if(changedTags.size()<=0)
		{
			showAlert("You Win!!", "Stalemate");
			return;
		}
		
		int previous = pieceToMove;
		
		//piece before modification
		piece = checkPiece(previous);
		setBlank(previous);
		
		movePiece(locationToMove);		
		changedTags.clear();
		numberOfMoves++;
		
	}
	private void updateUI() {
		// TODO Auto-generated method stub
		
		for(int i=0;i<64;i++)
		{
			setPiece(i);
		}
		
	}
	private void setPiece(int i) {
		// TODO Auto-generated method stub
		
		switch(checkPiece(i))
		{
		case PAWN_BLACK:
			chessBoard.get(i).setImageResource(R.drawable.pawn_black);
			break;
		case BISHOP_BLACK:
			chessBoard.get(i).setImageResource(R.drawable.bishop_black);
			break;
		case QUEEN_BLACK:
			chessBoard.get(i).setImageResource(R.drawable.queen_black);
			break;
		case KNIGHT_BLACK:
			chessBoard.get(i).setImageResource(R.drawable.knight_black);
			break;
		case KING_BLACK:
			chessBoard.get(i).setImageResource(R.drawable.king_black);
			break;
		case ROOK_BLACK:
			chessBoard.get(i).setImageResource(R.drawable.rook_black);
			break;
		case PAWN_WHITE:
			chessBoard.get(i).setImageResource(R.drawable.pawn_white);
			break;
		case BISHOP_WHITE:
			chessBoard.get(i).setImageResource(R.drawable.bishop_white);
			break;
		case QUEEN_WHITE:
			chessBoard.get(i).setImageResource(R.drawable.queen_white);
			break;
		case KNIGHT_WHITE:
			chessBoard.get(i).setImageResource(R.drawable.knight_white);
			break;
		case KING_WHITE:
			chessBoard.get(i).setImageResource(R.drawable.king_white);
			break;
		case ROOK_WHITE:
			chessBoard.get(i).setImageResource(R.drawable.rook_white);
			break;
		
		default:
			chessBoard.get(i).setImageDrawable(null);
		}
		
	}
	
	private int evaluate() {
		// TODO Auto-generated method stub
		int score=0;		
		
		score=score+scoreKingsCheck();
		score=score+scoreMaterialAdvantage();
		score=score+scoreMobility();
		score=score+scorePositionalAdvantage();
		
		return score;
	}
	
	private int scoreKingsCheck() {
		// TODO Auto-generated method stub
		if(checkIfCheck(WHITE))
		{
			return 50;
		}
		else if(checkIfCheck(BLACK))
		{
			return -50;
		}
		return 0;
		
	}
	private int scorePositionalAdvantage() {
		// TODO Auto-generated method stub
		int score=0;
		
		int i=0;
		
		for(i=0;i<64;i++)
		{	
			score=score+getPositionalScore(i);
			
		}

		return score;
	}
	private int getPositionalScore(int location) {
		// TODO Auto-generated method stub
		
		int piece=checkPiece(location);
		if(piece%2==BLACK)
		{
			return 0;
		}
		else
		{
			switch(piece)
			{	
			case PAWN_WHITE:
				return scorePawnPositions[location];
			
			case BISHOP_WHITE:
				return scoreBishopPositions[location];
				
			case QUEEN_WHITE:
				return scoreQueenPositions[location];
				
			case KNIGHT_WHITE:
				return scoreKnightPositions[location];
				
			case KING_WHITE:
				if(phaseOfGame!=3)
				return scoreKingPositions[location];
				else
				return scoreKingPositionsEnd[location];	
			case ROOK_WHITE:
				return scoreRookPositions[location];
			
			default:
				return 0;

			}
		}
	}
	private int scoreMobility() {
		// TODO Auto-generated method stub
		int score=0;
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		int i=0;
		
		for(i=0;i<64;i++)
		{
			if(checkPiece(i)!=BLANK)
			{		
					changedTags.clear();
					checkMoves(i);
					score = score + changedTags.size(); 	
			}
			
		}
		changedTags.clear();
		return score*10;

	}
	private int scoreMaterialAdvantage() {
		// TODO Auto-generated method stub
		int score=0;
		
		int i=0;
		
		for(i=0;i<64;i++)
		{
			if(checkPiece(i)!=BLANK)
			{
					score = score + checkScore(i);	
			}
			
		}
		
		return score;
	}
	
	
	private boolean cutoffTest(int depth) {
		// TODO Auto-generated method stub
		if(depth >= maxDepth)
		{
			return true;
		}
		return false;
	}

	private ArrayList<Integer> checkMovesKnight(int location) {
		// TODO Auto-generated method stub
		int tag;
		int i;
		int currentRow,currentCol;
		int newRow,newCol;
		
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		int type=getType(location);
	
		
		changedTags.clear();
		
		tag=location;

		i=tag;
		currentRow = (int) Math.ceil(((tag)/8 + 0.5));
		currentCol = tag%8;
		
		int[] moveKnight = {17,-17,10,-10,6,-6,15,-15}; 
		int j=0;
		
		while(j<8)
		{
			i=tag+moveKnight[j];
			newRow=(int) Math.ceil((i/8 + 0.5));
			newCol = i%8;
		
			if(Math.abs(newRow-currentRow)<3 && Math.abs(newCol-currentCol)<3 && i>-1 && i<64)
			{
				verifyAndAdd(i,type,changedTags);
			}
			j++;
		}
		return changedTags;
			
	}
	
	private ArrayList<Integer> checkMovesKing(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(getType(location)==WHITE)
		{
			changedTags.addAll(checkMovesKingWhite(location));
			changedTags.removeAll(safeKingMoves(WHITE));
		}
		else
		{
			changedTags.addAll(checkMovesKingBlack(location));
			changedTags.removeAll(safeKingMoves(BLACK));
		}
		return changedTags;
		
	}

	private ArrayList<Integer> checkMovesKingBlack(int location) {
		// TODO Auto-generated method stub
		

		int tag;
		int i;
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		changedTags.clear();
		tag=location;
		int type=getType(location);
		
		i=tag;
		
		if((i+1)%8!=0)
		{
			verifyAndAdd(i+1,type,changedTags);
		}
		
		if(i%8!=0)
		{
			verifyAndAdd(i-1,type,changedTags);
		}
		if(i<56)
		{
			verifyAndAdd(i+8,type,changedTags);
		}
		if(i>7)
		{
			verifyAndAdd(i-8,type,changedTags);
		}
		
		if(i<55 && (i+1)%8!=0)
		{
			verifyAndAdd(i+9,type,changedTags);
		}
		if(i>8 && i%8!=0)
		{
			verifyAndAdd(i-9,type,changedTags);
		}
		if(i<56 && i%8!=0)
		{
			verifyAndAdd(i+7,type,changedTags);
		}
		
		if(i>7 && (i+1)%8!=0)
		{
			verifyAndAdd(i-7,type,changedTags);
		}
		return changedTags;
	
		
	}
	private ArrayList<Integer> checkMovesKingWhite(int location) {
		// TODO Auto-generated method stub

		int tag;
		int i;
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		changedTags.clear();
		tag=location;
		int type=getType(location);
		
		i=tag;
		
		if((i+1)%8!=0)
		{
			verifyAndAdd(i+1,type,changedTags);
		}
		
		if(i%8!=0)
		{
			verifyAndAdd(i-1,type,changedTags);
		}
		if(i<56)
		{
			verifyAndAdd(i+8,type,changedTags);
		}
		if(i>7)
		{
			verifyAndAdd(i-8,type,changedTags);
		}
		
		if(i<55 && (i+1)%8!=0)
		{
			verifyAndAdd(i+9,type,changedTags);
		}
		if(i>8 && i%8!=0)
		{
			verifyAndAdd(i-9,type,changedTags);
		}
		if(i<56 && i%8!=0)
		{
			verifyAndAdd(i+7,type,changedTags);
		}
		
		if(i>7 && (i+1)%8!=0)
		{
			verifyAndAdd(i-7,type,changedTags);
		}
		return changedTags;
	
		
	}
	
	private ArrayList<Integer> checkMovesRook(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		changedTags.clear();
		changedTags=checkInLine(location);
		return changedTags;

	}

	private  ArrayList<Integer>  checkInLine(int location) {
		// TODO Auto-generated method stub
		int tag;
		int i;
		tag=location;
		int type=getType(location);
		i=tag;
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		
		while((i+1)%8!=0)
		{
			i++;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}

		}
		
		i=tag;
		while(i%8!=0)
		{
			i--;
		
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}

		}
		
		i=tag;
		
		while(i<56)
		{
			i=i+8;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}

		}
					
		i=tag;
		
		while(i>7)
		{
			i=i-8;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}

		}
		return changedTags;
		
	}


	private ArrayList<Integer> checkMovesQueen(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		//go upward
		//add 9
		changedTags.clear();
		changedTags.addAll(checkDiagonal(location));
		changedTags.addAll(checkInLine(location));
		
		return changedTags;
		
	}

	private  ArrayList<Integer> checkDiagonal(int location) {
		// TODO Auto-generated method stub
		int tag;
		int i;
		int type=getType(location);
		tag=location;
	
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		i=tag;
		
		while(i<55 && (i+1)%8!=0)
		{	
			i=i+9;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}
			
		}
		i=tag;
		
		while(i<56 && i%8!=0)
		{	
			i=i+7;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}
			
		 }
		
		i=tag;
		
		while(i>8 && i%8!=0)
		{	
			i=i-9;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}
			
		 }
		
		i=tag;
		
		while(i>7 && (i+1)%8!=0)
		{	
			i=i-7;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				break;
			}
			
		 }
		return changedTags;
		
	}
	
	private ArrayList<Integer> checkMovesPawn(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(Character.isUpperCase(ChessBoard[location]))
		{
			changedTags=checkMovesPawnWhite(location);
		}
		else
		{
			changedTags=checkMovesPawnBlack(location);
		}
		return changedTags;
		
		
	}

	private ArrayList<Integer> checkMovesPawnWhite(int location) {
		// TODO Auto-generated method stub
		
		int availablePositions;
		int tag=location;
		int row = (int) Math.ceil((tag/8) + 0.5);
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		changedTags.clear();
		
		if(tag>47 && tag<56){
			
			availablePositions = tag-8;
			if(isValid(availablePositions))
			{	
				if(checkPiece(availablePositions)==BLANK)
				{
					changedTags.add(availablePositions);	
					
					availablePositions = availablePositions-8;
					
					if(isValid(availablePositions))
					{
						if(checkPiece(availablePositions)==BLANK)
						{
							changedTags.add(availablePositions);	
							
						}
					}
					
					availablePositions = availablePositions + 8;
				}
			}
			
		}

		else
		{
			availablePositions = tag-8;
			
			if(isValid(availablePositions))
			{

				if(checkPiece(availablePositions)==BLANK)
				{
					changedTags.add(availablePositions);	
				}
				
			}
			
		}
		
		availablePositions = availablePositions -1;
		if(isValid(availablePositions) && availablePositions >= (row-2)*8)
		{
			if(Character.isLowerCase(ChessBoard[availablePositions]))
			{
				changedTags.add(availablePositions);
									
			}
		}
		availablePositions = availablePositions +2;
		if(isValid(availablePositions) && availablePositions < (row-1)*8)
		{
			if(Character.isLowerCase(ChessBoard[availablePositions]))
			{
				changedTags.add(availablePositions);
			}
		}
		return changedTags;
		
	}

	private ArrayList<Integer> checkMovesPawnBlack(int location) {
		// TODO Auto-generated method stub
		int availablePositions;
		int tag=location;
		
		int row = (int) Math.ceil((tag/8) + 0.5);

		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(tag>7 && tag<16){
			
			availablePositions = tag+8;
			if(isValid(availablePositions))
			{	
				if(checkPiece(availablePositions)==BLANK)
				{
					changedTags.add(availablePositions);	
					
					availablePositions = availablePositions+8;
					
					if(isValid(availablePositions))
					{
						if(checkPiece(availablePositions)==BLANK)
						{
							changedTags.add(availablePositions);	
							
						}
					}
					
					availablePositions = availablePositions - 8;
				}
			}
			
		}
		else
		{
			availablePositions = tag+8;
			
			if(isValid(availablePositions))
			{
				if(checkPiece(availablePositions)==BLANK)
				{
					changedTags.add(availablePositions);	
				}
				
			}
			
		}
		
		
		availablePositions = availablePositions -1;
		if(isValid(availablePositions) && availablePositions >= row*8)
		{
			if(Character.isUpperCase(ChessBoard[availablePositions]))
			{
				changedTags.add(availablePositions);						
			}
		}
		availablePositions = availablePositions +2;
		if(isValid(availablePositions) && availablePositions < (row+1)*8)
		{
			if(Character.isUpperCase(ChessBoard[availablePositions]))
			{
				changedTags.add(availablePositions);
			}
		}
		return changedTags;
		
	}

	private ArrayList<Integer> checkMovesBishop(int location) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		changedTags.clear();	
		changedTags=checkDiagonal(location);
		return changedTags;			
		
	}
	
	private ArrayList<Integer> getAttackedLocationsKnight(int location) {
		// TODO Auto-generated method stub
		int tag;
		int i;
		int currentRow,currentCol;
		int newRow,newCol;
		
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
			
		changedTags.clear();
		
		tag=location;

		i=tag;
		currentRow = (int) Math.ceil(((tag)/8 + 0.5));
		currentCol = tag%8;
		
		int[] moveKnight = {17,-17,10,-10,6,-6,15,-15}; 
		int j=0;
		
		while(j<8)
		{
			i=tag+moveKnight[j];
			newRow=(int) Math.ceil((i/8 + 0.5));
			newCol = i%8;
		
			if(Math.abs(newRow-currentRow)<3 && Math.abs(newCol-currentCol)<3 && i>-1 && i<64)
			{
				changedTags.add(i);
			}
			j++;
		}
		return changedTags;
			
	}
	
	/*private ArrayList<Integer> getAttackedLocationsKing(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(getType(location)==WHITE)
		{
			changedTags.addAll(checkMovesKingWhite(location));
			changedTags.removeAll(safeKingMoves(WHITE));
		}
		else
		{
			changedTags.addAll(checkMovesKingBlack(location));
			changedTags.removeAll(safeKingMoves(BLACK));
		}
		return changedTags;
		
	}*/

	private ArrayList<Integer> getAttackedLocationsKing(int location) {
		// TODO Auto-generated method stub
		
		int tag;
		int i;
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		changedTags.clear();
		tag=location;
		
		
		i=tag;
		
		if((i+1)%8!=0)
		{
			changedTags.add(i+1);
		}
		
		if(i%8!=0)
		{
			changedTags.add(i-1);
		}
		if(i<56)
		{
			changedTags.add(i+8);
		}
		if(i>7)
		{
			changedTags.add(i-8);
		}
		
		if(i<55 && (i+1)%8!=0)
		{
			changedTags.add(i+9);
		}
		if(i>8 && i%8!=0)
		{
			changedTags.add(i-9);
		}
		if(i<56 && i%8!=0)
		{
			changedTags.add(i+7);
		}
		
		if(i>7 && (i+1)%8!=0)
		{
			changedTags.add(i-7);
		}
		return changedTags;
	
		
	}
		
	private ArrayList<Integer> getAttackedLocationsRook(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		changedTags.clear();
		changedTags=getAttackedInLine(location);
		return changedTags;

	}

	private  ArrayList<Integer> getAttackedInLine(int location) {
		// TODO Auto-generated method stub
		int tag;
		int i;
		tag=location;
		int type=getType(location);
		i=tag;
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		while((i+1)%8!=0)
		{
			i++;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
			
		}
		
		i=tag;
		
		while(i%8!=0)
		{
			i--;
		
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}

		}
		
		i=tag;
		
		while(i<56)
		{
			i=i+8;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
		}
					
		i=tag;
		
		
		while(i>7)
		{
			i=i-8;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
		}
		return changedTags;
		
	}


	private ArrayList<Integer> getAttackedLocationsQueen(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		//go upward
		//add 9
		changedTags.clear();
		changedTags.addAll(getAttackedDiagonal(location));
		changedTags.addAll(getAttackedInLine(location));
		
		return changedTags;
		
	}

	private  ArrayList<Integer> getAttackedDiagonal(int location) {
		// TODO Auto-generated method stub
		int tag;
		int i;
		int type=getType(location);
		tag=location;
		
		
		ArrayList<Integer> changedTags=new ArrayList<Integer>();

		i=tag;
		
		
		while(i<55 && (i+1)%8!=0)
		{	
			i=i+9;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
			
		}
		i=tag;
		
		
		while(i<56 && i%8!=0)
		{	
			i=i+7;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
		 }
		
		i=tag;
		
		while(i>8 && i%8!=0)
		{	
			i=i-9;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
			
		 }
		
		i=tag;
		
		
		while(i>7 && (i+1)%8!=0)
		{	
			i=i-7;
			
			if(verifyAndAdd(i,type,changedTags)==false)
			{
				changedTags.add(i);
				if(type==getType(i))
				break;
			}
			
		 }
		return changedTags;
		
	}
	
	private ArrayList<Integer> getAttackedLocationsPawn(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(Character.isUpperCase(ChessBoard[location]))
		{
			changedTags=getAttackedLocationsPawnWhite(location);
		}
		else
		{
			changedTags=getAttackedLocationsPawnBlack(location);
		}
		return changedTags;
		
		
	}

	private ArrayList<Integer> getAttackedLocationsPawnWhite(int location) {
		// TODO Auto-generated method stub
		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(isValid(location-7))
		{
				changedTags.add(location-7);
		}
		if(isValid(location-9))
		{
				changedTags.add(location-9);
		}
		
		
		return changedTags;

		
	}

	private ArrayList<Integer> getAttackedLocationsPawnBlack(int location) {
		// TODO Auto-generated method stub

		ArrayList<Integer> changedTags = new ArrayList<Integer>();

		if(isValid(location+7))
		{
				changedTags.add(location+7);
		}
		if(isValid(location+9))
		{
				changedTags.add(location+9);
		}
		
		
		return changedTags;
		
	}

	private ArrayList<Integer> getAttackedLocationsBishop(int location) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> changedTags = new ArrayList<Integer>();
		changedTags.clear();	
		changedTags=getAttackedDiagonal(location);
		return changedTags;			
		
	}

	
	private boolean verifyAndAdd(int location,int type,ArrayList<Integer> changedTags) {
		// TODO Auto-generated method stub
		
		if(checkPiece(location)==BLANK)
		{
			changedTags.add(location);
			return true;
		}
		else if(checkPiece(location)%2!=type)
		{
			changedTags.add(location);
		}
		return false;
		
	}
	private int getType(int location) {
		// TODO Auto-generated method stub
		

		if(Character.isLowerCase(ChessBoard[location]))
		{
			return BLACK;
		}
		else if(Character.isUpperCase(ChessBoard[location]))
		{
			return WHITE;
		}
		
		return BLANK;
	}

	private void movePiece(int location) {
		// TODO Auto-generated method stub
		
		switch(piece)
		{
			case PAWN_BLACK:
				//v.setImageResource(R.drawable.pawn_black);
				ChessBoard[location] = 'p';
				break;
			case BISHOP_BLACK:
				ChessBoard[location] = 'b';
				break;
			case QUEEN_BLACK:
				ChessBoard[location] = 'q';
				break;
			case KNIGHT_BLACK:
				ChessBoard[location] = 'n';
				break;
			case KING_BLACK:
				ChessBoard[location] = 'k';
				break;
			case ROOK_BLACK:
				ChessBoard[location] = 'r';
				break;
			case PAWN_WHITE:
				ChessBoard[location] = 'P';
				break;
			case BISHOP_WHITE:
				ChessBoard[location] = 'B';
				break;
			case QUEEN_WHITE:
				ChessBoard[location] = 'Q';
				break;
			case KNIGHT_WHITE:
				ChessBoard[location] = 'N';
				break;
			case KING_WHITE:
				ChessBoard[location] = 'K';
				break;
			case ROOK_WHITE:
				ChessBoard[location] = 'R';
				break;
			
			default:

		}
	}

	private boolean isValid(int availablePositions) {
		// TODO Auto-generated method stub
		if(availablePositions>=0 && availablePositions<64)
		{
			return true;
		}
		return false;
	}

	private int checkPiece(int location) {
		// TODO Auto-generated method stub
		//ImageView imageView = (ImageView)v;
		try
		{
		if(ChessBoard[location]=='p')
		{
			return PAWN_BLACK;
		}
		if(ChessBoard[location]=='P')
		{
			return PAWN_WHITE;
		}
		if(ChessBoard[location]=='n')
		{
			return KNIGHT_BLACK;
		}
		if(ChessBoard[location]=='N')
		{
			return KNIGHT_WHITE;
		}
		if(ChessBoard[location]=='b')
		{
			return BISHOP_BLACK;
		}
		if(ChessBoard[location]=='B')
		{
			return BISHOP_WHITE;
		}
		if(ChessBoard[location]=='r')
		{
			return ROOK_BLACK;
		}
		if(ChessBoard[location]=='R')
		{
			return ROOK_WHITE;
		}
		if(ChessBoard[location]=='k')
		{
			return KING_BLACK;
		}
		if(ChessBoard[location]=='K')
		{
			return KING_WHITE;
		}
		if(ChessBoard[location]=='q')
		{
			return QUEEN_BLACK;
		}
		if(ChessBoard[location]=='Q')
		{
			return QUEEN_WHITE;
		}
		}
		catch(Exception e)
		{
			
		}
		return BLANK;
	}
	
	protected void highlight(ArrayList<Integer> changedTags) {
		// TODO Auto-generated method stub
		ImageView temp;
		toRestore.addAll(changedTags);
		for(int i=0;i<changedTags.size();i++)
		{
			temp = chessBoard.get(changedTags.get(i));
		
			if(temp.getBackground().getConstantState() == getResources().getDrawable(R.drawable.white_box).getConstantState())
				temp.setBackgroundResource(R.drawable.movable_white);
			else
				temp.setBackgroundResource(R.drawable.movable_red);
		}
	}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.display_chess_board, menu);
			return true;
		}
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	
			
	}
