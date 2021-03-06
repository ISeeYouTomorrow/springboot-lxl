package netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/13
 */
public class AbsIntegerEncoderTest {
    @Test
    public void testEncoded(){
        ByteBuf buf = Unpooled.buffer();

        for (int i = 1; i <10 ; i++) {
            buf.writeInt(i*-1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        boolean result = channel.writeOutbound(buf);
        System.out.println("line 22 result :"+result);
        Assert.assertTrue(channel.finish());

        for(int i=1;i<10;i++){
            System.out.println((int)channel.readOutbound());
        }
        Object o = channel.readOutbound();
        System.out.println("o is null: "+o);
    }
}
