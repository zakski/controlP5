package com.szadowsz.controlP5.behaviour.synch;

/**
 * Java Test Class for CPlugSpec
 *
 * @author Zakski : 28/09/2015.
 * @note necessary for field plug tests since scala classes use accessors in the jvm.
 */
class TestJavaObject {

    public boolean testBool = false;
    public int testInt = 0;
    public long testLong = 0L;
    public float testFloat = 0.0f;
    public double testDouble = 0.0;
    public char testChar = 'a';
    public String testString = "abc";

    public boolean testBool2 = false;

    private boolean testInaccessibleField = false;

    private boolean testInaccessibleAccessor = false;

    public void testMethod(boolean b) {
        testBool = b;
    }

    public void testMethod(int i) {
        testInt = i;
    }

    public void testMethod(long l) {
        testLong = l;
    }

    public void testMethod(float f) {
        testFloat = f;
    }

    public void testMethod(double d) {
        testDouble = d;
    }

    public void testMethod(char c) {
        testChar = c;
    }

    public void testMethod(String s) {
        testString = s;
    }

    private void testPrivateMethod(String s) {
        testString = s;
    }

    private boolean isTestInaccessibleAccessor() {
        return testInaccessibleAccessor;
    }

    public void setTestInaccessibleAccessor(boolean testParam) {
        testInaccessibleAccessor = testParam;
    }
}

