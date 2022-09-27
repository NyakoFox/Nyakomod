package gay.nyako.nyakomod.test;

import me.stupidcat.abcparser.ABCParser;
import me.stupidcat.abcparser.ABCSong;
import me.stupidcat.abcparser.struct.SongChord;
import me.stupidcat.abcparser.struct.SongComponent;
import me.stupidcat.abcparser.struct.SongNote;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;

public class SongPlayer {
    MinecraftClient client;

    public static String input = """
                        d/4 d/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        c/4 c/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        _B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        [d/4D/4D4F4] [d/4D/4] [d'/D/] [D/4a3/4] D/ [^g/D/] [=g/D/] [f/D/] [d/4D/4] [f/4D/4] [g/4D/4]\s
                        [c/4C/4C4E4] [c/4C/4] [d'/C/] [C/4a3/4] C/ [^g/C/] [=g/C/] [f/C/] [d/4C/4] [f/4C/4] [g/4C/4]\s
                        [=B/4B,/4G,4B,4] [B/4B,/4] [d'/B,/] [B,/4a3/4] B,/ [^g/B,/] [=g/B,/] [f/B,/] [d/4B,/4] [f/4B,/4] [g/4B,/4]\s
                        [_B/4_B,/4B,7/4D7/4] [B/4B,/4] [d'/B,/] [B,/4a3/4] B,/ [^g/C/C2E2] [=g/C/] [f/C/] [d/4C/4] [f/4C/4] [g/4C/4]\s
                        [d/4d'/4D/4D/4F/4A/4d/4] [d/4d'/4D/4D/4F/4A/4d/4] [d'/d''/D/d/f/a/d'/] [D/4a3/4a'3/4A3/4d3/4f3/4a3/4] D/ [^g/^g'/D/^G/c/f/g/] [=g/=g'/D/=G/c/d/g/] [f/f'/D/F/^G/c/f/] [d/4d'/4D/4D/4F/4G/4d/4] [f/4f'/4D/4F/4G/4c/4f/4] [g/4g'/4D/4=G/4c/4d/4g/4]\s
                        [c/4c'/4C/4C/4E/4G/4c/4] [c/4c'/4C/4C/4E/4G/4c/4] [d'/d''/C/d/f/a/d'/] [C/4a3/4a'3/4A3/4d3/4f3/4a3/4] C/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [d/4d'/4C/4D/4F/4G/4d/4] [f/4f'/4C/4F/4G/4c/4f/4] [g/4g'/4C/4=G/4c/4d/4g/4]\s
                        [=B/4b/4=B,/4B,/4D/4F/4B/4] [B/4b/4B,/4B,/4D/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/B,/^G/c/f/g/] [=g/=g'/B,/=G/c/d/g/] [f/f'/B,/F/^G/c/f/] [d/4d'/4B,/4D/4F/4G/4d/4] [f/4f'/4B,/4F/4G/4c/4f/4] [g/4g'/4B,/4=G/4c/4d/4g/4]\s
                        [_B/4_b/4_B,/4D/4F/4F/4B/4] [B/4b/4B,/4D/4F/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [d/4d'/4C/4D/4F/4G/4d/4] [f/4f'/4C/4F/4G/4c/4f/4] [g/4g'/4C/4=G/4c/4d/4g/4]\s
                        [d/4d'/4D/4D/4F/4A/4d/4] [d/4d'/4D/4D/4F/4A/4d/4] [d'/d''/D/d/f/a/d'/] [D/4a3/4a'3/4A3/4d3/4f3/4a3/4] D/ [^g/^g'/D/^G/c/f/g/] [=g/=g'/D/=G/c/d/g/] [f/f'/D/F/^G/c/f/] [d/4d'/4D/4D/4F/4G/4d/4] [f/4f'/4D/4F/4G/4c/4f/4] [g/4g'/4D/4=G/4c/4d/4g/4]\s
                        [c/4c'/4C/4C/4E/4G/4c/4] [c/4c'/4C/4C/4E/4G/4c/4] [d'/d''/C/d/f/a/d'/] [C/4a3/4a'3/4A3/4d3/4f3/4a3/4] C/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [d/4d'/4C/4D/4F/4G/4d/4] [f/4f'/4C/4F/4G/4c/4f/4] [g/4g'/4C/4=G/4c/4d/4g/4]\s
                        [=B/4=b/4=B,/4B,/4D/4F/4B/4] [B/4b/4B,/4B,/4D/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/B,/^G/c/f/g/] [=g/=g'/B,/=G/c/d/g/] [f/f'/B,/F/^G/c/f/] [d/4d'/4B,/4D/4F/4G/4d/4] [f/4f'/4B,/4F/4G/4c/4f/4] [g/4g'/4B,/4=G/4c/4d/4g/4]\s
                        [_B/4_b/4_B,/4D/4F/4F/4B/4] [B/4b/4B,/4D/4F/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [_B,,/16d/4d'/4C/4D/4F/4] C,/16 D,/16 E,/16 [F,/16f/4f'/4C/4F/4G/4] G,/16 A,/16 B,/16 [C/16g/4g'/4C/4=G/4c/4] D/16 E/16 F/16\s
                        [D/4F/D7/F7/] D/4 [F/4D/] [z/4F/] D/4 [D/F/] [D/F/] [D/D/] [D/D3/4] D/4 [D/4d/4D/F/D/] D/4\s
                        [C/4F/4C7/E7/] [C/4F/4] [F/4C/] [z/4F/] C/4 [C/G/] [C/^G/] [=G/4C/] F/4 [D/4C/] F/4 [C/4G3/4] [C/4c/4C/E/] C/4\s
                        [=B,/4F/G,7/B,7/] B,/4 [F/4B,/] [z/4F/] B,/4 [B,/G/] [B,/^G/] [B,/A/] [B,/c/] [B,/4A3/4] [B,/4=B/4G,/B,/] B,/4\s
                        [_B,/4d/B,7/4D7/4] B,/4 [B,/d/] [B,/4d/4] [A/4B,/] d/4 [C/C2E2c2] C/ C/ C/4 C/4 C/4\s
                        [d/4D/4F/A/D7/F7/] [d/4D/4] [F/4A/4d'/D/] [z/4F/A/] [D/4a3/4] [D/F/A/] [^g/D/F/A/] [=g/D/D/=G/] [f/D/DG] [d/4D/4] [f/4D/4D/F/] [g/4D/4]\s
                        [c/4C/4F/A/C7/E7/] [c/4C/4] [F/4A/4d'/C/] [z/4F/A/] [C/4a3/4] [C/F/A/] [^g/C/E/G/] [=g/C/F/A/] [f/C/A/d/] [d/4C/4F/4A/4] [f/4C/4E/4G/4C/E/] [g/4C/4]\s
                        [B/4D/4=B,/4A/d/] [B/4G/4B,/4] [C/4d'/B,/F/A/] G/4 [B,/4B,/4E/G/a3/4] [G/4B,/] [A,/4D/F/] [G/4^g/B,/] [D/4G/c/] [F/4=g/B,/] [C/4F/A/] [F/4f/B,/] [B,/4D/G/] [d/4F/4B,/4] [f/4A,/4B,/4C/F/] [g/4F/4B,/4]\s
                        [_B/4_B,/4D/B,7/4D7/4] [B/4B,/4] [E/4d'/B,/] [z/4F/] [B,/4a3/4] [B,/A/] [^g/C/C2E2A2c2] [=g/C/] [f/C/] [d/4C/4] [f/4C/4] [g/4C/4]\s
                        [D/4D4F4] D/4 D/ D/4 D/ [z/4D/] [f/4f/4] [d/4d/4D/] [f/4f/4] [g/4g/4D/] [^g/4g/4] [=g/4g/4D/4] [f/4f/4D/4] [d/4d/4D/4]\s
                        [^g/8g/8C/4C4E4] [=g/8g/8] [f/8f/8C/4] [d/8d/8] [f/f/C/] [C/4g2g2] C/ C/ C/ C/ [C/4^g/g/] C/4 [a/4a/4C/4]\s
                        [=B,/4c'/c'/G,4B,4] B,/4 [a/4a/4B,/] [g/4g/4] [=g/4g/4B,/4] [f/4f/4B,/] [d/4d/4] [e/4e/4B,/] [z/4f/f/] [z/4B,/] [z/4g/g/] [z/4B,/] [z/4a/a/] B,/4 [B,/4c'/c'/] B,/4\s
                        [^C/4^c'/c'/C7/4F7/4] C/4 [^g/g/C/] [g/4g/4C/4] [=g/4g/4C/] [f/4f/4] [g/4g/4^D/D2G2] z/4 D/ D/ D/4 D/4 D/4\s
                        [=D/4f/F/D4F4] D/4 [g/G/D/] [D/4a/A/] [z/4D/] [z/4d'/f'/d/f/] [z/4D/] [z/4=c'e'ce] D/ [z/4D/] [z/4bd'Bd] D/4 D/4 D/4\s
                        [=C/4c'e'ceC4E4] C/4 C/ [C/4d'f'df] C/ [z/4C/] [z/4e'g'eg] C/ [z/4C/] [z/4c'e'ce] C/4 C/4 C/4\s
                        [B,/4d'2a'2d2a2G,4B,4] B,/4 B,/ B,/4 B,/ [z/4B,/] [a'/4a/4] [^g'/4^g/4B,/] [=g'/4=g/4] [^f'/4^f/4B,/] [=f'/4=f/4] [e'/4e/4B,/4] [^d'/4^d/4B,/4] [=d'/4=d/4B,/4]\s
                        [^C/4f2^c'2C2F2F2^c2] C/4 C/ C/4 C/ [z/4^D/] [z/4g2^d'2D2G2G2^d2] D/ D/ D/4 D/4 D/4\s
                        [=D/4D4F4] D/4 D/ D/4 D/ [z/4D/] [f/4f/4] [=d/4d/4D/] [f/4f/4] [g/4g/4D/] [^g/4g/4] [=g/4g/4D/4] [f/4f/4D/4] [d/4d/4D/4]\s
                        [^g/8g/8=C/4C4E4] [=g/8g/8] [f/8f/8C/4] [d/8d/8] [f/f/C/] [C/4g2g2] C/ C/ C/ C/ [C/4^g/g/] C/4 [a/4a/4C/4]\s
                        [B,/4=c'/c'/G,4B,4] B,/4 [a/4a/4B,/] [g/4g/4] [=g/4g/4B,/4] [f/4f/4B,/] [d/4d/4] [e/4e/4B,/] [z/4f/f/] [z/4B,/] [z/4g/g/] [z/4B,/] [z/4a/a/] B,/4 [B,/4c'/c'/] B,/4\s
                        [^C/4^c'/c'/C7/4F7/4] C/4 [^g/g/C/] [g/4g/4C/4] [=g/4g/4C/] [f/4f/4] [g/4g/4^D/D2G2] z/4 D/ D/ D/4 D/4 D/4\s
                        [=D/4f/F/D4F4] D/4 [g/G/D/] [D/4a/A/] [z/4D/] [z/4=d'/f'/d/f/] [z/4D/] [z/4=c'e'=ce] D/ [z/4D/] [z/4bd'Bd] D/4 D/4 D/4\s
                        [=C/4c'e'ceC4E4] C/4 C/ [C/4d'f'df] C/ [z/4C/] [z/4e'g'eg] C/ [z/4C/] [z/4c'e'ce] C/4 C/4 C/4\s
                        [B,/4d'2a'2d2a2G,4B,4] B,/4 B,/ B,/4 B,/ [z/4B,/] [a'/4a/4] [^g'/4^g/4B,/] [=g'/4=g/4] [^f'/4^f/4B,/] [=f'/4=f/4] [e'/4e/4B,/4] [^d'/4^d/4B,/4] [=d'/4=d/4B,/4]\s
                        [^C/4f2^c'2C2F2F2^c2] C/4 C/ C/4 C/ [z/4^D/] [z/4g2^d'2=D2G2G2^d2] ^D/ D/ D/4 D/4 D/4\s
                        [_B,/=D/B,3] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/F] [B,/D/]\s
                        [=C/E/E2] [C/E/] [C/4E/4] [C/E/] [z/4C/E/] [z/4D2] [C/E/] [C/4E/4] [C/E/] [C/E/]\s
                        [G,/=B,/F8] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/]\s
                        [G,/B,/] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/]\s
                        [_B,/D/B,3] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/F] [B,/D/]\s
                        [C/E/E2] [C/E/] [C/4E/4] [C/E/] [z/4C/E/] [z/4D2] [C/E/] [C/4E/4] [C/E/] [C/E/]\s
                        [D/F/D8] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/]\s
                        [D/4B,/D/B,3] D/4 [B,/D/=d/] [B,/4D/4A3/4] [B,/D/] [B,/D/^G/] [B,/D/=G/] [B,/4D/4F/] [z/4B,/D/F] D/4 [F/4B,/D/] G/4\s
                        [C/4C/E/E2] C/4 [C/E/d/] [C/4E/4A3/4] [C/E/] [z/4C/E/^G/] [z/4D2] [C/E/=G/] [C/4E/4F/] [z/4C/E/] D/4 [F/4C/E/] G/4\s
                        [=B,/4G,/B,/F8] B,/4 [G,/B,/d/] [G,/4B,/4A3/4] [G,/B,/] [G,/B,/^G/] [G,/B,/=G/] [G,/4B,/4F/] [z/4G,/B,/] D/4 [F/4G,/B,/] G/4\s
                        [_B,/4G,/=B,/] [=d'/4_B,/4] [f'/4G,/=B,/d/] d'/4 [G,/4B,/4g'/A3/4] [z/4G,/B,/] [z/4f'/] [z/4G,/B,/^G/] d'/4 [=c'/G,/B,/=G/] [G,/4B,/4a/F/] [z/4G,/B,/] [g/4D/4] [a/4F/4G,/B,/] [c'/4G/4]\s
                        [D/4_B,/D/B,3] D/4 [B,/D/d/] [B,/4D/4A3/4] [B,/D/] [B,/D/^G/] [B,/D/=G/] [B,/4D/4F/] [z/4B,/D/F] D/4 [F/4B,/D/] G/4\s
                        [C/4C/E/E2] C/4 [C/E/d/] [C/4E/4A3/4] [C/E/] [z/4C/E/^G/] [z/4D2] [C/E/=G/] [C/4E/4F/] [z/4C/E/] D/4 [F/4C/E/] G/4\s
                        [=B,/4D/F/D8] B,/4 [d'/f'/D/F/d/] [D/4F/4c'3/4e'3/4A3/4] [D/F/] [g/c'/D/F/^G/] [c'/e'/D/F/=G/] [D/4F/4a/d'/F/] [z/4D/F/] [e/4g/4D/4] [f/4a/4F/4D/F/] [g/4c'/4G/4]\s
                        [_B,/4D/F/] B,/4 [d'/f'/D/F/d/] [D/4F/4c'3/4e'3/4A3/4] [D/F/] [g/c'/D/F/^G/] [c'/e'/D/F/=G/] [D/4F/4a/d'/F/] [z/4D/F/] [e/4g/4D/4] [f/4a/4F/4D/F/] [g/4c'/4G/4]\s
                        [B,/D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [^C/F/] [C/F/] [C/F/] [C/4F/4] [C/F/]\s
                        [=C/E/] [C/E/] [C/4E/4] [C/E/] [=B,/^D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/]\s
                        [_B,/=D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/4F/4] [D/4F/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/4F/4] [D/4F/4]\s
                        [B,/D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [^C/F/] [C/F/] [C/F/] [C/4F/4] [C/F/]\s
                        [=C/E/] [C/E/] [C/4E/4] [C/E/] [=B,/^D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/]\s
                        [_B,/=D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [d/4D/F/] d/4 [d'/D/F/] [D/4F/4a3/4] [D/F/] [^g/D/F/] [=g/D/F/] [f/D/F/] [d/4D/4F/4] [f/4D/4F/4] [g/4D/4F/4]\s
                        [d/4D/F/] d/4 [d'/D/F/] [D/4F/4a3/4] [D/F/] [^g/D/F/] [=g/D/F/] [f/D/F/] [d/4D/4F/4] [f/4D/4F/4] [g/4D/4F/4]\s
                        B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        =c/4 c/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        d/4 d/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        d/4 d/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        c/4 c/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
            """;

    ABCSong song;
    List<SongComponent> songComponents;
    boolean playing = false;
    long initial = 0;

    public SongPlayer() {
        client = MinecraftClient.getInstance();
    }

    public void play() {
        playing = !playing;
        initial = System.currentTimeMillis();

        song = new ABCSong()
                .setBpm(240)
                .setMeter("4/4")
                .setDefaultNoteLength("1/4");

        var parser = new ABCParser(song);

        songComponents = parser.parse(input);
    }

    public void tick() {
        var ms = System.currentTimeMillis() - initial;

        var passedComponents = songComponents.stream().filter(comp -> {
            return ms > comp.getStart();
        }).toList();

        for (var comp : passedComponents) {
            if (comp instanceof SongChord) {
                for (var note : ((SongChord) comp).notes) {
                    var pitch = ((float) note.getNote()) / 12f * 0.5f;
                    client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP, 1, pitch);
                }
            } else if (comp instanceof SongNote) {
                var pitch = ((float) ((SongNote) comp).getNote()) / 12f * 0.5f;
                client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP, 1, pitch);
            }
            songComponents.remove(comp);
        }
        client.player.sendMessage(Text.literal(String.valueOf(ms)), true);
    }

    public boolean isPlaying() {
        return playing;
    }
}
