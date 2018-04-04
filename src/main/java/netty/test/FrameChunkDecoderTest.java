package netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/14
 */
public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded(){
        ByteBuf buf = Unpooled.buffer();
        for(int i=0;i<10;i++){
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
        Assert.assertTrue(channel.writeInbound(input.readBytes(2)));
        try {
            channel.writeInbound(input.readBytes(4)); //5
            Assert.fail();  //6
        } catch (TooLongFrameException e) {
            // expected
            e.printStackTrace();
        }
        Assert.assertTrue(channel.writeInbound(input.readBytes(3)));  //7

        Assert.assertTrue(channel.finish());  //8

        ByteBuf read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(2), read); //9
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();

        buf.release();
    }



}

class FrameChunkDecoder extends ByteToMessageDecoder{

    private final int MAXSZIE ;

    public FrameChunkDecoder(int maxsize) {
        this.MAXSZIE = maxsize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() > MAXSZIE){
            in.clear();
            throw  new TooLongFrameException();
        }

        ByteBuf buf = in.readBytes(in.readableBytes());
        out.add(buf);
    }
}
