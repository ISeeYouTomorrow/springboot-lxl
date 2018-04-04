package netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/13
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder{

    private final int frameLength;
    public FixedLengthFrameDecoder(int frameLength){
        if(frameLength <= 0){
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= frameLength){
            ByteBuf buf = in.readBytes(frameLength);
            out.add(buf);
        }
    }
}
