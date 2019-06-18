package com.example.zephaniah.doudz;

import java.util.ArrayList;
import java.util.List;

public class AnalyzePoker {
    private int[] pokers;//一手牌
    private int[] countPokers = new int[12];//除了王和2之外的牌的数量
    private int count2;//2的数量
    private int countWang;//王的数量
    private List<int[]> card_zhadan = new ArrayList<int[]>(3);//存放炸弹
    private List<int[]> card_sanshun = new ArrayList<int[]>(3);//存放三顺
    private List<int[]> card_shuangshun = new ArrayList<int[]>(3);//存放双顺
    private List<int[]> card_sanzhang = new ArrayList<int[]>(3);//存放三张
    private List<int[]> card_danshun = new ArrayList<int[]>(3);//存放单顺
    private List<int[]> card_duipai = new ArrayList<int[]>(3);//存放对牌
    private List<int[]> card_danpai = new ArrayList<int[]>(5);//存放单牌

    public int[] getCountPokers() {
        return countPokers;
    }

    public int getCount2() {
        return count2;
    }

    public int getCountWang() {
        return countWang;
    }

    public List<int[]> getCard_zhadan() {
        return card_zhadan;
    }

    public List<int[]> getCard_sanshun() {
        return card_sanshun;
    }

    public List<int[]> getCard_shuangshun() {
        return card_shuangshun;
    }

    public List<int[]> getCard_sanzhang() {
        return card_sanzhang;
    }

    public List<int[]> getCard_danshun() {
        return card_danshun;
    }

    public List<int[]> getCard_duipai() {
        return card_duipai;
    }

    public List<int[]> getCard_danpai() {
        return card_danpai;
    }

    private void init() {
        for (int i = 0; i < countPokers.length; i++) {
            countPokers[i] = 0;
        }
        count2 = 0;
        countWang = 0;
        card_zhadan.clear();
        card_sanshun.clear();
        card_shuangshun.clear();
        card_sanzhang.clear();
        card_danshun.clear();
        card_duipai.clear();
        card_danpai.clear();
    }

    public void setPokers(int[] pokers) {
        Poker.sort(pokers);
        this.pokers = pokers;
        try {
            this.analyze();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean testAnalyze(int pokers[]) {
        try {
            init();
            for (int i = 0; i < pokers.length; i++) {//分析每一张牌的牌数
                int v = Poker.getPokerSurfaceValue(pokers[i]);
                if (v == 16 || v == 17) {
                    countWang++;
                } else if (v == 15) {
                    count2++;
                } else {
                    countPokers[v - 3]++;
                }
            }
            // 判断有没有三顺
            int start = -1;
            int end = -1;
            for (int i = 0; i <= countPokers.length - 1; i++) {
                if (countPokers[i] == 3) {
                    if (start == -1) {
                        start = i;//如果有三张就开始标记
                    } else {
                        end = i;//结束标记
                    }
                } else {
                    if (end != -1 && start != -1) {//开始结束标记都改变了证明有三顺
                        int dur = end - start + 1;
                        int[] sanshun = new int[dur * 3];
                        int m = 0;
                        for (int j = 0; j < pokers.length; j++) {
                            int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;//获取扑克值并对应标记
                            if (v >= start && v <= end) {
                                sanshun[m++] = pokers[j];//能对应就加入三顺数组
                            }
                        }
                        if (m == dur * 3) {
                            System.out.println("有三顺被放进数组啦！！！");
                        } else {
                            System.out.println("三顺放进数组失败！！！");
                        }
                        card_sanshun.add(sanshun);
                        for (int s = start; s <= end; s++) {
                            countPokers[s] = -1;//已经被放进三顺数组的就删掉
                        }
                        start = end = -1;
                        continue;//继续看看还有没有三顺
                    } else {//没有三顺或者只有三张但不连续，重置然后继续往下找
                        start = end = -1;
                    }
                }
            }
            // 双顺
            int sstart = -1;
            int send = -1;
            for (int i = 0; i < countPokers.length; i++) {
                if (countPokers[i] == 2) {
                    if (sstart == -1) {
                        sstart = i;
                    } else {
                        send = i;
                    }
                } else {
                    if (sstart != -1 && send != -1) {
                        int dur = send - sstart + 1;
                        if (dur < 3) {//双顺必须要有三对以上
                            sstart = send = -1;
                            continue;//继续重新找
                        } else {//有三对以上
                            int shuangshun[] = new int[dur * 2];
                            int m = 0;
                            for (int j = 0; j < pokers.length; j++) {
                                int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                                if (v >= sstart && v <= send) {
                                    shuangshun[m++] = pokers[j];
                                }
                            }
                            card_shuangshun.add(shuangshun);
                            for (int s = sstart; s <= send; s++) {
                                countPokers[s] = -1;
                            }
                            sstart = send = -1;
                            continue;
                        }
                    } else {
                        sstart = send = -1;
                    }
                }
            }
            // danshun
            int dstart = -1;
            int dend = -1;
            for (int i = 0; i < countPokers.length; i++) {
                if (countPokers[i] >= 1) {
                    if (dstart == -1) {
                        dstart = i;
                    } else {
                        dend = i;
                    }
                } else {
                    if (dstart != -1 && dend != -1) {
                        int dur = dend - dstart + 1;
                        if (dur >= 5) {
                            int m = 0;
                            int[] danshun = new int[dur];
                            for (int j = 0; j < pokers.length; j++) {
                                int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                                if (v == dend) {
                                    danshun[m++] = pokers[j];
                                    countPokers[dend]--;
                                    dend--;
                                }
                                if (dend == dstart - 1) {
                                    break;
                                }
                            }

                            card_danshun.add(danshun);
                        }
                        dstart = dend = -1;
                    } else {
                        dstart = dend = -1;
                    }
                }
            }
            // 三张
            for (int i = 0; i < countPokers.length; i++) {
                if (countPokers[i] == 3) {
                    countPokers[i] = -1;
                    int[] sanzhang = new int[3];
                    int m = 0;
                    for (int j = 0; j < pokers.length; j++) {
                        int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                        if (v == i) {//限制只放找到的那三张进去
                            sanzhang[m++] = pokers[j];
                        }
                    }
                    card_sanzhang.add(sanzhang);
                }
            }
            // 对牌
            for (int i = 0; i < countPokers.length; i++) {
                if (countPokers[i] == 2) {
                    countPokers[i] = -1;
                    int[] duipai = new int[2];
                    for (int j = 0; j < pokers.length; j++) {
                        int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                        if (v == i) {
                            duipai[0] = pokers[j];
                            duipai[1] = pokers[j + 1];
                            card_duipai.add(duipai);
                            break;
                        }
                    }
                }
            }
            // danpai
            for (int i = 0; i < countPokers.length; i++) {
                if (countPokers[i] == 1) {
                    countPokers[i] = -1;
                    for (int j = 0; j < pokers.length; j++) {
                        int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                        if (v == i) {
                            card_danpai.add(new int[] { pokers[j] });
                            countPokers[i] = -1;
                            break;
                        }

                    }
                }
            }
            // 炸弹
            for (int i = 0; i < countPokers.length; i++) {
                if (countPokers[i] == 4) {
                    card_zhadan.add(new int[] { i * 4 + 3, i * 4 + 2, i * 4 + 1, i * 4 });//存的是牌的顺序值，不是牌面值
                    countPokers[i] = -1;
                }
            }

            switch (count2) {
                case 4:
                    card_zhadan.add(new int[] { pokers[countWang],//假如有王炸则从pokes[3]开始存
                            pokers[countWang + 1], pokers[countWang + 2],
                            pokers[countWang + 3] });
                    break;
                case 3:
                    card_sanzhang.add(new int[] { pokers[countWang],
                            pokers[countWang + 1], pokers[countWang + 2] });
                    break;
                case 2:
                    card_duipai.add(new int[] { pokers[countWang],
                            pokers[countWang + 1] });
                    break;
                case 1:
                    card_danpai.add(new int[] { pokers[countWang] });
                    break;
            }

            if (countWang == 1) {
                card_danpai.add(new int[] { pokers[0] });
            } else if (countWang == 2) {
                card_zhadan.add(new int[] { pokers[0], pokers[1] });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // 分析几大主要牌型
    private void analyze() {
        // 初始化牌型容器
        init();
        // 分析王，2，普通牌的数量
        for (int i = 0; i < pokers.length; i++) {
            int v = Poker.getPokerSurfaceValue(pokers[i]);
            if (v == 16 || v == 17) {
                countWang++;
            } else if (v == 15) {
                count2++;
            } else {
                countPokers[v - 3]++;
            }
        }
        // 分析三顺牌型
        int start = -1;
        int end = -1;
        for (int i = 0; i <= countPokers.length - 1; i++) {
            if (countPokers[i] == 3) {
                if (start == -1) {
                    start = i;
                } else {
                    end = i;
                }
            } else {
                if (end != -1 && start != -1) {
                    int dur = end - start + 1;
                    int[] sanshun = new int[dur * 3];
                    int m = 0;
                    for (int j = 0; j < pokers.length; j++) {
                        int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                        if (v >= start && v <= end) {
                            sanshun[m] = pokers[j];
                            m++;
                            System.out.println("m=="+m);
                        }
                }
                    if (m == dur * 3) {//最后一次循环还加了1
                        System.out.println("m="+m+"sanshun is over!!!");
                    } else {
                        System.out.println("m="+m+"sanshun error!!!");
                    }
                    card_sanshun.add(sanshun);
                    for (int s = start; s <= end; s++) {
                        countPokers[s] = -1;
                    }
                    start = end = -1;
                    continue;
                } else {
                    start = end = -1;
                }
            }
        }
        // 分析双顺牌型
        int sstart = -1;
        int send = -1;
        for (int i = 0; i < countPokers.length; i++) {
            if (countPokers[i] == 2) {
                if (sstart == -1) {
                    sstart = i;
                } else {
                    send = i;
                }
            } else {
                if (sstart != -1 && send != -1) {
                    int dur = send - sstart + 1;
                    if (dur < 3) {
                        sstart = send = -1;
                        continue;
                    } else {
                        int shuangshun[] = new int[dur * 2];
                        int m = 0;
                        for (int j = 0; j < pokers.length; j++) {
                            int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                            if (v >= sstart && v <= send) {
                                shuangshun[m++] = pokers[j];
                            }
                        }
                        card_shuangshun.add(shuangshun);
                        for (int s = sstart; s <= send; s++) {
                            countPokers[s] = -1;
                        }
                        sstart = send = -1;
                        continue;
                    }
                } else {
                    sstart = send = -1;
                }
            }
        }
        // 分析单顺牌型
        int dstart = -1;
        int dend = -1;
        for (int i = 0; i < countPokers.length; i++) {
            if (countPokers[i] >= 1) {
                if (dstart == -1) {
                    dstart = i;
                } else {
                    dend = i;
                }
            } else {
                if (dstart != -1 && dend != -1) {
                    int dur = dend - dstart + 1;
                    if (dur >= 5) {
                        int m = 0;
                        int[] danshun = new int[dur];
                        for (int j = 0; j < pokers.length; j++) {
                            int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                            if (v == dend) {
                                danshun[m++] = pokers[j];
                                countPokers[dend]--;
                                dend--;
                            }
                            if (dend == dstart - 1) {
                                break;
                            }
                        }

                        card_danshun.add(danshun);
                    }
                    dstart = dend = -1;
                } else {
                    dstart = dend = -1;
                }
            }
        }
        // 分析三张牌型
        for (int i = 0; i < countPokers.length; i++) {
            if (countPokers[i] == 3) {
                countPokers[i] = -1;
                int[] sanzhang = new int[3];
                int m = 0;
                for (int j = 0; j < pokers.length; j++) {
                    int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                    if (v == i) {
                        sanzhang[m++] = pokers[j];
                    }
                }
                card_sanzhang.add(sanzhang);
            }
        }
        // 分析对牌
        for (int i = 0; i < countPokers.length; i++) {
            if (countPokers[i] == 2) {
                int[] duipai = new int[2];
                for (int j = 0; j < pokers.length; j++) {
                    int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                    if (v == i) {
                        duipai[0] = pokers[j];
                        duipai[1] = pokers[j + 1];
                        card_duipai.add(duipai);
                        break;
                    }
                }
                countPokers[i] = -1;
            }
        }
        // 分析单牌
        for (int i = 0; i < countPokers.length; i++) {
            if (countPokers[i] == 1) {
                for (int j = 0; j < pokers.length; j++) {
                    int v = Poker.getPokerSurfaceValue(pokers[j]) - 3;
                    if (v == i) {
                        card_danpai.add(new int[] { pokers[j] });
                        countPokers[i] = -1;
                        break;
                    }

                }
            }
        }

        // 根据2的数量进行分析
        switch (count2) {
            case 4:
                card_zhadan.add(new int[] { pokers[countWang],
                        pokers[countWang + 1], pokers[countWang + 2],
                        pokers[countWang + 3] });
                break;
            case 3:
                card_sanzhang.add(new int[] { pokers[countWang],
                        pokers[countWang + 1], pokers[countWang + 2] });
                break;
            case 2:
                card_duipai.add(new int[] { pokers[countWang],
                        pokers[countWang + 1] });
                break;
            case 1:
                card_danpai.add(new int[] { pokers[countWang] });
                break;
        }
        // 分析炸弹
        for (int i = 0; i < countPokers.length - 1; i++) {
            if (countPokers[i] == 4) {
                card_zhadan.add(new int[] { i * 4 + 3, i * 4 + 2,
                        i * 4 + 1, i * 4 });
                countPokers[i] = -1;
            }
        }
        // 分析火箭
        if (countWang == 1) {
            card_danpai.add(new int[] { pokers[0] });
        } else if (countWang == 2) {
            card_zhadan.add(new int[] { pokers[0], pokers[1] });
        }
    }
}
