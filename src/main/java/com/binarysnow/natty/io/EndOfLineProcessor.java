package com.binarysnow.natty.io;

import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;

public class EndOfLineProcessor implements ByteProcessor {

    private static final byte HEX_0A = 0x0a;
    private static final byte HEX_0D = 0x0d;

    private long maxFrameSize;
    private long count;
    private boolean previousIs0D;

    private final static boolean CONTINUE_TO_NEXT_BYTE = true;
    private final static boolean FINISHED = false;

    public void reset(final long maxFrameSize) {
        previousIs0D = false;
        this.maxFrameSize = maxFrameSize;
        this.count = 0;
    }

    @Override
    public boolean process(byte value) throws Exception {
        count ++;

        if (count > maxFrameSize) throw new TooLongFrameException("Frame size " + maxFrameSize + " exceeded with " + count + " bytes.");

        if (!previousIs0D) {
            if (value == HEX_0D) {
                previousIs0D = true;
            }
            return CONTINUE_TO_NEXT_BYTE;
        } else {
            if (value == HEX_0A) {
                return FINISHED;
            }

            if (value != HEX_0D) {
                previousIs0D = false;
            }

            return CONTINUE_TO_NEXT_BYTE;
        }
    }
}
