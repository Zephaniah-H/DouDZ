package com.example.zephaniah.doudz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;

public class Desk {

    Bitmap buyao;
    Bitmap bluebuyao;
    Bitmap chupai;
    Bitmap blueButton;
    Bitmap yellowButton;
    Bitmap cardBack;
    Bitmap clock;
    Bitmap lordlogo;
    Bitmap iwin;
    Bitmap ilose;
    boolean buyaopai[];
    boolean playExcitingMusic;

    public int GP = -1;// 游戏的进度控制
    public static int winId = -1;//游戏胜利的人id
    public static int threePokers[] = new int[3];// 三张底牌
    public static Player[] players = new Player[3];// 三个玩家
    public static int[] deskPokers = new int[54];// 一副扑克牌
    public static int boss = 0;// 地主
    public static int currentPlayer = 0;// 当前操作的人
    public static int currentCircle = 0;
    public static Discard currentCard = null;// 出的最新的一手牌
    public int[][] playerPokers = new int[3][17];
    int timeLimite = 290;

    GameActivity gameActivity;

    public Desk(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        buyao = Tools.scaleImageByScreen(BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.textpass1), MainActivity.ScreenWidth*0.09f,MainActivity.ScreenHeight*0.07f);
        bluebuyao = Tools.scaleImageByScreen(BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.playno), MainActivity.ScreenWidth*0.1f,MainActivity.ScreenHeight*0.09f);
        chupai = Tools.scaleImageByScreen(BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.textplay), MainActivity.ScreenWidth*0.09f,MainActivity.ScreenHeight*0.07f);
        blueButton = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.but_new3),MainActivity.ScreenWidth*0.12f,MainActivity.ScreenHeight*0.10f);
        yellowButton = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.but_new2),MainActivity.ScreenWidth*0.12f,MainActivity.ScreenHeight*0.10f);
        cardBack = BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.goldencardback);
        clock = BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.roomclock);
        lordlogo = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.lordcap),MainActivity.ScreenWidth*0.065f,MainActivity.ScreenHeight*0.11f);
        iwin = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.iwin),MainActivity.ScreenWidth/2,MainActivity.ScreenHeight/3);
        ilose = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.ilose),MainActivity.ScreenWidth/2,MainActivity.ScreenHeight/3);
    }

    public void gameLogic() {
        switch (GP) {
            case -1://发牌，随机地主
                init();
                GP = 0;
                break;
            case 0://游戏中
                gaming();
                break;
            case 1://游戏结束或重新开始
                break;
        }
    }

    public void gaming() {
        //游戏结束
        for (int k = 0; k < 3; k++) {
            //第一个剩余牌数为0的玩家
            if (players[k].theRestCard == 0) {
                // 切换到游戏结束状态
                GP = 1;
                // 得到最先出完牌的人的id
                winId = k;
                //播放胜利或失败音乐
                winORloseMusic(k);
                return;
            }
        }
        // 游戏继续。
        // 如果本家ID是AI玩家，调用ai的出牌算法
        if (currentPlayer == 1 || currentPlayer == 2) {
            if (timeLimite <= 250) {
                // 获取手中的牌中能够打过当前手牌
                Discard tempcard = players[currentPlayer].chupaiAI(currentCard);
                if (tempcard != null) {
                    // 手中有大过的牌，则出
                    buyaopai[currentPlayer] = false;
                    currentCircle++;
                    currentCard = tempcard;
                    addFemaleSound(tempcard);
                    nextPlayer();
                    System.out.print(OpeningVideoActivity.gamingSoundpool);
                } else {
                    // 没有打过的牌，则不要
                    buyao();
                    if (currentPlayer == 0){
                        OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nbuyao1"),1,1,0,0,1);
                    }
                    if (currentPlayer == 1){
                        OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nbuyao2"),1,1,0,0,1);
                    }
                }
            }

        }
        // 时间倒计时
        timeLimite -= 2;
//        System.out.println("time,,"+timeLimite);
//        if (timeLimite<10){
//            buyao();
//        }

    }

    public void init() {
        deskPokers = new int[54];
        playerPokers = new int[3][17];
        threePokers = new int[3];
        winId = -1;
        currentCard = null;
        currentCircle = 0;
        currentPlayer = 0;
        buyaopai = new boolean[3];
        playExcitingMusic = false;

        for (int i = 0; i < deskPokers.length; i++) {//初始化一副牌为0-53
            deskPokers[i] = i;
        }
        Poker.XiPai(deskPokers);
        FaPai(deskPokers);
        randDZ();
        Poker.sort(playerPokers[0]);
        Poker.sort(playerPokers[1]);
        Poker.sort(playerPokers[2]);
        players[0] = new Player(playerPokers[0], 0, this,gameActivity);
        players[1] = new Player(playerPokers[1], 1, this,gameActivity);
        players[2] = new Player(playerPokers[2], 2, this,gameActivity);
        players[0].setPosition(players[1], players[2]);
        players[1].setPosition(players[2], players[0]);
        players[2].setPosition(players[0], players[1]);

        AnalyzePoker ana = new AnalyzePoker();
        for (int i = 0; i < players.length; i++) {
            boolean b = ana.testAnalyze(playerPokers[i]);
            if (!b) {
                init();
                System.out.println("Desk215,chongxinglaiguo");
                break;
            }
        }
        for (int i = 0; i < 3; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("Desk221,chushipai---" + i + ":");
            for (int j = 0; j < playerPokers[i].length; j++) {
                sb.append(Poker.getPokerSurfaceValue(playerPokers[i][j]) + ",");
            }
            System.out.println(sb.toString());
        }
    }

    // 随机地主，将三张底牌给地主
    public void randDZ() {
        boss = Poker.getDZ();//获取一个0-2的随机数
        currentPlayer = boss;
        int[] newPlayerPokers = new int[20];
        for (int i = 0; i < 17; i++) {
            newPlayerPokers[i] = playerPokers[boss][i];
        }
        newPlayerPokers[17] = threePokers[0];
        newPlayerPokers[18] = threePokers[1];

        newPlayerPokers[19] = threePokers[2];
        playerPokers[boss] = newPlayerPokers;
    }

    public void FaPai(int[] pokers) {
        for (int i = 0; i < 51;) {
            playerPokers[i / 17][i % 17] = pokers[i++];
        }
        threePokers[0] = pokers[51];
        threePokers[1] = pokers[52];
        threePokers[2] = pokers[53];
    }

    public void paint(Canvas canvas) {

        switch (GP) {
            case -1:
                break;
            case 0:
                paintGaming(canvas);
                break;
            case 1:
                paintResult(canvas);
                break;
        }

    }

    public void paintResult(Canvas canvas) {

        if (winId == 0 || (winId != boss && boss != 0)) {
            canvas.drawBitmap(iwin, (MainActivity.ScreenWidth - iwin.getWidth()) / 2, (MainActivity.ScreenHeight - iwin.getHeight()) / 2, null);
        } else
            canvas.drawBitmap(ilose, (MainActivity.ScreenWidth - iwin.getWidth()) / 2, (MainActivity.ScreenHeight - ilose.getHeight()) / 2, null);
    }

    public void paintGaming(Canvas canvas) {
        players[0].paintMyThing(canvas);
        for (int i = 0; i < 3; i++) {//画地主牌
            canvas.drawBitmap (players[i].myPoker[threePokers[i]].cardFrame, Tools.imageScaleTran(0.5f,0.5f,MainActivity.ScreenWidth*42 / 100 + 75*i,MainActivity.ScreenHeight*7 / 100), null);
            if (threePokers[i]<52) {
                canvas.drawBitmap(players[i].myPoker[threePokers[i]].cardNum, Tools.imageScaleTran(0.9f, 0.9f, MainActivity.ScreenWidth * 42 / 100 + 75 * i, MainActivity.ScreenHeight * 7 / 100), null);
                canvas.drawBitmap(players[i].myPoker[threePokers[i]].cardType, Tools.imageScaleTran(0.9f, 0.9f, MainActivity.ScreenWidth * 42 / 100 + 75 * i, MainActivity.ScreenHeight * 7 / 100 + players[i].myPoker[threePokers[i]].cardNum.getHeight()*9/10), null);
            }else
                canvas.drawBitmap(players[i].myPoker[threePokers[i]].cardNum, Tools.imageScaleTran(0.5f, 0.5f, MainActivity.ScreenWidth * 42 / 100 + 75 * i, MainActivity.ScreenHeight * 7 / 100+5), null);
        }

        if (boss == 0){
            canvas.drawBitmap(lordlogo,MainActivity.ScreenWidth*0.13f,MainActivity.ScreenWidth/3,null);
        }else if (boss == 1){
            canvas.drawBitmap(lordlogo,0,MainActivity.ScreenHeight / 4,null);
        }else if (boss == 2){
            canvas.drawBitmap(lordlogo,MainActivity.ScreenWidth*0.91f,MainActivity.ScreenHeight / 4,null);
        }

        Paint timepaint = new Paint();
        timepaint.setTextSize(65);//字体大小
        timepaint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体变粗
        if (currentPlayer == 0) {
            canvas.drawBitmap(blueButton,MainActivity.ScreenWidth*0.556f,MainActivity.ScreenHeight*0.586f,null);
            canvas.drawBitmap (chupai, MainActivity.ScreenWidth*0.571f,MainActivity.ScreenHeight*0.597f, null);
            canvas.drawBitmap(clock,Tools.imageScaleTran(2.3f,2.3f,(MainActivity.ScreenWidth*46/100),(MainActivity.ScreenHeight*57/100)),null);
            canvas.drawText(String.valueOf(timeLimite/10),(MainActivity.ScreenWidth*487/1000),(MainActivity.ScreenHeight*657/1000),timepaint);

            if (currentCircle != 0) {//不是本轮第一个出牌就画不要
                canvas.drawBitmap(yellowButton,MainActivity.ScreenWidth*0.328f,MainActivity.ScreenHeight*0.586f,null);
                canvas.drawBitmap (buyao, MainActivity.ScreenWidth*0.343f,MainActivity.ScreenHeight*0.597f, null);
            }
        }
        if (currentPlayer == 1) {
            canvas.drawBitmap(clock,Tools.imageScaleTran(2.3f,2.3f,(MainActivity.ScreenWidth*23/100),MainActivity.ScreenHeight/4),null);
            canvas.drawText(String.valueOf(timeLimite/10),(MainActivity.ScreenWidth*257/1000),(MainActivity.ScreenHeight*337/1000),timepaint);
        }
        if (currentPlayer == 2) {
            canvas.drawBitmap(clock,Tools.imageScaleTran(2.3f,2.3f,(MainActivity.ScreenWidth*66/100),MainActivity.ScreenHeight/4),null);
            canvas.drawText(String.valueOf(timeLimite/10),(MainActivity.ScreenWidth*687/1000),(MainActivity.ScreenHeight*337/1000),timepaint);
        }

        if (buyaopai[0] && currentPlayer != 0){//画不要两个字
            canvas.drawBitmap(bluebuyao,(MainActivity.ScreenWidth-bluebuyao.getWidth())/2,MainActivity.ScreenHeight*0.586f,null);
        }
        if (buyaopai[1] && currentPlayer != 1){
            canvas.drawBitmap (bluebuyao, Tools.imageScaleTran(1,1,550,MainActivity.ScreenHeight/4), null);
        }
        if (buyaopai[2] && currentPlayer != 2){
            canvas.drawBitmap (bluebuyao, Tools.imageScaleTran(1,1,1550,MainActivity.ScreenHeight/4), null);
        }

        if (players[0].card != null && currentPlayer != 0) {//画出的牌
            players[0].card.paintLostCard(canvas,players[0], 0, false);
        }
        if (players[1].card != null && currentPlayer != 1) {
            players[1].card.paintLostCard(canvas,players[1], MainActivity.ScreenWidth*21/100, true);
        }
        if (players[2].card != null && currentPlayer != 2) {
            players[2].card.paintLostCard(canvas,players[2], MainActivity.ScreenWidth*67/100, true);
        }

        //画卡背
        canvas.drawBitmap (cardBack, Tools.imageScaleTran(0.65f,0.65f,MainActivity.ScreenWidth / 5 + 80,MainActivity.ScreenHeight / 2 -120), null);//左边卡背
        canvas.drawBitmap (cardBack, Tools.imageScaleTran(0.65f,0.65f,MainActivity.ScreenWidth * 4 / 5 - 140,MainActivity.ScreenHeight / 2 -120), null);//右边卡背

        Paint paint = new Paint ();
        paint.setTextSize (60);//设置字体大小
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体变粗
        //画剩余牌数
        canvas.drawText (String.valueOf(players[1].theRestCard), MainActivity.ScreenWidth / 5 + 90,MainActivity.ScreenHeight / 2 -35, paint);
        canvas.drawText (String.valueOf(players[2].theRestCard), MainActivity.ScreenWidth *4 / 5 - 130,MainActivity.ScreenHeight / 2 -35, paint);//drawText的坐标是屏幕坐标

    }

    public void onTuch(View v, MotionEvent event) {
        for (int i = 0; i < players.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("Desk427," + i + " : ");
            for (int j = 0; j < players[i].pokers.length; j++) {
                sb.append(Poker.getPokerSurfaceValue(players[i].pokers[j]) + ",");
            }
            System.out.println(sb.toString());
        }
        players[0].onTuch(v, event);

        if (GP == 1) {
            GameActivity.gamingMusic4.reset();
            GameActivity.gamingMusic5.reset();
            GameActivity.gamingMusic1 = MediaPlayer.create(gameActivity, R.raw.normal);
            GameActivity.gamingMusic2 = MediaPlayer.create(gameActivity, R.raw.normal2);
            GameActivity.gamingMusic3 = MediaPlayer.create(gameActivity, R.raw.exciting);
            GameActivity.gamingMusic4 = MediaPlayer.create(gameActivity, R.raw.win);
            GameActivity.gamingMusic5 = MediaPlayer.create(gameActivity, R.raw.lose);
            GameActivity.gamingMusic1.start();
            GameActivity.gamingMusic1.setLooping(true);
            GP = -1;
        }
        if (currentPlayer == 0) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            if (x >= MainActivity.ScreenWidth * 0.556f && x <= MainActivity.ScreenWidth * 0.556 + blueButton.getWidth() && y >= MainActivity.ScreenHeight * 0.586 && y <= MainActivity.ScreenHeight * 0.586 + blueButton.getHeight()) {
                System.out.println("chupai");
                Discard card = players[0].chupai(currentCard);
                if (card != null) {
                    buyaopai[0] = false;
                    currentCard = card;
                    currentCircle++;
                    nextPlayer();
                    addmySound(card);
                }
            }

            if (x >= MainActivity.ScreenWidth * 0.328f && x <= MainActivity.ScreenWidth * 0.328 + yellowButton.getWidth() && y >= MainActivity.ScreenHeight * 0.586 && y <= MainActivity.ScreenHeight * 0.586 + yellowButton.getHeight()) {
                System.out.println("buyao");
                buyao();
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("buyao"), 1, 1, 0, 0, 1);
            }
        }
    }

    //不要牌的操作
    public void buyao() {
        //设置画“不要”为true
        buyaopai[currentPlayer] = true;
        // 轮到下一个人
        currentCircle++;
        // 当前不要牌的人出的最后一手牌设为空
        players[currentPlayer].card = null;
        // 定位下一个人的id
        nextPlayer();
        // 如果轮回最初的人，且前面两个人都不要，则该人继续出牌，本轮清空，新一轮开始
        if (currentCard != null && currentPlayer == currentCard.playerID) {
            currentCircle = 0;
            currentCard = null;// 转回到最大牌的那个人再出牌
            players[currentPlayer].card = null;
        }
    }

    //添加游戏中male role的音频
    public void addmySound(Discard card){
        if (Poker.getPokerType(card.pokers) == 1) {
            if (Poker.getPokerSurfaceValue(card.pokers[0]) == 3) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("3"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 4) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("4"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 5) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("5"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 6) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("6"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 7) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("7"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 8) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("8"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 9) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("9"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 10) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("10"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 11) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("11"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 12) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("12"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 13) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("13"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 14) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("A"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 15) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("2"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 16) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("xiaowang"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 17) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dawang"), 1, 1, 0, 0, 1);
            }
        }else if (Poker.getPokerType(card.pokers) == 2){
            if (Poker.getPokerSurfaceValue(card.pokers[0]) == 3) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui3"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 4) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui4"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 5) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui5"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 6) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui6"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 7) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui7"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 8) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui8"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 9) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui9"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 10) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui10"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 11) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui11"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 12) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui12"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 13) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui13"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 14) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("duiA"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 15) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("dui2"), 1, 1, 0, 0, 1);
            }
        }else if (Poker.getPokerType(card.pokers) == 3){
            if (Poker.getPokerSurfaceValue(card.pokers[0]) == 3) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple3"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 4) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple4"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 5) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple5"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 6) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple6"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 7) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple7"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 8) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple8"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 9) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple9"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 10) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple10"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 11) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple11"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 12) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple12"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 13) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple13"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 14) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tupleA"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 15) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("tuple2"), 1, 1, 0, 0, 1);
            }
        }else if (Poker.getPokerType(card.pokers) == 4){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("sandaiyi"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 5){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("sandaiyidui"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 6){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("shunzi"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("star"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 7){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("liandui"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("flower"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 8 || Poker.getPokerType(card.pokers) == 9 || Poker.getPokerType(card.pokers) == 10){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("feiji"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("plane"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 11){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("sidaier"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 12){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("sidailiangdui"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 13){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("zhadan"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("bomb"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 14){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("wangzha"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("bomb_wangzha"),1,1,0,0,1);
        }
        if (players[0].theRestCard == 2 && playExcitingMusic) {
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("liangzhangpai"),1,1,0,0,1);
        }
        if (players[0].theRestCard == 1 && playExcitingMusic) {
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("yizhangpai"), 1, 1, 0, 0, 1);
        }
        if (players[0].theRestCard == 2 && !playExcitingMusic){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("liangzhangpai"),1,1,0,0,1);
            if (GameActivity.gamingMusic1.isPlaying()) {
                GameActivity.gamingMusic1.reset();
            }
//            if (GameActivity.gamingMusic2.isPlaying()) {
//                GameActivity.gamingMusic2.reset();
//            }
            if (!GameActivity.gamingMusic3.isPlaying()) {
                GameActivity.gamingMusic3.start();
            }
            GameActivity.gamingMusic3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    if (!GameActivity.gamingMusic3.isPlaying()){
//                    GameActivity.gamingMusic1 = MediaPlayer.create(gameActivity,R.raw.normal);
//                    GameActivity.gamingMusic2 = MediaPlayer.create(gameActivity,R.raw.normal2);
//                        try {
//                            GameActivity.gamingMusic1.prepare();
//                            GameActivity.gamingMusic2.prepare();
                            GameActivity.gamingMusic2.start();
                            GameActivity.gamingMusic2.setLooping(true);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                }
            });
            playExcitingMusic = true;
            return;
        }
        if (players[0].theRestCard == 1 && !playExcitingMusic){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("yizhangpai"),1,1,0,0,1);
            if (GameActivity.gamingMusic1.isPlaying()) {
                GameActivity.gamingMusic1.reset();
            }
//            if (GameActivity.gamingMusic2.isPlaying()) {
//                GameActivity.gamingMusic2.reset();
//            }
            if (!GameActivity.gamingMusic3.isPlaying()) {
                GameActivity.gamingMusic3.start();
            }
            GameActivity.gamingMusic3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    if (!GameActivity.gamingMusic3.isPlaying()){
//                    GameActivity.gamingMusic1 = MediaPlayer.create(gameActivity,R.raw.normal);
//                    GameActivity.gamingMusic2 = MediaPlayer.create(gameActivity,R.raw.normal2);
//                        try {
//                            GameActivity.gamingMusic1.prepare();
//                            GameActivity.gamingMusic2.prepare();
                            GameActivity.gamingMusic2.start();
                            GameActivity.gamingMusic2.setLooping(true);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                }
            });
            playExcitingMusic = true;
        }

    }
    //添加femal role音频
    public void addFemaleSound(Discard card){
        if (Poker.getPokerType(card.pokers) == 1) {
            if (Poker.getPokerSurfaceValue(card.pokers[0]) == 3) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n3"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 4) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n4"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 5) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n5"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 6) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n6"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 7) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n7"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 8) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n8"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 9) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n9"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 10) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n10"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 11) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n11"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 12) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n12"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 13) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n13"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 14) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nA"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 15) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("n2"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 16) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nxiaowang"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 17) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndawang"), 1, 1, 0, 0, 1);
            }
        }else if (Poker.getPokerType(card.pokers) == 2){
            if (Poker.getPokerSurfaceValue(card.pokers[0]) == 3) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui3"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 4) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui4"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 5) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui5"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 6) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui6"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 7) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui7"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 8) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui8"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 9) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui9"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 10) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui10"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 11) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui11"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 12) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui12"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 13) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui13"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 14) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nduiA"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 15) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ndui2"), 1, 1, 0, 0, 1);
            }
        }else if (Poker.getPokerType(card.pokers) == 3){
            if (Poker.getPokerSurfaceValue(card.pokers[0]) == 3) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple3"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 4) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple4"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 5) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple5"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 6) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple6"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 7) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple7"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 8) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple8"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 9) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple9"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 10) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple10"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 11) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple11"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 12) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple12"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 13) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple13"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 14) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntupleA"), 1, 1, 0, 0, 1);
            } else if (Poker.getPokerSurfaceValue(card.pokers[0]) == 15) {
                OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("ntuple2"), 1, 1, 0, 0, 1);
            }
        }else if (Poker.getPokerType(card.pokers) == 4){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nsandaiyi"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 5){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nsandaiyidui"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 6){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nshunzi"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("star"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 7){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nliandui"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("flower"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 8 || Poker.getPokerType(card.pokers) == 9 || Poker.getPokerType(card.pokers) == 10){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nfeiji"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("plane"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 11){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nsidaier"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 12){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nsidailiangdui"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 13){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nzhadan"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("bomb"),1,1,0,0,1);
        }else if (Poker.getPokerType(card.pokers) == 14){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nwangzha"),1,1,0,0,1);
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("bomb_wangzha"),1,1,0,0,1);
        }
        if (players[currentPlayer].theRestCard == 2 && playExcitingMusic) {
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nliangzhangpai"),1,1,0,0,1);
        }
        if (players[currentPlayer].theRestCard == 1 && playExcitingMusic) {
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nyizhangpai"),1,1,0,0,1);
        }
        if (players[currentPlayer].theRestCard == 2 && !playExcitingMusic){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nliangzhangpai"),1,1,0,0,1);
            System.out.println("music1"+GameActivity.gamingMusic1.isPlaying());
            if (GameActivity.gamingMusic1.isPlaying()) {
                GameActivity.gamingMusic1.reset();
            }
            System.out.println("music2"+GameActivity.gamingMusic2.isPlaying());
//            if (GameActivity.gamingMusic2.isPlaying()) {
//                GameActivity.gamingMusic2.reset();
//            }
            if (!GameActivity.gamingMusic3.isPlaying()) {
                GameActivity.gamingMusic3.start();
            }
            GameActivity.gamingMusic3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    if (!GameActivity.gamingMusic3.isPlaying()){
//                    GameActivity.gamingMusic1 = MediaPlayer.create(gameActivity,R.raw.normal);
//                    GameActivity.gamingMusic2 = MediaPlayer.create(gameActivity,R.raw.normal2);
//                        try {
//                            GameActivity.gamingMusic1.prepare();
//                            GameActivity.gamingMusic2.prepare();
                            GameActivity.gamingMusic2.start();
                            GameActivity.gamingMusic2.setLooping(true);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                }
            });
            playExcitingMusic = true;
            return;
        }
        if (players[currentPlayer].theRestCard == 1 && !playExcitingMusic){
            OpeningVideoActivity.gamingSoundpool.play(OpeningVideoActivity.poolMap.get("nyizhangpai"),1,1,0,0,1);
            System.out.println("music1"+GameActivity.gamingMusic1.isPlaying());
            if (GameActivity.gamingMusic1.isPlaying()) {
                GameActivity.gamingMusic1.reset();
            }
            System.out.println("music2"+GameActivity.gamingMusic2.isPlaying());
//            if (GameActivity.gamingMusic2.isPlaying()) {
//                GameActivity.gamingMusic2.reset();
//            }
            if (!GameActivity.gamingMusic3.isPlaying()) {
                GameActivity.gamingMusic3.start();
            }
            GameActivity.gamingMusic3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    if (!GameActivity.gamingMusic3.isPlaying()){
//                    GameActivity.gamingMusic1 = MediaPlayer.create(gameActivity,R.raw.normal);
//                    GameActivity.gamingMusic2 = MediaPlayer.create(gameActivity,R.raw.normal2);
//                        try {
//                            GameActivity.gamingMusic1.prepare();
//                            GameActivity.gamingMusic2.prepare();
                            GameActivity.gamingMusic2.start();
                            GameActivity.gamingMusic2.setLooping(true);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
//                }
            });
            playExcitingMusic = true;
        }

    }

    //一局游戏结束之后播放胜利或者失败音乐
    public void winORloseMusic(int winId){
        GameActivity.gamingMusic1.reset();
        GameActivity.gamingMusic2.reset();
        GameActivity.gamingMusic3.reset();
        if (winId == 0 || (winId != boss && boss != 0)){
            GameActivity.gamingMusic4.start();
        }else
            GameActivity.gamingMusic5.start();
    }

    // 定位下一个人的id并重新倒计时
    public void nextPlayer() {
        switch (currentPlayer) {
            case 0:
                currentPlayer = 2;
                break;
            case 1:
                currentPlayer = 0;
                break;
            case 2:
                currentPlayer = 1;
                break;
        }
        timeLimite = 290;
    }
}
