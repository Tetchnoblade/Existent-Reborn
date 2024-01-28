package zyx.existent.module.modules.misc;

import org.apache.commons.lang3.RandomStringUtils;
import zyx.existent.event.EventTarget;
import zyx.existent.event.events.EventUpdate;
import zyx.existent.module.Category;
import zyx.existent.module.Module;
import zyx.existent.module.data.Options;
import zyx.existent.module.data.Setting;
import zyx.existent.utils.timer.Timer;

import java.io.UnsupportedEncodingException;

public class Spammer extends Module {
    public static String MESSAGE = "MESSAGE";
    private String MODE = "MODE";
    private String OTHER = "OTHER";
    private String DELAY = "DELAY";
    private String ANNI = "ANNI";

    private final Timer timer = new Timer();
    public int var1;

    public Spammer(String name, String desc, int keybind, Category category) {
        super(name, desc, keybind, category);

        settings.put(MODE, new Setting<>(MODE, new Options("Spam Mode", "Custom", new String[]{"Custom", "Other"}), "Spam method"));
        settings.put(OTHER, new Setting<>(OTHER, new Options("Other Mode", "Random", new String[]{"Client", "Random", "Obfsucator", "Nasty1", "Noriaki1", "Nancy" ,"SiroQ"}), "Spam method"));
        settings.put(MESSAGE, new Setting<>(MESSAGE, "Test", "SpammerText"));
        settings.put(DELAY, new Setting<>(DELAY, 2.4, "SpamDelay.", 0.1, 0.1, 20.0));
        settings.put(ANNI, new Setting<>(ANNI, false, ""));
    }

    @Override
    public void onEnable() {
        var1 = 0;
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
        String othermode = ((Options) settings.get(OTHER).getValue()).getSelected();
        double delay = ((Number) settings.get(DELAY).getValue()).doubleValue();
        String[] msg = null;

        switch (currentmode) {
            case "Custom":
                msg = new String[]{settings.get(MESSAGE).getValue() + " <" + RandomStringUtils.randomNumeric(5) + ">"};
                break;
            case "Other":
                switch (othermode) {
                    case "Client":
                        msg = new String[]{
                                "今すぐ貧しいアトピーに寄付",
                                "https://sellix.io/Japanmanse"
                        };
                        break;
                    case "Random":
                        String str1 = RandomStringUtils.randomAlphabetic(20);
                        msg = new String[]{str1};
                        break;
                    case "Obfsucator":
                        try {
                            String[] c = {"縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺�", "縺ｾ", "縺ｿ", "繧�", "繧�", "繧�", "縺ｪ", "縺ｫ", "縺ｬ", "縺ｭ", "縺ｮ", "縺ｯ", "縺ｲ", "縺ｵ", "縺ｸ", "縺ｻ", "繧�", "繧�", "繧�", "繧�", "繧�", "繧�", "繧�", "繧�"};
                            int cl = c.length;
                            String k = c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)] + c[(int) Math.floor(Math.random() * cl)];
                            k = new String(k.getBytes("UTF-8"), "Shift-JIS");
                            msg = new String[]{k};
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Nasty1":
                        msg = new String[]{
                                "ここ、ここ、ここ、ここ、ここ、ここ、ここ、ここ",
                                "指をさせばすぐそこに",
                                "ここ、ここ、ここ、ここ、ここ、ここ、ここ、ここ",
                                "下北沢の一軒家よ",
                                "はえー、すっごい大きい、悔い改めて悔い改めて",
                                "後輩が驚いた、自慢の自宅さ",
                                "あー、いいっすねー、焼いていこう焼いていこう",
                                "後輩が頷いた、自慢の屋上さ",
                                "劣情入りアイスティー、後輩が揺らめく",
                                "おっ、大丈夫か大丈夫か",
                                "愛情入りアイスティー、後輩を堕とすよ",
                                "お前のことが好きだったんだよ！"};
                        break;
                    case "Noriaki1":
                        msg = new String[]{
                                "Yo! オレがSo! ノリアキ マジ手のつけられない悪ガキ",
                                "クラスのアイドルあの娘もオレのこのflowにメロメロ",
                                "マイク一本で今日上京 これでも最強のRapper志望",
                                "もう戻れないぜGo Go オレの叫びを聴かせよう",
                                "Yo! ニコタマへようこそ 今日のBBQ超ウマそう",
                                "オレがMCノリアキ お前等マジでオレに会えてlucky",
                                "YAMAGATAから来たbaby face エミネム・ジブラも全部FAKE",
                                "もう戻れないぜGo Go オレの叫びを聴かせよう",
                                "Say! you've gotta save me and my heart",
                                "オレのマシンが夜の地を這う",
                                "墾田永年私財法 まさに釈迦に説法 金の延べ棒",
                                "オレの採点は減点法 魔の言霊でアントンも死亡",
                                "もう届かないぜGo Go オレの走りを見てろよ",
                                "Hey! そこのHip Hop Guy! オレのアッスにキッスするかい？",
                                "グラビアアイドルあの娘もオレのこの刺青にレロレロ",
                                "フック船長の三重奏 もうギリギリのカウパーがこぼれそう",
                                "もう止まらないぜGo Go おれのマグナム噴かせよう",
                                "Yeah, Come on, Let's Go!",
                                "ガリガリノリアキガリガリノリアキ",
                                "You are fucker and さくらんぼfucker",
                                "Yeah, Come on, Let's Go!",
                                "ガリガリノリアキガリガリノリアキ",
                                "You are fucker and さくらんぼfucker",
                                "Yeah, Come on, Let's Go!",
                                "ガリガリノリアキガリガリノリアキ",
                                "You are fucker and さくらんぼfucker",
                                "Hey, 一生チェリーボーイ",
                                "めがねめがねめがねめがね",
                                "Yo! Fuck me Yo!",
                                "Yeah, Come on, Let's Go!",
                                "ガリガリノリアキガリガリノリアキ",
                                "You are fucker and さくらんぼfucker",
                                "Yeah, Come on, Let's Go!",
                                "ガリガリノリアキガリガリノリアキ",
                                "You are fucker and さくらんぼfucker",
                                "Hey, 一生チェリーボーイ",
                                "めがねめがねめがねめがね",
                                "Yo! Fuck me Yo!",
                        };
                        break;
                    case "Nancy":
                        msg = new String[]{
                                "お願い神様! ムニャムニャムニャ",
                                "あのヒトのユメが台無しになりますように",
                                "お願い！そしたら",
                                "切ないムネに風が吹いて・・・",
                                "ユメがユメで無くなる前に",
                                "ムニャムニャムニャ・・・",
                                "ところで",
                                "ナンシーシンナー吸った",
                                "ドロドロの頭の中",
                                "ナンシー空を飛んだ",
                                "両手ひろげて・・・",
                                "ナンシーシンナー吸った",
                                "本当は死ぬ気じゃなかった",
                                "どぉ神様？　世界中の人達が",
                                "アカペラでF-1のテーマ曲を唱うの",
                                "ねぇ*そしたら",
                                "街中がサーキットで",
                                "電柱はもう無いの",
                                "そういえば",
                                "あのヒトは言った",
                                "世界一のバカヤローナンシー",
                                "小さな声で言った",
                                "ずっと前から好きだった",
                                "ナンシーシンナー吸った",
                                "世界中が傾き始めて",
                                "ナンシー空を飛んだ",
                                "本当は死ぬ気じゃなかった",
                                "アタシもシンナー吸った",
                                "Let'go新世界見えた",
                        };
                        break;
                    case "SiroQ":
                        msg = new String[]{
                                "SiroQ！SiroQ！SiroQ！しろきゅぅぅうううわぁああああああああああああああああああああああああん！！！",
                                "あぁああああ...ああ..あっあっー！あぁああああああ！！！SiroQ！SiroQ！しろきゅぅううぁわぁああああ！！！",
                                "あぁクンカクンカ！クンカクンカ！スーハ―スーハ―！スーハ―スーハ―！いい匂いだなぁ...くんくん",
                                "んはぁっ！SiroQたんのSiro色の髪をクンカクンカしたいお！クンカクンカ！あぁあ！！",
                                "間違えた！モフモフしたいお！",
                                "モフモフ！モフモフ！銀髪モフモフ！カリカリモフモフ...きゅんきゅんきゅい！！",
                                "Hack作ってる時のSiroQたんかわいっかたよぅ！！",
                                "あぁぁああ...あああ...あっあぁああああ！！ふぁぁあああんっ！！",
                                "GokuakuBotNet嬉し...いやぁああああああ！！！にゃあああああああおん！！ぎゃああああああああ！！",
                                "ぐあああああああああああ！！！BotNetなんで現実じゃない！！！！あ...HackもDiscordもよく考えたら...",
                                "S i r o Q ち ゃ ん は 現 実 じ ゃ な い ？にゃあああああああああああああん！！うぉあああああああああ！！",
                                "そんなぁああああああ！！いやぁぁぁああああああああ！！はぁあああああん！！しろきゅあぁああああ！！",
                                "この！ちきしょー！やめてやる！！現実なんかやめ...て...え！？見...てる？アイコンのSiroQちゃんが俺をみてる？",
                                "アイコンのSiroQちゃんが僕を見てるぞ！SiroQちゃんが僕を見てるぞ！ヘッダーのSiroQちゃんが僕を見てるぞ！！",
                                "DiscordのSiroQちゃんが僕に話かけてるぞ！！！よかった...世の中まだまだ捨てたモンじゃないんだねっ！",
                                "いやっほぉおおおおおおお！！！僕にはSiroQちゃんがいる！！やったよねこもQ！！ひとりでできるもん！！！",
                                "あ、アイコンのSiroQちゃああああああああああああああん！！いやぁあああああああああああああああ！！！！",
                                "あっあんああっああんシロ様ぁあ！！シ、シロキュー！！しろきゅあぁああああああ！！！うんちぃぁぁあああ！！",
                                "ううっうぅうう！！俺の思いよSiroQへ届け！！DiscordのSiroQへ届け！",
                                

                        };
                        break;
                }
                break;
        }

        if (this.timer.delay((float) (delay * 1050.0D))) {
            if (this.var1 >= msg.length) {
                this.var1 = 0;
            }
            mc.thePlayer.sendChatMessage((Boolean) settings.get(ANNI).getValue() ? "!" + msg[this.var1] : msg[this.var1]);
            this.var1++;
            this.timer.reset();
        }
    }
}
