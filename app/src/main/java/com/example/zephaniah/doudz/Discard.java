package com.example.zephaniah.doudz;

import android.graphics.Canvas;

public class Discard {//玩家出的牌
    int cardValue=0;//(3~17)
    int pokerType=0;//(1~12)
    int[] pokers;//存放要出的牌
    int playerID;
    public Discard(int[] pokers,int id)
    {
        this.playerID=id;
        this.pokers=pokers;
        pokerType=Poker.getPokerType(pokers);
        cardValue=Poker.getPokerTypeValue(pokers, pokerType);
    }

    public void paintLostCard(Canvas canvas,Player player,int width,boolean ai)//画打出来的牌
    {
        for (int i = 0; i < pokers.length; i++) {
            if (ai) {
                canvas.drawBitmap(player.myPoker[pokers[i]].cardFrame, Tools.imageScaleTran(0.65f, 0.65f, width + 80 * i, MainActivity.ScreenHeight / 4), null);
                if (pokers[i] < 52) {
                    canvas.drawBitmap(player.myPoker[pokers[i]].cardNum, Tools.imageScaleTran(1, 1, width + 10 + 80 * i, MainActivity.ScreenHeight / 4), null);
                    canvas.drawBitmap(player.myPoker[pokers[i]].cardType, Tools.imageScaleTran(1, 1, width + 10 + 80 * i, MainActivity.ScreenHeight / 4 + player.myPoker[i].cardNum.getHeight()), null);
                }else {
                    canvas.drawBitmap(player.myPoker[pokers[i]].cardNum, Tools.imageScaleTran(0.65f, 0.65f, width + 10 + 80 * i, MainActivity.ScreenHeight / 4 + 5), null);
                }
                if (Desk.boss == player.id && i == pokers.length-1){
                    canvas.drawBitmap(player.myPoker[52].cardLord, Tools.imageScaleTran(0.65f, 0.65f, width + 80 * i, MainActivity.ScreenHeight / 4), null);
                }

            } else {
                canvas.drawBitmap (player.myPoker[pokers[i]].cardFrame, Tools.imageScaleTran(0.65f,0.65f,MainActivity.ScreenWidth*376/1000+80*i,MainActivity.ScreenHeight/2), null);
                if (pokers[i]<52) {
                    canvas.drawBitmap (player.myPoker[pokers[i]].cardNum, Tools.imageScaleTran(1,1,MainActivity.ScreenWidth*376/1000+10+80*i,MainActivity.ScreenHeight/2), null);
                    canvas.drawBitmap (player.myPoker[pokers[i]].cardType, Tools.imageScaleTran(1,1,MainActivity.ScreenWidth*376/1000+10+80*i, MainActivity.ScreenHeight/2+player.myPoker[i].cardNum.getHeight()), null);
                }else {
                    canvas.drawBitmap(player.myPoker[pokers[i]].cardNum, Tools.imageScaleTran(0.65f, 0.65f, MainActivity.ScreenWidth*376/1000+10+80*i, MainActivity.ScreenHeight/2 + 5), null);
                }
                if (Desk.boss == 0 && i == pokers.length-1){
                    canvas.drawBitmap (player.myPoker[52].cardLord, Tools.imageScaleTran(0.65f,0.65f,MainActivity.ScreenWidth*376/1000+80*i,MainActivity.ScreenHeight/2), null);
                }
            }

        }
    }
}
