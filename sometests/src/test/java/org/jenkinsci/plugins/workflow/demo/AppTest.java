package org.jenkinsci.plugins.workflow.demo;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URL;

/**
 * Integration test for webapp.
 */
public class AppTest extends Assert {


    @Test
    @Category(SmokeTest.class)
    public void smoke() throws Exception {
        testApp(20*1000);
    }

    @Test
    @Category(AcceptanceTest.class)
    public void acceptance() throws Exception {
        testApp(60*1000);
    }


    public void testApp(int duration) throws Exception {
        URL app = getSUT();
        String contents = IOUtils.toString(app.openStream());
        assertTrue(contents.contains("Hello Jenkins!"));

        // this is supposed to be an integration test,
        // let's take some time. We want this to be longer than the build for sure.
        Thread.sleep(duration);
    }

    private URL getSUT() throws Exception {
        String url = System.getProperty("url");
        assertTrue("Subject under test should be passed in via -Durl=...", url!=null);
        return new URL(url);
    }
}
