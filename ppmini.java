import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;
class ppmini{
  public static void main (String[] args) throws IOException{
    final int noOfQuestions = 5;
    int playerCounter=1;
    int[] scores;
    int noOfPlayers = 0;
    int questionCounter = 0;

    question[] questions = new question[noOfQuestions];
    questions = questionArray(noOfQuestions);

    welcomeMessage();

    load(questionCounter, playerCounter, questions, noOfPlayers);
    noOfPlayers = askNumberOfPlayers();
    scores = new int[noOfPlayers];
    for(int i = 0; i < noOfPlayers; i++){
      scores[i] = 0;
    }
    musicQuiz(questionCounter, playerCounter, questions, noOfPlayers, scores);
  }

  public static question[] questionArray(int n){
    question[] q = new question[n];

    question q1 = new question();
    q1 = setQuestion(q1, "Who is the King of Pop?");
    q1 = setAnswer(q1, "Michael Jackson");
    q[0]= q1;

    question q2 = new question();
    q2 = setQuestion(q2, "Name the person who left the band 'One Direction'?");
    q2 = setAnswer(q2, "Zayn Malik");
    q[1] = q2;

    question q3 = new question();
    q3 = setQuestion(q3, "Which artist sung the song 'Rolling in the Deep'?");
    q3 = setAnswer(q3, "Adele");
    q[2] = q3;

    question q4 = new question();
    q4 = setQuestion(q4, "Which band is the singer Chris Martin in?");
    q4 = setAnswer(q4, "Coldplay");
    q[3] = q4;

    question q5 = new question();
    q5 = setQuestion(q5, "Which US singer is known by the nickname J Lo?");
    q5 = setAnswer(q5, "Jennifer Lopez");
    q[4] = q5;

    return q;
  }

  public static void welcomeMessage(){
    JOptionPane.showMessageDialog(null, "Welcome to my music quiz!");
  }

  public static void load(int questionCounter, int playerCounter, question[] questions, int noOfPlayers) throws IOException{
    String input = JOptionPane.showInputDialog("Would you like to load your previous quiz? Y/N");
    try{
      if(input.equalsIgnoreCase("y")||input.equalsIgnoreCase("yes")) {
        BufferedReader inputStream = new BufferedReader(new FileReader("previousGame.txt"));
        questionCounter = Integer.parseInt(inputStream.readLine());
        playerCounter = Integer.parseInt(inputStream.readLine());
        noOfPlayers = Integer.parseInt(inputStream.readLine());
        int[] scores = new int[noOfPlayers];
        for(int i = 0; i < noOfPlayers; i++){
          scores[i] = Integer.parseInt(inputStream.readLine());
        }
        for(int i = 0; i < questions.length; i++){
          setCorrectSoFar(questions[i], Integer.parseInt(inputStream.readLine()));
        }
        inputStream.close();
        musicQuiz(questionCounter, playerCounter, questions, noOfPlayers, scores);
        System.exit(0);
      }else if(input.equalsIgnoreCase("n")||input.equalsIgnoreCase("no")) {
        return;
      }else{
        JOptionPane.showMessageDialog(null, "Please input 'y' for yes or 'n' for no");
        load(questionCounter, playerCounter, questions, noOfPlayers);
      }
    }catch(Exception e){
      JOptionPane.showMessageDialog(null, "No previous game found\nStarting new game");
    }
  }

  public static int askNumberOfPlayers(){
    int num;
    try{
      String number = JOptionPane.showInputDialog("How many players are there in your team?");
      num = Integer.parseInt(number);
      if(num < 1) {
          JOptionPane.showMessageDialog(null,"Please input a number greater than 0");
          num = askNumberOfPlayers();
      }
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,"Please input a number greater than 0");
      num = askNumberOfPlayers();
    }

    return num;
  }

  public static void musicQuiz(int questionCounter, int playerCounter, question[] qs, int num, int[] scores) throws IOException{
    JOptionPane.showMessageDialog(null, "Message:\nTo save the game enter 'save' to the input");

    for(; questionCounter < qs.length; questionCounter++){
      askquestions(questionCounter, playerCounter,qs[questionCounter],num,scores, qs);
      playerCounter =1;
    }
    //for(int i = 0; i < num; i++){
    //  System.out.println("----------------------------------------" + scores[i]);
    //}

    printScore(scores);

    hardestQuestions(qs);
  }

  public static void askquestions(int questionCounter, int playerCounter, question q, int num, int[] scores, question[] qs)throws IOException{
    for(; playerCounter<=num; playerCounter++){
      String answer = JOptionPane.showInputDialog("Player " + playerCounter + "\n"+getQuestion(q));
      if(answer.equalsIgnoreCase("save")){
        save(questionCounter, playerCounter, num, scores, qs);
        askquestions(questionCounter, playerCounter, q, num, scores, qs);
      }
      if(answer.equalsIgnoreCase(getAnswer(q))){
        JOptionPane.showMessageDialog(null, "That is correct, click OK to row a dice");
        int point = dicethrow();
        scores[playerCounter-1] = scores[playerCounter-1] + point;
        setCorrectSoFar(q, getCorrectSoFar(q) +1);
      } else {
        JOptionPane.showMessageDialog(null, "Sorry, that is incorrect, you scored 0 points");
      }
    }
  }
  public static void save(int questionCounter, int playerCounter, int num, int[] scores, question[] qs) throws IOException{
    PrintWriter output = new PrintWriter(new FileWriter("previousGame.txt"));
    output.println(questionCounter);
    output.println(playerCounter);
    output.println(num);
    for(int i = 0; i < scores.length; i++){
      output.println(scores[i]);
    }
    for(int i = 0; i < qs.length; i++){
      output.println(getCorrectSoFar(qs[i]));
    }
    output.close();
    JOptionPane.showMessageDialog(null, "Game saved");
  }

  public static int dicethrow(){
    Random dice = new Random();
    int thrown = dice.nextInt(6) + 1;
    int score;
    if(thrown<=5 && thrown>=1)
		{
			score = 3;
		}
		else
		{
			score = 6;
		}
    JOptionPane.showMessageDialog(null, "You rolled a " + thrown + "\nYou scored " + score + " points");
		return score;

  }

  public static void printScore(int[] scores) {
    JOptionPane.showMessageDialog(null, "To view Score look at the terminal");
    System.out.format("%20s|", "Player");
    System.out.print("Score\n");
    int num = 0;
    for (int i = 0; i< scores.length; i++) {
      System.out.format("%20s|", i+1);
      System.out.print(scores[i]+"\n");
      num = num + scores[i];
    }
    System.out.println("The total team score is: " + num);

  }

  public static void hardestQuestions(question[] q){
    //question[] hardestList = new question[q.length];
    for(int i = 0; i < q.length-1; i++){
      for(int j = 0; j <q.length-1-i; j++){
        if(getCorrectSoFar(q[j])>getCorrectSoFar(q[j+1])){
          question temp = q[j];
          q[j] = q[j+1];
          q[j+1] = temp;
        }
      }
    }
    System.out.println("List of questions from easiest to hardest:");
    int count =1;
    for(int i = q.length-1; i >= 0; i--){
      System.out.println(count + getQuestion(q[i]));
      count++;
    }

  }

  public static question setQuestion(question q, String question){
    q.question = question;
    return q;
  }

  public static String getQuestion(question q){
    return q.question;
  }

  public static question setAnswer(question q, String ans){
    q.answer = ans;
    return q;
  }

  public static String getAnswer(question q){
    return q.answer;
  }

  public static question setCorrectSoFar(question q, int correctsofar)
	{
		q.correctSoFar = correctsofar;
		return q;
	}

	public static int getCorrectSoFar(question q)
	{
		return q.correctSoFar;
	}
}

class question{
  String question;
  String answer;
  int correctSoFar = 0;
}
