package org.useragent.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class DeviceUtilsTest {

  private HttpServletRequest request;

  private Device device;

  @BeforeEach
  public void setUp() {
    device = LiteDevice.from(DeviceType.MOBILE, DevicePlatform.ANDROID);
    request = new MockHttpServletRequest();
    ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
    RequestContextHolder.setRequestAttributes(requestAttributes);
  }

  @Test
  void testGetCurrentDevice_HttpServletRequest() {
    request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);

    Device currentDevice = DeviceUtils.getCurrentDevice(request);

    assertNotNull(currentDevice);
    assertEquals(device, currentDevice);
  }

  @Test
  void testGetRequiredCurrentDevice_HttpServletRequest() {
    request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);

    Device currentDevice = DeviceUtils.getRequiredCurrentDevice(request);

    assertNotNull(currentDevice);
    assertEquals(device, currentDevice);
  }

  @Test
  void testGetRequiredCurrentDevice_HttpServletRequest_NoCurrentDevice() {
    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class, () -> DeviceUtils.getRequiredCurrentDevice(request));

    assertEquals(
        "No currenet device is set in this request and one is required - have you configured a"
            + " DeviceResolvingHandlerInterceptor?",
        exception.getMessage());
  }

  @Test
  void testGetCurrentDevice_RequestAttributes() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    requestAttributes.setAttribute(
        DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device, RequestAttributes.SCOPE_REQUEST);

    Device currentDevice = DeviceUtils.getCurrentDevice(requestAttributes);

    assertNotNull(currentDevice);
    assertEquals(device, currentDevice);
  }
}
