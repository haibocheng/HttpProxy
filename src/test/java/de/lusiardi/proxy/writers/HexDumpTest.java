package de.lusiardi.proxy.writers;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.StringContains.*;
import static org.hamcrest.core.IsEqual.*;

/**
 *
 * @author shing19m
 */
public class HexDumpTest {

    @Test
    public void test_hexDump_0() {
        HexDump hd = new HexDump();
        String result = hd.write("".getBytes(), "");
        assertThat(result, equalTo("\n"));

        result = hd.write("".getBytes(), ">>>>");
        assertThat(result, equalTo(">>>>\n"));

    }

    @Test
    public void test_hexDump_1() {
        HexDump hd = new HexDump();
        String result = hd.write("test for some ".getBytes(), "");
        assertThat(result, equalTo("00000000  74 65 73 74 20 66 6f 72  20 73 6f 6d 65 20       |test for some   \n"));

        result = hd.write("test for some ".getBytes(), ">>>>");
        assertThat(result, equalTo(">>>>00000000  74 65 73 74 20 66 6f 72  20 73 6f 6d 65 20       |test for some   \n"));

    }

    @Test
    public void test_hexDump_2() {
        HexDump hd = new HexDump();
        String result = hd.write("0123456789ABCDEF01234567".getBytes(), "");
        assertThat(result, containsString("00000000  30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46  |0123456789ABCDEF"));
        assertThat(result, containsString("00000010  30 31 32 33 34 35 36 37                           |01234567"));

        result = hd.write("0123456789ABCDEF01234567".getBytes(), ">>>>");
        assertThat(result, containsString(">>>>00000000  30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46  |0123456789ABCDEF"));
        assertThat(result, containsString(">>>>00000010  30 31 32 33 34 35 36 37                           |01234567"));
    }

    @Test
    public void test_hexDump_3() {
        HexDump hd = new HexDump();
        final byte[] bytes = "0123456789ABCDEF01234567".getBytes();
        bytes[3] = 3;
        bytes[4] = (byte) 0xaa;

        String result = hd.write(bytes, "");
        assertThat(result, containsString("00000000  30 31 32 03 aa 35 36 37  38 39 41 42 43 44 45 46  |012..56789ABCDEF"));
        assertThat(result, containsString("00000010  30 31 32 33 34 35 36 37                           |01234567"));

        result = hd.write(bytes, ">>>>");
        assertThat(result, containsString(">>>>00000000  30 31 32 03 aa 35 36 37  38 39 41 42 43 44 45 46  |012..56789ABCDEF"));
        assertThat(result, containsString(">>>>00000010  30 31 32 33 34 35 36 37                           |01234567"));
    }

    @Test
    public void test_hexDump_4() {
        HexDump hd = new HexDump();
        final byte[] bytes = "0123456789ABCDEF".getBytes();

        String result = hd.write(bytes, "");
        assertThat(result, containsString("00000000  30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46  |0123456789ABCDEF"));

        result = hd.write(bytes, ">>>>");
        assertThat(result, containsString(">>>>00000000  30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46  |0123456789ABCDEF"));
    }
}
