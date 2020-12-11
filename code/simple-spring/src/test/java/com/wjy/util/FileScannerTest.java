package com.wjy.util;

import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Set;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class FileScannerTest {

    @Test
    public void testPath(){
        System.out.println(FileScanner.class.getResource("/").getPath());
    }

    @Test
    public void testScan() throws URISyntaxException {
        final FileScanner scanner = new FileScanner();
        final Set<Class<?>> search = scanner.search("com.wjy");
        System.out.println(search);
    }
}
