package com.example.zephaniah.doudz;

import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class GameSound {
    static Map<String,Integer> poolMap = new HashMap<>();

    public static Map<String,Integer> getPoolMap(OpeningVideoActivity ga,SoundPool soundPool){
        poolMap.put("A",soundPool.load(ga,R.raw.dan1,1));
        poolMap.put("2",soundPool.load(ga,R.raw.dan2,1));
        poolMap.put("3",soundPool.load(ga,R.raw.dan3,1));
        poolMap.put("4",soundPool.load(ga,R.raw.dan4,1));
        poolMap.put("5",soundPool.load(ga,R.raw.dan5,1));
        poolMap.put("6",soundPool.load(ga,R.raw.dan6,1));
        poolMap.put("7",soundPool.load(ga,R.raw.dan7,1));
        poolMap.put("8",soundPool.load(ga,R.raw.dan8,1));
        poolMap.put("9",soundPool.load(ga,R.raw.dan9,1));
        poolMap.put("10",soundPool.load(ga,R.raw.dan10,1));
        poolMap.put("11",soundPool.load(ga,R.raw.dan11,1));
        poolMap.put("12",soundPool.load(ga,R.raw.dan12,1));
        poolMap.put("13",soundPool.load(ga,R.raw.dan13,1));
        poolMap.put("duiA",soundPool.load(ga,R.raw.dui1,1));
        poolMap.put("dui2",soundPool.load(ga,R.raw.dui2,1));
        poolMap.put("dui3",soundPool.load(ga,R.raw.dui3,1));
        poolMap.put("dui4",soundPool.load(ga,R.raw.dui4,1));
        poolMap.put("dui5",soundPool.load(ga,R.raw.dui5,1));
        poolMap.put("dui6",soundPool.load(ga,R.raw.dui6,1));
        poolMap.put("dui7",soundPool.load(ga,R.raw.dui7,1));
        poolMap.put("dui8",soundPool.load(ga,R.raw.dui8,1));
        poolMap.put("dui9",soundPool.load(ga,R.raw.dui9,1));
        poolMap.put("dui10",soundPool.load(ga,R.raw.dui10,1));
        poolMap.put("dui11",soundPool.load(ga,R.raw.dui11,1));
        poolMap.put("dui12",soundPool.load(ga,R.raw.dui12,1));
        poolMap.put("dui13",soundPool.load(ga,R.raw.dui13,1));
        poolMap.put("tupleA",soundPool.load(ga,R.raw.tuple1,1));
        poolMap.put("tuple2",soundPool.load(ga,R.raw.tuple2,1));
        poolMap.put("tuple3",soundPool.load(ga,R.raw.tuple3,1));
        poolMap.put("tuple4",soundPool.load(ga,R.raw.tuple4,1));
        poolMap.put("tuple5",soundPool.load(ga,R.raw.tuple5,1));
        poolMap.put("tuple6",soundPool.load(ga,R.raw.tuple6,1));
        poolMap.put("tuple7",soundPool.load(ga,R.raw.tuple7,1));
        poolMap.put("tuple8",soundPool.load(ga,R.raw.tuple8,1));
        poolMap.put("tuple9",soundPool.load(ga,R.raw.tuple9,1));
        poolMap.put("tuple10",soundPool.load(ga,R.raw.tuple10,1));
        poolMap.put("tuple11",soundPool.load(ga,R.raw.tuple11,1));
        poolMap.put("tuple12",soundPool.load(ga,R.raw.tuple12,1));
        poolMap.put("tuple13",soundPool.load(ga,R.raw.tuple13,1));
        poolMap.put("xiaowang",soundPool.load(ga,R.raw.dan14,1));
        poolMap.put("dawang",soundPool.load(ga,R.raw.dan15,1));
        poolMap.put("shunzi",soundPool.load(ga,R.raw.shunzi,1));
        poolMap.put("liandui",soundPool.load(ga,R.raw.liandui,1));
        poolMap.put("feiji",soundPool.load(ga,R.raw.feiji,1));
        poolMap.put("sandaiyi",soundPool.load(ga,R.raw.sandaiyi,1));
        poolMap.put("sandaiyidui",soundPool.load(ga,R.raw.sandaiyidui,1));
        poolMap.put("sidaier",soundPool.load(ga,R.raw.sidaier,1));
        poolMap.put("sidailiangdui",soundPool.load(ga,R.raw.sidailiangdui,1));
        poolMap.put("zhadan",soundPool.load(ga,R.raw.zhadan,1));
        poolMap.put("wangzha",soundPool.load(ga,R.raw.wangzha,1));
        poolMap.put("buyao",soundPool.load(ga,R.raw.buyao1,1));
        poolMap.put("dani",soundPool.load(ga,R.raw.dani1,1));
        poolMap.put("yizhangpai",soundPool.load(ga,R.raw.baojing1,1));
        poolMap.put("liangzhangpai",soundPool.load(ga,R.raw.baojing2,1));

        //female声音
        poolMap.put("nA",soundPool.load(ga,R.raw.n1,1));
        poolMap.put("n2",soundPool.load(ga,R.raw.n2,1));
        poolMap.put("n3",soundPool.load(ga,R.raw.n3,1));
        poolMap.put("n4",soundPool.load(ga,R.raw.n4,1));
        poolMap.put("n5",soundPool.load(ga,R.raw.n5,1));
        poolMap.put("n6",soundPool.load(ga,R.raw.n6,1));
        poolMap.put("n7",soundPool.load(ga,R.raw.n7,1));
        poolMap.put("n8",soundPool.load(ga,R.raw.n8,1));
        poolMap.put("n9",soundPool.load(ga,R.raw.n9,1));
        poolMap.put("n10",soundPool.load(ga,R.raw.n10,1));
        poolMap.put("n11",soundPool.load(ga,R.raw.n11,1));
        poolMap.put("n12",soundPool.load(ga,R.raw.n12,1));
        poolMap.put("n13",soundPool.load(ga,R.raw.n13,1));
        poolMap.put("nduiA",soundPool.load(ga,R.raw.ndui1,1));
        poolMap.put("ndui2",soundPool.load(ga,R.raw.ndui2,1));
        poolMap.put("ndui3",soundPool.load(ga,R.raw.ndui3,1));
        poolMap.put("ndui4",soundPool.load(ga,R.raw.ndui4,1));
        poolMap.put("ndui5",soundPool.load(ga,R.raw.ndui5,1));
        poolMap.put("ndui6",soundPool.load(ga,R.raw.ndui6,1));
        poolMap.put("ndui7",soundPool.load(ga,R.raw.ndui7,1));
        poolMap.put("ndui8",soundPool.load(ga,R.raw.ndui8,1));
        poolMap.put("ndui9",soundPool.load(ga,R.raw.ndui9,1));
        poolMap.put("ndui10",soundPool.load(ga,R.raw.ndui10,1));
        poolMap.put("ndui11",soundPool.load(ga,R.raw.ndui11,1));
        poolMap.put("ndui12",soundPool.load(ga,R.raw.ndui12,1));
        poolMap.put("ndui13",soundPool.load(ga,R.raw.ndui13,1));
        poolMap.put("ntupleA",soundPool.load(ga,R.raw.ntuple1,1));
        poolMap.put("ntuple2",soundPool.load(ga,R.raw.ntuple2,1));
        poolMap.put("ntuple3",soundPool.load(ga,R.raw.ntuple3,1));
        poolMap.put("ntuple4",soundPool.load(ga,R.raw.ntuple4,1));
        poolMap.put("ntuple5",soundPool.load(ga,R.raw.ntuple5,1));
        poolMap.put("ntuple6",soundPool.load(ga,R.raw.ntuple6,1));
        poolMap.put("ntuple7",soundPool.load(ga,R.raw.ntuple7,1));
        poolMap.put("ntuple8",soundPool.load(ga,R.raw.ntuple8,1));
        poolMap.put("ntuple9",soundPool.load(ga,R.raw.ntuple9,1));
        poolMap.put("ntuple10",soundPool.load(ga,R.raw.ntuple10,1));
        poolMap.put("ntuple11",soundPool.load(ga,R.raw.ntuple11,1));
        poolMap.put("ntuple12",soundPool.load(ga,R.raw.ntuple12,1));
        poolMap.put("ntuple13",soundPool.load(ga,R.raw.ntuple13,1));
        poolMap.put("nxiaowang",soundPool.load(ga,R.raw.n14,1));
        poolMap.put("ndawang",soundPool.load(ga,R.raw.n15,1));
        poolMap.put("nshunzi",soundPool.load(ga,R.raw.nshunzi,1));
        poolMap.put("nliandui",soundPool.load(ga,R.raw.nliandui,1));
        poolMap.put("nfeiji",soundPool.load(ga,R.raw.nfeiji,1));
        poolMap.put("nsandaiyi",soundPool.load(ga,R.raw.nsandaiyi,1));
        poolMap.put("nsandaiyidui",soundPool.load(ga,R.raw.nsandaiyidui,1));
        poolMap.put("nsidaier",soundPool.load(ga,R.raw.nsidaier,1));
        poolMap.put("nsidailiangdui",soundPool.load(ga,R.raw.nsidailiangdui,1));
        poolMap.put("nzhadan",soundPool.load(ga,R.raw.nzhadan,1));
        poolMap.put("nwangzha",soundPool.load(ga,R.raw.nwangzha,1));
        poolMap.put("nbuyao1",soundPool.load(ga,R.raw.nbuyao1,1));
        poolMap.put("nbuyao2",soundPool.load(ga,R.raw.nbuyao4,1));
        poolMap.put("ndani",soundPool.load(ga,R.raw.ndani1,1));
        poolMap.put("nyizhangpai",soundPool.load(ga,R.raw.nbaojing1,1));
        poolMap.put("nliangzhangpai",soundPool.load(ga,R.raw.nbaojing2,1));

        //牌型音效
        poolMap.put("flower",soundPool.load(ga,R.raw.special_flower,1));
        poolMap.put("bomb",soundPool.load(ga,R.raw.special_bomb,1));
        poolMap.put("bomb_wangzha",soundPool.load(ga,R.raw.special_bomb_wangzha,1));
        poolMap.put("multiply",soundPool.load(ga,R.raw.special_multiply,1));
        poolMap.put("plane",soundPool.load(ga,R.raw.special_plane,1));
        poolMap.put("star",soundPool.load(ga,R.raw.special_star,1));


        return poolMap;
    }
}
