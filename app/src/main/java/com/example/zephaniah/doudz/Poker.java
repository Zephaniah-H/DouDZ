package com.example.zephaniah.doudz;

import java.util.Random;
import java.util.List;

public class Poker {

    public static Random rand = new Random();

    // 0-53表示54张牌
    public static void XiPai(int[] pokers) {
        int len = pokers.length;
        // 对于54张牌中的任何一张，都随机找一张和它互换，将牌顺序打乱。
        // 然后再循环500次，应该能把牌完全打乱
        for (int i = 0; i<500;i++) {
            for (int l = 0; l < len; l++) {
                int des = rand.nextInt(54);
                int temp = pokers[l];
                pokers[l] = pokers[des];
                pokers[des] = temp;
            }
        }
    }

    public static int getDZ() {
        return rand.nextInt(3);
        // return 0;
    }

    // 对pokes进行从大到小排序，采用冒泡排序
    public static void sort(int[] pokers) {
        for (int i = 0; i < pokers.length; i++) {
            for (int j = i + 1; j < pokers.length; j++) {
                if (pokers[i] < pokers[j]) {
                    int temp = pokers[i];
                    pokers[i] = pokers[j];
                    pokers[j] = temp;
                }
            }
        }
    }

    public static int getPokerSurfaceValue(int poker) {//牌面值
        // 当扑克值为52时，是小王
        if (poker == 52) {
            return 16;
        }
        // 当扑克值为53时，是大王
        if (poker == 53) {
            return 17;
        }
        // 其它情况下返回相应的值(3,4,5,6,7,8,9,10,11(J),12(Q),13(K),14(A),15(2))
        return poker / 4 + 3;
    }

    public static int getPokerType(int[] pokers) {
        int len = pokers.length;
        // 当牌数量为1时,单牌
        if (len == 1) {
            return PokerType.danpai;
        }
        // 当牌数量为2时,可能是对牌和火箭
        if (len == 2) {
            if (pokers[0] == 53 && pokers[1] == 52) {
                return PokerType.huojian;
            }
            if (getPokerSurfaceValue(pokers[0]) == getPokerSurfaceValue(pokers[1])) {
                return PokerType.duipai;
            }
        }
        // 当牌数为3时,只可能是三张
        if (len == 3) {
            if (getPokerSurfaceValue(pokers[0]) == getPokerSurfaceValue(pokers[1])
                    && getPokerSurfaceValue(pokers[2]) == getPokerSurfaceValue(pokers[1])) {
                return PokerType.sanzhang;
            }
        }
        // 当牌数为4时,可能是三带一或炸弹
        if (len == 4) {
            int firstCount = getPokerCount(pokers, pokers[0]);
            if (firstCount == 3 || getPokerCount(pokers, pokers[1]) == 3) {
                return PokerType.sandaiyi;
            }
            if (firstCount == 4) {
                return PokerType.zhadan;
            }
        }
        // 当牌数为5时，可能是三带一对或顺子
        if (len == 5){
            if (getPokerCount(pokers,pokers[0]) == 3 || getPokerCount(pokers,pokers[4]) == 3){
                return PokerType.sandaiyidui;
            }
            if (shunzi(pokers)){
                return PokerType.danshun;
            }
        }
        // 当牌数大于5时,判断是不是单顺
        if (len >= 5) {
            if (shunzi(pokers)) {
                return PokerType.danshun;
            }

        }
        // 当牌数为6时,判断是不是四带二
        if (len == 6) {
            boolean have4 = false;
            boolean have1 = false;
            for (int i = 0; i < len; i++) {
                if (getPokerCount(pokers, pokers[i]) == 4) {
                    have4 = true;
                }
                if (getPokerCount(pokers, pokers[i]) == 1) {
                    have1 = true;
                }
            }

            if (have4 && have1) {
                return PokerType.sidaier;
            }
        }
        // 当牌数大于等于6时,检测是不是双顺和三顺
        if (len >= 6) {
            // 双顺
            boolean shuangshunflag = true;
            for (int i = 0; i < len; i++) {
                if (getPokerCount(pokers, pokers[i]) != 2) {
                    shuangshunflag = false;
                    break;
                }
            }
            int[] tempShuangshun = new int[len / 2];
            for (int i = 0; i < len / 2; i++) {
                tempShuangshun[i] = pokers[i * 2];
            }
            if (shuangshunflag && shunzi(tempShuangshun)) {//如果三张牌是顺子并且每张牌的数量都是2就是双顺了
                    return PokerType.shuangshun;
            }

            System.out.println("shuangshun:" + shuangshunflag);
            // 三顺
            boolean sanshunflag = true;
            for (int i = 0; i < len; i++) {
                if (getPokerCount(pokers, pokers[i]) != 3) {
                    sanshunflag = false;
                    break;
                }
            }
            int[] tempSanshun = new int[len / 3];
            for (int i = 0; i < len / 3; i++) {
                tempSanshun[i] = pokers[i * 3];
            }
            if (sanshunflag && shunzi(tempSanshun)) {
                return PokerType.sanshun;
            }


        }

        //当牌数等于8时，判断是不是四带两对
        if (len == 8){
            boolean have4 = false;
            int have2 = 0;
            for (int i = 0;i<pokers.length;i++){
                if (getPokerCount(pokers,pokers[i]) == 4){
                    have4 = true;
                }
                if (getPokerCount(pokers,pokers[i]) == 2){
                    have2 ++;
                }
            }
            if (have4 && have2 == 2){
                return PokerType.sidailiangdui;
            }
        }

        // 当牌数大于等于8,且能够被2整除时,判断是不是飞机，带两张或者两对
        if(len >= 8 && len % 2 == 0){
            int have3 = 0;
            int have1 = 0;
            int have2 = 0;
            for (int i = 0; i < pokers.length; i++){
                if (getPokerCount(pokers,pokers[i]) == 3){
                    have3++;
                }
                if (getPokerCount(pokers,pokers[i]) == 1){
                    have1++;
                }
                if (getPokerCount(pokers,pokers[i]) == 2){
                    have2++;
                }
            }
            if (have3 >= 2 && have1 >= 2 && have2 == 0){
                return PokerType.feiji;
            }else if (have3 >= 2 && have2 >= 2 && have1 == 0)
                return PokerType.feijiWithbigWing;
        }
        return PokerType.error;
    }

    //判断是不是顺子
    public static boolean shunzi(int[] pokers) {
        int start = getPokerSurfaceValue(pokers[0]);
        // 顺子中不能包含2,王
        if (start >= 15) {
            return false;
        }
        int next;
        for (int i = 1; i < pokers.length; i++) {
            next = getPokerSurfaceValue(pokers[i]);
            if (start - next != 1) {
                return false;
            }
            start = next;
        }
        return true;
    }

    // 统计一手牌中同值的牌出现的次数
    public static int getPokerCount(int[] pokers, int poker) {
        int count = 0;
        for (int i = 0; i < pokers.length; i++) {
            if (getPokerSurfaceValue(pokers[i]) == getPokerSurfaceValue(poker)) {
                count++;
            }
        }
        return count;
    }

    // 通过给给出的一手牌,来返回它的牌值大小，用于相同牌型的比较
    public static int getPokerTypeValue(int[] pokers, int pokerType) {
        // 这几种类型直接返回第一个值
        if (pokerType == PokerType.danpai || pokerType == PokerType.duipai
                || pokerType == PokerType.danshun || pokerType == PokerType.sanshun
                || pokerType == PokerType.shuangshun
                || pokerType == PokerType.sanzhang || pokerType == PokerType.zhadan) {
            return getPokerSurfaceValue(pokers[0]);
        }
        // 三带一（对）和飞机返回数量为3的牌的最大牌值
        if (pokerType == PokerType.sandaiyi || pokerType == PokerType.sandaiyidui || pokerType == PokerType.feiji) {
            for (int i = 0; i <= pokers.length - 3; i++) {
                if (getPokerSurfaceValue(pokers[i]) == getPokerSurfaceValue(pokers[i + 1])
                        && getPokerSurfaceValue(pokers[i + 1]) == getPokerSurfaceValue(pokers[i + 2])) {
                    return getPokerSurfaceValue(pokers[i]);
                }
            }
        }
        // 四带二（对）返回数量为4的牌值
        if (pokerType == PokerType.sidaier || pokerType == PokerType.sidailiangdui) {
            for (int i = 0; i < pokers.length - 3; i++) {
                if (getPokerSurfaceValue(pokers[i]) == getPokerSurfaceValue(pokers[i + 1])
                        && getPokerSurfaceValue(pokers[i + 1]) == getPokerSurfaceValue(pokers[i + 2])
                        && getPokerSurfaceValue(pokers[i + 2]) == getPokerSurfaceValue(pokers[i + 3])) {
                    return getPokerSurfaceValue(pokers[i]);
                }
            }
        }
        return 0;
    }

    public static boolean compare(Discard current, Discard last) {

        if (current.pokerType == last.pokerType && current.pokers.length == last.pokers.length) {
            return current.cardValue > last.cardValue;//牌型相同且数量相同比较牌值
        } else if (current.pokerType == PokerType.huojian) {//牌型不同若本家是火箭则大
            return true;
        } else if (current.pokerType == PokerType.zhadan) {//牌型不同若本家是炸弹则大
            return true;
        }
        return false;//其他情况默认上家大
    }

    public static int[] outAsetofCard(int pokers[], Player last, Player next) {
        AnalyzePoker analyze = new AnalyzePoker();
        analyze.setPokers(pokers);
        int afterAnalyzeCardArray[] = null;
        
        List<int[]> card_danpai = analyze.getCard_danpai();
        List<int[]> card_duipai = analyze.getCard_duipai();
        List<int[]> card_sanzhang = analyze.getCard_sanzhang();
        List<int[]> card_sanshun = analyze.getCard_sanshun();
        List<int[]> card_shuangshun = analyze.getCard_shuangshun();
        List<int[]> card_danshun = analyze.getCard_danshun();
        int danpaiCount = card_danpai.size();
        int duipaiCount = card_duipai.size();
        int sanzhangCount = card_sanzhang.size();
        int sanshunCount = card_sanshun.size();
        int shuangshunCount = card_shuangshun.size();
        int danshunCount = card_danshun.size();
        
        int willingCard = -1;
        if (sanshunCount > 0){
            willingCard = PokerType.sanshun;
        }else if (shuangshunCount > 0){
            willingCard = PokerType.shuangshun;
        }else if (danshunCount > 0){
            willingCard = PokerType.danshun;
        }else if (sanzhangCount > 0){
            willingCard = PokerType.sanzhang;
        }else if ((next.id == Desk.boss && next.theRestCard == 1) || (duipaiCount > danpaiCount && ((next.id == Desk.boss && next.theRestCard !=2) ||
                (next.id != Desk.boss && last.id != Desk.boss && next.theRestCard != 2) || (next.id != Desk.boss && last.id == Desk.boss)))){
            //如果下家是对手且牌大于两张、下家是队友可以出对牌
            willingCard = PokerType.duipai;
        }else if (danpaiCount == 0 && ((next.id == Desk.boss && next.theRestCard ==2) || (next.id != Desk.boss && last.id != Desk.boss && next.theRestCard == 2))){
            //如果对手的牌只剩两张而且我只剩下对牌没有单牌，要拆对牌，下面另外写
            willingCard = -1;
        }else if (duipaiCount <= danpaiCount){
            willingCard = PokerType.danpai;
        }
        System.out.println("danpai:"+danpaiCount+","+"duipai:"+duipaiCount+","+"Poker399,willingcard"+willingCard);

        switch (willingCard) {
            case PokerType.sanshun:
                System.out.println("打三顺");
                if (sanshunCount > 0) {
                    afterAnalyzeCardArray = card_sanshun.get(0);

                    if (danpaiCount > afterAnalyzeCardArray.length / 3) {//有大于2张的单牌给飞机加翅膀
                        int[] planeWithWingArray = new int[afterAnalyzeCardArray.length / 3 * 4];
                        for (int i = 0; i < afterAnalyzeCardArray.length; i++) {
                            planeWithWingArray[i] = afterAnalyzeCardArray[i];//没翅膀的飞机（三顺）
                        }
                        for (int j = 0; j < afterAnalyzeCardArray.length / 3; j++) {
                            planeWithWingArray[afterAnalyzeCardArray.length + j] = card_danpai.get(j)[0];//加翅膀
                        }
                        return planeWithWingArray;
                    } else if(duipaiCount > afterAnalyzeCardArray.length / 3){//有大于两对的对牌给飞机加翅膀
                        int[] planeWithWingArray = new int[afterAnalyzeCardArray.length / 3 * 5];
                        for (int i = 0; i < afterAnalyzeCardArray.length; i++) {
                            planeWithWingArray[i] = afterAnalyzeCardArray[i];//没翅膀的飞机（三顺）
                        }
                        for (int j = 0; j < afterAnalyzeCardArray.length / 3 * 2; j++) {
                            planeWithWingArray[afterAnalyzeCardArray.length + j] = card_duipai.get(j/2)[j%2];//加翅膀
                        }
                        return planeWithWingArray;
                    } else if (duipaiCount >= 1 && Poker.getPokerSurfaceValue(card_duipai.get(0)[0])!=15 && Poker.getPokerSurfaceValue(card_duipai.get(0)[0])!=14) {
                        //单（对）牌不够两张（对）用一对牌来加翅膀，对2、对A就不用来加翅膀了
                        int[] planeWithWingArray = new int[afterAnalyzeCardArray.length / 3 * 4];
                        for (int i = 0; i < afterAnalyzeCardArray.length; i++) {
                            planeWithWingArray[i] = afterAnalyzeCardArray[i];
                        }
                        for (int j = 0; j < afterAnalyzeCardArray.length / 3; j++){
                            planeWithWingArray[afterAnalyzeCardArray.length + j] = card_duipai.get(0)[j];
                        }
                        return planeWithWingArray;
                    } else
                        return afterAnalyzeCardArray;
                    }
                break;
            case PokerType.shuangshun:
                System.out.println("打双顺");
                card_shuangshun = analyze.getCard_shuangshun();
                System.out.println("shuangshun:" + card_shuangshun.size());
                if (shuangshunCount > 0) {
                    afterAnalyzeCardArray = card_shuangshun.get(0);
                    return afterAnalyzeCardArray;
                }
                break;
            case PokerType.danshun:
                System.out.println("打单顺");
                card_danshun = analyze.getCard_danshun();
                if (danshunCount > 0) {
                    return card_danshun.get(0);
                }
                break;
            case PokerType.sanzhang:
                System.out.println("打三张");
                card_sanzhang = analyze.getCard_sanzhang();
                if (sanzhangCount > 0) {
                    int[] sanzhangArray = card_sanzhang.get(0);
                    if (danpaiCount > 0 && (card_danpai.get(0)[0]/4+3)!=16 && Poker.getPokerSurfaceValue(card_danpai.get(0)[0])!=15) {//单牌不是大小王和2
                        int newSandaiyi[] = new int[] { sanzhangArray[0], sanzhangArray[1], sanzhangArray[2], card_danpai.get(0)[0] };
                        return newSandaiyi;
                    } else if (duipaiCount > 0 && Poker.getPokerSurfaceValue(card_duipai.get(0)[0])!=15 && Poker.getPokerSurfaceValue(card_duipai.get(0)[0])!=14){//对牌不是对2，对A
                        int newSandaiyidui[] = new int[] { sanzhangArray[0], sanzhangArray[1], sanzhangArray[2], card_duipai.get(0)[0] ,card_duipai.get(0)[1]};
                        return newSandaiyidui;
                    } else {
                        return sanzhangArray;
                    }
                }
                break;
            case PokerType.duipai:
                System.out.println("打对牌");
                if (duipaiCount > 0) {
                    return card_duipai.get(0);
                }
                break;
            case PokerType.danpai:
                System.out.println("打单牌");
                if (danpaiCount > 0 ){
                    if ((next.id == Desk.boss && next.theRestCard ==1) || (next.id != Desk.boss && last.id != Desk.boss && next.theRestCard == 1 && last.theRestCard == 1)) {
                        return card_danpai.get(danpaiCount - 1);
                    }else
                        return card_danpai.get(0);
                }
                break;
            case -1://下家是对手且只剩两张牌
                System.out.println("没有单牌，拆对牌");
                if (duipaiCount > 0){
                    int[] chaidui = new int[]{card_duipai.get(0)[0]};
                    return chaidui;
                }
        }

        List<int[]> card_zhadan = analyze.getCard_zhadan();
        if (card_zhadan.size() > 0) {
            return card_zhadan.get(0);
        }
        return new int[] { pokers[0] };
    }

    // 出牌智能
    // 判断一手牌是上家还是下家，是地主还是农民，然后判断要不要出牌
    public static int[] outTheRightCard(Discard card, int pokers[], Player last, Player next) {
        //下家是对手只剩两张和一张牌就一定要出牌
        if (next.id == Desk.boss && (next.theRestCard == 2 || next.theRestCard == 1)){
            return findBiggerCard(card, pokers, true);
        }

        // 判断我该不该要牌
        if ((Desk.boss != last.id && Desk.boss != next.id) || card.playerID == Desk.boss) {
            // 我是boss或者是boss出的牌
            // 判断他的剩余牌数和他出的牌有多长
            boolean must = false;
            int pokerLength = Desk.players[card.playerID].pokers.length;
            if (pokerLength <= 5) {
                must = true;
            }
            int cardLength = card.pokers.length;
            if (cardLength >= 7){
                must = true;
            }
            return findBiggerCard(card, pokers, must);

        } else {
            // 我不是地主，牌也不是地主的牌，是自己家的牌
            if (card.playerID == next.id) {//自家人是下家
                // 不要牌，让他继续出
                return null;
            } else if (card.playerID == last.id){//自家人是上家
                // 牌的大小如果大于一定值我不要，否则我小小的出一下牌
                int pokerLength = Desk.players[card.playerID].pokers.length;
                if (card.cardValue < 10 && card.pokerType == PokerType.duipai) {//对10以下的可以出
                    if (pokerLength > 4) {
                        return findBiggerCard(card,pokers,false);
                    }
                } else if (card.cardValue < 12 && card.pokerType == PokerType.danpai){//Q以下可以出
                    if (pokerLength > 4){
                        return findBiggerCard(card,pokers,false);
                    }
                }
            }
        }
        return null;
    }

    // 从pokes数组中找到比card大的一手牌
    public static int[] findBiggerCard(Discard card, int pokers[], boolean must) {
        try {
            // 获取card的信息，牌值，牌型
            int cardValue = card.cardValue;
            int cardType = card.pokerType;
            int cardLength = card.pokers.length;
            AnalyzePoker analyz = new AnalyzePoker();
            analyz.setPokers(pokers);
            List<int[]> temp;
            int size = 0;
            int[] afterAnalyzeCardArray = null;
            // 根据适当牌型选取适当牌
            switch (cardType) {
                case PokerType.danpai:
                    temp = analyz.getCard_danpai();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        afterAnalyzeCardArray = temp.get(i);
                        int v = Poker.getPokerTypeValue(afterAnalyzeCardArray,PokerType.danpai);
                        if (v > cardValue) {
                            return afterAnalyzeCardArray;
                        }
                    }
                    //如果单牌中没有，就从除了火箭和炸弹之后的最大一个
                    int pokersNum = 0;
                    System.out.println("Poker676,"+afterAnalyzeCardArray);
                    System.out.println("Poker677,count2: "+analyz.getCount2());
                    if (analyz.getCountWang() != 2 && analyz.getCount2() != 4 && Poker.getPokerSurfaceValue(pokers[0]) > cardValue) {
                        return new int[] { pokers[0] };
                    }else if (analyz.getCount2() == 4){
                        pokersNum += 4;
                        for (int i = analyz.getCountPokers().length - 1; i >=0; i--){
                            if (analyz.getCountPokers()[i] != 4 && (i+3) > cardValue){
                                return new int[] { pokers[pokersNum] };
                            }else if (analyz.getCountPokers()[i] == 4)
                                pokersNum += 4;
                        }
                    }
                    break;
                case PokerType.duipai:
                    temp = analyz.getCard_duipai();
                    size = temp.size();

                    for (int i = 0; i < size; i++) {
                        afterAnalyzeCardArray = temp.get(i);
                        int v = Poker.getPokerTypeValue(afterAnalyzeCardArray,PokerType.duipai);
                        if (v > cardValue) {
                            return afterAnalyzeCardArray;
                        }
                    }
                    // 如果对子中没有，则需要检查双顺
                    temp = analyz.getCard_shuangshun();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        afterAnalyzeCardArray = temp.get(i);
                        for (int j = afterAnalyzeCardArray.length-1; j >= 0; j--) {
                            int v = Poker.getPokerSurfaceValue(afterAnalyzeCardArray[j]);
                            if (v > cardValue) {
                                return new int[] { afterAnalyzeCardArray[j], afterAnalyzeCardArray[j - 1] };
                            }
                        }
                    }
                    // 如果双顺中没有，则需要检查三张
                    temp = analyz.getCard_sanzhang();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        afterAnalyzeCardArray = temp.get(i);
                        int v = Poker.getPokerTypeValue(afterAnalyzeCardArray,PokerType.sanzhang);
                        if (v > cardValue) {
                            return new int[] { afterAnalyzeCardArray[0], afterAnalyzeCardArray[1] };
                        }
                    }
                    break;
                case PokerType.sanzhang:
                    temp = analyz.getCard_sanzhang();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        afterAnalyzeCardArray = temp.get(i);
                        int v = Poker.getPokerTypeValue(afterAnalyzeCardArray,PokerType.sanzhang);
                        if (v > cardValue) {
                            return afterAnalyzeCardArray;
                        }
                    }
                    break;
                case PokerType.sandaiyi:
                    boolean haveSDY = false;
                    int[] sandaiyi = new int[4];
                    temp = analyz.getCard_sanzhang();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        afterAnalyzeCardArray = temp.get(i);
                        int v = Poker.getPokerTypeValue(afterAnalyzeCardArray,PokerType.sandaiyi);
                        if (v > cardValue) {
                            for (int j = 0; j < afterAnalyzeCardArray.length; j++) {
                                sandaiyi[j] = afterAnalyzeCardArray[j];
                                haveSDY = true;
                            }
                        }
                    }
                    // 没有三张满足条件
                    if (!haveSDY) {
                        break;
                    }
                    // 再找一张组合成三带一
                    temp = analyz.getCard_danpai();
                    size = temp.size();
                    if (size > 0) {//有单牌
                        sandaiyi[3] = temp.get(0)[0];
                    } else {//没有就在单顺里找
                        temp = analyz.getCard_danshun();
                        size = temp.size();
                        for (int i = 0; i < size; i++) {
                            int[] danshun = temp.get(i);
                            if (danshun.length >= 6) {
                                sandaiyi[3] = danshun[danshun.length - 1];
                            }
                        }
                    }
                    // 如果在单牌和顺子中都找不到，就从一手牌中随便找一个最小的
                    if (sandaiyi[3] == 0) {
                        for (int i = pokers.length - 1; i >= 0; i--) {
                            if (Poker.getPokerSurfaceValue(pokers[i]) != Poker.getPokerSurfaceValue(sandaiyi[0])) {
                                sandaiyi[3] = pokers[i];
                            }
                        }
                    }
                    if (sandaiyi[3] != 0) {
                        return sandaiyi;
                    }
                    break;
                case PokerType.danshun:
                    temp = analyz.getCard_danshun();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        int[] danshun = temp.get(i);
                        if (danshun.length == cardLength) {
                            if (cardValue < Poker.getPokerSurfaceValue(danshun[0])) {
                                return danshun;
                            }
                        }
                    }
                    int[] newDanshun = new int[cardLength];
                    int newDSlen = 0;
                    for (int i = 0; i < size; i++) {
                        int[] danshun = temp.get(i);
                        if (danshun.length > cardLength) {
                            if (danshun.length - cardLength >= 3) {
                                if (must) {//如果一定要出牌，那就算拆得超过三个散牌也要出
                                    if (cardValue >= Poker.getPokerSurfaceValue(danshun[0])) {
                                        continue;//没找到比上家大的牌继续找
                                    }else if (cardValue < Poker.getPokerSurfaceValue(danshun[0])){
                                        for (int j = danshun.length-1; j>=0; j--){
                                            if (Poker.getPokerSurfaceValue(danshun[j]) > cardValue && newDSlen == cardLength) {//只要比他大1就够了
                                                for (int k = 0; k < newDanshun.length; k++) {//danshun[0]存放最大的牌
                                                    newDanshun[k] = danshun[j++];
                                                }
                                                return newDanshun;
                                            }else
                                                newDSlen++;
                                        }
                                    }
                                }else
                                    break;
                            }else {//只是一两张散牌的话可以拆
                                if (cardValue >= Poker.getPokerSurfaceValue(danshun[0])){
                                    continue;
                                }else {
                                    for (int j = danshun.length-1; j>=0; j--){
                                        if (Poker.getPokerSurfaceValue(danshun[j]) > cardValue && newDSlen == cardLength) {
                                            for (int k = 0; k < newDanshun.length; k++) {
                                                newDanshun[k] = danshun[j++];
                                            }
                                            return newDanshun;
                                        }else
                                            newDSlen++;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case PokerType.shuangshun:
                    temp = analyz.getCard_shuangshun();
                    size = temp.size();

                    for (int i = 0; i < size; i++) {
                        int[] shuangshun = temp.get(i);
                        if (shuangshun.length == cardLength) {
                            if (Poker.getPokerSurfaceValue(shuangshun[0]) > cardValue) {
                                return shuangshun;
                            }
                        }
                    }
                    int[] newShuangshun = new int[cardLength];
                    int newSSlen = 0;
                    for (int i = 0; i < size; i++){
                        int[] shuangshun = temp.get(i);
                        if (shuangshun.length > cardLength){
                            for (int j = shuangshun.length-1 ; j >= 0 ;j++){
                                int z = j-1;//从数组的最后面往前面找，找到的符合的对牌只是一个，还有一个要往后挪一位，从这一位开始给新的双顺赋值
                                if (Poker.getPokerSurfaceValue(shuangshun[j]) > cardValue && newSSlen == cardLength){
                                    for (int k = 0 ; k < newShuangshun.length ; k++){
                                        newShuangshun[k] = shuangshun[z++];
                                    }
                                }else
                                    newSSlen++;
                            }
                        }
                    }

                    break;
                case PokerType.sanshun:
                    temp = analyz.getCard_sanshun();
                    size = temp.size();
                    for (int i = 0; i < size; i++) {
                        int[] sanshun = temp.get(i);
                        if (cardLength == sanshun.length) {
                            if (Poker.getPokerSurfaceValue(sanshun[0]) > cardValue) {
                                return sanshun;
                            }
                        }else if (cardLength < sanshun.length){
                            int[] newSanshun = new int[cardLength];
                            if (Poker.getPokerSurfaceValue(sanshun[0]) > cardValue) {
                                for (int j = 0 ; j < newSanshun.length ; j++){
                                    newSanshun[j] = sanshun[j];
                                }
                            }
                        }
                    }
                    break;
                case PokerType.feiji:
                    temp = analyz.getCard_sanshun();
                    size = temp.size();
                    int[] newFeiji = new int[cardLength];
                    boolean haveSanshun = false;
                    for (int i = 0; i < size ; i++){
                        afterAnalyzeCardArray = temp.get(i);
                        if (afterAnalyzeCardArray.length == (cardLength - 2)){
                            if (Poker.getPokerSurfaceValue(afterAnalyzeCardArray[0]) > cardValue){
                                haveSanshun = true;
                                for (int j = 0;j < newFeiji.length-2 ; j++){
                                    newFeiji[j] = afterAnalyzeCardArray[j];
                                }
                            }
                        }
                    }
                    if (!haveSanshun){//没有三顺就走了
                        break;
                    }
                    temp = analyz.getCard_danpai();
                    size = temp.size();
                    if (size > 2){//要有两张以上单牌
                        for (int i = 0; i < 2 ;i++){
                            afterAnalyzeCardArray = temp.get(i);
                            newFeiji[cardLength-2+i] = afterAnalyzeCardArray[i];
                        }
                    }else if (analyz.getCard_duipai().size() > 1) {//没有就如果有一对以上对牌
                        newFeiji[cardLength - 2] = analyz.getCard_duipai().get(0)[0];
                        newFeiji[cardLength - 1] = analyz.getCard_duipai().get(0)[1];
                    }
                    if (newFeiji[cardLength -2] != 0){
                        return newFeiji;
                    }
                    break;
                case PokerType.zhadan:
                    temp = analyz.getCard_zhadan();
                    size = temp.size();
                    int zd[] = null;
                    for (int i = 0; i < size; i++) {
                        zd = temp.get(i);
                        if (cardValue < Poker.getPokerSurfaceValue(zd[0])) {
                            return zd;
                        }
                    }

                    break;
                case PokerType.huojian:
                    return null;

            }
            // 如果一定要出牌就而没有对应的牌型就考虑要不要出炸弹了
            if (must) {
                temp = analyz.getCard_zhadan();
                size = temp.size();
                if (size > 0) {
                    return temp.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
