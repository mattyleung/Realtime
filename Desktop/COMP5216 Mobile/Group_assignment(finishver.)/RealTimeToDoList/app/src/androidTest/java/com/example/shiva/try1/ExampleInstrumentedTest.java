package com.example.shiva.try1;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented dashboardDummy, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /**
     * Use app context.
     *
     * @throws Exception the exception
     */
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under dashboardDummy.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.shiva.try1", appContext.getPackageName());
    }
}
