package com.example.zephaniah.doudz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class Player {

    int[] pokers;// 玩家手中的牌
    boolean[] pokersFlag;// 玩家选中牌的标志
    int id;// 玩家ID
    Desk desk;// 玩家所在桌子
    Discard card;// 玩家最近出的一手牌
    int theRestCard;// 玩家剩下的牌数
    APieceOfCard[] myPoker;
    Bitmap player1;
    Bitmap player2;
    Bitmap player3;
    Bitmap table;
    int tableHeight;
    float interrupt = MainActivity.ScreenWidth/25.74f;//每张牌的间隔
    Player last;
    Player next;
    GameActivity gameActivity;

    public Player(int[] pokers, int id, Desk desk,GameActivity gameActivity) {
        this.desk = desk;
        this.id = id;
        this.pokers = pokers;
        theRestCard = pokers.length;
        pokersFlag = new boolean[pokers.length];
        this.gameActivity = gameActivity;
        player1 = BitmapFactory.decodeResource (gameActivity.getResources(),R.drawable.daheng);
        player2 = BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.luoli);
        player3 = BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.arcyujie);
        table = Tools.scaleImageByScreen(BitmapFactory.decodeResource (gameActivity.getResources(), R.drawable.table8),MainActivity.ScreenWidth,MainActivity.ScreenHeight * 0.666f);
        tableHeight = table.getHeight();
        myPoker = new APieceOfCard[54];

        for (int i = 0; i < 13; i++){//红色数字和花色组合,数组双数下标存放红色
            for (int j = 0; j < 2; j++){
                APieceOfCard card = new APieceOfCard();
                card.cardType = Tools.scaleImageByScreen(Tools.split(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.pokertype),4,1).get(2 * j),MainActivity.ScreenWidth *0.025f,MainActivity.ScreenHeight *0.045f);
//                Log.e("cardtype", String.valueOf(card.cardType));
                card.cardNum = Tools.scaleImageByScreen(Tools.split(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.pokernum2),26,1).get(i),MainActivity.ScreenWidth *0.025f,MainActivity.ScreenHeight *0.06f);
                card.cardNumRotate = Tools.Rotate180(card.cardNum);
                card.cardTypeRotate = Tools.Rotate180(card.cardType);
//                Log.e("rotate", String.valueOf(card.cardNumRotate));
                card.cardFrame = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.cardframe),  MainActivity.ScreenWidth * 0.105f , MainActivity.ScreenHeight * 0.235f);
                myPoker[2 * (i * 2 + j)] = card;//2i+j=0,1,2,3......,所以2(2i+j)=0,2,4,6......
            }
        }
        for (int i = 13; i < 26; i++){//黑色数字和花色组合，数组单数下标存放黑色
            for (int j = 1; j < 3; j++){
                APieceOfCard card = new APieceOfCard();
                card.cardType = Tools.scaleImageByScreen(Tools.split(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.pokertype),4,1).get(2 * j - 1),MainActivity.ScreenWidth *0.025f,MainActivity.ScreenHeight *0.045f);
                card.cardNum = Tools.scaleImageByScreen(Tools.split(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.pokernum2),26,1).get(i),MainActivity.ScreenWidth *0.025f,MainActivity.ScreenHeight *0.06f);
                card.cardNumRotate = Tools.Rotate180(card.cardNum);
                card.cardTypeRotate = Tools.Rotate180(card.cardType);
                card.cardFrame = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.cardframe), MainActivity.ScreenWidth * 0.105f, MainActivity.ScreenHeight * 0.235f);
                myPoker[2 * (j + (i - 13) *2) - 1] = card;//2(i-13)+j=1,2,3......,所以2(2(i-13)+j)-1=1,3,5,7......
            }
        }
        APieceOfCard joker1 = new APieceOfCard();
        APieceOfCard joker2 = new APieceOfCard();
        joker1.cardFrame = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.cardframe), MainActivity.ScreenWidth * 0.105f, MainActivity.ScreenHeight * 0.235f);
        joker1.cardNum = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.smallking), MainActivity.ScreenWidth * 0.1f, MainActivity.ScreenHeight * 0.218f);
        joker1.cardLord = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.lordcardlogo), MainActivity.ScreenWidth * 0.105f, MainActivity.ScreenHeight * 0.235f);
        joker2.cardFrame = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.cardframe), MainActivity.ScreenWidth * 0.105f, MainActivity.ScreenHeight * 0.235f);
        joker2.cardNum = Tools.scaleImageByScreen(BitmapFactory.decodeResource(gameActivity.getResources(),R.drawable.bigking), MainActivity.ScreenWidth * 0.1f, MainActivity.ScreenHeight * 0.218f);
        myPoker[52] = joker1;
        myPoker[53] = joker2;
    }

    // 设置玩家上下家关系
    public void setPosition(Player last, Player next) {
        this.last = last;
        this.next = next;
    }

    // 绘制玩家及其手中的牌
    public void paintMyThing(Canvas canvas) {
        canvas.drawBitmap(player2,Tools.imageScaleTran(0.55f,0.55f,100,100),null);
        canvas.drawBitmap(player3,Tools.imageScaleTran(0.55f,0.55f,MainActivity.ScreenWidth * 7/10+100,110),null);
//        System.out.println("MainActivity.ScreenHeight:"+MainActivity.ScreenHeight);
        canvas.drawBitmap(table,Tools.imageScaleTran(1,1,0,MainActivity.ScreenHeight - tableHeight),null);
        canvas.drawBitmap(player1,Tools.imageScaleTran(0.65f,0.65f,0,MainActivity.ScreenHeight / 2),null);

        int select;
        for (int i = 0 ; i < pokers.length ; i++) {

            if (pokersFlag[i]){
                select = 50;
            }else
                select = 0;
            canvas.drawBitmap (myPoker[pokers[i]].cardFrame,  MainActivity.ScreenWidth * 0.15f +interrupt * i, MainActivity.ScreenHeight * 0.73f-select, null);
            canvas.drawBitmap(myPoker[pokers[i]].cardNum, MainActivity.ScreenWidth * 0.159f +interrupt * i, MainActivity.ScreenHeight * 0.742f-select, null);
            if (pokers[i]<52) {
                canvas.drawBitmap(myPoker[pokers[i]].cardType,  MainActivity.ScreenWidth * 0.16f +interrupt * i, MainActivity.ScreenHeight * 0.742f + myPoker[i].cardNum.getHeight()-select, null);
                canvas.drawBitmap(myPoker[pokers[i]].cardTypeRotate,  MainActivity.ScreenWidth * 0.17f +interrupt * i + myPoker[pokers[i]].cardFrame.getWidth()/2 , MainActivity.ScreenHeight * 0.742f + myPoker[pokers[i]].cardFrame.getHeight()/ 2 - 10 - select, null);
                canvas.drawBitmap(myPoker[pokers[i]].cardNumRotate,  MainActivity.ScreenWidth * 0.17f +interrupt * i + myPoker[pokers[i]].cardFrame.getWidth()/2 , MainActivity.ScreenHeight * 0.742f + myPoker[pokers[i]].cardFrame.getHeight()/ 2 + myPoker[pokers[i]].cardType.getHeight()- 10 - select, null);
            }
            if (Desk.boss == 0 && i == pokers.length-1){//在最小的一张牌上画地主logo
                canvas.drawBitmap (myPoker[52].cardLord,  MainActivity.ScreenWidth * 0.15f +interrupt * i, MainActivity.ScreenHeight * 0.73f-select, null);
            }

        }
    }

    // 判断出牌的人工智能
    public Discard chupaiAI(Discard card) {//传入的参数是桌面上最新的一手牌

        int[] pokerWanted = null;//需要打出去的牌
        if (card == null) {
            // 玩家出一手牌
            pokerWanted = Poker.outAsetofCard(pokers, last, next);
        } else {
            // 玩家需要出一手比card大的牌
            pokerWanted = Poker.outTheRightCard(card, pokers, last, next);
        }
        // 如果不能出牌，则返回
        if (pokerWanted == null) {
            return null;
        }
        // 以下为出牌的后续操作，将牌从玩家手中剔除
        for (int i = 0; i < pokerWanted.length; i++) {
            for (int j = 0; j < pokers.length; j++) {
                if (pokers[j] == pokerWanted[i]) {
                    pokers[j] = -1;
                    break;
                }
            }
        }
        int[] newpokers = new int[0];//剔除已经出的牌之后的剩下的牌
        if (pokers.length - pokerWanted.length > 0) {
            newpokers = new int[pokers.length - pokerWanted.length];
        }
        int j = 0;
        for (int i = 0; i < pokers.length; i++) {
            if (pokers[i] != -1) {
                newpokers[j] = pokers[i];
                j++;
            }
        }
        pokers = newpokers;
        theRestCard = pokers.length;
        Discard thiscard = new Discard(pokerWanted, id);
        // 更新打出桌子的最近的一手牌
        desk.currentCard = thiscard;
        this.card = thiscard;//更新本人打出的最新一手牌
        return thiscard;
    }

    public Discard chupai(Discard card) {//真人玩家出牌
        int count = 0;
        for (int i = 0; i < pokers.length; i++) {
            if (pokersFlag[i]) {
                count++;
            }
        }
        int[] cardPokers = new int[count];//存放玩家当前出的牌
        int j = 0;
        for (int i = 0; i < pokers.length; i++) {
            if (pokersFlag[i]) {
                cardPokers[j] = pokers[i];//把被玩家选中的牌存进数组
                j++;
            }
        }
        int cardType = Poker.getPokerType(cardPokers);
        System.out.println("Player236,cardType:" + cardType);
        if (cardType == PokerType.error) {
            return null;
        }
        Discard thiscard = new Discard(cardPokers, id);
        if (card == null) {//如果玩家是本轮第一个出牌，直接更新剩下的牌
            desk.currentCard = thiscard;
            this.card = thiscard;

            int[] newPokers = new int[pokers.length - count];
            int n = 0;
            for (int i = 0; i < pokers.length; i++) {
                if (!pokersFlag[i]) {
                    newPokers[n] = pokers[i];
                    n++;
                }
            }
            pokers = newPokers;
            theRestCard = pokers.length;
            pokersFlag = new boolean[pokers.length];
            return thiscard;
        } else {//否则比较一下上一个人出的牌

            if (Poker.compare(thiscard, card)) {
                desk.currentCard = thiscard;
                this.card = thiscard;

                int[] newPokers = new int[pokers.length - count];
                int ni = 0;
                for (int i = 0; i < pokers.length; i++) {
                    if (!pokersFlag[i]) {
                        newPokers[ni] = pokers[i];
                        ni++;
                    }
                }
                pokers = newPokers;
                theRestCard = pokers.length;
                pokersFlag = new boolean[pokers.length];
                return thiscard;
            } else {
                return null;
            }
        }
    }

    // 当玩家自己操作时，触摸屏事件的处理
    public void onTuch(View v, MotionEvent event) {

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        if (x >= MainActivity.ScreenWidth * 0.15f + interrupt * (pokers.length-1) && x <= MainActivity.ScreenWidth * 0.15f + interrupt * (pokers.length-1) +myPoker[1].cardFrame.getWidth() && y >= MainActivity.ScreenHeight * 0.73f && y <= MainActivity.ScreenHeight * 0.73f + myPoker[1].cardFrame.getHeight()) {
            pokersFlag[pokers.length-1] = !pokersFlag[pokers.length-1];//最右边的牌的范围是整一张牌
            return;
        }
        for (int i = 0; i < pokers.length-1; i++) {
            // 判断是哪张牌被选中，设置标志
            if (x >= MainActivity.ScreenWidth * 0.15f + interrupt * i && x <= MainActivity.ScreenWidth * 0.15f + interrupt * i + interrupt && y >= MainActivity.ScreenHeight * 0.73f && y <= MainActivity.ScreenHeight * 0.73f + myPoker[i].cardFrame.getHeight()) {
//                System.out.println("x"+x+","+">"+(MainActivity.ScreenWidth * 0.15f + interrupt * i)+","+"<"+(MainActivity.ScreenWidth * 0.15f + interrupt * i + interrupt)+"i"+i+pokesFlag[i]);
                pokersFlag[i] = !pokersFlag[i];
                break;
            }
        }
    }
}
