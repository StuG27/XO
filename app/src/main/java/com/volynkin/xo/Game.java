package com.volynkin.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game extends AppCompatActivity{

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButtonX;
    private Button mButton0;
    private Button mButtonB;
    private TextView mText;

    private boolean[] X = new boolean[9];
    private boolean[] O = new boolean[9];
    private int[] turn = {1,0};
    private int[] aiTurn = {0,0,0,0,0,0,0};//0-порядок хода, 1-ход ИИ, 2-ход игрока, 3-признак 1го хода,
    //4-счётчик ничьей, 5-флаг победы, 6-версия поворота
    private int aiSymbol = 0;
    private int mMode;
    private static final String EXTRA_INFO =
            "com.volynkin.test_intent.information";
    private static final String EXTRA_ANSWER =
            "com.volynkin.quiz.answer_shown";

    public static Intent newIntent(Context packageContext, int mode) {
        Intent intent = new Intent(packageContext, Game.class);
        intent.putExtra(EXTRA_INFO, mode);
        return intent;
    }

    public static char Answer(Intent result) {
        return result.getCharExtra(EXTRA_ANSWER, 'X');
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xo_main_activity);
        mMode = getIntent().getIntExtra(EXTRA_INFO, 0);

        mText = (TextView) findViewById(R.id.edit);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton5 = (Button) findViewById(R.id.button5);
        mButton6 = (Button) findViewById(R.id.button6);
        mButton7 = (Button) findViewById(R.id.button7);
        mButton8 = (Button) findViewById(R.id.button8);
        mButton9 = (Button) findViewById(R.id.button9);
        mButtonX = (Button) findViewById(R.id.buttonX);
        mButton0 = (Button) findViewById(R.id.button0);
        mButtonB = (Button) findViewById(R.id.buttonB);

        if(mMode==1) {
            offButtons(1);
            mText.setText("Крестики или нолики?");
            mButtonX.setVisibility(View.VISIBLE);
            mButton0.setVisibility(View.VISIBLE);
        }

        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mButtonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiSymbol=0;
                mButtonX.setVisibility(View.INVISIBLE);
                mButton0.setVisibility(View.INVISIBLE);
                onButtons(1);
            }
        });
        mButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiSymbol=1;
                mButtonX.setVisibility(View.INVISIBLE);
                mButton0.setVisibility(View.INVISIBLE);
                onButtons(1);
                mText.setText("Ваш ход");
                LogicX(aiTurn);
                //aiTurn=turnAi(aiTurn);
            }
        });

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = 0;
                switch (view.getId()) {
                    case R.id.button1: num=1;break;
                    case R.id.button2: num=2;break;
                    case R.id.button3: num=3;break;
                    case R.id.button4: num=4;break;
                    case R.id.button5: num=5;break;
                    case R.id.button6: num=6;break;
                    case R.id.button7: num=7;break;
                    case R.id.button8: num=8;break;
                    case R.id.button9: num=9;break;
                }

                if(mMode==0) {
                    turn(num, X, O, turn);
                }

                if((mMode==1)&&(aiSymbol==1)) {
                    turnHuman(aiTurn, num);
                    LogicX(aiTurn);
                   // aiTurn=turnAi(aiTurn);
                   // aiTurn=check(aiTurn);
                }
            }
        };
        mButton1.setOnClickListener(click);
        mButton2.setOnClickListener(click);
        mButton3.setOnClickListener(click);
        mButton4.setOnClickListener(click);
        mButton5.setOnClickListener(click);
        mButton6.setOnClickListener(click);
        mButton7.setOnClickListener(click);
        mButton8.setOnClickListener(click);
        mButton9.setOnClickListener(click);
    }
//__________________________________________________________________________________________________
    private void setAnswer(boolean Answer) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER, Answer);
        setResult(RESULT_OK, data);
    }
//__________________________________________________________________________________________________
    private void turnHuman(int aiTurn[], int num) {
        pushButton(num, 0);
        if (aiTurn[3]==0){
            aiTurn[6]=rotateNumber(aiTurn[2]);
            aiTurn[3]=1;
        }
        aiTurn[2]=rotate(aiTurn[2], aiTurn[6]);
        addTurn(O,X,aiTurn[2],0);
    }
//__________________________________________________________________________________________________
    private void pushButton(int num, int symbol) {
        Button button=chooseButton(num);
        button.setEnabled(false);
        if (symbol==0) {
            button.setText("0");
        }
        else {
            button.setText("X");
        }
    }
//__________________________________________________________________________________________________
    private void turnAi(boolean[] X,boolean[] O, int aiTurn[]) {
        //0-порядок хода, 1-ход ИИ, 2-ход игрока, 3-признак 1го хода,
        //4-счётчик ничьей, 5-флаг победы, 6-версия поворота
        if(aiTurn[1]==0){
            aiTurn[1]=сheckTwoInRow(X,O);//Можно ли победить?
        }
        if(aiTurn[1]==0) {
            aiTurn[1]=сheckTwoInRow(O,X);//Нужно не проиграть
        }
        if(aiTurn[1]==0) {
            aiTurn[1]=сheckFreeSpace(X,O);//Осталось 2 клетки
        }
        aiTurn[1]=rotateAway(aiTurn[1], aiTurn[6]);
        pushButton(aiTurn[1], 1);
        addTurn(O,X,aiTurn[1],1);
    }
//__________________________________________________________________________________________________
    private void turn(int num, boolean[] X,boolean[] O, int turn[]) {
        String symbolStr;
        if(turn[0]==0){
            symbolStr="0";
            mText.setText("Ход крестиков");
        }
        else{
            symbolStr="X";
            mText.setText("Ход ноликов");
        }
        int win;
        Button button=chooseButton(num);
        button.setEnabled(false);
        button.setText(symbolStr);
        addTurn(O,X,num,turn[0]);
        win=сheckWin(O);
        if(win==1) {
            offButtons(0);
            mText.setText("Нолики победили");
            mButtonB.setVisibility(View.VISIBLE);
            }
        if(win==0){
            win=сheckWin(X);
            if(win==1) {
                offButtons(0);
                mText.setText("Крестики победили");
                mButtonB.setVisibility(View.VISIBLE);
            }
        }
        turn[1]++;
        if ((turn[1]==9)&&(win==0)) {
            offButtons(0);
            mText.setText("Ничья");
            mButtonB.setVisibility(View.VISIBLE);
        }
        if(turn[0]==0){
            turn[0]=1;
        }
        else{
            turn[0]=0;
        }
    }
//__________________________________________________________________________________________________
    private void addTurn(boolean[] O, boolean X[], int turn, int symbol) {
        if (symbol==0) {
            O[turn-1]=true;
        }
        else{
            X[turn-1]=true;
        }
    }
//__________________________________________________________________________________________________
    private int сheckWin(boolean[] array) {
        int i=0;
        if((array[0]&&array[1]&&array[2])||(array[3]&&array[4]&&array[5])||
                (array[6]&&array[7]&&array[8])||(array[0]&&array[4]&&array[8])||
                (array[0]&&array[3]&&array[6])||(array[1]&&array[4]&&array[7])||
                (array[2]&&array[5]&&array[8])||(array[6]&&array[4]&&array[2])) {
            i=1;
        }
        return i;
    }
//__________________________________________________________________________________________________
    private void offButtons(int i) {
        if(i==0) {
            mButton1.setEnabled(false);
            mButton2.setEnabled(false);
            mButton3.setEnabled(false);
            mButton4.setEnabled(false);
            mButton5.setEnabled(false);
            mButton6.setEnabled(false);
            mButton7.setEnabled(false);
            mButton8.setEnabled(false);
            mButton9.setEnabled(false);
        }
        else {
            mButton1.setVisibility(View.INVISIBLE);
            mButton2.setVisibility(View.INVISIBLE);
            mButton3.setVisibility(View.INVISIBLE);
            mButton4.setVisibility(View.INVISIBLE);
            mButton5.setVisibility(View.INVISIBLE);
            mButton6.setVisibility(View.INVISIBLE);
            mButton7.setVisibility(View.INVISIBLE);
            mButton8.setVisibility(View.INVISIBLE);
            mButton9.setVisibility(View.INVISIBLE);
        }
    }
//__________________________________________________________________________________________________
    private void onButtons(int i) {
        if(i==0) {
            mButton1.setEnabled(true);
            mButton2.setEnabled(true);
            mButton3.setEnabled(true);
            mButton4.setEnabled(true);
            mButton5.setEnabled(true);
            mButton6.setEnabled(true);
            mButton7.setEnabled(true);
            mButton8.setEnabled(true);
            mButton9.setEnabled(true);
        }
        else {
            mButton1.setVisibility(View.VISIBLE);
            mButton2.setVisibility(View.VISIBLE);
            mButton3.setVisibility(View.VISIBLE);
            mButton4.setVisibility(View.VISIBLE);
            mButton5.setVisibility(View.VISIBLE);
            mButton6.setVisibility(View.VISIBLE);
            mButton7.setVisibility(View.VISIBLE);
            mButton8.setVisibility(View.VISIBLE);
            mButton9.setVisibility(View.VISIBLE);
        }
    }
//__________________________________________________________________________________________________
    private void LogicX(int[] logic) { //0-порядок хода, 1-ход ИИ, 2-ход игрока, ОСТАЛЬНОЕ НЕ ВАЖНО
        if(logic[0]==0) {
            logic[1] = 5;
            logic[0] = 1;
        }
        else if(logic[0]==1) {
            if(logic[2]==2) {
                int R = (int)(Math.random()*2);
                int[] freeTurnsTemp0 = {7,9};
                int num = freeTurnsTemp0[R];
                logic[1] = num;
                logic[0] = 10;
            }
            else {
                logic[1] = 9;
                logic[0] = 2;
            }
        }
        else if(logic[0]==2) {
            if(logic[2]==6) {
                int R =(int)(Math.random()*2);
                int[] freeTurnsTemp0 = {7,8};
                int num=freeTurnsTemp0[R];
                logic[1] = num;
                logic[0] = 10;
            }
            else if(logic[2]==8) {
                int R = (int) (Math.random() * 2);
                int[] freeTurnsTemp0 = {3, 6};
                int num = freeTurnsTemp0[R];
                logic[1] = num;
                logic[0] = 10;
            }
            else {
                logic[1] = 0;
                logic[0] = 3;
            }
        }
        else if(logic[0]==3) {
            if ((logic[2] == 2) && (logic[1] == 8)) {
                int R = (int) (Math.random() * 2);
                int[] freeTurnsTemp0 = {4, 6};
                int num = freeTurnsTemp0[R];
                logic[1] = num;
                logic[0] = 10;
            }
            else if ((logic[2] == 4) && (logic[1] == 6)) {
                int R = (int) (Math.random() * 2);
                int[] freeTurnsTemp0 = {2, 8};
                int num = freeTurnsTemp0[R];
                logic[1] = num;
                logic[0] = 10;
            }
            else {
                logic[1] = 0;
                logic[0] = 10;
            }
        }
        else{
            logic[1]=0;
        }
    }
//__________________________________________________________________________________________________
    private Button chooseButton(int num) {
        Button button=mButton1;
        switch (num) {
            case 1: button=mButton1;break;
            case 2: button=mButton2;break;
            case 3: button=mButton3;break;
            case 4: button=mButton4;break;
            case 5: button=mButton5;break;
            case 6: button=mButton6;break;
            case 7: button=mButton7;break;
            case 8: button=mButton8;break;
            case 9: button=mButton9;break;
        }
        return button;
    }
//__________________________________________________________________________________________________
    private int сheckFreeSpace(boolean[] humanArray,boolean[] aiArray) {
        int turn = 0;
        int R=0;
        int[] turnArray= {0,0};
        R =(int)(Math.random()*2);
        if(!humanArray[0]&&!aiArray[0]){
            if(turnArray[0]==0) {
                turnArray[0]=1;
            }
            else {
                turnArray[1]=1;
            }
        }
        if(!humanArray[1]&&!aiArray[1]){
            if(turnArray[0]==0) {
                turnArray[0]=2;
            }
            else {
                turnArray[1]=2;
            }
        }
        if(!humanArray[2]&&!aiArray[2]){
            if(turnArray[0]==0) {
                turnArray[0]=3;
            }
            else {
                turnArray[1]=3;
            }
        }
        if(!humanArray[3]&&!aiArray[3]){
            if(turnArray[0]==0) {
                turnArray[0]=4;
            }
            else {
                turnArray[1]=4;
            }
        }
        if(!humanArray[4]&&!aiArray[4]){
            if(turnArray[0]==0) {
                turnArray[0]=5;
            }
            else {
                turnArray[1]=5;
            }
        }
        if(!humanArray[5]&&!aiArray[5]){
            if(turnArray[0]==0) {
                turnArray[0]=6;
            }
            else {
                turnArray[1]=6;
            }
        }
        if(!humanArray[6]&&!aiArray[6]){
            if(turnArray[0]==0) {
                turnArray[0]=7;
            }
            else {
                turnArray[1]=7;
            }
        }
        if(!humanArray[7]&&!aiArray[7]){
            if(turnArray[0]==0) {
                turnArray[0]=8;
            }
            else {
                turnArray[1]=8;
            }
        }
        if(!humanArray[8]&&!aiArray[8]){
            if(turnArray[0]==0) {
                turnArray[0]=9;
            }
            else {
                turnArray[1]=9;
            }
        }
        turn=turnArray[R];
        return turn;
    }
//__________________________________________________________________________________________________
    private int сheckTwoInRow(boolean[] humanArray,boolean[] aiArray) {
        int turn = 0;
        if((humanArray[0]&&humanArray[1])&&(!aiArray[2])){
            turn=3;
        }
        else if ((humanArray[1]&&humanArray[2])&&(!aiArray[0])){
            turn=1;
        }
        else if ((humanArray[0]&&humanArray[2])&&(!aiArray[1])){
            turn=2;
        }
        else if ((humanArray[3]&&humanArray[4])&&(!aiArray[5])){
            turn=6;
        }
        else if ((humanArray[4]&&humanArray[5])&&(!aiArray[3])){
            turn=4;
        }
        else if ((humanArray[3]&&humanArray[5])&&(!aiArray[4])){
            turn=5;
        }
        else if ((humanArray[6]&&humanArray[7])&&(!aiArray[8])){
            turn=9;
        }
        else if ((humanArray[7]&&humanArray[8])&&(!aiArray[6])){
            turn=7;
        }
        else if ((humanArray[6]&&humanArray[8])&&(!aiArray[7])){
            turn=8;
        }
        else if ((humanArray[0]&&humanArray[3])&&(!aiArray[6])){
            turn=7;
        }
        else if ((humanArray[3]&&humanArray[6])&&(!aiArray[0])){
            turn=1;
        }
        else if ((humanArray[0]&&humanArray[6])&&(!aiArray[3])){
            turn=4;
        }
        else if ((humanArray[1]&&humanArray[4])&&(!aiArray[7])){
            turn=8;
        }
        else if ((humanArray[4]&&humanArray[7])&&(!aiArray[1])){
            turn=2;
        }
        else if ((humanArray[1]&&humanArray[7])&&(!aiArray[4])){
            turn=5;
        }
        else if ((humanArray[2]&&humanArray[5])&&(!aiArray[8])){
            turn=9;
        }
        else if ((humanArray[2]&&humanArray[8])&&(!aiArray[5])){
            turn=6;
        }
        else if ((humanArray[5]&&humanArray[8])&&(!aiArray[2])){
            turn=3;
        }
        else if ((humanArray[0]&&humanArray[4])&&(!aiArray[8])){
            turn=9;
        }
        else if ((humanArray[4]&&humanArray[8])&&(!aiArray[0])){
            turn=1;
        }
        else if ((humanArray[0]&&humanArray[8])&&(!aiArray[4])){
            turn=5;
        }
        else if ((humanArray[6]&&humanArray[4])&&(!aiArray[2])){
            turn=3;
        }
        else if ((humanArray[4]&&humanArray[2])&&(!aiArray[6])){
            turn=7;
        }
        else if ((humanArray[6]&&humanArray[2])&&(!aiArray[4])){
            turn=5;
        }
        return turn;
    }
//__________________________________________________________________________________________________
    private int rotateNumber(int firstTurn) {
        int i=0;
        if ((firstTurn==4)||(firstTurn==7)) {
            i=1;
        }
        else if ((firstTurn==8)||(firstTurn==9)) {
            i=2;
        }
        else if ((firstTurn==3)||(firstTurn==6)) {
            i=3;
        }
        return i;
    }
//__________________________________________________________________________________________________
    private int rotate(int a, int i) {
        switch (i) {
            case (1):
                switch (a) {
                    case (7):a=1;break;
                    case (4):a=2;break;
                    case (1):a=3;break;
                    case (8):a=4;break;
                    case (5):a=5;break;
                    case (2):a=6;break;
                    case (9):a=7;break;
                    case (6):a=8;break;
                    case (3):a=9;break;
                };break;
            case (2):
                switch (a) {
                    case (9):a=1;break;
                    case (8):a=2;break;
                    case (7):a=3;break;
                    case (6):a=4;break;
                    case (5):a=5;break;
                    case (4):a=6;break;
                    case (3):a=7;break;
                    case (2):a=8;break;
                    case (1):a=9;break;
                };break;
            case (3):
                switch (a) {
                    case (3):a=1;break;
                    case (6):a=2;break;
                    case (9):a=3;break;
                    case (2):a=4;break;
                    case (5):a=5;break;
                    case (8):a=6;break;
                    case (1):a=7;break;
                    case (4):a=8;break;
                    case (7):a=9;break;
                };break;
        }
        return a;
    }
//__________________________________________________________________________________________________
    public static int rotateAway(int b, int i) {
        switch (i) {
            case (1):
                switch (b) {
                    case (1):b=7;break;
                    case (2):b=4;break;
                    case (3):b=1;break;
                    case (4):b=8;break;
                    case (5):b=5;break;
                    case (6):b=2;break;
                    case (7):b=9;break;
                    case (8):b=6;break;
                    case (9):b=3;break;
                };break;
            case (2):
                switch (b) {
                    case (1):b=9;break;
                    case (2):b=8;break;
                    case (3):b=7;break;
                    case (4):b=6;break;
                    case (5):b=5;break;
                    case (6):b=4;break;
                    case (7):b=3;break;
                    case (8):b=2;break;
                    case (9):b=1;break;
                };break;
            case (3):
                switch (b) {
                    case (1):b=3;break;
                    case (2):b=6;break;
                    case (3):b=9;break;
                    case (4):b=2;break;
                    case (5):b=5;break;
                    case (6):b=8;break;
                    case (7):b=1;break;
                    case (8):b=4;break;
                    case (9):b=7;break;
                };break;
        }
        return b;
    }
}