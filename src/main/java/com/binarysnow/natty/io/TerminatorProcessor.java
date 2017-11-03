package com.binarysnow.natty.io;

import io.netty.util.ByteProcessor;

public final class TerminatorProcessor implements ByteProcessor {
    private final static byte A = 0x0a;
    private final static byte D = 0x0d;

    private boolean foundFirst = false;

    @Override
    public final boolean process(final byte value) throws Exception {
        final boolean result;

        if (!foundFirst) {
            foundFirst = value == A;
            result = false;
        } else {
            if (value == D) {
                result = true;
            } else if (value == A) {
                foundFirst = true;
                result = false;
            } else {
                foundFirst = false;
                result = false;
            }
        }

        return result;
    }
}
