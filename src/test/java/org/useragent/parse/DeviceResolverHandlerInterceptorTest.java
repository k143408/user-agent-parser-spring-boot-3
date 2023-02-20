package org.useragent.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class DeviceResolverHandlerInterceptorTest {

  @Mock private HttpServletResponse response;

  @Mock private DeviceResolver deviceResolver;

  private HttpServletRequest request;
  private DeviceResolverHandlerInterceptor interceptor;

  @BeforeEach
  public void setUp() {
    interceptor = new DeviceResolverHandlerInterceptor(deviceResolver);
    request = new MockHttpServletRequest();
  }

  @Test
  void testPreHandle() throws Exception {
    Device device = LiteDevice.from(DeviceType.MOBILE, DevicePlatform.IOS);
    when(deviceResolver.resolveDevice(request)).thenReturn(device);

    boolean result = interceptor.preHandle(request, response, null);

    assertTrue(result);
    assertNotNull(request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE));
    assertEquals(device, request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE));
  }
}
