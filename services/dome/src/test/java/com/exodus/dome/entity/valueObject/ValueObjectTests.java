package com.exodus.dome.entity.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectTests {

    // ==================== ResponseMessage Tests ====================

    @Test
    void responseMessage_allArgsConstructor_shouldSetMessage() {
        ResponseMessage responseMessage = new ResponseMessage("Test message");

        assertEquals("Test message", responseMessage.getMessage());
    }

    @Test
    void responseMessage_noArgsConstructor_shouldCreateEmpty() {
        ResponseMessage responseMessage = new ResponseMessage();

        assertNull(responseMessage.getMessage());
    }

    @Test
    void responseMessage_setter_shouldUpdateMessage() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Updated message");

        assertEquals("Updated message", responseMessage.getMessage());
    }

    @Test
    void responseMessage_withNullMessage_shouldAcceptNull() {
        ResponseMessage responseMessage = new ResponseMessage(null);

        assertNull(responseMessage.getMessage());
    }

    @Test
    void responseMessage_withEmptyMessage_shouldAcceptEmpty() {
        ResponseMessage responseMessage = new ResponseMessage("");

        assertEquals("", responseMessage.getMessage());
    }

    // ==================== ExceptionMessageParameter Tests ====================

    @Test
    void exceptionMessageParameter_allArgsConstructor_shouldSetFields() {
        ExceptionMessageParameter param = new ExceptionMessageParameter("key1", "value1");

        assertEquals("key1", param.getKey());
        assertEquals("value1", param.getValue());
    }

    @Test
    void exceptionMessageParameter_noArgsConstructor_shouldCreateEmpty() {
        ExceptionMessageParameter param = new ExceptionMessageParameter();

        assertNull(param.getKey());
        assertNull(param.getValue());
    }

    @Test
    void exceptionMessageParameter_setters_shouldUpdateFields() {
        ExceptionMessageParameter param = new ExceptionMessageParameter();

        param.setKey("newKey");
        param.setValue("newValue");

        assertEquals("newKey", param.getKey());
        assertEquals("newValue", param.getValue());
    }

    @Test
    void exceptionMessageParameter_withNullValues_shouldAcceptNull() {
        ExceptionMessageParameter param = new ExceptionMessageParameter(null, null);

        assertNull(param.getKey());
        assertNull(param.getValue());
    }

    @Test
    void exceptionMessageParameter_withEmptyValues_shouldAcceptEmpty() {
        ExceptionMessageParameter param = new ExceptionMessageParameter("", "");

        assertEquals("", param.getKey());
        assertEquals("", param.getValue());
    }

    @Test
    void exceptionMessageParameter_differentKeyValuePairs_shouldStoreCorrectly() {
        ExceptionMessageParameter param1 = new ExceptionMessageParameter("userId", "12345");
        ExceptionMessageParameter param2 = new ExceptionMessageParameter("email", "test@test.com");
        ExceptionMessageParameter param3 = new ExceptionMessageParameter("errorCode", "ERR_001");

        assertEquals("userId", param1.getKey());
        assertEquals("12345", param1.getValue());

        assertEquals("email", param2.getKey());
        assertEquals("test@test.com", param2.getValue());

        assertEquals("errorCode", param3.getKey());
        assertEquals("ERR_001", param3.getValue());
    }
}
