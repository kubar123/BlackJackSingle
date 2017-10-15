/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakub Rybicki
 */
public class BlackJack {
    //debugging on/off
    public static final boolean DEBUG=true;
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    
    public static int playerHandValue=0;
    public static int dealerHandValue=0;
    
    //The list of playing cards
    public static String mDeck[]={
        "♠ 2", "♠ 3", "♠ 4", "♠ 5", "♠ 6", "♠ 7", "♠ 8", "♠ 9", "♠ 10",
        "♠ K", "♠ Q", "♠ J", "♠ ACE", "♥ 2", "♥ 3", "♥ 4", "♥ 5", "♥ 6",
        "♥ 7", "♥ 8", "♥ 9", "♥ 10", "♥ K", "♥ Q", "♥ J", "♥ ACE", "♦ 2",
        "♦ 3", "♦ 4", "♦ 5", "♦ 6", "♦ 7", "♦ 8", "♦ 9", "♦ 10", "♦ K", 
        "♦ Q", "♦ J", "♦ ACE","♣ 2", "♣ 3", "♣ 4", "♣ 5", "♣ 6", "♣ 7", 
        "♣ 8", "♣ 9", "♣ 10", "♣ K", "♣ Q", "♣ J", "♣ ACE"
    };
    
    //shuffled deck of cards
    //public static Set<String> mGameDeck=new HashSet<>(Arrays.asList(mDeck));
    public static ArrayList<String> mGameDeck=new ArrayList<String>(Arrays.asList(mDeck));
    
    public static void main(String[] args) {
        //start the new game
        newGame(true);
    }
    

    private static void newGame(boolean isSinglePlayer) {
        //redirect player to multiplayer game
        if(!isSinglePlayer) throw new UnsupportedOperationException("Sigle player only");
        
        //repopulate the deck
       // mGameDeck=new HashSet<>(Arrays.asList(mDeck));
        mGameDeck=new ArrayList<String>(Arrays.asList(mDeck));
        startSinglePlayer();
    }

    private static void startSinglePlayer() {
        Set<String> playerHand=new HashSet<>();
        Set<String> dealerHand=new HashSet<>();
        
        //player takes 2 starting cards from the deck
        playerHand.add(TakeCardFromDeck());
        playerHand.add(TakeCardFromDeck());
        
        System.out.println("\n\nYour cards: "+playerHand);
        playerHandValue = determineHandValue(playerHand);
        System.out.println(playerHandValue);
        
        //Ask user to keep playing
        boolean keepLooping=false;
        do{
            keepLooping=false;
            
            System.out.println("(H)it or (S)tand >>");
            String input="";
            try {
                input = br.readLine();
            } catch (IOException ex) {
                keepLooping=true;
                continue;
            }
            
             if(input.equalsIgnoreCase("h")){
                 keepLooping=true;
                 playerHand.add(TakeCardFromDeck());
                 playerHandValue = determineHandValue(playerHand);
                 System.out.println("Your cards: "+playerHand);
                    System.out.println("Your points: "+playerHandValue);
                 
                 checkIfBusted(playerHandValue);
            }else if(input.equalsIgnoreCase("s")){
                keepLooping=false;
                 dealerMakeMove(dealerHand, dealerHandValue);
                 System.out.println("___"+dealerHandValue);
            }
        
        }while(keepLooping);
    }

    private static int determineHandValue(Set<String> playerHand) {
        int cardValue=0;
        for(String card:playerHand){
            String selected=card.substring(2);
            
            //try converting the card into a number - if cannot it is a KQJ/ACE
            try{
                cardValue+=Integer.parseInt(selected);
            }catch(NumberFormatException e){
                if(selected.equalsIgnoreCase("ACE")){
                    cardValue +=11;
                }else cardValue+= 10;
            }
            
        }
        return cardValue;
    }
    
    
    
public static void dealerMakeMove(Set dealerHand, int dealerPoints){
    do{
        dealerHand.add(TakeCardFromDeck());
        System.out.println(dealerHand);
        dealerHandValue=determineHandValue(dealerHand);
    }while(dealerHandValue<=17);
    if(dealerHandValue>21){
        System.out.println("Dealer busts!");
    }else if (dealerHandValue>playerHandValue){
        System.out.println("Dealer Wins");
    }else if(dealerHandValue<playerHandValue){
        System.out.println("Player wins!");
    }
    System.out.println("Dealer hand: "+dealerHand+"\n Dealer points: "+dealerHandValue);

}
    private static String TakeCardFromDeck(){
        int num = (int) (Math.random() * mGameDeck.size());
        //picks a random card from the deck
//        for(String t: mGameDeck) if (--num < 0){
//            mGameDeck.remove(t);
//            return t;
//        }
        
        int chosenCard = (int) (Math.random() * mGameDeck.size());
        //picks a random card from the deck
       // for(String card: mGameDeck) if (--chosenCard < 0){
            String card=mGameDeck.get(chosenCard);
            mGameDeck.remove(card);//remove the card from the deck
        
        return card;
    }

    private static void checkIfBusted(int playerHandValue) {
        while(playerHandValue>21){
            System.out.println("\nBUSTED! You lose");
            System.out.println("Play again? (Y)es | (N)o");
            try {
                String input = br.readLine();
                if(input.equalsIgnoreCase("y")){
                    newGame(true);
                }else if(input.equalsIgnoreCase("n")){
                    System.exit(0);
                }
            } catch (IOException ex) {

            }
        }
    }
}
