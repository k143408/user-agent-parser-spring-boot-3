package org.useragent.parse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LiteDeviceResolverTest {

  @Test
  public void testResolveDevice_WithMobileUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
                + " like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);

    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.IOS, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_WithTabletUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (iPad; CPU OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
                + " Version/14.0 Mobile/15E148 Safari/604.1");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);

    assertEquals(DeviceType.TABLET, device.getDeviceType());
    assertEquals(DevicePlatform.IOS, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_WithNormalUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1_0) AppleWebKit/605.1.15 (KHTML, like"
                + " Gecko) Version/14.0 Safari/605.1.15");
    Mockito.when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());
    LiteDeviceResolver deviceResolver = new LiteDeviceResolver(List.of("mozilla/5.0"));
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);

    assertEquals(DeviceType.NORMAL, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testAndroidUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("x-wap-profile")).thenReturn("user1");
    Mockito.when(request.getHeader("Profile")).thenReturn("user-profile");
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (Linux; Android 9; SM-G960F Build/PPR1.180610.011; wv) AppleWebKit/537.36"
                + " (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.157 Mobile Safari/537.36");
    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);

    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.ANDROID, device.getDevicePlatform());
  }

  @Test
  public void testIphoneUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("x-wap-profile")).thenReturn("userId");
    Mockito.when(request.getHeader("Profile")).thenReturn("1234-profile");
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML,"
                + " like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);

    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.IOS, device.getDevicePlatform());
  }

  @Test
  public void testUnknownWithWapProfileAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("x-wap-profile")).thenReturn("userId");
    Mockito.when(request.getHeader("Profile")).thenReturn("1234-profile");
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (; CPU  OS 14_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)"
                + " Version/14.0 Mobile/15E148 Safari/604.1");
    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);

    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_OperaMiniUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/89.0.4389.82 Safari/537.36");
    Mockito.when(request.getHeaderNames())
        .thenReturn(Collections.enumeration(List.of("OperaMini")));

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_MobilePrefixUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "bird/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/89.0.4389.82 Safari/537.36");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_KnownMobileUserAgent() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "windows ce/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/89.0.4389.82 Safari/537.36");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_AcceptBasedHeader() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent")).thenReturn(" ");
    Mockito.when(request.getHeader("Accept")).thenReturn("wap");
    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.MOBILE, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_AndroidTablet() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "Android/5.0 (Tablet NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/89.0.4389.82 Safari/537.36");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.TABLET, device.getDeviceType());
    assertEquals(DevicePlatform.ANDROID, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_KindleFireSpecialCase() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "silk/5.0 (Tablet NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/89.0.4389.82 Safari/537.36");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.TABLET, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }

  @Test
  public void testResolveDevice_tabletUserAgentKeywords() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getHeader("User-Agent"))
        .thenReturn(
            "playbook/5.0 (Tablet NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/89.0.4389.82 Safari/537.36");

    LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
    LiteDevice device = (LiteDevice) deviceResolver.resolveDevice(request);
    assertEquals(DeviceType.TABLET, device.getDeviceType());
    assertEquals(DevicePlatform.UNKNOWN, device.getDevicePlatform());
  }
}
